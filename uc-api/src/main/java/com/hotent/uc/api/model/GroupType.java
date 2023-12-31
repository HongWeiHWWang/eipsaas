package com.hotent.uc.api.model;

/**
 * 用户组类型
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class GroupType {
	/**
	 * 别名
	 */
	private String alias="";
	/**
	 * 名称
	 */
	private String name="";
	
	public GroupType(){
	}

	public GroupType(String alias, String name) {
		this.alias = alias;
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	

}
