package com.naver.mage4j.php.code;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class PhpAccessFunction extends PhpAccess {
	private final PhpExpression name;
	private List<PhpExpression> args;

	public PhpAccessFunction(PhpExpression name, List<PhpExpression> args) {
		super();
		this.name = name;
		this.args = args;
	}

	public PhpAccessFunction(String name, List<PhpExpression> args) {
		super();
		this.name = new PhpAccessVariable(name);
		this.args = args;
	}

	public PhpExpression getName() {
		return name;
	}

	public List<PhpExpression> getArgs() {
		return args != null ? args : Collections.emptyList();
	}

	@Override
	public String toString() {
		return name + "(" + StringUtils.join(args.stream().map(s -> s.toString()).collect(Collectors.toList()), ", ") + ")";
	}
}
