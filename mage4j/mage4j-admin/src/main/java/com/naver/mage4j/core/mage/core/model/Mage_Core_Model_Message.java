package com.naver.mage4j.core.mage.core.model;

import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Abstract;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Error;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Notice;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Success;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Warning;

/**
 * Message model
 */
public class Mage_Core_Model_Message {
	/**
	 * Message model
	 */
	public static final String ERROR = "error";

	/**
	 * Message model
	 */
	public static final String WARNING = "warning";

	/**
	 * Message model
	 */
	public static final String NOTICE = "notice";

	/**
	 * Message model
	 */
	public static final String SUCCESS = "success";

	/**
	 * Message model
	 * 
	 */
	protected <T extends Mage_Core_Model_Message_Abstract> T _factory(String code, String type, String _class/* = "" */, String method/* = "" */) {
		Mage_Core_Model_Message_Abstract message;
		switch (type.toLowerCase()) {
			case ERROR: {
				message = new Mage_Core_Model_Message_Error(code);
				break;
			}
			case WARNING: {
				message = new Mage_Core_Model_Message_Warning(code);
				break;
			}
			case SUCCESS: {
				message = new Mage_Core_Model_Message_Success(code);
				break;
			}
			default: {
				message = new Mage_Core_Model_Message_Notice(code);
				break;
			}
		}

		message.setClass(_class);
		message.setMethod(method);

		return (T)message;
	}

	/**
	 * Message model
	 * 
	 */
	public Mage_Core_Model_Message_Error error(String code, String _class/* = "" */, String method/* = "" */) {
		return this._factory(code, ERROR, _class, method);
	}

	/**
	 * Message model
	 * 
	 */
	public Mage_Core_Model_Message_Warning warning(String code, String _class/* = "" */, String method/* = "" */) {
		return this._factory(code, WARNING, _class, method);
	}

	/**
	 * Message model
	 * 
	 */
	public Mage_Core_Model_Message_Notice notice(String code, String _class/* = "" */, String method/* = "" */) {
		return this._factory(code, NOTICE, _class, method);
	}

	/**
	 * Message model
	 * 
	 */
	public Mage_Core_Model_Message_Success success(String code, String _class/* = "" */, String method/* = "" */) {
		return this._factory(code, SUCCESS, _class, method);
	}

}