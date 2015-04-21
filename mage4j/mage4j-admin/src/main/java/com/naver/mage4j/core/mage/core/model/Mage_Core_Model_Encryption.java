package com.naver.mage4j.core.mage.core.model;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.helper.Mage_Core_Helper_Data;
import com.naver.mage4j.core.util.EncodeUtils;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Crypt;
import com.naver.mage4j.external.varien.crypt.Varien_Crypt_Mcrypt;

/**
 * Provides basic logic for hashing passwords and encrypting/decrypting misc data
 */
public class Mage_Core_Model_Encryption {
	/**
	 */
	protected Varien_Crypt_Mcrypt _crypt;

	/**
	 */
	protected Mage_Core_Helper_Data _helper;

	/**
	 * Set helper instance
	 * 
	 * @param helper
	 * @return Mage_Core_Model_Encryption
	 */
	public Mage_Core_Model_Encryption setHelper(Mage_Core_Helper_Data helper) {
		this._helper = helper;
		return this;
	}

	/**
	 * Generate a [salted] hash.$salt can be:false - a random will be generatedinteger - a random with specified length will be generatedstring
	 * 
	 * @param password
	 * @param salt
	 * @return string
	 */
	public String getHash(String password, int salt) {
		return getHash(password, this._helper.getRandomString(salt, null));
	}

	public String getHash(String password, String salt) {
		return (salt == null) ? (this.hash(password)) : (this.hash(salt + password) + ":" + salt);
	}

	/**
	 * Hash a string
	 * 
	 * @param data
	 * @return string
	 */
	public String hash(String data) {
		return Standard.md5(data);
	}

	/**
	 * Validate hash against hashing method (with or without salt)
	 * 
	 * @param password
	 * @param hash
	 * @return bool
	 */
	public boolean validateHash(String password, String hash) {
		String[] hashArr = hash.split(":");
		switch (hashArr.length) {
			case 1: {
				return this.hash(password).equals(hash);
			}
			case 2: {
				return this.hash(hashArr[1] + password).equals(hashArr[0]);
			}
		}

		throw new Mage_Core_Exception("Invalid hash.");
	}

	/**
	 * Instantiate crypt model
	 * 
	 * @param key
	 * @return Varien_Crypt_Mcrypt
	 */
	protected Varien_Crypt_Mcrypt _getCrypt(String key/* = null */) {
		if (this._crypt == null) {
			if (null == key) {
				key = AppContext.getCurrent().getConfig().getNode("global/crypt/key").getText();
			}

			this._crypt = ((Varien_Crypt_Mcrypt)Varien_Crypt.factory("mcrypt")).init(key);
		}

		return this._crypt;
	}

	/**
	 * Encrypt a string
	 * 
	 * @param data
	 * @return string
	 */
	public String encrypt(String data) {
		return EncodeUtils.base64_encode(this._getCrypt(null).encrypt(data));
	}

	/**
	 * Decrypt a string
	 * 
	 * @param data
	 * @return string
	 */
	public String decrypt(String data) {
		return StringUtils.trim(this._getCrypt(null).decrypt(EncodeUtils.base64_decode(data))).replace("\\x0", "");
	}

	/**
	 * Return crypt model, instantiate if it is empty
	 * 
	 * @param key
	 * @return Varien_Crypt_Mcrypt
	 */
	public Varien_Crypt_Mcrypt validateKey(String key) {
		return this._getCrypt(key);
	}

}