package com.cml.netty.learn.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class DefaultHandlerRequestMapping implements HandlerRequestMapping {

	private Map<String, HandlerRequestAdapter> adapters = new HashMap<>();

	@Override
	public HandlerRequestAdapter mapping(String path) {
		Iterator<String> it = adapters.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (Pattern.compile(key).matcher(path).find()) {
				return adapters.get(key);
			}
		}
		return null;
	}

	public HandlerRequestAdapter register(String path, HandlerRequestAdapter adapter) {
		return adapters.put(path, adapter);
	}

}
