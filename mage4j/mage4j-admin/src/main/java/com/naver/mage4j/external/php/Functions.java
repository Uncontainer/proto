package com.naver.mage4j.external.php;

public class Functions {
	/**
	 * Tiny function to enhance functionality of ucwords
	 * 
	 * Will capitalize first letters and convert separators if needed
	 * 
	 * @param string
	 *            $str
	 * @param string
	 *            $destSep
	 * @param string
	 *            $srcSep
	 * @return string
	 */
	public static String uc_words(String str, String destSep, String srcSep) {
		return Standard.ucwords(str.replace(srcSep, " ")).replace(" ", destSep);
	}

	public static String uc_words(String str) {
		return uc_words(str, "_");
	}

	public static String uc_words(String str, String destSep) {
		return uc_words(str, destSep, "_");
	}
}
