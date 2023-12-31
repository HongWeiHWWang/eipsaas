package com.hotent.bpm.api.cmd;

import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.constant.DataType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.NodeDefTransient;

/**
 * 
 * 描述：任务操作处理基础接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-15-上午10:23:08
 * 版权：广州宏天软件有限公司版权所有
 */
public interface ActionCmd {
	
	/**
	 * 数据可以存放主表名，主键值。
	 * 可以值多个主键。
	 */
	public static final String DATA_MODE_PAIR="pair";
	
	/**
	 * 单主键。
	 */
	public static final String DATA_MODE_PK="pk";
	
	/**
	 * bo数据。
	 */
	public static final String DATA_MODE_BO="bo";
	
	/*
	 * 当前线程记录已添加的网关堆栈
	 */
	public  List<NodeDefTransient> getGateways();
	
	/*
	 * 向当前线程中记录一个已记录的网关
	 */
	public  void addGateway(NodeDefTransient gatewayNode);

	/**
	 * 获取变量。
	 * @return
	 */
	Map<String,Object> getVariables();
	
	/**
	 * 添加变量。
	 * @param name
	 * @param value 
	 * void
	 */
	void addVariable(String name,String value);
	
	void setVariables(Map<String,Object> vars);
	
	/**
	 * 获取临时变量，这个变量不作为流程变量，用于临时存放变量。
	 * @return  Map<String,Object>
	 */
	Map<String,Object>  getTransitVars();
	
	/**
	 * 添加临时变量。
	 * @param name
	 * @param value 
	 * void
	 */
	void addTransitVars(String name,Object value);
	
	/**
	 * 放入临时变量。
	 * @param transitVars 
	 * void
	 */
	void putTransitVars(Map<String,Object> transitVars);
	
	/**
	 * 根据名称获取临时变量。 
	 * @param name
	 * @return 
	 * Object
	 */
	Object getTransitVars(String name);
	
	/**
	 * 清除临时变量。
	 * void
	 */
	void cleanTransitVars();
	
	/**
	 * 获取临时变量。
	 * @param name
	 * @param defaultValue 默认值
	 * @return
	 */
	Object getTransitVars(String name,Object defaultValue);
	
	/**
	 * 获取下一步节点执行人。
	 * <pre>
	 * 	键为节点ID
	 *  值为接收人员。
	 * </pre>
	 * @return 
	 * Map&lt;String,List&lt;BpmIdentity>>
	 */
	Map<String, List<BpmIdentity>> getBpmIdentities();
	
	/**
	 * 目标节点，在一般情况下不需要指定，流程会按照流程图进行运行。
	 * 在需要指定节点跳转的情况下，才会起作用。
	 */
	String getDestination();
 
	/**
	 * 获取流程实例ID,一个cmd实例对应一个一个流程实例ID，
	 * 主要是为了防止线程变量被覆盖。
	 * @return String
	 */
	String getInstId();
	
	/**
	 * 设置通知类型。
	 * @param notifyType 
	 * void
	 */
	void setNotifyType(String notifyType);
	
	/**
	 * 获取通知类型。
	 * @return 
	 * String
	 */
	String getNotifyType();
	
	/**
	 * 获取业务数据模式。
	 * @return  String
	 */
	String getDataMode();
	
	/**
	 * 设置业务数据模式。
	 * void
	 */
	void setDataMode(String mode);
	
	/**
	 * bo的JSON数据。
	 * @param json 
	 * void
	 */
	void setBusData(String json);
	/**
	 * 获取BO的JSON数据。
	 * @return
	 */
	String getBusData();
	
	
	/**
	 * 获取业务主键
	 * @return  String
	 */
	String getBusinessKey();
	
	/**
	 * 获取业务系统编码
	 * @return  String
	 */
	String getSysCode();
	
	/**
	 * 获取主键的的数据类型。
	 * @return  Datatype
	 */
	DataType getPkDataType();
	
	/**
	 * 设置数据类型 
	 * @param dataType 
	 * void
	 */
	void setPkDataType(DataType dataType);
	
	/**
	 * 设置主键。
	 * @param businessKey 
	 * void
	 */
	void setBusinessKey(String businessKey);
	
	/**
	 * 设置业务系统编码。
	 * @param businessKey 
	 * void
	 */
	void setSysCode(String sysCode);
	
	/**
	 * 设置业务数据对。
	 * 键为业务标识，值为对应的值。
	 * @param pair
	 */
	void setDataPair(Map<String,String> pair);
	
	/**
	 * 获取业务数据对。
	 * @return
	 */
	Map<String,String> getDataPair();
	
	
	/**
	 * 获取审批的处理状态或动作。
	 * <pre>
	 * 启动流程:
	 * 1.启动流程 startFlow
	 * 2.保存草稿 saveDraft
	 * 
	 * 审批流程：
	 * 1.agree 审批。
	 * 2.abandon 弃权。
	 * 3.oppose 反对。
	 * 4.startTrans 发起流转
	 * 5.agreeTrans 流转同意。
	 * 6.opposeTrans 流转反对。
	 * 7.delivertoAgree 转办同意
	 * 8.delivertoOppose 转办反对
	 * 9.startCommu 沟通。
	 * 10.commu 反馈。
	 * 11.reject 驳回。
	 * 12.backToStart 驳回到发起人。
	 * 13.instanceTrans 抄送
	 * 14.endProcess 终止流程
	 * 15.delegate 转办
	 * 16.lockUnlock 锁定/解锁
	 * 17.taskDelay 任务延期
	 * 18.inqu 征询
	 * 19.inqu_repy 征询回复
	 * 20.follow 跟踪
	 * 21.addSign 加签
	 * </pre>
	 * @return  String
	 */
	String getActionName();
	
	/**
	 * 设置动作名称。
	 * @param actionName
	 * @return
	 */
	void setActionName(String actionName);
	
    String getAgentLeaderId();
    
    void setAgentLeaderId(String agentLeaderId);
	
}
