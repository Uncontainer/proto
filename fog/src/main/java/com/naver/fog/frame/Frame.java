package com.naver.fog.frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.naver.fog.IdeaResource;
import com.naver.fog.Resource;
import com.naver.fog.ResourceType;
import com.naver.fog.field.Field;

public class Frame extends IdeaResource {
	private transient List<Frame> parents;
	private transient List<Field> fields;

	@Override
	public ResourceType getType() {
		return ResourceType.FRAME;
	}

	public synchronized List<Frame> getParents() {
		if (parents == null) {
			parents = FrameContext.getContext().getParents(id);
		}

		if (!parents.isEmpty()) {
			parents = Collections.unmodifiableList(parents);
		}

		return parents;
	}

	public boolean isChildOf(long frameId) {
		for (Frame parent : getParents()) {
			if (parent.getId() == frameId) {
				return true;
			}
		}

		for (Frame parent : getParents()) {
			if (parent.isChildOf(frameId)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasParent() {
		return !getParents().isEmpty();
	}

	public synchronized List<Field> getFields() {
		if (fields == null) {
			fields = FrameContext.getContext().getFields(id);
		}

		if (!fields.isEmpty()) {
			fields = Collections.unmodifiableList(fields);
		}

		return fields;
	}

	public List<Field> getFieldsAll() {
		// TODO 캐싱 가능여부 확인
		List<Field> fields = fillFields(new ArrayList<Field>());
		Collections.reverse(fields);

		return fields;
	}

	private List<Field> fillFields(List<Field> allFields) {
		allFields.addAll(getFields());
		for (Frame parent : getParents()) {
			parent.fillFields(allFields);
		}

		return allFields;
	}

	@Override
	public void replaceWith(Resource other) {
		if (!(other instanceof Frame)) {
			throw new IllegalArgumentException();
		}

		Frame otherFrame = (Frame)other;
		super.replaceWith(otherFrame);
		this.parents = null;
		this.fields = null;
	}
}
