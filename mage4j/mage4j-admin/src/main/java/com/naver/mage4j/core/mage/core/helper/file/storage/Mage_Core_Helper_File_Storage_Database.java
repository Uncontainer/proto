package com.naver.mage4j.core.mage.core.helper.file.storage;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.helper.file.Mage_Core_Model_File_Storage;

public class Mage_Core_Helper_File_Storage_Database {
	private static Boolean _useDb;

	/**
	 * Check if we use DB storage
	 * Note: Disabled as not completed feature
	 */
	public static boolean checkDbUsage() {
		if (_useDb == null) {
			String value = AppContext.getCurrent().getConfig().getNode(Mage_Core_Model_File_Storage.XML_PATH_STORAGE_MEDIA).getText();
			int currentStorage = Integer.parseInt(value);
			_useDb = currentStorage == Mage_Core_Model_File_Storage.STORAGE_MEDIA_DATABASE;
		}

		return _useDb;
	}
}
