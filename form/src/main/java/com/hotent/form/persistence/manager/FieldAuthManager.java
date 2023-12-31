package com.hotent.form.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.FieldAuth;

/**
 * 字段授权管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FieldAuthManager extends BaseManager<FieldAuth>{
	/**
	 * 通过实体名获取字段权限设置
	 * @param className
	 * @return
	 */
	FieldAuth getByEntName(String entName);
	/**
	 * 通过表名获取字段权限设置
	 * @param tableName
	 * @return
	 */
	FieldAuth getByTableName(String tableName);
	
	/**
	 * 通过类名获取字段权限设置
	 * @param className
	 * @return
	 */
	FieldAuth getByClassName(String className);
}
