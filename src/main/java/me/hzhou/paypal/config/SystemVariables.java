package me.hzhou.paypal.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SystemVariables {
	private static Map<String, Object> map = new HashMap<String, Object>();
	
	public static Object get(String key) {
		return map.get(key);
	}
	
	public static Object set(String key, Object value) {
		map.put(key, value);
		return value;
	}
	
	public Set<String> keySet() {
		return map.keySet();
	}
}
