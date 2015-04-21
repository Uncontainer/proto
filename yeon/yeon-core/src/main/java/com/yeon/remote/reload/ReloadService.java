package com.yeon.remote.reload;

import com.yeon.util.MapModel;

import java.util.List;

public interface ReloadService {
	List<MapModel> list(MapModel option);

	int listCount(MapModel option);

	MapModel get(MapModel option);

	void refresh(MapModel option);

	void expire(MapModel option);

	void validate(MapModel option);

	Object add(MapModel option);

	int modify(MapModel option);

	int remove(MapModel option);
}
