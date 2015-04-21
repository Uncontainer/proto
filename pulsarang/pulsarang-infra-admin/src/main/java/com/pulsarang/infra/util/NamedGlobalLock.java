package com.pulsarang.infra.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class NamedGlobalLock {
	private final Logger log = LoggerFactory.getLogger(NamedGlobalLock.class);

	public static final int LOCK_NAME_MAX_LENGTH = 50;

	@Resource(name = "dataSourceForLock")
	private DataSource dataSource;

	/**
	 * 주어진 이름의 lock을 가져온다. lock을 이용한 작업을 마쳤을 경우 반드시 {@link NamedGlobalLock#unlock(String)}을 호출한다.
	 * 
	 * @param name lock 이름
	 * @param maxLockTimeInMillie 밀리초 단위의 최대 lock 유지 시간. 0보다 같거나 작을 경우 시간 제한을 두지 않는다.
	 * @return lock을 성공적으로 가져왔을 경우 true, 그렇지 않을 경우 false
	 * @throws SQLException 
	 * @see NamedGlobalLock#unlock(String)
	 * @throws IllegalArgumentException name이 비었거나 길이가 {@link LockInfoBOProxy#LOCK_NAME_MAX_LENGTH}보다 클 경우.
	 */
	public boolean tryLock(String name, long maxLockTimeInMillie) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Empty lock name.");
		}
		if (name.length() > LOCK_NAME_MAX_LENGTH) {
			throw new IllegalArgumentException("Too long lock name.");
		}

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<LockEntity> list = jdbcTemplate.query("SELECT lck_ymdt, lck_max_dr FROM nm_lck WHERE lck_nm=?", new Object[] {name}, new RowMapper<LockEntity>() {
			@Override
			public LockEntity mapRow(ResultSet rs, int arg1) throws SQLException {
				LockEntity lock = new LockEntity();
				lock.setLockTime(rs.getTimestamp(1));
				lock.setMaxLockDuration(rs.getLong(2));

				return lock;
			}
		});

		if (!list.isEmpty()) {
			LockEntity lock = list.get(0);
			if ((System.currentTimeMillis() - lock.getLockTime().getTime()) > lock.getMaxLockDuration()) {
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

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("DELETE FROM nm_lck WHERE lck_nm=?", name);
	}
}