package com.hotent.portal.persistence.manager;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.uc.api.model.IUser;


/**
 * 首页栏目管理层
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface  SysIndexColumnManager extends BaseManager<SysIndexColumn>{
	
	/**
	 * 解析设计模版的html
	 * 
	 * @param designHtml 设计模板
	 * @param columnList 有权限栏目列表
	 * @return
	 */
	public String parserDesignHtml(String designHtml,
			List<SysIndexColumn> columnList);
	
	/**
	 * 
	 * 是否存在别名
	 * @param alias 别名
	 * @param id	id
	 * @return
	 */
	public Boolean isExistAlias(String alias, String id);
	
	/**
	 * 通过别名获取栏目
	 * 
	 * @param alias 别名
	 * @return
	 */
	public SysIndexColumn getByColumnAlias(String alias);
	
	/**
	 * 批量通过别名获取栏目
	 * @param aliases
	 * @return
	 */
	public List<SysIndexColumn> batchGetColumnAliases(String aliases);
	/**
	 * 通过别名获取模板
	 * @param alias  别名
	 * @param params 参数
	 * @return
	 * @throws Exception 
	 */
	public String getHtmlByColumnAlias(String alias, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取类型下的列表Map
	 * 
	 * @param columnList 首页栏目集合
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<SysIndexColumn>> getColumnMap(
			List<SysIndexColumn> columnList) throws Exception;
	
	public Map<String, List<Map<String, Object>>> getColumnMap2(List<SysIndexColumn> columnList);
	/**
	 * 获取有权限的栏目
	 * 
	 * @param filter 通用查询器
	 * @param params 参数
	 * @param isParse 
	 * @param type   类型
	 * @return
	 * @throws Exception
	 */
	public List<SysIndexColumn> getHashRightColumnList(QueryFilter<SysIndexColumn> filter,
			Map<String, Object> params, Boolean isParse, short type , IUser user) throws Exception;

	/**
	 * 创建栏目并且给栏目默认授权
	 * @param sysIndexColumn
	 */
	public void createAndAuth(SysIndexColumn sysIndexColumn) throws IOException;


	/**
	 * 获取栏目类型获取所有栏目，不分页
	 * @param isPublic
	 * @return
	 */
    List<SysIndexColumn> getAllByLayoutType(QueryFilter queryFilter);

}
