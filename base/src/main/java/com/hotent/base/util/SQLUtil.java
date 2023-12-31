package com.hotent.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.hotent.base.datasource.DatabaseContext;

/**
 * SQl的帮助类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月16日
 */
public class SQLUtil {

	/**
	 * 根据数据库连接的URL，取得数据库类型
	 * @param url
	 * @return
	 */
	public static String getDbTypeByUrl(String url) {
		return JdbcUtils.getDbType(url).getDb();
	}

	/**
	 * 取得当前数据源的数据库类型
	 * @return
	 */
	public static String getDbType() {
		return getDbTypeObj().getDb();
	}

	public static DbType getDbTypeObj() {
		DatabaseContext databaseContext = AppUtil.getBean(DatabaseContext.class);
		return databaseContext.getDbTypeObj();
	}
	/**
	 * 是否含有sql注入
	 * @param sql sql语句
	 * @return true:含有，false:含有
	 */
	public static boolean containsSqlInjection(String sql) {
		if(StringUtil.isEmpty(sql)){
			return false;
		}
		Pattern pattern = Pattern.compile("\\b(insert|drop|grant|alter|delete|update|truncate)\\b");
		Matcher matcher = pattern.matcher(sql.toLowerCase());
		return matcher.find();
	}
}
