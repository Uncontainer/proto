package com.pulsarang.infra.config.prop;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ConfigPropertyRepository extends ConfigPropertyRepositoryCustom, JpaRepository<ConfigProperty, ConfigPropertyId> {
}
