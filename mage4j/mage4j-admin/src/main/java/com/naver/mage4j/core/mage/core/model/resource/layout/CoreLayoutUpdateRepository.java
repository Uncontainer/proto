package com.naver.mage4j.core.mage.core.model.resource.layout;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreLayoutUpdateRepository extends JpaRepository<CoreLayoutUpdate, Integer>, CoreLayoutUpdateRepositoryCustom {

}
