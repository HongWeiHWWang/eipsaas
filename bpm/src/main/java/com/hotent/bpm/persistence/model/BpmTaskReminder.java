package com.hotent.bpm.persistence.model;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述：任务催办 实体对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "任务催办 实体对象")
@TableName("bpm_task_reminder")
public class BpmTaskReminder extends BaseModel<BpmTaskReminder> {

	private static final long serialVersionUID = -3420731810622673782L;

	public final static String TASK_DUE_ACTION_NO_ACTION = "no-action"; // 无动作
	public final static String TASK_DUE_ACTION_AUTO_NEXT = "auto-next"; // 自动下一个任务
	public final static String TASK_DUE_ACTION_END_PROCESS = "end-process"; // 结束任务
	public final static String TASK_DUE_ACTION_CALL_METHOD = "call-method"; // 调用方法

	@TableId("id_")
	@ApiModelProperty(name = "id", notes = "主键")
	protected String id;

	@TableField("task_id_")
	@ApiModelProperty(name = "taskId", notes = "催办任务ID")
	protected String taskId;

	@TableField("name_")
	@ApiModelProperty(name = "name", notes = "催办名称")
	protected String name;

	@TableField("rel_date_")
	@ApiModelProperty(name = "relDate", notes = "相对时间")
	protected LocalDateTime relDate;

	@TableField("due_action_")
	@ApiModelProperty(name = "dueAction", notes = "到期执行动作")
	protected String dueAction;

	@TableField("due_script_")
	@ApiModelProperty(name = "dueScript", notes = "调用指定方法")
	protected String dueScript;

	@TableField("due_date_")
	@ApiModelProperty(name = "dueDate", notes = "到期日期")
	protected LocalDateTime dueDate;

	@TableField("is_send_msg_")
	@ApiModelProperty(name = "isSendMsg", notes = "期间是否发送催办")
	protected Integer isSendMsg;

	/**
	 * 发送催办消息开始时间
	 */
	@TableField("msg_send_date_")
	@ApiModelProperty(name = "", notes = "")
	protected LocalDateTime msgSendDate;

	/**
	 * 发送消息间隔
	 */
	@TableField("msg_interval_")
	@ApiModelProperty(name = "", notes = "")
	protected Integer msgInterval;

	/**
	 * 发送次数
	 */
	@TableField("msg_count_")
	@ApiModelProperty(name = "", notes = "")
	protected Integer msgCount;

	/**
	 * 消息类型 inner,msg,email 等
	 */
	@TableField("msg_type_")
	@ApiModelProperty(name = "", notes = "")
	protected String msgType;

	/**
	 * 富文本内容
	 */
	@TableField("html_msg_")
	@ApiModelProperty(name = "", notes = "")
	protected String htmlMsg = "";

	/**
	 * 普通文本内容
	 */
	@TableField("plain_msg_")
	@ApiModelProperty(name = "", notes = "")
	protected String plainMsg = "";

	/**
	 * 预警配置（预警名称，triggerDate，change2level）
	 */
	@TableField("warningSet_")
	@ApiModelProperty(name = "", notes = "")
	protected String warningset;

	/**
	 * 触发时间(每次触发后更新触发时间)
	 */
	@TableField("trigger_date_")
	@ApiModelProperty(name = "", notes = "")
	protected LocalDateTime triggerDate;
	
	@TableField("send_Person_")
	@ApiModelProperty(name = "sendPerson", notes = "消息发送对象。本人，秘书")
	protected String sendPerson;

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
	 * 返回 催办任务ID
	 * 
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 催办名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setRelDate(LocalDateTime relDate) {
		this.relDate = relDate;
	}

	/**
	 * 返回 相对时间
	 * 
	 * @return
	 */
	public LocalDateTime getRelDate() {
		return this.relDate;
	}

	public void setDueAction(String dueAction) {
		this.dueAction = dueAction;
	}

	/**
	 * 返回 到期执行动作
	 * 
	 * @return
	 */
	public String getDueAction() {
		return this.dueAction;
	}

	public void setDueScript(String dueScript) {
		this.dueScript = dueScript;
	}

	/**
	 * 返回 调用指定方法
	 * 
	 * @return
	 */
	public String getDueScript() {
		return this.dueScript;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * 返回 到期日期
	 * 
	 * @return
	 */
	public LocalDateTime getDueDate() {
		return this.dueDate;
	}

	public void setIsSendMsg(Integer isSendMsg) {
		this.isSendMsg = isSendMsg;
	}

	/**
	 * 返回 期间是否发送催办
	 * 
	 * @return
	 */
	public Integer getIsSendMsg() {
		return this.isSendMsg;
	}

	public void setMsgSendDate(LocalDateTime msgSendDate) {
		this.msgSendDate = msgSendDate;
	}

	/**
	 * 返回 发送催办消息开始时间
	 * 
	 * @return
	 */
	public LocalDateTime getMsgSendDate() {
		return this.msgSendDate;
	}

	public void setMsgInterval(Integer msgInterval) {
		this.msgInterval = msgInterval;
	}

	/**
	 * 返回 发送消息间隔
	 * 
	 * @return
	 */
	public Integer getMsgInterval() {
		return this.msgInterval;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgCount = msgCount;
	}

	/**
	 * 返回 发送次数
	 * 
	 * @return
	 */
	public Integer getMsgCount() {
		return this.msgCount;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * 返回 消息类型 inner,msg,email 等
	 * 
	 * @return
	 */
	public String getMsgType() {
		return this.msgType;
	}

	public void setHtmlMsg(String htmlMsg) {
		this.htmlMsg = htmlMsg;
	}

	/**
	 * 返回 富文本内容
	 * 
	 * @return
	 */
	public String getHtmlMsg() {
		return this.htmlMsg;
	}

	public void setPlainMsg(String plainMsg) {
		this.plainMsg = plainMsg;
	}

	/**
	 * 返回 普通文本内容
	 * 
	 * @return
	 */
	public String getPlainMsg() {
		return this.plainMsg;
	}

	public void setWarningset(String warningset) {
		this.warningset = warningset;
	}

	/**
	 * 返回 预警配置（预警名称，triggerDate，change2level）
	 * 
	 * @return
	 */
	public String getWarningset() {
		return this.warningset;
	}

	public void setTriggerDate(LocalDateTime triggerDate) {
		this.triggerDate = triggerDate;
	}

	/**
	 * 返回 触发时间(每次触发后更新触发时间)
	 * 
	 * @return
	 */
	public LocalDateTime getTriggerDate() {
		return this.triggerDate;
	}
	

	public String getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("taskId", this.taskId).append("name", this.name)
				.append("relDate", this.relDate).append("dueAction", this.dueAction).append("dueScript", this.dueScript)
				.append("dueDate", this.dueDate).append("isSendMsg", this.isSendMsg)
				.append("msgSendDate", this.msgSendDate).append("msgInterval", this.msgInterval)
				.append("msgCount", this.msgCount).append("msgType", this.msgType).append("htmlMsg", this.htmlMsg)
				.append("plainMsg", this.plainMsg).append("warningset", this.warningset)
				.append("triggerDate", this.triggerDate).toString();
	}
}