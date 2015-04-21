package com.naver.mage4j.core.mage.core.model.message;


/**
 * Abstract message model
 */
public abstract class Mage_Core_Model_Message_Abstract {
	/**
	 * Abstract message model
	 */
	protected String _type;

	/**
	 * Abstract message model
	 */
	protected String _code;

	/**
	 * Abstract message model
	 */
	protected String _class;

	/**
	 * Abstract message model
	 */
	protected String _method;

	/**
	 * Abstract message model
	 */
	protected String _identifier;

	/**
	 * Abstract message model
	 */
	protected boolean _isSticky = false;

	/**
	 * Abstract message model
	 * 
	 */
	public Mage_Core_Model_Message_Abstract(String type, String code/* = "" */) {
		this._type = type;
		this._code = code;
	}

	/**
	 * Abstract message model
	 * 
	 */
	public String getCode() {
		return this._code;
	}

	/**
	 * Abstract message model
	 * 
	 */
	public String getText() {
		return this.getCode();
	}

	/**
	 * Abstract message model
	 * 
	 */
	public String getType() {
		return this._type;
	}

	/**
	 * Abstract message model
	 * 
	 */
	public void setClass(String _class) {
		this._class = _class;
	}

	/**
	 * Abstract message model
	 * 
	 */
	public void setMethod(String method) {
		this._method = method;
	}

	/**
	 * Abstract message model
	 * 
	 */
	@Override
	public String toString() {
		return (this.getType() + ": " + this.getText());
	}

	/**
	 * Set message identifier
	 * 
	 * @param id
	 * @return Mage_Core_Model_Message_Abstract
	 */
	public Mage_Core_Model_Message_Abstract setIdentifier(String id) {
		this._identifier = id;
		return this;
	}

	/**
	 * Get message identifier
	 * 
	 * @return string
	 */
	public String getIdentifier() {
		return this._identifier;
	}

	/**
	 * Set message sticky status
	 * 
	 * @param isSticky
	 * @return Mage_Core_Model_Message_Abstract
	 */
	public Mage_Core_Model_Message_Abstract setIsSticky(boolean isSticky/* = true */) {
		this._isSticky = isSticky;
		return this;
	}

	/**
	 * Get whether message is sticky
	 * 
	 * @return bool
	 */
	public boolean getIsSticky() {
		return this._isSticky;
	}

	/**
	 * Set code
	 * 
	 * @param code
	 * @return Mage_Core_Model_Message_Abstract
	 */
	public Mage_Core_Model_Message_Abstract setCode(String code) {
		this._code = code;
		return this;
	}

}