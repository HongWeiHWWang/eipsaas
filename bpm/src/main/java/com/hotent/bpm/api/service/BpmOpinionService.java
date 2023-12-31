package com.hotent.bpm.api.service;

import java.util.List;

import com.hotent.base.groovy.IScript;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;


/**
 * 流程意见的API接口。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-2-上午11:56:44
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmOpinionService  extends IScript {
	
	/**
	 * 取得某个流程实例（含子流程实例）的所有的审批意见列表
	 * @param procInstId
	 * @return
	 */
	List<BpmTaskOpinion> getTaskOpinions(String procInstId);
	
	/**
	 * 获取某节点的审批信息
	 * @param instId
	 * @param nodeId
	 * @return
	 */
	List<BpmTaskOpinion> getByInstNodeId(String instId,String nodeId);
}
