package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;


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
public class Params extends UcBaseModel {
	private static final long serialVersionUID = 1L;

	/**
	* 编号
	*/
	@ApiModelProperty(name="id",notes="参数id")
	protected String id; 
	
	/**
	* 参数名
	*/
	@ApiModelProperty(name="name",notes="参数名称")
	protected String name; 
	
	/**
	* 参数key
	*/
	@ApiModelProperty(name="code",notes="参数编码")
	protected String code; 
	
	/**
	* 参数类型
	*/
	@ApiModelProperty(name="type",notes="参数类型 1：用户参数 2：组织参数")
	protected String type; 
	
	/**
	* 数据来源
	*/
	@ApiModelProperty(name="ctlType",notes="数据来源")
	protected String ctlType; 
	
	/**
	* 数据
	*/
	@ApiModelProperty(name="json",notes="数据来源json")
	protected String json; 
	

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
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}
