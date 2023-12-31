package com.hotent.table.datasource;

import java.lang.reflect.Method;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.DataSourceConsts;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;

/**
 * 数据源工具,动态添加删除数据源
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class DataSourceUtil {

	public static final String DYNAMIC_DATASOURCE = "dataSource";

	/**
	 * 添加数据源 
	 * @param key			数据源别名
	 * @param dataSource	数据源
	 * @param replace		是否覆盖添加
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static void addDataSource(String key, DataSource dataSource,boolean replace) throws IllegalAccessException, NoSuchFieldException {
		DynamicRoutingDataSource dynamicDataSource =  (DynamicRoutingDataSource) AppUtil.getBean(DYNAMIC_DATASOURCE);
		DataSource existDataSource = dynamicDataSource.getDataSource(key);
		if(BeanUtils.isNotEmpty(existDataSource)){
			if(!replace) return;
			dynamicDataSource.removeDataSource(key);
		}
		dynamicDataSource.addDataSource(key, dataSource);
	}

	/**
	 * 数据源别名删除数据源
	 * 
	 * @param key	数据源别名
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static void removeDataSource(String key) throws IllegalAccessException, NoSuchFieldException {
		DynamicRoutingDataSource dynamicDataSource = (DynamicRoutingDataSource) AppUtil.getBean(DYNAMIC_DATASOURCE);
		dynamicDataSource.removeDataSource(key);
	}

	/**
	 * 取得数据源集合
	 * 
	 * @return	数据源Map
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static Map<String, DataSource> getDataSources() throws IllegalAccessException, NoSuchFieldException {
		DynamicRoutingDataSource dynamicDataSource = (DynamicRoutingDataSource) AppUtil.getBean(DYNAMIC_DATASOURCE);
		return dynamicDataSource.getCurrentDataSources();
	}
	
	/**
	 * 根据别名返回容器里对应的数据源
	 * @param alias	数据源别名
	 * @return		数据源
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static DataSource getDataSourceByAlias(String alias) throws IllegalAccessException, NoSuchFieldException {
		DatabaseContext databaseContext = AppUtil.getBean(DatabaseContext.class);
		return databaseContext.getDataSourceByAlias(alias);
	}
	
	/**
	 * 根据数据源别名返回容器里对应的JdbcTemplate
	 * @param alias	数据源别名
	 * @return		JdbcTemplate
	 * @throws Exception
	 */
	public static JdbcTemplate getJdbcTempByDsAlias(String alias) throws Exception {
		if(alias.equals(DataSourceConsts.LOCAL_DATASOURCE)){
			return AppUtil.getBean(JdbcTemplate.class);
		}
		return new JdbcTemplate(DataSourceUtil.getDataSourceByAlias(alias)); 
	}
	
	/**
	 * 
	 * 利用Java反射机制把dataSource成javax.sql.DataSource对象
	 * 
	 * @param JsonNode
	 * @return javax.sql.DataSource
	 * @exception
	 * @since 1.0.0
	 */
	public static DataSource getDataSource(JsonNode jsonNode) {
		try {
			// 获取对象
			Class<?> _class = null;
			_class = Class.forName(jsonNode.get("classPath").asText());
			javax.sql.DataSource sqldataSource = null;
			sqldataSource = (javax.sql.DataSource) _class.newInstance();// 初始化对象

			String settingJson = jsonNode.get("settingJson").asText();
			ArrayNode arrayNode = (ArrayNode) JsonUtil.toJsonNode(settingJson);
			for (int i = 0; i < arrayNode.size(); i++) {
				ObjectNode jo = (ObjectNode) arrayNode.get(i);
				Object value = BeanUtils.convertByActType(JsonUtil.getString(jo, "type"),JsonUtil.getString(jo, "value"));
				BeanUtils.setProperty(sqldataSource, JsonUtil.getString(jo, "name"), value);
			}
			// 如果有初始化方法，需要调用，必须是没参数的
			JsonNode jsonNode2 = jsonNode.get("initMethod");
			if (jsonNode2!=null && !(jsonNode2 instanceof NullNode)) {
				String initMethodStr = jsonNode.get("initMethod").asText();
				Method method = _class.getMethod(initMethodStr);
				method.invoke(sqldataSource);
			}
			return sqldataSource;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
