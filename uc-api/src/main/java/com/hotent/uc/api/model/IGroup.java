package com.hotent.uc.api.model;

import java.util.Map;

import com.hotent.uc.api.constant.GroupStructEnum;

/**
 * 接口 {@code IGroup} 用户组
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IGroup extends IdentityType{
	/**
	 * 获取用户组ID
	 * @return 用户组ID
	 */
	String getGroupId();
	
	/**
	 * 获取用户组名称
	 * @return 用户组名称
	 */
	String getName();
	
	/**
	 * 获取用户组编码
	 * @return 用户组编码
	 */
	String getGroupCode();
	
	/**
	 * 获取组织排序
	 * @return 组织排序
	 */
	Long getOrderNo();
	
	/**
	 * 获取用户组类型
	 * 比如：org,role,pos
	 * @return 用户组类型
	 */
	String getGroupType();
	
	/**
	 * 获取用户组结构
	 * @return 用户组结构
	 */
	GroupStructEnum getStruct();
	
	/**
	 * 获取组织上级ID
	 * @return 组织上级ID
	 */
	String getParentId();
	
    /**
     * 获取组织路径 例如xxx.xxxx
     * @return 组织路径
     */
	String getPath();
	/**
	 * 获取用户组参数
	 * @return 用户组参数
	 */
	Map<String, Object> getParams();
}
