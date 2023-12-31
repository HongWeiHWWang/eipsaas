package com.hotent.bpm.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:代理指定流程 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-04-30 15:11:06
 */
@TableName("bpm_agent_def")
public class BpmAgentDef extends BaseModel<BpmAgentDef	>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6987814152633171417L;
	@TableId("id_")
	protected String  id; /*主键*/
	@TableField("setting_id_")
	protected String  settingId; /*设定ID*/
	@TableField("flow_key_")
	protected String  flowKey; /*流程定义KEY*/
	@TableField("flow_name_")
	protected String flowName="";
	@TableField("node_id_")
	protected String  nodeId; /*节点定义ID(为空的情况,如果指定ID,那么代理只在这些ID的任务生效)*/
	@TableField("node_name_")
	protected String  nodeName; /*节点名称*/
	
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
	public void setSettingId(String settingId) 
	{
		this.settingId = settingId;
	}
	/**
	 * 返回 设定ID
	 * @return
	 */
	public String getSettingId() 
	{
		return this.settingId;
	}
	public void setFlowKey(String flowKey) 
	{
		this.flowKey = flowKey;
	}
	/**
	 * 返回 流程定义KEY
	 * @return
	 */
	public String getFlowKey() 
	{
		return this.flowKey;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public void setNodeId(String nodeId) 
	{
		this.nodeId = nodeId;
	}
	/**
	 * 返回 节点定义ID(为空的情况,如果指定ID,那么代理只在这些ID的任务生效)
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
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("settingId", this.settingId) 
		.append("flowKey", this.flowKey) 
		.append("nodeId", this.nodeId) 
		.append("nodeName", this.nodeName) 
		.toString();
	}
}