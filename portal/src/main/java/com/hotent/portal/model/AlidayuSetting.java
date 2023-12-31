package com.hotent.portal.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 阿里大鱼配置
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
@Service
public class AlidayuSetting implements Serializable{
	protected static final long serialVersionUID = -1563590072023033989L;

	protected String freeSignName;
	protected String appkey;
	protected String secret;
	protected String url;
	protected String extend;
	protected String calledShowNum;

	public String getFreeSignName() {
		return freeSignName;
	}
	public String getAppkey() {
		return appkey;
	}
	public String getSecret() {
		return secret;
	}
	public String getUrl() {return url;}
	public String getExtend() {return extend;}
	public String getCalledShowNum() {
		return calledShowNum;
	}

	@Value("${alidayu.freeSignName:''}")
	public void setFreeSignName(String freeSignName) {
		this.freeSignName = freeSignName;
	}
	@Value("${alidayu.appkey:''}")
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	@Value("${alidayu.secret:''}")
	public void setSecret(String secret) {
		this.secret = secret;
	}
	@Value("${alidayu.url:''}")
	public void setUrl(String url) {
		this.url = url;
	}
	@Value("${alidayu.extend:''}")
	public void setExtend(String extend) {
		this.extend = extend;
	}
	@Value("${alidayu.calledShowNum:''}")
	public void setCalledShowNum(String calledShowNum) {
		this.calledShowNum = calledShowNum;
	}
}