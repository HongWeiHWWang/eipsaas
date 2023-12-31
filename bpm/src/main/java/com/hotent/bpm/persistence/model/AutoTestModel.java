package com.hotent.bpm.persistence.model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.hotent.base.util.BeanUtils;
import com.hotent.uc.api.model.IUser;


/**
 * 流程仿真model
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-01-03-下午3:57:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class AutoTestModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 任务ID
	 */
	private String taskId="";
	
	/**
	 * 节点id
	 */
	private String nodeId="";
	
	private String nodeName="";
	
	private String subject="";
	
	/**
	 * 流程实例ID
	 */
	private String procInstId="";
	
	/**
	 * 任务审批人
	 */
	private List<IUser> userList;
	
	/**
	 * 跳过debugger,重新启动时设置为不执行断点设置
	 */
	private Boolean skipDebugger = false;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<IUser> getUserList() {
		return userList;
	}

	public void setUserList(List<IUser> userList) {
		this.userList = userList;
	}
	
	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Boolean getSkipDebugger() {
		return skipDebugger;
	}

	public void setSkipDebugger(Boolean skipDebugger) {
		this.skipDebugger = skipDebugger;
	}
	
	
	/**
	 * 随机获取一个用户去审批
	 * @return
	 */
	public String getRandomAccount(){
		if(BeanUtils.isEmpty(userList)) return "";
		Random ran = new Random();
		return userList.get(ran.nextInt(userList.size())).getAccount();
	}
	
}
