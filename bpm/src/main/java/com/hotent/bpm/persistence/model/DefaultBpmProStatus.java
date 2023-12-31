package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:流程节点审批状态 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-03-18 10:56:35
 */
@TableName("bpm_pro_status")
public class DefaultBpmProStatus extends BaseModel<DefaultBpmProStatus>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8206044536482924700L;
	@TableId("id_")
	protected String  id; /*主键*/
	
	@TableField("proc_inst_id_")
	protected String  procInstId; /*流程实例ID*/
	
	@TableField("proc_def_id_")
	protected String  procDefId; /*ACT流程定义ID*/
	
	@TableField("node_id_")
	protected String  nodeId; /*节点ID*/
	
	@TableField("node_name_")
	protected String  nodeName; /*节点名称*/
	
	@TableField("status_")
	protected String  status; /*状态*/
	
	@TableField("last_update_")
	protected LocalDateTime  lastUpdate; /*最后更新时间*/
	
	@TableField("last_userid_")
	protected String  lastUserid; /*last_userid_*/
	
	
	public DefaultBpmProStatus(){}
	
	public DefaultBpmProStatus(String procInstId,String procDefId,String nodeId,String nodeName,String status,String lastUserId){
		this.procInstId=procInstId;
		this.procDefId=procDefId;
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.status=status;
		this.lastUserid=lastUserId;
		this.lastUpdate=LocalDateTime.now();
	}
	
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
	public void setProcDefId(String procDefId) 
	{
		this.procDefId = procDefId;
	}
	/**
	 * 返回 ACT流程定义ID
	 * @return
	 */
	public String getProcDefId() 
	{
		return this.procDefId;
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
	 * 返回 节点名称
	 * @return
	 */
	public String getNodeName() 
	{
		return this.nodeName;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 状态
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	public void setLastUpdate(LocalDateTime lastUpdate) 
	{
		this.lastUpdate = lastUpdate;
	}
	/**
	 * 返回 最后更新时间
	 * @return
	 */
	public LocalDateTime getLastUpdate() 
	{
		return this.lastUpdate;
	}
	public void setLastUserid(String lastUserid) 
	{
		this.lastUserid = lastUserid;
	}
	/**
	 * 返回 last_userid_
	 * @return
	 */
	public String getLastUserid() 
	{
		return this.lastUserid;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("procInstId", this.procInstId) 
		.append("procDefId", this.procDefId) 
		.append("nodeId", this.nodeId) 
		.append("nodeName", this.nodeName) 
		.append("status", this.status) 
		.append("lastUpdate", this.lastUpdate) 
		.append("lastUserid", this.lastUserid) 
		.toString();
	}
}