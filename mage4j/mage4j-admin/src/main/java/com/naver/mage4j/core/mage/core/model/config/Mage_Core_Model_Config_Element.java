package com.naver.mage4j.core.mage.core.model.config;

import org.jdom2.Element;

import com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom;

public class Mage_Core_Model_Config_Element extends Simplexml_Element_JDom {
	public Mage_Core_Model_Config_Element(Mage_Core_Model_Config_Element other) {
		super(other);
	}

	public Mage_Core_Model_Config_Element(Element other) {
		super(other);
	}

	@Override
	protected Mage_Core_Model_Config_Element createElement(Element element) {
		return new Mage_Core_Model_Config_Element(element);
	}

	/**
	 * @param var
	 * @param value
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#is(java.lang.String, java.lang.String)
	 */
	public boolean is(String var, String value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value of 'is'");
		}

		String flag = getString(var);

		if ("true".equals(value)) {
			if (flag == null) {
				return false;
			}

			flag = flag.toLowerCase();
			return !("false".equals(flag) || "off".equals(flag));
		}

		return flag != null && flag.equalsIgnoreCase(value);
	}

	/**
	 * @param var
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#is(java.lang.String)
	 */
	public boolean is(String var) {
		return is(var, "true");
	}
}
