package com.naver.mage4j.external.varien;

import java.util.HashMap;
import java.util.Map;

public class Varien_Profiler {
	/**
	 * Timers for code profiling
	 *
	 * @var array
	 */
	private static Map<String, Timer> _timers = new HashMap<String, Timer>();
	private static boolean _enabled = false;

	public static void start(String timerName) {
		resume(timerName);
	}

	public static void stop(String timerName) {
		pause(timerName);
	}

	public static void pause(String timerName) {
		if (!_enabled) {
			return;
		}

		long time = System.currentTimeMillis(); // Get current time as quick as possible to make more accurate calculations

		Timer timer = getSafely(timerName);

		if (timer.isStarted()) {
			timer.sum += time - timer.start;
			timer.start = 0L;
		}
	}

	public static void resume(String timerName) {
		if (!_enabled) {
			return;
		}

		Timer timer = getSafely(timerName);

		timer.start = System.currentTimeMillis();
		timer.count++;
	}

	private static Timer getSafely(String timerName) {
		Timer timer = _timers.get(timerName);
		if (timer == null) {
			timer = reset(timerName);
		}

		return timer;
	}

	public static Timer reset(String timerName) {
		Timer timer = new Timer();
		_timers.put(timerName, timer);

		return timer;
	}

	public static class Timer {
		long start = 0L;
		int count = 0;
		int sum = 0;

		public Timer() {
		}

		boolean isStarted() {
			return start > 0L;
		}
	}
}
