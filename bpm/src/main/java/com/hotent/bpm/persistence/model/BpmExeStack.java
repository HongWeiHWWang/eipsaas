package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.StringUtil;

/**
 * 对象功能:流程执行堆栈树 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyg
 * 创建时间:2014-09-10 09:12:36
 */
@TableName("bpm_exe_stack")
public class BpmExeStack extends BaseModel<BpmExeStack>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5064536029189556136L;

	/**
	 * 按照流程图执行。
	 */
	public final static String HAND_MODE_NORMAL="normal";
	
	/**
	 * 跳转回驳回节点。
	 */
	public final static String HAND_MODE_DIRECT="direct";
	
	
	/**
	 * 按照流程图执行的驳回时目标节点的路径
	 */
	public final static String HAND_MODE_NORMAL_TARGET_NODE_PATH="targetNodePath";
	
	/**
	 * 是否回收目标节点之后的所有节点的任务
	 */
	public final static String HAND_MODE_NORMAL_IS_CANCLE_NODE_PATH_TASK="isCancleNodePathTask";
	
	@TableId("id_")
	protected String  id; /*主键*/
	@TableField("prco_def_id_")
	protected String  prcoDefId; /*流程定义ID*/
	
	@TableField("node_id_")
	protected String  nodeId; /*节点ID*/
	
	@TableField("node_name_")
	protected String  nodeName; /*节点名*/
	
	@TableField("start_time_")
	protected LocalDateTime  startTime; /*开始时间*/
	
	@TableField("end_time")
	protected LocalDateTime  endTime; /*结束时间*/
	
	@TableField("is_muliti_task_")
	protected Short  isMulitiTask; /*1=是*/
	
	@TableField("parent_id_")
	protected String  parentId="0"; /*父ID*/
	
	@TableField("proc_inst_id_")
	protected String  procInstId; /*流程实例ID*/
	
	@TableField("node_path_")
	protected String  nodePath; /*节点路径*/
	
	@TableField("task_token_")
	protected String  taskToken; /*是针对分发任务时，携带的令牌，方便查找其父任务堆栈*/
	
	@TableField("node_type_")
	protected String nodeType;/*节点类型*/
 
	//驳回时返回节点。（正常审批,normal,直接返回direct.) HAND_MODE_
	@TableField("target_node_")
	protected String  targetNode="";
	
	@TableField("target_token_")
	protected String  targetToken="";
 
	
	/**
	 * 是否为干预。
	 * 干预的意思是通过在任务管理页面对任务进行操作时。
	 * 这个字段填写为干预执行。
	 * 如果干预执行，那么驳回是的人员就不从ASSIGNEE字段中获取。
	 */
	@TableField("interpose_")
	protected int interpose=0;
	
	//
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setPrcoDefId(String prcoDefId) 
	{
		this.prcoDefId = prcoDefId;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getPrcoDefId() 
	{
		return this.prcoDefId;
	}
	public void setNodeId(String nodeId) 
	{
		this.nodeId = nodeId;
	}
	/**
	 * 返回 节点ID
	 * @return
	 */
	public String getNodeId() 
	{
		return this.nodeId;
	}
	public void setNodeName(String nodeName) 
	{
		this.nodeName = nodeName;
	}
	/**
	 * 返回 节点名
	 * @return
	 */
	public String getNodeName() 
	{
		return this.nodeName;
	}
	public void setStartTime(LocalDateTime startTime) 
	{
		this.startTime = startTime;
	}
	/**
	 * 返回 开始时间
	 * @return
	 */
	public LocalDateTime getStartTime() 
	{
		return this.startTime;
	}
	public void setEndTime(LocalDateTime endTime) 
	{
		this.endTime = endTime;
	}
	/**
	 * 返回 结束时间
	 * @return
	 */
	public LocalDateTime getEndTime() 
	{
		return this.endTime;
	}
	
	public void setIsMulitiTask(Short isMulitiTask) 
	{
		this.isMulitiTask = isMulitiTask;
	}
	/**
	 * 返回 1=是
	 * @return
	 */
	public Short getIsMulitiTask() 
	{
		return this.isMulitiTask;
	}
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}
	/**
	 * 返回 父ID
	 * @return
	 */
	public String getParentId() 
	{
		if(StringUtil.isZeroEmpty(this.parentId)) return "0";
		return this.parentId;
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
	
	public void setNodePath(String nodePath) 
	{
		this.nodePath = nodePath;
	}
	/**
	 * 返回 节点路径
	 * @return
	 */
	public String getNodePath() 
	{
		return this.nodePath;
	}

	public void setTaskToken(String taskToken) 
	{
		this.taskToken = taskToken;
	}
	/**
	 * 返回 是针对分发任务时，携带的令牌，方便查找其父任务堆栈
	 * @return
	 */
	public String getTaskToken() 
	{
		return this.taskToken;
	}
	
	public void setTargetToken(String targetToken) {
		this.targetToken = targetToken;
	}
	
	public String getTargetToken() {
		return targetToken;
	}
	
	public String getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}
	
	public int getInterpose() {
		return interpose;
	}
	public void setInterpose(int interpose) {
		this.interpose = interpose;
	}
	
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("prcoDefId", this.prcoDefId) 
		.append("nodeId", this.nodeId) 
		.append("nodeName", this.nodeName) 
		.append("startTime", this.startTime) 
		.append("endTime", this.endTime) 
		.append("isMulitiTask", this.isMulitiTask) 
		.append("parentId", this.parentId) 
		.append("procInstId", this.procInstId) 
		.append("nodePath", this.nodePath) 
		.append("taskToken", this.taskToken) 
		.append("nodeType", this.nodeType) 
		.toString();
	}
}