package net.me2day.async.processor.commcast;

import java.util.List;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.processor.EventProcessingDistributor;
import net.me2day.async.me2day.UserBOProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = FriendDistributor.NAME)
@Scope(value = "prototype")
public class FriendDistributor implements EventProcessingDistributor {
	public static final String NAME = "friendSpliter";

	private List < Object > friendIds;
	private EventProcessContext eventProcessContext;

	private static final int NUMBER_PER_PART = 100;
	private int index = 0;

	@Autowired
	private UserBOProxy userBo;

	public FriendDistributor() {
	}

	@Override
	public List < Object > getNextTarget() {
		if (friendIds == null) {
			friendIds = getFriendIdList();
			if (friendIds.isEmpty()) {
				return null;
			}
		}

		if (index < friendIds.size()) {
			int toIndex = index + NUMBER_PER_PART;
			if (toIndex > friendIds.size()) {
				toIndex = friendIds.size();
			}

			try {
				return friendIds.subList(index, index + NUMBER_PER_PART);
			} finally {
				index = toIndex;
			}
		}

		return null;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private List < Object > getFriendIdList() {
		int authorId = Integer.parseInt(eventProcessContext.getEvent().getEventOption("authorId"));
		return (List) userBo.getMyFriendIdList(authorId);
	}

	@Override
	public void setContext(EventProcessContext eventProcessContext) {
		this.eventProcessContext = eventProcessContext;
	}
}
