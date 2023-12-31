package com.hotent.sys.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 日志配置
 * <pre> 
 * 描述：日志配置 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 16:19:34
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @ApiModel(description = "日志配置") 
 @TableName("portal_sys_logs_settings")
public class SysLogsSettings extends BaseModel<SysLogsSettings>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="编码")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty(value="模块")
	@TableField("module_type_")
	protected String moduleType; 
	
	@ApiModelProperty(value="状态")
	@TableField("status_")
	protected String status; 
	
	@ApiModelProperty(value="保留天数")
	@TableField("save_days_")
	protected Integer saveDays; 
	
	@ApiModelProperty(value="描述")
	@TableField("remark_")
	protected String remark; 
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 编码
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	
	/**
	 * 返回 模块
	 * @return
	 */
	public String getModuleType() {
		return this.moduleType;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 返回 状态
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}
	
	public void setSaveDays(Integer saveDays) {
		this.saveDays = saveDays;
	}
	
	/**
	 * 返回 保留天数
	 * @return
	 */
	public Integer getSaveDays() {
		return this.saveDays;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 返回 描述
	 * @return
	 */
	public String getRemark() {
		return this.remark;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("moduleType", this.moduleType) 
		.append("status", this.status) 
		.append("saveDays", this.saveDays) 
		.append("remark", this.remark) 
		.toString();
	}
}