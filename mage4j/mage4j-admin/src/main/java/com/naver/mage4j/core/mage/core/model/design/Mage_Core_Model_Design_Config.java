package com.naver.mage4j.core.mage.core.model.design;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom;
import com.naver.mage4j.external.varien.simplexml.Varien_Simplexml_Config;

@Component
public class Mage_Core_Model_Design_Config extends Varien_Simplexml_Config implements InitializingBean {
	protected String _designRoot;

	@Autowired
	private Mage_Core_Model_Config modelConfig;

	public Mage_Core_Model_Design_Config() {
		super(Simplexml_Element_JDom.class);
	}

	public Mage_Core_Model_Design_Config(Mage_Core_Model_Design_Config other) {
		super(other);
		this._designRoot = other._designRoot;
		this.modelConfig = other.modelConfig;
	}

	/**
	 * Assemble themes inheritance config
	 *
	 */
	@Override
	public void afterPropertiesSet(/*Map<String, Object> params*/) throws Exception {
		// TODO designRootPath를 파라미터로 받아야 하는 경우가 있는지 확인.
//		String designRootPath = null/*(String)params.get("designRoot")*/;
//		if (designRootPath != null) {
//			File designRoot = new File(designRootPath);
//			if (!designRoot.isDirectory()) {
//				throw new Mage_Core_Exception("Design root '" + designRootPath + "' isn't a directory.");
//			}
//			_designRoot = designRootPath;
//		} else {
			_designRoot = modelConfig.getBaseDir("design");
//		}

		//	        $this->_cacheChecksum = null;
		//	        $this->setCacheId("config_theme");
		//	        $this->setCache(Mage::app()->getCache());
		//	        if (!$this->loadCache()) {
		loadString("<theme />");
		List<File> files = new ArrayList<File>();
		collectDesignFile(new File(_designRoot), 0, files);

		for (File file : files) {
			Varien_Simplexml_Config config = new Varien_Simplexml_Config(Simplexml_Element_JDom.class);
			config.loadFile(file);
			String[] pathes = file.getPath().split("/|\\\\");
			String area = pathes[4];
			String pack = pathes[3];
			String theme = pathes[2];
			setNode(area + "/" + pack + "/" + theme, null);
			getNode(area + "/" + pack + "/" + theme).extend(config.getNode(null));
		}
		//	            $this->saveCache();
		////	        }
	}

	// match "/*/*/*/etc/theme.xml"
	private void collectDesignFile(File current, int depth, List<File> result) {
		if (depth < 3) {
			for (File file : current.listFiles()) {
				if (!file.isDirectory()) {
					continue;
				}

				collectDesignFile(file, depth + 1, result);
			}
		} else if (depth == 3) {
			for (File file : current.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return dir.isDirectory() && "etc".equals(name);
				}
			})) {
				collectDesignFile(file, depth + 1, result);
			}
		} else {
			for (File file : current.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return "theme.xml".equals(name);
				}
			})) {
				result.add(file);
			}
		}
	}
}
