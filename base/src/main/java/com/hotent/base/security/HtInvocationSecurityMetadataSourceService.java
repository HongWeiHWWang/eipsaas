package com.hotent.base.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.StringUtil;

@Service
public class HtInvocationSecurityMetadataSourceService  implements FilterInvocationSecurityMetadataSource {
	// security认证对象的线程变量 Authentication
	private static ThreadLocal<HashMap<String, Collection<ConfigAttribute>>> mapThreadLocal = new ThreadLocal<HashMap<String, Collection<ConfigAttribute>>>();
	
    @Resource
    private MethodAuthService methodAuthService;
    
    /**
     * 加载权限表中所有权限
     * 
     * 同一个方法可能多个角色都具有访问权限
     * 
     */
	public void loadResourceDefine(){
    	HashMap<String, Collection<ConfigAttribute>> map = getMapThreadLocal();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        HtInvocationSecurityMetadataSourceService bean = AppUtil.getBean(getClass());
        List<HashMap<String, String>> methodAuth = bean.getMethodAuthFromCache();
        for(HashMap<String, String> mapAuth : methodAuth) {
            array = new ArrayList<ConfigAttribute>();
            String roleAlias = mapAuth.get("roleAlias");
            String key = mapAuth.get("methodRequestUrl");
            if(StringUtil.isEmpty(roleAlias) || StringUtil.isEmpty(key) ) {
            	continue;
            }
            cfg = new SecurityConfig(roleAlias);
            
            if( mapAuth.containsKey("dataPermission") && StringUtil.isNotEmpty(mapAuth.get("dataPermission")) ){
            	bean.putDataPermissionInCache(CacheKeyConst.DATA_PERMISSION+roleAlias+key, mapAuth.get("dataPermission"));
            }
           
			if(map.containsKey(key)){
               array = map.get(key);
            }
        	array.add(cfg);
            map.put(key, array);  
        }
    }
    
    @Cacheable(value = CacheKeyConst.METHOD_AUTH_CACHENAME, key = CacheKeyConst.SYS_METHOD_ROLE_AUTH, pureKey = true)
    protected List<HashMap<String, String>> getMethodAuthFromCache(){
    	return methodAuthService.getMethodAuth();
    }
    
    @CachePut(value = CacheKeyConst.DATA_PERMISSION_CACHENAME, key="#key")
    protected String putDataPermissionInCache(String key, String data) {
    	return data;
    }

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     * 返回空 ， 则不用经过 decide 方法 判断权限， 直接具有访问权限了
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 匿名访问不需要获取权限信息
        if(AuthenticationUtil.isAnonymous(authentication)) return null;
        if(object instanceof FilterInvocation){
    		FilterInvocation filterInvocation = (FilterInvocation) object;
    		String method = filterInvocation.getRequest().getMethod();
    		resUrl = filterInvocation.getRequestUrl();
    		// options 返回null   
    		if(HttpMethod.OPTIONS.matches(method)){
    			return null;
    		}
    	}
        
        loadResourceDefine();
        // 从线程中获取保证线程安全
        HashMap<String, Collection<ConfigAttribute>> map = getMapThreadLocal();
        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)) {
                return map.get(resUrl);
            }
        }
        
        return null;
    }
    
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
    
    private static HashMap<String, Collection<ConfigAttribute>> getMapThreadLocal(){
    	HashMap<String, Collection<ConfigAttribute>> hashMap = mapThreadLocal.get();
    	if(BeanUtils.isEmpty(hashMap)){
    		hashMap = new HashMap<String, Collection<ConfigAttribute>>();
    		mapThreadLocal.set(hashMap);
    	}
    	return hashMap;
    }
    
    /**
     * 清空线程变量中的权限数据
     */
    public static void clearMapThreadLocal() {
    	HashMap<String, Collection<ConfigAttribute>> hashMap = mapThreadLocal.get();
		if(hashMap!=null) {
			hashMap.clear();
		}
    }
}