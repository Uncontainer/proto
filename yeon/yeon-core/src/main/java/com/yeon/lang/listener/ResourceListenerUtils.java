package com.yeon.lang.listener;

import com.yeon.lang.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceListenerUtils {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceListenerUtils.class);

	// TODO ResourceListenerRegistry 설정 코드 추가.
	private static ResourceListenerRegistry registry;

	public static void validate(ListenTarget target, Resource nextResource, Resource currentResource) throws Exception {
		List<ResourceListener> listeners = registry.getListeners(target);
		for (ResourceListener listener : listeners) {
			listener.validate(target, nextResource, currentResource);
		}
	}

	public static void validated(ListenTarget target, Resource currentResource, Resource previousResource) {
		List<ResourceListener> listeners = registry.getListeners(target);
		for (ResourceListener listener : listeners) {
			try {
				listener.validated(target, currentResource, previousResource);
			} catch (Throwable t) {
				LOG.error("[YEON] Fail to apply some changed configuration. But, successfully loaded.", t);
			}
		}
	}

	public static void changed(ListenTarget target, Resource currentResource, Resource previousResource) {
		List<ResourceListener> listeners = registry.getListeners(target);
		for (ResourceListener listener : listeners) {
			try {
				listener.changed(target, currentResource, previousResource);
			} catch (Throwable t) {
				LOG.error("[YEON] Fail to apply some changed configuration. But, successfully loaded.", t);
			}
		}
	}
}
