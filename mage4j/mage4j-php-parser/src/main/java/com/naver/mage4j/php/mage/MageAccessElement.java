package com.naver.mage4j.php.mage;

public interface MageAccessElement extends MageExpression {
	int LITERAL = 0x00000001;
	int FUNCTION = 0x00000002;
	int STATIC = 0x00000004;
	int ARRAY = 0x00000008;
	int MAP = 0x00000010;
	int LIST = 0x00000020;
	int CLASS = 0x00000040;

//	int getAccessTypes();
//
//	String getNameString();

	//
	//	List<MageExpression> getArgs();
}
