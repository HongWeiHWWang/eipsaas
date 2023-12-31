package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
* 
* <pre> 
* 描述：用户参数 实体对象
* 构建组：x5-bpmx-platform
* 作者:liyg
* 邮箱:liyg@jee-soft.cn
* 日期:2016-11-01 17:11:33
* 版权：广州宏天软件有限公司
* </pre>
*/
public class UserParams extends UcBaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2233174804869288487L;

	/**
	* ID_
	*/
	@ApiModelProperty(name="id",notes="参数id")
	protected String id; 
	
	/**
	* USER_ID_
	*/
	@ApiModelProperty(name="userId",notes="用户id")
	protected String userId; 
	
	/**
	* ALIAS_
	*/
	@ApiModelProperty(name="alias",notes="参数别名")
	protected String alias; 
	
	/**
	* VALUE_
	*/
	@ApiModelProperty(name="value",notes="参数值")
	protected String value; 
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 USER_ID_
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 返回 ALIAS_
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 返回 VALUE_
	 * @return
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("alias", this.alias) 
		.append("value", this.value)
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}
