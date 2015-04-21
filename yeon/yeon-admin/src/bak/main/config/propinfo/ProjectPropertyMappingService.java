package com.pulsarang.infra.config.propinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectPropertyMappingService {
	@Autowired
	ProjectPropertyMappingRepository ptpmRepository;

	public List<String> getProjectNames(ConfigPropertyInfoId configPropertyInfoId) {
		return ptpmRepository.findProjectNames(configPropertyInfoId);
	}

	@Transactional
	public void removeProjectConfigPropertyMapping(ProjectPropertyMappingId id) {
		ptpmRepository.delete(id);
	}

	@Transactional
	public void createProjectConfigPropertyMapping(ProjectPropertyMapping mapping) {
		ptpmRepository.save(mapping);
	}
}
