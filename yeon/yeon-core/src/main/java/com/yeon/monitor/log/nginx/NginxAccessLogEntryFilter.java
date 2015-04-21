package com.yeon.monitor.log.nginx;

import com.yeon.monitor.common.ffm.FilterFieldMatcher;
import com.yeon.monitor.common.ffm.MatchExpressionParser;
import com.yeon.monitor.log.ServerLogEntryFilter;
import com.yeon.util.MapModel;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class NginxAccessLogEntryFilter extends MapModel implements ServerLogEntryFilter<NginxAccessLogEntry> {
	private static final String PARAM_AGENT = "agent";
	private static final String PARAM_REFERER = "referer";
	private static final String PARAM_STATUS = "status";
	private static final String PARAM_PATH = "path";
	private static final String PARAM_METHOD = "method";
	private static final String PARAM_IP = "ip";

	private FilterFieldMatcher ipMatcher = FilterFieldMatcher.TRUE;
	private FilterFieldMatcher methodMatcher = FilterFieldMatcher.TRUE;
	private FilterFieldMatcher pathMatcher = FilterFieldMatcher.TRUE;
	private FilterFieldMatcher statusMatcher = FilterFieldMatcher.TRUE;
	private FilterFieldMatcher refererMatcher = FilterFieldMatcher.TRUE;
	private FilterFieldMatcher agentMatcher = FilterFieldMatcher.TRUE;

	public NginxAccessLogEntryFilter() {
	}

	public NginxAccessLogEntryFilter(Map<String, Object> properties) {
		super(properties);

		this.ipMatcher = MatchExpressionParser.parse(getIp(), MatchExpressionParser.MatcherType.STARTS);
		this.methodMatcher = MatchExpressionParser.parse(getMethod(), MatchExpressionParser.MatcherType.EQUALS);
		this.pathMatcher = MatchExpressionParser.parse(getPath());
		this.statusMatcher = MatchExpressionParser.parse(getStatus(), MatchExpressionParser.MatcherType.STARTS);
		this.refererMatcher = MatchExpressionParser.parse(getReferer());
		this.agentMatcher = MatchExpressionParser.parse(getAgent());
	}

	public boolean isEmptyFilter() {
		return StringUtils.isBlank(getIp())
			&& StringUtils.isBlank(getMethod())
			&& StringUtils.isBlank(getPath())
			&& StringUtils.isBlank(getStatus())
			&& StringUtils.isBlank(getReferer())
			&& StringUtils.isBlank(getAgent());
	}

	public String getIp() {
		return getString(PARAM_IP);
	}

	public void setIp(String ip) {
		this.ipMatcher = MatchExpressionParser.parse(ip, MatchExpressionParser.MatcherType.STARTS);
		setString(PARAM_IP, ip);
	}

	public String getMethod() {
		return getString(PARAM_METHOD);
	}

	public void setMethod(String method) {
		this.methodMatcher = MatchExpressionParser.parse(method);
		setString(PARAM_METHOD, method);
	}

	public String getPath() {
		return getString(PARAM_PATH);
	}

	public void setPath(String path) {
		this.pathMatcher = MatchExpressionParser.parse(path);
		setString(PARAM_PATH, path);
	}

	public String getStatus() {
		return getString(PARAM_STATUS);
	}

	public void setStatus(String status) {
		this.statusMatcher = MatchExpressionParser.parse(status, MatchExpressionParser.MatcherType.STARTS);
		setString(PARAM_STATUS, status);
	}

	public String getReferer() {
		return getString(PARAM_REFERER);
	}

	public void setReferer(String referer) {
		this.refererMatcher = MatchExpressionParser.parse(referer);
		setString(PARAM_REFERER, referer);
	}

	public String getAgent() {
		return getString(PARAM_AGENT);
	}

	public void setAgent(String agent) {
		this.agentMatcher = MatchExpressionParser.parse(agent);
		setString(PARAM_AGENT, agent);
	}

	@Override
	public boolean accept(NginxAccessLogEntry logEntry) {
		if (logEntry == null) {
			return false;
		}

		return ipMatcher.match(logEntry.getIp())
			&& methodMatcher.match(logEntry.getMethod())
			&& pathMatcher.match(logEntry.getPath())
			&& statusMatcher.match(logEntry.getStatus())
			&& refererMatcher.match(logEntry.getReferer())
			&& agentMatcher.match(logEntry.getAgent());
	}
}
