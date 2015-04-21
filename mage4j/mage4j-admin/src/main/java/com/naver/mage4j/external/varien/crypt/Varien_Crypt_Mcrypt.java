package com.naver.mage4j.external.varien.crypt;

import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Map;

import com.naver.mage4j.external.varien.Varien_Exception;

/**
 * Mcrypt plugin
 */
public class Varien_Crypt_Mcrypt extends Varien_Crypt_Abstract {
	public static final int MCRYPT_ENCRYPT = 0;
	public static final int MCRYPT_DECRYPT = 1;
	public static final int MCRYPT_DEV_RANDOM = 0;
	public static final int MCRYPT_DEV_URANDOM = 1;
	public static final int MCRYPT_RAND = 2;
	public static final String MCRYPT_3DES = "tripledes";
	public static final String MCRYPT_ARCFOUR_IV = "arcfour-iv";
	public static final String MCRYPT_ARCFOUR = "arcfour";
	public static final String MCRYPT_BLOWFISH = "blowfish";
	public static final String MCRYPT_BLOWFISH_COMPAT = "blowfish-compat";
	public static final String MCRYPT_CAST_128 = "cast-128";
	public static final String MCRYPT_CAST_256 = "cast-256";
	public static final String MCRYPT_CRYPT = "crypt";
	public static final String MCRYPT_DES = "des";
	public static final String MCRYPT_ENIGNA = "crypt";
	public static final String MCRYPT_GOST = "gost";
	public static final String MCRYPT_LOKI97 = "loki97";
	public static final String MCRYPT_PANAMA = "panama";
	public static final String MCRYPT_RC2 = "rc2";
	public static final String MCRYPT_RIJNDAEL_128 = "rijndael-128";
	public static final String MCRYPT_RIJNDAEL_192 = "rijndael-192";
	public static final String MCRYPT_RIJNDAEL_256 = "rijndael-256";
	public static final String MCRYPT_SAFER64 = "safer-sk64";
	public static final String MCRYPT_SAFER128 = "safer-sk128";
	public static final String MCRYPT_SAFERPLUS = "saferplus";
	public static final String MCRYPT_SERPENT = "serpent";
	public static final String MCRYPT_THREEWAY = "threeway";
	public static final String MCRYPT_TRIPLEDES = "tripledes";
	public static final String MCRYPT_TWOFISH = "twofish";
	public static final String MCRYPT_WAKE = "wake";
	public static final String MCRYPT_XTEA = "xtea";
	public static final String MCRYPT_IDEA = "idea";
	public static final String MCRYPT_MARS = "mars";
	public static final String MCRYPT_RC6 = "rc6";
	public static final String MCRYPT_SKIPJACK = "skipjack";
	public static final String MCRYPT_MODE_CBC = "cbc";
	public static final String MCRYPT_MODE_CFB = "cfb";
	public static final String MCRYPT_MODE_ECB = "ecb";
	public static final String MCRYPT_MODE_NOFB = "nofb";
	public static final String MCRYPT_MODE_OFB = "ofb";
	public static final String MCRYPT_MODE_STREAM = "stream";

	/**
	 * Constuctor
	 * 
	 * @param data
	 */
	public Varien_Crypt_Mcrypt(Map<String, Object> data/* = Collections.emptyMap() */) {
		super(data);
	}

	private String getCipher() {
		return (String)getData("cipher");
	}

	private void setCipher(String cipher) {
		setData("cipher", cipher);
	}

	private void setMode(String mode) {
		setData("mode", mode);
	}

	private String getMode() {
		return (String)getData("mode");
	}

	private String getInitVector() {
		return (String)getData("init_vector");
	}

	private void setInitVector(String initVector) {
		setData("init_vector", initVector);
	}

	/**
	 * Initialize mcrypt module
	 * 
	 * @param key key 
	 * @return Varien_Crypt_Mcrypt
	 */
	public Varien_Crypt_Mcrypt init(String key) {
		throw new UnsupportedOperationException();
//		if (this.getCipher() == null) {
//			this.setCipher(MCRYPT_BLOWFISH);
//		}
//
//		if (this.getMode() == null) {
//			this.setMode(MCRYPT_MODE_ECB);
//		}
//
//		this.setHandler(mcrypt_module_open(this.getCipher(), "", this.getMode(), ""));
//		if (this.getInitVector() == null) {
//			int handlerSize = mcrypt_enc_get_iv_size(this.getHandler());
//			if (MCRYPT_MODE_CBC.equals(this.getMode())) {
//				this.setInitVector(Standard.md5(mcrypt_create_iv(handlerSize, MCRYPT_RAND)).substring(-handlerSize));
//			} else {
//				this.setInitVector(mcrypt_create_iv(handlerSize, MCRYPT_RAND));
//			}
//
//		}
//
//		int maxKeySize = mcrypt_enc_get_key_size(this.getHandler());
//		if (key.length() > maxKeySize) {
//			this.setHandler(null);
//			throw new Varien_Exception("Maximum key size must be smaller " + maxKeySize);
//		}
//
//		mcrypt_generic_init(this.getHandler(), key, this.getInitVector());
//		return this;
	}

	/**
	 * Encrypt data
	 * 
	 * @param data string 
	 * @return string
	 */
	public String encrypt(String data) {
		if (this.getHandler() == null) {
			throw new Varien_Exception("Crypt module is not initialized.");
		}

		if (data.length() == 0) {
			return data;
		}
		
		throw new UnsupportedAddressTypeException();
//		return mcrypt_generic(this.getHandler(), data);
	}

	/**
	 * Decrypt data
	 * 
	 * @param data string 
	 * @return string
	 */
		public String decrypt(String data) {
			if (this.getHandler() == null) {
				throw new Varien_Exception("Crypt module is not initialized.");
			}
	
			if (data.length() == 0) {
				return data;
			}
	
			throw new UnsupportedAddressTypeException();
//			return mdecrypt_generic(this.getHandler(), data);
		}

	/**
	 * Desctruct cipher module
	 * 
	 */
	//	public void __destruct() {
	//		if (this.getHandler() != null) {
	//			this._reset();
	//		}
	//	}

	/**
	 * Desctruct cipher module
	 * 
	 */
	//	protected void _reset() {
	//		mcrypt_generic_deinit(this.getHandler());
	//		mcrypt_module_close(this.getHandler());
	//	}

	static class Handler {

	}

	private Handler getHandler() {
		throw new UnsupportedOperationException();
	}

	private void setHandler(Handler handler) {
		setData("handler", handler);
	}
}