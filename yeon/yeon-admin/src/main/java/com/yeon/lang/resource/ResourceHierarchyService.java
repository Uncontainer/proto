package com.yeon.lang.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceHierarchyService {
	@Autowired
	private ResourceHierarchyDao hierarchyDao;

	public void add(String id, List<String> parentIds) {
		int order = 100;
		for (String parentId : parentIds) {
			// TODO 존재하는 parentId인지 조사하는 코드 추가.
			hierarchyDao.insert(id, parentId, order);
			order += 100;
		}
	}

	public List<String> getParentIds(String id) {
		return hierarchyDao.selectParentIds(id);
	}
}
