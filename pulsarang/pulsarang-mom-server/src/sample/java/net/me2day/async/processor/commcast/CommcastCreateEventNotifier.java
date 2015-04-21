/*
 * Commcast.java $version 2010. 8. 26
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.me2day.async.processor.commcast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.ReprocessableException;
import net.me2day.async.event.EventCode;
import net.me2day.async.event.EventTarget;
import net.me2day.async.event.SimpleEventCode;
import net.me2day.async.me2day.UserBOProxy;
import net.me2day.async.model.App;
import net.me2day.async.model.Comment;
import net.me2day.async.model.Friend;
import net.me2day.async.model.Gift;
import net.me2day.async.model.Mention;
import net.me2day.async.model.Message;
import net.me2day.async.util.StrUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommcastCreateEventNotifier extends AbstractCommcastProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(CommcastCreateEventNotifier.class);

	private static final String MODE_SINGLE = "single";
	private static final String OPERATION_CREATE = "create";

	@Autowired
	private UserBOProxy userBo;

	@Override
	public List < ? extends EventCode > getEventCodes() {
		return Arrays.asList(new SimpleEventCode(null, OPERATION_CREATE));
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		String target = context.getEvent().getEventTarget();
		String fireUserId = context.getEvent().getEventFireUserId();
		String fireNaverUserId = userBo.getNaverIdByMe2dayUserId(fireUserId);
		String toNaverUserId = "";
		String toUserId = "";
		String eventId = "";

		if (StringUtils.isEmpty(fireNaverUserId)) {
			LOG.info("[COMMCAST] fireUserId(" + fireUserId + ") have NO naverId.");
			return;
		}

		Map < String, Object > resultMap = null;

		if (StringUtils.equals(target, EventTarget.FRIEND_REQUEST.getTargetName())) {
			LOG.info("[COMMCAST] requestFriend event received =======");

			toNaverUserId = userBo.getNaverUserId(context.getEvent().getEventOption("toUserId"));

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("toUserId") + ") have NO naverId.");
				return;
			}

			Friend friend = new Friend(
								context.getEvent().getEventOption("authorName"),
								context.getEvent().getEventOption("toUserId"),
								context.getEvent().getEventOperation(),
								toNaverUserId,
								context.getEvent().getEventOption("content")
							);

			eventId = context.getEvent().getEventOption("eventId");

			resultMap = sendByPutMethod(friend, null, EventTarget.FRIEND_REQUEST.getCatId(), MODE_SINGLE, eventId);

			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]에게 친신했습니다.", fireUserId, friend.getToUserId()));
			LOG.info("[COMMCAST] ====================================");
		} else if (StringUtils.equals(target, EventTarget.GIFT.getTargetName())) {
			LOG.info("[COMMCAST] gift event received ================");

			toNaverUserId = userBo.getNaverUserId(context.getEvent().getEventOption("toUserId"));

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("toUserId") + ") have NO naverId.");
				return;
			}

			Gift gift = new Gift(
					fireUserId,
					context.getEvent().getEventOption("authorName"),
					context.getEvent().getEventOption("toUserId"),
					context.getEvent().getEventOperation(),
					toNaverUserId
				);

			eventId = context.getEvent().getEventOption("eventId");

			resultMap = sendByPutMethod(gift, null, EventTarget.GIFT.getCatId(), MODE_SINGLE, eventId);
			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]에게 선물을 보냈습니다.", fireUserId, gift.getToUserId()));
			LOG.info("[COMMCAST] ====================================");
		} else if (StringUtils.equals(target, EventTarget.MESSAGE.getTargetName())) {
			LOG.info("[COMMCAST] message event received =============");

			toNaverUserId = userBo.getNaverUserId(context.getEvent().getEventOption("toUserId"));

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("toUserId") + ") have NO naverId.");
				return;
			}

			if (StringUtils.isEmpty(context.getEvent().getEventOption("authorName"))
				|| StringUtils.isEmpty(context.getEvent().getEventOption("toUserId"))) {
				LOG.info("[COMMCAST] message invalid argument");
				return;
			}

			Message message = new Message(
								fireUserId,
								context.getEvent().getEventOption("authorName"),
								context.getEvent().getEventOption("toUserId"),
								context.getEvent().getEventOption("message"),
								toNaverUserId
							);

			message.setToUserUserId(context.getEvent().getEventOption("toUserId"));

			eventId = context.getEvent().getEventOption("eventId");

			resultMap = sendByPutMethod(message, null, EventTarget.MESSAGE.getCatId(), MODE_SINGLE, eventId);
			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]에게 쪽지를 보냈습니다.", fireUserId, message.getToUserUserId()));
			LOG.info("[COMMCAST] ====================================");
		} else if (StringUtils.equals(target, EventTarget.COMMENT.getTargetName())) {
			LOG.info("[COMMCAST] comment event received =============");

			toNaverUserId = userBo.getNaverUserId(context.getEvent().getEventOption("bodyAuthorUserId"));

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("bodyAuthorUserId")
					+ ") have NO naverId.");
				return;
			}

			Comment comment = new Comment(
				context.getEvent().getEventOption("postContent"),
				Integer.parseInt(context.getEvent().getEventOption("commentsCount")),
				context.getEvent().getEventOption("authorName"),
				context.getEvent().getEventOption("commentBody"),
				context.getEvent().getEventOption("commentBodyWithoutHtml"),
				toNaverUserId,
				Integer.parseInt(context.getEvent().getEventOption("postId")),
				fireUserId,
				context.getEvent().getEventOption("registered")
				);

			eventId = StrUtil.encodedPostIdForComment(Integer.parseInt(context.getEvent().getEventOption("postId")));

			resultMap = sendByPutMethod(comment, null, EventTarget.COMMENT.getCatId(), MODE_SINGLE, eventId);
			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]에게 댓글을 달았습니다.", fireUserId, comment.getBodyAuthorUserId()));
			LOG.info("[COMMCAST] ====================================");
		} else if (StringUtils.equals(target, EventTarget.FRIEND_ACCEPT.getTargetName())) {
			LOG.info("[COMMCAST] acceptFriend event received ========");

			toNaverUserId = userBo.getNaverUserId(fireUserId);

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("fireUserId")
					+ ") have NO naverId.");
				return;
			}

			Friend friend = new Friend(
					context.getEvent().getEventOption("toUserName"),
					context.getEvent().getEventOption("toUserId"),
					context.getEvent().getEventOperation(),
					toNaverUserId,
					context.getEvent().getEventOption("content")
				);

			eventId = context.getEvent().getEventOption("eventId");

			resultMap = sendByPutMethod(friend, null, EventTarget.FRIEND_ACCEPT.getCatId(), MODE_SINGLE, eventId);

			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]님의 친신을 수락했습니다.", fireUserId, friend.getToUserId()));
			LOG.info("[COMMCAST] ====================================");
		} else if (StringUtils.equals(target, EventTarget.MENTION.getTargetName())) {
			LOG.info("[COMMCAST] mention event received =============");

			toUserId = context.getEvent().getEventOption("toUserId");
			toNaverUserId = userBo.getNaverUserId(toUserId);
			String linkDate = "";

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("toUserId") + ") have NO naverId.");
				return;
			}

			try {
				linkDate = CommcastUtil.getLinkDate(context.getEvent().getEventOption("registered"));
			} catch (Exception e) {
				LOG.error("[COMMCAST-mention] linkDate : " + e.getMessage());
			}

            Mention mention = new Mention(
            		fireUserId,
            		context.getEvent().getEventOption("title"),
            		context.getEvent().getEventOption("tag"),
            		context.getEvent().getEventOption("authorName"),
            		toNaverUserId,
            		context.getEvent().getEventOption("htmlTitle"),
            		context.getEvent().getEventOption("replyKey"),
            		context.getEvent().getEventOption("closeComment"),
       				StringUtils.equals(context.getEvent().getEventOption("hasPhoto"), "true") ? true : false,
       				linkDate
            		);

            eventId = context.getEvent().getEventOption("eventId");

			resultMap = sendByPutMethod(mention, null, EventTarget.MENTION.getCatId(), MODE_SINGLE, eventId);

			LOG.info("[COMMCAST] closeComment : " + context.getEvent().getEventOption("closeComment"));
			LOG.info("[COMMCAST] isHasPhoto : " + context.getEvent().getEventOption("hasPhoto"));
			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]님을 소환했습니다.", fireUserId, toUserId));
			LOG.info("[COMMCAST] ====================================");
		} else if (StringUtils.equals(target, EventTarget.APP.getTargetName())) {
			LOG.info("[COMMCAST] app event received =============");

			toUserId = context.getEvent().getEventOption("toUserId");
			toNaverUserId = userBo.getNaverUserId(toUserId);

			if (StringUtils.isEmpty(toNaverUserId)) {
				LOG.info("[COMMCAST] toUserId(" + context.getEvent().getEventOption("toUserId") + ") have NO naverId.");
				return;
			}

			App app = new App(
					fireUserId,
					context.getEvent().getEventOption("authorName"),
					context.getEvent().getEventOption("toUserId"),
					context.getEvent().getEventOption("message"),
					toNaverUserId,
					context.getEvent().getEventOption("appName"),
					context.getEvent().getEventOption("appId"),
					context.getEvent().getEventOption("appLinkUrl"),
					context.getEvent().getEventOption("type")
					);

			eventId = context.getEvent().getEventOption("eventId");

			resultMap = sendByPutMethod(app, null, EventTarget.APP.getCatId(), MODE_SINGLE, eventId);

			LOG.info(String.format("[COMMCAST] [%s]님이 [%s]님에게 [%s] 앱 추천을 했습니다.", fireUserId, toUserId, app.getAppName()));
			LOG.info("[COMMCAST] ====================================");
		}

		if (resultMap != null && StringUtils.equals((String) resultMap.get("result"), "1")) {
			// TODO
			// LOG.info(String.format("[COMMCAST] target name : %s, sending result : %s", target,
			// resultMap));
		}
	}
}
