package com.pulsarang.infra.mom.util.async;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;

public class H2FailoverQueue<T extends MapModel> implements FailoverQueue<T> {
	private static final Logger log = LoggerFactory.getLogger(H2FailoverQueue.class);

	static final String DB_PATH = ".nmp/mom/db/";

	private final static String TABLE_SCHEMA = "CREATE TABLE IF NOT EXISTS queue ("
		+ " id BIGINT AUTO_INCREMENT PRIMARY KEY"
		+ " , status INT NOT NULL default " + Entry.STATUS_WAIT + ""
		+ " , message VARCHAR2(65000) NOT NULL"
		+ ")\n";

	/** Main lock guarding all access */
	private final ReentrantLock takeLock = new ReentrantLock();
	/** Lock held by put, offer, etc */
	private final ReentrantLock putLock = new ReentrantLock();
	/** Condition for waiting takes */
	private final Condition notEmpty = takeLock.newCondition();

	private final AtomicInteger count;

	private final ConnectionPool connectionPool;

	private final Class<T> clazz;

	H2FailoverQueue(String name, Class<T> clazz) {
		this.clazz = clazz;
		this.connectionPool = new ConnectionPool(name);
		this.count = new AtomicInteger(count(Entry.STATUS_WAIT));
	}

	@Override
	public void put(T model) throws InterruptedException {
		if (model == null) {
			throw new IllegalArgumentException("Null message.");
		}

		int c;
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			insert(model.toJson());
			c = count.getAndIncrement();
		} finally {
			putLock.unlock();
		}

		if (c == 0) {
			final ReentrantLock takeLock = this.takeLock;
			takeLock.lock();
			try {
				notEmpty.signal();
			} finally {
				takeLock.unlock();
			}
		}
	}

	@Override
	public Entry<T> take() throws InterruptedException {
		Entry<T> item;
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			try {
				while (count.get() == 0) {
					notEmpty.await();
				}
			} catch (InterruptedException ie) {
				notEmpty.signal(); // propagate to a non-interrupted thread
				throw ie;
			}

			item = extract();
			int c = -1;
			if (item != null) {
				c = count.getAndDecrement();
			} else {
				// TODO Count 동기화 로직 필요할 수 있음.
			}

			if (c > 1) {
				notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}

		return item;
	}

	@Override
	public void clear() {
		putLock.lock();
		takeLock.lock();
		try {
			deleteAll();
			count.set(0);
		} finally {
			putLock.unlock();
			takeLock.unlock();
		}
	}

	public int remove(long id) {
		return delete(id);
	}

	@Override
	public List<Entry<T>> elements() {
		return extracts();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return count.get();
	}

	private int count(final int status) {
		return (Integer)execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = connection.prepareStatement("SELECT count(id) FROM queue WHERE status=?");
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

	private void insert(final String message) {
		execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement insertPstmt = connection.prepareStatement("INSERT INTO queue(message) VALUES(?)");
				try {
					insertPstmt.setString(1, message);
					insertPstmt.executeUpdate();
				} finally {
					insertPstmt.close();
				}

				return null;
			}
		});
	}

	private Entry<T> extract() {
		Object objEntity = execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				while (true) {
					PreparedStatement updatePstmt = null;
					PreparedStatement selectPstmt = connection.prepareStatement("SELECT id, status, message FROM queue WHERE status=? ORDER BY id ASC LIMIT 2");
					selectPstmt.setInt(1, Entry.STATUS_WAIT);
					try {
						ResultSet rs = selectPstmt.executeQuery();
						if (!rs.next()) {
							rs.close();
							return null;
						}

						List<H2Entry<T>> entries = new ArrayList<H2Entry<T>>(2);
						do {
							T model = MapModel.fromJson(rs.getString(3), clazz);
							entries.add(new H2Entry<T>(H2FailoverQueue.this, rs.getLong(1), rs.getInt(2), model));
						} while (rs.next());
						rs.close();

						updatePstmt = connection.prepareStatement("UPDATE queue SET status=? WHERE id=?");
						for (H2Entry<T> entry : entries) {
							updatePstmt.setInt(1, Entry.STATUS_RUNNING);
							updatePstmt.setLong(2, entry.getId());
							int updatedCount = updatePstmt.executeUpdate();
							if (updatedCount != 0) {
								return entry;
							}
						}
					} finally {
						selectPstmt.close();
						if (updatePstmt != null) {
							updatePstmt.close();
						}
					}
				}
			}
		});

		@SuppressWarnings("unchecked")//
		Entry<T> entity = (Entry<T>)objEntity;

		return entity;
	}

	private List<Entry<T>> extracts() {
		Object elements = execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = connection.prepareStatement("SELECT id, status, message FROM queue ORDER BY id ASC");
				List<Entry<T>> elements = new ArrayList<Entry<T>>();
				ResultSet rs = selectPstmt.executeQuery();
				try {
					while (rs.next()) {
						T model = MapModel.fromJson(rs.getString(3), clazz);
						elements.add(new H2Entry<T>(H2FailoverQueue.this, rs.getLong(1), rs.getInt(2), model));
					}
				} finally {
					rs.close();
					selectPstmt.close();
				}

				return elements;
			}
		});

		@SuppressWarnings("unchecked")// 
		List<Entry<T>> entries = (List<Entry<T>>)elements;
		return entries;
	}

	private int delete(final long id) {
		return (Integer)execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement deletePstmt = connection.prepareStatement("DELETE FROM queue WHERE id=?");
				try {
					deletePstmt.setLong(1, id);
					return deletePstmt.executeUpdate();
				} finally {
					deletePstmt.close();
				}
			}
		});
	}

	private void deleteAll() {
		execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement deletePstmt = connection.prepareStatement("TRUNCATE TABLE queue");
				try {
					deletePstmt.executeUpdate();
				} finally {
					deletePstmt.close();
				}

				return null;
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
	 * @param retryOnFail pool의 연결이 끊겼을 수 있으므로 SqlException 예외 발생 시 connection을 다시 맺은 후 재시도한다.
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

	private static interface SqlCommand {
		Object execute(Connection connection) throws SQLException;
	}

	public static final int dropDatabase(final String dbName) {
		File dir = new File(System.getProperty("user.home"), DB_PATH);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.startsWith(dbName) && fileName.endsWith(".db");
			}
		};

		File[] files = dir.listFiles(filter);
		if (files == null) {
			return 0;
		}

		for (File file : files) {
			file.delete();
		}

		return files.length;
	}

	private static class ConnectionPool {
		BlockingQueue<Connection> allocationQueue;
		int createCount = 0;
		String url;

		ConnectionPool(String name) {
			url = "jdbc:h2:~/" + DB_PATH + name;
		}

		Connection borrowConnection() {
			checkInitialized();

			synchronized (this) {
				if (allocationQueue.isEmpty() && createCount < 10) {
					return createConnection();
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

			createCount--;
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
				try {
					Class.forName("org.h2.Driver");
					Connection conn = createConnection();
					Statement stmt = conn.createStatement();
					stmt.execute(TABLE_SCHEMA);
					stmt.close();
					conn.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				createCount--;
			}
		}

		private Connection createConnection() {
			Connection conn;
			try {
				conn = DriverManager.getConnection(url, "sa", "");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			createCount++;
			return conn;
		}
	}

	private static class H2Entry<E extends MapModel> implements Entry<E> {
		H2FailoverQueue<E> queue;
		long id;
		int status;
		E model;

		H2Entry(H2FailoverQueue<E> queue, long id, int status, E model) {
			super();
			this.queue = queue;
			this.id = id;
			this.status = status;
			this.model = model;
		}

		long getId() {
			return id;
		}

		@Override
		public E getMessage() {
			return model;
		}

		@Override
		public int getStatus() {
			return status;
		}

		@Override
		public String toString() {
			return id + ":" + status + ":" + model;
		}

		@Override
		public int ack() {
			return queue.remove(id);
		}
	}
}
