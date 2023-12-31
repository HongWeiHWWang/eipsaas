package com.hotent.bpm.api.cmd;


/**
 * 
 * 描述：流程实例对象命令对象
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-15-上午10:59:01
 * 版权：广州宏天软件有限公司版权所有
 */
public interface ProcessInstCmd  extends ActionCmd {
	/**
	 * 取得BPMN流程定义ID
	 * getBpmnDefId(),getProcDefId(),getFlowKey()三者只需要设置一值
	 * @return  String
	 */
	String getBpmnDefId(); 
	/**
	 * 取得X5流程定义ID
	 * getBpmnDefId(),getProcDefId(),getFlowKey()三者只需要设置一值
	 * @return  String
	 */
	String getProcDefId();
	/**
	 * 获取流程定义标识键
	 * getBpmnDefId(),getProcDefId(),getFlowKey()三者只需要设置一值
	 * @return  String
	 */
	String getFlowKey();
	/**
	 * 流程实例ID。
	 * @return  String
	 */
	String getInstId();
	/**
	 * 获取实例标题
	 * @return  String
	 */
	String getSubject();
	
	String getUrgentState();

    Boolean getApproval();

    int getSupportMobile();
}
