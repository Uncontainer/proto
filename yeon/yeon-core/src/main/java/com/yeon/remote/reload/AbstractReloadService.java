package com.yeon.remote.reload;

import com.yeon.remote.annotation.RemoteMethod;
import com.yeon.util.MapModel;

import java.util.List;

public class AbstractReloadService implements ReloadService {
	@Override
	@RemoteMethod
	public void expire(MapModel option) {
		throw createException("", "expire");
	}

	@Override
	@RemoteMethod
	public MapModel get(MapModel option) {
		throw createException("", "get");
	}

	@Override
	@RemoteMethod
	public void refresh(MapModel option) {
		throw createException("", "refresh");
	}

	@Override
	@RemoteMethod
	public void validate(MapModel option) {
		throw createException("", "validate");
	}

	@Override
	@RemoteMethod
	public List<MapModel> list(MapModel option) {
		throw createException("", "list");
	}

	@Override
	@RemoteMethod
	public int listCount(MapModel option) {
		throw createException("", "listCount");
	}

	@Override
	@RemoteMethod
	public Object add(MapModel option) {
		throw createException("", "add");
	}

	@Override
	@RemoteMethod
	public int modify(MapModel option) {
		throw createException("", "modify");
	}

	@Override
	@RemoteMethod
	public int remove(MapModel option) {
		throw createException("", "remove");
	}

	private UnsupportedOperationException createException(String target, String name) {
		return new UnsupportedOperationException("'" + target + "' dost not support '" + name + "'.");
	}
}
