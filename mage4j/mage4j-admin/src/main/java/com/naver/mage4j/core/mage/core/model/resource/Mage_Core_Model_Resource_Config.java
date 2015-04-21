package com.naver.mage4j.core.mage.core.model.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.ScopeType;
import com.naver.mage4j.core.mage.core.model.resource.config.CoreConfigData;
import com.naver.mage4j.core.mage.core.model.resource.config.CoreConfigDataRepository;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.core.mage.core.model.resource.website.Website;
import com.naver.mage4j.core.mage.core.model.resource.website.WebsiteContext;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

@Component
public class Mage_Core_Model_Resource_Config {
	private final Logger log = LoggerFactory.getLogger(Mage_Core_Model_Resource_Config.class);

	@Autowired
	private CoreConfigDataRepository coreConfigDataRepository;

	@Autowired
	private WebsiteContext websiteContext;

	@Autowired
	private StoreContext storeContext;

	@Transactional
	public void loadToXml(Mage_Core_Model_Config xmlConfig) {
		Map<Short, Website> websites = new HashMap<Short, Website>();
		for (Website w : websiteContext.listAll()) {
			xmlConfig.setNode("websites/" + w.getCode() + "/system/website/id", Short.toString(w.getWebsiteId()));
			xmlConfig.setNode("websites/" + w.getCode() + "/system/website/name", w.getName());
			websites.put(w.getWebsiteId(), w);
			//            websites.put(w.getWebsiteId(), array("code" => w.get("code"));
		}

		Map<Short, Store> stores = new HashMap<Short, Store>();
		List<Store> ss = storeContext.listAll();
		Collections.sort(ss, new Comparator<Store>() {
			@Override
			public int compare(Store o1, Store o2) {
				return o1.getSortOrder() - o2.getSortOrder();
			}
		});

		for (Store s : ss) {
			Website coreWebsite = websites.get(s.getWebsite().getWebsiteId());
			if (coreWebsite == null) {
				continue;
			}

			xmlConfig.setNode("stores/" + s.getCode() + "/system/store/id", Short.toString(s.getStoreId()));
			xmlConfig.setNode("stores/" + s.getCode() + "/system/store/name", s.getName());
			xmlConfig.setNode("stores/" + s.getCode() + "/system/website/id", Short.toString(s.getWebsite().getWebsiteId()));
			xmlConfig.setNode("websites/" + coreWebsite.getCode() + "/system/stores/" + s.getCode(), Short.toString(s.getStoreId()));
			stores.put(s.getStoreId(), s);
			//			coreWebsite.getStores().add(s);
			//            stores[s["store_id"]] = array("code"=>s.getCode());
			//            websites[s["website_id"]]["stores"][s["store_id"]] = s.getCode();
		}

		//		substFrom = array();
		//		substTo = array();

		// TODO condition이 파마미터로 넘어올 경우 where 조건에 추가.
		// load all configuration records from database, which are not inherited
		List<CoreConfigData> configs = coreConfigDataRepository.findAll();
		for (CoreConfigData r : configs) {
			if (r.getScopeType() != ScopeType.DEFAULT) {
				continue;
			}

			String value = r.getValue();
			//			value = str_replace(substFrom, substTo, value);
			xmlConfig.setNode("default/" + r.getPath(), value);
		}

		// inherit default config values to all websites
		Simplexml_Element extendSource = xmlConfig.getNode("default");
		for (Website w : websites.values()) {
			Simplexml_Element websiteNode = xmlConfig.selectDescendant("websites/" + w.getCode());
			websiteNode.extend(extendSource);
		}

		List<Short> deleteWebsites = new ArrayList<Short>();
		// set websites config values from database
		for (CoreConfigData r : configs) {
			if (r.getScopeType() != ScopeType.WEBSITE) {
				continue;
			}

			String value = r.getValue();
			//			value = str_replace(substFrom, substTo, value);
			Website coreWebsite = websites.get((short)r.getScopeId());
			if (coreWebsite != null) {
				String nodePath = String.format("websites/%s/%s", coreWebsite.getCode(), r.getValue());
				xmlConfig.setNode(nodePath, value);
			} else {
				deleteWebsites.add((short)r.getScopeId());
			}
		}

		// extend website config values to all associated stores
		for (Website website : websites.values()) {
			extendSource = xmlConfig.getNode("websites/" + website.getCode());
			for (Store store : website.getStores()) {
				Simplexml_Element storeNode = xmlConfig.getNode("stores/" + store.getCode());
				/**
				 * extendSource DO NOT need overwrite source
				 */
				storeNode.extend(extendSource, false);
			}
		}

		List<Short> deleteStores = new ArrayList<Short>();
		// set stores config values from database
		for (CoreConfigData r : configs) {
			if (r.getScopeType() != ScopeType.STORE) {
				continue;
			}

			String value = r.getValue();
			//			value = str_replace(substFrom, substTo, value);
			Store coreStore = stores.get((short)r.getScopeId());
			if (coreStore != null) {
				String nodePath = String.format("stores/%s/%s", coreStore.getCode(), r.getPath());
				xmlConfig.setNode(nodePath, value);
			} else {
				deleteStores.add((short)r.getScopeId());
			}
		}

		// TODO 삭제되어야 하는 경우를 정확하게 파악 후 실행 여부 검토.
		for (short websiteId : deleteWebsites) {
			//			websiteContext.remove(websiteId);
		}

		for (short storeId : deleteStores) {
			//			storeContext.remove(storeId);
		}
	}
}
