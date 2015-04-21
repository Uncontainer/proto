package com.yeon.lang.remote;

import com.yeon.lang.Class;
import com.yeon.lang.Resource;
import com.yeon.lang.ResourceServiceUtils;
import com.yeon.lang.impl.MapResource;
import com.yeon.lang.query.ResultSet;
import com.yeon.lang.query.ResultSetMetaData;
import com.yeon.lang.triple.Triple;
import yeon.query.ResourcePointer;
import yeon.query.Select;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcResultSet implements ResultSet {
	final Select command;
	final ResultSetWrapper rs;
	Class clazz;

	MapResource resource;
	Triple nextTriple;
	boolean eof;

	public static class ResultSetWrapper {
		public ResultSetWrapper(Connection con, Statement stmt, java.sql.ResultSet rs) {
			super();
			this.con = con;
			this.stmt = stmt;
			this.rs = rs;
		}

		public final Connection con;
		public final Statement stmt;
		public final java.sql.ResultSet rs;

		void close() throws SQLException {
			rs.close();
			stmt.close();
			con.close();
		}
	}

	public JdbcResultSet(Select command, ResultSetWrapper rs) {
		this.command = command;
		this.rs = rs;

		if (command.getTableFilter() != null) {
			String classId = command.getTableFilter().getResourceId();
			clazz = ResourceServiceUtils.getClassSafely(classId);
		}
	}

	@Override
	public Class getTye() {
		return clazz;
	}

	@Override
	public boolean next() {
		if (eof) {
			return false;
		}
		try {

			if (nextTriple == null) {
				if (!rs.rs.next()) {
					eof = true;
					return false;
				}

				String subject = rs.rs.getString(1);
				String propertyId = rs.rs.getString(2);
				String object = rs.rs.getString(3);

				nextTriple = new Triple(subject, propertyId, object);
			}

			// TODO clazz가 query에 명시된 경우와 그렇지 않은 경우를 구분하여 처리.
			resource = new MapResource(clazz.getId());
			resource.setId(nextTriple.getSubject());
			resource.setPropertyObjectById(nextTriple.getPropertyId(), nextTriple.getObject());

			while (rs.rs.next()) {
				String subject = rs.rs.getString(1);
				String propertyId = rs.rs.getString(2);
				String object = rs.rs.getString(3);

				if (subject.equals(resource.getId())) {
					resource.setPropertyObjectById(propertyId, object);
				} else {
					nextTriple = new Triple(subject, propertyId, object);
					return true;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		eof = true;

		return true;
	}

	@Override
	public Resource get() {
		return resource;
	}

	@Override
	public int getColumnCount() {
		return clazz.getProperties().size();
	}

	@Override
	public Object getObject(int columnIndex) {
		ResourcePointer resourcePointer = command.getFields().get(columnIndex - 1);
		return resource.getPropertyObjectById(resourcePointer.getResourceId());
	}

	@Override
	public ResultSetMetaData getMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
