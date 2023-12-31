package com.hotent.form.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.QueryMetafield;

/**
 * 自定义查询设置
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface QueryMetafieldManager extends BaseManager<QueryMetafield>{
	/**
	 * 通过sqlId查询自定义设置
	 * @param sqlId
	 * @return
	 */
	List<QueryMetafield> getBySqlId(String sqlId);
	
	/**
	 * 通过sqlID删除自定义设置
	 * @param sqlId
	 */
	void removeBySqlId(String sqlId);
}
