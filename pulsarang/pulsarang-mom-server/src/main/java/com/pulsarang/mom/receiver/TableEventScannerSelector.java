package com.pulsarang.mom.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.mom.dispatcher.router.TableEventScanner;

@Component
public class TableEventScannerSelector {
	public static final int TABLE_COUNT = 4;

	@Autowired
	private ApplicationContext applicationContext;

	private TableEventScanner[] scanners;

	public TableEventScanner select(Event event) {
		long channelNo = event.getChannelNo();

		// TODO bucket 분산 로직과 겹친다.
		int queueNo = (int) (channelNo % TABLE_COUNT);
		return scanners[queueNo];
	}

	public void init() {
		int tableCount = TableEventScannerSelector.TABLE_COUNT;
		scanners = new TableEventScanner[tableCount];
		for (int i = 0; i < tableCount; i++) {
			scanners[i] = (TableEventScanner) applicationContext.getBean(TableEventScanner.NAME, i);
			scanners[i].init();
		}
	}

	public void destroy() throws Exception {
		for (TableEventScanner scanner : scanners) {
			scanner.destory();
		}
	}
}
