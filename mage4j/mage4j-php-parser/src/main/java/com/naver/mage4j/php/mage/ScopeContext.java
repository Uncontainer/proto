package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.MageContext.VariableScopeType;

public class ScopeContext {
	public static final ScopeContext ROOT = new ScopeContext();

	private final MageScope scope;
	private final ScopeContext parent;
	private Map<String, ScopedVariable> variableMap = new HashMap<String, ScopedVariable>();

	//	private Map<String, Entry> literalMap = new HashMap<String, Entry>();

	static class ScopedVariable extends MageAccessVariable {
		private ScopedVariable(VariableScopeType scope, String name) {
			super(name);
			this.scope = scope;
			this.name = name;
		}

		@Override
		public PhpType getType() {
			return super.getType();
		}

		@Override
		public void setType(PhpType type) {
			super.setType(type);
		}
	}

	private ScopeContext() {
		this(null, null);
	}

	public ScopeContext(MageScope scope, ScopeContext parent) {
		this.scope = scope;
		this.parent = parent;
	}

	public MageScope getScope() {
		return scope;
	}

	public PhpType getVariableType(String name, boolean literal) {
		if (variableMap.containsKey(name)) {
			ScopedVariable entry = variableMap.get(name);
			return entry != null ? entry.getType() : null;
		}

		if (parent != null) {
			return parent.getVariableType(name, literal);
		}

		return null;
	}

	public List<MageVariableDeclaration> getNeedsDeclarationTypes() {
		List<MageVariableDeclaration> result = new ArrayList<MageVariableDeclaration>();

		for (MageAccessVariable variable : variableMap.values()) {
			VariableScopeType scopeType = variable.getScope();
			if (scopeType == null || scopeType == VariableScopeType.FUNCTION) {
				result.add(new MageVariableDeclaration(variable));
			}
		}

		return result;
	}

	public MageAccessVariable getVariable(String name) {
		ScopedVariable entry = variableMap.get(name);
		if (entry != null) {
			return entry;
		}

		if (parent != null) {
			return parent.getVariable(name);
		}

		return null;
	}

	public MageAccessVariable addVariable(String name, VariableScopeType scope) {
		if (variableMap.containsKey(scope)) {
			throw new IllegalStateException("Variable has already declared.(" + name + ")");
		}

		ScopedVariable result = new ScopedVariable(scope, name);
		variableMap.put(name, result);

		return result;
	}
}
