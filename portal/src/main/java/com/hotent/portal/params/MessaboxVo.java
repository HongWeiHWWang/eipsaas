package com.hotent.portal.params;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 消息盒子返回参数对象
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年8月2日
 */
public class MessaboxVo {

	@ApiModelProperty(name = "messCount", notes = "消息总数")
	protected int messCount;

	@ApiModelProperty(name = "noReadMessCount", notes = "未读消息数")
	protected int noReadMessCount;
	
	public MessaboxVo(int messCount,int noReadMessCount){
		this.messCount = messCount;
		this.noReadMessCount = noReadMessCount;
	}

	public int getMessCount() {
		return messCount;
	}

	public void setMessCount(int messCount) {
		this.messCount = messCount;
	}

	public int getNoReadMessCount() {
		return noReadMessCount;
	}

	public void setNoReadMessCount(int noReadMessCount) {
		this.noReadMessCount = noReadMessCount;
	}

}