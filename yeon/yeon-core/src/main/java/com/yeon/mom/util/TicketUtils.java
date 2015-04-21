package com.yeon.mom.util;

import com.yeon.YeonContext;
import com.yeon.YeonPredefinedClassId;
import com.yeon.YeonPredefinedResourceId;
import com.yeon.lang.NamedResourceId;
import com.yeon.lang.ResourceServiceUtils;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.MomInfo;
import com.yeon.mom.ProjectInfo;
import com.yeon.server.ProjectId;
import com.yeon.server.Server;
import com.yeon.util.LocalAddressHolder;

public class TicketUtils {
	static String projectId;
	static String serverId;

	public static String getServerId() {
		String si = serverId;
		if (si == null) {
			si = serverId = LocalAddressHolder.getLocalAddress() + "$" + getProjectId();
		}

		return si;
	}

	public static String getProjectId() {
		String pi = projectId;
		if (pi == null) {
			pi = projectId = ProjectId.toString(YeonContext.getSolutionName(), YeonContext.getProjectName());
		}

		return pi;
	}

	public static MomInfo getMomInfo() {
		return ResourceServiceUtils.getResourceSafely(YeonPredefinedResourceId.MOM, MomInfo.class);
	}

	public static ProjectInfo getProjectInfo() {
		NamedResourceId configId = new NamedResourceId("en", getProjectId(), YeonPredefinedClassId.PROJECT);

		return ResourceServiceUtils.getResourceSafely(configId, ProjectInfo.class);
	}

	public static Server getServer() {
		NamedResourceId configId = new NamedResourceId("en", getServerId(), YeonPredefinedClassId.SERVER);

		return ResourceServiceUtils.getResourceSafely(configId, Server.class);
	}

	public static EventProcessorInfo getEventProcessorInfo(String canonicalName) {
		NamedResourceId configId = new NamedResourceId("en", canonicalName, YeonPredefinedClassId.EVENT_PROCESSOR);

		return ResourceServiceUtils.getResourceSafely(configId, EventProcessorInfo.class);
	}
}
