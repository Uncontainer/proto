package com.naver.mage4j.external.varien.simplexml;

import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public interface Simplexml_Element {
	String getName();

	String getText();

	Simplexml_Element selectSingle(String name);

	List<Simplexml_Element> selectAll(String name);

	Simplexml_Element selectDescendant(String path);

	List<Simplexml_Element> selectDescendants(String path);

	List<Simplexml_Element> children();

	String getString(String name);

	boolean hasChild(String name);

	String asNiceXml(boolean indent);

	String getAttribute(String name);

	void setAttribute(String name, String value);

	boolean isNull();

	Map<String, Object> toMap();

	String xmlentities(String value);

	Simplexml_Element appendChild(Simplexml_Element source);

	Simplexml_Element setNode(String path, String value, boolean overwrite);

	Simplexml_Element extend(Simplexml_Element source, boolean overwrite);

	Simplexml_Element extend(Simplexml_Element source);

	Simplexml_Element extendChild(Element source, boolean overwrite);

	Simplexml_Element NULL = new Simplexml_Element_Null();
}