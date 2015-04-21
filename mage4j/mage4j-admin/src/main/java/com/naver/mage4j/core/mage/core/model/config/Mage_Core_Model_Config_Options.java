package com.naver.mage4j.core.mage.core.model.config;

import java.io.File;
import java.util.Map;

import com.naver.mage4j.external.varien.Varien_Object;

public class Mage_Core_Model_Config_Options extends Varien_Object {
	public Mage_Core_Model_Config_Options(Map<String, Object> options) {
		super(options);

		// TODO 설정으로 분리
		String appRoot = new File(".").getAbsolutePath();
		appRoot = "E:/repository/httpd/magento/app";

		String root = new File(appRoot).getParent();
		_data.put("app_dir", appRoot);
		_data.put("base_dir", root);
		_data.put("etc_dir", appRoot + "/etc");
		_data.put("code_dir", appRoot + "/code");
		_data.put("design_dir", appRoot + "/design");
		_data.put("etc_dir", appRoot + "/etc");
		_data.put("lib_dir", root + "/lib");
		_data.put("locale_dir", appRoot + "/locale");
		_data.put("media_dir", root + "/media");
		_data.put("skin_dir", root + "/skin");
		String varDir = root + "/var";
		new File(varDir).mkdirs();
		_data.put("var_dir", varDir);
		_data.put("tmp_dir", varDir + "/tmp");
		_data.put("cache_dir", varDir + "/cache");
		_data.put("log_dir", varDir + "/log");
		_data.put("session_dir", varDir + "/session");
		_data.put("upload_dir", _data.get("media_dir") + "/upload");
		_data.put("export_dir", varDir + "/export");
	}

	public String getDir(String type) {
		return (String)_data.get(type + "_dir");
	}

	public String getAppDir() {
		return (String)_data.get("app_dir");
	}

	public String getBaseDir() {
		return (String)_data.get("base_dir");
	}

	public String getCodeDir() {
		return (String)_data.get("code_dir");
	}

	public String getDesignDir() {
		return (String)_data.get("design_dir");
	}

	public String getAppRoot() {
		return (String)_data.get("app_dir");
	}

	public String getEtcDir() {
		return (String)_data.get("etc_dir");
	}

	public String getLibDir() {
		return (String)_data.get("lib_dir");
	}

	public String getLocaleDir() {
		return (String)_data.get("locale_dir");
	}

	public String getMediaDir() {
		return (String)_data.get("media_dir");
	}

	public String getSkinDir() {
		return (String)_data.get("skin_dir");
	}

	//    public String getSysTmpDir()
	//    {
	//        return sys_get_temp_dir();
	//    }

	public String getVarDir() {
		throw new UnsupportedOperationException();
		//	        $dir = isset((String)_data.get("var_dir")) ? (String)_data.get("var_dir");
		//	            : (String)_data.get("base_dir"); . DS . self::VAR_DIRECTORY;
		//	        if (!$this->createDirIfNotExists($dir)) {
		//	            $dir = $this->getSysTmpDir().DS.'magento'.DS.'var';
		//	            if (!$this->createDirIfNotExists($dir)) {
		//	                throw new Mage_Core_Exception('Unable to find writable var_dir');
		//	            }
		//	        }
		//	        return $dir;
	}
	//
	//    public String getTmpDir()
	//    {
	//        //$dir = $this->getDataSetDefault('tmp_dir', $this->getVarDir().DS.'tmp');
	//        $dir = (String)_data.get("tmp_dir");;
	//        if (!$this->createDirIfNotExists($dir)) {
	//            $dir = $this->getSysTmpDir().DS.'magento'.DS.'tmp';
	//            if (!$this->createDirIfNotExists($dir)) {
	//                throw new Mage_Core_Exception('Unable to find writable tmp_dir');
	//            }
	//        }
	//        return $dir;
	//    }

	//    public String getCacheDir()    {
	//        //$dir = $this->getDataSetDefault('cache_dir', $this->getVarDir().DS.'cache');
	//        String dir = (String)_data.get("cache_dir");;
	//        createDirIfNotExists(dir);
	//        return dir;
	//    }
	//
	//    public String getLogDir()
	//    {
	//        //$dir = $this->getDataSetDefault('log_dir', $this->getVarDir().DS.'log');
	//        $dir = (String)_data.get("log_dir");;
	//        $this->createDirIfNotExists($dir);
	//        return $dir;
	//    }
	//
	//    public String getSessionDir()
	//    {
	//        //$dir = $this->getDataSetDefault('session_dir', $this->getVarDir().DS.'session');
	//        $dir = (String)_data.get("session_dir");;
	//        $this->createDirIfNotExists($dir);
	//        return $dir;
	//    }
	//
	//    public String getUploadDir()
	//    {
	//        //$dir = $this->getDataSetDefault('upload_dir', $this->getMediaDir().DS.'upload');
	//        $dir = (String)_data.get("upload_dir");;
	//        $this->createDirIfNotExists($dir);
	//        return $dir;
	//    }
	//
	//    public String getExportDir()
	//    {
	//        //$dir = $this->getDataSetDefault('export_dir', $this->getVarDir().DS.'export');
	//        $dir = (String)_data.get("export_dir");;
	//        $this->createDirIfNotExists($dir);
	//        return $dir;
	//    }
	//
	//    public String createDirIfNotExists($dir)
	//    {
	//        if (!empty($this->_dirExists[$dir])) {
	//            return true;
	//        }
	//        if (file_exists($dir)) {
	//            if (!is_dir($dir)) {
	//                return false;
	//            }
	//            if (!is_dir_writeable($dir)) {
	//                return false;
	//            }
	//        } else {
	//            $oldUmask = umask(0);
	//            if (!@mkdir($dir, 0777, true)) {
	//                return false;
	//            }
	//            umask($oldUmask);
	//        }
	//        $this->_dirExists[$dir] = true;
	//        return true;
	//    }
}
