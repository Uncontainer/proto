package com.pulsarang.mom.store.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.EventUtils;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.EventStore;

public abstract class DbEventStore implements EventStore {
	private static final Logger log = LoggerFactory.getLogger(DbEventStore.class);

	protected final QuerySet querySet;

	private final ConnectionPool connectionPool;

	final String name;

	public DbEventStore(String name, QuerySet querySet) {
		this.name = name;
		this.querySet = querySet;
		this.connectionPool = new ConnectionPool(name);
	}

	@Override
	public int count(final int status) {
		return (Integer) execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = connection.prepareStatement(querySet.count());
				selectPstmt.setInt(1, status);
				ResultSet rs = selectPstmt.executeQuery();
				try {
					rs.next();
					return rs.getInt(1);
				} finally {
					rs.close();
					selectPstmt.close();
				}
			}
		});
	}

	@Override
	public void insert(final Event event) {
		execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement insertPstmt = connection.prepareStatement(querySet.insert());
				try {
					insertPstmt.setString(1, event.toJson());
					insertPstmt.executeUpdate();
				} finally {
					insertPstmt.close();
				}

				return null;
			}
		});
	}

	@Override
	public int updateStatus(final long id, final int status) {
		return (Integer) execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement updatePstmt = connection.prepareStatement(querySet.updateStatus());
				try {
					updatePstmt.setInt(1, status);
					updatePstmt.setLong(2, id);
					return updatePstmt.executeUpdate();
				} finally {
					updatePstmt.close();
				}
			}
		});
	}

	@Override
	public List<EventEnvelope> extracts(final int status, final int count) {
		Object objEventEvnelopes = execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = connection.prepareStatement(querySet.list());
				selectPstmt.setInt(1, status);
				selectPstmt.setInt(2, count);
				List<EventEnvelope> elements = new ArrayList<EventEnvelope>();
				ResultSet rs = selectPstmt.executeQuery();
				try {
					while (rs.next()) {
						long id = rs.getLong(1);
						int status = rs.getInt(2);
						String message = rs.getString(3);
						Event event = EventUtils.fromJson(message);
						elements.add(new DbEventEnvelope(DbEventStore.this, event, id, status));
					}
				} finally {
					rs.close();
					selectPstmt.close();
				}

				return elements;
			}
		});

		@SuppressWarnings("unchecked")
		List<EventEnvelope> eventEvnelopes = (List<EventEnvelope>) objEventEvnelopes;
		return eventEvnelopes;
	}

	@Override
	public int delete(final long id) {
		return (Integer) execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement deletePstmt = connection.prepareStatement(querySet.delete());
				try {
					deletePstmt.setLong(1, id);
					return deletePstmt.executeUpdate();
				} finally {
					deletePstmt.close();
				}
			}
		});
	}

	@Override
	public void deleteAll() {
		execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement deletePstmt = connection.prepareStatement(querySet.deleteAll());
				try {
					return deletePstmt.executeUpdate();
				} finally {
					deletePstmt.close();
				}
			}
		});
	}

	@Override
	public void close() {
		connectionPool.close();
	}

	private Object execute(SqlCommand command) {
		return executeSqlCommand(command, true);
	}

	/**
	 * @param command
	 * @param retryOnFail
	 *            pool의 연결이 끊겼을 수 있으므로 SqlException 예외 발생 시 connection을 다시 맺은 후 재시도한다.
	 * @return
	 */
	private Object executeSqlCommand(SqlCommand command, boolean retryOnFail) {
		Connection connection = connectionPool.borrowConnection();
		try {
			if (retryOnFail) {
				try {
					return command.execute(connection);
				} catch (SQLException e) {
					connection = connectionPool.renewConnection(connection);
				}
			}

			return command.execute(connection);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connectionPool.returnConnection(connection);
		}
	}

	static interface SqlCommand {
		Object execute(Connection connection) throws SQLException;
	}

	protected abstract void init();

	protected abstract Connection createConnection();

	class ConnectionPool {
		BlockingQueue<Connection> allocationQueue;
		int createCount = 0;

		ConnectionPool(String name) {
		}

		Connection borrowConnection() {
			checkInitialized();

			synchronized (this) {
				if (allocationQueue.isEmpty() && createCount < 10) {
					try {
						createCount++;
						return createConnection();
					} catch (RuntimeException e) {
						createCount--;
						throw e;
					}
				}
			}

			try {
				Connection connection = allocationQueue.poll(3, TimeUnit.SECONDS);
				if (connection == null) {
					throw new IllegalStateException("ConnectionPool is busy.");
				}
				return connection;
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		void returnConnection(Connection connection) {
			checkInitialized();

			try {
				allocationQueue.put(connection);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		synchronized Connection renewConnection(Connection connection) {
			try {
				connection.close();
			} catch (SQLException ignore) {
				// do nothing
			}

			return createConnection();
		}

		public void close() {
			if (allocationQueue == null) {
				return;
			}

			for (Connection connection : allocationQueue) {
				try {
					connection.close();
				} catch (SQLException e) {
					log.info("[MOM] Fail to close connection.", e);
				}
			}
		}

		private synchronized void checkInitialized() {
			if (allocationQueue == null) {
				allocationQueue = new LinkedBlockingQueue<Connection>(10);
				init();
			}
		}
	}
}
