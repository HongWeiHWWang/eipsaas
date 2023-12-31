package com.hotent.base.datasource.impl;

import javax.sql.DataSource;
import org.apache.ibatis.datasource.DataSourceException;
import org.springframework.stereotype.Service;
import com.hotent.base.datasource.DataSourceLoader;

/**
 * 数据源加载器的空实现类
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月22日
 */
@Service
public class EmptyDataSourceLoader implements DataSourceLoader{

	@Override
	public DataSource loadByAlias(String alias) {
		throw new DataSourceException("当前实现类中不能加载外部数据源.");
	}
}
