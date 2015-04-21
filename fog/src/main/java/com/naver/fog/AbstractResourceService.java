package com.naver.fog;

import java.util.List;

public abstract class AbstractResourceService<T extends Resource> {
	protected AbstractResourceDao<T> resourceDao;

	public T getById(long id) {
		return resourceDao.selectById(id);
	}

	public int removeById(long id) {
		return resourceDao.deleteById(id);
	}

	public void add(T resource) {
		resourceDao.insert(resource);
	}

	public int modify(T resource) {
		return resourceDao.update(resource);
	}

	public List<T> listLatest(int count) {
		return resourceDao.selectLatest(count);
	}

	public List<T> listByIds(List<Long> ids) {
		return resourceDao.selectByIds(ids);
	}

	public List<Long> listIdsByNamePrefix(String namePrefix, int count) {
		return resourceDao.selectIdsByNamePrefix(namePrefix, count);
	}
}
