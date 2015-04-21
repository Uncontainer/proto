package com.yeon.lang.value.locale;

import com.yeon.lang.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocalValueService {
	LocalValueEntity get(LocalValueType type, long entityId);

	List<LocalValueEntity> listByResourceId(LocalValueType type, String resourceId);

	List<LocalValueEntity> listByLocale(LocalValueType type, String resourceId, String locale);

	Page<LocalValueEntity> listByPrefix(LocalValueType type, ResourceType resourceType, String namePrefix, Pageable pageable);

	List<LocalValueEntity> listByValue(LocalValueType type, ResourceType resourceType, String locale, String value);

	LocalValueEntity getByValue(LocalValueType type, String resourceId, String locale, String value);

	LocalValueEntity add(LocalValueType type, LocalValueEntity entity);

	int modify(LocalValueType type, LocalValueEntity entity);

	int remove(LocalValueType type, long entityId);
}
