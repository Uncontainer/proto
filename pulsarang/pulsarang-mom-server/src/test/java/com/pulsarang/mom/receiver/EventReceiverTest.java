//package com.pulsarang.mom.receiver;
//
//import junit.framework.Assert;
//
//import org.apache.commons.httpclient.HttpStatus;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import com.pulsarang.mom.core.event.Event;
//import com.pulsarang.mom.core.receiver.EventPushResult;
//import com.pulsarang.mom.dispatcher.router.TableEventScanner;
//
//public class EventReceiverTest {
//
//	@Test
//	public void pushTest() throws InterruptedException {
//		EventReceiverImpl receiver = new EventReceiverImpl();
//		Event event = new Event();
//
//		TableEventScannerSelector selector = Mockito.mock(TableEventScannerSelector.class);
//		receiver.tableSelector = selector;
//
//		TableSelection queueSelection = Mockito.mock(TableSelection.class);
//		Mockito.when(selector.select(event)).thenReturn(queueSelection);
//
//		TableEventScanner fetcher = Mockito.mock(TableEventScanner.class);
//		Mockito.when(queueSelection.getTableEventFetcher()).thenReturn(fetcher);
//
//		EventPushResult eventPushResult = receiver.push(event);
//
//		Mockito.verify(fetcher).push(event);
//		Assert.assertEquals(eventPushResult.getResponseCode(), HttpStatus.SC_OK);
//	}
//}
