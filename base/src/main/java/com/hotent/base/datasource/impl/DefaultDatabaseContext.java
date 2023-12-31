package com.hotent.base.datasource.impl;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.hotent.base.constants.DataSourceConsts;
import com.hotent.base.datasource.DataSourceLoader;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.DataBaseException;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.interceptor.MasterSlaveAutoRoutingPlugin;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;

/**
 * 默认的当前数据库上下文
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月22日
 */
@Service
public class DefaultDatabaseContext implements DatabaseContext{
	@Resource
	DynamicRoutingDataSource dynamicRoutingDataSource;
	@Resource
	DataSourceLoader dataSourceLoader;
	@Resource
	JdbcTemplate jdbcTemplate;
	// 缓存不同数据源对应的dbType
	private Map<Object, DbType> map = new ConcurrentHashMap<>();
	// 缓存当前datasource
	private ThreadLocal<DataSource> currentDatasource = new ThreadLocal<>();

	/**
	 * 清空当前DataSource
	 */
	public void clear() {
		currentDatasource.remove();
	}

	/**
	 * 指定别名是否本地数据源
	 * <pre>
	 * 主从数据库中的主和从均为本地数据源，别名为{@link com.hotent.base.constants.DataSourceConsts.LOCAL_DATASOURCE}的数据源也被认为本地数据源。
	 * </pre>
	 * @param alias
	 * @return
	 */
	public static boolean isLocalDataSource(String alias) {
		return MasterSlaveAutoRoutingPlugin.MASTER.equalsIgnoreCase(alias) 
				|| MasterSlaveAutoRoutingPlugin.SLAVE.equalsIgnoreCase(alias) 
				|| DataSourceConsts.LOCAL_DATASOURCE.equalsIgnoreCase(alias);		
	}

	@Override
	public DatabaseSwitchResult setDataSource(String alias) {
		Assert.isTrue(StringUtil.isNotEmpty(alias), "要切换的数据源别名不能为空");
		DataSource datasource = getDataSourceByAlias(alias);
		// 获取当前数据源别名
		String currentDsAlias = DynamicDataSourceContextHolder.peek();
		// 设置当前数据源
		DynamicDataSourceContextHolder.push(alias);
		currentDatasource.set(datasource);
		// 切换jdbcTemplate中的当前数据源
		jdbcTemplate.setDataSource(datasource);
		if(!DefaultDatabaseContext.isLocalDataSource(alias)) {
			// 通知读写分离路由器：当前处于外部数据源中。
			MasterSlaveAutoRoutingPlugin.setInExternalDatasource();
			// 切换到非本地数据源后，系统接下来的SQL操作均不会追加租户ID（即：外部数据源的所有操作不追加租户ID），该设置在切换回数据源时重置。
			MultiTenantHandler.setThreadLocalIgnore();
		}
		// 获取切换后的数据源的数据库类型
		String dbType = this.getDbTypeByAlias(alias);
		return new DatabaseSwitchResult(currentDsAlias, alias, dbType);
	}

	@Override
	public DataSource getDataSourceByAlias(String alias) {
		Assert.isTrue(StringUtil.isNotEmpty(alias), "数据源别名不能为空");
		// 别名为本地数据源时，让动态数据源返回默认datasource
		if(DataSourceConsts.LOCAL_DATASOURCE.equalsIgnoreCase(alias)) {
			dynamicRoutingDataSource.setStrict(false);
		}
		else {
			// 启用严格模式，通过别名找不到对应数据源时报错，否则会返回默认数据源
			dynamicRoutingDataSource.setStrict(true);
		}
		DataSource dataSource = null;
		try {
			// 通过别名从当前数据源池中获取
			dataSource = dynamicRoutingDataSource.getDataSource(alias);
		}
		// 未获取到时从平台数据源管理中初始化
		catch(RuntimeException e) {
			// 通过数据源加载器获取数据源
            PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
			dataSource = getDsFromSysSource(portalFeignService.getBeanByAlias(alias));
			// 加入动态数据源
			dynamicRoutingDataSource.addDataSource(alias, dataSource);
		}
		return dataSource;
	}

    /**
     *
     * 利用Java反射机制把dataSource成javax.sql.DataSource对象
     *
     * @param sysDataSource
     * @return javax.sql.DataSource
     * @exception
     * @since 1.0.0
     */
    public DataSource getDsFromSysSource(JsonNode sysDataSource) {

        try {

            // 获取对象
            Class<?> _class = null;
            _class = Class.forName(sysDataSource.get("classPath").asText());
            javax.sql.DataSource sqldataSource = null;
            sqldataSource = (javax.sql.DataSource) _class.newInstance();// 初始化对象

            // 开始set它的属性
            String settingJson = sysDataSource.get("settingJson").asText();

            ArrayNode arrayNode = (ArrayNode) JsonUtil.toJsonNode(settingJson);


            for (int i = 0; i < arrayNode.size(); i++) {
                ObjectNode jo = (ObjectNode) arrayNode.get(i);
                Object value = BeanUtils.convertByActType(JsonUtil.getString(jo, "type"),JsonUtil.getString(jo, "value"));
                BeanUtils.setProperty(sqldataSource, JsonUtil.getString(jo, "name"), value);
            }

            // 如果有初始化方法，需要调用，必须是没参数的
            String initMethodStr = sysDataSource.get("initMethod").asText();
            if (!StringUtil.isEmpty(initMethodStr)) {
                Method method = _class.getMethod(initMethodStr);
                method.invoke(sqldataSource);
            }

            return sqldataSource;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	@Override
	public DataSource getDataSource() {
		DataSource dataSource = currentDatasource.get();
		// 默认获取Spring容器中的DataSource
		if(dataSource==null) {
			return dynamicRoutingDataSource;
		}
		return dataSource;
	}

	@Override
	public String getDbType() {
		return this.getDbTypeObj().getDb();
	}

	@Override
	public DbType getDbTypeObj() {
		DataSource dataSource = this.getDataSource();
		return this.getDbTypeObj(dataSource);
	}

	@Override
	public String getDbTypeByAlias(String alias) {
		DataSource dataSourceByAlias = this.getDataSourceByAlias(alias);
		return getDbTypeObj(dataSourceByAlias).getDb();
	}

	public DbType getDbTypeObj(DataSource dataSource) {
		DbType type = map.get(dataSource);
		if(BeanUtils.isEmpty(type)) {
			try {
				Connection con = DataSourceUtils.getConnection(dataSource);
				type = JdbcUtils.getDbType(con.getMetaData().getURL());
				map.put(dataSource, type);
			}
			catch(Exception e) {
				throw new DataBaseException("获取当前数据源异常");
			}
		}
		return type;
	}
}