package com.yeon.mom.event;

import com.yeon.mom.EventProcessorInfo;

import java.util.Date;

public interface EventApiService {
	String NAME = "_EVENT_API";

	void publish(Event event);

	long reserveSerial(int amount);

	void retry(Event event);

	void logProcessing(EventProcessInfo eventProcessInfo);

	void registEventProcessor(EventProcessorInfo eventProcessInfo);

	void delayProcess(String eventProcessorName, Date pauseStartDate, Date pauseEndDate);
}