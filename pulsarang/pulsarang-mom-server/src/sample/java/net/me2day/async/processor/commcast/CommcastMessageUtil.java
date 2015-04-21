/*
 * Commcast.java $version 2010. 8. 26
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.me2day.async.processor.commcast;

import java.util.List;

import net.me2day.async.model.App;
import net.me2day.async.model.Comment;
import net.me2day.async.model.Friend;
import net.me2day.async.model.Gift;
import net.me2day.async.model.Mention;
import net.me2day.async.model.Message;
import net.me2day.async.model.Post;
import net.me2day.async.util.StrUtil;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommcastMessageUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CommcastMessageUtil.class);

	/**
	 * json 메시지 구성
	 * 
	 * @param obj 구성할 개체
	 * @param idList 네이버 ID 리스트
	 * @return String json 메시지
	 */
	public static String makeJsonMessage(Object obj, List < Object > idList) {
		JSONObject replyKeyValueJSONObject = new JSONObject();
		JSONObject msgValueJSONObject = new JSONObject();
		JSONObject commcastValueJSONObject = new JSONObject();
		JSONObject wrapJSONObject = new JSONObject();

		long currentTime = System.currentTimeMillis();

		if (obj instanceof Post) {
			replyKeyValueJSONObject.put("post_id", ((Post) obj).getReplyKey());

			msgValueJSONObject.put("title", StrUtil.removeMe2Link(((Post) obj).getBody()));
			msgValueJSONObject.put("by", ((Post) obj).getAuthorName());
			msgValueJSONObject.put("htmlTitle", ((Post) obj).getBodyWithHtml());
			msgValueJSONObject.put("tag", ((Post) obj).getTag());
			msgValueJSONObject.put("replyKey", replyKeyValueJSONObject);
			msgValueJSONObject.put("profileUrl", StrUtil.profileImageUrl(((Post) obj).getAuthorUserId()));
			msgValueJSONObject.put("closeComment", ((Post) obj).getCloseComment());
			msgValueJSONObject.put("hasPhoto", ((Post) obj).getIsHasPhoto());
			msgValueJSONObject.put("murl", StrUtil.getMurl(((Post) obj).getAuthorUserId()));

			commcastValueJSONObject.put("userIds", idList.toArray());
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("message", msgValueJSONObject);
			commcastValueJSONObject.put("url", "http://me2day.net/" + ((Post) obj).getAuthorUserId());
		} else if (obj instanceof Friend) {
			msgValueJSONObject.put("by", ((Friend) obj).getAuthorName());
			msgValueJSONObject.put("content", ((Friend) obj).getContent());
			msgValueJSONObject.put("profileUrl", StrUtil.profileImageUrl(((Friend) obj).getToUserId()));

			commcastValueJSONObject.put("userId", ((Friend) obj).getToNaverUserId());
			commcastValueJSONObject.put("url", "http://me2day.net/" + ((Friend) obj).getToUserId());
			commcastValueJSONObject.put("tstamp", currentTime);
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("message", msgValueJSONObject);
		} else if (obj instanceof Gift) {
			msgValueJSONObject.put("title", "");
			msgValueJSONObject.put("by", ((Gift) obj).getAuthorName());
			msgValueJSONObject.put("murl", StrUtil.getMurl(((Gift) obj).getFromUserId()));
			msgValueJSONObject.put("profileUrl", StrUtil.profileImageUrl(((Gift) obj).getFromUserId()));

			commcastValueJSONObject.put("userId", ((Gift) obj).getToNaverUserId());
			commcastValueJSONObject.put("url", "http://me2day.net/" + ((Gift) obj).getToUserId());
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("tstamp", currentTime);
			commcastValueJSONObject.put("event_id", "eventId");
			commcastValueJSONObject.put("message", msgValueJSONObject);
		} else if (obj instanceof Message) {
			msgValueJSONObject.put("title", ((Message) obj).getMessage());
			msgValueJSONObject.put("by", ((Message) obj).getAuthorName());
			msgValueJSONObject.put("profileUrl", StrUtil.profileImageUrl(((Message) obj).getAuthorUserId()));

			commcastValueJSONObject.put("userId", ((Message) obj).getToNaverUserId());
			commcastValueJSONObject.put("url", "http://me2day.net/" + ((Message) obj).getToUserUserId() + "/mailet");
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("tstamp", currentTime);
			commcastValueJSONObject.put("message", msgValueJSONObject);
		} else if (obj instanceof Comment) {
			replyKeyValueJSONObject.put("post_id", StrUtil.encodedPostId(((Comment) obj).getPostId()));

			msgValueJSONObject.put("title", ((Comment) obj).getBody()); // 게시글 본문
			msgValueJSONObject.put("by", ((Comment) obj).getAuthorName()); // 댓글 작성자 닉네임
			msgValueJSONObject.put("cnt", ((Comment) obj).getCommentsCount()); // 댓글 수
			msgValueJSONObject.put("content", ((Comment) obj).getCommentBodyWithoutHtml()); // HTML 제거된 댓글
			msgValueJSONObject.put("htmlContent", ((Comment) obj).getCommentBody()); // 댓글 본문
			msgValueJSONObject.put("replyKey", replyKeyValueJSONObject);
			msgValueJSONObject.put("profileUrl", StrUtil.profileImageUrl(((Comment) obj).getAuthorUserId()));

			commcastValueJSONObject.put("userId", ((Comment) obj).getBodyAuthorUserId());
			commcastValueJSONObject.put("url", "http://me2day.net/" + ((Comment) obj).getBodyAuthorUserId());
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("tstamp", currentTime);
			commcastValueJSONObject.put("message", msgValueJSONObject);
		} else if (obj instanceof Mention) {
			replyKeyValueJSONObject.put("post_id", ((Mention) obj).getReplyKey());

			msgValueJSONObject.put("title", ((Mention) obj).getBody());
			msgValueJSONObject.put("by", ((Mention) obj).getAuthorName());
			msgValueJSONObject.put("htmlTitle", ((Mention) obj).getHtmlBody());
			msgValueJSONObject.put("tag", ((Mention) obj).getTag());
			msgValueJSONObject.put("replyKey", replyKeyValueJSONObject);
			msgValueJSONObject.put("profileUrl", StrUtil.profileImageUrl(((Mention) obj).getAuthorUserId()));
			msgValueJSONObject.put("closeComment", ((Mention) obj).getCloseComment());
			msgValueJSONObject.put("hasPhoto", ((Mention) obj).getIsHasPhoto());

			commcastValueJSONObject.put("userId", ((Mention) obj).getToUserId());
			commcastValueJSONObject.put("url", "http://me2day.net/" + ((Mention) obj).getToUserId());
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("tstamp", currentTime);
			commcastValueJSONObject.put("message", msgValueJSONObject);
		} else if (obj instanceof App) {
			msgValueJSONObject.put("by", ((App) obj).getAuthorName());
			msgValueJSONObject.put("requestMessage", ((App) obj).getMessage());
			msgValueJSONObject.put("appName", ((App) obj).getAppName());

			commcastValueJSONObject.put("url", ((App) obj).getAppLinkUrl());
			commcastValueJSONObject.put("createdTime", currentTime);
			commcastValueJSONObject.put("tstamp", currentTime);
		} else {
			LOG.error("[COMMCAST] invalid instance");
		}

		wrapJSONObject.put("commcast", commcastValueJSONObject);

		LOG.info("[COMMCAST-sending]" + wrapJSONObject.toString());

		return wrapJSONObject.toString();
	}
}
