package com.naver.mage4j.core.mage.core.model.resource.url;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlRewriteRepository extends JpaRepository<UrlRewrite, Integer> {
	@Query("SELECT u FROM UrlRewrite u WHERE u.requestPath IN :pathes AND u.store.id IN :storeIds")
	List<UrlRewrite> findByRequestPathInAndStoreIdIn(@Param("pathes") List<String> path, @Param("storeIds") List<Short> storeIds);

	UrlRewrite findByIdPath(String idPath);
}
