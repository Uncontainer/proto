package com.naver.mage4j.external.varien.simplexml;

import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class Simplexml_Element_Adapter implements Simplexml_Element {
	protected final Simplexml_Element data;

	public Simplexml_Element_Adapter(Simplexml_Element data) {
		this.data = data;
	}

	@Override
	public String getName() {
		return data.getName();
	}

	@Override
	public String getText() {
		return data.getText();
	}

	@Override
	public Simplexml_Element selectSingle(String name) {
		return data.selectSingle(name);
	}

	@Override
	public List<Simplexml_Element> selectAll(String name) {
		return data.selectAll(name);
	}

	@Override
	public Simplexml_Element selectDescendant(String path) {
		return data.selectDescendant(path);
	}

	@Override
	public List<Simplexml_Element> selectDescendants(String path) {
		return data.selectDescendants(path);
	}

	@Override
	public List<Simplexml_Element> children() {
		return data.children();
	}

	@Override
	public String getString(String name) {
		return data.getString(name);
	}

	@Override
	public boolean hasChild(String name) {
		return data.hasChild(name);
	}

	@Override
	public String asNiceXml(boolean indent) {
		return data.asNiceXml(indent);
	}

	@Override
	public String getAttribute(String name) {
		return data.getAttribute(name);
	}

	@Override
	public void setAttribute(String name, String value) {
		data.setAttribute(name, value);
	}

	@Override
	public boolean isNull() {
		return data.isNull();
	}

	@Override
	public Map<String, Object> toMap() {
		return data.toMap();
	}

	@Override
	public String xmlentities(String value) {
		return data.xmlentities(value);
	}

	@Override
	public Simplexml_Element appendChild(Simplexml_Element source) {
		return data.appendChild(source);
	}

	@Override
	public Simplexml_Element setNode(String path, String value, boolean overwrite) {
		return data.setNode(path, value, overwrite);
	}

	@Override
	public Simplexml_Element extend(Simplexml_Element source, boolean overwrite) {
		return data.extend(source, overwrite);
	}

	@Override
	public Simplexml_Element extend(Simplexml_Element source) {
		return data.extend(source);
	}

	@Override
	public Simplexml_Element extendChild(Element source, boolean overwrite) {
		return data.extendChild(source, overwrite);
	}
}
