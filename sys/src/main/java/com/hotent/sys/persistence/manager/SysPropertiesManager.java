package com.hotent.sys.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysProperties;

/**
 * 
 * <pre> 
 * 描述：portal_sys_properties 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-07-28 09:19:53
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysPropertiesManager extends BaseManager<SysProperties>{
	
	
	/**
	 * 分组列表。
	 * @return
	 */
	List<String> getGroups();
	
	/**
	 * 判断别名是否存在。
	 * @param sysProperties
	 * @return 
	 */
	boolean isExist(SysProperties sysProperties);
	
	/**
	 * 根据别名从数据库中获取系统属性
	 * @param alias
	 * @return
	 */
	SysProperties getByAliasFromDb(String alias);
	
	/**
	 * 根据别名获取系统属性值
	 * @param alias
	 * @return
	 */
	String getByAlias(String alias);

	String getByAlias(String alias, String defaultValue);

	Integer getIntByAlias(String alias);

	Integer getIntByAlias(String alias, Integer defaulValue);

	Long getLongByAlias(String alias);

	boolean getBooleanByAlias(String alias);

	boolean getBooleanByAlias(String alias, boolean defaulValue);
	
	
	
	
	
}
