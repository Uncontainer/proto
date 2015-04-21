package com.naver.mage4j.php.mage.converter;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;

public class PhpTypeUtils {
	public static boolean isUndecided(PhpType type) {
		return (type == null || type == PhpTypeFactory.ARRAY || type == PhpTypeFactory.UNDECIDED || type == PhpTypeFactory.RUNTIME);
	}

	public static boolean isDecided(PhpType type) {
		return !isUndecided(type);
	}

	public static PhpType select(PhpType type, PhpType... others) {
		if (others.length == 0) {
			return type;
		}

		List<PhpType> types = new ArrayList<PhpType>(others.length + 1);
		types.add(type);
		for (PhpType other : others) {
			types.add(other);
		}

		return select(types);
	}

	public static PhpType select(List<PhpType> types) {
		if (types == null || types.isEmpty()) {
			throw new IllegalArgumentException();
		}

		PhpType type = types.get(0);
		for (int i = 1; i < types.size(); i++) {
			PhpType other = types.get(i);
			if (other == null) {
				continue;
			}

			if (isUndecided(type) || other.isAssignableFrom(type)) {
				type = other;
			}
		}

		return type;
	}

	public static boolean equals(PhpType x, PhpType y) {
		return (x == null && y == null)
			|| (x != null && y != null && x.getName().equals(y.getName()));
	}
}
