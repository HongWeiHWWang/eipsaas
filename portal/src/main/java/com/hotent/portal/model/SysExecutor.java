package com.hotent.portal.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统用户对象。
 * 
 * @company 广州宏天软件股份有限公司
 * @author ray
 * @email zhangyg@jee-soft.cn
 * @date 2014-10-30-下午5:26:502018年6月20日
 */
@ApiModel(description="系统用户对象")
public class SysExecutor{

	public static String TYPE_USER="user";
	public static String TYPE_GROUP="group";
	
	@ApiModelProperty(name="id", notes="对象ID")
	private String id="";

	@ApiModelProperty(name="name", notes="对象名称")
	private String name="";

	@ApiModelProperty(name="type", notes="对象类型")
	private String type="";
	
	@ApiModelProperty(name="groupType", notes="组类型，比如 org,role等")
	private String groupType="";
	
	public SysExecutor(){
		
	}
	
	public SysExecutor(String id_,String name,String type){
		this.id=id_;
		this.name=name;
		this.type=type;
	}
	

	
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 返回主键
	 * @return
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回对象名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置对象名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回对象类型
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 设置对象类型
	 * @param type 类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回组类型
	 * @return
	 */
	public String getGroupType() {
		return groupType;
	}
	
	/**
	 * 设置组类型
	 * @param groupType 组类型
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

}
