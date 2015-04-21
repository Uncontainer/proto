package yeon.query;

public class ResourceIdPointer implements ResourcePointer {
	private final String resourceId;

	public ResourceIdPointer(String resourceId) {
		super();
		this.resourceId = resourceId;
	}

	public String getResourceId() {
		return resourceId;
	}
}
