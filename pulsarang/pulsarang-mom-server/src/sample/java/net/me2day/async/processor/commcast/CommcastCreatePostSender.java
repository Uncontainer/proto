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
import net.me2day.async.dispatcher.processor.ProcessingDistributableEventProcesssor;
import net.me2day.async.event.EventCode;
import net.me2day.async.event.EventTarget;
import net.me2day.async.event.SimpleEventCode;
import net.me2day.async.me2day.UserBOProxy;
import net.me2day.async.model.Post;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommcastCreatePostSender extends AbstractCommcastProcessor implements
	ProcessingDistributableEventProcesssor {

	private static final Logger LOG = LoggerFactory.getLogger(CommcastCreatePostSender.class);

	private static final String MODE_MULTIPLE = "multiple";
	private static final String OPERATION_CREATE = "create";

	@Autowired
	private UserBOProxy userBo;

	@Override
	public List < ? extends EventCode > getEventCodes() {
		return Arrays.asList(new SimpleEventCode(EventTarget.POST.getTargetName(), OPERATION_CREATE));
	}

	@Override
	public void process(List < Object > targets, EventProcessContext context) throws ReprocessableException {
		if (targets.size() == 0) {
			return;
		}
		
		String fireUserId = context.getEvent().getEventFireUserId();
		String fireNaverUserId = userBo.getNaverIdByMe2dayUserId(fireUserId);
		String eventId = "";
		String linkDate = "";

		if (StringUtils.isEmpty(fireNaverUserId)) {
			LOG.info("[COMMCAST] fireUserId(" + fireUserId + ") have NO naverId.");
			return;
		}

		Map < String, Object > resultMap = null;

		LOG.info("[COMMCAST] post event received ================");

		try {
			linkDate = CommcastUtil.getLinkDate(context.getEvent().getEventOption("registered"));
		} catch (Exception e) {
			LOG.error("[COMMCAST-comment] linkDate : " + e.getMessage());
		}

		Post post = new Post(
						Integer.parseInt(context.getEvent().getEventOption("authorId")),
						context.getEvent().getEventOption("authorUserId"),
						context.getEvent().getEventOption("body"),
						context.getEvent().getEventOption("authorName"),
						context.getEvent().getEventOption("bodyWithHtml"),
						context.getEvent().getEventOption("tag"),
						context.getEvent().getEventOption("replyKey"),
						context.getEvent().getEventOption("closeComment"),
						StringUtils.equals(context.getEvent().getEventOption("hasPhoto"), "true") ? true : false,
						linkDate
					);

		eventId = context.getEvent().getEventOption("eventId");

		// 컴캐 개발 서버 부하 때문에 특정 아이디만 보내도록 함
		LOG.info("[COMMCAST] 특정 아이디임! : " + fireNaverUserId);
		resultMap = sendByPutMethod(post, targets, EventTarget.POST.getCatId(), MODE_MULTIPLE, eventId);

		LOG.info(String.format("[COMMCAST](100명 이하, " + targets.size()
					+ ") [%s]님이 새 글을 썼고 , 그의 친구(들) [%s]에게 친구새글 알림을 보냈습니다.", post.getAuthorUserId(), targets));

		LOG.info("[COMMCAST] ====================================");
		if (resultMap != null && StringUtils.equals((String) resultMap.get("result"), "1")) {
			// TODO
			// LOG.info(String.format("[COMMCAST] target name : %s, sending result : %s", target,
			// resultMap));
		}
	}

	@Override
	public String getDistributorBeanName() {
		return FriendDistributor.NAME;
	}
}
