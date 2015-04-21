package com.pulsarang.infra.config.info;

import com.pulsarang.infra.config.ConfigEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigInfoRepository extends ConfigInfoRepositoryCustom, JpaRepository<ConfigInfoEntity, ConfigEntityId> {

}
