package com.hotent.bpmModel.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：常用语管理 实体对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "常用语管理 实体对象")
@TableName("bpm_approval_item")
public class BpmApprovalItem extends AutoFillModel<BpmApprovalItem> {
	private static final long serialVersionUID = -6586617649414953005L;
	// 全局
	public final static Short TYPE_GLOBAL = 1;
	// 对于流程分类
	public final static Short TYPE_FLOWTYPE = 2;
	// 对于流程
	public final static Short TYPE_FLOW = 3;
	// 对于个人的常用语
	public final static Short TYPE_USER = 4;
	
	@ApiModelProperty(name = "id", notes = "主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name = "userId", notes = "用户ID")
	@TableField("user_id_")
	protected String userId;
	
	@ApiModelProperty(name = "defKey", notes = "流程定义KEY")
	@TableField("def_key_")
	protected String defKey;
	
	@ApiModelProperty(name = "defName", notes = "流程定义Name")
	@TableField("def_name_")
	protected String defName;
	
	@ApiModelProperty(name = "typeId", notes = "流程分类ID")
	@TableField("type_id_")
	protected String typeId;
	
	@ApiModelProperty(name = "type", notes = "常用语类型")
	@TableField("type_")
	protected Short type;
	
	@ApiModelProperty(name = "expression", notes = "常用语")
	@TableField("expression_")
	protected String expression;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 ID
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 返回 用户ID
	 * 
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}

	/**
	 * 返回 流程定义KEY
	 * 
	 * @return
	 */
	public String getDefKey() {
		return this.defKey;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}

	/**
	 * 返回 流程定义KEY
	 * 
	 * @return
	 */
	public String getDefName() {
		return this.defName;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * 返回 流程分类ID
	 * 
	 * @return
	 */
	public String getTypeId() {
		return this.typeId;
	}

	public void setType(Short type) {
		this.type = type;
	}

	/**
	 * 返回 常用语类型
	 * 
	 * @return
	 */
	public Short getType() {
		return this.type;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * 返回 流程项名项
	 * 
	 * @return
	 */
	public String getExpression() {
		return this.expression;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("userId", this.userId)
				.append("defKey", this.defKey).append("typeId", this.typeId).append("type", this.type)
				.append("expression", this.expression).toString();
	}
}