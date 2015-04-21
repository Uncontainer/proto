package com.yeon.monitor.log;

import com.yeon.monitor.common.MomString;
import com.yeon.remote.RemoteContext;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerLogMonitor extends AbstractReloadService {
	private final Logger log = LoggerFactory.getLogger(ServerLogMonitor.class);

	public static final String NAME = "SERVER_LOG";
	public static final String COLLECT_STATUS = "SERVER_LOG_COLLECT_STATUS";
	public static final String PARAM_OPTIONS = "options";

	public static final int MAX_PROCESS_COUNT = 2;
	private final char[][] buffers = new char[MAX_PROCESS_COUNT][];
	private int processCount = 0;
	private final List<ServerLogCollector> collectors;

	public ServerLogMonitor() {
		collectors = Collections.synchronizedList(new ArrayList<ServerLogCollector>());
	}

	public void registAsRemoteService(RemoteContext remoteContext) {
		remoteContext.setRemoteService(NAME, this);
		remoteContext.setRemoteService(COLLECT_STATUS, new StatusMonitor(collectors));
	}

	@Override
	public List<MapModel> list(MapModel optionMap) {
		List<Map<String, Object>> options = optionMap.getList(PARAM_OPTIONS);
		List<MapModel> result;

		if (options == null || options.isEmpty()) {
			result = new ArrayList<MapModel>(1);
			result.add(ServerLog.createFail("Empty options"));
		} else {
			result = new ArrayList<MapModel>(options.size());
			for (Map<String, Object> option : options) {
				result.add(get(new MapModel(option)));
			}
		}

		return result;
	}

	@Override
	public MapModel get(MapModel optionMap) {
		char[] buffer = null;
		ServerLogCollector collector = null;
		ServerLog serverLog;
		ServerLogCollectOption option = null;
		try {
			option = new ServerLogCollectOption(optionMap);
			option.validate();

			buffer = getBuffer();
			collector = new ServerLogCollector(new MomString(buffer), option);
			collectors.add(collector);
			serverLog = collector.collect();
		} catch (Exception e) {
			log.debug("[YEON] Fail to get server log entry.", e);
			serverLog = ServerLog.createFail(e.getMessage());
		} finally {
			if (collector != null) {
				collectors.remove(collector);
			}
			releaseBuffer(buffer);
			CalendarHolder.remove();
		}

		serverLog.setOption(option);

		return serverLog;
	}

	private synchronized char[] getBuffer() {
		if (processCount >= MAX_PROCESS_COUNT) {
			throw new RuntimeException("Too may concurrent request.(max=" + MAX_PROCESS_COUNT + ")");
		}

		if (buffers[processCount] == null) {
			buffers[processCount] = new char[(int) (ServerLogCollectOption.MAX_SCAN_SIZE * 1.1)];
		}

		char[] buffer = buffers[processCount++];

		return buffer;
	}

	private synchronized void releaseBuffer(char[] buffer) {
		if (buffer == null) {
			return;
		}

		buffers[--processCount] = buffer;
	}

	static class StatusMonitor extends AbstractReloadService {
		List<ServerLogCollector> collectors;

		StatusMonitor(List<ServerLogCollector> collectors) {
			super();
			this.collectors = collectors;
		}

		@Override
		public List<MapModel> list(MapModel optionMap) {
			if (collectors.isEmpty()) {
				return Collections.emptyList();
			}

			List<MapModel> result = new ArrayList<MapModel>(collectors.size());
			for (ServerLogCollector collector : collectors) {
				result.add(collector.getStatus());
			}

			return result;
		}

		@Override
		public MapModel get(MapModel optionMap) {
			ServerLogCollector collector = findCollector(optionMap);
			if (collector == null) {
				return null;
			}

			return collector.getStatus();
		}

		@Override
		public int remove(MapModel optionMap) {
			ServerLogCollector collector = findCollector(optionMap);
			if (collector == null) {
				return 0;
			}

			collector.stop(optionMap);
			return 1;
		}

		private ServerLogCollector findCollector(MapModel optionMap) {
			int id = optionMap.getInteger(ServerLogCollectStatus.PARAM_ID, 0);
			for (ServerLogCollector collector : collectors) {
				if (collector.getId() == id) {
					return collector;
				}
			}

			return null;
		}
	}
}
