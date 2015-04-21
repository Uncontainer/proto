/*
 * SlowestRequest.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.infra.monitoring.access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pulsarang.core.util.Tracable;

/**
 * 
 * @author pulsarang
 */
public class SlowestRequest {
	private final int count;
	private volatile int size;
	private volatile long mininum;
	private final Tracable[] list;

	public void reset() {
		size = 0;
		mininum = Long.MIN_VALUE;
		Arrays.fill(list, null);
	}

	public SlowestRequest(int count) {
		if (count < 1 || count > 10) {
			throw new IllegalArgumentException();
		}

		this.count = count;
		this.list = new Tracable[count];

		reset();
	}

	public List<Long> getTimes() {
		List<Long> result = new ArrayList<Long>(count);
		for (Tracable entry : list) {
			if (entry == null) {
				break;
			}
			result.add(entry.getTimeInMillies());
		}

		return result;
	}

	public synchronized void add(Tracable entry) {
		if (size < count) {
			int index;
			for (index = 0; index < count; index++) {
				if (list[index] == null) {
					break;
				}

				if (list[index].getTimeInMillies() < entry.getTimeInMillies()) {
					for (int j = size; j > index; j--) {
						list[j] = list[j - 1];
					}

					break;
				}
			}

			list[index] = entry;
			size++;
		} else {
			if (entry.getTimeInMillies() <= mininum) {
				return;
			}

			int index;
			for (index = 0; index < size; index++) {
				if (list[index].getTimeInMillies() < entry.getTimeInMillies()) {
					for (int j = size - 1; j > index; j--) {
						list[j] = list[j - 1];
					}

					break;
				}
			}

			list[index] = entry;
		}

		mininum = list[size - 1].getTimeInMillies();
	}
}
