package com.hotent.uc.api.model;

import java.io.Serializable;

/**
 * 接口 {@code IdentityType} 身份类型
 * <pre>
 * 总计抽象为两类：用户，用户组
 * </pre>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IdentityType extends Serializable{
	
	/**
	 * 用户 {@value}
	 */
	public static final String USER="user";
	
	/**
	 * 用户组 {@value}
	 */
	public static final String GROUP="group";
	

	/**
	 * 返回用户标识类型
	 * @return 用户标识类型
	 */
	String getIdentityType();
}
