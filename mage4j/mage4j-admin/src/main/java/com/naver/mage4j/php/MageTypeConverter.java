package com.naver.mage4j.php;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;
import com.naver.mage4j.core.mage.core.model.resource.store.UrlType;
import com.naver.mage4j.php.code.PhpAccessClass;
import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.access.AccessConverterClass;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;

public class MageTypeConverter extends AccessConverterClass {
	@Override
	public MageExpression convert(AccessConvertContext context, PhpAccessClass access) {
		PhpExpression current = access.getIndex();
		if (current instanceof PhpAccessFunction) {
			PhpAccessFunction f = (PhpAccessFunction)current;
			String name = PhpAccessUtils.getLiteralFunctionName(f);
			if (name != null) {
				List<MageExpression> args = getMethodArgs(context, f);
				MageAccessJavaMethod appGetMethod = new MageAccessJavaMethod(JavaClassUtils.getMethod(AppContext.class, "getCurrent"));
				MageAccessJavaMethod storeGetMethod = new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getStore"));
				MageAccessJavaMethod storeHelperGetMethod = new MageAccessJavaMethod(storeGetMethod, JavaClassUtils.getMethod(Store.class, "getHelper"));

				if ("app".equals(name)) {
					return appGetMethod;
				} else if ("dispatchEvent".equals(name)) {
					MageAccessElement result = new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getEventDispatcher"));
					result = new MageAccessJavaMethod(result, JavaClassUtils.getMethod(EventDispatcher.class, "dispatchEvent", String.class, Map.class), args);

					return result;
				} else if ("getStoreConfig".equals(name)) {
					if (args.size() == 1) {
						return new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethodWithArgumentCount(Mage_Core_Model_App.class, "getStoreConfigAsString", args), args);
					}
				} else if ("getConfig".equals(name)) {
					return new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getConfig"));
				} else if ("getStoreConfigFlag".equals(name)) {
					return new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getStoreConfigFlag", String.class), args);
				} else if ("getBaseUrl".equals(name)) {
					MageAccessJavaMethod result = new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getStore"));
					result = new MageAccessJavaMethod(result, JavaClassUtils.getMethod(Store.class, "getHelper"));
					if (args.size() == 0) {
						return new MageAccessJavaMethod(result, JavaClassUtils.getMethod(StoreHelper.class, "getBaseUrl"));
					} else {
						return new MageAccessJavaMethod(result, JavaClassUtils.getMethod(StoreHelper.class, "getBaseUrl", UrlType.class, Boolean.class));
					}
				} else if ("getDesign".equals(name)) {
					return new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getDesignPackage"));
				} else if ("isInstalled".equals(name)) {
					return new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "isInstalled"));
				} else if ("getBaseUrl".equals(name)) {
					if (args.size() == 0) {
						Method method = JavaClassUtils.getMethod(StoreHelper.class, "getBaseUrl");
						return new MageAccessJavaMethod(storeHelperGetMethod, method);
					} else if (args.size() == 2) {
						Method method = JavaClassUtils.getMethod(StoreHelper.class, "getBaseUrl", UrlType.class, Mage_Core_Model_Design_Package.Param.class);
						return new MageAccessJavaMethod(storeHelperGetMethod, method, args);
					}
				} else if ("getUrl".equals(name)) {
					return new MageAccessJavaMethod(new MageAccessJavaMethod(appGetMethod, JavaClassUtils.getMethod(Mage_Core_Model_App.class, "getUrl"))
						, JavaClassUtils.getMethod(Mage_Core_Model_Url.class, "getUrl", String.class, RouteParams.class), args);
				}
			}
		}

		return AccessConverterClass.BY_PASS.convert(context);
	}
}
