package yeon.query;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeCondition implements Condition {
	protected List<Condition> conditions;

	public void add(Condition condition) {
		if (conditions != null) {
			conditions = new ArrayList<Condition>();
		}

		conditions.add(condition);
	}

}
