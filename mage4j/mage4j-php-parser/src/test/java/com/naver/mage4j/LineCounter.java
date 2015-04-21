package com.naver.mage4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LineCounter {
	public int count(File file, String... extendions) throws IOException {
		int count = 0;
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				count += count(child, extendions);
			}
		}

		boolean match = false;
		for (String extension : extendions) {
			if (file.getName().endsWith("." + extension)) {
				match = true;
				break;
			}
		}

		if (match) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) {
				count++;
			}
			reader.close();
		}

		return count;
	}

	public static void main(String[] args) throws IOException {
		String path;
		path = "D:/project/java/main/maje4j/mage4j/mage4j-admin/src/main/java/com/naver/mage4j";
		//		path = "D:/project/java/main/maje4j/mage4j/mage4j-php-parser/src/main/java/com/naver/mage4j/php/code";
		//		path = "E:/repository/git/magento/lib";
		path = "D:/project/java/main/mom/mom-core/src/main";
		System.out.println(new LineCounter().count(new File(path), "java"));
	}
}
