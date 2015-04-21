package com.naver.mage4j.external.varien;

import com.naver.mage4j.core.mage.MageInstanceLoader;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.crypt.Varien_Crypt_Abstract;

/**
 * Crypt factory
 */
public class Varien_Crypt {
	/**
	 * Factory method to return requested cipher logic
	 * 
	 * @param method
	 * @return Varien_Crypt_Abstract
	 */
	public static Varien_Crypt_Abstract factory(String method/* = "mcrypt" */) {
		if (method == null) {
			method = "mcrypt";
		}

		String uc = Standard.ucwords(method.replace("_", " ")).replace(" ", "_");
		return MageInstanceLoader.get().getInstance("Varien_Crypt_" + uc);
	}
}