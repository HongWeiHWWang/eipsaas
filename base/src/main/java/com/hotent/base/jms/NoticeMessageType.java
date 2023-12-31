package com.hotent.base.jms;

/**
 * 通知的消息类型
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月29日
 */
public enum NoticeMessageType {
	SMS("sms","短信", false),
	VOICE("voice","语音", false),
	/* INNER("inner","站内消息", false), 7.1前端没入口，暂时先去掉站内消息*/
	MAIL("mail","邮件", false),
	WXENTERPRISE("wxEnterprise","微信", false),
	DINGTALK("dingTalk","钉钉",false);

	private String key;
	private String label;
	private Boolean isPlain;	/*是否纯文本*/
	
	NoticeMessageType(String key, String label, Boolean isPlain){
		this.key = key;
		this.label = label;
		this.isPlain = isPlain;
	}
	/**
	 * 获取key
	 * @return
	 */
	public String key(){
		return key;		
	}
	/**
	 * 获取说明
	 * @return
	 */
	public String label(){
		return label;		
	}
	/**
	 * 获取是否仅支持纯文本
	 * @return
	 */
	public Boolean isPlain() {
		return isPlain;
	}
}
