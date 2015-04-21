package com.naver.mage4j.core.mage.core.model.resource.website;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WebsiteRepository extends JpaRepository<Website, Short> {
	Website findByCode(String code);

	@Query("SELECT c FROM Website c WHERE isDefault = 1")
	Website findDefault();
}
