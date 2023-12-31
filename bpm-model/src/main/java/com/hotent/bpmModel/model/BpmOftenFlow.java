package com.hotent.bpmModel.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用流程
 * <pre> 
 * 描述：通用流程 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-04 15:23:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@ApiModel("常用流程")
@TableName("bpm_often_flow")
public class BpmOftenFlow extends BaseModel<BpmOftenFlow>{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="通用流程ID")
	@TableId("id_")
	protected String id; 

	@ApiModelProperty(value="流程名称")
	@TableField(exist=false)
	protected String name; 

	@ApiModelProperty(value="流程定义KEY")
	@TableField("def_key_")
	protected String defKey; 

	@ApiModelProperty(value="所属人id。-1为所有人都有权限")
	@TableField("user_id_")
	protected String userId; 

	@ApiModelProperty(value="所属人名称")
	@TableField("user_name_")
	protected String userName; 

	@ApiModelProperty(value="排序号")
	@TableField("order_")
	protected Integer order; 
	
	public BpmOftenFlow(){};
	
	public BpmOftenFlow(String userId, String userName, String defKey) {
		this.userId = userId;
		this.userName = userName;
		this.defKey = defKey;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 流程名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setDefKey(String defId) {
		this.defKey = defId;
	}

	/**
	 * 返回 流程定义KEY
	 * @return
	 */
	public String getDefKey() {
		return this.defKey;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 返回 所属人id。-1为所有人都有权限
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id) 
				.append("name", this.name) 
				.append("defId", this.defKey) 
				.append("userId", this.userId) 
				.toString();
	}
}