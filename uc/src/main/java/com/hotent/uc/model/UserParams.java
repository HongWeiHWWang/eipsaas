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
* 描述：用户参数 实体对象
* 构建组：x5-bpmx-platform
* 作者:liyg
* 邮箱:liyg@jee-soft.cn
* 日期:2016-11-01 17:11:33
* 版权：广州宏天软件有限公司
* </pre>
*/
@TableName("UC_USER_PARAMS")
@ApiModel(description="用户参数")
public class UserParams extends UcBaseModel<UserParams>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2233174804869288487L;

	/**
	* ID_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="参数id")
	protected String id; 
	
	/**
	* USER_ID_
	*/
	@TableField("USER_ID_")
	@ApiModelProperty(name="userId",notes="用户id")
	protected String userId; 
	
	/**
	* ALIAS_
	*/
	@TableField("CODE_")
	@ApiModelProperty(name="alias",notes="参数别名")
	protected String alias; 
	
	/**
	* VALUE_
	*/
	@TableField("VALUE_")
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
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}
