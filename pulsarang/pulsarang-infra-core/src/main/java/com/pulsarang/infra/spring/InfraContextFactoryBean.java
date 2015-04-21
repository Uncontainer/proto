package com.pulsarang.infra.spring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.pulsarang.infra.BasicInfraConfiguration;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.mom.EventProcessor;
import com.pulsarang.infra.mom.MomContext;
import com.pulsarang.infra.mom.ResqueWorker;
import com.pulsarang.infra.mom.event.EventType;
import com.pulsarang.infra.mom.sxb.SxbMomContext;
import com.pulsarang.infra.util.NameUtils;

public class InfraContextFactoryBean implements FactoryBean<InfraContext>, InitializingBean, DisposableBean, ApplicationListener<ApplicationEvent> {
	private final Logger log = LoggerFactory.getLogger(InfraContextFactoryBean.class);

	@Autowired
	ApplicationContext applicationContext;

	static boolean contextRefreshedEventReceived = false;

	private boolean scanEventProcessor = true;

	private String infraAdminAddress;
	private String solutionName;
	private String projectName;

	public void setInfraAdminAddress(String infraAdminAddress) {
		this.infraAdminAddress = infraAdminAddress;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public InfraContext getObject() throws Exception {
		return InfraContextFactory.getInfraContext();
	}

	@Override
	public Class<?> getObjectType() {
		return InfraContext.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		BasicInfraConfiguration infraConfiguration = new BasicInfraConfiguration();
		infraConfiguration.setInfraAdminAddress(infraAdminAddress);
		infraConfiguration.setSolutionName(solutionName);
		infraConfiguration.setProjectName(projectName);

		InfraContextFactory.init(infraConfiguration);
	}

	@Override
	public void destroy() throws Exception {
		InfraContextFactory.destroy();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (!scanEventProcessor) {
			return;
		}

		if (event instanceof ContextRefreshedEvent) {
			synchronized (InfraContextFactoryBean.class) {
				if (contextRefreshedEventReceived) {
					throw new IllegalStateException("ContextRefreshedEvent has already received.");
				}

				contextRefreshedEventReceived = true;
			}

			try {
				registEventProcessors();
			} catch (Throwable t) {
				log.error("Fail to regist eventProcessor", t);
			}

			try {
				registResqueWorkers();
			} catch (Throwable t) {
				log.error("Fail to regist resqueWorker", t);
			}

		}
	}

	void registEventProcessors() {
		Map<String, EventProcessor> eventProcessors = applicationContext.getBeansOfType(EventProcessor.class);
		if (MapUtils.isEmpty(eventProcessors)) {
			return;
		}

		validateEventProcessors(eventProcessors);

		MomContext momContext = InfraContextFactory.getInfraContext().getMomContext();
		for (EventProcessor eventProcessor : eventProcessors.values()) {
			momContext.addEventProcessor(eventProcessor);
		}
	}

	void validateEventProcessors(Map<String, EventProcessor> eventProcessors) {
		Map<String, Set<String>> subscribeEventsMap = new HashMap<String, Set<String>>();
		for (EventProcessor eventProcessor : eventProcessors.values()) {
			String name = eventProcessor.getName();
			if (NameUtils.isInvalidJavaIdentifier(name)) {
				throw new IllegalArgumentException("EventProcessorName must be a java identifier.(" + eventProcessor.getClass().getCanonicalName()
						+ ")");
			}

			Set<String> subscribeEvents = subscribeEventsMap.get(name);
			if (subscribeEvents == null) {
				subscribeEvents = new HashSet<String>();
				subscribeEventsMap.put(name, subscribeEvents);
			}

			for (EventType eventType : eventProcessor.getSubscribeEventTypes()) {
				String eventCode = eventType.getCode();
				if (subscribeEvents.contains(eventCode)) {
					throw new IllegalArgumentException("EventProcessor '" + name + "' subscribes duplicated event.(" + eventCode + ")");
				}
				subscribeEvents.add(eventCode);
			}
		}
		subscribeEventsMap = null;
	}

	void registResqueWorkers() {
		Map<String, ResqueWorker> workers = applicationContext.getBeansOfType(ResqueWorker.class);
		if (MapUtils.isEmpty(workers)) {
			return;
		}

		SxbMomContext momContext = (SxbMomContext) InfraContextFactory.getInfraContext().getMomContext();
		for (ResqueWorker worker : workers.values()) {
			momContext.registerResqueWorker(worker);
		}
	}
}
