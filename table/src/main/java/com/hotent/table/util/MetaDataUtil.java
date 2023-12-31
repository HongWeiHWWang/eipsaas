package com.hotent.table.util;

import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.base.util.AppUtil;
import com.hotent.table.factory.DatabaseFactory;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.operator.IViewOperator;

/**
 * 表元数据工具类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class MetaDataUtil {

	/**
	 * 获取一个BaseTableMeta，已经设置好方言和jdbcTemplate
	 * 
	 * @param dbType
	 * @return BaseTableMeta
	 * @exception
	 * @since 1.0.0
	 */
	public static BaseTableMeta getBaseTableMetaAfterSetDT(String dbType) {
		JdbcTemplate jdbcTemplate=AppUtil.getBean(JdbcTemplate.class);
		BaseTableMeta baseTableMeta = null;
		try {
			baseTableMeta = DatabaseFactory.getTableMetaByDbType(dbType);
			/**
			 * 配置文件中的对象
			 * 
			 * @Resource JdbcTemplate jdbcTemplate;
			 */
			baseTableMeta.setJdbcTemplate(jdbcTemplate);
		} catch (Exception e) {
		}
		return baseTableMeta;
	}

	/**
	 * 获取一个IViewOperator，已经设置好方言和jdbcTemplate
	 * 
	 * @param dbType
	 * @return IViewOperator
	 * @exception
	 * @since 1.0.0
	 */
	public static IViewOperator getIViewOperatorAfterSetDT(String dbType) {
		JdbcTemplate jdbcTemplate=AppUtil.getBean(JdbcTemplate.class);
		IViewOperator iViewOperator = null;
		try {
			iViewOperator = DatabaseFactory.getViewOperator(dbType);
			/**
			 * 配置文件中的对象
			 * 
			 * @Resource JdbcTemplate jdbcTemplate;
			 */
			iViewOperator.setJdbcTemplate(jdbcTemplate);
		} catch (Exception e) {
		}

		return iViewOperator;
	}
}
