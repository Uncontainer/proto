package com.naver.mage4j.core.mage.core.helper.file;

public class Mage_Core_Model_File_Storage {
	/**
	 * Storage systems ids
	 */
	public static final int STORAGE_MEDIA_FILE_SYSTEM = 0;
	public static final int STORAGE_MEDIA_DATABASE = 1;

	/**
	 * Config pathes for storing storage configuration
	 */
	public static final String XML_PATH_STORAGE_MEDIA = "default/system/media_storage_configuration/media_storage";
	public static final String XML_PATH_STORAGE_MEDIA_DATABASE = "default/system/media_storage_configuration/media_database";
	public static final String XML_PATH_MEDIA_RESOURCE_WHITELIST = "default/system/media_storage_configuration/allowed_resources";
	public static final String XML_PATH_MEDIA_RESOURCE_IGNORED = "default/system/media_storage_configuration/ignored_resources";
	public static final String XML_PATH_MEDIA_UPDATE_TIME = "system/media_storage_configuration/configuration_update_time";
}
