package com.naver.mage4j.core.mage.core.model.resource.url;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class UrlRewriteHelper {
	public static final String REWRITE_REQUEST_PATH_ALIAS = "rewrite_request_path";

	private final UrlRewrite rewrite;

	UrlRewriteHelper(UrlRewrite rewrite) {
		super();
		this.rewrite = rewrite;
	}

	public boolean hasOption(String key) {
		String options = rewrite.getOptions();
		if (options == null) {
			return false;
		}

		return ArrayUtils.contains(StringUtils.split(options, ","), key);
	}
}
