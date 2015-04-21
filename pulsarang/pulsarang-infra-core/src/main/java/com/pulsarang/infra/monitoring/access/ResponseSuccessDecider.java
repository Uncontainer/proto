/*
 * ResponseSuccessDecider.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.infra.monitoring.access;

/**
 * 
 * @author pulsarang
 */
public interface ResponseSuccessDecider {
	boolean isSuccess(Object response, Throwable throwable);
}
