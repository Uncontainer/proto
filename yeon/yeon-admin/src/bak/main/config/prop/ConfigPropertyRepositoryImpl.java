package com.pulsarang.infra.config.prop;

import com.mysema.query.jpa.impl.JPAQuery;
import com.pulsarang.core.data.BaseCustomRepository;
import com.pulsarang.infra.config.ConfigEntityId;

import javax.persistence.Query;
import java.util.List;

public class ConfigPropertyRepositoryImpl extends BaseCustomRepository<ConfigProperty, ConfigPropertyId> implements ConfigPropertyRepositoryCustom {
	QConfigProperty configProperty = QConfigProperty.configProperty;

	@Override
	public List<ConfigProperty> findConfigProperties(ConfigEntityId id) {
		JPAQuery query = createJPAQuery().from(configProperty).where(configProperty.id.configCategory.eq(id.getCategory()),
				configProperty.id.configName.eq(id.getName()));

		return query.list(configProperty);
	}

	@Override
	public int deleteAll(ConfigEntityId configEntityId) {
		Query query = entityManager.createQuery("DELETE FROM ConfigProperty t "
				+ "WHERE t.id.configName = :configName AND t.id.configCategory = :configCategory");
		query.setParameter("configName", configEntityId.getName());
		query.setParameter("configCategory", configEntityId.getCategory());
		return query.executeUpdate();
	}
}
