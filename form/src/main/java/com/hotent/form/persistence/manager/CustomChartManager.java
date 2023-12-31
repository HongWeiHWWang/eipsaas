package com.hotent.form.persistence.manager;

import java.io.IOException;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.form.model.CustomChart;

/**
 * 自定义图表管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface CustomChartManager extends BaseManager<CustomChart>{
	/**
	 * 通过通用查询对自定义图表进行查询
	 * @param customDialog
	 * @param filter
	 * @param dbType
	 * @return
	 * @throws IOException
	 */
	Object getListData(CustomChart customDialog, QueryFilter<?> filter, String dbType) throws IOException;
	
	/**
	 * 通过别名获取自定义图表
	 * @param alias
	 * @return
	 */
	CustomChart getChartByAlias(String alias);
	
	/**
	 * 判断指定别名的自定义图表是否存在
	 * @param alias
	 * @return
	 */
	boolean listChartByAlias(String alias);
}
