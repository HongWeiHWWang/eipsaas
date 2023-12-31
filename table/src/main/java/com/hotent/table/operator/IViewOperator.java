package com.hotent.table.operator;

import java.sql.SQLException;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.table.meta.IDbType;
import com.hotent.table.model.Table;

/**
 * 视图接口定义类
 * <pre>
 * 1.获取数据库视图列表数据；
 * 2.获取某个视图的具体信息，数据保存到Table中。
 * </pre>
 * 
 * @开发公司：广州宏天软件股份有限公司
 * @作者：heyifan
 * @邮箱：heyf@jee-soft.cn
 * @创建时间：2018年4月3日
 */
public interface IViewOperator extends IDbType{

	/**
	 * 创建或者替换视图
	 * 
	 * @param viewName 视图名
	 * @param sql
	 * @throws Exception
	 */
	public void createOrRep(String viewName, String sql) throws Exception;

	/**
	 * 使用模糊匹配，获取系统视图名称
	 * 
	 * @return
	 * @throws Exception 
	 */
	public PageList<String> getViews(String viewName) throws Exception;

	/**
	 * 使用模糊匹配，获取系统视图名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public PageList<String> getViews(String viewName, PageBean pageBean) throws SQLException, Exception;

	/**
	 * 根据视图名称，使用精确匹配，获取视图详细信息
	 * 
	 * @param viewName 视图名
	 * @return
	 */
	public Table getModelByViewName(String viewName) throws SQLException;

	/**
	 * 根据视图名，使用模糊匹配，获取视图详细信息
	 * 
	 * @param viewName
	 *            视图名称
	 * @param page
	 *            分页
	 * @return
	 */
	public PageList<Table> getViewsByName(String viewName, PageBean page) throws Exception;
}
