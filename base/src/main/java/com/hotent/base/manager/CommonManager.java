package com.hotent.base.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.exception.SystemException;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;

/**
 * 通用管理器
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public interface CommonManager{
	/**
	 * 执行sql语句
	 * @param sql	sql语句
	 */
	int execute(String sql);
	/**
	 * 执行sql语句
	 * <p>
	 * update .. set key=?
	 * </p>
	 * @param sql	sql语句
	 * @param objs	参数化中参数的值
	 */
	int execute(String sql, Object...objs);
	/**
	 * 执行sql语句
	 * <p>
	 * update .. set key=#{key}
	 * </p>
	 * @param sql	sql语句
	 * @param map	以Map构成的参数值（注意，map中不能定义key为sql的参数）
	 */
	int execute(String sql, Map<String,Object> map);
	
	/**
	 * 查询列表数据
	 * @param sql	sql语句
	 * @return		列表数据
	 */
	List<Map<String,Object>> query(String sql);
	
	/**
	 * 查询列表数据
	 * <p>select * from ... where key=?</p>
	 * @param sql	sql语句
	 * @param objs	参数化中参数的值
	 * @return
	 */
	List<Map<String,Object>> query(String sql, Object...objs);
	
	/**
	 * 查询列表数据
	 * <p>select * from ... where key=#{key}</p>
	 * @param sql	sql语句
	 * @param map	查询参数（可以结合sql语句参数化实现查询）
	 * @return
	 */
	List<Map<String,Object>> query(String sql, Map<String, Object> map);
	
	/**
	 * 分页查询数据
	 * @param sql		sql语句
	 * @param pageBean	分页对象
	 * @return			分页结果
	 */
	PageList<Map<String,Object>> query(String sql, PageBean pageBean);
	
	/**
	 * 通用查询条件进行查询
	 * @param sql			sql语句
	 * @param queryFilter	通用查询条件
	 * @return				查询结果列表
	 * @throws SystemException
	 */
	PageList<Map<String,Object>> query(String sql, QueryFilter<?> queryFilter) throws SystemException;

	PageList<Map<String,Object>> queryByCustomSql(String sql, QueryFilter<?> queryFilter) throws SystemException;
}
