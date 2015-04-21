package com.yeon.monitor.log;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.log.ServerLogCollectOption.ScanType;
import com.yeon.monitor.log.nginx.NginxAccessLogEntry;
import com.yeon.monitor.log.nginx.NginxAccessLogEntryFilter;
import com.yeon.monitor.log.nginx.NginxAccessLogEntryScanner;
import com.yeon.monitor.log.tomcat.TomcatLogEntry;
import com.yeon.monitor.log.tomcat.TomcatLogEntryFilter;
import com.yeon.monitor.log.tomcat.TomcatLogEntryScanner;
import com.yeon.monitor.resource.cpu.CpuListener;
import com.yeon.monitor.resource.cpu.CpuMonitor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class ServerLogCollector implements CpuListener {
	private static AtomicInteger seed = new AtomicInteger();

	private final MomString buffer;
	private final ServerLogCollectOption option;

	private ServerLogEntryScanner<? extends ServerLogEntry> entryScanner;
	private ServerLogLineScanner lineScanner;

	private final int id;
	private long startTime;
	private long endTime;
	private int returnCode;

	private final int timeout;
	private String stoperEmpNo;
	private double cpuUsage;

	public ServerLogCollector(MomString buffer, ServerLogCollectOption option) {
		if (buffer == null || option == null) {
			throw new IllegalArgumentException();
		}

		this.id = seed.incrementAndGet();
		this.buffer = buffer;
		this.option = option;
		this.timeout = option.getTimeout();
	}

	public int getId() {
		return id;
	}

	public ServerLogCollectStatus getStatus() {
		ServerLogCollectStatus status = new ServerLogCollectStatus();
		status.setId(id);
		status.setCallerIp(option.getCallerIp());
		status.setStartTime(startTime);
		status.setCpuUsage(CpuMonitor.getInstance().getUsePercentage());
		status.setScanFileSize(entryScanner.getScanFileSize());
		status.setScanLineCount(entryScanner.getScanLineCount());
		status.setScanLogCount(entryScanner.getScanLogCount());
		if (entryScanner instanceof FilteredServerLogScanner) {
			status.setMatchLogCount(((FilteredServerLogScanner<? extends ServerLogEntry>) entryScanner).getMatchCount());
		} else {
			status.setMatchLogCount(entryScanner.getScanLogCount());
		}

		return status;
	}

	public ServerLog collect() throws IOException {
		ServerLog serverLog;
		startTime = System.currentTimeMillis();
		try {
			CpuMonitor.getInstance().addListener(option.getMaxCpuUsage(), this);
			entryScanner = scanServerLog(buffer, option);
			entryScanner.clearReady();

			serverLog = new ServerLog();
			serverLog.setContent(buffer.toString());
			serverLog.setSuccess(true);
		} catch (Exception e) {
			serverLog = ServerLog.createFail(e.getClass().getSimpleName() + " " + e.getMessage());
		} finally {
			endTime = System.currentTimeMillis();
			CpuMonitor.getInstance().removeListener(this);
		}

		serverLog.setScanSpentTime(endTime - startTime);
		serverLog.setScanFileSize(entryScanner.getScanFileSize());
		serverLog.setScanLineCount(entryScanner.getScanLineCount());
		serverLog.setScanLogCount(entryScanner.getScanLogCount());

		switch (returnCode) {
		case ServerLogCollectStatus.BUFFER_OVERFLOW:
			serverLog.setMessage(String.format("Result buffer overflow.(%d자)", option.getMaxScanSize()));
			break;
		case ServerLogCollectStatus.TIMEOUT:
			serverLog.setMessage(String.format("Scan timeout.(%dms)", timeout));
			break;
		case ServerLogCollectStatus.STOP:
			serverLog.setMessage(String.format("Stop by user.(%s)", stoperEmpNo));
			break;
		case ServerLogCollectStatus.SERVER_TOO_BUSY:
			serverLog.setMessage(String.format("Server too busy.(cpu=%.2f%%)", cpuUsage));
			break;
		}

		if (entryScanner instanceof FilteredServerLogScanner) {
			serverLog.setMatchLogCount(((FilteredServerLogScanner<? extends ServerLogEntry>) entryScanner).getMatchCount());
		} else {
			serverLog.setMatchLogCount(entryScanner.getScanLogCount());
		}

		entryScanner.close();

		return serverLog;
	}

	@Override
	public void cpuAlarm(double value) {
		cpuUsage = value;
		stop(ServerLogCollectStatus.SERVER_TOO_BUSY);
	}

	public void stop(Map<String, Object> optionMap) {
		if (optionMap != null) {
			stoperEmpNo = (String) optionMap.get(ServerLogCollectStatus.PARAM_EMPLOYEE_ID);
		}
		stop(ServerLogCollectStatus.STOP);
	}

	private void stop(int reason) {
		option.setTimeout(reason);
		int maxSleepCount = 30;
		while (maxSleepCount > 0 && endTime == 0L) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {
			}
		}
	}

	private ServerLogEntryScanner<? extends ServerLogEntry> scanServerLog(MomString buffer, ServerLogCollectOption option) throws IOException {
		if (option.getScanType() == ScanType.TAIL) {
			ServerLogTailer serverLogTailer = new ServerLogTailer(buffer, option);
			entryScanner = serverLogTailer;
			ServerLogEntry logEntry = new ServerLogEntry();
			serverLogTailer.next(logEntry);

			return serverLogTailer;
		}

		Map<String, Object> filterOption = option.getFilterOption();
		if (filterOption != null && filterOption.isEmpty()) {
			filterOption = null;
		}

		switch (option.getFileType()) {
		case TOMCAT:
			return scanTomcat(buffer, option, filterOption);
		case NGINX_ACCESS:
			return scanNginxAccess(buffer, option, filterOption);
		default:
			throw new IllegalArgumentException("Unsupported file type.(" + option.getFileType() + ")");
		}
	}

	private ServerLogEntryScanner<TomcatLogEntry> scanTomcat(MomString buffer, ServerLogCollectOption option, Map<String, Object> filterOption) throws IOException {
		ServerLogEntryScanner<TomcatLogEntry> tomcatLogScanner;

		lineScanner = new FileServerLogLineScanner(option);
		tomcatLogScanner = new TomcatLogEntryScanner(buffer, lineScanner, option.getStartDate(), option.getEndDate());

		if (filterOption != null) {
			TomcatLogEntryFilter tomcatLogFilter = new TomcatLogEntryFilter(filterOption);
			if (!tomcatLogFilter.isEmptyFilter()) {
				tomcatLogScanner = new FilteredServerLogScanner<TomcatLogEntry>(tomcatLogScanner, tomcatLogFilter);
			}
		}
		entryScanner = tomcatLogScanner;

		int maxScanSize = option.getMaxScanSize();
		TomcatLogEntry tomcatLogEntry = new TomcatLogEntry();
		while ((returnCode = tomcatLogScanner.next(tomcatLogEntry)) >= 0) {
			if (buffer.length() >= maxScanSize) {
				if (returnCode != ServerLogCollectStatus.EOF) {
					returnCode = ServerLogCollectStatus.BUFFER_OVERFLOW;
				}
				break;
			}
		}

		return tomcatLogScanner;
	}

	private ServerLogEntryScanner<NginxAccessLogEntry> scanNginxAccess(MomString buffer, ServerLogCollectOption option, Map<String, Object> filterOption) throws IOException {
		ServerLogEntryScanner<NginxAccessLogEntry> nginxAccessLogScanner;

		lineScanner = new FileServerLogLineScanner(option);
		nginxAccessLogScanner = new NginxAccessLogEntryScanner(buffer, lineScanner, option.getStartDate(), option.getEndDate());

		if (filterOption != null) {
			NginxAccessLogEntryFilter nginxAccessLogFilter = new NginxAccessLogEntryFilter(filterOption);
			if (!nginxAccessLogFilter.isEmptyFilter()) {
				nginxAccessLogScanner = new FilteredServerLogScanner<NginxAccessLogEntry>(nginxAccessLogScanner, nginxAccessLogFilter);
			}
		}
		entryScanner = nginxAccessLogScanner;

		int maxScanSize = option.getMaxScanSize();
		NginxAccessLogEntry logEntry = new NginxAccessLogEntry();
		while ((returnCode = nginxAccessLogScanner.next(logEntry)) >= 0) {
			if (buffer.length() >= maxScanSize) {
				if (returnCode != ServerLogCollectStatus.EOF) {
					returnCode = ServerLogCollectStatus.BUFFER_OVERFLOW;
				}
				break;
			}
		}

		return nginxAccessLogScanner;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ServerLogCollector)) {
			return false;
		}

		return id == ((ServerLogCollector) obj).id;
	}
}
