package com.hotent.table.factory;

import com.hotent.base.constants.SQLConst;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.meta.impl.DB2TableMeta;
import com.hotent.table.meta.impl.DmTableMeta;
import com.hotent.table.meta.impl.H2TableMeta;
import com.hotent.table.meta.impl.MySQLTableMeta;
import com.hotent.table.meta.impl.OracleTableMeta;
import com.hotent.table.meta.impl.SQLServer2005TableMeta;
import com.hotent.table.meta.impl.SQLServerTableMeta;
import com.hotent.table.meta.impl.PostgreSQLTableMeta;
import com.hotent.table.operator.IIndexOperator;
import com.hotent.table.operator.ITableOperator;
import com.hotent.table.operator.IViewOperator;
import com.hotent.table.operator.impl.db2.DB2IndexOperator;
import com.hotent.table.operator.impl.db2.DB2TableOperator;
import com.hotent.table.operator.impl.db2.DB2ViewOperator;
import com.hotent.table.operator.impl.dm.DmIndexOperator;
import com.hotent.table.operator.impl.dm.DmTableOperator;
import com.hotent.table.operator.impl.dm.DmViewOperator;
import com.hotent.table.operator.impl.h2.H2IndexOperator;
import com.hotent.table.operator.impl.h2.H2TableOperator;
import com.hotent.table.operator.impl.h2.H2ViewOperator;
import com.hotent.table.operator.impl.mysql.MySQLIndexOperator;
import com.hotent.table.operator.impl.mysql.MySQLTableOperator;
import com.hotent.table.operator.impl.mysql.MySQLViewOperator;
import com.hotent.table.operator.impl.oracle.OracleIndexOperator;
import com.hotent.table.operator.impl.oracle.OracleTableOperator;
import com.hotent.table.operator.impl.oracle.OracleViewOperator;
import com.hotent.table.operator.impl.postgresql.PostgreSQLIndexOperator;
import com.hotent.table.operator.impl.postgresql.PostgreSQLTableOperator;
import com.hotent.table.operator.impl.postgresql.PostgreSQLViewOperator;
import com.hotent.table.operator.impl.sqlserver.SQLServerIndexOperator;
import com.hotent.table.operator.impl.sqlserver.SQLServerTableOperator;
import com.hotent.table.operator.impl.sqlserver.SQLServerViewOperator;

/**
 * 元数据读取工厂
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class DatabaseFactory {

	public static String EXCEPTION_MSG = "没有设置合适的数据库类型";

	/**
	 * 通过数据库类型获得表操作
	 * 
	 * @param dbType
	 *            数据库类型
	 * @return
	 * @throws Exception
	 */
	public static ITableOperator getTableOperator(String dbType)
			throws Exception {
		ITableOperator tableOperator = null;
		if (dbType.equals(SQLConst.DB_ORACLE)) {
			tableOperator = new OracleTableOperator();
		} else if (dbType.equals(SQLConst.DB_MYSQL)) {
			tableOperator = new MySQLTableOperator();
		} else if (dbType.equals(SQLConst.DB_SQLSERVER) || dbType.equals(SQLConst.DB_SQLSERVER2005)) {
			tableOperator = new SQLServerTableOperator();
		} else if (dbType.equals(SQLConst.DB_DB2)) {
			tableOperator = new DB2TableOperator();
		} else if (dbType.equals(SQLConst.DB_H2)) {
			tableOperator = new H2TableOperator();
		} else if (dbType.equals(SQLConst.DB_DM)) {
			tableOperator = new DmTableOperator();
		} else if (dbType.equals(SQLConst.DB_POSTGRESQL)) {
			tableOperator = new PostgreSQLTableOperator();
		}else {
			throw new Exception(EXCEPTION_MSG);
		}
		return tableOperator;
	}

	/**
	 * 通过数据库类型获得表元操作
	 * 
	 * @param dbType
	 *            数据库类型
	 * @return
	 * @throws Exception
	 */
	public static BaseTableMeta getTableMetaByDbType(String dbType)
			throws Exception {
		BaseTableMeta meta = null;
		if (dbType.equals(SQLConst.DB_ORACLE)) {
			meta = new OracleTableMeta();
		} else if (dbType.equals(SQLConst.DB_MYSQL)) {
			meta = new MySQLTableMeta();
		}else if (dbType.equals(SQLConst.DB_SQLSERVER)) {
			meta = new SQLServerTableMeta();
		}
		else if (dbType.equals(SQLConst.DB_SQLSERVER2005)) {
			meta = new SQLServer2005TableMeta();
		}
		else if (dbType.equals(SQLConst.DB_DB2)) {
			meta = new DB2TableMeta();
		} else if (dbType.equals(SQLConst.DB_H2)) {
			meta = new H2TableMeta();
		} else if (dbType.equals(SQLConst.DB_DM)) {
			meta = new DmTableMeta();
		} else if (dbType.equals(SQLConst.DB_POSTGRESQL)) {
			meta = new PostgreSQLTableMeta();
		} else {
			throw new Exception(EXCEPTION_MSG);
		}
		return meta;
	}

	/**
	 * 根据数据类型获取 索引操作
	 * 
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	public static IIndexOperator getIndexOperator(String dbType)
			throws Exception {
		IIndexOperator indexOperator = null;
		if (dbType.equals(SQLConst.DB_ORACLE)) {
			indexOperator = new OracleIndexOperator();
		} else if (dbType.equals(SQLConst.DB_MYSQL)) {
			indexOperator = new MySQLIndexOperator();
		} else if (dbType.equals(SQLConst.DB_SQLSERVER) || dbType.equals(SQLConst.DB_SQLSERVER2005)) {
			indexOperator = new SQLServerIndexOperator();
		} else if (dbType.equals(SQLConst.DB_DB2)) {
			indexOperator = new DB2IndexOperator();
		} else if (dbType.equals(SQLConst.DB_H2)) {
			indexOperator = new H2IndexOperator();
		} else if (dbType.equals(SQLConst.DB_DM)) {
			indexOperator = new DmIndexOperator();
		} else if (dbType.equals(SQLConst.DB_POSTGRESQL)) {
			indexOperator = new PostgreSQLIndexOperator();
		}else {
			throw new Exception(EXCEPTION_MSG);
		}
		return indexOperator;

	}

	/**
	 * 根据数据源获取 视图操作
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public static IViewOperator getViewOperator(String dbType)
			throws Exception {

		IViewOperator view = null;
		if (dbType.equals(SQLConst.DB_ORACLE)) {
			view = new OracleViewOperator();
		} else if (dbType.equals(SQLConst.DB_MYSQL)) {
			view = new MySQLViewOperator();
		} else if (dbType.equals(SQLConst.DB_SQLSERVER) || dbType.equals(SQLConst.DB_SQLSERVER2005)) {
			view = new SQLServerViewOperator();
		} else if (dbType.equals(SQLConst.DB_DB2)) {
			view = new DB2ViewOperator();
		} else if (dbType.equals(SQLConst.DB_H2)) {
			view = new H2ViewOperator();
		} else if (dbType.equals(SQLConst.DB_DM)) {
			view = new DmViewOperator();
		}else if (dbType.equals(SQLConst.DB_POSTGRESQL)) {
			view = new PostgreSQLViewOperator();
		} else {
			throw new Exception(EXCEPTION_MSG);
		}

		return view;
	}
}
