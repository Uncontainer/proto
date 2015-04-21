//package com.naver.mage4j.php.mage;
//
//import com.naver.mage4j.php.lang.PhpTypeFactory;
//import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;
//
//public class MageAccessStatic extends MageExpressionTypeDelegated implements MageAccessElement {
//	private String className;
//	private MageAccessElement member;
//
//	public MageAccessStatic() {
//		super();
//	}
//
//	public String getClassName() {
//		return className;
//	}
//
//	public MageAccessElement getMember() {
//		return member;
//	}
//
//	@Override
//	public String toString() {
//		return className + "::" + member;
//	}
//
//	@Override
//	public void visitJava(JavaCodeGenerateContext context) {
//		if (!"self".equals(className)) {
//			context.addImport(PhpTypeFactory.get(className));
//			context.appendCode(className).appendCode(".");
//		}
//		context.visit(member);
//	}
//
//	@Override
//	public MageExpression getDeletateElement() {
//		return member;
//	}
//
//	//	@Override
//	//	public int getAccessTypes() {
//	//		return STATIC | member.getAccessTypes();
//	//	}
//	//
//	//	@Override
//	//	public String getNameString() {
//	//		return member.getNameString();
//	//	}
//}
