package com.yeon.remote.server.io;

public enum SerializationType {
	JAVA_SERIALIZATION(1, new JavaSerializer()),
	JSON(2, new JsonSerializer()),
	MAP_MODEL_JAVA_SERIALIZATION(3, new MapModelJavaSerializer());
	// ,
	// MAP_MODEL_JSON(4);

	final byte code;
	final Serializer serializer;

	private SerializationType(int code, Serializer serializer) {
		this.code = (byte) code;
		this.serializer = serializer;
	}

	public byte getCode() {
		return code;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public static SerializationType fromCode(int code) {
		switch (code) {
		case 1:
			return JAVA_SERIALIZATION;
		case 2:
			return JSON;
		case 3:
			return MAP_MODEL_JAVA_SERIALIZATION;
			// case 4:
			// return MAP_MODEL_JSON;
		default:
			throw new IllegalArgumentException("Unsupported serialization type code.(" + code + ")");
		}
	}
}
