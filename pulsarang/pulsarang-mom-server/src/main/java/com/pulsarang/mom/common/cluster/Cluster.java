/*
 * ClusterInfo.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.mom.common.cluster;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.config.ConfigContext;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.mom.MomInfo;
import com.pulsarang.mom.common.MomConfigCategory;

/**
 * 
 * @author pulsarang
 */
@Component
public class Cluster implements InitializingBean {
	private static final Logger LOG = LoggerFactory.getLogger(Cluster.class);

	public static final String DST_MAIN = "main";
	public static final String DST_FAILOVER = "failover";
	public static final String DST_LOG = "log";

	private String clusterId;
	private volatile int eventTableCount;

	@Autowired
	private InfraContext infraContext;

	private ClusterInfo clusterInfo;

	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigContext configContext = infraContext.getConfigContext();

		if (true) {
			clusterInfo = new ClusterInfo();
			return;
		}

		String clusterId = getClusterIdFromConfiguration();
		if (clusterId == null) {
			// Unit Test용 cluster
			ConfigId unitTestClusterConfigurationId = new ConfigId(MomConfigCategory.CLUSTER, "cluster-0");
			if (configContext.get(unitTestClusterConfigurationId) != null) {
				clusterId = "cluster-0";
			}
		}

		// this.clusterId = "cluster-1";

		if (clusterId == null) {
			throw new IllegalStateException("Fail to find cluster. (" + LocalAddressHolder.getLocalAddress() + ")");
		}

		this.clusterId = clusterId;
		ConfigId configurationId = new ConfigId(MomConfigCategory.CLUSTER, clusterId);
		clusterInfo = configContext.get(configurationId);

		LOG.info("Cluster initialized. ({})", clusterId);
	}

	public ClusterInfo getClusterInfo() {
		return clusterInfo;
	}

	public String getClusterId() {
		return clusterId;
	}

	private String getClusterIdFromConfiguration() {
		ConfigContext configContext = infraContext.getConfigContext();

		MomInfo mqInfo = configContext.get(new ConfigId(MomConfigCategory.MOM, "mom"));
		Collection<String> clusterNos = mqInfo.getClusterNos();
		if (clusterNos == null || clusterNos.isEmpty()) {
			throw new IllegalStateException("Cluster information does not exist.");
		}

		String localAddress = LocalAddressHolder.getLocalAddress();
		for (String clusterNo : clusterNos) {
			String tempClusterId = "cluster-" + clusterNo;
			ConfigId tempConfigurationId = new ConfigId(MomConfigCategory.CLUSTER, tempClusterId);
			ClusterInfo clusterInfo = configContext.get(tempConfigurationId);
			if (clusterInfo == null) {
				LOG.warn("Cluster '{}' configuration does not exist.", tempClusterId);
				continue;
			}

			Collection<String> serverIps = clusterInfo.getClusterServerIps();
			if (serverIps.contains(localAddress)) {
				return tempClusterId;
			}
		}

		return null;
	}

	public int getDbSharingServerCount() {
		return clusterInfo.getClusterServerIps().size();
	}

	public int getDbNo(String dataSourceType) {
		if (DST_MAIN.equals(dataSourceType)) {
			return getClusterInfo().getInteger(ClusterInfo.CLUSTER_MAIN_DB_NO);
		} else if (DST_FAILOVER.equals(dataSourceType)) {
			return getClusterInfo().getInteger(ClusterInfo.CLUSTER_FAILOVER_DB_NO);
		} else if (DST_LOG.equals(dataSourceType)) {
			return getClusterInfo().getInteger(ClusterInfo.CLUSTER_LOG_DB_NO);
		} else {
			throw new IllegalArgumentException("Unknown datasource type: " + dataSourceType);
		}
	}

	public DbInfo getDbInfo(String dataSourceType) {
		String dbId = "db-" + getDbNo(dataSourceType);
		ConfigContext configContext = infraContext.getConfigContext();

		return configContext.get(new ConfigId(MomConfigCategory.DB, dbId));
	}

	public ClusterStatus getClusterStatus() {
		return clusterInfo.getClusterStatus();
	}

	public void setClusterStatus(ClusterStatus clusterStatus) {
		clusterInfo.setClusterStatus(clusterStatus);
	}

	public int getEventTableCount() {
		if (eventTableCount == 0) {
			eventTableCount = getDbInfo(DST_MAIN).getEventTableCount();
		}

		return eventTableCount;
	}
}
