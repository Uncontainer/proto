package com.pulsarang.infra.config.prop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigCache;
import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.ConfigServerSelector;
import com.pulsarang.infra.config.info.ConfigInfoEntity;
import com.pulsarang.infra.config.info.ConfigInfoRepository;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfo;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfoRepository;
import com.pulsarang.infra.config.propinfo.type.PropertyValueType;
import com.pulsarang.infra.config.validator.ExpectedValidator;
import com.pulsarang.infra.config.validator.Validator;
import com.pulsarang.infra.config.validator.ValidatorFactory;
import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceResponseList;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Server;

@Service
public class ConfigPropertyService {
	@Autowired
	private ConfigPropertyRepository cpRepository;

	@Autowired
	private ConfigInfoRepository ciRepository;

	@Autowired
	private ConfigPropertyInfoRepository cpiRepository;

	@Autowired
	private ConfigServerSelector configServerSelector;

	public List<ConfigProperty> listRich(ConfigEntityId configEntityId, Map<String, Object> cacheValues) {
		if (cacheValues == null) {
			cacheValues = Collections.emptyMap();
		}

		List<ConfigProperty> configProperties = cpRepository.findConfigProperties(configEntityId);
		Map<ConfigPropertyId, ConfigProperty> configPropertyMap = new HashMap<ConfigPropertyId, ConfigProperty>();
		for (ConfigProperty configProperty : configProperties) {
			configPropertyMap.put(configProperty.getId(), configProperty);
		}

		List<ConfigPropertyInfo> configPropertyInfos = cpiRepository.findConfigPropertyInfos(configEntityId.getCategory());

		List<ConfigProperty> richProperties = new ArrayList<ConfigProperty>(configPropertyInfos.size());
		for (ConfigPropertyInfo configPropertyInfo : configPropertyInfos) {
			String propertyName = configPropertyInfo.getId().getPropertyName();
			ConfigPropertyId id = new ConfigPropertyId(configEntityId, propertyName);
			ConfigProperty configProperty = configPropertyMap.get(id);
			String propertyValue = configProperty != null ? configProperty.getPropertyValue() : null;

			ConfigProperty richProperty = new ConfigProperty(id, propertyValue);
			richProperty.setConfigPropertyInfo(configPropertyInfo);
			Object cacheValue = cacheValues.get(propertyName);
			if (cacheValue != null) {
				richProperty.setCacheValue(cacheValue.toString());
			}

			String validationExpression = configPropertyInfo.getValidationExpression();
			Validator validator = ValidatorFactory.createValidator(validationExpression);
			if (validator != null && (validator instanceof ExpectedValidator)) {
				String dataType = configPropertyInfo.getDataTypeCode();
				switch (PropertyValueType.valueOf(dataType.toUpperCase())) {
				case LIST:
				case SET:
					break;
				default:
					configPropertyInfo.setCandidates(((ExpectedValidator) validator).getCandidates());
					break;
				}
			}

			richProperties.add(richProperty);
		}

		return richProperties;
	}

	public Map<String, Object> getConfigPropertyMap(ConfigEntityId configEntityId) {
		List<ConfigProperty> configProperties = cpRepository.findConfigProperties(configEntityId);

		Map<String, Object> properties = new HashMap<String, Object>();
		for (ConfigProperty configProperty : configProperties) {
			properties.put(configProperty.getId().getPropertyName(), configProperty.getPropertyValue());
		}

		return properties;
	}

	public Map<String, Object> get(ConfigEntityId configEntityId, String defaultRunEnv) {
		// TODO 환경에 따른 설정값 가져오는 기능 추가.
		return getConfigPropertyMap(configEntityId);
	}

	@Transactional
	public void create(ConfigEntityId configEntityId, Map<String, String> params, boolean skipValidation) {
		Map<String, Object> insertParams = new HashMap<String, Object>();
		List<ConfigPropertyInfo> configPropertyInfos = cpiRepository.findConfigPropertyInfos(configEntityId.getCategory());
		for (ConfigPropertyInfo configPropertyInfo : configPropertyInfos) {
			String name = configPropertyInfo.getId().getPropertyName();
			String value = params.get(name);

			if (StringUtils.isEmpty(value)) {
				continue;
			}

			insertParams.put(name, value);
		}

//		insertParams.put("configEntityId", configEntityId);

		if (!skipValidation) {
			Config config = new Config(insertParams);
			config.setConfigCategoryName(configEntityId.getCategory());
			config.setConfigName(configEntityId.getName());

			RemoteServiceResponseList response = validateConfigProperties(configEntityId, config);

			if (response.getEntries().isEmpty()) {
				ConfigInfoEntity configInfo = ciRepository.findOne(configEntityId);
				// TODO 기본적인 유효성 검사만 하는 코드 추가.
				// ConfigInitUtil.validate(new ConfigKey(null,
				// null, configEntityId), configInfo.getCategory(),
				// (Map)insertParams);
			} else {
				if (response.isFail()) {
					throw new RuntimeException(response.getFailedEntries().toString());
				}
			}
		}

		for (Map.Entry<String, Object> entry : insertParams.entrySet()) {
			ConfigPropertyId id = new ConfigPropertyId(configEntityId, entry.getKey());
			ConfigProperty configPropery = new ConfigProperty(id, (String) entry.getValue());
			cpRepository.save(configPropery);
		}
	}

	@Transactional
	public void update(ConfigEntityId configEntityId, Map<String, Object> properties, boolean skipValidation) {
		Map<String, Object> configProperties = getConfigPropertyMap(configEntityId);
		Map<String, Object> changed = getChanged(configEntityId, properties, configProperties);

		Map<String, Object> changeApplied = getChangeApplied(configProperties, changed);
		// changeApplied.put("configEntityId", configEntityId);

		if (!skipValidation) {
			ConfigInfoEntity configInfo = ciRepository.findOne(configEntityId);
			List<Map<String, Object>> validatingConfigProperties = getValidatingConfigProperties(configInfo, changeApplied);

			for (Map<String, Object> validatingConfigProperty : validatingConfigProperties) {
				Config config = new Config(validatingConfigProperty);
				config.setConfigCategoryName(configEntityId.getCategory());
				config.setConfigName(configEntityId.getName());

				RemoteServiceResponseList response = validateConfigProperties(configEntityId, config);

				if (!response.getEntries().isEmpty() && response.isFail()) {
					throw new RuntimeException(response.getFailedEntries().toString());
				}
			}
		}

		// 설정 변경
		Map<String, String> updateOption = new HashMap<String, String>();
		Map<String, String> deleteOption = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : changed.entrySet()) {
			if (entry.getValue() != null && !(entry.getValue() instanceof String)) {
				throw new IllegalArgumentException("Option values type must be string.");
			}

			String value = (String) entry.getValue();

			if (StringUtils.isEmpty(value)) {
				deleteOption.put(entry.getKey(), value);
			} else {
				updateOption.put(entry.getKey(), value);
			}
		}

		save(configEntityId, updateOption, deleteOption);

		properties.put("config_modify_date", new Date());
		// dao.updateConfigModifyDate(paramMap);
	}

	private int save(ConfigEntityId id, Map<String, String> updated, Map<String, String> deleted) {
		int updateCount = 0;

		if (updated != null && !updated.isEmpty()) {
			for (Map.Entry<String, String> entry : updated.entrySet()) {
				ConfigProperty tp = new ConfigProperty(new ConfigPropertyId(id, entry.getKey()), entry.getValue());
				cpRepository.save(tp);
			}
			updateCount = updated.size();
		}

		if (deleted != null && !deleted.isEmpty()) {
			for (Map.Entry<String, String> entry : deleted.entrySet()) {
				ConfigProperty tp = new ConfigProperty();
				tp.setId(new ConfigPropertyId(id, entry.getKey()));
				cpRepository.delete(tp);
			}
		}

		return updateCount;
	}

	private RemoteServiceResponseList validateConfigProperties(ConfigEntityId configEntityId, Config config) {
		List<Server> servers = configServerSelector.getServers(configEntityId);

		ReloadService reloadService = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, ConfigCache.NAME, servers, 5000);

		reloadService.refresh(config);

		RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();

		return response;
	}

	private Map<String, Object> getChanged(ConfigEntityId id, Map<String, Object> changing, Map<String, Object> saved) {
		Map<String, Object> changed = new HashMap<String, Object>();

		List<ConfigPropertyInfo> configPropertyInfos = cpiRepository.findConfigPropertyInfos(id.getCategory());

		for (ConfigPropertyInfo configPropertyInfo : configPropertyInfos) {
			String name = configPropertyInfo.getId().getPropertyName();
			if (!changing.containsKey(name)) {
				continue;
			}

			String paramValue = (String) changing.get(name);
			Object dbValue = saved.get(name);

			if (isEmptyValue(paramValue)) {
				if (isEmptyValue(dbValue)) {
					continue;
				}
			} else {
				if (paramValue.equals(dbValue)) {
					continue;
				}
			}

			if (!configPropertyInfo.isModifiable()) {
				throw new IllegalArgumentException("'" + name + "' is unmodifiable config item.");
			}

			changed.put(name, paramValue);
		}

		return changed;
	}

	private static Map<String, Object> getChangeApplied(Map<String, Object> saved, Map<String, Object> changed) {
		saved = new HashMap<String, Object>(saved);
		for (Map.Entry<String, Object> element : changed.entrySet()) {
			if (isEmptyValue(element.getValue())) {
				saved.remove(element.getKey());
			} else {
				saved.put(element.getKey(), element.getValue());
			}
		}

		return saved;
	}

	private List<Map<String, Object>> getValidatingConfigProperties(ConfigInfoEntity configInfo, Map<String, Object> configProperties) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		setSubConfigProperties(result, configInfo, configProperties);

		return result;
	}

	private void setSubConfigProperties(List<Map<String, Object>> result, ConfigInfoEntity configInfo, Map<String, Object> configConfig) {
		if (configInfo.isAbstract()) {
			List<String> subConfigNames = ciRepository.findSubConfigNames(configInfo.getId());
			for (String subConfigName : subConfigNames) {
				ConfigEntityId subConfigEntityId = new ConfigEntityId(configInfo.getId().getCategory(), subConfigName);
				ConfigInfoEntity subConfigInfo = ciRepository.findOne(subConfigEntityId);
				Map<String, Object> subConfigProperties = getConfigPropertyMap(subConfigEntityId);

				setSubConfigProperties(result, subConfigInfo, subConfigProperties);
			}
		} else if (!configInfo.isStop()) {
			result.add(configConfig);
		}
	}

	private static boolean isEmptyValue(Object value) {
		if (value == null) {
			return true;
		}

		if (value instanceof String) {
			return ((String) value).isEmpty();
		}

		return true;
	}
}
