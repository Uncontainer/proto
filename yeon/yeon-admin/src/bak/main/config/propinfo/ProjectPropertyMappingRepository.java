package com.pulsarang.infra.config.propinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

// TODO ID type 추가.
public interface ProjectPropertyMappingRepository extends ProjectPropertyMappingRepositoryCustom, JpaRepository<ProjectPropertyMapping, Serializable> {
}
