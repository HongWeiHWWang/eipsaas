package com.hotent.bo.model;

import java.time.LocalDateTime;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
  * 存储bo实例数据
  * 
  * @company 广州宏天软件股份有限公司
  * @author heyifan
  * @email heyf@jee-soft.cn
  * @date 2018年4月12日
  */
@ApiModel("存储bo实例数据")
@TableName("form_bo_int")
public class BoInst extends BaseModel<BoInst>{
	private static final long serialVersionUID = 9069698153502001622L;

	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty("对象定义ID")
	@TableField("def_id_")
	protected String defId; 
	
	@ApiModelProperty("实例数据")
	@TableField("inst_data_")
	protected String instData;
	
	@ApiModelProperty("创建时间")
	@TableField("create_time_")
	protected LocalDateTime createTime;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 业务实例ID
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setDefId(String defId) {
		this.defId = defId;
	}
	
	/**
	 * 返回 对象定义ID
	 * @return
	 */
	public String getDefId() {
		return this.defId;
	}
	
	public void setInstData(String instData) {
		this.instData = instData;
	}
	
	/**
	 * 返回 实例数据
	 * @return
	 */
	public String getInstData() {
		return this.instData;
	}
	
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("defId", this.defId) 
		.append("instData", this.instData) 
		.append("createTime", this.createTime) 
		.toString();
	}
}