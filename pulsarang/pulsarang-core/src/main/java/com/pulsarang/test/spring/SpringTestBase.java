package com.pulsarang.test.spring;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.pulsarang.test.TestBase;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@Ignore
@Transactional
@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/root-context.xml"}/*, loader = XmlWebApplicationContextLoader.class*/)
public class SpringTestBase extends TestBase {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	protected EntityManager entityManager;

	@BeforeClass
	public static void setLogLevel() {
		setLogLevel(Level.INFO);
	}

	protected static void setLogLevel(Level level) {
		LoggerContext ctx = (LoggerContext)LoggerFactory.getILoggerFactory();
		List<ch.qos.logback.classic.Logger> loggers = ctx.getLoggerList();

		for (ch.qos.logback.classic.Logger logger : loggers) {
			if (logger.getLevel() != null) {
				logger.setLevel(level);
			}
		}
	}
}
