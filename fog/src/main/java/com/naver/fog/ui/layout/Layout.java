package com.naver.fog.ui.layout;

import java.util.Map;

import org.htmlcleaner.TagNode;

import com.naver.fog.content.Content;
import com.naver.fog.web.ViewMode;

/**
 * {@link Content}의 layout을 정의한다.<br/>
 * 새로 정의한 layout은 반드시 {@link LayoutFactory#regist(String, Class)}를 이용하여 등록해 줘야한다.
 * 
 * @see LayoutFactory
 * @author pulsarang
 */
public interface Layout {
	/**
	 * Layout을 적용할 타켓의 리소스 ID를 돌려준다.
	 * @return
	 */
	long getTargetResourceId();

	/**
	 * Layout의 별칭을 돌려준다.<br/>
	 * 별칭으로 layout을 생성하기 때문에 다른 layout 정의와 겹쳐서는 안된다.
	 * @return
	 */
	String getAlias();

	/**
	 * 주어진 {@code mode}에서 이 layout이 보여질지의 여부를 돌려준다.
	 * @param mode
	 * @return
	 */
	boolean isEnabled(ViewMode mode);

	/**
	 * 주어진 {@code mode}에 해당하는 html template을 {@code builder}에 기록한다.
	 * 
	 * @param builder
	 * @param mode
	 */
	void toTemplateHtml(HtmlBuilder builder, ViewMode mode);

	/**
	 * 레이이웃 html을 {@code builder}에 기록한다.
	 * 
	 * @param builder
	 */
	void toLayoutHtml(HtmlBuilder builder);

	/**
	 * 주어진 layout의 {@code parentTag}로부터 복구한 내용으로 이 객체를 설정한다.
	 * @param parentTag
	 */
	void fromLayoutTag(TagNode parentTag);

	/**
	 * 주어진 {@code layout}을 하위 요소로 추가한다.
	 * 
	 * @param layout
	 */
	void add(Layout layout);

	/**
	 * 레이아웃의 예상 너비를 돌려준다.
	 * @return
	 */
	int getExpectedWidth(Map<Long, Object> valueMap);
}
