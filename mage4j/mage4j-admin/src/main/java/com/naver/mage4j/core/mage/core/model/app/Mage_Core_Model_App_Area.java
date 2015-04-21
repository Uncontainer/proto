package com.naver.mage4j.core.mage.core.model.app;

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Translate;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;
import com.naver.mage4j.core.mage.core.model.resource.design.DesignChange;
import com.naver.mage4j.core.mage.core.model.resource.design.DesignChangeRepository;
import com.naver.mage4j.external.varien.Varien_Profiler;

@Component(Mage_Core_Model_App_Area.NAME)
@Scope("prototype")
public class Mage_Core_Model_App_Area {
	public static final String NAME = "area";

	//	private static final String AREA_GLOBAL = "global";
	//	private static final String AREA_FRONTEND = "frontend";
	//	private static final String AREA_ADMIN = "admin";
	//	private static final String AREA_ADMINHTML = "adminhtml";
	//
	//	private static final String PART_CONFIG = "config";
	//	private static final String PART_EVENTS = "events";
	//	private static final String PART_TRANSLATE = "translate";
	//	private static final String PART_DESIGN = "design";

	public enum AreaType {
		GLOBAL, FRONTEND, ADMIN, ADMINHTML;

		final String code;

		private AreaType() {
			code = this.name().toLowerCase();
		}

		public String getCode() {
			return code;
		}

		public static AreaType fromCode(String code) {
			return AreaType.valueOf(code.toLowerCase());
		}
	}

	public enum PartType {
		CONFIG, EVENTS, TRANSLATE, DESIGN;

		final String code;

		private PartType() {
			code = this.name().toLowerCase();
		}

		public String getCode() {
			return code;
		}

		public static PartType fromCode(String code) {
			return PartType.valueOf(code.toLowerCase());
		}
	}

	@Autowired
	private EventDispatcher eventDispatcher;

	@Autowired
	private Mage_Core_Model_Translate _translator;

	@Autowired
	private DesignChangeRepository designChangeRepository;

	/**
	 * Area code
	 *
	 * @var string
	 */
	protected final AreaType _code;

	/**
	 * Area application
	 *
	 * @var Mage_Core_Model_App
	 */
	//    protected $_application;

	public Mage_Core_Model_App_Area(AreaType areaType) {
		_code = areaType;
		//        $this->_application = $application;
	}

	/**
	 * Load area data
	 *
	 * @param   string|null $part
	 * @return  Mage_Core_Model_App_Area
	 */
	public Mage_Core_Model_App_Area load(PartType partType) {
		if (partType != null) {
			for (PartType eachPart : PartType.values()) {
				_loadPart(eachPart);
			}
		}
		else {
			_loadPart(partType);
		}

		return this;
	}

	/**
	* Array of area loaded parts
	*/
	protected Map<PartType, Boolean> _loadedParts = new EnumMap<PartType, Boolean>(PartType.class);

	/**
	* Loading part of area
	*
	* @param   string $part
	* @return  Mage_Core_Model_App_Area
	*/
	protected Mage_Core_Model_App_Area _loadPart(PartType part) {
		if (_loadedParts.get(part) == Boolean.TRUE) {
			return this;
		}

		Varien_Profiler.start("mage::dispatch::controller::action::predispatch::load_area::" + _code + "::" + part);

		switch (part) {
			case CONFIG:
				_initConfig();
				break;
			case EVENTS:
				_initEvents();
				break;
			case TRANSLATE:
				_initTranslate();
				break;
			case DESIGN:
				_initDesign();
				break;
		}

		_loadedParts.put(part, true);
		Varien_Profiler.stop("mage::dispatch::controller::action::predispatch::load_area::" + _code + "::" + part);

		return this;
	}

	protected void _initConfig() {
		// do nothing
	}

	protected void _initEvents() {
		eventDispatcher.addEventArea(_code);
		eventDispatcher.loadEventObservers(_code);
	}

	protected void _initTranslate() {
		_translator.init(_code, false);
	}

	protected void _initDesign() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (app.getRequest().isStraight(null)) {
			return;
		}

		Mage_Core_Model_Design_Package designPackage = new Mage_Core_Model_Design_Package();

		if (designPackage.getArea() != AreaType.FRONTEND) {
			return;
		}

		Short currentStore = app.getStore().getStoreId();

		DesignChange designChange = designChangeRepository.findByStoreId(currentStore, new Date());
		if (designChange != null) {
			designPackage.setPackageName(designChange.getPackage());
			designPackage.setTheme(designChange.getTheme());
		}
	}
}
