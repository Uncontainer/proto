package com.pulsarang.mom;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.pulsarang.mom.common.cluster.Cluster;
import com.pulsarang.mom.common.cluster.DbInfo;

public class AbstractDBAccessAsyncTestCase extends AbstractAsyncTestCase implements InitializingBean {
	@Resource(name = "mainDataSource")
	private DataSource mainDataSource;

	@Resource(name = "logDataSource")
	private DataSource logDataSource;

	@Override
	public void afterPropertiesSet() throws Exception {
		truncateTables();
	}

	protected void truncateTables() {
		truncateEventTables();
		truncateLogTables();
	}

	protected void truncateEventTables() {
		DbInfo dbInfo = cluster.getDbInfo(Cluster.DST_MAIN);
		if (!dbInfo.getConnectionUrl().contains("114.111.34.51")) {
			throw new IllegalStateException();
		}

		Connection connection = null;
		try {
			connection = mainDataSource.getConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE me2_event_1");
			stmt.executeUpdate("TRUNCATE TABLE me2_event_process_schedule_1");
			stmt.executeUpdate("TRUNCATE TABLE me2_event_2");
			stmt.executeUpdate("TRUNCATE TABLE me2_event_process_schedule_2");

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void truncateLogTables() {
		DbInfo dbInfo = cluster.getDbInfo(Cluster.DST_LOG);
		if (!dbInfo.getConnectionUrl().contains("114.111.34.51")) {
			throw new IllegalStateException();
		}

		Connection connection = null;
		try {
			connection = logDataSource.getConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE me2_event_log");
			stmt.executeUpdate("TRUNCATE TABLE me2_event_process_fail_log");

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected int getEventTableRecordCount(boolean isEvent) {
		int count = 0;
		Connection connection = null;
		try {
			connection = mainDataSource.getConnection();
			Statement stmt = connection.createStatement();

			String tableName = isEvent ? "me2_event_1" : "me2_event_process_schedule_1";
			ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + tableName);
			rs.next();
			count = rs.getInt(1);
			rs.close();

			tableName = isEvent ? "me2_event_2" : "me2_event_process_schedule_2";
			rs = stmt.executeQuery("SELECT count(*) FROM " + tableName);
			rs.next();
			count += rs.getInt(1);
			rs.close();

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return count;
	}
}
