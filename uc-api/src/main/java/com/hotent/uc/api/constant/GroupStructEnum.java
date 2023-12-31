package com.hotent.uc.api.constant;

/**
 * 枚举 {@code GroupStructEnum} 用户组结构枚举
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public enum GroupStructEnum
{
    /**
     * 平铺结构
     */
	PLAIN("plain","平铺"),
    /**
     * 树形结构
     */
	TREE("tree","树形");


    /**
     * 枚举key
     */
	private String key;
    /**
     * 枚举值
     */
	private String label;
	
	GroupStructEnum(String key,String label){
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
}
