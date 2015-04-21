package com.naver.mage4j.php.mage.converter.op;



public interface BinaryOperationConverter {
	BinaryOperationConverter BY_PASS = new BinaryOperationConverter() {
		@Override
		public void convert(BinaryOperationConvertContext context) {
			context.pass();
		}
	};

	void convert(BinaryOperationConvertContext context);
}
