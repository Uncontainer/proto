package com.yeon.monitor.log.tomcat;

import ch.qos.logback.classic.Level;
import com.yeon.monitor.common.MomString;
import com.yeon.monitor.log.ServerLogEntry;

/**
 * log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} [%t] [%-5p] [%c:%M:%L] - %m%n
 *  
 * @author pulsarang
 */
public class TomcatLogEntry extends ServerLogEntry {
	protected MomString threadName = new MomString();
	protected int logLevel = SEVERITY_UNDEFINED;
	protected MomString path = new MomString();
	protected MomString message = new MomString();

	public TomcatLogEntry() {
	}

	public TomcatLogEntry(MomString log) {
		init(log);
	}

	public void init(MomString log) {
		content.init(log.getBuffer(), log.getOffset(), log.getLength());

		int setCount;
		if (log.isEmpty()) {
			setCount = 0;
		} else {
			setCount = parseLog(log);
		}

		switch (setCount) {
			case 0:
				createTime = -1;
			case 1:
				threadName.reset();
			case 2:
				logLevel = -1;
			case 3:
				path.reset();
			case 4:
				message.reset();
		}
	}

	@Override
	public int getSeverity() {
		return logLevel;
	}

	public MomString getThreadName() {
		return threadName;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public MomString getPath() {
		return path;
	}

	public MomString getMessage() {
		return message;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1000);
		sb.append("thread=").append(threadName).append('\n');
		sb.append("logLevel=").append(logLevel).append('\n');
		sb.append("path=").append(path).append('\n');
		sb.append("message=\n").append(message).append('\n');

		return sb.toString();
	}

	private int parseLog(MomString log) {
		char[] charArray = log.getBuffer();
		int startIndex = log.getOffset();
		int endIndex = startIndex + 19;
		super.createTime = TomcatLogDateParser.parseDate(charArray, startIndex);
		if (super.createTime == -1) {
			return 0;
		}

		startIndex = endIndex + 2;
		if ((endIndex = nextMatch(charArray, startIndex + 1, ']')) == -1) {
			return 1;
		}
		this.threadName.init(charArray, startIndex, endIndex - startIndex);

		startIndex = endIndex + 3;
		if ((endIndex = nextMatch(charArray, startIndex + 1, ']')) == -1) {
			return 2;
		}
		this.logLevel = toLogLevelInt(charArray, startIndex);

		startIndex = endIndex + 3;
		if ((endIndex = nextMatch(charArray, startIndex + 1, ']')) == -1) {
			return 3;
		}
		this.path.init(charArray, startIndex, endIndex - startIndex);

		startIndex = endIndex + 4;
		if (startIndex >= charArray.length) {
			return 4;
		}
		this.message.init(charArray, startIndex, log.getOffset() + log.length() - startIndex);

		return 5;
	}

	private static int toLogLevelInt(char[] charArray, int offset) {
		switch (charArray[offset]) {
			case 'T':
				return Level.TRACE.levelInt;
			case 'D':
				return Level.DEBUG.levelInt;
			case 'I':
				return Level.INFO.levelInt;
			case 'W':
				return Level.WARN.levelInt;
			case 'E':
				return Level.ERROR.levelInt;
			default:
				return -1;
		}
	}
}
