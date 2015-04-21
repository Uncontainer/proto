package com.naver.mage4j.core.mage.core.model.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Message;

/**
 * Messages collection
 */
public class Mage_Core_Model_Message_Collection {
	/**
	 * All messages by type array
	 */
	protected Map<String, Map<String, Mage_Core_Model_Message_Abstract>> _messages = new HashMap<String, Map<String, Mage_Core_Model_Message_Abstract>>();

	/**
	 * All messages by type array
	 */
	protected Mage_Core_Model_Message_Abstract _lastAddedMessage;

	/**
	 * Adding new message to collection
	 * 
	 * @param message
	 * @return Mage_Core_Model_Message_Collection
	 */
	public Mage_Core_Model_Message_Collection add(Mage_Core_Model_Message_Abstract message) {
		return this.addMessage(message);
	}

	/**
	 * Adding new message to collection
	 * 
	 * @param message
	 * @return Mage_Core_Model_Message_Collection
	 */
	public Mage_Core_Model_Message_Collection addMessage(Mage_Core_Model_Message_Abstract message) {
		Map<String, Mage_Core_Model_Message_Abstract> messages = this._messages.get(message.getType());
		if (messages == null) {
			messages = new HashMap<String, Mage_Core_Model_Message_Abstract>();
			this._messages.put(message.getType(), messages);
		}

		messages.put(message.getIdentifier(), message);
		this._lastAddedMessage = message;
		return this;
	}

	/**
	 * Clear all messages except sticky
	 * 
	 * @return Mage_Core_Model_Message_Collection
	 */
	public Mage_Core_Model_Message_Collection clear() {
		Map<String, Map<String, Mage_Core_Model_Message_Abstract>> aliveMessages = new HashMap<String, Map<String, Mage_Core_Model_Message_Abstract>>();
		for (Map.Entry<String, Map<String, Mage_Core_Model_Message_Abstract>> each : this._messages.entrySet()) {
			Map<String, Mage_Core_Model_Message_Abstract> aliveMessage = null;
			for (Map.Entry<String, Mage_Core_Model_Message_Abstract> eachMessage : each.getValue().entrySet()) {
				Mage_Core_Model_Message_Abstract message = eachMessage.getValue();
				if (message.getIsSticky()) {
					if (aliveMessage == null) {
						aliveMessage = new HashMap<String, Mage_Core_Model_Message_Abstract>();
						aliveMessage.put(message.getIdentifier(), message);
					}
				}
			}

			if (aliveMessage != null) {
				aliveMessages.put(each.getKey(), aliveMessage);
			}
		}

		this._messages = aliveMessages;

		return this;
	}

	/**
	 * Get last added message if any
	 * 
	 * @return Mage_Core_Model_Message_Abstract|null
	 */
	public Mage_Core_Model_Message_Abstract getLastAddedMessage() {
		return this._lastAddedMessage;
	}

	/**
	 * Get first even message by identifier
	 * 
	 * @param identifier
	 * @return Mage_Core_Model_Message_Abstract|null
	 */
	public Mage_Core_Model_Message_Abstract getMessageByIdentifier(String identifier) {
		for (Map.Entry<String, Map<String, Mage_Core_Model_Message_Abstract>> each : this._messages.entrySet()) {
			Mage_Core_Model_Message_Abstract result = each.getValue().get(identifier);
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	/**
	 * Get first even message by identifier
	 * 
	 * @param identifier
	 * @return Mage_Core_Model_Message_Abstract|null
	 */
	public void deleteMessageByIdentifier(String identifier) {
		Iterator<Entry<String, Map<String, Mage_Core_Model_Message_Abstract>>> iter = this._messages.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Map<String, Mage_Core_Model_Message_Abstract>> each = iter.next();
			each.getValue().remove(identifier);

			if (each.getValue().isEmpty()) {
				iter.remove();
			}
		}
	}

	/**
	 * Retrieve messages collection items
	 * 
	 * @param type
	 * @return array
	 */
	public Map<String, Mage_Core_Model_Message_Abstract> getItems(String type/* = null */) {
		if (type != null) {
			return this._messages.get(type) != null ? this._messages.get(type) : Collections.emptyMap();
		}

		Map<String, Mage_Core_Model_Message_Abstract> result = new HashMap<String, Mage_Core_Model_Message_Abstract>();
		for (Map.Entry<String, Map<String, Mage_Core_Model_Message_Abstract>> each : this._messages.entrySet()) {
			result.putAll(each.getValue());
		}

		return result;
	}

	/**
	 * Retrieve all messages by type
	 * 
	 * @param type
	 * @return array
	 */
	public Map<String, Mage_Core_Model_Message_Abstract> getItemsByType(String type) {
		return this._messages.get(type) != null ? this._messages.get(type) : Collections.emptyMap();
	}

	/**
	 * Retrieve all error messages
	 * 
	 * @return array
	 */
	public Map<String, Mage_Core_Model_Message_Abstract> getErrors() {
		return this.getItemsByType(Mage_Core_Model_Message.ERROR);
	}

	/**
	 * Retrieve all error messages
	 * 
	 * @return array
	 */
	@Override
	public String toString() {
		return this.getItems(null).toString();
	}

	/**
	 * Retrieve messages count
	 * 
	 * @return int
	 */
	public int count(String type/* = null */) {
		if (type != null) {
			Map<String, Mage_Core_Model_Message_Abstract> map = this._messages.get(type);
			if (map != null) {
				return map.size();
			}

			return 0;
		}

		return this._messages.size();
	}
}