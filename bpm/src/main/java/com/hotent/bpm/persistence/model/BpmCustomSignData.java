package com.hotent.bpm.persistence.model;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * bpm_custom_signdata
 * 
 * <pre>
 *  
 * 描述：bpm_custom_signdata 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-03 17:18:18
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("bpm_custom_signdata")
@ApiModel(value = "BpmCustomSignData", description = "bpm_custom_signdata")
public class BpmCustomSignData extends BaseModel<BpmCustomSignData> {
	// status
	/**
	 * 审批中
	 */
	public static final String STATUS_APPROVAL = "approval";
	/**
	 * 完成
	 */
	public static final String STATUS_COMPLETE = "complete";
	/**
	 * 撤回审批中
	 */
	public static final String STATUS_WITHDRAW_APPROVAL = "withdrawApproval";
	/**
	 * 撤回无待办
	 */
	public static final String STATUS_WITHDRAW = "withdraw";
	/**
	 * 被撤回
	 */
	public static final String STATUS_RETRACTED = "retracted";

	/**
	 * 再次签署中
	 */
	public static final String STATUS_HALF = "half";

	// type signType 保持和流程设置页面一样的
	// 并签
	public static final String TYPE_PARALLEL = CustomSignNodeDef.SIGNTYPE_PARALLEL;
	// 顺签
	public static final String TYPE_SEQUENTIAL = CustomSignNodeDef.SIGNTYPE_SEQUENTIAL;
	// 并批
	public static final String TYPE_PARALLEL_APPROVAL = CustomSignNodeDef.SIGNTYPE_PARALLELAPPROVE;
	

	private static final long serialVersionUID = 1L;

	@TableId("id_")
	@ApiModelProperty(value = "主键")
	protected String id;

	@TableField("type_")
	@ApiModelProperty(value = "任务类型（并签，顺签，并批）")
	protected String type;

	@TableField("status_")
	@ApiModelProperty(value = "审批中，完成，二次并签，被撤回，撤回审批中，撤回无待办")
	protected String status;
	
	@TableField("parent_id_")
	@ApiModelProperty(value = "父id")
	protected String parentId;
	
	@TableField("path_")
	@ApiModelProperty(value = "路径")
	protected String path;
	
	@TableField("inst_id_")
	@ApiModelProperty(value = "实例id")
	protected String instId;
	
	@TableField("node_id_")
	@ApiModelProperty(value = "节点id")
	protected String nodeId;
	
	@TableField("task_id_")
	@ApiModelProperty(value = "任务id")
	protected String taskId;
	@TableField("executor")
	@ApiModelProperty(value = "执行人")
	protected String executor;
	
	@TableField("is_read_")
	protected Integer isRead = 0;// 0：待阅，1：已阅
	
	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public void setExecutor(String executor) throws IOException {
		this.executor = executor;
		
	}

	/**
	 * 返回 执行人
	 * 
	 * @return
	 */
	public String getExecutor() {
		return this.executor;
	}

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

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 返回 任务类型（并签，顺签，并批）
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 返回 审批中，完成，二次并签，被撤回，撤回审批中，撤回无待办
	 * 
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 返回 父id
	 * 
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 返回 路径
	 * 
	 * @return
	 */
	public String getPath() {
		return this.path;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	/**
	 * 返回 实例id
	 * 
	 * @return
	 */
	public String getInstId() {
		return this.instId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 返回 节点id
	 * 
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
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

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("type", this.type).append("status", this.status)
				.append("parentId", this.parentId).append("path", this.path).append("instId", this.instId)
				.append("nodeId", this.nodeId).append("taskId", this.taskId).toString();
	}
}