package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhpAtomArray extends PhpExpressionAbstract implements PhpAtom {
	List<PhpStatement> entries = new ArrayList<PhpStatement>();

	public void add(PhpStatement entry) {
		entries.add(entry);
	}

	public List<PhpStatement> getEntries() {
		return entries;
	}

	public static <K, V> Map<K, V> createMap(Object[]... pairs) {
		Map<K, V> result = new HashMap<K, V>();
		for (Object[] pair : pairs) {
			result.put((K)pair[0], (V)pair[1]);
		}

		return result;
	}

	@Override
	public String toString() {
		return "ATOM_ARRAY: " + entries;
	}
}
