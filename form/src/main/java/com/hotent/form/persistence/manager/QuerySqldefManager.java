package com.hotent.form.persistence.manager;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.QueryMetafield;
import com.hotent.form.model.QuerySqldef;

/**
 * 自定义SQL管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface QuerySqldefManager extends BaseManager<QuerySqldef>{
	/**
	 * 验证sql是否正确，为防止恶意sql修改，验证完会自动回滚
	 * @param dsName	：数据源别名
	 * @param sql 	：sql语句
	 * void
	 * @return  
	 * @exception 
	 * @since  1.0.0
	 */
	ObjectNode checkSql(String dsName, String sql);
	
	/**
	 * 通过别名获取自定义SQL
	 * @param alias
	 * @return
	 */
	QuerySqldef getByAlias(String alias);
	
	/**
	 * 导出自定义查询列表。
	 * @param idList
	 * @return
	 */
	String export(String[] idList) throws Exception;
	
	/**
	 * 导入流程定义。
	 * @param path
	 */
	void importDef(String path);
	
	/**
	 * 刷新自定义SQL的字段集合
	 * @param id
	 * @return
	 */
	List<QueryMetafield> refreshFields(String id);
}
