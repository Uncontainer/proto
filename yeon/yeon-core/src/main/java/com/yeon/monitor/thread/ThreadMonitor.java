package com.yeon.monitor.thread;

import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author pulsarang
 */
public class ThreadMonitor extends AbstractReloadService {
	public static final String NAME = "THREAD";

	@Override
	public List<MapModel> list(MapModel optionMap) {
		Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
		List<MapModel> result = new ArrayList<MapModel>(allStackTraces.size());
		for (Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
			result.add(new ThreadInfo(entry.getKey(), entry.getValue()));
		}

		return result;
	}
}
