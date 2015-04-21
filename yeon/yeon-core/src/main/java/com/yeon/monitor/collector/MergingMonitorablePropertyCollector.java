package com.yeon.monitor.collector;

import com.yeon.server.ProjectId;
import com.yeon.util.MapModel;

import java.util.*;

/**
 * 
 * @author pulsarang
 */
public class MergingMonitorablePropertyCollector {
	RemoteMonitorablePropertyCollector invoker;

	public MergingMonitorablePropertyCollector(String solutionName) {
		invoker = new RemoteMonitorablePropertyCollector(solutionName);
	}

	public MergingMonitorablePropertyCollector(String solutionName, String projectName) {
		invoker = new RemoteMonitorablePropertyCollector(solutionName, projectName);
	}

	public MergingMonitorablePropertyCollector(ProjectId projectId) {
		invoker = new RemoteMonitorablePropertyCollector(projectId);
	}

	public List<Snapshot> listHistory(List<? extends MergableProperty> mergableProperties, int fetchCount) {
		List<String> propertyNames = getPropertyNames(mergableProperties);
		Map<Long, List<Map<String, Object>>> sourceShapshots = invoker.getHistory(propertyNames, fetchCount);

		List<Snapshot> mergedSnapshots = new ArrayList<Snapshot>(sourceShapshots.size());
		for (Map.Entry<Long, List<Map<String, Object>>> sourceShapshot : sourceShapshots.entrySet()) {
			Snapshot snapshot = new Snapshot();
			Map<String, Object> merged = merge(mergableProperties, sourceShapshot.getValue());
			snapshot.setValues(merged);
			snapshot.setCreateTime(sourceShapshot.getKey());

			mergedSnapshots.add(snapshot);
		}

		if (fetchCount > 1) {
			Collections.sort(mergedSnapshots, new Comparator<Snapshot>() {
				@Override
				public int compare(Snapshot o1, Snapshot o2) {
					long diff = o2.getCreateTime() - o1.getCreateTime();
					if (diff < 0) {
						return -1;
					} else if (diff > 0) {
						return 1;
					} else {
						return 0;
					}
				}
			});
		}

		return mergedSnapshots;
	}

	public Map<String, Object> getProperties(List<? extends MergableProperty> mergableProperties) {
		List<String> propertyNames = getPropertyNames(mergableProperties);

		List<Map<String, Object>> properties = invoker.getProperties(propertyNames);
		Map<String, Object> merged = merge(mergableProperties, properties);

		return merged;
	}

	public <T> T getProperty(MergableProperty mergableProperty, Class<T> clazz) {
		Map<String, Object> properties = getProperties(Arrays.asList(mergableProperty));
		if (properties.isEmpty()) {
			return null;
		}

		Object objProperty = properties.get(mergableProperty.getPropertyName());
		boolean isMapModel = MapModel.class.isAssignableFrom(clazz);
		if (isMapModel && objProperty instanceof Map) {
			return (T) MapModel.fromMap((Map) objProperty, (Class) clazz);
		} else {
			return (T) objProperty;
		}
	}

	private static List<String> getPropertyNames(List<? extends MergableProperty> mergableProperties) {
		List<String> propertyNames = new ArrayList<String>(mergableProperties.size());
		for (MergableProperty mergableProperty : mergableProperties) {
			propertyNames.add(mergableProperty.getPropertyName());
		}

		return propertyNames;
	}

	private Map<String, Object> merge(List<? extends MergableProperty> mergableProperties, List<Map<String, Object>> properties) {
		Map<String, Object> merged = new HashMap<String, Object>();
		for (MergableProperty mergableProperty : mergableProperties) {
			String propertyName = mergableProperty.getPropertyName();
			Mergable mergable = mergableProperty.getMergable();

			Object sourceValue = null;
			for (Map<String, Object> target : properties) {
				Object targetValue = target.get(propertyName);
				if (sourceValue == null) {
					sourceValue = targetValue;
					continue;
				} else if (targetValue == null) {
					continue;
				}

				sourceValue = mergable.merge(sourceValue, targetValue);
			}
			merged.put(propertyName, sourceValue);
		}

		return merged;
	}
}
