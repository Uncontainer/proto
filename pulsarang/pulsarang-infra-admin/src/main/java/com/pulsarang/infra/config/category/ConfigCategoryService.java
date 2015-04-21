package com.pulsarang.infra.config.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pulsarang.infra.config.ConfigId;

@Service
public class ConfigCategoryService {
	@Autowired
	ConfigCategoryRepository ccRepository;

	public List<ConfigCategoryEntity> getConfigCategories() {
		return ccRepository.findAll();
	}

	public ConfigCategoryEntity getConfigCategory(String categoryName) {
		return ccRepository.findOne(categoryName);
	}

	public void createConfigCategory(ConfigCategoryEntity configCategory) {
		if (ccRepository.findOne(configCategory.getName()) != null) {
			throw new IllegalStateException("Configuraion category already exist.(" + configCategory.getName() + ")");
		}

		ccRepository.save(configCategory);
	}

	public void removeConfigCategory(String categoryName) {
		ccRepository.delete(categoryName);
	}

	public void modifyConfigCategory(ConfigCategoryEntity configCategory) {
		if (ccRepository.findOne(configCategory.getName()) == null) {
			throw new IllegalStateException("Configuraion category does not exist.(" + configCategory.getName() + ")");
		}

		ccRepository.save(configCategory);
	}

	public List<String> getCategoryNames() {
		return ccRepository.findCategoryNames();
	}
}
