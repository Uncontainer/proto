package com.pulsarang.infra.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraParameters;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.config.category.ConfigCategoryEntity;
import com.pulsarang.infra.config.category.ConfigCategoryService;
import com.pulsarang.infra.config.info.ConfigInfoEntity;
import com.pulsarang.infra.config.info.ConfigInfoRepository;
import com.pulsarang.infra.config.info.ConfigInfoService;
import com.pulsarang.infra.config.prop.ConfigProperty;
import com.pulsarang.infra.config.prop.ConfigPropertyRepository;
import com.pulsarang.infra.config.prop.ConfigPropertyService;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfo;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfoService;
import com.pulsarang.infra.config.propinfo.type.PropertyValueType;
import com.pulsarang.infra.config.validator.Validator;
import com.pulsarang.infra.config.validator.ValidatorFactory;
import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceResponseList;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Server;

@Service
public class ConfigService {
	private final Logger log = LoggerFactory.getLogger(ConfigService.class);

	@Autowired
	private ConfigInfoRepository ciRepository;

	@Autowired
	private ConfigPropertyRepository cpRepository;

	@Autowired
	private ConfigPropertyService cpService;

	@Autowired
	private ConfigPropertyInfoService cpiService;

	@Autowired
	private ConfigCategoryService ccService;

	@Autowired
	private ConfigInfoService ciService;

	@Autowired
	private ConfigServerSelector configServerSelector;

	@Transactional
	public void remove(ConfigEntityId configEntityId) {
		cpRepository.deleteAll(configEntityId);
		ciRepository.delete(configEntityId);
	}

	public List<Map<String, Object>> getDisplaying(ConfigEntityId configEntityId) {
		// TODO configInfo 설정 코드 추가.
		// ConfigInfo dbConfigInfo = tiRepository.findOne(configKey);
		List<ConfigProperty> dbConfigProperties = cpRepository.findConfigProperties(configEntityId);

		// TODO remote 호출 기능 추가.
		// ServerEntity server = selectServer(model, criteria);
		Map<String, Object> appConfig = new HashMap<String, Object>();

		List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();

		for (ConfigProperty dbOption : dbConfigProperties) {
			Map<String, Object> option = new HashMap<String, Object>();

			option.put("name", dbOption.getId().getPropertyName());
			option.put("db", dbOption.getPropertyValue());
			option.put("app", appConfig.remove(dbOption.getId().getPropertyName()));

			options.add(option);
		}

		for (Map.Entry<String, Object> appOption : appConfig.entrySet()) {
			Map<String, Object> option = new HashMap<String, Object>();
			option.put("name", appOption.getKey());
			option.put("app", appConfig.get(appOption.getKey()));

			options.add(option);
		}

		Collections.sort(options, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((String) o1.get("name")).compareTo((String) o2.get("name"));
			}
		});

		return options;
	}

	public List<ConfigInfoEntity> getConfigInfos() {
		// TODO 권한별로 가져오는 코드 추가.
		List<ConfigInfoEntity> configInfos = ciRepository.findAll();

		Collections.sort(configInfos, new Comparator<ConfigInfoEntity>() {
			@Override
			public int compare(ConfigInfoEntity o1, ConfigInfoEntity o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		return configInfos;
	}

	// @Override
	public Config get(ConfigEntityId configEntityId) {
		Map<String, Object> mapConfig = new HashMap<String, Object>();

		Map<String, Object> properties = cpService.getConfigPropertyMap(configEntityId);
		Map<String, ConfigPropertyInfo> configPropertyInfoMap = cpiService.listByCategory(configEntityId.getCategory());

		// 1. 설정 정보에 명시된 type에 맞게 설정값 type 변경.
		// 2. 설정 가능한 값 리스트에 없는 항목들은 제거
		for (Map.Entry<String, Object> property : properties.entrySet()) {
			ConfigPropertyInfo configPropertyInfo = configPropertyInfoMap.get(property.getKey());
			if (configPropertyInfo == null) {
				log.debug("Option '{}' is unregistered item. This is removed.({})", property.getKey(), configEntityId);
				continue;
			}

			String value = (String) property.getValue();
			if (StringUtils.isNotEmpty(value)) {
				PropertyValueType propertyValueType = PropertyValueType.valueOf(configPropertyInfo.getDataTypeCode());
				mapConfig.put(property.getKey(), propertyValueType.parse(value));
			}
		}

		// 필수 파라미터 검사 및 기본 값 설정
		for (ConfigPropertyInfo configPropertyInfo : configPropertyInfoMap.values()) {
			String propertyName = configPropertyInfo.getId().getPropertyName();
			Object value = mapConfig.get(propertyName);

			if (value == null) {
				PropertyValueType propertyValueType = PropertyValueType.valueOf(configPropertyInfo.getDataTypeCode());
				Object defalutValue = propertyValueType.parse(configPropertyInfo.getDefaultValue());
				if (defalutValue != null) {
					value = defalutValue;
					mapConfig.put(propertyName, value);
				} else if (configPropertyInfo.isRequired()) {
					throw new IllegalArgumentException("'" + propertyName + "' is required option.(" + configEntityId + ")");
				} else {
					continue;
				}
			}

			Validator validator = ValidatorFactory.createValidator(configPropertyInfo.getValidationExpression());
			if (validator != null) {
				if (value instanceof Collection<?>) {
					Iterator<?> iterator = ((Collection<?>) value).iterator();
					while (iterator.hasNext()) {
						validator.validate(iterator.next().toString(), propertyName);
					}
				} else {
					validator.validate(value.toString(), propertyName);
				}
			}
		}

		Config config = new Config(mapConfig);
		config.setConfigCategoryName(configEntityId.getCategory());
		config.setConfigName(configEntityId.getName());

		return config;
	}

	public Config create(Config paramConfig) {
		ConfigCategoryEntity configCategory = ccService.getConfigCategory(paramConfig.getConfigCategoryName());
		if (configCategory == null) {
			return null;
		}

		ConfigEntityId configEntityId = new ConfigEntityId(paramConfig.getConfigCategoryName(), paramConfig.getConfigName());

		ConfigInfoEntity configInfoEntity = new ConfigInfoEntity();
		configInfoEntity.setId(configEntityId);
		configInfoEntity.setAbstract(false);
		configInfoEntity.setDescription(paramConfig.getString("description"));
		ciService.createConfigInfo(configInfoEntity);

		List<ConfigPropertyInfo> configPropertyInfos = cpiService.getConfigPropertyInfos(paramConfig.getConfigCategoryName());
		Map<String, Object> properties = paramConfig.getProperties();
		Map<String, String> params = new HashMap<String, String>();
		for (ConfigPropertyInfo configPropertyInfo : configPropertyInfos) {
			String propertyName = configPropertyInfo.getId().getPropertyName();
			Object property = properties.get(propertyName);
			if (property != null) {
				// TODO object로 변환하여 사용하도록 수정.
				params.put(propertyName, configPropertyInfo.toString(property));
			}
		}

		cpService.create(configEntityId, params, false);

		return get(configEntityId);
	}

	public Config update(Config paramConfig) {
		ConfigEntityId configEntityId = new ConfigEntityId(paramConfig.getConfigCategoryName(), paramConfig.getConfigName());

		List<ConfigPropertyInfo> configPropertyInfos = cpiService.getConfigPropertyInfos(paramConfig.getConfigCategoryName());
		Map<String, Object> properties = paramConfig.getProperties();
		Map<String, Object> params = new HashMap<String, Object>();
		for (ConfigPropertyInfo configPropertyInfo : configPropertyInfos) {
			String propertyName = configPropertyInfo.getId().getPropertyName();
			Object property = properties.get(propertyName);
			if (property != null) {
				// TODO type check 추가.
				params.put(propertyName, property);
			}
		}

		cpService.update(configEntityId, params, false);

		return get(configEntityId);
	}

	public void refresh(ConfigEntityId configEntityId) {
		List<Server> servers = configServerSelector.getServers(configEntityId);
		ReloadService reloadService = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, ConfigCache.NAME, servers, 5000);

		MapModel option = new MapModel();
		option.setString(InfraParameters.CATEGORY, configEntityId.getCategory());
		option.setString(InfraParameters.TICKET, configEntityId.getName());
		reloadService.refresh(option);

		RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();

		if (!response.getEntries().isEmpty() && response.isFail()) {
			throw new RuntimeException(response.getFailedEntries().toString());
		}
	}

	public List<ConfigId> listIdsByCategory(ConfigCategory category) {
		List<String> configNames = ciRepository.findConfigNamesByCategory(category);
		if (configNames.isEmpty()) {
			return Collections.emptyList();
		}

		List<ConfigId> configIds = new ArrayList<ConfigId>(configNames.size());
		for (String configName : configNames) {
			configIds.add(new ConfigId(category.getName(), configName));
		}

		return configIds;
	}
}
