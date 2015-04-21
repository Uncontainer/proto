package com.naver.mage4j.core.mage.core.model.translate;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Translate;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.util.HtmlUtils;
import com.naver.mage4j.php.mage.MageAtomArray;

/**
 * Inline Translations PHP part
 */
public class Mage_Core_Model_Translate_Inline {
	/**
	 * Regular Expression for detected and replace translate
	 */
	protected String _tokenRegex = "\\{\\{\\{(.*?)\\}\\}\\{\\{(.*?)\\}\\}\\{\\{(.*?)\\}\\}\\{\\{(.*?)\\}\\}\\}";

	/**
	 * Response body or JSON content string
	 */
	protected String _content;

	/**
	 * Is enabled and allowed inline translates flags
	 */
	protected Boolean _isAllowed;

	/**
	 * Flag about inserted styles and scripts for inline translates
	 */
	protected boolean _isScriptInserted = false;

	/**
	 * Current content is JSON or Response body
	 */
	protected boolean _isJson = false;

	/**
	 * Get max translate block in same tag
	 */
	protected int _maxTranslateBlocks = 7;

	/**
	 * List of global tags
	 */
	protected Map _allowedTagsGlobal = MageAtomArray.createMap(new Object[] {"script", "String in Javascript"}, new Object[] {"title", "Page title"});

	/**
	 * List of simple tags
	 */
	protected Map _allowedTagsSimple = MageAtomArray.createMap(
		new Object[] {"legend", "Caption for the fieldset element"}, new Object[] {"label", "Label for an input element."}, new Object[] {"button", "Push button"},
		new Object[] {"a", "Link label"}, new Object[] {"b", "Bold text"}, new Object[] {"strong", "Strong emphasized text"}, new Object[] {"i", "Italic text"},
		new Object[] {"em", "Emphasized text"}, new Object[] {"u", "Underlined text"}, new Object[] {"sup", "Superscript text"}, new Object[] {"sub", "Subscript text"},
		new Object[] {"span", "Span element"}, new Object[] {"small", "Smaller text"}, new Object[] {"big", "Bigger text"}, new Object[] {"address", "Contact information"},
		new Object[] {"blockquote", "Long quotation"}, new Object[] {"q", "Short quotation"}, new Object[] {"cite", "Citation"}, new Object[] {"caption", "Table caption"},
		new Object[] {"abbr", "Abbreviated phrase"}, new Object[] {"acronym", "An acronym"}, new Object[] {"var", "Variable part of a text"}, new Object[] {"dfn", "Term"},
		new Object[] {"strike", "Strikethrough text"}, new Object[] {"del", "Deleted text"}, new Object[] {"ins", "Inserted text"}, new Object[] {"h1", "Heading level 1"},
		new Object[] {"h2", "Heading level 2"}, new Object[] {"h3", "Heading level 3"}, new Object[] {"h4", "Heading level 4"}, new Object[] {"h5", "Heading level 5"},
		new Object[] {"h6", "Heading level 6"}, new Object[] {"center", "Centered text"}, new Object[] {"select", "List options"}, new Object[] {"img", "Image"},
		new Object[] {"input", "Form element"});

	/**
	 * Is enabled and allowed Inline Translates
	 * 
	 * @param store
	 * @return bool
	 */
	public boolean isAllowed(Store store/* = null */) {
		store = defaultStore(store);

		if (this._isAllowed == null) {
			boolean active;
			if (AppContext.getCurrent().getDesignPackage().getArea() == AreaType.ADMINHTML) {
				active = store.getHelper().getConfigFlag("dev/translate_inline/active_admin");
			} else {
				active = store.getHelper().getConfigFlag("dev/translate_inline/active");
			}

			this._isAllowed = active && AppContext.getCurrent().getHelperData().isDevAllowed(store);
		}

		Mage_Core_Model_Translate translate = AppContext.getCurrent().getTranslator();
		return translate.getTranslateInline() && this._isAllowed;
	}

	private Store defaultStore(Store store) {
		return store != null ? store : AppContext.getCurrent().getStore();
	}

	/**
	 * Parse and save edited translate
	 * 
	 * @param translate
	 * @return Mage_Core_Model_Translate_Inline
	 */
	//	public Mage_Core_Model_Translate_Inline processAjaxPost(array translate) {
	//		NoType resource;
	//		Short storeId;
	//		if (!(this.isAllowed(null))) {
	//			return this;
	//		}
	//
	//		resource = Mage.getResourceModel("core/translate_string");
	//		for (Map t : translate) {
	//			if (AppContext.getCurrent().getDesignPackage().getArea() != AreaType.ADMINHTML) {
	//				storeId = 0;
	//			} else {
	//				if (t.get("perstore") == null) {
	//					resource.deleteTranslate(t.get("original"), null, false);
	//					storeId = 0;
	//				} else {
	//					storeId = AppContext.getCurrent().getStore().getId();
	//				}
	//			}
	//
	//			resource.saveTranslate(t.get("original"), t.get("custom"), null, storeId);
	//		}
	//
	//		return this;
	//	}

	/**
	 * Strip inline translations from text
	 * 
	 * @param body
	 * @return Mage_Core_Model_Translate_Inline
	 */
	public String stripInlineTranslations(String body) {
		//		if (is_array(body)) {
		//			for (array part : body) {
		//				this.stripInlineTranslations(part);
		//			}
		//		} else {
		return body.replaceAll(this._tokenRegex, "$1");
		//		}
	}

	/**
	 * Replace translate templates to HTML fragments
	 * 
	 * @param body
	 */
	public String processResponseBody(String body) {
		if (!this.isAllowed(null)) {
			if (AppContext.getCurrent().getDesignPackage().getArea() == AreaType.ADMINHTML) {
				body = this.stripInlineTranslations(body);
			}

			return body;
		}

		//		if (is_array(body)) {
		//			for (String part : body) {
		//				this.processResponseBody(part);
		//			}
		//
		//		} else {
		this._content = body;
		this._specialTags();
		this._tagAttributes();
		this._otherText();
		this._insertInlineScriptsHtml();
		body = this._content;
		//		}

		return body;
	}

	/**
	 * Add translate js to body
	 * 
	 */
	protected void _insertInlineScriptsHtml() {
		String html;

		if (this._isScriptInserted || !StringUtils.containsIgnoreCase(this._content, "</body>")) {
			return;
		}

		String baseJsUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl();
		String url_prefix = (AppContext.getCurrent().getStore().getHelper().isAdmin()) ? "adminhtml" : "core";
		RouteParams routeParams = new RouteParams();
		routeParams.setSecure(AppContext.getCurrent().getStore().getHelper().isCurrentlySecure());
		String ajaxUrl = AppContext.getCurrent().getUrl().getUrl(url_prefix + "/ajax/translate", routeParams);
		String trigImg = AppContext.getCurrent().getDesignPackage().getSkinUrl("images/fam_book_open.png", null);
		String magentoSkinUrl = AppContext.getCurrent().getDesignPackage().getSkinUrl("lib/prototype/windows/themes/magento.css", null);
		StringBuilder output = new StringBuilder();
		output.append("<!-- script type=\"text/javascript\" src=\"" + baseJsUrl + "prototype/effects.js\"></script -->");
		output.append("<script type=\"text/javascript\" src=\"" + baseJsUrl + "prototype/window.js\"></script>");
		output.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseJsUrl + "prototype/windows/themes/default.css\"/>");
		output.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + magentoSkinUrl + "\"/>");
		output.append("<script type=\"text/javascript\" src=\"" + baseJsUrl + "mage/translate_inline.js\"></script>");
		output.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseJsUrl + "mage/translate_inline.css\"/>");
		output.append("<div id=\"translate-inline-trig\"><img src=\"" + trigImg + "\" alt=\"[TR]\"/></div>");
		output.append("<script type=\"text/javascript\">");
		output.append("new TranslateInline('translate-inline-trig', '" + ajaxUrl + "', '" + AppContext.getCurrent().getDesignPackage().getArea().getCode() + "');");
		output.append("</script>");

		html = output.toString();
		this._content = this._content.replaceFirst("(?i)</body>", html + "</body>");
		this._isScriptInserted = true;
	}

	/**
	 * Escape Translate data
	 * 
	 * @param string
	 * @return string
	 */
	protected String _escape(String string) {
		return HtmlUtils.htmlspecialchars(string).replace("'", "\\\\'");
	}

	/**
	 * Get attribute location
	 * 
	 * @param matches
	 * @param options
	 * @return string
	 */
	//	protected String _getAttributeLocation(array matches, array options) {
	//		return "Tag attribute (ALT, TITLE, etc.)";
	//	}

	/**
	 * Get tag location
	 * 
	 * @param matches
	 * @param options
	 * @return string
	 */
	//	protected String _getTagLocation(array matches, Map options) {
	//		String tagName;
	//		tagName = options.get("tagName").toLowerCase();
	//		if (options.get("tagList").get(tagName) != null) {
	//			return options.get("tagList").get(tagName);
	//		}
	//
	//		return StringUtils.capitalize(tagName) + " Text";
	//	}

	/**
	 * Get translate data by regexp
	 * 
	 * @param regexp
	 * @param text
	 * @param locationCallback
	 * @param options
	 * @return array
	 */
	protected List<String> _getTranslateData(String regexp, String text, List locationCallback, Map options/* = Collections.emptyMap() */) {
		throw new UnsupportedOperationException();
		//		NoType next;
		//		List<String> trArr = new ArrayList<>();
		//		List m;
		//		next = 0;
		//		while (preg_match(regexp, text, m, PREG_OFFSET_CAPTURE, next)) {
		//			String json = JacksonUtil.toJson(MageAtomArray.createMap(
		//				new Object[] {"shown", m.get(1).get(0)},
		//				new Object[] {"translated", m.get(2).get(0)},
		//				new Object[] {"original", m.get(3).get(0)},
		//				new Object[] {"location", call_user_func(locationCallback, m, options)},
		//				new Object[] {"scope", m.get(4).get(0)}));
		//			trArr.add(json);
		//			text = substr_replace(text, m.get(1).get(0), m.get(0).get(1), m.get(0).get(0).length());
		//			next = m.get(0).get(1);
		//		}
		//
		//		return trArr;
	}

	/**
	 * Prepare tags inline translates
	 * 
	 */
	protected void _tagAttributes() {
		this._prepareTagAttributesForContent();
	}

	/**
	 * Prepare tags inline translates for the content
	 * 
	 * @param content
	 * @return void
	 */
	protected void _prepareTagAttributesForContent() {
		throw new UnsupportedOperationException();
		//		String quoteHtml;
		//		if (this.getIsJson()) {
		//			quoteHtml = "\\\"";
		//		} else {
		//			quoteHtml = "\"";
		//		}
		//
		//		Map tagMatch;
		//		NoType nextTag = 0;
		//		String tagRegExp = "(?iS)<([a-z]+)\\s*?[^>]+?((" + this._tokenRegex + ")[^>]*?)+\\\\\\\\?/?>";
		//		while (preg_match(tagRegExp, this._content, tagMatch, PREG_OFFSET_CAPTURE, nextTag)) {
		//			String tagHtml = tagMatch.get(0).get(0);
		//			Map m = Collections.emptyMap();
		//			String attrRegExp = "(?S)" + this._tokenRegex;
		//			String trArr = this._getTranslateData(attrRegExp, tagHtml, Arrays.asList(this, "_getAttributeLocation"), Collections.emptyMap());
		//			if (trArr) {
		//				String transRegExp = "(?i) translate=" + quoteHtml + "\\[([^" + preg_quote(quoteHtml) + "]*)]" + quoteHtml;
		//				String trAttr;
		//				if (preg_match(transRegExp, tagHtml, m)) {
		//					tagHtml = tagHtml.replace(m.get(0), "");
		//					trAttr = " translate=" + quoteHtml + HtmlUtils.htmlspecialchars("[" + m.get(1) + "," + join(",", trArr) + "]") + quoteHtml;
		//				} else {
		//					trAttr = " translate=" + quoteHtml + HtmlUtils.htmlspecialchars("[" + join(",", trArr) + "]") + quoteHtml;
		//				}
		//
		//				tagHtml = substr_replace(tagHtml, trAttr, tagMatch.get(1).get(0).length() + 1, 1);
		//				content = substr_replace(content, tagHtml, tagMatch.get(0).get(1), tagMatch.get(0).get(0).length());
		//			}
		//
		//			nextTag = tagMatch.get(0).get(1) + tagHtml.length();
		//		}
	}

	/**
	 * Get html quote symbol
	 * 
	 * @return string
	 */
	protected String _getHtmlQuote() {
		if (this.getIsJson()) {
			return "\\\"";
		} else {
			return "\"";
		}

	}

	/**
	 * Prepare special tags
	 * 
	 */
	protected void _specialTags() {
		this._translateTags(this._allowedTagsGlobal, "_applySpecialTagsFormat", false);
		this._translateTags(this._allowedTagsSimple, "_applySimpleTagsFormat", true);
	}

	/**
	 * Format translate for special tags
	 * 
	 * @param tagHtml
	 * @param  $tagName 
	 * @param trArr
	 * @return string
	 */
	//	protected String _applySpecialTagsFormat(String tagHtml, String tagName, array trArr) {
	//		return tagHtml + "<span class=\"translate-inline-" + tagName + "\" translate=" + this._getHtmlQuote() + HtmlUtils.htmlspecialchars("[" + join(",", trArr) + "]") + this._getHtmlQuote() + ">" + tagName.toUpperCase() + "</span>";
	//	}

	/**
	 * Format translate for simple tags
	 * 
	 * @param tagHtml
	 * @param  $tagName 
	 * @param trArr
	 * @return string
	 */
	//	protected String _applySimpleTagsFormat(String tagHtml, String tagName, array trArr) {
	//		return tagHtml.substring(0, tagName.length() + 1) + " translate=" + this._getHtmlQuote() + HtmlUtils.htmlspecialchars("[" + join(",", trArr) + "]") + this._getHtmlQuote() + tagHtml.substring(tagName.length() + 1);
	//	}

	/**
	 * Prepare simple tags
	 * 
	 * @param content
	 * @param tagsList
	 * @param formatCallback
	 * @param isNeedTranslateAttributes
	 */
	protected void _translateTags(Map<String, ?> tagsList, String formatCallback, boolean isNeedTranslateAttributes) {
		throw new UnsupportedOperationException();
		//		NoType nextTag;
		//		nextTag = 0;
		//		String tags = StringUtils.join(tagsList.keySet(), "|");
		//		String tagRegExp = "#<(" + tags + ")(/?>| \\s*[^>]*+/?>)#iSU";
		//		Map tagMatch = Collections.emptyMap();
		//		while (preg_match(tagRegExp, content, tagMatch, PREG_OFFSET_CAPTURE, nextTag)) {
		//			String tagName = tagMatch.get(1).get(0).toLowerCase();
		//			int tagClosurePos;
		//			if (tagMatch.get(0).get(0).substring(-2) == "/>") {
		//				tagClosurePos = tagMatch.get(0).get(1) + tagMatch.get(0).get(0).length();
		//			} else {
		//				tagClosurePos = this.findEndOfTag(content, tagName, tagMatch.get(0).get(1));
		//			}
		//
		//			if (tagClosurePos == false) {
		//				nextTag += tagMatch.get(0).get(0).length();
		//				continue;
		//			}
		//
		//			int tagLength = tagClosurePos - tagMatch.get(0).get(1);
		//			int tagStartLength = tagMatch.get(0).get(0).length();
		//			String tagHtml = tagMatch.get(0).get(0) + content.substring(tagMatch.get(0).get(1) + tagStartLength, tagLength - tagStartLength);
		//			tagClosurePos = tagMatch.get(0).get(1) + tagHtml.length();
		//			List trArr = this._getTranslateData("(?iS)" + this._tokenRegex, tagHtml, Arrays.asList(this, "_getTagLocation"), MageAtomArray.createMap(new Object[] {"tagName", tagName}, new Object[] {"tagList", tagsList}));
		//			if (!(trArr == null)) {
		//				trArr = array_unique(trArr);
		//				tagHtml = call_user_func(Arrays.asList(this, formatCallback), tagHtml, tagName, trArr);
		//				tagClosurePos = tagMatch.get(0).get(1) + tagHtml.length();
		//				content = substr_replace(content, tagHtml, tagMatch.get(0).get(1), tagLength);
		//			}
		//
		//			nextTag = tagClosurePos;
		//		}
	}

	/**
	 * Find end of tag
	 * 
	 * @param null
	 * @param null
	 * @param null
	 * @return bool|int  return false if end of tag is not found
	 */
	//	private boolean findEndOfTag(String body, String tagName, NoType from) {
	//		String closeTag;
	//		String openTag;
	//		int length;
	//		int tagLength;
	//		List tagMatch;
	//		int end;
	//		openTag = "<" + tagName;
	//		closeTag = ((this.getIsJson()) ? ("<\\\\/") : ("</")) + tagName;
	//		tagLength = tagName.length();
	//		length = tagLength + 1;
	//		end = from + 1;
	//		while (substr_count(body, openTag, from, length) != substr_count(body, closeTag, from, length)) {
	//			end = body.indexOf(closeTag, end + tagLength + 1);
	//			if (end == false) {
	//				return false;
	//			}
	//
	//			length = end - from + tagLength + 3;
	//		}
	//
	//		if (preg_match("#<\\\\\\\\?\\/" + tagName + "\\s*?>#i", body, tagMatch, null, end)) {
	//			return end + tagMatch.get(0).length();
	//		} else {
	//			return false;
	//		}
	//
	//	}

	/**
	 * Prepare other text inline translates
	 * 
	 */
	protected void _otherText() {
		throw new UnsupportedOperationException();
		//		String quoteHtml;
		//		if (this.getIsJson()) {
		//			quoteHtml = "\\\"";
		//		} else {
		//			quoteHtml = "\"";
		//		}
		//
		//		NoType next = 0;
		//		Map m = new HashMap<>();
		//		while (preg_match(this._tokenRegex, this._content, m, PREG_OFFSET_CAPTURE, next)) {
		//			String tr = json_encode(MageAtomArray.createMap(
		//				new Object[] {"shown", m.get(1).get(0)},
		//				new Object[] {"translated", m.get(2).get(0)},
		//				new Object[] {"original", m.get(3).get(0)},
		//				new Object[] {"location", "Text"},
		//				new Object[] {"scope", m.get(4).get(0)}));
		//			String spanHtml = "<span translate=" + quoteHtml + HtmlUtils.htmlspecialchars("[" + tr + "]") + quoteHtml + ">" + m.get(1).get(0) + "</span>";
		//			this._content = substr_replace(this._content, spanHtml, m.get(0).get(1), m.get(0).get(0).length());
		//			next = m.get(0).get(1) + spanHtml.length() - 1;
		//		}
	}

	/**
	 * Check is a Request contain Json flag
	 * 
	 * @return bool
	 */
	//	public boolean getIsAjaxRequest() {
	//		return (boolean)AppContext.getCurrent().getRequest().getQuery("isAjax");
	//	}

	/**
	 * Set is a Request contain Json flag
	 * 
	 * @param flag
	 * @return Mage_Core_Model_Translate_Inline
	 */
	//	public Mage_Core_Model_Translate_Inline setIsAjaxRequest(boolean flag) {
	//		AppContext.getCurrent().getRequest().setQuery("isAjax", intval(flag));
	//		return this;
	//	}

	/**
	 * Retrieve flag about parsed content is Json
	 * 
	 * @return bool
	 */
	public boolean getIsJson() {
		return this._isJson;
	}

	/**
	 * Set flag about parsed content is Json
	 * 
	 * @param flag
	 * @return Mage_Core_Model_Translate_Inline
	 */
	public Mage_Core_Model_Translate_Inline setIsJson(boolean flag) {
		this._isJson = flag;
		return this;
	}

}