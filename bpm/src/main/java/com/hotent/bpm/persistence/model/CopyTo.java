package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.bpm.api.model.process.inst.BpmProCpto;

/**
 * 对象功能:流程实例抄送 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-05-03 11:46:20
 */
@TableName("bpm_pro_cpto")
public class CopyTo extends BaseModel<CopyTo> implements BpmProCpto{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7257951764520663570L;
	
	@TableId("id_")
	protected String  id; /*主键*/
	
	@TableField("inst_id_")
	protected String  instId; /*流程实例ID*/
	
	@TableField("bpmn_inst_id_")
	protected String  bpmnInstId; /*ACT实例ID*/
	
	@TableField("node_id_")
	protected String  nodeId; /*节点ID*/
	
	@TableField("create_time_")
	protected LocalDateTime  createTime; /*抄送时间*/
	
	@TableField("opinion_")
	protected String  opinion; /*意见*/
	
	@TableField("subject_")
	protected String  subject; /*流程实例标题*/
	
	@TableField("type_")
	protected String  type; /*抄送类型(copyto抄送,trans转发)*/
	
	@TableField("startor_id_")
	protected String  startorId; /*流程发起人*/
	
	@TableField("startor_")
	protected String  startor; /*流程发起人*/
	
	@TableField("type_id_")
	protected String  typeId; /*流程分类*/
	
	//转发接收人
	@TableField("recever")
	protected String recever;
	
	/*是否已读*/
	@TableField(exist=false)
	protected Integer  isRead=0; 
	
	@TableField(exist=false)
	protected String  bId; /*接收表ID*/
	
	@TableField(exist=false)
	protected List<BpmCptoReceiver> receivers=new ArrayList<BpmCptoReceiver>();
	
	
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
	
	public void setBId(String bId) 
	{
		this.bId = bId;
	}
 
	public String getBId() 
	{
		return this.bId;
	}
	public void setInstId(String instId) 
	{
		this.instId = instId;
	}
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	public String getInstId() 
	{
		return this.instId;
	}
	public void setBpmnInstId(String bpmnInstId) 
	{
		this.bpmnInstId = bpmnInstId;
	}
	/**
	 * 返回 ACT实例ID
	 * @return
	 */
	public String getBpmnInstId() 
	{
		return this.bpmnInstId;
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
	
	public void setOpinion(String opinion) 
	{
		this.opinion = opinion;
	}
	/**
	 * 返回 意见
	 * @return
	 */
	public String getOpinion() 
	{
		return this.opinion;
	}
	public void setSubject(String subject) 
	{
		this.subject = subject;
	}
	/**
	 * 返回 流程实例标题
	 * @return
	 */
	public String getSubject() 
	{
		return this.subject;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}
	/**
	 * 返回 抄送类型(copyto抄送,trans转发)
	 * @return
	 */
	public String getType() 
	{
		return this.type;
	}
	public void setStartorId(String startorId) 
	{
		this.startorId = startorId;
	}
	/**
	 * 返回 流程发起人
	 * @return
	 */
	public String getStartorId() 
	{
		return this.startorId;
	}
	public String getStartor() {
		return startor;
	}
	public void setStartor(String startor) {
		this.startor = startor;
	}
	public void setTypeId(String typeId) 
	{
		this.typeId = typeId;
	}
	/**
	 * 返回 流程分类
	 * @return
	 */
	public String getTypeId() 
	{
		return this.typeId;
	}
	
	
	public String getRecever() {
		return recever;
	}
	public void setRecever(String recever) {
		this.recever = recever;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public List<BpmCptoReceiver> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<BpmCptoReceiver> receivers) {
		this.receivers = receivers;
	}
	
	public void addReceiver(BpmCptoReceiver receiver){
		this.receivers.add(receiver);
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
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("instId", this.instId) 
		.append("bpmnInstId", this.bpmnInstId) 
		.append("nodeId", this.nodeId) 
	
		.append("createTime", this.createTime) 
		.append("opinion", this.opinion) 
		.append("subject", this.subject) 
	
		.append("type", this.type) 
		.append("startorId", this.startorId) 
		.append("typeId", this.typeId)
		.append("recever",this.recever)
		.toString();
	}

}