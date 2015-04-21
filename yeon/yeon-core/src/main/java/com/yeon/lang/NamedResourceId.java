package com.yeon.lang;

public class NamedResourceId implements NamedResourceIdentifiable {
	private String locale;
	private String name;
	private String typeClassId;
	private String resourceId;

	public NamedResourceId(String locale, String name, ResourceIdentifiable classIdentifiable) {
		this(locale, name, classIdentifiable.getResourceId());
	}

	public NamedResourceId(String locale, String name, String typeClassId) {
		super();
		this.typeClassId = typeClassId;
		this.name = name;
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}

	public String getName() {
		return name;
	}

	public LocalValue getLocalName() {
		return new LocalValue(locale, name);
	}

	@Override
	public String getResourceId() {
		String ri = resourceId;

		if (ri == null) {

			ResourceGetCondition condition = ResourceGetCondition.createResourceByNameCondition(locale, name, typeClassId);
			ri = resourceId = ResourceServiceUtils.getId(condition);
		}

		return ri;
	}

	@Override
	public String toString() {
		return locale + ":" + name + "@" + typeClassId;
	}
}
