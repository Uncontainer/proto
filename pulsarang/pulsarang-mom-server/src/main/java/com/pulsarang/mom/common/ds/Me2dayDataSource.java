/*
 * Me2dayDataSource.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.mom.common.ds;


import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.mom.common.cluster.Cluster;
import com.pulsarang.mom.common.cluster.DbInfo;

/**
 * 
 * @author pulsarang
 */
public class Me2dayDataSource extends BasicDataSource {
	private static final Logger LOG = LoggerFactory.getLogger(Me2dayDataSource.class);

	public Me2dayDataSource(Cluster clusterInfo, String dataSourceType) {
		DbInfo configuration = clusterInfo.getDbInfo(dataSourceType);
		if (configuration == null) {
			throw new IllegalStateException("Fail to find db configuration. (" + dataSourceType + ")");
		}

		super.setDriverClassName("com.mysql.jdbc.Driver");
		super.setUrl(configuration.getConnectionUrl());
		super.setUsername(configuration.Username());
		super.setPassword(configuration.getPassword());
		super.setMaxActive(configuration.getMaxActive());
		super.setMinIdle(configuration.getMinIdle());

		super.setTestOnBorrow(false);
		super.setTestWhileIdle(true);
		super.setValidationQuery("SELECT 1");
		super.setMaxWait(10000);
		super.setPoolPreparedStatements(false);
		super.setMaxOpenPreparedStatements(1024);
		super.setTimeBetweenEvictionRunsMillis(30000);
		super.setNumTestsPerEvictionRun(5);
		super.setMinEvictableIdleTimeMillis(-1);

		StringBuilder sb = new StringBuilder();
		sb.append("=================================================\n");
		sb.append("url: ").append(super.getUrl()).append("\n");
		sb.append("maxActive: ").append(super.getMaxActive()).append("\n");
		sb.append("minIdle: ").append(super.getMinIdle()).append("\n");
		sb.append("testOnBorrow: ").append(super.getTestOnBorrow()).append("\n");
		sb.append("testWhileIdle: ").append(super.getTestWhileIdle()).append("\n");
		sb.append("maxWait: ").append(super.getMaxWait()).append("\n");
		sb.append("poolPreparedStatements: ").append(false).append("\n");
		sb.append("maxOpenPreparedStatements: ").append(super.getMaxOpenPreparedStatements()).append("\n");
		sb.append("=================================================\n");

		LOG.info("Create datasource '{}'\n{}", dataSourceType, sb.toString());
	}
}
