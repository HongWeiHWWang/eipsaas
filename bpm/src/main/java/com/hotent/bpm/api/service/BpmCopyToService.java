package com.hotent.bpm.api.service;

import java.util.List;

import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.uc.api.model.IUser;

/**
 * 抄送服务接口。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-11-25-上午10:07:27
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmCopyToService {	
	
	void copyTo(List<IUser> receiverUsers,BpmProcessInstance instance,String nodeId);
	
	
	
}
