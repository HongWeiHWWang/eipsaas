package com.hotent.base.security;

import java.util.HashMap;
import java.util.List;

public interface MethodAuthService {
	
	/**
	 * 返回请求资源跟角色的映射关系 
	 * @return
	 */
	public List<HashMap<String, String>> getMethodAuth();
	
}
