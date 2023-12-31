package com.hotent.bpm.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:流程代理条件 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-04-30 15:11:06
 */
@TableName("bpm_agent_condition")
public class BpmAgentCondition extends BaseModel<BpmAgentCondition>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1009231165090113124L;
	@TableId("id_")
	protected String  id; /*主键*/
	@TableField("setting_id_")
	protected String  settingId=""; /*设定ID*/
	@TableField("condition_desc_")
	protected String  conditionDesc; /*条件描述*/
	@TableField("condition_")
	protected String  condition; /*条件*/
	@TableField("agent_id_")
	protected String  agentId; /*代理人ID*/
	@TableField("agent_name_")
	protected String  agentName; /*代理人*/
	
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
	public void setConditionDesc(String conditionDesc) 
	{
		this.conditionDesc = conditionDesc;
	}
	/**
	 * 返回 条件描述
	 * @return
	 */
	public String getConditionDesc() 
	{
		return this.conditionDesc;
	}
	public void setCondition(String condition) 
	{
		this.condition = condition;
	}
	/**
	 * 返回 条件
	 * @return
	 */
	public String getCondition() 
	{
		return this.condition;
	}
	public void setAgentId(String agentId) 
	{
		this.agentId = agentId;
	}
	/**
	 * 返回 代理人ID
	 * @return
	 */
	public String getAgentId() 
	{
		return this.agentId;
	}
	public void setAgentName(String agentName) 
	{
		this.agentName = agentName;
	}
	/**
	 * 返回 代理人
	 * @return
	 */
	public String getAgentName() 
	{
		return this.agentName;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("setting", this.settingId) 
		.append("conditionDesc", this.conditionDesc) 
		.append("condition", this.condition) 
		.append("agentId", this.agentId) 
		.append("agentName", this.agentName) 
		.toString();
	}
}