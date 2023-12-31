package com.hotent.sys.util;

import com.hotent.base.util.AppUtil;
import com.hotent.sys.persistence.manager.SysPropertiesManager;

/**
 * 系统参数工具类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月6日
 */
public class SysPropertyUtil {

	/**
	 * 获取字符串参数值。
	 * @param alias
	 * @return
	 */
	public static String getByAlias(String alias){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getByAlias(alias);
		
	}
	
	/**
	 * 获取字符串参数值，带默认值。
	 * @param alias
	 * @param defaultValue
	 * @return
	 */
	public static String getByAlias(String alias,String defaultValue){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getByAlias(alias,defaultValue);
		
	}
	
	/**
	 * 根据参数别名获取整形参数。
	 * @param alias
	 * @return
	 */
	public static Integer getIntByAlias(String alias){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getIntByAlias(alias);
		
	}
	
	/**
	 * 根据参数别名获取整形参数，带默认值。
	 * @param alias
	 * @param defaulValue
	 * @return
	 */
	public static Integer getIntByAlias(String alias,Integer defaulValue){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getIntByAlias(alias,defaulValue);
	}
	
	/**
	 * 根据别名获取长整型参数。
	 * @param alias
	 * @return
	 */
	public static Long getLongByAlias(String alias){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getLongByAlias(alias);
		
	}
	
	/**
	 * 根据别名获取布尔型参数值。
	 * @param alias
	 * @return
	 */
	public static boolean getBooleanByAlias(String alias){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getBooleanByAlias(alias);
		
	}
	
	/**
	 * 根据别名获取布尔型参数值,带默认值。
	 * @param alias
	 * @param defaulValue
	 * @return
	 */
	public static boolean getBooleanByAlias(String alias,boolean defaulValue){
		SysPropertiesManager service=AppUtil.getBean(SysPropertiesManager.class);
		return service.getBooleanByAlias(alias,defaulValue);
	}

}