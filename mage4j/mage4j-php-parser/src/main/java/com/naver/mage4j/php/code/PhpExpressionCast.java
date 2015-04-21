package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;

public class PhpExpressionCast implements PhpExpression {
	private PhpExpression target;
	private List<PhpType> types = new ArrayList<PhpType>();

	public PhpExpressionCast(PhpExpression expression, List<String> typeNames) {
		super();
		this.target = expression;
		this.types = new ArrayList<PhpType>(typeNames.size());
		for (String typeName : typeNames) {
			types.add(PhpTypeFactory.get(typeName));
		}
	}

	public void setTarget(PhpExpression target) {
		this.target = target;
	}

	public PhpExpression getTarget() {
		return target;
	}

	public List<PhpType> getTypes() {
		return types;
	}

	@Override
	public String toString() {
		return StringUtils.join(types.stream().map(s -> "(" + s.getName() + ")").collect(Collectors.toList()), "") + target;
	}
}
