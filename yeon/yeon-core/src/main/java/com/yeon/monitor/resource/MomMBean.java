package com.yeon.monitor.resource;

import java.util.List;

/**
 * 
 * @author pulsarang
 */
public interface MomMBean {
	String getName();

	Object getProperty(String name);

	List<MomMBeanPropertyInfo> getPropertyInfos();

	boolean isExistProperty(String name);
}
