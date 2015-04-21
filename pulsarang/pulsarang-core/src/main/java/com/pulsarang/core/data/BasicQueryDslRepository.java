package com.pulsarang.core.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.dml.DeleteClause;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.PathBuilderFactory;

// TODO QueryDslRepositorySupport로 교체하여야 함. 
public class BasicQueryDslRepository {
	@PersistenceContext
	private EntityManager entityManager;
	private PathBuilderFactory builderFactory = new PathBuilderFactory();

	// @Required
	// public void setEntityManager(EntityManager entityManager) {
	//
	// Assert.notNull(entityManager);
	// this.entityManager = entityManager;
	// }

	protected JPQLQuery from(EntityPath<?>... paths) {

		return new JPAQuery(entityManager).from(paths);
	}

	protected DeleteClause<JPADeleteClause> delete(EntityPath<?> path) {

		return new JPADeleteClause(entityManager, path);
	}

	protected UpdateClause<JPAUpdateClause> update(EntityPath<?> path) {

		return new JPAUpdateClause(entityManager, path);
	}

	protected <T> PathBuilder<T> getBuilder(Class<T> type) {

		return builderFactory.create(type);
	}
}
