package com.yeon.lang.listener;

import java.util.List;

public class ResourceListenerRegistry {
	private ResourceListenerTree typeListenerTree;
	private ResourceListenerTree instanceListenerTree;

	public ResourceListenerRegistry() {
		typeListenerTree = new ResourceListenerTree();
		instanceListenerTree = new ResourceListenerTree();
	}

	public void addListener(ListenTarget target, ResourceListener listener) {
		if (target == null) {
			throw new IllegalArgumentException("Empty target.");
		}
		if (listener == null) {
			throw new IllegalArgumentException("Listener is empty.");
		}

		if (target.getScope() == ListenScope.TYPE) {
			typeListenerTree.add(target.getValue(), listener);
		} else {
			instanceListenerTree.add(target.getValue(), listener);
		}
	}

	public List<ResourceListener> getListeners(ListenTarget target) {
		if (target.getScope() == ListenScope.TYPE) {
			return typeListenerTree.getAllChildListeners(target.getValue());
		} else {
			return instanceListenerTree.getAllChildListeners(target.getValue());
		}
	}

	public boolean removeListener(ListenTarget target, ResourceListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener is empty.");
		}

		if (target.getScope() == ListenScope.TYPE) {
			return typeListenerTree.remove(target.getValue(), listener);
		} else {
			return instanceListenerTree.remove(target.getValue(), listener);
		}
	}
}
