package com.pulsarang.infra.monitoring.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = SystemMonitoringItemMetaInfoProvider.NAME)
public class SystemMonitoringItemMetaInfoProvider extends AbstractReloadService {
	public static final String NAME = "SYSTEM_MONITORING_ITEM";

	@Override
	public List<MapModel> list(MapModel option) {
		Map<String, MomMBean> monitoringInfoMap = MomMBeanFactory.getInstance().getMonitoringInfoMap();
		List<MapModel> list = new ArrayList<MapModel>();
		for (Entry<String, MomMBean> entry : monitoringInfoMap.entrySet()) {
			for (MomMBeanPropertyInfo propertyInfo : entry.getValue().getPropertyInfos()) {
				MapModel item = new MapModel();
				item.setString("bean", entry.getKey());
				item.setString("property", propertyInfo.getName());
				item.setString("javaType", propertyInfo.getJavaType().getCanonicalName());
				list.add(item);
			}
		}

		return list;
	}

	@Override
	public int listCount(MapModel option) {
		int count = 0;
		Map<String, MomMBean> monitoringInfoMap = MomMBeanFactory.getInstance().getMonitoringInfoMap();
		for (MomMBean mbean : monitoringInfoMap.values()) {
			count += mbean.getPropertyInfos().size();
		}

		return count;
	}

}
