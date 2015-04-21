package com.yeon.mom.rabbitmq;

import com.yeon.YeonContext;
import com.yeon.mom.util.TicketUtils;
import com.yeon.monitor.resource.annotation.MonitoringBean;
import com.yeon.monitor.resource.annotation.MonitoringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EC셀러서비스개발팀
 */
@MonitoringBean
public class MomMBean {
	private final RabbitMqContext rabbitMqContext;

	public MomMBean(RabbitMqContext rabbitMqContext) {
		super();
		this.rabbitMqContext = rabbitMqContext;
	}

	@MonitoringProperty(name = "summary")
	public Map<String, Object> getSummary() {
		Map<String, Object> summary = new HashMap<String, Object>();
		summary.put("version", getVersion());
		summary.put("status", getStatus());
		summary.put("momSubscribeEnabled", getMomSubscribeEnabled());
		summary.put("projectSubscribeEnabled", getProjectSubscribeEnabled());
		summary.put("serverSubscribeEnabled", getServerSubscribeEnabled());

		return summary;
	}

	@MonitoringProperty(name = "version")
	public String getVersion() {
		return YeonContext.getConfiguration().getVersion();
	}

	@MonitoringProperty(name = "status")
	public String getStatus() {
		return rabbitMqContext.getStatus().name();
	}

	@MonitoringProperty(name = "momSubscribeEnabled")
	public boolean getMomSubscribeEnabled() {
		return TicketUtils.getMomInfo().getMomStatus().isProcessEnabled();
	}

	@MonitoringProperty(name = "projectSubscribeEnabled")
	public boolean getProjectSubscribeEnabled() {
		return TicketUtils.getProjectInfo().isSubscribeEnabled();
	}

	@MonitoringProperty(name = "serverSubscribeEnabled")
	public boolean getServerSubscribeEnabled() {
		return TicketUtils.getServer().isNormal();
	}
}