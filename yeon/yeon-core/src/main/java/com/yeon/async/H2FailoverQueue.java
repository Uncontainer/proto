package com.yeon.async;

import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class H2FailoverQueue<T extends MapModel> extends AbstractFailoverQueue<T> {
	private final Logger log = LoggerFactory.getLogger(H2FailoverQueue.class);

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
	private final UncompletedItemProcessor uncompletedItemProcessor;

	private final Class<T> clazz;

	H2FailoverQueue(String name, Class<T> clazz) {
		this.clazz = clazz;
		this.connectionPool = new ConnectionPool(name);
		this.count = new AtomicInteger(count(Entry.STATUS_WAIT));

		this.uncompletedItemProcessor = new UncompletedItemProcessor(maxId());
		this.uncompletedItemProcessor.start();
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
				// TODO [LOW] Count 동기화 로직 필요할 수 있음.
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
	public List<Entry<T>> elements(int maxCount) {
		return extracts(maxCount);
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
				PreparedStatement selectPstmt = null;
				ResultSet rs = null;
				try {
					selectPstmt = connection.prepareStatement("SELECT count(id) FROM queue WHERE status=?");
					selectPstmt.setInt(1, status);
					rs = selectPstmt.executeQuery();
					rs.next();
					return rs.getInt(1);
				} finally {
					closeQuietly(rs);
					closeQuietly(selectPstmt);
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
					closeQuietly(insertPstmt);
				}

				return null;
			}
		});
	}

	private H2Entry<T> extract() {
		Object objEntity = execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				while (true) {
					PreparedStatement selectPstmt = null;
					ResultSet rs = null;
					List<H2Entry<T>> entries;
					try {
						selectPstmt = connection.prepareStatement("SELECT id, status, message FROM queue WHERE status=? ORDER BY id ASC LIMIT 2");
						selectPstmt.setInt(1, Entry.STATUS_WAIT);
						rs = selectPstmt.executeQuery();
						if (!rs.next()) {
							return null;
						}

						entries = new ArrayList<H2Entry<T>>(2);
						do {
							T model = MapModel.fromJson(rs.getString(3), clazz);
							entries.add(new H2Entry<T>(H2FailoverQueue.this, rs.getLong(1), rs.getInt(2), model));
						} while (rs.next());
					} finally {
						closeQuietly(rs);
						closeQuietly(selectPstmt);
					}

					PreparedStatement updatePstmt = null;
					try {
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
						closeQuietly(updatePstmt);
					}
				}
			}
		});

		@SuppressWarnings("unchecked")//
		H2Entry<T> entity = (H2Entry<T>)objEntity;

		return entity;
	}

	private H2Entry<T> extractRunning(final long minId, final long maxId) {
		Object objEntity = execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = null;
				ResultSet rs = null;
				try {
					selectPstmt = connection.prepareStatement("SELECT id, status, message FROM queue WHERE status=? AND ? < id AND id <= ? ORDER BY id ASC");
					selectPstmt.setInt(1, Entry.STATUS_RUNNING);
					selectPstmt.setLong(2, minId);
					selectPstmt.setLong(3, maxId);
					rs = selectPstmt.executeQuery();
					if (!rs.next()) {
						return null;
					}

					T model = MapModel.fromJson(rs.getString(3), clazz);
					return new H2Entry<T>(H2FailoverQueue.this, rs.getLong(1), rs.getInt(2), model);
				} finally {
					closeQuietly(rs);
					closeQuietly(selectPstmt);
				}
			}
		});

		@SuppressWarnings("unchecked")//
		H2Entry<T> entity = (H2Entry<T>)objEntity;

		return entity;
	}

	private List<Entry<T>> extracts(final int maxCount) {
		Object elements = execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = null;
				ResultSet rs = null;
				List<Entry<T>> elements = new ArrayList<Entry<T>>();
				try {
					selectPstmt = connection.prepareStatement("SELECT id, status, message FROM queue ORDER BY id ASC LIMIT ?");
					selectPstmt.setInt(1, maxCount);
					rs = selectPstmt.executeQuery();

					while (rs.next()) {
						T model = MapModel.fromJson(rs.getString(3), clazz);
						elements.add(new H2Entry<T>(H2FailoverQueue.this, rs.getLong(1), rs.getInt(2), model));
					}
				} finally {
					closeQuietly(rs);
					closeQuietly(selectPstmt);
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
					closeQuietly(deletePstmt);
				}
			}
		});
	}

	private long maxId() {
		return (Long)execute(new SqlCommand() {
			@Override
			public Object execute(Connection connection) throws SQLException {
				PreparedStatement selectPstmt = null;
				ResultSet rs = null;
				try {
					selectPstmt = connection.prepareStatement("SELECT max(id) FROM queue");
					rs = selectPstmt.executeQuery();

					while (rs.next()) {
						return rs.getLong(1);
					}
				} finally {
					closeQuietly(rs);
					closeQuietly(selectPstmt);
				}

				return 0L;
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
					closeQuietly(deletePstmt);
				}

				return null;
			}
		});
	}

	@Override
	public void close() {
		connectionPool.close();
		uncompletedItemProcessor.stop();
	}

	private Object execute(SqlCommand command) {
		return executeSqlCommand(command, true);
	}

	private static void closeQuietly(ResultSet rs) {
		if (rs == null) {
			return;
		}

		try {
			rs.close();
		} catch (SQLException ignore) {
		}
	}

	private static void closeQuietly(Statement stmt) {
		if (stmt == null) {
			return;
		}

		try {
			stmt.close();
		} catch (SQLException ignore) {
		}
	}

	private static void closeQuietly(Connection con) {
		if (con == null) {
			return;
		}

		try {
			con.close();
		} catch (SQLException ignore) {
		}
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

	/**
	 * 
	 * @author pulsarang
	 */
	private static interface SqlCommand {
		Object execute(Connection connection) throws SQLException;
	}

	/**
	 * 
	 * @author pulsarang
	 */
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
			closeQuietly(connection);

			createCount--;
			return createConnection();
		}

		public void close() {
			if (allocationQueue == null) {
				return;
			}

			for (Connection connection : allocationQueue) {
				closeQuietly(connection);
			}
		}

		private synchronized void checkInitialized() {
			if (allocationQueue == null) {
				allocationQueue = new LinkedBlockingQueue<Connection>(10);
				Connection conn = null;
				Statement stmt = null;
				try {
					Class.forName("org.h2.Driver");
					conn = createConnection();
					stmt = conn.createStatement();
					stmt.execute(TABLE_SCHEMA);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					closeQuietly(stmt);
					closeQuietly(conn);
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

	/**
	 * 
	 * @author pulsarang
	 * @param <E>
	 */
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

	/**
	 * @author EC셀러서비스개발팀
	 */
	private class UncompletedItemProcessor implements Runnable {
		private final long maxId;
		private Thread thread;
		private volatile boolean stop;

		public UncompletedItemProcessor(long maxId) {
			super();
			this.maxId = maxId;
		}

		public void start() {
			thread = new Thread(this, "mom-uncompleted-item-processor");
			thread.start();
		}

		public void stop() {
			thread.interrupt();
			stop = true;
		}

		@Override
		public void run() {
			long minId = 0L;

			// item을 수신할 서비스가 초기화되기 위한 시간을 준다.
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				return;
			}

			while (!stop) {
				H2Entry<T> entry = extractRunning(minId, maxId);
				if (entry == null) {
					break;
				}

				try {
					if (uncompletedItemListener != null) {
						uncompletedItemListener.uncompletedItemDetected(entry.getMessage());
					} else if (removedItemListener != null) {
						removedItemListener.itemRemoved(entry.getMessage());
					} else {
						log.info("[YEON] Uncompleted queue item removed.({})", entry.getMessage());
					}
				} catch (Exception e) {
					log.error("[MON] Fail to process uncompleted item.({})", entry.getMessage());
				} finally {
					entry.ack();
				}

				minId = entry.id;
			}
		}
	}
}
