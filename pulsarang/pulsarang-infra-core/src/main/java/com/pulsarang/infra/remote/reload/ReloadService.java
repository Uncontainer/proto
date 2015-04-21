package com.pulsarang.infra.remote.reload;

import java.util.List;

import com.pulsarang.core.util.MapModel;

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
