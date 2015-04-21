package com.naver.mage4j.external.varien.simplexml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class SimplexmlUtils {
	public static boolean isNull(Simplexml_Element element) {
		return element == null || element == Simplexml_Element.NULL;
	}

	public static boolean isNotNull(Simplexml_Element element) {
		return isNull(element);
	}

	public static String innerHtml(Simplexml_Element element) {
		if (isNull(element)) {
			return null;
		}

		StringBuilder out = new StringBuilder();

		for (Simplexml_Element child : element.children()) {
			out.append(child.asNiceXml(true));
		}

		return out.toString();
	}

	public static Class<? extends Simplexml_Element_JDom> forElementClassName(String elementClassName) {
		return null;
	}

	public static Simplexml_Element_JDom build(String xmlString, String elementClassName) {
		return build(xmlString, forElementClassName(elementClassName));
	}

	public static Simplexml_Element_JDom build(String xmlString, Class<? extends Simplexml_Element_JDom> elementClass) {
		SAXBuilder saxBuilder = new SAXBuilder();

		// converted file to document object
		Document document;
		try {
			document = saxBuilder.build(new StringReader(xmlString));
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return build(elementClass, document.getRootElement());
	}

	public static Simplexml_Element_JDom build(File xmlFile, String elementClassName) {
		return build(xmlFile, forElementClassName(elementClassName));
	}

	public static Simplexml_Element_JDom build(File xmlFile, Class<? extends Simplexml_Element_JDom> elementClass) {
		// reading can be done using any of the two 'DOM' or 'SAX' parser
		// we have used saxBuilder object here
		// please note that this saxBuilder is not internal sax from jdk
		SAXBuilder saxBuilder = new SAXBuilder();

		// converted file to document object
		Document document;
		try {
			document = saxBuilder.build(xmlFile);
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return build(elementClass, document.getRootElement());
	}

	static Simplexml_Element_JDom build(Class<? extends Simplexml_Element_JDom> elementClass, Element element) {
		Constructor<? extends Simplexml_Element_JDom> constructor;
		try {
			constructor = elementClass.getConstructor(Element.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}

		try {
			return constructor.newInstance(element);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getString(Simplexml_Element node, String name, String defaultValue) {
		return StringUtils.defaultString(node.getString(name), defaultValue);
	}
}
