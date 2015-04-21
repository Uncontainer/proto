package com.yeon.lang.listener;

import com.yeon.lang.Class;
import com.yeon.lang.ResourceList;
import com.yeon.lang.ResourceServiceUtils;

import java.util.*;

public class ResourceListenerTree {
	private final Node ROOT;
	private final Map<String, Node> nodeMap;

	public ResourceListenerTree() {
		ROOT = new Node();
		nodeMap = new HashMap<String, Node>();
	}

	public void add(String targetResourceId, ResourceListener listener) {
		Node node = nodeMap.get(targetResourceId);
		if (node == null) {
			node = insertNodeRecursively(ResourceServiceUtils.getClassSafely(targetResourceId));
		}

		node.addListener(listener);
	}

	public boolean remove(String targetResourceId, ResourceListener listener) {
		Node node = nodeMap.get(targetResourceId);
		if (node == null) {
			return false;
		}

		if (node.removeListener(listener)) {
			removeNodeRecursivelyIfEmpty(node);
			return true;
		}

		return false;
	}

	private Node insertNodeRecursively(Class clazz) {
		Node node = nodeMap.get(clazz.getId());
		if (node != null) {
			return node;
		}

		List<Node> parents;
		ResourceList superClasses = clazz.getPrototypes();
		if (superClasses.isEmpty()) {
			parents = new ArrayList<ResourceListenerTree.Node>(1);
			parents.add(ROOT);
		} else {
			parents = new ArrayList<ResourceListenerTree.Node>(superClasses.size());
			for (int i = 0; i < superClasses.size(); i++) {
				parents.add(insertNodeRecursively((Class) superClasses.get(i)));
			}
		}

		node = new Node(clazz.getId(), parents);
		for (Node parent : parents) {
			parent.addChild(node);
		}
		nodeMap.put(clazz.getId(), node);

		return node;
	}

	private void removeNodeRecursivelyIfEmpty(Node node) {
		if (node == ROOT) {
			return;
		}

		if (!node.isEmpty()) {
			return;
		}

		nodeMap.remove(node.id);
		for (Node parent : node.parents) {
			parent.removeChild(node);
			removeNodeRecursivelyIfEmpty(parent);
		}
	}

	static class Node {
		final String id;
		List<Node> parents;
		List<Node> children;
		List<ResourceListener> listeners;

		/**
		 * For root node only
		 */
		private Node() {
			id = "ROOT";
		}

		Node(String id, List<Node> parents) {
			super();
			this.id = id;
			this.parents = parents;
		}

		boolean isEmpty() {
			return (listeners == null || listeners.isEmpty()) && (children == null || children.isEmpty());
		}

		void addListener(ResourceListener listener) {
			if (listeners == null) {
				listeners = new ArrayList<ResourceListener>(2);
			} else if (listeners.contains(listener)) {
				throw new IllegalArgumentException("Duplicate listener.");
			}

			listeners.add(listener);
		}

		boolean removeListener(ResourceListener listener) {
			if (listeners == null) {
				return false;
			}

			return listeners.remove(listener);
		}

		void addChild(Node node) {
			if (children == null) {
				children = new ArrayList<ResourceListenerTree.Node>();
			} else {
				if (isChild(node.id)) {
					throw new IllegalArgumentException("Duplicate child node.");
				}
			}

			children.add(node);
		}

		void removeChild(Node node) {
			children.remove(node);
		}

		boolean isChild(String resourceId) {
			for (Node child : children) {
				if (child.id.equals(resourceId)) {
					return true;
				}
			}

			return false;
		}
	}

	public List<ResourceListener> getAllChildListeners(String resourceId) {
		Node node = nodeMap.get(resourceId);
		if (node == null) {
			return Collections.emptyList();
		}

		return addListeners(node, new ArrayList<ResourceListener>());
	}

	private static List<ResourceListener> addListeners(Node node, List<ResourceListener> listeners) {
		if (node.listeners != null) {
			listeners.addAll(node.listeners);
		}

		if (node.children != null) {
			for (Node child : node.children) {
				addListeners(child, listeners);
			}
		}

		return listeners;
	}
}
