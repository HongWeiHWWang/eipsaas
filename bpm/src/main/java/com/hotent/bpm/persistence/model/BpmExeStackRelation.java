package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：堆栈关系表 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:hugh
 * 邮箱:zxh@jee-soft.cn
 * 日期:2015-06-18 17:38:35
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("bpm_exe_stack_relation")
public class BpmExeStackRelation extends BaseModel<BpmExeStackRelation>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8397898835230428716L;
	@TableId("relation_id_")
	protected String relationId; /*关系ID*/
	
	@TableField("proc_inst_id_")
	protected String procInstId; /*流程实例ID*/
	
	@TableField("from_stack_id_")
	protected String fromStackId; /*来自堆栈ID*/
	
	@TableField("to_stack_id_")
	protected String toStackId; /*到达堆栈ID*/
	
	@TableField("from_node_id_")
	protected String fromNodeId; /*来自节点*/
	
	@TableField("to_node_id_")
	protected String toNodeId; /*到达节点*/
	
	@TableField("relation_state_")
	protected Short relationState=1; /*关系状态：1正常，0回收作废*/
	
	@TableField("from_node_type_")
	protected String fromNodeType; /*来自的节点类型*/
	
	@TableField("to_node_type_")
	protected String toNodeType; /*到达的节点类型*/
	
	@TableField("created_time_")
	protected LocalDateTime createdTime=LocalDateTime.now(); /*创建时间*/
	
	@TableField(exist=false)
	protected boolean isMarked=false; /*是否遍历过，用于驳回时寻找历史时标记*/
	
	public void setIsMarked(boolean isMarked) {
		this.isMarked =isMarked;
	}
	public boolean getIsMarked() {
		return isMarked;
	}
	
	public void setId(String relationId) {
		this.relationId = relationId.toString();
	}
	public String getId() {
		return relationId.toString();
	}	
	public void setRelationId(String relationId) 
	{
		this.relationId = relationId;
	}
	/**
	 * 返回 关系ID
	 * @return
	 */
	public String getRelationId() 
	{
		return this.relationId;
	}
	public void setProcInstId(String procInstId) 
	{
		this.procInstId = procInstId;
	}
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	public String getProcInstId() 
	{
		return this.procInstId;
	}
	public void setFromStackId(String fromStackId) 
	{
		this.fromStackId = fromStackId;
	}
	/**
	 * 返回 来自堆栈ID
	 * @return
	 */
	public String getFromStackId() 
	{
		return this.fromStackId;
	}
	public void setToStackId(String toStackId) 
	{
		this.toStackId = toStackId;
	}
	/**
	 * 返回 到达堆栈ID
	 * @return
	 */
	public String getToStackId() 
	{
		return this.toStackId;
	}
	public void setFromNodeId(String fromNodeId) 
	{
		this.fromNodeId = fromNodeId;
	}
	/**
	 * 返回 来自节点
	 * @return
	 */
	public String getFromNodeId() 
	{
		return this.fromNodeId;
	}
	public void setToNodeId(String toNodeId) 
	{
		this.toNodeId = toNodeId;
	}
	/**
	 * 返回 到达节点
	 * @return
	 */
	public String getToNodeId() 
	{
		return this.toNodeId;
	}
	public void setRelationState(Short relationState) 
	{
		this.relationState = relationState;
	}
	/**
	 * 返回 关系状态：1正常，0回收作废
	 * @return
	 */
	public Short getRelationState() 
	{
		return this.relationState;
	}
	public void setFromNodeType(String fromNodeType) 
	{
		this.fromNodeType = fromNodeType;
	}
	/**
	 * 返回 来自的节点类型
	 * @return
	 */
	public String getFromNodeType() 
	{
		return this.fromNodeType;
	}
	public void setToNodeType(String toNodeType) 
	{
		this.toNodeType = toNodeType;
	}
	/**
	 * 返回 到达的节点类型
	 * @return
	 */
	public String getToNodeType() 
	{
		return this.toNodeType;
	}
	public void setCreatedTime(LocalDateTime createdTime) 
	{
		this.createdTime = createdTime;
	}
	/**
	 * 返回 创建时间
	 * @return
	 */
	public LocalDateTime getCreatedTime() 
	{
		return this.createdTime;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("relationId", this.relationId) 
		.append("procInstId", this.procInstId) 
		.append("fromStackId", this.fromStackId) 
		.append("toStackId", this.toStackId) 
		.append("fromNodeId", this.fromNodeId) 
		.append("toNodeId", this.toNodeId) 
		.append("relationState", this.relationState) 
		.append("fromNodeType", this.fromNodeType) 
		.append("toNodeType", this.toNodeType) 
		.append("createdTime", this.createdTime) 
		.toString();
	}
}