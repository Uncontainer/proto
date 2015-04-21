package com.pulsarang.infra.config.propinfo;

import com.pulsarang.infra.config.info.ConfigInfoRepository;
import com.pulsarang.infra.config.propinfo.type.PropertyValueType;
import com.pulsarang.infra.config.validator.Validator;
import com.pulsarang.infra.config.validator.ValidatorFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConfigPropertyInfoService {

	@Autowired
	ConfigInfoRepository ciRepository;

	@Autowired
	ConfigPropertyInfoRepository cpiRepository;

	public List<ConfigPropertyInfo> getConfigPropertyInfos(String configCategory) {
		if (StringUtils.isEmpty(configCategory)) {
			return cpiRepository.findAll();
		} else {
			return cpiRepository.findConfigPropertyInfos(configCategory);
		}
	}

	public Map<String, ConfigPropertyInfo> listByCategory(String configCategory) {
		List<ConfigPropertyInfo> configPropertyInfos = getConfigPropertyInfos(configCategory);

		Map<String, ConfigPropertyInfo> configPropertyInfoMap = new HashMap<String, ConfigPropertyInfo>();
		for (ConfigPropertyInfo e : configPropertyInfos) {
			configPropertyInfoMap.put(e.getId().getPropertyName(), e);
		}

		return configPropertyInfoMap;
	}

	public ConfigPropertyInfo getConfigPropertyInfo(ConfigPropertyInfoId id) {
		return cpiRepository.findOne(id);
	}

	@Transactional
	public void removeConfigPropertyInfo(ConfigPropertyInfoId id) {
		cpiRepository.delete(id);
	}

	@Transactional
	public void createConfigPropertyInfo(ConfigPropertyInfo configPropertyInfo) {
		ConfigPropertyInfoId id = configPropertyInfo.getId();
		if (id == null || StringUtils.isBlank(id.getConfigCategory()) || StringUtils.isBlank(id.getPropertyName())) {
			throw new IllegalArgumentException("Invalid id.(" + id + ")");
		}

		validateDefaultValue(configPropertyInfo);
		cpiRepository.save(configPropertyInfo);
	}

	@Transactional
	public void modifyConfigPropertyInfo(ConfigPropertyInfo configPropertyInfo) {
		validateDefaultValue(configPropertyInfo);
		cpiRepository.save(configPropertyInfo);
	}

	private void validateDefaultValue(ConfigPropertyInfo configPropertyInfo) {
		Validator validator = ValidatorFactory.createValidator(configPropertyInfo.getValidationExpression());
		if (validator == null) {
			return;
		}

		String defaultValue = configPropertyInfo.getDefaultValue();
		if (StringUtils.isEmpty(defaultValue)) {
			return;
		}

		PropertyValueType parser = PropertyValueType.valueOf(configPropertyInfo.getDataTypeCode());
		Object parsedValue = parser.parse(defaultValue);

		String propertyName = configPropertyInfo.getId().getPropertyName();
		if (parsedValue instanceof Collection<?>) {
			Iterator<?> iterator = ((Collection<?>) parsedValue).iterator();
			while (iterator.hasNext()) {
				validator.validate(iterator.next().toString(), propertyName);
			}
		} else {
			validator.validate(defaultValue, propertyName);
		}
	}
}
