package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpCode;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.code.PhpStatement;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;

public class MageContextImpl implements MageContext {
	private Stack<ScopeContext> scopeStack = new Stack<ScopeContext>();
	private Stack<MageClass> classStack = new Stack<MageClass>();
	private Stack<PhpCode> callStack = new Stack<PhpCode>();

	private List<VisitListener> listeners = new ArrayList<VisitListener>();

	private Map<PhpStatement, MageStatement> buildResult = new HashMap<PhpStatement, MageStatement>();

	public MageContextImpl() {
		scopeStack.push(ScopeContext.ROOT);
	}

	@Override
	public void addListener(VisitListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(VisitListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void enterScope(MageScope scope) {
		scopeStack.push(new ScopeContext(scope, scopeStack.peek()));
	}

	@Override
	public void leaveScope() {
		scopeStack.pop();
	}

	@Override
	public void enterClass(MageClass mageClass) {
		classStack.add(mageClass);
	}

	@Override
	public void leaveClass() {
		classStack.pop();
	}

	@Override
	public MageClass getCurrentClass() {
		return !classStack.isEmpty() ? classStack.peek() : null;
	}

	@Override
	public MageAccessVariable registVariable(PhpAccessVariable variable, PhpType type, VariableScopeType scope) {
		if (variable.isLiteral()) {
			throw new IllegalStateException();
		}

		return registVariable(variable.getName(), type, scope);
	}

	@Override
	public MageAccessVariable registVariable(String name, PhpType type, VariableScopeType scope) {
		MageAccessVariable result = scopeStack.peek().addVariable(name, scope);
		if (type != null) {
			result.setType(type);
		}

		return result;
	}

	@Override
	public MageAccessVariable getVariable(String name) {
		if ("this".equals(name)) {
			return new MageAccessVariable(name, getCurrentClass().getType());
		} else if ("super".equals(name)) {
			return new MageAccessVariable(name, getCurrentClass().getExtendsClass());
		}

		return scopeStack.peek().getVariable(name);
	}

	@Override
	public void setVariableScope(String name, VariableScopeType scope) {
		getVariable(name).setScope(scope);
	}

	@Override
	public MageAccessVariable getVariableOrRegist(PhpAccessVariable variable, PhpType type, VariableScopeType scope) {
		if (variable.isLiteral()) {
			throw new IllegalStateException();
		}

		return getVariableOrRegist(variable.getName(), type, scope);
	}

	@Override
	public MageAccessVariable getVariableOrRegist(String name, PhpType type, VariableScopeType scope) {
		MageAccessVariable variable = getVariable(name);
		if (variable == null) {
			variable = registVariable(name, type, scope);
		}

		return variable;
	}

	public PhpType getType(String name, boolean function) {
		if (function) {
			MageClassMethod signature = getFunctionSignature(name);
			return signature != null ? signature.getReturnType() : null;
		} else {
			if ("this".equals(name) || "self".equals(name)) {
				return getCurrentClass().getType();
			} else if ("super".equals(name)) {
				return getCurrentClass().getExtendsClass();
			} else {
				return getVariable(name).getType();
			}
		}
	}

	//	public void updateType(String name, PhpType type, boolean function, VariableScopeType scopeType) {
	//		scopeStack.peek().updateType(name, type, function, scopeType);
	//	}
	//

	@Override
	public void registFunction(String name, PhpType type, boolean classMember) {
		if (!classMember) {
			throw new UnsupportedOperationException();
		}

		//		for (int i = scopeStack.size() - 1; i > 0; i--) {
		//			MageClass currentClass = getCurrentClass();
		//			ScopeContext scopeContext = scopeStack.get(i);
		//			if (scopeContext.getScope() == currentClass) {
		//				scopeContext.updateType(name, type, true, VariableScopeType.FUNCTION);
		//			}
		//		}
	}

	@Override
	public MageClassMethod getFunctionSignature(String name) {
		MageClass clazz = getCurrentClass();
		if (clazz != null) {
			return clazz.getFunction(name);
		} else {
			// TODO 전역함수 검색 코드 추가.
			return null;
		}
	}

	@Override
	public ScopeContext getScopeContext() {
		return scopeStack.peek();
	}

	@Override
	public String getStackTrace(String message) {
		StringBuilder builder = new StringBuilder();
		if (message != null) {
			builder.append(message).append("\n");
		}

		for (int i = callStack.size() - 1; i >= 0; i--) {
			PhpCode code = callStack.get(i);
			builder.append(code.getClass().getSimpleName()).append(": ").append(code).append("\n");
		}

		return builder.toString();
	}

	private static class TypeEntry {
		final PhpExpression expression;
		final PhpType[] types;

		TypeEntry(PhpExpression expression, List<PhpType> types) {
			super();
			this.expression = expression;
			this.types = types.toArray(new PhpType[types.size()]);
		}
	}

	private Stack<TypeEntry> expectedTypesStack = new Stack<TypeEntry>();
	private final PhpType[] TYPES_NULL = new PhpType[0];

	public PhpType[] getExpectedTypes(PhpExpression expression) {
		if (expectedTypesStack.isEmpty()) {
			return TYPES_NULL;
		}

		if (expectedTypesStack.peek().expression == expression) {
			return expectedTypesStack.peek().types;
		} else {
			return TYPES_NULL;
		}
	}

	@Override
	public <T extends MageStatement> T buildStatement(PhpStatement statement) {
		if (statement == null) {
			return null;
		}

		//		if (buildResult.containsKey(statement)) {
		//			throw new IllegalStateException();
		//		}
		T result = (T)buildResult.get(statement);
		if (result != null) {
			return result;
		}

		callStack.push(statement);
		try {
			result = MageStatementFactory.create(this, statement);
			buildResult.put(statement, result);
			for (VisitListener listener : listeners) {
				listener.afterVisit(result);
			}

			return result;
		} catch (RuntimeException e) {
			if (e instanceof MageConvertException) {
				throw e;
			} else {
				throw new MageConvertException(getStackTrace(e.getMessage()), e);
			}
		} finally {
			callStack.pop();
		}
	}

	@Override
	public <T extends MageExpression> T buildExpression(PhpExpression statement, PhpType... expectedTypes) {
		if (statement == null) {
			return null;
		}

		boolean typePushed = false;
		if (expectedTypes.length != 0) {
			List<PhpType> decideTypes = new ArrayList<PhpType>(expectedTypes.length);
			for (PhpType type : expectedTypes) {
				if (PhpTypeUtils.isDecided(type)) {
					decideTypes.add(type);
				}
			}

			if (!decideTypes.isEmpty()) {
				typePushed = true;
				expectedTypesStack.push(new TypeEntry(statement, decideTypes));
			}
		}

		try {
			return buildStatement(statement);
		} finally {
			if (typePushed) {
				expectedTypesStack.pop();
			}
		}
	}

	@Override
	public <T extends MageStatement> List<T> buildStatements(List<? extends PhpStatement> phpStatements) {
		if (phpStatements == null || phpStatements.isEmpty()) {
			return Collections.emptyList();
		}

		List<T> result = new ArrayList<T>(phpStatements.size());
		for (PhpStatement phpStatement : phpStatements) {
			result.add(buildStatement(phpStatement));
		}

		return result;
	}
}
