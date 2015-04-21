package com.naver.mage4j.php.mage.converter.op;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.naver.mage4j.php.code.PhpBinaryOperator;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.code.PhpExpressionBinary;
import com.naver.mage4j.php.mage.MageContext;
import com.naver.mage4j.php.mage.MageContextAdapter;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.MageExpressionBinary;

public class BinaryOperationConvertContext extends MageContextAdapter {
	private Scanner scanner;
	private MageExpression outputLeft;
	private Stack<PhpBinaryOperator> outputOperations = new Stack<PhpBinaryOperator>();
	private Stack<MageExpression> outputRights = new Stack<MageExpression>();

	public BinaryOperationConvertContext(PhpExpressionBinary input, MageContext mageContext) {
		super(mageContext);
		this.outputLeft = mageContext.buildExpression(input.getLeft());
		this.scanner = new Scanner(input.getOperations(), input.getRights());
	}

	public MageExpressionBinary convert() {
		//		mageContext.visit(outputLeft);
		while (!scanner.eof()) {
			int position = scanner.getPosition();

			BinaryOperationConverter converter = getConverter(getOperation());
			if (converter == null) {
				converter = BinaryOperationConverter.BY_PASS;
			}

			//			mageContext.visit(getRight());
			converter.convert(this);

			if (position == scanner.getPosition()) {
				throw new IllegalStateException(getStackTrace("Scanner position is not changed."));
			}
		}

		return new MageExpressionBinary(outputLeft, outputOperations, outputRights);
	}

	public MageExpression getLeft() {
		if (outputRights.isEmpty()) {
			return outputLeft;
		} else {
			return outputRights.peek();
		}
	}

	public PhpBinaryOperator getOperation() {
		return scanner.getOperation();
	}

	public MageExpression getRight() {
		return scanner.getRight();
	}

	public void pass() {
		outputOperations.add(scanner.getOperation());
		outputRights.add(scanner.getRight());
		scanner.consume();
	}

	public void addMerged(MageExpression right) {
		if (outputRights.isEmpty()) {
			outputLeft = right;
		} else {
			outputRights.pop();
			outputRights.push(right);
		}
		scanner.consume();
	}

	private class Scanner {
		private List<PhpBinaryOperator> operations;
		private List<MageExpression> rights;
		private int index;

		public Scanner(List<PhpBinaryOperator> operations, List<PhpExpression> phpRights) {
			super();
			this.operations = operations;
			this.rights = new ArrayList<MageExpression>(phpRights.size());
			for (PhpExpression phpRight : phpRights) {
				this.rights.add(buildExpression(phpRight));
			}
		}

		public PhpBinaryOperator getOperation() {
			return operations.get(index);
		}

		public MageExpression getRight() {
			return rights.get(index);
		}

		public void consume() {
			index++;
		}

		public int getPosition() {
			return index;
		}

		public boolean eof() {
			return index >= operations.size();
		}
	}

	private static final ConverterResistory REGISTRY = new ConverterResistory();

	public static BinaryOperationConverter getConverter(PhpBinaryOperator operator) {
		return REGISTRY.get(operator);
	}

	public static void registConverter(PhpBinaryOperator operator, BinaryOperationConverter converter) {
		REGISTRY.regist(operator, converter);
	}

	private static class ConverterResistory {
		private Map<PhpBinaryOperator, BinaryOperationConverter> converterMap;

		ConverterResistory() {
			converterMap = new EnumMap<PhpBinaryOperator, BinaryOperationConverter>(PhpBinaryOperator.class);
			regist(PhpBinaryOperator.PLUS, new PhpAdditionConverter());
			regist(PhpBinaryOperator.ASSIGN, new PhpAssignmentConverter());
		}

		public BinaryOperationConverter get(PhpBinaryOperator operator) {
			return converterMap.get(operator);
		}

		public void regist(PhpBinaryOperator operator, BinaryOperationConverter converter) {
			converterMap.put(operator, converter);
		}
	}
}
