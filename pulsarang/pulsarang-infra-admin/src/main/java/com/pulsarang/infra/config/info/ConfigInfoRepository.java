package com.pulsarang.infra.config.info;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulsarang.infra.config.ConfigEntityId;

public interface ConfigInfoRepository extends ConfigInfoRepositoryCustom, JpaRepository<ConfigInfoEntity, ConfigEntityId> {

}
