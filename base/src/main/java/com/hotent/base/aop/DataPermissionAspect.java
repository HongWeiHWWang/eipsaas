package com.hotent.base.aop;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

/**
 * 系统日志切面
 *
 * @company 广州宏天软件股份有限公司
 * @author liyg
 * @email liygui@jee-soft.cn
 * @date 2018年8月31日
 */
@Aspect
@Component
public class DataPermissionAspect {
	private Logger logger = LoggerFactory.getLogger(DataPermissionAspect.class);

	private static final String LOGIN_USER = "loginUser";
	private static final String LOGIN_USER_ORGS = "loginUserOrgs";
	private static final String LOGIN_USER_SUB_ORGS = "loginUserSubOrgs";
	private static final String CUSTOM_ORGS = "customOrgs";
	public static final String CREATE_BY_ = "CREATE_BY_";
	public static final String CREATE_ORG_ID_ = "CREATE_ORG_ID_";
	@Resource
	AopCacheHelper aopCacheHelper;

	@SuppressWarnings("unchecked")
	@Around("execution(* *..*Controller.*(..)) && @annotation(com.hotent.base.annotation.DataPermission)")
	public Object dataPermission(ProceedingJoinPoint joinPoint) throws Throwable{
		Object[] params=joinPoint.getArgs();

		// 当前切中的方法
		HttpServletRequest request = HttpUtil.getRequest();
		if(request==null) {
			return joinPoint.proceed();
		}
		String reqUri = request.getRequestURI();
		logger.debug(" 请求地址   " + reqUri);
		//  获取数据权限配置 从缓存中获取
		Set<String> currentUserRolesAlias = AuthenticationUtil.getCurrentUserRolesAlias();
		Map<String,Object> map = new HashMap<String, Object>();
		for (String alias : currentUserRolesAlias) {
			getDataPermission(CacheKeyConst.DATA_PERMISSION + alias + reqUri, map);
		}
		// 处理select sql 的情况
		for (Object object : params) {
			if( object instanceof QueryFilter ){
				QueryFilter filter = (QueryFilter) object;
				if(map.containsKey(CREATE_BY_)){
					filter.addFilter(CREATE_BY_, map.get(CREATE_BY_).toString(), QueryOP.EQUAL,FieldRelation.OR,"dataPermission");
				}

				if( map.containsKey(CREATE_ORG_ID_) && BeanUtils.isNotEmpty(map.get(CREATE_ORG_ID_))){
					filter.addFilter(CREATE_ORG_ID_, String.join(",",(Set<String>)map.get(CREATE_ORG_ID_)), QueryOP.IN,FieldRelation.OR,"dataPermission");
				}

			}
		}
		// 将map 放到线程中 给 DataFilterInterceptor 拦截器处理 update 和delete sql 
		AuthenticationUtil.setMapThreadLocal(map);

		// 执行方法后
		AuthenticationUtil.removeMapThreadLocal();
		return joinPoint.proceed();
	}

	private void getDataPermission(String key, Map<String,Object> resultMap){
		String dataPermission = aopCacheHelper.getDataPermissionFromCache(key);
		try {
			if(StringUtil.isEmpty(dataPermission)){
				return;
			}
			ArrayNode createArrayNode =  (ArrayNode) JsonUtil.toJsonNode(dataPermission);
			Set<String> orgIds = new HashSet<String>(); 
			for (JsonNode jsonNode : createArrayNode) {
				String type = jsonNode.get("type").asText();

				if(LOGIN_USER.equals(type) && !resultMap.containsKey(CREATE_BY_) ){
					resultMap.put(CREATE_BY_, AuthenticationUtil.getCurrentUserId());
				}
				if(LOGIN_USER_ORGS.equals(type) && AuthenticationUtil.getCurrentUserOrgIds() != null  ){
					orgIds.add(AuthenticationUtil.getCurrentUserOrgIds());
				}
				if(LOGIN_USER_SUB_ORGS.equals(type) && AuthenticationUtil.getCurrentUserSubOrgIds() != null ){
					orgIds.add(AuthenticationUtil.getCurrentUserSubOrgIds());
				}
				if(CUSTOM_ORGS.equals(type)){
					ArrayNode tmpArray = (ArrayNode)jsonNode.get("orgs");
					for (JsonNode tmpJsonNode : tmpArray) {
						orgIds.add(tmpJsonNode.get("id").asText());
					}
				}
			}
			resultMap.put(CREATE_ORG_ID_, orgIds);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BaseException(" 转换数据权限设置失败，未能正确获取数据 ");
		}
	}
}
