package com.hotent.base.datasource;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * 当前数据源上下文接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月22日
 */
public interface DatabaseContext {
	/**
	 * 通过数据源别名设置当前数据源
	 * <pre>
	 * 1.请尽量使用注解@DS("alias")来切换数据源，该注解可以用<br/>在方法或者类上，其数据源切换的作用域仅限于该方法或该类；
	 * 2.如不能使用注解时可以调用本方法切换数据源，但是需要采用<br/>try(DatabaseSwitchResult result = databaseContext.setDataSource(alias)){<br/>}<br/>这样的方式调用，因为切换的数据源最终需要清除。
	 * </pre>
	 * @param alias
	 * @return
	 */
	DatabaseSwitchResult setDataSource(String alias);
	/**
	 * 通过别名获取数据源
	 * <pre>
	 * 1.首先从当前多数据源池获取别名所对应的数据源；
	 * 2.没获取到时尝试从系统数据源管理的库表中查找对应连接池，找到后初始化数据源并加入当前数据源池。
	 * </pre>
	 * @param alias
	 * @return
	 */
	DataSource getDataSourceByAlias(String alias);
	/**
	 * 获取当前线程的数据源
	 * @return
	 */
	DataSource getDataSource();
	/**
	 * 获取当前数据源的DbType
	 * @return
	 */
	DbType getDbTypeObj();
	/**
	 * 获取当前线程数据源类型
	 * @return
	 */
	String getDbType();
	/**
	 * 获取指定别名数据源的数据库类型
	 * @param alias
	 * @return
	 */
	String getDbTypeByAlias(String alias);
}
