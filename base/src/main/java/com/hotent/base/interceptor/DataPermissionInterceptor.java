package com.hotent.base.interceptor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.base.aop.DataPermissionAspect;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;

/**
 * 数据权限过滤
 * 这里处理update 和 delete 语句  select 语句放在切面处理  可以改为拦截 Executor.update 方法
 * @author liygui
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})})
public class DataPermissionInterceptor implements Interceptor{
	
	private Logger logger = LoggerFactory.getLogger(DataPermissionInterceptor.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
	    // 通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性
	    MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
	    // 先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
	    MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
	    
	    BoundSql boundSql = statementHandler.getBoundSql();
	    // 原始的SQL语句
	    String sql = boundSql.getSql();
	    // 改造后带过滤条件的sql 
		metaObject.setValue("delegate.boundSql.sql", sql);
	    
		Map<String, Object> mapThreadLocal = AuthenticationUtil.getMapThreadLocal();
		if(BeanUtils.isEmpty(mapThreadLocal)){
			// 没有配置数据权限设置的不处理
	    	return invocation.proceed();
		}
	    
	    SqlCommandType commondType = mappedStatement.getSqlCommandType();
	    if (commondType.compareTo(SqlCommandType.SELECT) == 0) {
	    	// 查询语句在这里不处理 在切面中已经处理了
	    	return invocation.proceed();
	    }
	    
	    String createBySql="";
	    String createOrgIdSql="";
	    if(mapThreadLocal.containsKey(DataPermissionAspect.CREATE_BY_)){
	    	createBySql = " CREATE_BY_ =  " + mapThreadLocal.get(DataPermissionAspect.CREATE_BY_);
	    }
	    
	    if(mapThreadLocal.containsKey(DataPermissionAspect.CREATE_ORG_ID_)){
	    	Object object = mapThreadLocal.get(DataPermissionAspect.CREATE_ORG_ID_);
	    	Set<String> orgIds = (Set<String>) object;
	    	String inSql = getInSql(DataPermissionAspect.CREATE_ORG_ID_, orgIds);
	    	createOrgIdSql = inSql;
	    }
	    if(StringUtil.isNotEmpty(createBySql)){
	    	createOrgIdSql = createBySql + " or " + createOrgIdSql;
	    }
	    if(StringUtil.isNotEmpty(createOrgIdSql)){
	    	sql = sql + " and ( " + createOrgIdSql + ") "  ;
	    }
	   
	    logger.debug(" custom sql " + sql );
	    // 改造后带过滤条件的sql 
	    metaObject.setValue("delegate.boundSql.sql", sql);
	    
	    return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		logger.debug(" properties " + properties.toString());
	}
	
	/**
	 * 
	 * @param field
	 * @param orgIds
	 * @return  ( field in (1, 2,3 ) or field in (4,5,6) )
	 */
	private String getInSql( String field, Set<String> orgIds ){
		
		StringBuffer sb = new StringBuffer(" (");
		
		Iterator<String> iterator = orgIds.iterator();
		List<String> list = new ArrayList<String>();
		int i =1;
		while (iterator.hasNext()) {
			String next = iterator.next();
			if(i>500){
				sb.append( field + " in (" +  String.join(",", list) +")");
				list =  new ArrayList<String>();
				i=1;
			}
			list.add(next);
			i++;
		}
		
		if(BeanUtils.isNotEmpty(list)){
			sb.append( field + " in (" +  String.join(",", list) +")");
		}
		
		sb.append(")");
		return sb.toString();
	}
	
}