package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;

/**
 * 字段初始化类。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014年8月22日-上午10:06:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class FieldInitSetting implements Serializable {
	
	private static final long serialVersionUID = -7739124819357590388L;

	/**
	 * 字段描述
	 */
	private String description="";
	
	/**
	 * 设定，脚本或流水号。
	 */
	private String setting="";
	


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}
	
	

}
