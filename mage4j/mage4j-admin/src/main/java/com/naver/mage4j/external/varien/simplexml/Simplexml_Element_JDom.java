package com.naver.mage4j.external.varien.simplexml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * http://www.beingjavaguys.com/2013/06/jdom-xml-parser-in-java.html
 * 
 * @author Naver
 */
public class Simplexml_Element_JDom implements Simplexml_Element {
	final Element rootNode;

	public Simplexml_Element_JDom(Simplexml_Element other) {
		if (!(other instanceof Simplexml_Element_JDom)) {
			throw new IllegalArgumentException("Require Varien_Simplexml_Element instance.(" + (other != null ? other.getClass() : "null") + ")");
		}

		rootNode = ((Simplexml_Element_JDom)other).rootNode.clone();
	}

	public Simplexml_Element_JDom(Element element) {
		if (element == null) {
			throw new IllegalArgumentException("Null element.");
		}

		rootNode = element;
	}

	protected Simplexml_Element_JDom createElement(Element element) {
		return new Simplexml_Element_JDom(element);
	}

	@Override
	public boolean isNull() {
		return false;
	}

	/**
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#getName()
	 */
	@Override
	public String getName() {
		return rootNode.getName();
	}

	/**
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#getText()
	 */
	@Override
	public String getText() {
		return rootNode.getText();
	}

	/**
	 * @param source
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#appendChild(com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom)
	 */
	@Override
	public Simplexml_Element appendChild(Simplexml_Element source) {
		if (!source.isNull()) {
			rootNode.addContent(((Simplexml_Element_JDom)source).rootNode.clone());
		}

		return this;
	}

	/**
	 * @param name
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#selectSingle(java.lang.String)
	 */
	@Override
	public Simplexml_Element selectSingle(String name) {
		Element child = rootNode.getChild(name);
		return child != null ? createElement(child) : NULL;
	}

	@Override
	public List<Simplexml_Element> selectAll(String name) {
		List<Element> children = rootNode.getChildren(name);
		if (children.isEmpty()) {
			return Collections.emptyList();
		}

		List<Simplexml_Element> result = new ArrayList<Simplexml_Element>(children.size());
		for (Element child : children) {
			result.add(createElement(child));
		}

		return result;
	}

	/**
	 * @param path
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#selectDescendant(java.lang.String)
	 */
	@Override
	public Simplexml_Element selectDescendant(String path) {
		XPathFactory xFactory = XPathFactory.instance();
		XPathExpression<Element> expr = xFactory.compile(path, Filters.element());
		Element child = expr.evaluateFirst(rootNode);
		return child != null ? createElement(child) : NULL;
	}

	/**
	 * @param path
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#selectDescendants(java.lang.String)
	 */
	@Override
	public List<Simplexml_Element> selectDescendants(String path) {
		XPathFactory xFactory = XPathFactory.instance();
		XPathExpression<Element> expr = xFactory.compile(path, Filters.element());
		List<Element> children = expr.evaluate(rootNode);
		if (children.isEmpty()) {
			return Collections.emptyList();
		}

		List<Simplexml_Element> result = new ArrayList<Simplexml_Element>(children.size());
		for (Element child : children) {
			result.add(createElement(child));
		}

		return result;
	}

	/**
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#children()
	 */
	@Override
	public List<Simplexml_Element> children() {
		List<Element> children = rootNode.getChildren();
		if (children == null || children.isEmpty()) {
			return Collections.emptyList();
		}

		List<Simplexml_Element> result = new ArrayList<Simplexml_Element>(children.size());
		for (Element child : rootNode.getChildren()) {
			result.add(createElement(child));
		}

		return result;
	}

	/**
	 * @param name
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		return rootNode.getChildText(name);
	}

	/**
	 * @param source
	 * @param overwrite
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#extend(com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom, boolean)
	 */
	@Override
	public Simplexml_Element extend(Simplexml_Element source, boolean overwrite) {
		if (!source.isNull()) {
			for (Element child : ((Simplexml_Element_JDom)source).rootNode.getChildren()) {
				this.extendChild(child, overwrite);
			}
		}

		return this;
	}

	/**
	 * @param source
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#extend(com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom)
	 */
	@Override
	public Simplexml_Element extend(Simplexml_Element source) {
		return extend(source, false);
	}

	/**
	* Returns parent node for the element
	*
	* Currently using xpath
	*
	* @return Varien_Simplexml_Element
	*/
	public Simplexml_Element getParent() {
		return selectDescendant("..");
	}

	/**
	 * @param name
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#hasChild(java.lang.String)
	 */
	@Override
	public boolean hasChild(String name) {
		return rootNode.getChild(name) != null;
	}

	/**
	 * @param source
	 * @param overwrite
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#extendChild(org.jdom2.Element, boolean)
	 */
	@Override
	public Simplexml_Element extendChild(Element source, boolean overwrite) {
		// name of the source node
		String sourceName = source.getName();

		Element existSource = rootNode.getChild(sourceName);

		// here we have children of our source node
		if (source.getChildren().isEmpty()) {
			if (existSource != null) {
				// if target already has children return without regard
				if (!existSource.getChildren().isEmpty()) {
					return this;
				}

				if (overwrite) {
					rootNode.removeChild(sourceName);
				} else {
					return this;
				}
			}

			rootNode.addContent(source.clone());

			return this;
		}

		if (existSource == null) {
			rootNode.addContent(source.clone());
		} else {
			Simplexml_Element target = createElement(existSource);
			for (Element child : source.getChildren()) {
				target.extendChild(child, overwrite);
			}
		}

		return this;
	}

	/**
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#toMap()
	 */
	@Override
	public Map<String, Object> toMap() {
		return toMap(rootNode);
	}

	private Map<String, Object> toMap(Element element) {
		Map<String, Object> result = new HashMap<String, Object>();
		//		if (element.hasAttributes()) {
		//			for (Attribute attribute : element.getAttributes()) {
		//				result.put(attribute.getName(), attribute.getValue());
		//			}
		//		}

		for (Element child : rootNode.getChildren()) {
			if (child.getChildren().isEmpty()/* && !child.hasAttributes()*/) {
				result.put(child.getName(), child.getValue());
			} else {
				result.put(child.getName(), toMap(child));
			}
		}

		return result;
	}

	/**
	 * @param value
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#xmlentities(java.lang.String)
	 */
	@Override
	public String xmlentities(String value) {
		if (value == null) {
			value = toString();
		}

		return StringEscapeUtils.escapeXml(value);
	}

	/**
	 * @param path
	 * @param value
	 * @param overwrite
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#setNode(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public Simplexml_Element setNode(String path, String value, boolean overwrite) {
		String[] arr1 = path.split("/");
		List<String> arr = new ArrayList<String>();
		for (String v : arr1) {
			if (StringUtils.isNotBlank(v)) {
				arr.add(v);
			}
		}

		Simplexml_Element_JDom node = this;
		for (int i = 0, last = arr.size() - 1; i < arr.size(); i++) {
			String nodeName = arr.get(i);
			Simplexml_Element existNode = node.selectSingle(nodeName);
			if (i == last) {
				if (existNode.isNull() || overwrite) {
					Element child = new Element(nodeName);
					child.setText(value);
					node.rootNode.setContent(child);
				}
			} else {
				if (existNode.isNull()) {
					Element child = new Element(nodeName);
					node.rootNode.addContent(child);
					node = createElement(child);
				} else {
					node = (Simplexml_Element_JDom)existNode;
				}
			}

		}

		return this;
	}

	/**
	 * @param indent
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#asNiceXml(boolean)
	 */
	@Override
	public String asNiceXml(boolean indent) {
		return asNiceXml(0, indent);
	}

	/**
	 * @param level
	 * @param indent
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#asNiceXml(int, boolean)
	 */
	public String asNiceXml(int level, boolean indent) {
		String pad;
		String nl;
		if (indent) {
			pad = "";
			for (int i = 0; i < level; i++) {
				pad += "   ";
			}
			nl = "\n";
		} else {
			pad = "";
			nl = "";
		}

		StringBuilder out = new StringBuilder();
		out.append(pad + '<' + rootNode.getName());
		for (Attribute attribute : rootNode.getAttributes()) {
			out.append(attribute.getName()).append("=\"").append(attribute.getValue().replace("\"", "\\\"")).append("\"");
		}

		List<Simplexml_Element> children = children();
		if (!children.isEmpty()) {
			out.append(">" + nl);
			for (Simplexml_Element child : children) {
				out.append(((Simplexml_Element_JDom)child).asNiceXml(level + 1, indent));
			}
			out.append(pad + "</" + rootNode.getName() + ">" + nl);
		} else {
			String value = rootNode.getText();
			if (StringUtils.isNotEmpty(value)) {
				out.append(">" + xmlentities(value) + "</" + rootNode.getName() + ">" + nl);
			} else {
				out.append("/>" + nl);
			}
		}

		return out.toString();
	}

	/**
	 * @param name
	 * @return
	 * @see com.naver.mage4j.external.varien.simplexml.Simplexml_Element#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String name) {
		return rootNode.getAttributeValue(name);
	}

	@Override
	public void setAttribute(String name, String value) {
		rootNode.setAttribute(name, value);
	}
}
