package com.hotent.uc.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
  
/**
 * 接口 {@code IUser} 用户
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IUser extends IdentityType, UserDetails, Serializable {
	/**
	 * 男性=Male {@value}
	 */
	public static final String SEX_MALE="Male";
	/**
	 * 女性=Female {@value}
	 */
	public static final String SEX_FAMALE="Female";
	
	/**
	 * 获取用户标识Id
	 * @return 用户标识Id
	 */
	String getUserId();

    /**
     * 设置用户标识Id
     * @param userId 用户标识Id
     */
	void setUserId(String userId);
	/**
	 * 获取用户姓名
	 * @return 用户姓名
	 */
	String getFullname();

    /**
     * 设置用户姓名
     * @param fullName 用户姓名
     */
	void setFullname(String fullName);
	/**
	 * 获取用户账号
	 * @return  用户账号
	 */
	String getAccount();

    /**
     * 设置 用户账号
     * @param account 用户账号
     */
	void setAccount(String account);
	/**
	 * 获取密码
	 * @return  密码
	 */
	String getPassword();

	/**
	 * 获取邮件
	 * @return 邮件
	 */
	String getEmail();

	/**
	 * 获取手机
	 * @return 手机
	 */
	String getMobile();

	/**
	 * 设置用户其它属性
	 * @param map 其他用户属性值
	 */
	void setAttributes(Map<String,String> map);

    /**
     * 获取是否是管理员
     * @return 是否是管理员
     */
	boolean isAdmin();

	/**
	 * 是否禁用用户
	 * @return 是否禁用用户
	 */
	boolean isEnable();

    /**
     * 获取用户其它属性
     * @return 用户其它属性
     */
	Map<String,String> getAttributes();

	/**
	 * 根据属性获取属性值
	 * @param key 属性key
	 * @return 属性值
	 */
	String getAttrbuite(String key);
	
	/**
	 * 获取用户状态
	 * @return 用户状态
	 */
	Integer getStatus();
	
	/**
	 * 获取用户头像
     * @return 用户头像
	 */
	String getPhoto();
	
	/**
	 * 获取用户微信
     * @return 用户微信
	 */
	String getWeixin();
	
	/**
	 * 获取用户是否已经同步微信企业号通讯录
     * @return 用户是否已经同步微信企业号通讯录
	 */
	Integer getHasSyncToWx();
	
	/**
	 * 获取用户租户ID
	 * @return
	 */
	String getTenantId();
	
	/**
	 * 获取密码策略时间
	 * @return
	 */
	LocalDateTime getPwdCreateTime();
	
}
