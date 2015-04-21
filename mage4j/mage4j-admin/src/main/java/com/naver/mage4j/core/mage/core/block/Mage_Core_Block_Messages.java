package com.naver.mage4j.core.mage.core.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.inject.internal.Lists;
import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Message;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Abstract;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Collection;

/**
 * Messages block
 */
public class Mage_Core_Block_Messages extends Mage_Core_Block_Template {
	/**
	 * Messages collection
	 */
	protected Mage_Core_Model_Message_Collection _messages;

	/**
	 * Store first level html tag name for messages html output
	 */
	protected String _messagesFirstLevelTagName = "ul";

	/**
	 * Store second level html tag name for messages html output
	 */
	protected String _messagesSecondLevelTagName = "li";

	/**
	 * Store content wrapper html tag name for messages html output
	 */
	protected String _messagesContentWrapperTagName = "span";

	/**
	 * Flag which require message text escape
	 */
	protected boolean _escapeMessageFlag = false;

	/**
	 * Storage for used types of message storages
	 */
	protected List<String> _usedStorageTypes = Lists.newArrayList("core/session");

	public Mage_Core_Block_Messages(Map<String, Object> args) {
		super(args);
	}

	/**
	 * Storage for used types of message storages
	 * 
	 */
	@Override
	public void _prepareLayout() {
		Mage_Core_Model_Message_Collection messages = AppContext.getCurrent().getSession(null).getMessages(true);
		this.addMessages(messages);
		super._prepareLayout();
	}

	/**
	 * Set message escape flag
	 * 
	 * @param flag
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages setEscapeMessageFlag(boolean flag) {
		this._escapeMessageFlag = flag;
		return this;
	}

	/**
	 * Set messages collection
	 * 
	 * @param messages
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages setMessages(Mage_Core_Model_Message_Collection messages) {
		this._messages = messages;
		return this;
	}

	/**
	 * Add messages to display
	 * 
	 * @param messages
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages addMessages(Mage_Core_Model_Message_Collection messages) {
		for (Mage_Core_Model_Message_Abstract message : messages.getItems(null).values()) {
			this.getMessageCollection().add(message);
		}

		return this;
	}

	/**
	 * Retrieve messages collection
	 * 
	 * @return Mage_Core_Model_Message_Collection
	 */
	public Mage_Core_Model_Message_Collection getMessageCollection() {
		if (this._messages == null) {
			this._messages = AppContext.getCurrent().getMessageCollection();
		}

		return this._messages;
	}

	/**
	 * Adding new message to message collection
	 * 
	 * @param message
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages addMessage(Mage_Core_Model_Message_Abstract message) {
		this.getMessageCollection().add(message);
		return this;
	}

	/**
	 * Adding new error message
	 * 
	 * @param message
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages addError(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().error(message, null, null));
		return this;
	}

	/**
	 * Adding new warning message
	 * 
	 * @param message
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages addWarning(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().warning(message, null, null));
		return this;
	}

	/**
	 * Adding new nitice message
	 * 
	 * @param message
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages addNotice(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().notice(message, null, null));
		return this;
	}

	/**
	 * Adding new success message
	 * 
	 * @param message
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages addSuccess(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().success(message, null, null));
		return this;
	}

	/**
	 * Retrieve messages array by message type
	 * 
	 * @param type
	 * @return array
	 */
	public Collection<Mage_Core_Model_Message_Abstract> getMessages(String type/* = null */) {
		return this.getMessageCollection().getItems(type).values();
	}

	/**
	 * Retrieve messages in HTML format
	 * 
	 * @param type
	 * @return string
	 */
	public String getHtml(String type/* = null */) {
		StringBuilder html = new StringBuilder();
		html.append("<" + this._messagesFirstLevelTagName + " id=\"admin_messages\">");
		for (Mage_Core_Model_Message_Abstract message : this.getMessages(type)) {
			html.append("<" + this._messagesSecondLevelTagName + " class=\"" + message.getType() + "-msg\">");
			html.append(this._escapeMessageFlag ? this.escapeHtml(message.getText(), null) : message.getText());
			html.append("</" + this._messagesSecondLevelTagName + ">");
		}

		html.append("</" + this._messagesFirstLevelTagName + ">");
		return html.toString();
	}

	/**
	 * Retrieve messages in HTML format grouped by type
	 * 
	 * @param type
	 * @return string
	 */
	public String getGroupedHtml() {
		List<String> types = Arrays.asList(Mage_Core_Model_Message.ERROR, Mage_Core_Model_Message.WARNING, Mage_Core_Model_Message.NOTICE, Mage_Core_Model_Message.SUCCESS);
		StringBuilder html = new StringBuilder();
		for (String type : types) {
			Collection<Mage_Core_Model_Message_Abstract> messages = this.getMessages(type);
			if (!messages.isEmpty()) {
				if (html.length() == 0) {
					html.append("<" + this._messagesFirstLevelTagName + " class=\"messages\">");
				}

				html.append("<" + this._messagesSecondLevelTagName + " class=\"" + type + "-msg\">");
				html.append("<" + this._messagesFirstLevelTagName + ">");
				for (Mage_Core_Model_Message_Abstract message : messages) {
					html.append("<" + this._messagesSecondLevelTagName + ">");
					html.append("<" + this._messagesContentWrapperTagName + ">");
					html.append((this._escapeMessageFlag) ? (this.escapeHtml(message.getText(), null)) : (message.getText()));
					html.append("</" + this._messagesContentWrapperTagName + ">");
					html.append("</" + this._messagesSecondLevelTagName + ">");
				}

				html.append("</" + this._messagesFirstLevelTagName + ">");
				html.append("</" + this._messagesSecondLevelTagName + ">");
			}
		}

		if (html.length() > 0) {
			html.append("</" + this._messagesFirstLevelTagName + ">");
		}

		return html.toString();
	}

	/**
	 * Retrieve messages in HTML format grouped by type
	 * 
	 * @param type
	 * @return string
	 */
	@Override
	protected String _toHtml() {
		return this.getGroupedHtml();
	}

	/**
	 * Set messages first level html tag name for output messages as html
	 * 
	 * @param tagName
	 */
	public void setMessagesFirstLevelTagName(String tagName) {
		this._messagesFirstLevelTagName = tagName;
	}

	/**
	 * Set messages first level html tag name for output messages as html
	 * 
	 * @param tagName
	 */
	public void setMessagesSecondLevelTagName(String tagName) {
		this._messagesSecondLevelTagName = tagName;
	}

	/**
	 * Get cache key informative items
	 * 
	 * @return array
	 */
	@Override
	public List<String> getCacheKeyInfo() {
		throw new UnsupportedOperationException();
		//		return Collections.singletonMap("storage_types", serialize(this._usedStorageTypes));
	}

	/**
	 * Add used storage type
	 * 
	 * @param type
	 */
	public void addStorageType(String type) {
		this._usedStorageTypes.add(type);
	}

}