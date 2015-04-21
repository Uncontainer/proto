package com.naver.mage4j.php.mage;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessFunction extends MageExpressionAbstract implements MageAccessElement {
	private MageExpression name;
	private List<MageExpression> args;

	public MageAccessFunction(MageExpression name, List<MageExpression> args) {
		super();
		this.name = name;
		this.args = args;
	}

	public MageExpression getName() {
		return name;
	}

	public List<MageExpression> getArgs() {
		return args;
	}

	//	@Override
	//	public int getAccessTypes() {
	//		return (name.getAccessTypes() & LITERAL) | FUNCTION;
	//	}
	//
	//	@Override
	//	public String getNameString() {
	//		return name.getNameString();
	//	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.visit(name).visit(args, "(", ")", ", ");
	}

	@Override
	public String toString() {
		return name + "(" + StringUtils.join(args.stream().map(s -> s.toString()).collect(Collectors.toList()), ", ") + ")";
	}
}
