package com.naver.mage4j.php.mage.converter.access;

import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.MageExpressionAbstract;

public abstract class MageAccessComposite extends MageExpressionAbstract implements MageAccessElement {
	protected final MageExpression name;
	protected final MageExpression index;

	public MageAccessComposite(MageExpression name, MageExpression index) {
		super();
		this.name = name;
		this.index = index;
	}

	public MageExpression getName() {
		return name;
	}

	public MageExpression getIndex() {
		return index;
	}

	//	@Override
	//	public List<PhpExpression> getArgs() {
	//		return index != null ? Arrays.asList(index) : Collections.emptyList();
	//	}
	//
	//	@Override
	//	public void visitType(PhpTypeCheckContext context, PhpType... expectedTypes) {
	//		context.visit(name, expectedTypes).visit(index);
	//	}

//	@Override
//	public int getAccessTypes() {
//		return (name.getAccessTypes() & LITERAL) | FUNCTION;
//	}
//
//	@Override
//	public String getNameString() {
//		return name.getNameString();
//	}
}
