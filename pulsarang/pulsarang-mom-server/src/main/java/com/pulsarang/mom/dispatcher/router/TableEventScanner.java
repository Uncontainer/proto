package com.pulsarang.mom.dispatcher.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.pulsarang.mom.dispatcher.processor.ProcessorEventQueue;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.db.h2.H2EventStore;
import com.pulsarang.mom.store.proc.AbstractEventStoreScanner;
import com.pulsarang.mom.store.proc.EventConsumer;

/**
 * {@link TableEventScanner} 에서는 이상적으로 size를 0으로 유지하게 만든다. <br/>
 * 여기에서는 failover의 역할만을 수행하여, 실제적인 queueing은 {@link ProcessorEventQueue} 단에서 수행한다.
 * 
 * @author pulsarang
 * 
 */

@Component(TableEventScanner.NAME)
@Scope("prototype")
public class TableEventScanner extends AbstractEventStoreScanner {
	public static final String NAME = "tableEventScanner";

	@Autowired
	private ApplicationContext applicationContext;

	private EventRouterExecutorSelector executorSelector;

	public TableEventScanner(int tableId) {
		// TODO 이름 규칙 수정.
		super("table-" + tableId, new H2EventStore(Integer.toString(tableId)), 10);
	}

	public void init() {
		executorSelector = applicationContext.getBean(EventRouterExecutorSelector.class);
		executorSelector.init();

		super.start();
	}

	public void destory() {
		super.stop();
	}

	@Override
	protected EventConsumer createEventConsumer(EventEnvelope eventEnvelope) {
		return executorSelector.select(eventEnvelope.getEvent());
	}

}
