package com.naver.mage4j.external.varien.simplexml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class Simplexml_Element_Null implements Simplexml_Element {
	protected Simplexml_Element_Null() {
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public Simplexml_Element appendChild(Simplexml_Element source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Simplexml_Element selectSingle(String name) {
		return this;
	}

	@Override
	public List<Simplexml_Element> selectAll(String name) {
		return Collections.emptyList();
	}

	@Override
	public Simplexml_Element selectDescendant(String path) {
		return this;
	}

	@Override
	public List<Simplexml_Element> selectDescendants(String path) {
		return Collections.emptyList();
	}

	@Override
	public List<Simplexml_Element> children() {
		return Collections.emptyList();
	}

	@Override
	public String getString(String name) {
		return null;
	}

	@Override
	public Simplexml_Element extend(Simplexml_Element source, boolean overwrite) {
		throw new UnsupportedOperationException("Null Simplexml_Element.");
	}

	@Override
	public Simplexml_Element extend(Simplexml_Element source) {
		throw new UnsupportedOperationException("Null Simplexml_Element.");
	}

	@Override
	public Simplexml_Element extendChild(Element source, boolean overwrite) {
		throw new UnsupportedOperationException("Null Simplexml_Element.");
	}

	@Override
	public boolean hasChild(String name) {
		return false;
	}

	@Override
	public Map<String, Object> toMap() {
		return Collections.emptyMap();
	}

	@Override
	public String xmlentities(String value) {
		return null;
	}

	@Override
	public Simplexml_Element setNode(String path, String value, boolean overwrite) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String asNiceXml(boolean indent) {
		return null;
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public void setAttribute(String name, String value) {
		throw new UnsupportedOperationException("Null Simplexml_Element.");
	}
}
