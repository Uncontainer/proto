package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpClass;
import com.naver.mage4j.php.code.PhpClassField;
import com.naver.mage4j.php.code.PhpClassFunction;
import com.naver.mage4j.php.code.PhpClassMember;
import com.naver.mage4j.php.doc.PhpDocClass;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageClass implements MageStatement, MageStatementInitializable<PhpClass>, MageScope {
	private PhpClass original;

	private PhpDocClass comment;
	private List<PhpType> superTypes = new ArrayList<PhpType>();
	private Map<String, MageClassField> fieldMap = new HashMap<String, MageClassField>();
	private Map<String, MageClassMethod[]> methodMap = new HashMap<String, MageClassMethod[]>();

	private MageContext context;

	private List<PhpClassMember> rawPhpClassMembers;

	MageClass() {
	}

	@Override
	public void init(MageContext context, PhpClass phpClass) {
		this.context = context;
		this.original = phpClass;

		PhpTypeFactory.regist(new PhpTypeMageClass(this));

		List<String> documentComments = phpClass.getDocumentComments();
		if (CollectionUtils.isNotEmpty(documentComments)) {
			comment = new PhpDocClass(documentComments.get(documentComments.size() - 1));
		}

		String extendsClassName = phpClass.getExtendsClassName();
		if (extendsClassName != null) {
			superTypes.add(PhpTypeFactory.get(extendsClassName));
		}

		List<String> interfaceNames = phpClass.getImplementsInterfaceNames();
		if (CollectionUtils.isNotEmpty(interfaceNames)) {
			for (String interfaceName : interfaceNames) {
				superTypes.add(PhpTypeFactory.get(interfaceName));
			}
		}

		// 호출 순서를 알 수 없으므로 getFunction()과 같이 하나씩 소진한다.
		rawPhpClassMembers = new ArrayList<PhpClassMember>(phpClass.getMembers());
		for (PhpClassMember phpMember : phpClass.getMembers()) {
			if (rawPhpClassMembers.remove(phpMember)) {
				if (phpMember instanceof PhpClassField) {
					buildField((PhpClassField)phpMember);
				} else if (phpMember instanceof PhpClassFunction) {
					buildMethod((PhpClassFunction)phpMember);
				} else {
					throw new IllegalArgumentException("Unsupporeted Class: " + phpMember.getClass());
				}
			}
		}
		if (!rawPhpClassMembers.isEmpty()) {
			throw new IllegalStateException();
		}
		rawPhpClassMembers = Collections.emptyList();
	}

	private MageClassField buildField(PhpClassField field) {
		if (field == null) {
			return null;
		}

		return context.buildStatement(field);
	}

	private MageClassMethod buildMethod(PhpClassFunction function) {
		if (function == null) {
			return null;
		}

		return context.buildStatement(function);
	}

	void setField(PhpClassField phpFiled, MageClassField field) {
		String name = phpFiled.getName();
		if (fieldMap.containsKey(name)) {
			throw new IllegalStateException("Field has already existed.(" + name + ")");
		}

		fieldMap.put(name, field);
	}

	void setMethod(PhpClassFunction phpFunction, MageClassMethod method) {
		MageClassMethod[] methods = methodMap.get(phpFunction.getName());
		if (methods != null) {
			for (MageClassMethod each : methods) {
				if (each.getParameters().size() == method.getParameters().size()) {
					throw new IllegalStateException("Method has already existed.(" + phpFunction.getName() + ")");
				}
			}
		}

		if (methods == null) {
			methods = new MageClassMethod[] {method};
		} else {
			methods = Arrays.copyOf(methods, methods.length + 1);
			methods[methods.length - 1] = method;
			Arrays.sort(methods, new Comparator<MageClassMethod>() {
				@Override
				public int compare(MageClassMethod o1, MageClassMethod o2) {
					return o1.getParameters().size() - o2.getParameters().size();
				}
			});
		}

		methodMap.put(phpFunction.getName(), methods);
	}

	public MageClassMethod getFunction(PhpAccessFunction phpFunctionCall) {
		String name = PhpAccessUtils.getLiteralFunctionName(phpFunctionCall);
		if (name == null) {
			return null;
		}

		return getFunction(name, phpFunctionCall.getArgs().size());
	}

	public MageClassMethod getFunction(String name) {
		MageClassMethod[] methods = methodMap.get(name);
		if (methods == null) {
			return null;
		}

		if (methods.length == 1) {
			return methods[0];
		}

		return null;
	}

	public MageClassMethod getFunction(String name, int argumentCount) {
		MageClassMethod[] methods = methodMap.get(name);
		if (methods == null) {
			return null;
		}

		if (methods.length == 1) {
			return methods[0];
		}

		if (!rawPhpClassMembers.isEmpty()) {
			Iterator<PhpClassMember> iter = rawPhpClassMembers.iterator();
			while (iter.hasNext()) {
				PhpClassMember phpClassMember = iter.next();
				if (phpClassMember instanceof PhpClassFunction) {
					PhpClassFunction f = (PhpClassFunction)phpClassMember;
					if (f.getName().equals(name) && f.getParams().size() == argumentCount) {
						iter.remove();
						return buildMethod(f);
					}
				}
			}
		}

		return null;
	}

	@Override
	public String getName() {
		return original.getName();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (comment != null) {
			context.appendCode(comment.toString()).appendNewLine();
		}

		context.appendCode("public ");
		String modifier = original.getModifier();
		if (modifier != null) {
			context.appendCode(modifier).appendCode(" ");
		}
		context.appendCode("class ").appendCode(original.getName());
		String extendsClassName = original.getExtendsClassName();
		if (extendsClassName != null) {
			context.appendCode(" extends ").appendCode(extendsClassName);
		}

		List<String> implementsInterfaceNames = original.getImplementsInterfaceNames();
		if (CollectionUtils.isNotEmpty(implementsInterfaceNames)) {
			context.appendCode(" implements ").appendCode(StringUtils.join(implementsInterfaceNames, ", "));
		}
		context.appendCode(" {").appendNewLine().increaseIndent();

		// 선언 순서는 유지한다.
		for (PhpClassMember phpMember : original.getMembers()) {
			MageClassMember member;
			if (phpMember instanceof PhpClassFunction) {
				member = getFunction(phpMember.getName(), ((PhpClassFunction)phpMember).getParams().size());
			} else {
				member = fieldMap.get(phpMember.getName());
			}

			if (member == null) {
				throw new IllegalStateException("Fail to find member.(" + phpMember.getName() + ")");
			}

			context.visit(member);
			if (member instanceof MageClassField) {
				context.appendCode(";");
			}
			context.appendNewLine().appendNewLine();
		}

		context.appendNewLine().decreaseIndent().appendCode("}");
	}

	public PhpType getExtendsClass() {
		return PhpTypeFactory.get(original.getExtendsClassName());
	}

	public PhpType getType() {
		return PhpTypeFactory.get(getName());
	}
}
