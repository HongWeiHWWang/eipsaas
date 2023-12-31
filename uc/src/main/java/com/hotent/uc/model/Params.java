package com.hotent.uc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * 
 * <pre> 
 * 描述：组织参数 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-10-31 14:29:12
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("UC_PARAMS")
@ApiModel(description="组织参数")
public class Params extends UcBaseModel<Params> {
	private static final long serialVersionUID = 1L;

	/**
	* 编号
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="参数id")
	protected String id; 
	
	/**
	* 参数名
	*/
	@TableField("NAME_")
	@ApiModelProperty(name="name",notes="参数名称")
	protected String name; 
	
	/**
	* 参数key
	*/
	@TableField("CODE_")
	@ApiModelProperty(name="code",notes="参数编码")
	protected String code; 
	
	/**
	* 参数类型
	*/
	@TableField("TYPE_")
	@ApiModelProperty(name="type",notes="参数类型 1：用户参数 2：组织参数 3：租户类型参数")
	protected String type; 
	
	/**
	* 数据来源
	*/
	@TableField("CTL_TYPE_")
	@ApiModelProperty(name="ctlType",notes="数据来源")
	protected String ctlType; 
	
	@TableField("TENANT_TYPE_ID_")
	@ApiModelProperty(name="tenantTypeId",notes="租户类型id")
	protected String tenantTypeId; 
	
	/**
	* 数据
	*/
	@TableField("JSON_")
	@ApiModelProperty(name="json",notes="数据来源json")
	protected String json; 
	
	@TableField(exist=false)
	@ApiModelProperty(name="typeName",notes="租户类型名称")
	protected String typeName="";
	

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 编号
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 参数名
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 返回 参数key
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回 参数类型
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	public void setCtlType(String ctlType) {
		this.ctlType = ctlType;
	}
	
	/**
	 * 返回 数据来源
	 * @return
	 */
	public String getCtlType() {
		return this.ctlType;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	
	/**
	 * 返回 数据
	 * @return
	 */
	public String getJson() {
		return this.json;
	}
	public String getTenantTypeId() {
		return tenantTypeId;
	}

	public void setTenantTypeId(String tenantTypeId) {
		this.tenantTypeId = tenantTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("code", this.code) 
		.append("type", this.type) 
		.append("ctlType", this.ctlType) 
		.append("json", this.json)
		.append("tenantTypeId", this.tenantTypeId)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}
