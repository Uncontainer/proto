package com.naver.mage4j.php.mage.visitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.MageStatement;

public class JavaCodeGenerateContext {
	private String javaPackage;
	private StringBuilder codeBuilder = new StringBuilder(1024 * 100);
	private Stack<MageStatement> callStack = new Stack<MageStatement>();
	Set<String> imports = new HashSet<String>();
	private int indent = 0;
	private boolean fol = true; // first of line

	public JavaCodeGenerateContext() {
	}

	public JavaCodeGenerateContext(String javaPackage) {
		this.javaPackage = javaPackage;
	}

	public JavaCodeGenerateContext appendCode(String code) {
		if (fol) {
			for (int i = 0; i < indent; i++) {
				codeBuilder.append("\t");
			}
			fol = false;
		}

		codeBuilder.append(code);

		return this;
	}

	public JavaCodeGenerateContext appendMultilineCode(String code) {
		String[] lines = code.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (i > 0) {
				appendNewLine();
			}

			appendCode(lines[i]);
		}

		return this;
	}

	public JavaCodeGenerateContext appendNewLine() {
		codeBuilder.append("\n");
		fol = true;

		return this;
	}

	public JavaCodeGenerateContext increaseIndent() {
		indent++;
		return this;
	}

	public JavaCodeGenerateContext decreaseIndent() {
		indent--;
		return this;
	}

	public JavaCodeGenerateContext visit(MageStatement code) {
		if (code != null) {
			callStack.push(code);
			try {
				code.visitJava(this);
			} finally {
				callStack.pop();
			}
		}

		return this;
	}

	public JavaCodeGenerateContext visits(List<? extends MageStatement> codes) {
		for (MageStatement code : codes) {
			visit(code);
		}

		return this;
	}

	public JavaCodeGenerateContext visit(MageStatement code, String open, String close) {
		if (open != null) {
			appendCode(open);
		}

		visit(code);

		if (close != null) {
			appendCode(close);
		}

		return this;
	}

	public JavaCodeGenerateContext visit(List<? extends MageStatement> codes, String open, String close, String seperator) {
		if (open != null) {
			appendCode(open);
		}

		if (CollectionUtils.isNotEmpty(codes)) {
			visit(codes.get(0));
			for (int i = 1; i < codes.size(); i++) {
				appendCode(seperator).visit(codes.get(i));
			}
		}

		if (close != null) {
			appendCode(close);
		}

		return this;
	}

	public JavaCodeGenerateContext visit(PhpType type) {
		appendCode(type != null ? type.getName() : "ERROR");

		return this;
	}

	public String getCode() {
		StringBuilder importBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(javaPackage)) {
			importBuilder.append("package ").append(javaPackage).append(";\n\n");
		}
		for (String importString : imports) {
			importBuilder.append("import ").append(importString).append(";\n");
		}
		importBuilder.append("\n");

		return importBuilder.toString() + codeBuilder.toString();
	}

	public void addImport(String name) {
		imports.add(name);
	}

	public void addImport(Class<?> clazz) {
		if (clazz == null) {
			return;
		}

		Package _package = clazz.getPackage();
		if (_package == null) {
			return;
		}

		if ("java.lang".equals(_package.getName())) {
			return;
		}

		addImport(clazz.getName());
	}

	public void addImport(PhpType type) {
		if (type == null) {
			return;
		}

		addImport(type.getJavaClass());
	}

	public String getStackTrace(String message) {
		StringBuilder builder = new StringBuilder();
		if (message != null) {
			builder.append(message).append("\n");
		}

		for (int i = callStack.size() - 1; i >= 0; i--) {
			MageStatement code = callStack.get(i);
			builder.append(code.getClass().getSimpleName()).append(": ").append(code).append("\n");
		}

		return builder.toString();
	}
}
