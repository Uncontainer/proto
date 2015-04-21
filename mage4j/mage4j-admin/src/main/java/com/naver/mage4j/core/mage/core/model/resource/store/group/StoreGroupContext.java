package com.naver.mage4j.core.mage.core.model.resource.store.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreGroupContext {
	@Autowired
	private StoreGroupRepository storeGroupRepository;

	public StoreGroup getById(short id) {
		return storeGroupRepository.findOne(id);
	}
}
