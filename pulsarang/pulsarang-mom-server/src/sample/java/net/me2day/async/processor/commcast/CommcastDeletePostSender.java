/*
 * Commcast.java $version 2010. 8. 26
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.me2day.async.processor.commcast;

import java.util.Arrays;
import java.util.List;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.ReprocessableException;
import net.me2day.async.dispatcher.processor.ProcessingDistributableEventProcesssor;
import net.me2day.async.event.EventCode;
import net.me2day.async.event.EventTarget;
import net.me2day.async.event.SimpleEventCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommcastDeletePostSender extends AbstractCommcastProcessor implements
	ProcessingDistributableEventProcesssor {

	private static final Logger LOG = LoggerFactory.getLogger(CommcastDeletePostSender.class);

	private static final String MODE_SINGLE = "single";
	private static final String OPERATION_DELETE = "delete";

	@Override
	public List < ? extends EventCode > getEventCodes() {
		return Arrays.asList(new SimpleEventCode(EventTarget.POST.getTargetName(), OPERATION_DELETE));
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void process(List < Object > targets, EventProcessContext context) throws ReprocessableException {
		String fireUserId = context.getEvent().getEventFireUserId();
		long createdTime = context.getEvent().getEventTime().getTime();

		// 삭제 통계 내기위한 임시
		LOG.info("[COMMCAST-delete] post event received ================" + " [" + fireUserId + "] 의 친구 수는 "
				+ targets.size() + "명");

		String eventId = context.getEvent().getEventOption("eventId");

		for (Object userId : targets) {
			sendByDeleteMethod((String) userId, EventTarget.POST.getCatId(), MODE_SINGLE, eventId, createdTime);
		}
	}

	@Override
	public String getDistributorBeanName() {
		return FriendDistributor.NAME;
	}
}
