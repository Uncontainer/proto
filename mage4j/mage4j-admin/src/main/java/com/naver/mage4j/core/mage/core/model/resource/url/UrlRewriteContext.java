package com.naver.mage4j.core.mage.core.model.resource.url;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;

@Component
public class UrlRewriteContext extends AbstractContext implements InitializingBean {
	public static final String REWRITE_REQUEST_PATH_ALIAS = "rewrite_request_path";

	@Autowired
	private UrlRewriteRepository urlRewriteRepository;

	private static UrlRewriteContext INSTANCE;

	public static UrlRewriteContext getContext() {
		if (INSTANCE == null) {
			throw new IllegalStateException("UrlRewriteContext has not initialized.");
		}

		return INSTANCE;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		INSTANCE = this;
	}

	public UrlRewrite getByRequestPath(Store store, List<String> path) {
		// TODO Store가 null이 아니여야 함.
		UrlRewrite result = _loadByRequestPath(store, path);
		if (result != null) {
			_afterLoad(result);
		}
		//        $this->setOrigData();
		//        $this->_hasDataChanges = false;

		return result;
	}

	/**
	 * Load rewrite information for request
	 * If $path is array - we must load all possible records and choose one matching earlier record in array
	 */
	private UrlRewrite _loadByRequestPath(Store store, List<String> path) {
		List<Short> storeIds = Arrays.asList(Mage_Core_Model_App.ADMIN_STORE_ID, store.getStoreId());
		List<UrlRewrite> items = urlRewriteRepository.findByRequestPathInAndStoreIdIn(path, storeIds);

		// Go through all found records and choose one with lowest penalty - earlier path in array, concrete store
		Map<String, Integer> mapPenalty = new HashMap<String, Integer>();
		for (int i = 0; i < path.size(); i++) {
			mapPenalty.put(path.get(i), i);
		}
		UrlRewrite foundItem = null;
		int currentPenalty = Integer.MAX_VALUE;

		for (UrlRewrite item : items) {
			Integer penalty = mapPenalty.get(item.getRequestPath());
			if (penalty == null) {
				continue;
			}

			penalty = penalty << 1 + (item.getStore() != null ? 0 : 1);
			if (foundItem == null || currentPenalty > penalty) {
				foundItem = item;
				currentPenalty = penalty;
				if (currentPenalty == 0) {
					break; // Found best matching item with zero penalty, no reason to continue
				}
			}
		}

		// TODO urlRewrite 사용 패턴 확인 이후 구현 추가.
		// Set data and finish loading
		//		if (foundItem != null) {
		//			            $object->setData($foundItem);
		//		}
		// Finish
		//        $this->unserializeFields($object);
		//        $this->_afterLoad($object);

		return foundItem;
	}

	public UrlRewrite getById(int id) {
		return urlRewriteRepository.findOne(id);
	}

	public UrlRewrite getByIdPath(String idPath) {
		return urlRewriteRepository.findByIdPath(idPath);
	}
}
