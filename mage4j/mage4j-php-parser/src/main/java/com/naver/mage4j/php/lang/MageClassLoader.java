package com.naver.mage4j.php.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.naver.mage4j.php.MagePhpParser;
import com.naver.mage4j.php.code.PhpClass;
import com.naver.mage4j.php.code.PhpProgram;
import com.naver.mage4j.php.code.PhpStatement;
import com.naver.mage4j.php.mage.MageClass;
import com.naver.mage4j.php.mage.MageContextImpl;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageClassLoader {
	private static MageClassLoader INSTANCE = new MageClassLoader();

	//	private Map<String, PhpClass> classMap = new HashMap<String, PhpClass>();

	private final File MAGENTO_ROOT = new File("E:/repository/git/magento");
	private final File CORE_ROOT = new File(MAGENTO_ROOT, "app/code/core");
	private final File LIB_ROOT = new File(MAGENTO_ROOT, "lib");

	private final File JAVA_MAIN_SRC_ROOT = new File("D:/project/java/main/maje4j/mage4j/mage4j-admin/src/main/java");

	private Map<String, Object> loading = Collections.synchronizedMap(new HashMap<String, Object>());

	private MagePhpParser parser = new MagePhpParser();

	public static MageClassLoader getContext() {
		return INSTANCE;
	}

	private int depth = 0;
	private boolean loggingEnabled = true;

	private String indent() {
		String str = "";
		for (int i = 0; i < depth; i++) {
			str += "  ";
		}

		return str;
	}

	private void log(String str) {
		if (loggingEnabled) {
			System.out.println(indent() + str);
		}
	}

	public String getJavaPackage(String name) {
		String result = getFilePath(name);
		result = result.substring(0, result.lastIndexOf("/"));

		return result.replace("/", ".");
	}

	public File getJavaClassFile(String name) {
		return new File(JAVA_MAIN_SRC_ROOT, getFilePath(name));
	}

	public Class<?> getJavaClass(String name) {
		try {
			return getJavaClassUnsafe(name);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			return null;
		}
	}

	public Class<?> getJavaClassUnsafe(String name) throws ClassNotFoundException {
		String canonicalName = getFilePath(name);
		canonicalName = canonicalName.substring(0, canonicalName.lastIndexOf("."));
		canonicalName = canonicalName.replace("/", ".");

		return Class.forName(canonicalName);
	}

	private String getFilePath(String name) {
		String[] tokens = name.split("_");
		String path = "";
		for (int i = 0; i < tokens.length - 1; i++) {
			path += tokens[i].toLowerCase() + "/";
		}
		path += name + ".java";

		if (isCoreClass(name)) {
			path = "core/" + path;
		} else {
			path = "external/" + path;
		}

		return "com/naver/mage4j/" + path;
	}

	private boolean isCoreClass(String name) {
		String filename = name.replace("_", "/") + ".php";
		File classFile = new File(CORE_ROOT, filename);
		return classFile.exists();
	}

	public File getClassFile(String name) {
		String filename = name.replace("_", "/") + ".php";
		File classFile = new File(CORE_ROOT, filename);
		if (classFile.exists()) {
			return classFile;
		}

		classFile = new File(LIB_ROOT, filename);
		if (classFile.exists()) {
			return classFile;
		}

		return null;
	}

	public MageClass load(String name) {
		File classFile = getClassFile(name);
		if (classFile == null) {
			return null;
		}

		depth++;
		log("LOADING: " + name);

		if (loading.containsKey(name)) {
			throw new IllegalStateException("Class already loading.(" + name + ")");
		}

		loading.put(name, classFile);

		try {
			PhpProgram phpProgram = parser.createSyntaxTree(classFile);
			PhpClass phpClass = null;
			for (PhpStatement statement : phpProgram.getStatements()) {
				if (statement instanceof PhpClass) {
					phpClass = (PhpClass)statement;
				}
			}

			if (phpClass == null) {
				return null;
			}

			log("LOADED_: " + name);
			//			classMap.put(name, clazz);

			log("CHECKING: " + name);
			MageClass mageClass = new MageContextImpl().buildStatement(phpClass);
			log("CHECKED_: " + name);

			return mageClass;
		} finally {
			depth--;
			loading.remove(name);
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = MageClassLoader.getContext().getJavaClassFile("Mage_Core_Model_Url");
		if (file != null) {
			System.out.println(file);
			return;
		}
		//		"Mage_Core_Helper_File_Storage"
		MageClass php = MageClassLoader.getContext().load("Mage_Core_Helper_Abstract");

		JavaCodeGenerateContext context = new JavaCodeGenerateContext();
		php.visitJava(context);
		System.out.println(context.getCode());
	}
}
