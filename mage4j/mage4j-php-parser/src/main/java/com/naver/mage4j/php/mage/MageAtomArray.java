package com.naver.mage4j.php.mage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.naver.mage4j.php.code.PhpAtomArray;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAtomArray extends MageExpressionAbstract implements MageAtom, MageStatementInitializable<PhpAtomArray> {
	List<? extends MageStatement> entries;

	public MageAtomArray(List<? extends MageStatement> entries) {
		this.entries = entries;
		setType(PhpTypeFactory.LIST);
	}

	MageAtomArray() {
	}

	@Override
	public void init(MageContext context, PhpAtomArray statement) {
		entries = context.buildStatements(statement.getEntries());
		if (CollectionUtils.isEmpty(entries) || (entries.get(0) instanceof MagePair)) {
			setType(PhpTypeFactory.MAP);
		} else {
			setType(PhpTypeFactory.LIST);
		}
	}

	@Override
	public void setType(PhpType type) {
		if (type != PhpTypeFactory.MAP && type != PhpTypeFactory.LIST) {
			//			throw new IllegalArgumentException("Try to set not a array type.(" + type + ")");
			return;
		}

		super.setType(type);
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.addImport(Collections.class);
		if (getType() == PhpTypeFactory.LIST) {
			switch (entries.size()) {
				case 0:
					context.appendCode("Collections.emptyList()");
					break;
				case 1:
					context.appendCode("Collections.singletonList(").visit(entries.get(0)).appendCode(")");
					break;
				default:
					context.visit(entries, "Arrays.asList(", ")", ",");
					break;
			}
		} else {
			switch (entries.size()) {
				case 0:
					context.appendCode("Collections.emptyMap()");
					break;
				case 1: {
					try {
						MagePair pair = (MagePair)entries.get(0);
						context.appendCode("Collections.singletonMap(").visit(pair.getKey()).appendCode(", ").visit(pair.getValue()).appendCode(")");
					} catch (ClassCastException e) {
						throw e;
					}
					break;
				}
				default: {
					context.addImport(this.getClass());
					context.appendCode("MageAtomArray.createMap(");
					for (int i = 0; i < entries.size(); i++) {
						MagePair pair = (MagePair)entries.get(i);
						if (i > 0) {
							context.appendCode(", ");
						}
						context.appendCode("new Object[]{").visit(pair.getKey()).appendCode(", ").visit(pair.getValue()).appendCode("}");
					}
					context.appendCode(")");
					break;
				}
			}
		}
	}

	public static <K, V> Map<K, V> createMap(Object[]... pairs) {
		Map<K, V> result = new HashMap<K, V>();
		for (Object[] pair : pairs) {
			result.put((K)pair[0], (V)pair[1]);
		}

		return result;
	}

	public static <K, V> Map<K, V> merge(Map<K, V>... params) {
		Map<K, V> result = new HashMap<K, V>();
		for (Map<K, V> param : params) {
			result.putAll(param);
		}

		return result;
	}

	@Override
	public String toString() {
		return "ATOM_ARRAY: " + entries;
	}
}
