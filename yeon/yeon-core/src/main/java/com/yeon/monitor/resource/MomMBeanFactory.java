package com.yeon.monitor.resource;

import com.yeon.monitor.resource.cpu.CpuMonitor;
import org.apache.commons.lang.StringUtils;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author pulsarang
 */
public class MomMBeanFactory {
	private static final MomMBeanFactory INSTANCE = new MomMBeanFactory();

	public static MomMBeanFactory getInstance() {
		return INSTANCE;
	}

	final Map<String, MomMBean> monitoringInfoMap = new ConcurrentHashMap<String, MomMBean>();

	/**
	 * {@linkplain SystemResourceMonitor}에 의해서 초기화.
	 */
	private MomMBeanFactory() {
		super();

		addSunMXBean("os", ManagementFactory.getOperatingSystemMXBean());
		addSunMXBean("thread", ManagementFactory.getThreadMXBean());
		addSunMXBean("memory", ManagementFactory.getMemoryMXBean());
		addSunMXBean("classLoading", ManagementFactory.getClassLoadingMXBean());
		addSunMXBean("runtime", ManagementFactory.getRuntimeMXBean());
		addSunMXBean("compilation", ManagementFactory.getCompilationMXBean());
		addMBean("cpu", CpuMonitor.getInstance());
	}

	private void addSunMXBean(String name, Object bean) {
		POJOMBean mxBeanInfo = new POJOMBean(name, bean);
		monitoringInfoMap.put(name, mxBeanInfo);
	}

	Map<String, MomMBean> getMonitoringInfoMap() {
		return monitoringInfoMap;
	}

	public void addMBean(MomMBean item) {
		if (item == null || StringUtils.isEmpty(item.getName())) {
			throw new IllegalArgumentException();
		}

		monitoringInfoMap.put(item.getName(), item);
	}

	public void addMBean(String name, Object bean) {
		POJOMBean mxBeanInfo = new POJOMBean(name, bean);
		monitoringInfoMap.put(name, mxBeanInfo);
	}

	public Object getProperty(String beanName, String propertyName) {
		MomMBean mbean = monitoringInfoMap.get(beanName);
		if (mbean == null) {
			throw new IllegalArgumentException("Unregistered bean '" + beanName + "'");
		}

		return mbean.getProperty(propertyName);
	}

	public boolean isExistPropery(String beanName, String propertyName) {
		MomMBean mbean = monitoringInfoMap.get(beanName);
		if (mbean == null) {
			return false;
		}

		return mbean.isExistProperty(propertyName);
	}

	//	public static void main(String[] args) {
	//		for (Entry<String, MomMBean> entry : getInstance().monitoringInfoMap.entrySet()) {
	//			for (Entry<String, Method> entry2 : ((POJOMBean)entry.getValue()).methodMap.entrySet()) {
	//				System.out.print(entry.getKey() + "." + entry2.getKey() + ": ");
	//				System.out.println(getInstance().getProperty(entry.getKey(), entry2.getKey()));
	//			}
	//		}
	//	}
}
