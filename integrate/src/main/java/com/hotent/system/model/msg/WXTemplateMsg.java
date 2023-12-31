package com.hotent.system.model.msg;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信模板消息
 * @author mikel
 *
 */
public class WXTemplateMsg {

	/** 消息目标用户 */
	private String openid;
	/** 消息链接地址 */
	private String url;
	/** 模板消息头部标题 */
	private WXTemplateParam title;
	/** 模板消息尾部描述 */
	private WXTemplateParam remark;
	/** 模板消息体参数列表 */
	private Map<String, WXTemplateParam> params = new HashMap<String, WXTemplateParam>();

	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public WXTemplateParam getTitle() {
		return title;
	}
	public void setTitle(WXTemplateParam title) {
		this.title = title;
	}
	public WXTemplateParam getRemark() {
		return remark;
	}
	public void setRemark(WXTemplateParam remark) {
		this.remark = remark;
	}
	public Map<String, WXTemplateParam> getParams() {
		return params;
	}
	public void addParam(String name, WXTemplateParam param) {
		this.params.put(name, param);
	}
}
