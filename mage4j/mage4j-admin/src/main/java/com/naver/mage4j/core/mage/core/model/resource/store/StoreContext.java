package com.naver.mage4j.core.mage.core.model.resource.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.store.Mage_Core_Model_Store_Exception;

@Component
public class StoreContext implements InitializingBean {
	/**
	 * Cookie name
	 */
	public static final String COOKIE_NAME = "store";

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private Mage_Core_Model_Config model_Config;

	/**
	 * Default store code
	 */
	protected String _currentStore;

	protected boolean _isSingleStore;

	/**
	* Application store object
	*
	* @var Mage_Core_Model_Store
	*/
	protected Store _store;

	private Map<String, Store> _storesByCode = new HashMap<String, Store>();
	private Map<Short, Store> _storesById = new HashMap<Short, Store>();

	private static StoreContext INSTANCE;

	public static StoreContext getContext() {
		if (INSTANCE == null) {
			throw new IllegalStateException("Store context has not initialized.");
		}

		return INSTANCE;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (model_Config.isSingleStoreAllowed()) {
			_isSingleStore = getStoreCount() < 3;
		}

		INSTANCE = this;
	}

	public int getStoreCount() {
		return storeRepository.findCount();
	}

	/**
	 * Is single Store mode (only one store without default)
	 *
	 * @return bool
	 */
	public boolean isSingleStoreMode() {
		if (!AppContext.getCurrent().getConfig().isInstalled()) {
			return false;
		}

		return _isSingleStore;
	}

	/**
	 * Retrieve application store object
	 *
	 * @param null|string|bool|int|Mage_Core_Model_Store $id
	 * @return Mage_Core_Model_Store
	 * @throws Mage_Core_Model_Store_Exception
	 */
	public Store getById(Short id) {
		//        if (!Mage::isInstalled() || $this->getUpdateMode()) {
		//            return $this->_getDefaultStore();
		//        }
		//
		//        if ($id === true && isSingleStoreMode()) {
		//            return $this->_store;
		//        }
		//
		//        if (!isset($id) || ''===$id || $id === true) {
		//            $id = $this->_currentStore;
		//        }
		//        if ($id instanceof Mage_Core_Model_Store) {
		//            return $id;
		//        }
		//        if (!isset($id)) {
		//            $this->throwStoreException();
		//        }

		Store store = _storesById.get(id);
		if (store == null) {
			store = storeRepository.findOne(id);
			if (store == null) {
				throw new Mage_Core_Model_Store_Exception();
			}

			_storesById.put(store.getStoreId(), store);
			_storesByCode.put(store.getCode(), store);
		}

		return store;
	}

	public Store getByCode(String code) {
		Store store = _storesByCode.get(code);
		if (store == null) {
			store = storeRepository.findByCode(code);
			if (store == null) {
				throw new Mage_Core_Model_Store_Exception();
			}

			_storesById.put(store.getStoreId(), store);
			_storesByCode.put(store.getCode(), store);
		}

		return store;
	}

	public Store getAdminStore() {
		return storeRepository.findOne(Mage_Core_Model_App.ADMIN_STORE_ID);
	}

	public void remove(short storeId) {
		storeRepository.delete(storeId);
	}

	public List<Store> listAll() {
		return storeRepository.findAll();
	}
}
