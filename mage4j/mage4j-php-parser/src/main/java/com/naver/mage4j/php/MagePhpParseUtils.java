package com.naver.mage4j.php;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import php.PhpParser;

public class MagePhpParseUtils {
	public static void printTokens(BufferedTokenStream tokens) {
		for (Token token : tokens.getTokens()) {
			System.out.format("[%d:%03d] ", token.getChannel(), token.getType());
			System.out.print(token.getText().replace("\n", " ~~ "));
			System.out.println();
		}
	}

	public static void printParseTree(ParseTree node, int depth) {
		for (int i = 0; i < depth; i++) {
			System.out.print("  ");
		}

		if (node instanceof RuleNode) {
			int ruleIndex = ((RuleNode)node).getRuleContext().getRuleIndex();
			String ruleName = PhpParser.ruleNames[ruleIndex];
			System.out.println(ruleName);
			for (int i = 0; i < node.getChildCount(); i++) {
				printParseTree(node.getChild(i), depth + 1);
			}
		} else {
			System.out.println("[T] " + node.getText().replace("\n", " ~~ "));
		}
	}
}
