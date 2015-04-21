package com.pulsarang.core.data;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathBuilder;

public abstract class BaseCustomRepository<T, ID extends Serializable> {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	@PersistenceContext
	protected EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Autowired(required = false)
	private SqlMapClientTemplate sqlMapClientTemplate;

	protected SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public BaseCustomRepository() {
		super();
	}

	protected JPAQuery createJPAQuery() {
		return new JPAQuery(entityManager);
	}

	protected void setIndexHint(JPAQuery jpaQuery, String string) {
		//TODO implement this
	}

	public void detach(T entity) {
		entityManager.detach(entity);
	}

	protected <T> Page<T> getPageableElements(JPAQuery baseQuery, Path<T> entityPath, Pageable pageable) {
		JPQLQuery listQuery = applyPagination(baseQuery, pageable, entityPath);

		return new PageImpl<T>(listQuery.list(entityPath), pageable, baseQuery.count());
	}

	protected <T> Page<T> getPageableElements(JPAQuery baseQuery, Expression countExpression, Path<T> entityPath, Pageable pageable) {
		JPQLQuery listQuery = applyPagination(baseQuery, pageable, entityPath);

		return new PageImpl<T>(listQuery.list(entityPath), pageable, (Long)baseQuery.uniqueResult(countExpression));
	}

	protected <T> JPAQuery applyPagination(JPAQuery query, Pageable pageable, Path<T> entityPath) {
		if (pageable == null) {
			return query.clone(entityManager);
		}

		JPAQuery listQuery = query.clone(entityManager);
		listQuery.offset(pageable.getOffset());
		listQuery.limit(pageable.getPageSize());
		return applySort(listQuery, pageable, entityPath);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private <T> JPAQuery applySort(JPAQuery query, Pageable pageable, Path<T> entityPath) {
		// TODO orderBy할 파라미터 검사 로직 필요
		Sort sort = pageable.getSort();
		if (sort == null) {
			return query;
		}
		PathBuilder<T> builder = new PathBuilder<T>(entityPath.getType(), entityPath.getMetadata());
		for (Sort.Order order : sort) {
			Expression<Object> property = builder.get(order.getProperty());
			query.orderBy(new OrderSpecifier(order.isAscending() ? com.mysema.query.types.Order.ASC
						: com.mysema.query.types.Order.DESC, property));
		}

		return query;
	}
}