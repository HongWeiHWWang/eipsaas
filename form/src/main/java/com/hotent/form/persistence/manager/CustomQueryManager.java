package com.hotent.form.persistence.manager;

import java.io.IOException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.form.model.CustomQuery;
import com.hotent.form.vo.CustomQueryControllerVo;

public interface CustomQueryManager extends BaseManager<CustomQuery> {
	
	/**
	 * 传入自定义查询参数，返回自定义查询结果。
	 * @param customQuery
	 * @param queryData
	 * @param dsType
	 * @param page
	 * @param pageSize
	 * @return customQueryResult
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public PageList getData(CustomQuery customQuery, String queryData, String dsType, int page, int pageSize) throws IOException;

	/**
	 * @param alias
	 * @return Object
	 * @exception
	 * @since 1.0.0
	 */
	public CustomQuery getByAlias(String alias);
	
	/**
	 * 通过数据源名称查询库表或视图
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public ArrayNode getTableOrViewByDsName(CustomQueryControllerVo vo) throws Exception;
}
