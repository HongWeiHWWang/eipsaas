package com.hotent.form.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.form.model.QueryView;

/**
 * 自定义查询管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface QueryViewManager extends BaseManager<QueryView>{
	/**
	 * 通过别名获取自定义查询
	 * @param sqlAlias
	 * @return
	 */
	List<QueryView> getBySqlAlias(String sqlAlias);
	
	/**
	 * 通过别名删除自定义查询
	 * @param sqlAlias
	 */
	void removeBySqlAlias(String sqlAlias);
	
	/**
	 * 通过SQL别名和自定义查询别名查询自定义视图
	 * @param sqlAlias
	 * @param alias
	 * @return
	 */
	QueryView getBySqlAliasAndAlias(String sqlAlias, String alias);
	
	/**
	 * 通过别名查询自定义视图
	 * @param alias
	 * @return
	 */
	QueryView getByAlias(String alias);
	
	/**
	 * 判断指定别名的自定义视图是否存在
	 * @param alias
	 * @return
	 */
	boolean listByAlias(String alias);
	
	/**
	 * <pre>
	 * 获取SQL，利用sqlbuilder工具，工作重点是拼装sqlbuildermodel中
	 * 1 拼装select语句 
	 * 2 拼装 条件字段
	 * 3 拼装 排序字段
	 * </pre>
	 * @param queryView
	 * @param queryParams
	 * @return 
	 * String
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @exception 
	 * @since  1.0.0
	 */
	String getShowSql(QueryView queryView, Map<String, Object> queryParams) throws Exception;
	
	/**
	 * 获取过滤sql语句
	 * @param filterType
	 * @param filterField
	 * @param dsName
	 * @param param
	 * @return
	 * @throws IOException
	 */
	String getFilterSql(short filterType,String filterField,String dsName,Map<String, Object> param) throws IOException;
	
	/**
	 * 获取数据权限过滤语句
	 * @param dataPermission
	 * @return
	 * @throws IOException
	 */
	String getDataPermissionSql(String dataPermission) throws IOException;
	
	/**
	 * <pre>
	 * 这里做的操作是拼装显示字段，因为上个sql是查询出所有字段的
	 * 主要是为了虚拟列字段
	 * </pre>
	 * @param queryView
	 * @param list 
	 * void
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	void handleShowData(QueryView queryView, List<?> list) throws IOException;

	/**
	 * 处理模板
	 * @param queryView
	 * @throws Exception
	 */
	void handleTemplate(QueryView queryView) throws Exception;

	/**
	 * 获取显示data
	 * @param sqlAlias
	 * @param alias
	 * @param queryFilter
	 * @param getAll
	 * @param initSearch
	 * @return
	 * @throws Exception
	 */
	PageList getShowData(String sqlAlias, String alias, QueryFilter queryFilter, boolean getAll, boolean initSearch) throws Exception;
}
