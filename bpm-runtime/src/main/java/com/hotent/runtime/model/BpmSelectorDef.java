package com.hotent.runtime.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * <pre>
 * 对象功能:控件组合定义  实体对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:hugh
 * 创建时间:2014-09-28 11:14:53
 * </pre>
 */
@TableName("bpm_selector_def")
@ApiModel(value = "BpmSelectorDef",description = "控件组合定义") 
public class BpmSelectorDef extends BaseModel<BpmSelectorDef> {
	
	private static final long serialVersionUID = 8449554393575047286L;
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String id;
	
	@XmlAttribute(name = "name")
	@TableField("name_")
	@ApiModelProperty(name="name",notes="名称")
	protected String name;
	
	@XmlAttribute(name = "alias")
	@TableField("alias_")
	@ApiModelProperty(name="alias",notes="别名")
	protected String alias;
	
	@XmlAttribute(name = "groupField")
	@TableField("group_field_")
	@ApiModelProperty(name="groupField",notes="组合字段")
	protected String groupField;
	
	@XmlAttribute(name = "buttons")
	@TableField("buttons_")
	@ApiModelProperty(name="buttons",notes="按钮定义")
	protected String buttons;
	
	@XmlAttribute(name = "isCustom")
	@TableField("is_custom_")
	@ApiModelProperty(name="isCustom",notes="系统预定义1=表示来自自定义查询 ")
	protected Short isCustom=0;
	
	@XmlAttribute(name = "flag")
	@TableField("flag_")
	@ApiModelProperty(name="flag",notes="标记是否系统默认")
	protected Short flag = 0;
	
	@XmlAttribute(name = "method")
	@TableField("method_")
	@ApiModelProperty(name="method",notes="选择器对应的js方法名称")
	protected String method;
	
	@XmlAttribute(name = "confKey")
	@TableField("conf_key_")
	@ApiModelProperty(name="confKey",notes="已选数据参数的传递key")
	protected String confKey;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 返回 别名
	 * 
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}

	public void setGroupField(String groupField) {
		this.groupField = groupField;
	}

	/**
	 * 返回 组合字段
	 * 
	 * @return
	 */
	public String getGroupField() {
		return this.groupField;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	/**
	 * 返回 按钮定义
	 * 
	 * @return
	 */
	public String getButtons() {
		return this.buttons;
	}

	public void setIsCustom(Short isCustom) {
		this.isCustom = isCustom;
	}

	/**
	 * 返回 系统预定义
	 * 
	 * @return
	 */
	public Short getIsCustom() {
		return this.isCustom;
	}


	public void setFlag(Short flag) {
		this.flag = flag;
	}

	/**
	 * 返回 是否系统默认
	 * 
	 * @return
	 */
	public Short getFlag() {
		return flag;
	}
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getConfKey() {
		return confKey;
	}

	public void setConfKey(String confKey) {
		this.confKey = confKey;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("name", this.name).append("alias", this.alias)
				.append("groupField", this.groupField)
				.append("buttons", this.buttons)
				.append("isCustom", this.isCustom)
				.append("flag", this.flag).toString();
	}
}