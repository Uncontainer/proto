package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.code.PhpStatement;
import com.naver.mage4j.php.lang.PhpType;

public class MageContextAdapter implements MageContext {
	protected final MageContext context;

	public MageContextAdapter(MageContext context) {
		super();
		if (context == null) {
			throw new IllegalArgumentException();
		}

		this.context = context;
	}

	@Override
	public void addListener(VisitListener listener) {
		context.addListener(listener);
	}

	@Override
	public void removeListener(VisitListener listener) {
		context.removeListener(listener);
	}

	@Override
	public <T extends MageStatement> T buildStatement(PhpStatement statement) {
		return context.buildStatement(statement);
	}

	@Override
	public <T extends MageExpression> T buildExpression(PhpExpression statement, PhpType... expectedTypes) {
		return context.buildExpression(statement, expectedTypes);
	}

	@Override
	public <T extends MageStatement> List<T> buildStatements(List<? extends PhpStatement> phpStatements) {
		return context.buildStatements(phpStatements);
	}

	@Override
	public MageClass getCurrentClass() {
		return context.getCurrentClass();
	}

	@Override
	public void enterScope(MageScope scope) {
		context.enterScope(scope);
	}

	@Override
	public void leaveScope() {
		context.leaveScope();
	}

	@Override
	public void enterClass(MageClass mageClass) {
		context.enterClass(mageClass);
	}

	@Override
	public void leaveClass() {
		context.leaveClass();
	}

	@Override
	public MageAccessVariable getVariable(String name) {
		return context.getVariable(name);
	}

	@Override
	public MageAccessVariable registVariable(PhpAccessVariable variable, PhpType type, VariableScopeType scope) {
		return context.registVariable(variable, type, scope);
	}

	@Override
	public MageAccessVariable registVariable(String name, PhpType type, VariableScopeType scope) {
		return context.registVariable(name, type, scope);
	}

	@Override
	public void setVariableScope(String name, VariableScopeType scope) {
		context.setVariableScope(name, scope);
	}

	@Override
	public MageAccessVariable getVariableOrRegist(PhpAccessVariable variable, PhpType type, VariableScopeType scope) {
		return context.getVariableOrRegist(variable, type, scope);
	}

	@Override
	public MageAccessVariable getVariableOrRegist(String name, PhpType type, VariableScopeType scope) {
		return context.getVariableOrRegist(name, type, scope);
	}

	@Override
	public ScopeContext getScopeContext() {
		return context.getScopeContext();
	}

	@Override
	public void registFunction(String name, PhpType type, boolean classMember) {
		context.registFunction(name, type, classMember);
	}

	@Override
	public MageClassMethod getFunctionSignature(String name) {
		return context.getFunctionSignature(name);
	}

	@Override
	public String getStackTrace(String message) {
		return context.getStackTrace(message);
	}

}
