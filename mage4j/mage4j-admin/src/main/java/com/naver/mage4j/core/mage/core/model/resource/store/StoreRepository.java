package com.naver.mage4j.core.mage.core.model.resource.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Short> {
	Store findByCode(String code);

	@Query("SELECT count(*) FROM Store")
	int findCount();
}
