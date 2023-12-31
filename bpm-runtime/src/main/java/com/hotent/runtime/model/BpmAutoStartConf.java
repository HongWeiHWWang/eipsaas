package com.hotent.runtime.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 流程自动发起配置表
 * <pre> 
 * 描述：流程自动发起配置表 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-07 10:51:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("bpm_auto_start_conf")
@ApiModel(value = "BpmAutoStartConf",description = "流程自动发起配置表") 
public class BpmAutoStartConf extends BaseModel<BpmAutoStartConf>{
	
	public static final String START_OPINION = "配置自动发起";

	private static final long serialVersionUID = 1L;
	
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@TableField("def_key_")
	@ApiModelProperty(value="自动发起的流程定义key")
	protected String defKey; 
	
	@TableField("start_user_")
	@ApiModelProperty(value="自动发起人配置")
	protected String startUser; 
	
	@TableField("form_data_")
	@ApiModelProperty(value="表单数据配置")
	protected String formData; 
	
	@TableField("trigger_")
	@ApiModelProperty(value="自动发起时机")
	protected String trigger; 
	
	@TableField("tenant_id_")
	@ApiModelProperty(name="tenantId",notes="租户id")
	protected String tenantId;
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}
	
	/**
	 * 返回 自动发起的流程定义key
	 * @return
	 */
	public String getDefKey() {
		return this.defKey;
	}
	
	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}
	
	/**
	 * 返回 自动发起人配置
	 * @return
	 */
	public String getStartUser() {
		return this.startUser;
	}
	
	public void setFormData(String formData) {
		this.formData = formData;
	}
	
	/**
	 * 返回 表单数据配置
	 * @return
	 */
	public String getFormData() {
		return this.formData;
	}
	
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	
	/**
	 * 返回 自动发起时机
	 * @return
	 */
	public String getTrigger() {
		return this.trigger;
	}
	
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("defKey", this.defKey) 
		.append("startUser", this.startUser) 
		.append("formData", this.formData) 
		.append("trigger", this.trigger) 
		.toString();
	}
}