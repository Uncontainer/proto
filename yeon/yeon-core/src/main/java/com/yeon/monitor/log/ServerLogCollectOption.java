package com.yeon.monitor.log;

import com.yeon.util.MapModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerLogCollectOption extends MapModel {
	public static final String PARAM_CALLER_IP = "caller_ip";
	private static final String PARAM_END_DATE = "end_date";
	private static final String PARAM_START_DATE = "start_date";
	private static final String PARAM_TIMEOUT = "timeout";
	private static final String PARAM_TODAY = "today";
	private static final String PARAM_SCAN_TYPE = "scan_type";
	private static final String PARAM_FILE_TYPE = "file_type";
	private static final String PARAM_FILTER_OPTION = "filter_option";
	private static final String PARAM_FILE_PATH = "file_path";
	private static final String PARAM_MAX_SCAN_SIZE = "max_scan_size";
	private static final String PARAM_MAX_CPU_USAGE = "max_cpu_usage";

	public static final int MAX_SCAN_SIZE = 1024 * 1024;
	private volatile int timeout = Integer.MIN_VALUE;

	public String getCallerIp() {
		return getString(PARAM_CALLER_IP);
	}

	public void setCallerIp(String ip) {
		setString(PARAM_CALLER_IP, ip);
	}

	public FileType getFileType() {
		return getEnum(PARAM_FILE_TYPE, null, FileType.class);
	}

	public void setFileType(FileType serverType) {
		setEnum(PARAM_FILE_TYPE, serverType);
	}

	public void setFilePath(String filePath) {
		setString(PARAM_FILE_PATH, filePath);
	}

	public String getFilePath() {
		return getString(PARAM_FILE_PATH);
	}

	public int getMaxScanSize() {
		int scanSize = getInteger(PARAM_MAX_SCAN_SIZE, 1024 * 100);
		if (scanSize > MAX_SCAN_SIZE) {
			return MAX_SCAN_SIZE;
		}

		return scanSize;
	}

	public void setMaxScanSize(int scanSize) {
		setInteger(PARAM_MAX_SCAN_SIZE, scanSize);
	}

	public Map<String, Object> getFilterOption() {
		return getMap(PARAM_FILTER_OPTION);
	}

	public void setFilterOption(Map<String, Object> filterOption) {
		setMap(PARAM_FILTER_OPTION, filterOption);
	}

	public ServerLogCollectOption() {
		setDate(PARAM_TODAY, clearTime(new Date()));
	}

	public ServerLogCollectOption(Map<String, Object> properties) {
		super(properties);
	}

	public ScanType getScanType() {
		return getEnum(PARAM_SCAN_TYPE, null, ScanType.class);
	}

	public void setScanType(ScanType scanType) {
		setEnum(PARAM_SCAN_TYPE, scanType);
	}

	public Date getStartDate() {
		return getDate(PARAM_START_DATE);
	}

	public void setStartDate(Date startDate) {
		setDate(PARAM_START_DATE, startDate);
	}

	public Date getEndDate() {
		return getDate(PARAM_END_DATE);
	}

	public void setEndDate(Date endDate) {
		setDate(PARAM_END_DATE, endDate);
	}

	public int getTimeout() {
		if (timeout == Integer.MIN_VALUE) {
			timeout = getInteger(PARAM_TIMEOUT, 10000);
		}

		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
		setInteger(PARAM_TIMEOUT, timeout);
	}

	public double getMaxCpuUsage() {
		return getDouble(PARAM_MAX_CPU_USAGE, 60.0);
	}

	public void setMaxCpuUsage(double maxCpuUsage) {
		setDouble(PARAM_MAX_CPU_USAGE, maxCpuUsage);
	}

	public void validate() {
		if (getScanType() == null) {
			throw new IllegalArgumentException("Null scan-type.");
		}

		if (getScanType() != ScanType.TAIL) {
			Date startDate = getStartDate();
			if (startDate == null) {
				throw new IllegalArgumentException("Null start date.");
			}

			Date endDate = getEndDate();
			if (endDate == null) {
				throw new IllegalArgumentException("Null end date.");
			}

			Date tomorrow = clearTime(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
			if (endDate.after(tomorrow)) {
				throw new IllegalArgumentException("End date is after tomorrow.");
			}

			if (startDate.after(endDate)) {
				throw new IllegalArgumentException("Start date must be earlier than end date.");
			}
		}

		if (getFilePath() == null) {
			throw new IllegalArgumentException("Null file-path.");
		}

		if (getFileType() == null) {
			throw new IllegalArgumentException("Null file-type.");
		}

		if (getMaxScanSize() < 0) {
			throw new IllegalArgumentException("Negative max-scan-size.");
		}
	}

	public Date getStartDay() {
		return clearTime(getStartDate());
	}

	public Date getEndDay() {
		return clearTime(getEndDate());
	}

	public Date getToday() {
		Date today = getDate(PARAM_TODAY);
		if (today == null) {
			today = clearTime(new Date());
			setDate(PARAM_TODAY, today);
		}

		return today;
	}

	protected static Date clearTime(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 
	 * @author pulsarang
	 */
	public enum FileType {
		TOMCAT("T"),
		NGINX_ACCESS("N");

		final String code;

		FileType(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	public enum ScanType {
		TAIL,
		RANGE;
	}
}
