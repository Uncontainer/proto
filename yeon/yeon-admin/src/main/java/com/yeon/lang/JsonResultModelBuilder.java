package com.yeon.lang;

import com.yeon.util.CollectionJsonMapper;

public class JsonResultModelBuilder {
	private boolean success;
	private String code;
	private String message;
	private String value;

	public static JsonResultModelBuilder aSuccess() {
		JsonResultModelBuilder builder = new JsonResultModelBuilder();
		builder.success = true;

		return builder;
	}

	public static JsonResultModelBuilder aFail() {
		JsonResultModelBuilder builder = new JsonResultModelBuilder();
		builder.success = false;

		return builder;
	}

	public JsonResultModelBuilder code(String code) {
		this.code = code;

		return this;
	}

	public JsonResultModelBuilder message(String message) {
		this.message = message;

		return this;
	}

	public JsonResultModelBuilder jsonValue(String jsonValue) {
		this.value = jsonValue;

		return this;
	}

	public JsonResultModelBuilder value(Object value) {
		this.value = CollectionJsonMapper.getInstance().toJson(value);

		return this;
	}

	public JsonResultModel build() {
		JsonResultModel model = new JsonResultModel(success, code, message, value);

		return model;
	}
}
