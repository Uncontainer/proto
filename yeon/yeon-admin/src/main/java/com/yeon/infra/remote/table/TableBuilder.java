package com.yeon.infra.remote.table;

import com.yeon.util.MapModel;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TableBuilder {
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final Map<String, TableInfo> tableInfos = new ConcurrentHashMap<String, TableInfo>();

	static {
		//		TableInfo tableInfo = new TableInfo(EventQueueAdminMonitor.NAME);
		//		tableInfo.columns.add(new Column("queue", 200, "left"));
		//		tableInfo.columns.add(new Column("syncSuccess<br>count", 100, "center"));
		//		tableInfo.columns.add(new Column("syncFail<br>count", 100, "center"));
		//		tableInfo.columns.add(new Column("lastPushTime", 100, "center"));
		//		tableInfos.put(tableInfo.name, tableInfo);

		//		tableInfo = new TableInfo(EventQueueMonitor.NAME);
		//		tableInfo.columns.add(new Column("eventType", 100, "left"));
		//		tableInfo.columns.add(new Column("name", 150, "left"));
		//		tableInfo.columns.add(new Column("activeCount", 100, "center"));
		//		tableInfo.columns.add(new Column("executeCount", 100, "center"));
		//		tableInfo.columns.add(new Column("failCount", 100, "center"));
		//		tableInfo.columns.add(new Column("processEvents", 300, "left"));
		//		//		tableInfo.columns.add(new Column("closed", 100, "center"));
		//		tableInfos.put(tableInfo.name, tableInfo);
		//
		//		tableInfo = new TableInfo(EventProcessingMonitor.NAME);
		//		tableInfo.columns.add(new Column("_class", 70, "center"));
		//		tableInfo.columns.add(new Column("_op", 120, "center"));
		//		tableInfo.columns.add(new Column("evt_id", 100, "center"));
		//		tableInfo.columns.add(new Column("proc_nm", 150, "center"));
		//		tableInfo.columns.add(new Column("svr_ip", 100, "center"));
		//		tableInfo.columns.add(new Column("prc_strt_ymdt", 100, "center"));
		//		tableInfos.put(tableInfo.name, tableInfo);
	}

	public static void addTableInfo(TableInfo tableInfo) {
		tableInfos.put(tableInfo.getName(), tableInfo);
	}

	public static Table build(String name, String refreshUrl, List<MapModel> list) {
		TableInfo tableInfo = tableInfos.get(name);
		if (list == null || list.isEmpty()) {
			if (tableInfo == null) {
				tableInfo = new TableInfo(name);
				tableInfo.links.add(new Link("list", "/rpc/list"));
			}

			return new Table(tableInfo, refreshUrl, new ArrayList<List<String>>());
		}

		if (tableInfo == null) {
			tableInfo = new TableInfo(name);
			tableInfo.links = Arrays.asList(new Link("list", "/rpc/list"));

			Set<String> columnNames = new HashSet<String>();
			for (MapModel item : list) {
				columnNames.addAll(item.getValues().keySet());
			}

			tableInfo.columns = new ArrayList<Column>(columnNames.size());
			for (String columnName : columnNames) {
				tableInfo.columns.add(new Column(columnName));
			}
		}

		return build(tableInfo, refreshUrl, list);
	}

	private static Table build(TableInfo tableInfo, String refreshUrl, List<MapModel> list) {
		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		int seed = 0;
		for (Column column : tableInfo.columns) {
			indexMap.put(column.getName(), seed++);
		}

		List<List<String>> rows = new ArrayList<List<String>>(list.size());
		for (MapModel item : list) {
			List<String> row = createEmptyRow(tableInfo.columns.size());
			rows.add(row);
			for (Entry<String, Object> entry : item.entrySet()) {
				Integer index = indexMap.get(entry.getKey());
				if (index == null) {
					continue;
				}

				row.set(index, toString(entry.getValue()));
			}
		}

		return new Table(tableInfo, refreshUrl, rows);
	}

	private static List<String> createEmptyRow(int size) {
		List<String> row = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			row.add("");
		}

		return row;
	}

	private static String toString(Object obj) {
		if (obj == null) {
			return "";
		}

		if (obj instanceof Date) {
			return sdf.format((Date)obj);
		} else {
			return obj.toString();
		}
	}
}
