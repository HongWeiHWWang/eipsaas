package com.hotent.uc.api.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举 {@code GroupTypeConstant} 用户组类型
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public enum GroupTypeConstant {
    /**
     * 所有类型
     */
	All("all","所有"),
    /**
     * 组织类型
     */
	ORG("org","组织"),
    /**
     * 角色类型
     */
	ROLE("role","角色"),
    /**
     * 职位类型
     */
	JOB("job","职位"),
    /**
     * 岗位类型
     */
	POSITION("position","岗位");

    /**
     * 枚举key
     */
	private String key;
    /**
     * 枚举值
     */
	private String label;

	GroupTypeConstant(String key,String label){
		this.key = key;
		this.label = label;
	}

    /**
     * 获取key
     * @return key
     */
	public String key(){
		return key;
	}
    /**
     * 获取值
     * @return 值
     */
	public String label(){
		return label;
	}


	public static Map<String, String> getGroupTypes()
	{
		Map<String, String> map=new HashMap<String, String>();
		for (GroupTypeConstant e : GroupTypeConstant.values())
		{
			map.put(e.key(), e.label());
		}
		return map;
	}
}
