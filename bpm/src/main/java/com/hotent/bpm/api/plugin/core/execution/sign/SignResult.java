/**
 * 描述：TODO
 * 包名：com.hotent.runtime.api.plugin.core.execution.sign
 * 文件名：SignResult.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-11-上午9:45:53
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.execution.sign;

import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.OpinionStatus;


/**
 * 会签结果对象。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-11-上午9:45:53
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SignResult {
	/**
	 * 判断该会签handler是否完成
	 */
	private boolean isComplete=false;
	
	/**
	 * 要更新为的节点状态
	 */
	private NodeStatus nodeStatus;
	
	
	private OpinionStatus opinionStatus;
	
	
	public SignResult(){
		
	} 
	

	public SignResult(boolean isComplete, NodeStatus nodeStatus,OpinionStatus opinionStatus) {
		this.isComplete = isComplete;
		this.nodeStatus = nodeStatus;
		this.opinionStatus=opinionStatus;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public NodeStatus getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(NodeStatus nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public OpinionStatus getOpinionStatus() {
		return opinionStatus;
	}

	public void setOpinionStatus(OpinionStatus opinionStatus) {
		this.opinionStatus = opinionStatus;
	}
	
	
	
}
