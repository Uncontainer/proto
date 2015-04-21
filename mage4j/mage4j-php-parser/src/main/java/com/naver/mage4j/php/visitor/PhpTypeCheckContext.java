//package com.naver.mage4j.php.visitor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Stack;
//
//import com.naver.mage4j.php.code.PhpClass;
//import com.naver.mage4j.php.code.PhpCode;
//import com.naver.mage4j.php.code.PhpFunction;
//import com.naver.mage4j.php.code.PhpScope;
//import com.naver.mage4j.php.lang.PhpType;
//import com.naver.mage4j.php.lang.PhpTypeFactory;
//
//public class PhpTypeCheckContext {
//	private Stack<PhpScopeContext> scopeStack = new Stack<PhpScopeContext>();
//	private Stack<PhpClass> classStack = new Stack<PhpClass>();
//	private Stack<PhpCode> callStack = new Stack<PhpCode>();
//
//	private List<VisitListener> listeners = new ArrayList<VisitListener>();
//
//	public enum VariableScopeType {
//		CLASS, FUNCTION, UNKNOWN, FUNCTION_PARAM;
//	}
//
//	public PhpTypeCheckContext() {
//		scopeStack.push(PhpScopeContext.ROOT);
//	}
//
//	public PhpTypeCheckContext visit(PhpCode code, PhpType... expectedTypes) {
//		if (code != null) {
//			callStack.push(code);
//			try {
//				boolean newScope = code instanceof PhpScope;
//				boolean isClass = code instanceof PhpClass;
//				if (newScope) {
//					enterBlock((PhpScope)code);
//				}
//				if (isClass) {
//					classStack.push((PhpClass)code);
//				}
//
//				try {
//					code.visitType(this, expectedTypes);
//					listeners.stream().forEach(s -> s.afterVisit(code));
//				} finally {
//					if (newScope) {
//						leaveBlock();
//					}
//					if (isClass) {
//						classStack.pop();
//					}
//				}
//			} finally {
//				callStack.pop();
//			}
//		}
//
//		return this;
//	}
//
//	public PhpTypeCheckContext visits(List<? extends PhpCode> codes) {
//		if (codes != null) {
//			for (PhpCode code : codes) {
//				visit(code);
//			}
//		}
//
//		return this;
//	}
//
//	public void addListener(VisitListener listener) {
//		listeners.add(listener);
//	}
//
//	public void removeListener(VisitListener listener) {
//		listeners.remove(listener);
//	}
//
//	public interface VisitListener {
//		void afterVisit(PhpCode code);
//	}
//
//	private void enterBlock(PhpScope scope) {
//		scopeStack.push(new PhpScopeContext(scope, scopeStack.peek()));
//	}
//
//	private void leaveBlock() {
//		scopeStack.pop();
//	}
//
//	public void updateType(String name, PhpType type, boolean literal, VariableScopeType scopeType) {
//		scopeStack.peek().updateType(name, type, literal, scopeType);
//	}
//
//	public void registFunction(String name, PhpType type, boolean classMember) {
//		if (!classMember) {
//			throw new UnsupportedOperationException();
//		}
//
//		for (int i = scopeStack.size() - 1; i > 0; i--) {
//			PhpClass currentClass = getCurrentClass();
//			PhpScopeContext scopeContext = scopeStack.get(i);
//			if (scopeContext.getScope() == currentClass) {
//				scopeContext.updateType(name, type, true, VariableScopeType.FUNCTION);
//			}
//		}
//	}
//
//	public PhpScopeContext getScopeContext() {
//		return scopeStack.peek();
//	}
//
//	public PhpType getType(String name, boolean literal) {
//		if (!literal) {
//			if ("this".equals(name) || "self".equals(name)) {
//				return PhpTypeFactory.get(getCurrentClass().getName());
//			} else if ("super".equals(name)) {
//				return PhpTypeFactory.get(getCurrentClass().getExtendsClassName());
//			}
//		}
//
//		return scopeStack.peek().getType(name, literal);
//	}
//
//	public PhpClass getCurrentClass() {
//		return !classStack.isEmpty() ? classStack.peek() : null;
//	}
//
//	public PhpFunction getFunctionPrototye(String name) {
//		PhpClass clazz = getCurrentClass();
//		if (clazz != null) {
//			return clazz.getFunction(name);
//		} else {
//			// TODO 전역함수 검색 코드 추가.
//			return null;
//		}
//	}
//
//	public String getStackTrace(String message) {
//		StringBuilder builder = new StringBuilder();
//		if (message != null) {
//			builder.append(message).append("\n");
//		}
//
//		for (int i = callStack.size() - 1; i >= 0; i--) {
//			PhpCode code = callStack.get(i);
//			builder.append(code.getClass().getSimpleName()).append(": ").append(code).append("\n");
//		}
//
//		return builder.toString();
//	}
//}
