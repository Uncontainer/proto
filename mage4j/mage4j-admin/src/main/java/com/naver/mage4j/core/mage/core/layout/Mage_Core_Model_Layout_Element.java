package com.naver.mage4j.core.mage.core.layout;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.core.model.ModelInstanceLoader;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom;

public class Mage_Core_Model_Layout_Element extends Simplexml_Element_JDom {
	@Autowired
	private ModelInstanceLoader modelnstanceLoader;

	public Mage_Core_Model_Layout_Element(Mage_Core_Model_Layout_Element other) {
		super(other);
		this.modelnstanceLoader = other.modelnstanceLoader;
	}

	public Mage_Core_Model_Layout_Element(Element other) {
		super(other);
	}

	@Override
	protected Mage_Core_Model_Layout_Element createElement(Element element) {
		return new Mage_Core_Model_Layout_Element(element);
	}

	public void prepare(Map<String, Object> args) {
		switch (getName()) {
			case "layoutUpdate":
				break;
			case "layout":
				break;
			case "update":
				break;
			case "remove":
				break;
			case "block":
				prepareBlock(args);
				break;
			case "reference":
				prepareReference(args);
				break;
			case "action":
				prepareAction(args);
				break;
			default:
				prepareActionArgument(args);
				break;
		}

		for (Simplexml_Element child : children()) {
			((Mage_Core_Model_Layout_Element)child).prepare(args);
		}
	}

	public String getBlockName() {
		String tagName = getName();
		if (!"block".equals(tagName) && "reference".equals(tagName) || StringUtils.isEmpty(tagName)) {
			return null;
		}

		return tagName;
	}

	public Mage_Core_Model_Layout_Element prepareBlock(Map<String, Object> args) {
		String type = getString("type");
		String name = getString("name");

		String className = getString("class");
		if (className == null) {
			className = modelnstanceLoader.getBlockClassName(type);
			setAttribute("class", className);
		}

		Simplexml_Element parent = getParent();
		name = parent.getString("name");
		if (name != null && SimplexmlUtils.isNull(selectSingle("parent"))) {
			setAttribute("parent", name);
		}

		return this;
	}

	public void prepareReference(Map<String, Object> args) {
	}

	public void prepareAction(Map<String, Object> args) {
		Simplexml_Element parent = getParent();
		setAttribute("block", parent.getString("name"));
	}

	public void prepareActionArgument(Map<String, Object> args) {
	}
}
