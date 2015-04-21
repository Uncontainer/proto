package com.pulsarang.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapModelFileUtils {
	private static Logger log = LoggerFactory.getLogger(MapModelFileUtils.class);

	public static <T extends MapModel> List<T> loadList(File file, Class<T> clazz) {
		if (!file.exists()) {
			return null;
		}

		List<T> result = new ArrayList<T>();

		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}

				T item = MapModel.fromJson(line, clazz);
				result.add(item);
			}

			try {
				fis.close();
				isr.close();
			} catch (IOException ignore) {
				// do nothing
			}

			return result;
		} catch (Exception e) {
			log.error("[PIC] Fail to load stored information.", e);
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignore) {
					// do nothing
				}
			}
		}
	}

	public static <T extends MapModel> void saveList(File file, List<T> list) {
		if (list == null) {
			file.delete();
			return;
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file, "UTF-8");
			for (MapModel model : list) {
				writer.println(model.toJson());
			}
		} catch (Exception e) {
			log.warn("[PIC] Fail to save information.", e);
			file.delete();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public static <T extends MapModel> T loadItem(File file, Class<T> clazz) {
		if (!file.exists()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			reader = new BufferedReader(isr);
			String json = reader.readLine();

			try {
				fis.close();
				isr.close();
			} catch (IOException ignore) {
				// do nothing
			}

			return MapModel.fromJson(json, clazz);
		} catch (Exception e) {
			log.error("[PIC] Fail to load stored information.", e);
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.info("[PIC] Fail to close file.", e);
				}
			}
		}
	}

	public static void saveItem(File file, MapModel model) {
		if (model == null) {
			file.delete();
			return;
		}

		MapModelWriter<MapModel> writer = null;
		try {
			writer = new MapModelWriter<MapModel>(new PrintWriter(file, "UTF-8"));
			writer.write(model);
		} catch (Exception e) {
			log.warn("[PIC] Fail to save information.", e);
			file.delete();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.info("[PIC] Fail to close file.", e);
				}
			}
		}
	}
}
