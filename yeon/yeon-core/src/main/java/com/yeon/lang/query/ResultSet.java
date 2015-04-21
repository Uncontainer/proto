package com.yeon.lang.query;

import com.yeon.lang.Resource;

public interface ResultSet {
	com.yeon.lang.Class getTye();

	boolean next();

	Resource get();

	int getColumnCount();

	Object getObject(int columnIndex);

	ResultSetMetaData getMetaData();

	void close();
}
