package com.hotent.base.interceptor;

import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.support.DbHealthIndicator;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.hotent.base.util.BeanUtils;

/**
 * 读写分离路由器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月6日
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
			 @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MasterSlaveAutoRoutingPlugin implements Interceptor{
	private final Logger log = LoggerFactory.getLogger(getClass());
	// 主库组名
	public static final String MASTER = "master";
	// 从库组名
	public static final String SLAVE = "slave";
	// 是否正处于切换外部数据源中
	public static ThreadLocal<Boolean> inExternalDatasource = new ThreadLocal<>();
	@Autowired
	private DynamicDataSourceProperties properties;
	
	public static boolean getInExternalDatasource() {
		Boolean result = inExternalDatasource.get();
    	if(BeanUtils.isNotEmpty(result) && result) {
    		return true;
    	}
    	return false;
	}

	public static void setInExternalDatasource() {
		inExternalDatasource.set(true);
	}
	
	public static void removeInExternalDatasource() {
		inExternalDatasource.remove();
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 当前处于切换到外部数据源状态时，不做读写分离
		if(MasterSlaveAutoRoutingPlugin.getInExternalDatasource()) {
			return invocation.proceed();
		}
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		try {
			String result = MASTER;
			
			if(SqlCommandType.SELECT == ms.getSqlCommandType()) {
				// 从库不可连接时切换回主库
				if(getDataSource(ms)) {
					result = SLAVE;
					log.info("操作类型为：{}，使用的数据库为：{}", ms.getSqlCommandType(), result);
				}
			}
			DynamicDataSourceContextHolder.push(result);
			return invocation.proceed();
		} finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	/**
	 * 判断从库是否正常可连接
	 * @param mappedStatement
	 * @return
	 */
	public boolean getDataSource(MappedStatement mappedStatement) {
		Map<String, DataSourceProperty> datasource = properties.getDatasource();
		if(datasource==null || !datasource.containsKey(SLAVE)) {
			return false;
		}		
		if (properties.isHealth()) {
			return DbHealthIndicator.getDbHealth(SLAVE);
		}
		return true;
	}

	@Override
	public Object plugin(Object target) {
		return target instanceof Executor ? Plugin.wrap(target, this) : target;
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
