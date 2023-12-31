package com.hotent.table.meta.impl;

import com.hotent.table.meta.ITableMeta;


/**
 * 数据表元数据抽象类
 * <pre>
 *  用于读取数据库表的元数据信息:
 *  1.查询数据库中的表列表
 *  2.取得表的详细数据
 * </pre>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public abstract class BaseTableMeta  extends BaseDbType implements ITableMeta {
	public abstract String getAllTableSql();
}