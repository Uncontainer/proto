package com.yeon.infra.util;

import com.yeon.lang.AbstractYeonDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Component
public class NamedGlobalLock extends AbstractYeonDao {
	private final Logger log = LoggerFactory.getLogger(NamedGlobalLock.class);

	public static final int LOCK_NAME_MAX_LENGTH = 50;

	/**
	 * 주어진 이름의 lock을 가져온다. lock을 이용한 작업을 마쳤을 경우 반드시 {@link NamedGlobalLock#unlock(String)}을 호출한다.
	 * 
	 * @param name
	 *            lock 이름
	 * @param maxLockTimeInMillie
	 *            밀리초 단위의 최대 lock 유지 시간. 0보다 같거나 작을 경우 시간 제한을 두지 않는다.
	 * @return lock을 성공적으로 가져왔을 경우 true, 그렇지 않을 경우 false
	 * @throws SQLException
	 * @see NamedGlobalLock#unlock(String)
	 * @throws IllegalArgumentException
	 *             name이 비었거나 길이가 {@link LockInfoBOProxy#LOCK_NAME_MAX_LENGTH}보다 클 경우.
	 */
	public boolean tryLock(String name, long maxLockTimeInMillie) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Empty lock name.");
		}
		if (name.length() > LOCK_NAME_MAX_LENGTH) {
			throw new IllegalArgumentException("Too long lock name.");
		}

		Entry entry = jdbcTemplate.query("SELECT lck_ymdt, lck_max_dr FROM nm_lck WHERE lck_nm=?", new Object[] { name },
				new ResultSetExtractor<Entry>() {
					@Override
					public Entry extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							return new Entry(rs.getTimestamp(1), rs.getLong(2));
						} else {
							return null;
						}
					}
				});

		if (entry != null) {
			if ((System.currentTimeMillis() - entry.lockTime.getTime()) > entry.maxLockDuration) {
				jdbcTemplate.update("DELETE FROM nm_lck WHERE lck_nm=?", name);
			} else {
				return false;
			}
		}

		try {
			int updateCount = jdbcTemplate.update("INSERT INTO nm_lck(lck_nm, lck_ymdt, lck_max_dr) VALUES(?, ?, ?)", name, new Date(), maxLockTimeInMillie);
			return updateCount > 0;
		} catch (DuplicateKeyException e) {
			return false;
		} catch (Exception e) {
			log.error("Fail to get lock.", e);
			return false;
		}
	}

	private static class Entry {
		final Date lockTime;
		final long maxLockDuration;

		private Entry(Date lockTime, long maxLockDuration) {
			super();
			this.lockTime = lockTime;
			this.maxLockDuration = maxLockDuration;
		}
	}

	/**
	 * 주어진 이름의 lock을 해제한다.
	 * 
	 * @see NamedGlobalLock#tryLock(String, long)
	 * @param name
	 */
	public void unlock(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Empty lock name.");
		}

		jdbcTemplate.update("DELETE FROM nm_lck WHERE lck_nm=?", name);
	}
}