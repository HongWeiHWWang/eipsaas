package com.hotent.base.util.string;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotent.base.util.time.DateFormatUtil;

/**
 * 字符串转换器
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月11日
 */
public class StringConverter {
	/**
	 * 字符串转换整型
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static Integer toInteger(String str) {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转换短整型
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static Short toShort(String str) {
		try {
			return Short.valueOf(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转换长整型
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static Long toLong(String str) {
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转换BigDecimal
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static BigDecimal toBigDecimal(String str) {
		try {
			return new BigDecimal(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转换为日期型
	 * 
	 * @param str
	 * @return
	 */
	public static LocalDateTime toDate(String str) {
		try {
			return DateFormatUtil.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
}
