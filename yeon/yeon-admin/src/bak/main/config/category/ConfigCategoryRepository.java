package com.pulsarang.infra.config.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigCategoryRepository extends ConfigCategoryRepositoryCustom, JpaRepository<ConfigCategoryEntity, String> {
}
