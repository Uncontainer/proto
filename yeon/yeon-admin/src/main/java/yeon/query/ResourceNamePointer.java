package yeon.query;

import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceServiceUtils;

public class ResourceNamePointer implements ResourcePointer {
	private final String locale;
	private final String name;

	public ResourceNamePointer(String name) {
		this(null, name);
	}

	public ResourceNamePointer(String locale, String name) {
		super();
		this.locale = locale;
		this.name = name;
	}

	public String getLocale() {
		return locale;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getResourceId() {
		// TODO classId를 셋팅하는 코드 추가.
		ResourceGetCondition condition = ResourceGetCondition.createResourceByNameCondition(locale, name, null);
		ResourceServiceUtils.getId(condition);

		// TODO resouceId를 구하는 코드 추가.
		throw new UnsupportedOperationException();
	}
}
