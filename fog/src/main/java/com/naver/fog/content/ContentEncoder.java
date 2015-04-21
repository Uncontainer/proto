package com.naver.fog.content;

// TODO Content의 value이외의 값도 encode/decode에 포함할지여 여부 결정 필요.
// Anonymous 클래스와 유사하게 값만 취하는 개념으로 봐도 괜찮을 듯...
public class ContentEncoder {
	public static String encode(Content content) {
		String encodedContent = ContentValueEncoder.encode(content.getFrame(), content.getFieldValueMap());

		return content.getFrame().getId() + ":" + encodedContent.length() + "." + encodedContent;
	}

	public static Content decode(String encodedContent) {
		int index = 0;
		int colonIndex = encodedContent.indexOf(':', index);
		long frameId = Long.valueOf(encodedContent.substring(index, colonIndex));
		index = colonIndex + 1;

		int dotIndex = encodedContent.indexOf('.', index);
		int length = Integer.valueOf(encodedContent.substring(index, dotIndex));
		index = dotIndex + 1;
		String encodedFrameContent = encodedContent.substring(index, index + length);

		return new Content(frameId, encodedFrameContent);
	}
}
