package com.yeon.lang.listener;

import com.yeon.lang.Resource;

import java.util.List;

/**
 * @author pulsarang
 */
public interface ResourceListener {
	void validate(ListenTarget target, Resource nextTicket, Resource currentTicket) throws Exception;

	void validated(ListenTarget target, Resource currentTicket, Resource previousTicket);

	void changed(ListenTarget target, Resource currentTicket, Resource previousTicket);

	List<ListenTarget> getTargets();
}
