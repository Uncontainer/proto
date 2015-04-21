/*
 * @(#)BaseCustomRepository.java $version 2011. 3. 22.
 *
 * Copyright 2011 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.mage4j.core.mage.core.model.resource;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

/**
 * @author EC회원서비스개발팀
 * @param <T>
 * @param <ID>
 */
public abstract class BaseCustomRepository<T, ID extends Serializable> {
	@PersistenceContext
	protected EntityManager entityManager;

	public BaseCustomRepository() {
		super();
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected JPAQuery createJPAQuery() {
		return new JPAQuery(entityManager);
	}

	//	protected void setIndexHint(JPAQuery jpaQuery, String string) {
	//		//TODO implement this
	//	}
	//
	//	protected void setCache(JPAQuery jpaQuery, CacheRegion second5) {
	//		//TODO implement this
	//	}
	//
	//	public void detach(T entity) {
	//		entityManager.detach(entity);
	//	}
	//
	//	@SuppressWarnings("unchecked")
	//	public T getReference(ID id) {
	//		Class<?> clazz = GenericUtils.getFirstGenericParameterClass(this.getClass());
	//		return entityManager.getReference((Class<T>)clazz, id);
	//	}
	//
	//	protected Path<?> getPathByPropertyName(Object object, String propertyName) {
	//		try {
	//			return (Path<?>)object.getClass().getField(propertyName).get(object);
	//		} catch (Exception e) {
	//			throw new NMPRepositoryException(e);
	//		}
	//	}
	//
	//	/**
	//	 * 페이징을 적용(applyPagination 함수)한 결과는 현재 페이지에 따라 다르다.
	//	 * 1 페이지의 경우, rownum 제한이 적기 때문에 select구문이 2개가 반환되고,
	//	 * 2 페이지 이후의 경우, rownum 제한이 추가되어 select구문이 3개로 반환된다.
	//	 * 리스트 조회 쿼리에 oracle hint를 적용할 필요가 생겼는데, baseQuery를 가지고 리스트/카운트를 체크할 때
	//	 * select 구문이 달라 일관적인 hint 적용이 어렵다.
	//	 * 본 함수에서는 list 조회용, count 조회용 쿼리를 각각 인자로 받아 각각 수행을 한다.
	//	 * 각 쿼리에 hint는 개별적으로 설정할 수 있다.
	//	 */
	//	protected <T> Page<T> getPageableElements(JPAQuery baseQuery, JPAQuery countQuery, Path<T> entityPath, Pageable pageable) {
	//		JPQLQuery listQuery = applyPagination(baseQuery, pageable, entityPath);
	//
	//		return new PageImpl<T>(listQuery.list(entityPath), pageable, countQuery.count());
	//	}
	//
	//	protected <T> Page<T> getPageableElements(JPAQuery baseQuery, Path<T> entityPath, Pageable pageable) {
	//		JPQLQuery listQuery = applyPagination(baseQuery, pageable, entityPath);
	//
	//		return new PageImpl<T>(listQuery.list(entityPath), pageable, baseQuery.count());
	//	}
	//
	//	protected <T> Page<T> getPageableElements(JPAQuery baseQuery, Expression countExpression, Path<T> entityPath, Pageable pageable) {
	//		JPQLQuery listQuery = applyPagination(baseQuery, pageable, entityPath);
	//
	//		return new PageImpl<T>(listQuery.list(entityPath), pageable, (Long)baseQuery.uniqueResult(countExpression));
	//	}
	//
	//	protected <T> JPAQuery applyPagination(JPAQuery query, Pageable pageable, Path<T> entityPath) {
	//		if (pageable == null) {
	//			return query.clone(entityManager);
	//		}
	//
	//		JPAQuery listQuery = query.clone(entityManager);
	//		listQuery.offset(pageable.getOffset());
	//		listQuery.limit(pageable.getPageSize());
	//		return applySort(listQuery, pageable, entityPath);
	//	}
	//
	//	@SuppressWarnings({"rawtypes", "unchecked"})
	//	protected <T> JPAQuery applySort(JPAQuery query, Pageable pageable, Path<T> entityPath) {
	//		// TODO orderBy할 파라미터 검사 로직 필요
	//		Sort sort = pageable.getSort();
	//		if (sort == null) {
	//			return query;
	//		}
	//		PathBuilder<T> builder = new PathBuilder<T>(entityPath.getType(), entityPath.getMetadata());
	//		for (Sort.Order order : sort) {
	//			Expression<Object> property = builder.get(order.getProperty());
	//			query.orderBy(new OrderSpecifier(order.isAscending() ? com.mysema.query.types.Order.ASC : com.mysema.query.types.Order.DESC, property));
	//		}
	//
	//		return query;
	//	}
}