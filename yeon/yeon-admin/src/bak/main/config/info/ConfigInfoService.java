package com.pulsarang.infra.config.info;

import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfoRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ConfigInfoService {
	@Autowired
	ConfigInfoRepository ciRepository;

	@Autowired
	ConfigPropertyInfoRepository cpiRepository;

	public ConfigInfoEntity getConfigInfo(ConfigEntityId configEntityId) {
		return ciRepository.findOne(configEntityId);
	}

	@Transactional
	public void createConfigInfo(ConfigInfoEntity configInfoEntity) {
		ConfigEntityId id = configInfoEntity.getId();
		if (id == null || StringUtils.isBlank(id.getCategory()) || StringUtils.isBlank(id.getName())) {
			throw new IllegalArgumentException("Empty config id.(" + id + ")");
		}
		checkSuperConfig(configInfoEntity);

		if (getConfigInfo(configInfoEntity.getId()) != null) {
			throw new IllegalArgumentException("Config '" + configInfoEntity.getId() + "' has already exist.");
		}

		ciRepository.save(configInfoEntity);
	}

	@Transactional
	public void updateConfigInfo(ConfigInfoEntity configInfoEntity) {
		ConfigEntityId id = configInfoEntity.getId();
		ConfigInfoEntity existConfigInfo = getConfigInfo(id);
		// 상속받아 사용하는 config이 있는 추상 config인 경우 abstract_yn을 'N'으로 변경할 수 없다.
		if (!configInfoEntity.isAbstract() && existConfigInfo.isAbstract()) {
			List<String> subConfigs = ciRepository.findSubConfigNames(id);
			if (!subConfigs.isEmpty()) {
				throw new IllegalArgumentException("Abstract config which has children cannot change to concrete.");
			}
		}

		checkSuperConfig(configInfoEntity);

		ciRepository.save(configInfoEntity);
	}

	private void checkSuperConfig(ConfigInfoEntity configInfo) {
		if (StringUtils.isEmpty(configInfo.getSuperConfigNames())) {
			return;
		}

		Set<String> childConfigs = new HashSet<String>();
		childConfigs.add(configInfo.getId().toString());

		String category = configInfo.getId().getCategory();

		String[] superConfigNames = StringUtils.split(configInfo.getSuperConfigNames(), ", \t\r\n");
		for (String superConfigName : superConfigNames) {
			ConfigEntityId superConfigId = new ConfigEntityId(category, superConfigName);
			ConfigInfoEntity superConfigInfo = ciRepository.findOne(superConfigId);
			if (superConfigInfo == null) {
				throw new IllegalArgumentException("Super config does not exist.(" + superConfigName + ")");
			}

			if (!superConfigInfo.isAbstract()) {
				throw new IllegalArgumentException("Super config must be a abstract.(" + superConfigName + ")");
			}

			checkCycle(superConfigId, childConfigs);
		}
	}

	private void checkCycle(ConfigEntityId configEntityId, Set<String> childConfigNames) {
		if (childConfigNames.contains(configEntityId.toString())) {
			childConfigNames.add(configEntityId.toString());
			throw new IllegalArgumentException("Config hierarchy has cycle." + childConfigNames);
		}

		List<String> superConfigNamess = ciRepository.findSuperConfigNames(configEntityId);
		if (superConfigNamess.isEmpty()) {
			return;
		}

		Set<String> newSuperConfigNames = new HashSet<String>(childConfigNames);
		newSuperConfigNames.add(configEntityId.toString());

		String category = configEntityId.getCategory();

		for (String superConfigName : superConfigNamess) {
			checkCycle(new ConfigEntityId(category, superConfigName), newSuperConfigNames);
		}
	}

}
