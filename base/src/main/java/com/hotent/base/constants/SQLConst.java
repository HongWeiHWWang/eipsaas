package com.hotent.base.constants;

/**
 * 常用数据库变量
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月19日
 */
public interface SQLConst {
	/**
	 * 数据库类型
	 * <p>平台通过mybatis-plus获取数据库类型，所以这里的数据库类型需要与com.baomidou.mybatisplus.annotation.DbType中保持一致。</p>
	 */
	String DB_ORACLE = "oracle";
	String DB_MYSQL = "mysql";
	/**
	 * SQLServer2012
	 */
	String DB_SQLSERVER = "sqlserver";
	/**
	 * SQLServer2005或SQLServer2008
	 */
	String DB_SQLSERVER2005 = "sqlserver2005";
	String DB_DB2 = "db2";
	String DB_H2 = "h2";
	String DB_POSTGRESQL = "postgresql";
	String DB_DM = "dm";

	/*** 自定义表的主键(ID)*/
	String PK_COLUMN_NAME = "ID";
	/**
	 * 自定义表的外键(REFID)
	 */
	String FK_COLUMN_NAME = "REFID";
	/**
	 * 自定义字段的字段前缀(F_)
	 */
	String CUSTOMER_COLUMN_PREFIX = "F_";
	/**
	 * 自定义表的表前缀(W_)
	 */
	String CUSTOMER_TABLE_PREFIX = "W_";
	/**
	 * 自定义表的索引前缀(IDX_)
	 */
	String CUSTOMER_INDEX_PREFIX = "IDX_";

	/**
	 * 历史业务数据表名的后缀(_HISTORY)
	 */
	String CUSTOMER_TABLE_HIS_SUFFIX = "_HISTORY";

	/**
	 * 新添加的表通用前缀 (TT_)
	 */
	String CUSTOMER_TABLE_COMM_PREFIX = "TT_";

	/**
	 * 在主表表中默认添加用户字段。(curentUserId_)
	 */
	String CUSTOMER_COLUMN_CURRENTUSERID = "curentUserId_";
	/**
	 * 在主表和从表 表中默认添加组织字段。
	 */
	String CUSTOMER_COLUMN_CURRENTORGID = "curentOrgId_";
	/**
	 * 流程运行ID
	 */
	String CUSTOMER_COLUMN_FLOWRUNID = "flowRunId_";
	/**
	 * 流程定义ID
	 */
	String CUSTOMER_COLUMN_DEFID = "defId_";

	
	/**
	 * queryfilter mybatis参数别名
	 */
	String QUERY_FILTER = "htqf";
	
}
