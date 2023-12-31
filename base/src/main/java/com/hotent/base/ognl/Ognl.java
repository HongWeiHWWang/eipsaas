package com.hotent.base.ognl;
import com.hotent.base.util.BeanUtils;

/**
 * Ognl工具类
 * <p>
 * 主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写 Ognl访问静态方法的表达式
 * </p>
 * @class@method(args) 示例使用
 * <pre>
 * &lt;if test=&quot;@Ognl@isNotEmpty(userId)&quot;&gt;
 * 	and user_id = #{userId}
 * &lt;/if&gt;
 * </pre>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月4日
 */
public class Ognl {
	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * 
	 * @param o
	 *            java.lang.Object.
	 * @return boolean.
	 */
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		return BeanUtils.isEmpty(o);
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * 可以用于判断Long类型是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Long o) {
		return !isEmpty(o);
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o) {
		return BeanUtils.isNumber(o);
	}

	/**
	 * 判断是否相等
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equals(Object o1, Object o2) {
		return o1.equals(o2);
	}
	/**
	 * 判断是否相等
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean notEquals(Object o1, Object o2) {
		return !equals(o1, o2);
	}

	/**
	 * o1是否以o2开头（忽略空格和大小写）
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean startsWith(Object o1, Object o2) {
		if(o1==null || !(o1 instanceof String) || o2==null || !(o2 instanceof String)) {
			return false;
		}
		return o1.toString().trim().startsWith(o2.toString().trim()) 
			|| o1.toString().trim().startsWith(o2.toString().trim().toLowerCase())
			|| o1.toString().trim().startsWith(o2.toString().trim().toUpperCase());
	}

	/**
	 * 去掉mp构建的查询条件中默认追加的where
	 * @param o1
	 * @return
	 */
	public static Object withOutWhere(Object o1) {
		if(o1 != null && o1 instanceof String) {
			String str = o1.toString();
			if(str.length() > 6 && (str.startsWith("where") || str.startsWith("WHERE"))) {
				return str.substring(6);
			}
		}
		return o1;
	}

	/**
	 * order by 前插入filterSql
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static Object insertBeforeOrderBy(Object o1, Object o2){
		StringBuilder sql1 = new StringBuilder(isNotEmpty(o1)?o1.toString():" WHERE 1=1 ");
		String sql2 = isNotEmpty(o2)?o2.toString():"";
		int index = sql1.toString().toUpperCase().indexOf("ORDER BY");
		if (index>-1){
			if (sql1.toString().toUpperCase().startsWith("WHERE")){
				sql1.insert(index,sql2);
				return sql1.toString();
			}else{
				sql2 = " WHERE 1=1" + sql2 + sql1.toString();
				return sql2;
			}
		}else{
			return sql1 + sql2;
		}
	}
}
