package com.yeon.lang.triple;

import java.util.List;

public interface TripleService {
	long add(Triple triple);

	Triple get(long tripleId);

	List<Triple> listByResourceId(String resourceId);
}
