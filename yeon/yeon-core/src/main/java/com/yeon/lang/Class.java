package com.yeon.lang;

import java.util.List;

/**
 * 한 resource_id에 description이 다른 name이 있을 수 없다.<br>
 * 완전히 동일할 개념의 단어는 없다. 아주 사소한 이유(단순한 어감?)라도 새로운 이름으로 단어가 말들어진 이유는 있다.
 * 
 * @author pulsarang
 * 
 */
public interface Class extends Resource {

	/**
	 * 주어지 이름의 property를 가져온다. 현재 class에 존재하지 않을 경우 부모 class에서 찾는다.
	 * 
	 * @param name
	 * @return
	 */
	Property getPropertyByName(String name);

	Property getPropertyById(String id);

	List<Property> getProperties();
}
