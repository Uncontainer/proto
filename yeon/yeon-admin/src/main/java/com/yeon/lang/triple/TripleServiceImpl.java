package com.yeon.lang.triple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripleServiceImpl implements TripleService {
	@Autowired
	private TripleDao tripleDao;

	@Override
	public Triple get(long tripleId) {
		return tripleDao.select(tripleId);
	}

	@Override
	public List<Triple> listByResourceId(String resourceId) {
		return tripleDao.selectByResourceId(resourceId);
	}

	@Override
	public long add(Triple triple) {
		return tripleDao.insert(triple);
	}
}
