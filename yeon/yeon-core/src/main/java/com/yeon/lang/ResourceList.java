package com.yeon.lang;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceList implements Serializable {
	private static final long serialVersionUID = 7002846695805442675L;

	public static final ResourceList EMPTY = new ResourceList() {
		private static final long serialVersionUID = 6944045473436067087L;

		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Resource get(int index) {
			throw new IndexOutOfBoundsException();
		}
	};

	protected transient ResourceService resourceService;
	protected List<String> resourceIds;

	public ResourceList() {
	}

	public ResourceList(List<String> resourceIds) {
		this.resourceIds = new ArrayList<String>(resourceIds);
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	// TODO 접근 제한을 걸어서 특정 class에서만 접근이 가능하도록 수정.
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public int size() {
		return resourceIds.size();
	}

	public boolean isEmpty() {
		return resourceIds.isEmpty();
	}

	public List<String> getResourceIds() {
		return Collections.unmodifiableList(resourceIds);
	}

	public Resource get(int index) {
		return resourceService.get(resourceIds.get(index));
	}
}
