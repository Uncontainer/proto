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
import net.me2day.async.event.EventCode;
import net.me2day.async.event.EventTarget;
import net.me2day.async.event.SimpleEventCode;
import net.me2day.async.me2day.UserBOProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommcastDeleteFriendSender extends AbstractCommcastProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(CommcastDeleteFriendSender.class);

	private static final String MODE_SINGLE = "single";
	private static final String OPERATION_DELETE = "delete";
	
	@Autowired
	private UserBOProxy userBo;

	@Override
	public List < ? extends EventCode > getEventCodes() {
		return Arrays.asList(new SimpleEventCode(EventTarget.FRIEND_REQUEST.getTargetName(), OPERATION_DELETE));
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		long createdTime = context.getEvent().getEventTime().getTime();

		LOG.info("[COMMCAST-delete] friend event received ================");

		String eventId = context.getEvent().getEventOption("eventId");

		String toNaverUserId = userBo.getNaverUserId(context.getEvent().getEventOption("toUserId"));
		this.sendByDeleteMethod(toNaverUserId, EventTarget.FRIEND_REQUEST.getCatId(), MODE_SINGLE,
					eventId, createdTime);
	}
}
