package com.hotent.bpm.persistence.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程实例与分支
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月2日
 */

@ApiModel(value="流程实例与分支")
@TableName("act_ru_execution")
public class ActExecution extends BaseModel<ActExecution> implements Cloneable{

	private static final long serialVersionUID = -7764078260748703148L;
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String  id; /*ID_*/
	
	@TableField("rev_")
	@ApiModelProperty(name="rev",notes="乐观锁")
	protected Integer  rev; /*REV_*/
	
	@TableField("proc_inst_id_")
	@ApiModelProperty(name="procInstId",notes="流程实例id")
	protected String  procInstId; /*PROC_INST_ID_*/
	
	@TableField("business_key_")
	@ApiModelProperty(name="businessKey",notes="业务标识")
	protected String  businessKey; /*BUSINESS_KEY_*/
	
	@TableField("parent_id_")
	@ApiModelProperty(name="parentId",notes="父执行")
	protected String  parentId; /*PARENT_ID_*/
	
	@TableField("proc_def_id_")
	@ApiModelProperty(name="procDefId",notes="流程定义id")
	protected String  procDefId; /*PROC_DEF_ID_*/
	
	@TableField("super_exec_")
	@ApiModelProperty(name="superExec",notes="父流程实例中对应的执行")
	protected String  superExec; /*SUPER_EXEC_*/
	
	@TableField("act_id_")
	@ApiModelProperty(name="actId",notes="环节ID")
	protected String  actId; /*ACT_ID_*/
	
	@TableField("is_active_")
	@ApiModelProperty(name="isActive",notes="是否激活")
	protected Short  isActive; /*IS_ACTIVE_*/
	
	@TableField("is_concurrent_")
	@ApiModelProperty(name="isConcurrent",notes="是否并行分支")
	protected Short  isConcurrent; /*IS_CONCURRENT_*/
	
	@TableField("is_scope_")
	@ApiModelProperty(name="isScope",notes="是否处于多实例或环节嵌套状态")
	protected Short  isScope; /*IS_SCOPE_*/
	
	@TableField("is_event_scope_")
	@ApiModelProperty(name="isEventScope",notes="是否激活状态")
	protected Short  isEventScope; /*IS_EVENT_SCOPE_*/
	
	@TableField("suspension_state_")
	@ApiModelProperty(name="suspensionState",notes="暂停状态 1激活 2暂停")
	protected Integer  suspensionState; /*SUSPENSION_STATE_*/
	
	@TableField("cached_ent_state_")
	@ApiModelProperty(name="cachedEntState",notes="缓存的状态，事件监听第1位 人工任务第2位 异步作业第3位")
	protected Integer  cachedEntState; /*CACHED_ENT_STATE_*/
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setRev(Integer rev) 
	{
		this.rev = rev;
	}
	/**
	 * 返回 REV_
	 * @return
	 */
	public Integer getRev() 
	{
		return this.rev;
	}
	public void setProcInstId(String procInstId) 
	{
		this.procInstId = procInstId;
	}
	/**
	 * 返回 PROC_INST_ID_
	 * @return
	 */
	public String getProcInstId() 
	{
		return this.procInstId;
	}
	public void setBusinessKey(String businessKey) 
	{
		this.businessKey = businessKey;
	}
	/**
	 * 返回 BUSINESS_KEY_
	 * @return
	 */
	public String getBusinessKey() 
	{
		return this.businessKey;
	}
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}
	/**
	 * 返回 PARENT_ID_
	 * @return
	 */
	public String getParentId() 
	{
		return this.parentId;
	}
	public void setProcDefId(String procDefId) 
	{
		this.procDefId = procDefId;
	}
	/**
	 * 返回 PROC_DEF_ID_
	 * @return
	 */
	public String getProcDefId() 
	{
		return this.procDefId;
	}
	public void setSuperExec(String superExec) 
	{
		this.superExec = superExec;
	}
	/**
	 * 返回 SUPER_EXEC_
	 * @return
	 */
	public String getSuperExec() 
	{
		return this.superExec;
	}
	public void setActId(String actId) 
	{
		this.actId = actId;
	}
	/**
	 * 返回 ACT_ID_
	 * @return
	 */
	public String getActId() 
	{
		return this.actId;
	}
	public void setIsActive(Short isActive) 
	{
		this.isActive = isActive;
	}
	/**
	 * 返回 IS_ACTIVE_
	 * @return
	 */
	public Short getIsActive() 
	{
		return this.isActive;
	}
	public void setIsConcurrent(Short isConcurrent) 
	{
		this.isConcurrent = isConcurrent;
	}
	/**
	 * 返回 IS_CONCURRENT_
	 * @return
	 */
	public Short getIsConcurrent() 
	{
		return this.isConcurrent;
	}
	public void setIsScope(Short isScope) 
	{
		this.isScope = isScope;
	}
	/**
	 * 返回 IS_SCOPE_
	 * @return
	 */
	public Short getIsScope() 
	{
		return this.isScope;
	}
	public void setIsEventScope(Short isEventScope) 
	{
		this.isEventScope = isEventScope;
	}
	/**
	 * 返回 IS_EVENT_SCOPE_
	 * @return
	 */
	public Short getIsEventScope() 
	{
		return this.isEventScope;
	}
	public void setSuspensionState(Integer suspensionState) 
	{
		this.suspensionState = suspensionState;
	}
	/**
	 * 返回 SUSPENSION_STATE_
	 * @return
	 */
	public Integer getSuspensionState() 
	{
		return this.suspensionState;
	}
	public void setCachedEntState(Integer cachedEntState) 
	{
		this.cachedEntState = cachedEntState;
	}
	/**
	 * 返回 CACHED_ENT_STATE_
	 * @return
	 */
	public Integer getCachedEntState() 
	{
		return this.cachedEntState;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("rev", this.rev) 
		.append("procInstId", this.procInstId) 
		.append("businessKey", this.businessKey) 
		.append("parentId", this.parentId) 
		.append("procDefId", this.procDefId) 
		.append("superExec", this.superExec) 
		.append("actId", this.actId) 
		.append("isActive", this.isActive) 
		.append("isConcurrent", this.isConcurrent) 
		.append("isScope", this.isScope) 
		.append("isEventScope", this.isEventScope) 
		.append("suspensionState", this.suspensionState) 
		.append("cachedEntState", this.cachedEntState) 
		.toString();
	}
	
	
	@Override
	public Object clone(){
		ActExecution o = null;  
        try {  
            o = (ActExecution) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
	    return o;  
	}
}