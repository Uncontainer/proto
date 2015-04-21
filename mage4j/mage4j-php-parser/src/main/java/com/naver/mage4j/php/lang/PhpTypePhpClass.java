//package com.naver.mage4j.php.lang;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.collections.CollectionUtils;
//
//import com.naver.mage4j.php.code.PhpClass;
//
//public class PhpTypePhpClass implements PhpType {
//	private final PhpClass phpClass;
//	private List<PhpType> superTypes = new ArrayList<PhpType>();
//
//	public PhpTypePhpClass(PhpClass phpClass) {
//		super();
//		this.phpClass = phpClass;
//
//		String extendsClassName = phpClass.getExtendsClassName();
//		if (extendsClassName != null) {
//			superTypes.add(PhpTypeFactory.get(extendsClassName));
//		}
//
//		List<String> interfaceNames = phpClass.getImplementsInterfaceNames();
//		if (CollectionUtils.isNotEmpty(interfaceNames)) {
//			for (String interfaceName : interfaceNames) {
//				superTypes.add(PhpTypeFactory.get(interfaceName));
//			}
//		}
//	}
//
//	@Override
//	public String getName() {
//		return phpClass.getName();
//	}
//
//	@Override
//	public boolean isAssignableFrom(PhpType child) {
//		if (child == this) {
//			return true;
//		}
//
//		if (child instanceof PhpTypePhpClass) {
//			for (PhpType each : ((PhpTypePhpClass)child).superTypes) {
//				if (isAssignableFrom(each)) {
//					return true;
//				}
//			}
//		}
//
//		return false;
//	}
//
//	@Override
//	public Class<?> getJavaClass() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
