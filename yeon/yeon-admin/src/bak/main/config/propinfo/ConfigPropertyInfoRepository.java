package com.pulsarang.infra.config.propinfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigPropertyInfoRepository extends ConfigPropertyInfoRepositoryCustom, JpaRepository<ConfigPropertyInfo, ConfigPropertyInfoId> {
}
