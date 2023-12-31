package com.hotent.base.datasource;

import javax.sql.DataSource;

/**
 * DataSource的加载器
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月22日
 */
public interface DataSourceLoader {
	/**
	 * 通过别名加载DataSource
	 * @param alias
	 * @return
	 */
	DataSource loadByAlias(String alias);
}
