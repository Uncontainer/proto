//package com.pulsarang.mom.dispatcher.router;
//
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import com.pulsarang.mom.core.event.Event;
//import com.pulsarang.mom.core.event.SimpleEventType;
//import com.pulsarang.mom.store.EventEnvelope;
//
//public class TableEventScannerTest {
//
//	@Test
//	public void test() throws InterruptedException {
//		TableEventScanner scanner = new TableEventScanner(Integer.MAX_VALUE);
//		EventRouterExecutor eventRouterExecutor = Mockito.mock(EventRouterExecutor.class);
//		scanner.bufferSize = 1;
//		scanner.eventRouterExecutors = new EventRouterExecutor[] { eventRouterExecutor };
//		scanner.start();
//
//		Event event = new Event(new SimpleEventType("dummy", "dummy"));
//		event.setString("message", "xxx");
//		event.setTarget("x");
//		scanner.push(event);
//
//		Thread.sleep(500);
//		event = new Event(new SimpleEventType("dummy", "dummy"));
//		event.setString("message", "yyy");
//		event.setTarget("y");
//		scanner.push(event);
//
//		Thread.sleep(100);
//
//		Mockito.verify(eventRouterExecutor, Mockito.times(2)).process(Mockito.any(EventEnvelope.class), Mockito.anyBoolean());
//
//		scanner.stop();
//	}
//}
