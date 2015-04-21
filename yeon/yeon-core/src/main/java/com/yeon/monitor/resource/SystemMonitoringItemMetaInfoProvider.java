package com.yeon.monitor.resource;

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
public class SystemMonitoringItemMetaInfoProvider extends AbstractReloadService {
	public static final String NAME = "SYSTEM_MONITORING_ITEM";

	@Override
	public List<MapModel> list(MapModel optionMap) {
		Map<String, MomMBean> monitoringInfoMap = MomMBeanFactory.getInstance().getMonitoringInfoMap();
		List<MapModel> list = new ArrayList<MapModel>();
		for (Entry<String, MomMBean> entry : monitoringInfoMap.entrySet()) {
			for (MomMBeanPropertyInfo propertyInfo : entry.getValue().getPropertyInfos()) {
				MapModel item = new MapModel();
				item.put("bean", entry.getKey());
				item.put("property", propertyInfo.getName());
				item.put("javaType", propertyInfo.getJavaType().getCanonicalName());
				list.add(item);
			}
		}

		return list;
	}

	@Override
	public int listCount(MapModel optionMap) {
		int count = 0;
		Map<String, MomMBean> monitoringInfoMap = MomMBeanFactory.getInstance().getMonitoringInfoMap();
		for (MomMBean mbean : monitoringInfoMap.values()) {
			count += mbean.getPropertyInfos().size();
		}

		return count;
	}

}
