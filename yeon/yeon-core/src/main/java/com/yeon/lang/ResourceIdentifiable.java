package com.yeon.lang;

/**
 * 리소스를 식별하기 위한 인터페이스
 * 
 * @author ghost
 * 
 */
public interface ResourceIdentifiable {
	ResourceIdentifiable NULL = new ResourceIdentifiable() {
		@Override
		public String getResourceId() {
			return null;
		}
	};

	/**
	 * Resource의 ID를 돌려준다.
	 * 
	 * @return resourceId
	 * @throws ResourceNotFoundException
	 *             resource를 찾을 수 없을 경우.
	 * @throws ResourceDuplicatedException
	 *             resource가 2개 이상 있을 경우.
	 */
	String getResourceId();
}
