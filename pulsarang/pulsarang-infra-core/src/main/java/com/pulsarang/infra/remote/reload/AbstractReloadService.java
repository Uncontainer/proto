package com.pulsarang.infra.remote.reload;

import java.util.List;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.RemoteServiceRequest;
import com.pulsarang.infra.remote.annotation.RemoteMethod;

public class AbstractReloadService implements ReloadService {
	@Override
	@RemoteMethod
	public void expire(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "expire");
	}

	@Override
	@RemoteMethod
	public MapModel get(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "get");
	}

	@Override
	@RemoteMethod
	public void refresh(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "refresh");
	}

	@Override
	@RemoteMethod
	public void validate(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "validate");
	}

	@Override
	@RemoteMethod
	public List<MapModel> list(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "list");
	}

	@Override
	@RemoteMethod
	public int listCount(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "listCount");
	}

	@Override
	@RemoteMethod
	public Object add(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "add");
	}

	@Override
	@RemoteMethod
	public int modify(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "modify");
	}

	@Override
	@RemoteMethod
	public int remove(MapModel option) {
		throw createException(((RemoteServiceRequest) option).getTarget(), "remove");
	}

	private UnsupportedOperationException createException(String target, String name) {
		return new UnsupportedOperationException("'" + target + "' dost not support '" + name + "'.");
	}
}
