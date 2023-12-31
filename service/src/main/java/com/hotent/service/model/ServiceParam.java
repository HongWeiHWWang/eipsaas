package com.hotent.service.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.service.constant.ServiceParamType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 服务调用参数
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
@ApiModel(description="服务调用参数")
@TableName("portal_service_param")
public class ServiceParam extends BaseModel<ServiceParam>{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="setId", notes="所属服务设置", required=true)
	@TableField("set_id_")
	protected String setId = "0";
	
	@ApiModelProperty(name="name", notes="参数名称", required=true)
	@TableField("name_")
	protected String name;
	
	@ApiModelProperty(name="type", notes="参数类型", required=true)
	@TableField("type_")
	protected ServiceParamType type;
	
	@ApiModelProperty(name="desc", notes="参数说明")
	@TableField("desc_")
	protected String desc;
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setSetId(String setId) 
	{
		this.setId = setId;
	}
	/**
	 * 返回 服务设置ID
	 * @return
	 */
	public String getSetId() 
	{
		return this.setId;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 参数名称
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public ServiceParamType getType() {
		return type;
	}
	public void setType(ServiceParamType type) {
		this.type = type;
	}
	public void setDesc(String desc) 
	{
		this.desc = desc;
	}
	/**
	 * 返回 参数说明
	 * @return
	 */
	public String getDesc() 
	{
		return this.desc;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("setId", this.setId) 
		.append("name", this.name) 
		.append("type", this.type) 
		.append("desc", this.desc) 
		.toString();
	}
}