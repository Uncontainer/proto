/*
 * @(#)JacksonUtil.java $version 2011. 3. 26.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.naver.mage4j.core.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.deser.CustomDeserializerFactory;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * serialize 의 경우 데이터 필드는 일괄적으로 적용되지만 deserialize 필드는 필드별로 아래처럼 어노테이션을 적용 시켜줘야 함
 * <br>@JsonSerialize(using = JacksonUtil.SimpleDateSerializer.class)
 *
 * @author EC전시서비스개발팀
 */
public class JacksonUtil {
	static Logger log = LoggerFactory.getLogger(JacksonUtil.class);
	private final ObjectMapper mapper;
	private final ObjectMapper indentWriter;

	private JacksonUtil() {
		mapper = new ObjectMapper();
		mapper.getSerializationConfig().enable(Feature.AUTO_DETECT_GETTERS);
		mapper.getSerializationConfig().enable(Feature.AUTO_DETECT_IS_GETTERS);
		mapper.getSerializationConfig().disable(Feature.FAIL_ON_EMPTY_BEANS);
		mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 적용하지 않으면 "birthday":1304055874077 와 같은 형식으로 serialize 
		CustomSerializerFactory serializerFactory = new CustomSerializerFactory();
		serializerFactory.addSpecificMapping(Date.class, new DefaultDateSerializer());
		mapper.setSerializerFactory(serializerFactory);

		CustomDeserializerFactory customDeserializerFactory = new CustomDeserializerFactory();
		mapper.setDeserializerProvider(new StdDeserializerProvider(customDeserializerFactory));
		customDeserializerFactory.addSpecificMapping(Date.class, new DefaultDateDeserializer());

		// TODO 기존에 숫자형 필드에 toObject 가 사용된 곳에 문제가 없는지 확인 후 적용해야 함
		//		customDeserializerFactory.addSpecificMapping(Integer.class, new NullableIntegerDeserializer()); //Long타입도 기본으로 적용 시켜줄지 협의 필요
		//		customDeserializerFactory.addSpecificMapping(Long.class, new NullableLongDeserializer()); //Integer 타입도 기본으로 적용 시켜줄지 협의 필요.

		indentWriter = new ObjectMapper();
		indentWriter.getSerializationConfig().enable(Feature.AUTO_DETECT_GETTERS);
		indentWriter.getSerializationConfig().enable(Feature.AUTO_DETECT_IS_GETTERS);
		indentWriter.getSerializationConfig().enable(Feature.INDENT_OUTPUT);
		indentWriter.getSerializationConfig().disable(Feature.FAIL_ON_EMPTY_BEANS);
		CustomSerializerFactory serializerFactory2 = new CustomSerializerFactory();
		serializerFactory2.addSpecificMapping(Date.class, new DefaultDateSerializer());
		indentWriter.setSerializerFactory(serializerFactory);
		CustomDeserializerFactory customDeserializerFactory2 = new CustomDeserializerFactory();
		customDeserializerFactory2.addSpecificMapping(Date.class, new DefaultDateDeserializer());
		indentWriter.setDeserializerProvider(new StdDeserializerProvider(customDeserializerFactory2));
	}

	public static JacksonUtil getInstance() {
		return new JacksonUtil();
	}

	private static ObjectMapper getMapper() {
		return getInstance().mapper;
	}

	public static String toJson(Object object) {
		try {
			return getMapper().writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String toPrettyJson(Object object) {
		try {
			return getInstance().indentWriter.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T toObject(String jsonStr) {
		if (StringUtils.isEmpty(jsonStr)) {
			return null;
		}

		if (jsonStr.startsWith("[")) {
			return (T)toObject(jsonStr, List.class);
		} else {
			return (T)toObject(jsonStr, Map.class);
		}
	}

	public static <T> T toObject(String jsonStr, Class<T> cls) {
		try {
			return getMapper().readValue(jsonStr, cls);
		} catch (Exception e) {
			log.debug(String.format("Exception occured when performing JacksonUtil.toObject()\n\tsource : %s\n\ttargetClass : %s", jsonStr, cls.getName()), e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(String jsonStr, TypeReference<?> typeReference) {
		try {
			return (T)getMapper().readValue(jsonStr, typeReference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * java.util.Date 필드를 DatePatterns.DATE_TIME("yyyy-MM-dd HH:mm:ss") 형식으로 출력
	 *
	 * @author nhn
	 */
	public static class DefaultDateSerializer extends JsonSerializer<Date> {
		@Override
		public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeRawValue(value == null ? "" : Long.toString(value.getTime()));
		}
	}

	/**
	 * Json 문자열을 Object 로 변환할 때 DatePatterns.DATE_TIME("yyyy-MM-dd HH:mm:ss") 형식의 문자열을  deserialize 해서 java.util.Date 를 생성함.
	 * java.util.Date 타입의 필드의 경우"null" 이 입력되면 에러가 발생하지 않지만 "" 공백이 입력되면 에러가 발생함 "" 도 입력가능하게 수정함
	 * TODO  "yyyy-MM-dd HH:mm:ss" 형식 뿐만 아니라 "yyyy/MM/dd HH-mm-ss" 또는 "yyyy년MM월dd일 HH시mm분ss초" 등도 딜리미터에 상관 없이 파싱가능하게 할지 여부를 결정해서
	 * 구현할 필요가 있어보임.
	 * <br> Serialize 시리얼라이즈 가능
	 * <br>@JsonSerialize(using = JacksonUtil.SimpleDateSerializer.class)
	 *
	 * @author nhn
	 */
	public static class DefaultDateDeserializer extends JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
			String value = jsonparser.getText();
			if (StringUtils.isBlank(value)) {
				return null;
			}

			if (NumberUtils.isDigits(value)) {
				return new Date(Long.parseLong(value));
			}

			throw new RuntimeException("(" + value + ")은 지원되지 않은 날짜 포멧입니다.");
		}
	}

	/**
	 * Json 문자열을 Object 로 변환할 때 숫자타입의 필드의 경우 숫자값이나 null 이 입력되면 에러가 발생하지 않지만 "" 공백이 입력되면 에러가 발생함.
	 * 화면에서 아무런 값도 입력하지 않을 경우 공백은 null 로 입력해줌
	 * 사용예:
	 * <pre>
	 * <br>@JsonSerialize(using = JacksonUtil.NullableLongDeserializer.class)
	 * public void setFamilyCount(Integer familyCount) {
	 * 	this.familyCount = familyCount;
	 * }
	 * </pre>
	 *
	 * @author nhn
	 */
	public static class NullableNumberDeserializer extends JsonDeserializer<Number> {
		@Override
		public Number deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
			String value = jsonparser.getText();
			log.debug("numberField.value={}", value);
			return (value == null || value.length() == 0) ? null : jsonparser.getNumberValue();
		}

	}

	/**
	 * @author nhn
	 */
	public static class NullableIntegerDeserializer extends JsonDeserializer<Integer> {
		@Override
		public Integer deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
			String value = jsonparser.getText();
			return (value == null || value.length() == 0) ? null : jsonparser.getIntValue();
		}

	}

	/**
	 * @author nhn
	 */
	public static class NullableLongDeserializer extends JsonDeserializer<Long> {
		@Override
		public Long deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
			String value = jsonparser.getText();
			return (value == null || value.length() == 0) ? null : jsonparser.getLongValue();
		}

	}
}