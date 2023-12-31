package com.hotent.service.ws.security;

import java.util.HashMap;
import java.util.Map;

public final class AuthInfoContext {
	private AuthInfoContext() {
	}

	private static final ThreadLocal<Map<String, Object>> _cache = new ThreadLocal<Map<String, Object>>();

	public static final String AUTH_USERNAME = "auth_username";

	public static final String AUTH_PASSWORD = "auth_password";

	/**
	 * 设置属性及值
	 * 
	 * @param property
	 * @param value
	 */
	public static synchronized void setProperty(String property, Object value) {
		Map<String, Object> map = _cache.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			_cache.set(map);
		}
		map.put(property, value);
	}

	/**
	 * 获取属性值
	 * 
	 * @param property
	 * @return
	 */
	public static synchronized Object getProperty(String property) {
		Map<String, Object> map = _cache.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			_cache.set(map);
		}
		return map.get(property);
	}

	/**
	 * 清空上下文信息
	 */
	public static synchronized void clear() {
		_cache.remove();
	}

	/**
	 * 判断是否包含认证信息
	 * 
	 * @return
	 */
	public static synchronized boolean authed() {
		return _cache.get() != null && _cache.get().size() > 0;
	}
}
