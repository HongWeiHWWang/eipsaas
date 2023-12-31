package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import com.hotent.bpm.api.model.process.nodedef.JumpRule;


/**
 * 跳转规则。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-27-上午9:05:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultJumpRule implements JumpRule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 规则名称
	 */
	private String ruleName="";
	
	/**
	 * 目标节点
	 */
	private String targetNode="";
	
	/**
	 * 条件
	 */
	private String condition="";
	
	public DefaultJumpRule(){
		
	}
	
	public DefaultJumpRule(String ruleName,String targetNode,String condition){
		this.ruleName=ruleName;
		this.targetNode=targetNode;
		this.condition=condition;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	

}
