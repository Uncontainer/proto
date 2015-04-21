package net.me2day.async.processor.simple;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.processor.EventProcessingDistributor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("simpleDistributor")
@Scope("prototype")
public class SimpleDistributor implements EventProcessingDistributor {

	private Iterator < String > iterator;

	@Override
	public void setContext(EventProcessContext eventProcessContext) {
		iterator = Arrays.asList("1", "2").iterator();
	}

	@Override
	public List < Object > getNextTarget() {
		if (!iterator.hasNext()) {
			return null;
		}

		return Arrays.asList((Object) iterator.next());
	}
}
