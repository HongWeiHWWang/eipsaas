package com.hotent.bpm.api.model.component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 
 * 描述：BPM 消息实体接口,含邮件、短信、内部消息的基本信息
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-7-下午4:51:37
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmMessage {
	/**
	 * 消息ID
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getMessageId();
	/**
	 * 获取消息标题
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getSubject();
	/**
	 * 消息的内容
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getContent();
	/**
	 * 消息的模板
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getMsgTemplate();
	/**
	 * 模板内容合并的数据实体
	 * @return Map<String,Object>
	 * @exception 
	 * @since  1.0.0
	 */
	public Map<String, Object> getMsgModel();
	/**
	 * 发送用户
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getSenderId();
	/**
	 * 接收用户
	 * @return String[]
	 * @exception 
	 * @since  1.0.0
	 */
	public String[]getReceiverIds();
	/**
	 * 消息类型
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getMsgType();	
	/**
	 * 状态
	 * @return String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getStatus();
	/**
	 * 发送日期
	 * @return Date
	 * @exception 
	 * @since  1.0.0
	 */
	public LocalDateTime getSendTime();
}
