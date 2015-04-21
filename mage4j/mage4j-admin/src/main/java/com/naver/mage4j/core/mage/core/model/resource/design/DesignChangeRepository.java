package com.naver.mage4j.core.mage.core.model.resource.design;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DesignChangeRepository extends JpaRepository<DesignChange, Integer> {
	@Query("SELECT d FROM DesignChange d WHERE d.store.id=:storeId and (dateFrom <= :date OR dateFrom IS NULL) and (dateTo >= :date OR dateTo IS NULL)")
	DesignChange findByStoreId(@Param("storeId") Short storeId, @Param("date") Date date);
}
