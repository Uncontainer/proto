package com.yeon.mom.rabbitmq.consumer;

import com.rabbitmq.client.*;
import com.yeon.YeonContext;
import com.yeon.api.ApiServiceFactory;
import com.yeon.mom.DelayProcessException;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.MomConstants;
import com.yeon.mom.ReprocessableException;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventProcessOperation;
import com.yeon.mom.event.EventProcessOption;
import com.yeon.mom.rabbitmq.RabbitMqContext;
import com.yeon.mom.rabbitmq.RabbitMqUtils;
import com.yeon.mom.service.ServiceWorkerStatus;
import com.yeon.mom.util.EventUtils;
import com.yeon.monitor.alarm.AlarmLog;
import com.yeon.monitor.alarm.AlarmLogType;
import com.yeon.util.ExceptionUtils;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author pulsarang
 */
public class RabbitMqMessageConsumer implements Consumer, MessageConsumer {
	private static AtomicInteger seed = new AtomicInteger();

	private final Logger log = LoggerFactory.getLogger(RabbitMqMessageConsumer.class);

	EventProcessorExecutionInfo executionInfo;

	Connection connection;
	Channel channel;

	private final int id;
	private ServiceWorkerStatus status = ServiceWorkerStatus.INIT_WAIT;

	private volatile long idleSinceTime = System.currentTimeMillis();
	private final AtomicInteger receiveMessageCount = new AtomicInteger();

	public RabbitMqMessageConsumer(EventProcessorExecutionInfo executionInfo) {
		super();
		id = seed.incrementAndGet();

		this.executionInfo = executionInfo;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return executionInfo.getCanonicalName() + "#" + id;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		idleSinceTime = System.currentTimeMillis();
		receiveMessageCount.incrementAndGet();

		boolean closed;
		synchronized (this) {
			closed = status.isClose();
			if (!closed) {
				setStatus(ServiceWorkerStatus.PROCESSING);
			}
		}

		if (closed) {
			channel.basicNack(envelope.getDeliveryTag(), false, true);
			return;
		}

		try {
			executionInfo.getMaxTpsChecker().ensureIntervalAndTouch();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

			channel.basicNack(envelope.getDeliveryTag(), false, true);
			return;
		}

		Event event = null;
		BasicEventProcessContext context = null;
		try {
			event = parseEvent(body);

			context = new BasicEventProcessContext(event, executionInfo, envelope.isRedeliver());
			EventProcessOption option = event.getEventProcessOption();

			if (option != null) {
				ensureRetryInterval(option.getLastTryTime());
			}

			try {
				execute(context, option);
			} catch (Throwable t) {
				if (t instanceof DelayProcessException) {
					try {
						handleDelayProcessException((DelayProcessException) t);
					} catch (Exception e) {
						log.error("[YEON] Fail to handle DelayProcessException.(" + t + ")", e);
					}
				}

				if (executionInfo.getEventProcessorInfo().isRetryOnAllException() || (t instanceof ReprocessableException)) {
					if (context.getEventProcessOperation() != EventProcessOperation.FAIL) {
						handleReprocessableException(t, event, option);
					}
				}

				// TODO 제거해도 괜찮을 것 같음...
				// if (executionInfo.getExceptionCountChecker().touch()) {
				// try {
				// Thread.sleep(MomConstants.SLEEP_TIME_ON_FREQUENT_EVENT_PROCESS_FAIL);
				// } catch (InterruptedException e) {
				// Thread.currentThread().interrupt();
				// log.info("[YEON] Event processor interrupted.({})", executionInfo.getCanonicalName());
				// }
				// }

				ExceptionUtils.launderThrowable(t);
			}
		} catch (Throwable t) {
			// 예외가 그대로 던져질 경우 기본적으로 동작하는 ExceptionHandler에서는 channel을 닫는다.
			if (t instanceof SkipProcessException) {
				log.debug("[YEON] Skip process event.({})\n{}", event, t.getMessage());
			} else if (t instanceof ReprocessableException && context != null && context.getEventProcessOperation() != EventProcessOperation.FAIL) {
				log.info("[YEON] Fail to process event.(" + event + ")", t);
			} else {
				log.error("[YEON] Fail to process event.(" + event + ")", t);
			}

			ensureAlive();
		} finally {
			try {
				channel.basicAck(envelope.getDeliveryTag(), false);
			} finally {
				synchronized (this) {
					if (status.isClose()) {
						close();
					} else {
						setStatus(ServiceWorkerStatus.MSG_WAIT);
					}
				}
			}
		}
	}

	@Override
	public long getIdleSinceTime() {
		return idleSinceTime;
	}

	@Override
	public int getReceiveCount() {
		return receiveMessageCount.get();
	}

	private void ensureAlive() {
		if (status != ServiceWorkerStatus.MSG_WAIT) {
			return;
		}

		long idleTime = System.currentTimeMillis() - idleSinceTime;
		if (idleTime < MomConstants.MAX_RABBIT_MQ_CONSUMER_IDLE_TIME) {
			return;
		}

		try {
			channel.basicQos(0, 1, false);
		} catch (IOException e) {
			try {
				close();
			} finally {
				setStatus(ServiceWorkerStatus.UNEXPECTED_STOP);
			}
		}
	}

	private Event parseEvent(byte[] body) {
		Event rawEvent;
		String message = null;
		try {
			message = new String(body, MomConstants.MESSAGE_CHARSET);
			rawEvent = MapModel.fromJson(message, Event.class);
		} catch (Exception e) {
			RuntimeException re = new RuntimeException("Invalid message.(" + message + ")", e);
			ApiServiceFactory.getMonitorApiService().logAlarm(new AlarmLog(AlarmLogType.ALT_DECODE_EVENT_FAIL, re, getName()));
			throw re;
		}

		return EventUtils.convertEvent(rawEvent);
	}

	private void execute(BasicEventProcessContext context, EventProcessOption option) {
		EventProcessOperation op = context.getEventProcessOperation();
		EventProcessor eventProcessor = executionInfo.getEventProcessor();
		switch (op) {
		case PROCESS:
			eventProcessor.process(context);
			break;
		case REPROCESS:
			eventProcessor.reprocess(context);
			break;
		case VERIFY:
			eventProcessor.verify(context);
			break;
		case FAIL:
			eventProcessor.fail(context);
			break;
		default:
			throw new IllegalArgumentException("Unsupported operation.(" + op + ")");

		}
	}

	private void ensureRetryInterval(Date lastTryTime) {
		if (lastTryTime == null) {
			return;
		}

		int sleepTime = executionInfo.getEventProcessorInfo().getMinTryInterval() - (int) (System.currentTimeMillis() - lastTryTime.getTime());
		if (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new ReprocessableException("Interrupted while ensure retry interval.", e);
			}
		}
	}

	private void handleDelayProcessException(DelayProcessException exception) throws InterruptedException {
		long sleepTime = exception.getEndDate().getTime() - System.currentTimeMillis();
		if (sleepTime < TimeUnit.SECONDS.toMillis(10)) {
			Thread.sleep(sleepTime);
		} else {
			// TODO [LOW] mom-admin 서버가 죽었을 경우는...
			ApiServiceFactory.getEventApiService().delayProcess(executionInfo.getCanonicalName(), exception.getStartDate(), exception.getEndDate());
		}
	}

	private void handleReprocessableException(Throwable throwable, Event event, EventProcessOption option) {
		if (option == null) {
			option = new EventProcessOption(executionInfo.getCanonicalName());
		}
		option.setOperation(EventProcessOperation.REPROCESS);
		option.increaseTryCount();
		if (throwable instanceof ReprocessableException) {
			option.setArguments(((ReprocessableException) throwable).getArguments());
		}
		option.setLastTryTime(new Date());

		event.setEventProcessOption(option);

		YeonContext.getMomContext().getEventProducer().retry(event);
	}

	@Override
	public boolean start() {
		log.debug("[YEON] Starting rabbitmq message consumer...({}#{})", executionInfo.getSimpleName(), id);
		synchronized (this) {
			if (status.isClose()) {
				throw new IllegalStateException("MessageConsumer has already closed.");
			}

			if (status != ServiceWorkerStatus.INIT_WAIT) {
				throw new IllegalStateException("MessageConsumer has already started.");
			}

			setStatus(ServiceWorkerStatus.INITIALIZING);
		}

		RabbitMqContext rabbitMqContext = (RabbitMqContext) YeonContext.getMomContext();
		// if (executionInfo.getMqConnectFailCountChecker().isViolated()) {
		// log.error("[YEON] Fail to connect RabbitMq Server.({} {}#{})",
		// new Object[] {rabbitMqContext.getConnectionFactory().getAddressString(),
		// executionInfo.getSimpleName(), id});
		//
		// setStatus(MessageConsumerStatus.INIT_FAIL);
		//
		// return false;
		// }

		try {
			connection = rabbitMqContext.getConnectionFactory().createConnection();
			channel = connection.createChannel();
			channel.basicQos(0, 1, false);

			channel.basicConsume(executionInfo.getCanonicalName(), false, this);
			setStatus(ServiceWorkerStatus.MSG_WAIT);

			log.debug("[YEON] Start rabbitmq message consumer successfully.({}#{})", executionInfo.getSimpleName(), id);

			return true;
		} catch (IOException e) {
			String message = String.format("[YEON] Start rabbitmq message consumer fail.(%s %s#%s)", rabbitMqContext.getConnectionFactory().getAddressString(), executionInfo.getSimpleName(), id);
			log.error("[YEON] " + message, e);

			executionInfo.getMqConnectFailCountChecker().touch();
			close();
			setStatus(ServiceWorkerStatus.INIT_FAIL);

			AlarmLog alarmLog = new AlarmLog(AlarmLogType.ALT_PROCESSOR_MQ_CONNECT_FAIL, message, e, getName());
			ApiServiceFactory.getMonitorApiService().logAlarm(alarmLog);

			return false;
		}
	}

	@Override
	public void stop() {
		boolean closeNow;
		synchronized (this) {
			if (status.isClose()) {
				return;
			}

			closeNow = (status != ServiceWorkerStatus.PROCESSING);
			setStatus(ServiceWorkerStatus.STOP_WAIT);
		}

		log.debug("[YEON] Close MessageConsumer.({})", executionInfo.getCanonicalName());

		if (closeNow) {
			close();
		}
	}

	private void close() {
		if (channel != null) {
			try {
				RabbitMqUtils.closeQuietly(connection);
			} finally {
				channel = null;
				connection = null;
			}
		}

		setStatus(ServiceWorkerStatus.STOP);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
	}

	@Override
	public void handleCancelOk(String consumerTag) {
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
	}

	@Override
	public void handleRecoverOk() {
	}

	private synchronized void setStatus(ServiceWorkerStatus status) {
		this.status = status;
	}

	@Override
	public synchronized ServiceWorkerStatus getStatus(boolean checkAlive) {
		if (checkAlive) {
			ensureAlive();
		}

		return status;
	}
}
