package com.hotent.base.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 实体类属性和物理表字段互转工具类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月11日
 */
public class FieldConvertUtil {
	
	/**
	 * 根据实体类class获取实体字段和数据字段映射关系
	 * @param clazz
	 * @return map{实体字段名:数据库字段名}
	 */
	@SuppressWarnings({"rawtypes" })
	public static Map<String, String> getTableFieldMapping(Class clazz){
		SqlSessionTemplate sqlSessionTemplate = AppUtil.getBean(SqlSessionTemplate.class);
		Configuration configuration = sqlSessionTemplate.getConfiguration();
		Collection<String> resultMapNames = configuration.getResultMapNames();
		Map<String, String> map =new HashMap<>();
		for(String name : resultMapNames){
			if(name.indexOf(".")==-1){
				continue;
			}
			ResultMap resultMap = configuration.getResultMap(name);
			Class<?> type = resultMap.getType();
			if(type.equals(clazz)){
				for(ResultMapping resultMappirng : resultMap.getPropertyResultMappings()){
					map.put(resultMappirng.getProperty(), resultMappirng.getColumn());
				}
				break;
			}
		}
		return map;
	}
	
	/**
	 * 实体类属性转换为数据库列名
	 * @param property	实体类属性
	 * @param clazz		实体类
	 * @return			数据库列名
	 */
	@SuppressWarnings({"rawtypes" })
	public static String property2Field(String property, Class clazz){
		Map<String, String> tableFieldMapping = getTableFieldMapping(clazz);
		if (BeanUtils.isNotEmpty(tableFieldMapping) && tableFieldMapping.containsKey(property)) {
			return tableFieldMapping.get(property);
		}
		return property;
	}
	
	/**
	 * 数据库列名转换为实体类属性
	 * @param field	数据库列名
	 * @param clazz	实体类
	 * @return 		实体类属性
	 */
	@SuppressWarnings("rawtypes")
	public static String field2Property(String field, Class clazz){
		Map<String, String> tableFieldMapping = getTableFieldMapping(clazz);
		if (BeanUtils.isNotEmpty(tableFieldMapping)) {
			for (Iterator<Entry<String, String>> iterator = tableFieldMapping.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String> next = iterator.next();
				if (field.equalsIgnoreCase(next.getValue())) {
					return next.getKey();
				}
			}
		}
		return field;
	}
}
