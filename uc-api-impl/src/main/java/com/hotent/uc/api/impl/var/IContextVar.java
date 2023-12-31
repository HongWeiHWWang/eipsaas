package com.hotent.uc.api.impl.var;

/**
 * 接口 {@code IContextVar} 常用变量
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
public interface IContextVar {
	
	/**
	 * 获取命名
	 * @return 命名
	 */
	String getTitle();
	/**
	 * 获取别名
	 * @return 别名
	 */
	String getAlias();
	
	/**
	 * 获取值
	 * @return 值
	 */
	String getValue();

}
