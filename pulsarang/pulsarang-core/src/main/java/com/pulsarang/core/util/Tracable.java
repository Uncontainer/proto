/*
 * Tracable.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.core.util;

/**
 * 
 * @author pulsarang
 */
public interface Tracable {
	long getStartTime();

	long getEndTime();

	long getTimeInMillies();
}
