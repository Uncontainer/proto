package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.code.PhpStatement;
import com.naver.mage4j.php.lang.PhpType;

public interface MageContext {
	public enum VariableScopeType {
		CLASS, FUNCTION, FUNCTION_PARAM, FOR;
	}

	public interface VisitListener {
		void afterVisit(MageStatement statement);
	}

	void addListener(VisitListener listener);

	void removeListener(VisitListener listener);

	<T extends MageStatement> T buildStatement(PhpStatement statement);

	<T extends MageExpression> T buildExpression(PhpExpression statement, PhpType... expectedTypes);

	<T extends MageStatement> List<T> buildStatements(List<? extends PhpStatement> phpStatements);

	MageClass getCurrentClass();

	void enterScope(MageScope scope);

	void leaveScope();

	void enterClass(MageClass mageClass);

	void leaveClass();

	MageAccessVariable getVariable(String name);

	MageAccessVariable registVariable(PhpAccessVariable variable, PhpType type, VariableScopeType scope);

	MageAccessVariable registVariable(String name, PhpType type, VariableScopeType scope);

	void setVariableScope(String name, VariableScopeType scope);

	MageAccessVariable getVariableOrRegist(PhpAccessVariable variable, PhpType type, VariableScopeType scope);

	MageAccessVariable getVariableOrRegist(String name, PhpType type, VariableScopeType scope);

	ScopeContext getScopeContext();

	void registFunction(String name, PhpType type, boolean classMember);

	MageClassMethod getFunctionSignature(String name);

	String getStackTrace(String message);
}
