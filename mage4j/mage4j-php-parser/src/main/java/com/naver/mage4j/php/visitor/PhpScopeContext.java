//package com.naver.mage4j.php.visitor;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.naver.mage4j.php.code.PhpAccessVariable;
//import com.naver.mage4j.php.code.PhpScope;
//import com.naver.mage4j.php.code.PhpVariableDeclaration;
//import com.naver.mage4j.php.converter.PhpTypeUtils;
//import com.naver.mage4j.php.lang.PhpType;
//import com.naver.mage4j.php.visitor.PhpTypeCheckContext.VariableScopeType;
//
//public class PhpScopeContext {
//	public static final PhpScopeContext ROOT = new PhpScopeContext();
//
//	private final PhpScope scope;
//	private final PhpScopeContext parent;
//	private Map<String, Entry> variableTypeMap = new HashMap<String, Entry>();
//	private Map<String, Entry> literalMap = new HashMap<String, Entry>();
//	private Set<String> needsDeclaration;
//
//	static class Entry {
//		final VariableScopeType scope;
//		final PhpType type;
//
//		public Entry(VariableScopeType scope, PhpType type) {
//			super();
//			this.scope = scope;
//			this.type = type;
//		}
//	}
//
//	private PhpScopeContext() {
//		this(null, null);
//	}
//
//	public PhpScopeContext(PhpScope scope, PhpScopeContext parent) {
//		this.scope = scope;
//		this.parent = parent;
//	}
//
//	public PhpScope getScope() {
//		return scope;
//	}
//
//	public PhpType getType(String name, boolean literal) {
//		Map<String, Entry> typeMap = getTypeMap(literal);
//		if (typeMap.containsKey(name)) {
//			Entry entry = typeMap.get(name);
//			return entry != null ? entry.type : null;
//		}
//
//		if (parent != null) {
//			return parent.getType(name, literal);
//		}
//
//		return null;
//	}
//
//	public List<PhpVariableDeclaration> getNeedsDeclarationTypes() {
//		if (needsDeclaration == null) {
//			return Collections.emptyList();
//		}
//
//		List<PhpVariableDeclaration> result = new ArrayList<PhpVariableDeclaration>(needsDeclaration.size());
//		for (String each : needsDeclaration) {
//			Entry entry = variableTypeMap.get(each);
//			if (entry != null) {
//				result.add(new PhpVariableDeclaration(entry.type, new PhpAccessVariable(each, 1)));
//			}
//		}
//
//		return result;
//	}
//
//	public void updateType(String name, PhpType type, boolean literal, VariableScopeType scope) {
//		if (upateTypeIfExist(name, type, literal, scope)) {
//			return;
//		}
//
//		getTypeMap(literal).put(name, new Entry(scope, type));
//		if (!literal && scope == VariableScopeType.UNKNOWN && !"this".equals(name) && !"super".equals(name) && !"self".equals(name)) {
//			if (needsDeclaration == null) {
//				needsDeclaration = new HashSet<String>();
//			}
//			needsDeclaration.add(name);
//		}
//	}
//
//	private Map<String, Entry> getTypeMap(boolean literal) {
//		return literal ? literalMap : variableTypeMap;
//	}
//
//	private boolean upateTypeIfExist(String name, PhpType type, boolean literal, VariableScopeType scope) {
//		Map<String, Entry> typeMap = getTypeMap(literal);
//		if (typeMap.containsKey(name)) {
//			if (type == null) {
//				typeMap.remove(name);
//			} else {
//				Entry savedType = typeMap.get(name);
//				PhpType selectedType = PhpTypeUtils.select(savedType.type, type);
//				typeMap.put(name, new Entry(scope, selectedType));
//			}
//
//			return true;
//		} else {
//			if (parent == null) {
//				return false;
//			}
//
//			return parent.upateTypeIfExist(name, type, literal, scope);
//		}
//	}
//}
