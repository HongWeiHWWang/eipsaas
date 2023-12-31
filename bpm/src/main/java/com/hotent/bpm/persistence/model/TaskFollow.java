package com.hotent.bpm.persistence.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务跟踪表
 * 
 * <pre>
 *  
 * 描述：任务跟踪表 实体对象
 * 构建组：x7
 * 作者:maoww
 * 邮箱:maoww@jee-soft.cn
 * 日期:2018-11-13 10:04:44
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("bpm_task_follow")
@ApiModel(value = "TaskFollow", description = "任务跟踪表")
public class TaskFollow extends BaseModel<TaskFollow> {

	private static final long serialVersionUID = 1L;

	@TableId("id")
	@ApiModelProperty(value = "主键")
	protected String id;

	@TableField("task_id")
	@ApiModelProperty(value = "任务id")
	protected String taskId;

	@TableField("creator_id_")
	@ApiModelProperty(value = "创建者ID")
	protected String creatorId;

	@TableField("pro_inst_")
	@ApiModelProperty(value = "流程实例id")
	protected String proInst;

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

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 返回 任务id
	 * 
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * 返回 创建者ID
	 * 
	 * @return
	 */
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setProInst(String proInst) {
		this.proInst = proInst;
	}

	/**
	 * 返回 流程实例id
	 * 
	 * @return
	 */
	public String getProInst() {
		return this.proInst;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("taskId", this.taskId)
				.append("creatorId", this.creatorId).append("proInst", this.proInst).toString();
	}
}