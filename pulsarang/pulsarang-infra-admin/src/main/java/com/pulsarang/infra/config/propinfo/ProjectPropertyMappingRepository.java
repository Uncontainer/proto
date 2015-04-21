package com.pulsarang.infra.config.propinfo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

// TODO ID type 추가.
public interface ProjectPropertyMappingRepository extends ProjectPropertyMappingRepositoryCustom, JpaRepository<ProjectPropertyMapping, Serializable> {
}
