package com.hotent.table.meta;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库类型接口
 * <pre>
 * 设置JdbcTemplate;
 * </pre>
 * 
 * @开发公司：广州宏天软件股份有限公司
 * @作者：heyifan
 * @邮箱：heyf@jee-soft.cn
 * @创建时间：2018年4月3日
 */
public interface IDbType {

	/**
	 * 设置spring 的JDBCTemplate
	 * 
	 * @param template
	 */
	void setJdbcTemplate(JdbcTemplate template);
}
