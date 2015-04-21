package com.naver.mage4j.php;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import php.PhpLexer;
import php.PhpParser;
import php.PhpParser.ProgContext;

import com.naver.mage4j.php.code.PhpProgram;
import com.naver.mage4j.php.code.PhpStatement;
import com.naver.mage4j.php.lang.MageClassLoader;
import com.naver.mage4j.php.mage.MageClass;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MagePhpParser {
	public MagePhpParser() {
	}

	public void traverse(File file) throws FileNotFoundException, IOException {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				traverse(child);
			}
			return;
		}

		if (!file.getName().endsWith(".php")) {
			return;
		}

		System.out.println("FILE: " + file.getAbsolutePath().replace("\\", "/"));
		createSyntaxTree(file);
	}

	public PhpProgram createSyntaxTree(File file) {
		ANTLRInputStream input;
		try {
			input = new ANTLRInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throw new RuntimeException("Fail to create tokens: " + file, e);
		}

		PhpLexer lexer = new PhpLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		PhpParser parser = new PhpParser(tokens);
		SyntaxErrorCounter errorCounter = new SyntaxErrorCounter();
		parser.addErrorListener(errorCounter);

		ProgContext parseTree = parser.prog();

		final List<PhpStatement> statements = new ArrayList<PhpStatement>();
		parseTree.statement().stream().forEach(s -> statements.add(s.value));

		PhpProgram phpProgram = new PhpProgram(statements, errorCounter.syntaxErrorCount, tokens, parseTree);

		return phpProgram;
	}

	static class SyntaxErrorCounter extends BaseErrorListener {
		private int syntaxErrorCount;

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
			syntaxErrorCount++;
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		MagePhpParser parser = new MagePhpParser();
		//		parser.traverse(new File("E:/repository/git/magento/app/code/core/Mage"));

		MageClass php = MageClassLoader.getContext().load("Mage_Core_Helper_Abstract");

		//		parser.createSyntaxTree(new File("E:/repository/git/magento/app/code/core/Mage/Catalog/Model/Resource/Product/Collection.php"));
		//		PhpProgram php = converter.createSyntaxTree(new File("E:/repository/git/magento/app/code/core/Mage/Core/Helper/Abstract.php"));
		//		printTokens(php.getTokens());
		//		printParseTree(php.getParseTree(), 0);
		JavaCodeGenerateContext context = new JavaCodeGenerateContext();
		php.visitJava(context);
		System.out.println(context.getCode());
	}
}
