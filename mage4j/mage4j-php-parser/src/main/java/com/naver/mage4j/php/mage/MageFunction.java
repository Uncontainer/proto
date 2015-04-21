package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpClassFunction;
import com.naver.mage4j.php.code.PhpFunction;
import com.naver.mage4j.php.code.PhpVariableDeclaration;
import com.naver.mage4j.php.doc.PhpDocFunction;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageFunction<T extends PhpFunction> implements MageFunctionSignature, MageStatement, MageStatementInitializable<T>, MageScope {
	protected PhpFunction original;

	protected PhpDocFunction comment;
	protected PhpType returnType;
	protected List<MageVariableDeclaration> params = new ArrayList<MageVariableDeclaration>();
	protected MageStatementBlock body;

	MageFunction() {
	}

	@Override
	public void init(MageContext context, PhpFunction function) {
		this.original = function;

		List<String> commentStrings = function.getDocumentComments();
		if (CollectionUtils.isNotEmpty(commentStrings)) {
			comment = new PhpDocFunction(commentStrings.get(commentStrings.size() - 1));
		}

		ReturnExpressionCollector listener = new ReturnExpressionCollector();
		context.addListener(listener);

		for (PhpVariableDeclaration phpParam : function.getParams()) {
			MageVariableDeclaration param = context.buildStatement(phpParam);
			params.add(param);

			PhpType paramType = comment != null ? comment.getParamType(phpParam.getName().getName()) : null;
			param.setType(paramType);
			param.getName().setScope(MageContext.VariableScopeType.FUNCTION_PARAM);
		}

		body = context.buildStatement(function.getBody());

		context.removeListener(listener);

		returnType = PhpTypeUtils.select(function.getReturnType(), listener.getType(), comment != null ? comment.getReturnType() : null);

		if (returnType == null) {
			returnType = PhpTypeFactory.UNDECIDED;
		}

		context.registFunction(getName(), returnType, function instanceof PhpClassFunction);

		List<MageVariableDeclaration> needsDeclarationTypes = context.getScopeContext().getNeedsDeclarationTypes();
		if (!needsDeclarationTypes.isEmpty()) {
			body.addAll(0, needsDeclarationTypes);
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		visitComment(context);
		context.visit(returnType).appendCode(" ").appendCode(original.getName()).visit(params, "(", ")", ", ");
		visitBody(context);
	}

	protected void visitComment(JavaCodeGenerateContext context) {
		if (comment != null && !comment.isBlank()) {
			context.appendMultilineCode(comment.toString()).appendNewLine();
		}
	}

	protected void visitBody(JavaCodeGenerateContext context) {
		context.appendCode("{");
		if (body != null) {
			context.appendNewLine()
				.increaseIndent().visit(body)
				.appendNewLine().decreaseIndent();
		}
		context.appendCode("}");
	}

	@Override
	public PhpType getReturnType() {
		return returnType;
	}

	public void setReturnType(PhpType returnType) {
		this.returnType = returnType;
	}

	@Override
	public String getName() {
		return original.getName();
	}

	@Override
	public List<MageVariableDeclaration> getParameters() {
		return params;
	}

	public MageStatementBlock getBody() {
		return body;
	}

	@Override
	public String toString() {
		return returnType + " " + getName() + "(" + StringUtils.join(getParameters().stream().map(s -> s.toString()).collect(Collectors.toList()), ", ") + ")";
	}

	private static class ReturnExpressionCollector implements MageContext.VisitListener {
		private List<MageStatementReturn> returnStatements = new ArrayList<MageStatementReturn>(3);

		@Override
		public void afterVisit(MageStatement code) {
			if (code instanceof MageStatementReturn) {
				returnStatements.add((MageStatementReturn)code);
			}
		}

		public PhpType getType() {
			if (returnStatements.isEmpty()) {
				return PhpTypeFactory.VOID;
			}

			return PhpTypeUtils.select(returnStatements.stream().map(s -> s.getReturnType()).collect(Collectors.toList()));
		}
	}
}
