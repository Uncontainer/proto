package com.yeon.lang.value.locale;

import com.yeon.lang.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LocalValueServiceImpl implements LocalValueService {
	@Autowired
	private LocalValueDao localValueDao;

	@Override
	public LocalValueEntity get(LocalValueType type, long entityId) {
		return localValueDao.select(type, entityId);
	}

	@Override
	@Transactional
	public LocalValueEntity add(LocalValueType type, LocalValueEntity entry) {
		if (entry.getId() != null) {
			throw new IllegalArgumentException("NameEntryEntity id is not null.");
		}
		entry.setCreateDate(new Date());

		long id = localValueDao.insert(type, entry);
		entry.setId(id);

		return entry;
	}

	@Override
	@Transactional
	public int remove(LocalValueType type, long id) {
		return localValueDao.delete(type, id);
	}

	@Override
	@Transactional
	public int modify(LocalValueType type, LocalValueEntity entry) {
		if (entry.getId() == null) {
			throw new IllegalArgumentException("NameEntryEntity id is null.");
		}

		return localValueDao.update(type, entry.getId(), entry.getValue());
	}

	@Override
	public List<LocalValueEntity> listByLocale(LocalValueType type, String resourceId, String locale) {
		return localValueDao.selectAllByLocale(type, resourceId, locale);
	}

	@Override
	public LocalValueEntity getByValue(LocalValueType type, String resourceId, String locale, String value) {
		return localValueDao.selectByValue(type, resourceId, locale, value);
	}

	@Override
	public Page<LocalValueEntity> listByPrefix(LocalValueType type, ResourceType resourceType, String value, Pageable pageable) {
		return localValueDao.selectAllByPrefix(type, resourceType, value, pageable);
	}

	@Override
	public List<LocalValueEntity> listByValue(LocalValueType type, ResourceType resourceType, String locale, String value) {
		return localValueDao.selectAllByValue(type, resourceType, locale, value);
	}

	@Override
	public List<LocalValueEntity> listByResourceId(LocalValueType type, String resourceId) {
		return localValueDao.selectAllByResourceId(type, resourceId);
	}
}
