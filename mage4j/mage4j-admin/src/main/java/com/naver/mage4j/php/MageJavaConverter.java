package com.naver.mage4j.php;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.php.code.PhpProgram;
import com.naver.mage4j.php.lang.MageClassLoader;
import com.naver.mage4j.php.mage.MageClass;
import com.naver.mage4j.php.mage.converter.access.AccessConverterClass;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageJavaConverter {
	public MageJavaConverter() {
		AccessConverterClass.registTypeConverter("Mage", new MageTypeConverter());
		AccessConverterClass.registTypeConverter(Mage_Core_Model_App.class.getTypeName(), new AppTypeConverter());

		new FunctionConverter();
		new FunctionArrayConverter();
		new FunctionRegexConverter();
		new FunctionStringConverter();
	}

	public void convert(String magePhpClassName, boolean writeToFile) {
		MageClass php = MageClassLoader.getContext().load(magePhpClassName);

		JavaCodeGenerateContext context = new JavaCodeGenerateContext(MageClassLoader.getContext().getJavaPackage(magePhpClassName));
		try {
			php.visitJava(context);
		} catch (Exception e) {
			throw new RuntimeException(context.getStackTrace(e.getMessage()), e);
		}

		String code = context.getCode();
		System.out.println(code);

		if (writeToFile) {
			File file = MageClassLoader.getContext().getJavaClassFile(magePhpClassName);
			if (file.exists()) {
				throw new IllegalStateException();
			}

			file.getParentFile().mkdirs();

			Writer out;
			try {
				out = new OutputStreamWriter(new FileOutputStream(file), "UTF8");
				IOUtils.write(code, out);
				out.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static void printTree(String magePhpClassName) {
		File classFile = MageClassLoader.getContext().getClassFile(magePhpClassName);

		MagePhpParser parser = new MagePhpParser();
		PhpProgram php = parser.createSyntaxTree(classFile);

		MagePhpParseUtils.printParseTree(php.getParseTree(), 0);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		MageJavaConverter converter = new MageJavaConverter();

		converter.convert("Mage_Adminhtml_Controller_Action", true);

		//		printTree("Zend_Http_Client");

		//		converter.printTree("Mage_Core_Model_Locale");

		//		"Mage_Core_Helper_Abstract"
	}
}
