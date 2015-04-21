package com.naver.fog.frame.field;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrameFieldService {
	@Autowired
	private FrameFieldDao frameFieldDao;

	public void add(FrameField frameField) {
		frameFieldDao.insert(frameField);
	}

	public List<Long> listByFrame(long frameId) {
		return frameFieldDao.selectIdsByFrame(frameId);
	}

	public List<Long> listFrameIdsByField(long fieldId, int count) {
		return frameFieldDao.selectFrameIdsByFieldId(fieldId, count);
	}

	public int remove(long frameId, long fieldId) {
		return frameFieldDao.delete(frameId, fieldId);
	}
}
