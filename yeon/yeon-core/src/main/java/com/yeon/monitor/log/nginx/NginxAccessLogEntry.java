package com.yeon.monitor.log.nginx;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.log.ServerLogEntry;

import java.util.Date;

public class NginxAccessLogEntry extends ServerLogEntry {
	private final MomString ip = new MomString();
	private final MomString user = new MomString();
	private final MomString method = new MomString();
	private final MomString path = new MomString();
	private final MomString version = new MomString();
	private final MomString status = new MomString();
	private int sentBytes = -1;
	private final MomString referer = new MomString();
	private final MomString agent = new MomString();
	private final MomString forwardedUrl = new MomString();

	public NginxAccessLogEntry() {
	}

	public NginxAccessLogEntry(MomString line) {
		init(line);
	}

	public void init(MomString line) {
		content.init(line.getBuffer(), line.getOffset(), line.getLength());

		int setCount;
		if (line.isEmpty()) {
			setCount = 0;
		} else {
			setCount = parseLine(line.getBuffer(), line.getOffset());
		}

		switch (setCount) {
			case 0:
				ip.reset();
			case 1:
				user.reset();
			case 2:
				createTime = -1;
			case 3:
				method.reset();
			case 4:
				path.reset();
			case 5:
				version.reset();
			case 6:
				status.reset();
			case 7:
				sentBytes = -1;
			case 8:
				referer.reset();
			case 9:
				agent.reset();
			case 10:
				forwardedUrl.reset();
		}
	}

	@Override
	public int getSeverity() {
		if (status.isEmpty()) {
			return 0;
		}

		switch (status.charAt(0)) {
			case '4':
				return SEVERITY_WARN;
			case '5':
				return SEVERITY_ERROR;
			default:
				return SEVERITY_DEBUG;
		}
	}

	/**
	 * '$remote_addr - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent" "$http_x_forwarded_for"';
	 * 
	 * 10.96.241.72 - - [18/Apr/2012:23:56:16 +0900] "POST /api/event/publish HTTP/1.1" 200 0 "-" "Jakarta Commons-HttpClient/3.1" "-"
	 * 
	 * @param line
	 */
	private int parseLine(char[] chars, int offset) {
		int endIndex = next(chars, offset, ' ', ip);
		if (endIndex == -1) {
			return 0;
		}
		offset = endIndex + 3;

		endIndex = next(chars, offset, ' ', user);
		if (endIndex == -1) {
			return 1;
		}
		offset = endIndex + 2;

		endIndex = nextMatch(chars, offset, ']');
		if (endIndex == -1) {
			return 2;
		}
		createTime = NginxLogDateParser.parseDate(chars, offset);
		if (createTime == -1) {
			return 2;
		}
		offset = endIndex + 3;

		int requestEndIndex = nextMatch(chars, offset, '"');

		endIndex = next(chars, offset, ' ', method);
		if (endIndex < requestEndIndex) {
			if (endIndex == -1) {
				return 3;
			}
			offset = endIndex + 1;

			endIndex = next(chars, offset, ' ', path);
			if (endIndex == -1) {
				return 4;
			}
			offset = endIndex + 1;

			endIndex = next(chars, offset, '"', version);
			if (endIndex == -1) {
				return 5;
			}
		} else {
			endIndex = requestEndIndex;
			method.reset();
			path.reset();
			version.reset();
		}
		offset = endIndex + 2;

		endIndex = next(chars, offset, ' ', status);
		if (endIndex == -1) {
			return 6;
		}
		offset = endIndex + 1;

		endIndex = nextMatch(chars, offset, ' ');
		if (endIndex == -1) {
			return 7;
		}
		sentBytes = parseInt(chars, offset, endIndex, -1);
		if (sentBytes == -1) {
			return 7;
		}
		offset = endIndex + 2;

		endIndex = next(chars, offset, '"', referer);
		if (endIndex == -1) {
			return 8;
		}
		offset = endIndex + 3;

		endIndex = next(chars, offset, '"', agent);
		if (endIndex == -1) {
			return 9;
		}
		offset = endIndex + 3;

		endIndex = next(chars, offset, '"', forwardedUrl);
		if (endIndex == -1) {
			return 10;
		}

		return 11;
	}

	private int next(char[] chars, int offset, char sepChar, MomString momString) {
		int endIndex = nextMatch(chars, offset + 1, sepChar);
		if (endIndex == -1) {
			return -1;
		}

		momString.init(chars, offset, endIndex - offset);
		return endIndex;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("ip=").append(ip).append('\n');
		sb.append("user=").append(user).append('\n');
		sb.append("date=").append(new Date(createTime)).append('\n');
		sb.append("method=").append(method).append('\n');
		sb.append("path=").append(path).append('\n');
		sb.append("version=").append(version).append('\n');
		sb.append("status=").append(status).append('\n');
		sb.append("sentBytes=").append(sentBytes).append('\n');
		sb.append("referer=").append(referer).append('\n');
		sb.append("agent=").append(agent).append('\n');
		sb.append("forwardedUrl=").append(forwardedUrl).append('\n');

		return sb.toString();
	}

	public MomString getIp() {
		return ip;
	}

	public MomString getUser() {
		return user;
	}

	public MomString getMethod() {
		return method;
	}

	public MomString getPath() {
		return path;
	}

	public MomString getVersion() {
		return version;
	}

	public MomString getStatus() {
		return status;
	}

	public int getSentBytes() {
		return sentBytes;
	}

	public MomString getReferer() {
		return referer;
	}

	public MomString getAgent() {
		return agent;
	}

	public MomString getForwardedUrl() {
		return forwardedUrl;
	}
}
