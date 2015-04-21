package yeon.query;

import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceServiceUtils;

public class ResourceAliasPointer implements ResourcePointer {
	private String alias;

	public ResourceAliasPointer(String alias) {
		super();
		this.alias = alias;
	}

	@Override
	public String getResourceId() {
		ResourceGetCondition condition = ResourceGetCondition.createClassByAliasCondition(alias);

		return ResourceServiceUtils.getId(condition);
	}
}
