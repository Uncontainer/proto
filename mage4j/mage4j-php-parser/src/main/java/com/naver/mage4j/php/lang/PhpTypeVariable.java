package com.naver.mage4j.php.lang;


public class PhpTypeVariable extends PhpTypeAdapter {
	final Class<?> javaClass;

	PhpTypeVariable(String name, Class<?> javaClass) {
		super(name);
		this.javaClass = javaClass;
	}

	@Override
	public Class<?> getJavaClass() {
		return javaClass;
	}

//	public void visitJava(JavaCodeGenerateContext context) {
//		context.addImport(javaClass);
//		context.appendCode(name);
//	}
}
