package com.naver.mage4j.core.mage.core.model.resource.website;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

@Component
public class WebsiteContext {
	@Autowired
	private WebsiteRepository websiteRepository;

	public Store getStoreByWebsite(String websiteCode) {
		throw new UnsupportedOperationException();
	}

	public Website getWebsiteById(short numScopeCode) {
		return websiteRepository.findOne(numScopeCode);
	}

	public Website getByCode(String code) {
		return websiteRepository.findByCode(code);
	}

	public Website getDefaultWebsite() {
		return websiteRepository.findDefault();
	}

	public List<Website> listAll() {
		return websiteRepository.findAll();
	}

	public void remove(short websiteId) {
		websiteRepository.delete(websiteId);
	}
}
