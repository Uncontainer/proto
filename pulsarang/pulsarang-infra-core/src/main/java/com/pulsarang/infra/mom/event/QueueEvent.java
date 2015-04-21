package com.pulsarang.infra.mom.event;

public class QueueEvent extends Event {

	public QueueEvent() {
	}

	public QueueEvent(QueueEventType eventType) {
		super.setEventType(eventType);
	}

	@Override
	public void setEventType(EventType eventType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public QueueEventType getEventType() {
		return (QueueEventType) super.getEventType();
	}

	public String getQueueName() {
		QueueEventType eventType = getEventType();
		if (eventType == null) {
			return null;
		}

		return eventType.getQueueName();
	}

	public void setQueueName(String queueName) {
		super.setEventType(new QueueEventType(queueName));
	}
}
