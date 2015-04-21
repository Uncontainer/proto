package com.yeon.lang.remote;

import com.yeon.lang.Resource;
import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceService;
import com.yeon.lang.impl.MapResource;
import com.yeon.lang.query.ResultSet;
import com.yeon.lang.resource.BasicMapResourceService;
import com.yeon.remote.annotation.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import yeon.query.Select;
import yeon.query.SqlParser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@RemoteService(name = ResourceService.NAME)
public class RemoteMapResourceService implements ResourceService {
	@Autowired
	private BasicMapResourceService mapResourceService;

//	@Autowired
//	private MapClassService mapClassService;
//
//	@Autowired
//	private MapPropertyService mapPropertyService;

	private SqlParser sqlParser = new SqlParser();

	protected JdbcTemplate jdbcTemplate;

	protected DataSource dataSource;

	@javax.annotation.Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void add(Resource resource) {
		mapResourceService.add((MapResource) resource);
	}

	@Override
	public void modify(Resource resource) {
		mapResourceService.modify((MapResource) resource);
	}

	@Override
	public void remove(String resourceId) {
		mapResourceService.remove(resourceId);
	}

	@Override
	public Resource get(String resourceId) {
		return mapResourceService.get(resourceId);
	}

	@Override
	public ResultSet select(String query) {
		Select select = sqlParser.select(query);

		try {
			Connection con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(select.toSql());
			java.sql.ResultSet rs = pstmt.executeQuery();

			return new JdbcResultSet(select, new JdbcResultSet.ResultSetWrapper(con, pstmt, rs));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public String getId(ResourceGetCondition condition) {
		return mapResourceService.getId(condition);
	}
//
//	private MapResourceService getResourceService(ResourceType resourceType) {
//		if (resourceType == null) {
//			return mapResourceService;
//		}
//
//		switch (resourceType) {
//		case RESOURCE:
//			return mapResourceService;
//		case CLASS:
//			return mapClassService;
//		case PROPERTY:
//			return mapPropertyService;
//		default:
//			throw new IllegalArgumentException("Unsupported resource type.(" + resourceType + ")");
//		}
//	}
}
