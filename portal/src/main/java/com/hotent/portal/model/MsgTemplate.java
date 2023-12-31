package com.hotent.portal.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:消息模版 entity对象
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
@ApiModel(description="消息模板")
@TableName("portal_sys_msg_tpl")
public class MsgTemplate extends AutoFillModel<MsgTemplate>{
	private static final long serialVersionUID = 1L;
	public static Short IS_DEFAULT_YES = 1;
	public static Short IS_DEFAULT_NO = 0;
	
	/** 模板用途类型静态变量  **/
	/**
	 * 任务创建通知
	 */
	public static String TYPE_TASKCREATE = "taskCreate";
	/**
	 * 任务沟通
	 */
	public static String TYPE_BPMCOMMUSEND = "bpmCommuSend";
	/**
	 * 通知沟通人
	 */
	public static String TYPE_BPMCOMMUFEEDBACK = "bpmCommuFeedBack";
	/**
	 * 任务流转默认
	 */
	public static String TYPE_BPMNTASKTRANS = "bpmnTaskTrans";
	/**
	 * 任务转交通知
	 */
	public static String TYPE_BPMHANDTO = "bpmHandTo";
	/**
	 * 加签通知
	 */
	public static String TYPE_ADDSIGNTASK = "addSignTask";
	/**
	 * 任务完成通知
	 */
	public static String TYPE_TASKCOMPLETE = "taskComplete";
	/**
	 * 任务驳回通知
	 */
	public static String TYPE_TASKBACK = "taskBack";
	/**
	 * 流程结束通知
	 */
	public static String TYPE_PROCESSEND = "processEnd";
	/**
	 * 审批提醒
	 */
	public static String TYPE_BPMNAPPROVAL = "bpmnApproval";
	/**
	 * 驳回提醒
	 */
	public static String TYPE_BPMNBACK = "bpmnBack";
	/**
	 * 撤销提醒
	 */
	public static String TYPE_BPMNRECOVER = "bpmnRecover";
	/**
	 * 代理任务审批
	 */
	public static String TYPE_BPMNAGENT = "bpmnAgent";
	/**
	 * 通知被代理人
	 */
	public static String TYPE_BPMNDELEGATE = "bpmnDelegate";
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id; 				
	
	@ApiModelProperty(name="name", notes="模版名称")
	@TableField("name_")
	protected String  name; 			
	
	@ApiModelProperty(name="key", notes="模版业务键")
	@TableField("key_")
	protected String  key;
	
	@ApiModelProperty(name="typeKey", notes="模板分类（可以按任务操作类型分类，也可以按其它方式分类）")
	@TableField("type_key_")
	protected String  typeKey;
	
	@ApiModelProperty(name="isDefault", notes="是否默认模板（对于同一组（模板分类+接收者类型）下的多个模板其中默认的一个）")
	@TableField("is_default_")
	protected char  isDefault;
	
	@ApiModelProperty(name="subject", notes="标题")
	@TableField("subject_")
	protected String  subject; 
	
	@ApiModelProperty(name="plain", notes="纯文本")
	@TableField("plain_")
	protected String  plain;
	
	@ApiModelProperty(name="html", notes="模版体HTML")
	@TableField("html_")
	protected String  html;

	@ApiModelProperty(name="smsTemplateNo", notes="短信模板")
	@TableField("sms_template_no_")
	protected String smsTemplateNo; 
	
	@ApiModelProperty(name="voiceTemplateNo", notes="语音模板")
	@TableField("voice_template_no_")
	protected String voiceTemplateNo;
	
	public void setSmsTemplateNo(String smsTemplateNo) 
	{
		this.smsTemplateNo = smsTemplateNo;
	}

	public String getSmsTemplateNo() 
	{
		return this.smsTemplateNo;
	}
	
	public void setVoiceTemplateNo(String voiceTemplateNo) 
	{
		this.voiceTemplateNo = voiceTemplateNo;
	}
 
	public String getVoiceTemplateNo() 
	{
		return this.voiceTemplateNo;
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
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 模版名称
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setKey(String key) 
	{
		this.key = key;
	}
	/**
	 * 返回 模版业务键
	 * @return
	 */
	public String getKey() 
	{
		return this.key;
	}
	
	public void setTypeKey(String typeKey) 
	{
		this.typeKey = typeKey;
	}
	/**
	 * 返回 模板分类。可以按任务操作类型分类，也可以按其它方式分类。
	 * @return
	 */
	public String getTypeKey() 
	{
		return this.typeKey;
	}
	
	public void setIsDefault(char isDefault) 
	{
		this.isDefault = isDefault;
	}
	/**
	 * 返回 是否默认模板。对于同一组（模板分类+接收者类型）下的多个模板其中默认的一个。
	 * @return
	 */
	public char getIsDefault() 
	{
		return this.isDefault;
	}
	public void setSubject(String subject) 
	{
		this.subject = subject;
	}
	/**
	 * 返回 标题
	 * @return
	 */
	public String getSubject() 
	{
		return this.subject;
	}
	public void setPlain(String plain) 
	{
		this.plain = plain;
	}
	/**
	 * 返回 纯文本
	 * @return
	 */
	public String getPlain() 
	{
		return this.plain;
	}
	public void setHtml(String html) 
	{
		this.html = html;
	}
	/**
	 * 返回 模版体HTML
	 * @return
	 */
	public String getHtml()
	{
		return this.html;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("key", this.key) 
		.append("typeKey", this.typeKey) 
		.append("isDefault", this.isDefault) 
		.append("subject", this.subject) 
		.append("plain", this.plain) 
		.append("html", this.html) 
		.toString();
	}
	
}