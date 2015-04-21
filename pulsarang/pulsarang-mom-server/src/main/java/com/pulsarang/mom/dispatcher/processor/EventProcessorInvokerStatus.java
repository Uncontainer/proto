/*
 * ExecutorStatus.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.mom.dispatcher.processor;

/**
 * 
 * @author pulsarang
 */
public enum EventProcessorInvokerStatus {
	WAIT, RUNNING, CANCELLED, SUCCESS, FAIL, INTERRUPTED, DAEMON;
}
