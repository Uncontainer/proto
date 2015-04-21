package com.naver.mage4j.core.mage.core.model.resource.translate;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TranslateContext {
	public static TranslateContext getContext() {
		throw new UnsupportedOperationException();
	}

	public Map<String, String> getTranslationArray(Short storeId, String locale) {
		throw new UnsupportedOperationException();
	}

	public CoreTranslate get() {
		throw new UnsupportedOperationException();
	}
}
