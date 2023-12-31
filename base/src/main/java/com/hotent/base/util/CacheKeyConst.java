package com.hotent.base.util;

import com.hotent.base.cache.setting.CacheSetting;

/**
 * 缓存key 管理
 * @author liyg
 *
 */
public interface CacheKeyConst {
	/**
	 * 模块名+name+userid
	 * 用户菜单缓存的可以
	 * 在对菜单时需要清空用户菜单缓存
	 */
	String USER_MENU_CACHENAME = "userMenu";
	
	/**
	 * 后台方法和角色的关系
	 * 在对资源授权时操作时需要清空方法和角色的缓存
	 */
	String SYS_METHOD_ROLE_AUTH = "SYS_METHOD_ROLE_AUTH";
	String METHOD_AUTH_CACHENAME = "methodAuth";
	
	/**
	 * 系统日志配置的缓存key 是否开始保存日志
	 * SYS_LOGS_SETTING_STATUS + 微服务名称
	 */
	String SYS_LOGS_SETTING_STATUS = "SYS_LOGS_SETTING_STATUS";
	String LOGS_SETTING_STATUS_CACHENAME = "logsSetting:status";
	
	/**
	 * 系统日志配置的缓存key  保留天数
	 * SYS_LOGS_SETTING_SAVE_DAY + 微服务名称
	 */
	
	String SYS_LOGS_SETTING_SAVE_DAY = "SYS_LOGS_SETTING_SAVE_DAY";
	String SYS_LOGS_SETTING_DAY_CACHENAME = "logsSetting:days";
	
	/**
	 * 数据权限设置前缀
	 * 
	 * DATA_PERMISSION + roleAlias + requestUri
	 */
	String DATA_PERMISSION = "DATA_PERMISSION";
	String DATA_PERMISSION_CACHENAME = "dataPermission";
	
	String USER_NAME_CACHENAME = "user:userName";
	String USER_ID_CACHENAME = "user:userId";
	
	/**
	 * 国际化资源语种
	 */
	String I18N_MESSAGE_TYPE = "I18N_MESSAGE_TYPE";
	String I18N_MESSAGE_TYPE_CACHENAME = "i18n:messageType";
	
	/**
	 * 国际化资源
	 */
	String I18N_RESOURCES_CACHENAME = "i18n:resources";
	CacheSetting I18N_RESOURCES_CACHESETTING = CacheSetting.buildDefault("国际化资源");
}
