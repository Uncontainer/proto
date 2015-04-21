package com.pulsarang.mom.common.cluster;

import com.pulsarang.infra.config.Config;

public class DbInfo extends Config {
	public static final String DB_CONNECTION_URL = "db_connection_url";
	public static final String DB_USERNAME = "db_username";
	public static final String DB_PASSWORD = "db_password";
	public static final String DB_MAX_ACTIVE = "db_max_active";
	public static final String DB_MIN_IDLE = "db_min_idle";

	public static final String DB_EVENT_TABLE_COUNT = "db_event_table_count";

	public String getConnectionUrl() {
		return getString(DB_CONNECTION_URL);
	}

	public String Username() {
		return getString(DB_USERNAME);
	}

	public String getPassword() {
		return getString(DB_PASSWORD);
	}

	public int getMaxActive() {
		return getInteger(DB_MAX_ACTIVE);
	}

	public int getMinIdle() {
		return getInteger(DB_MIN_IDLE);
	}

	public int getEventTableCount() {
		return getInteger(DB_EVENT_TABLE_COUNT, 1);
	}
}
