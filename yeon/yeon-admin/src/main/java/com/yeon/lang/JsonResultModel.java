package com.yeon.lang;

public class JsonResultModel {
	public static final String PARAM_RESULT = "result";

	public static final JsonResultModel SUCCESS = new JsonResultModel() {
		@Override
		public void setSuccess(boolean success) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCode(String code) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMessage(String message) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue(String value) {
			throw new UnsupportedOperationException();
		}
	};

	private boolean success;
	private String code;
	private String message;
	private String value;

	public JsonResultModel() {
	}

	JsonResultModel(boolean success, String code, String message, String value) {
		super();
		this.success = success;
		this.code = code;
		this.message = message;
		this.value = value;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
