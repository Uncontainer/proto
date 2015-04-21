package com.naver.mage4j.php.code;

import java.util.List;

import org.antlr.v4.runtime.BufferedTokenStream;

import php.PhpParser.ProgContext;

public class PhpProgram implements PhpStatement {
	private List<PhpStatement> phpStatements;
	private int syntaxErrorCount;
	private BufferedTokenStream tokens;
	private ProgContext parseTree;

	public PhpProgram(List<PhpStatement> statements, int syntaxErrorCount, BufferedTokenStream tokens, ProgContext parseTree) {
		super();
		this.phpStatements = statements;
		this.syntaxErrorCount = syntaxErrorCount;
		this.tokens = tokens;
		this.parseTree = parseTree;
	}

	public List<PhpStatement> getStatements() {
		return phpStatements;
	}

	public int getSyntaxErrorCount() {
		return syntaxErrorCount;
	}

	public BufferedTokenStream getTokens() {
		return tokens;
	}

	public ProgContext getParseTree() {
		return parseTree;
	}

	/**
	 * 불필요한 메모리가 정리될 수 있도록 부가적인 데이터들을 초기화한다.
	 */
	public void clear() {
		parseTree = null;
		tokens = null;
	}
}
