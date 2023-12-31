package com.hotent.bpm.api.constant;

import javax.xml.namespace.QName;

/**
 * BPM常用变量。
 * 
 * <pre>
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-1-下午5:54:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmConstants {

	String TRUE = "true";

	String FALSE = "false";
	
	String NO = "no";
	
	String YES = "yes";

	String PROCESSDEFINITIONENTITY_CACHENAME = "bpm:ProcessDefinitionEntity";
	
	String BPMPROCESSDEF_CACHENAME = "bpm:BpmProcessDef";

	/**
	 * 流程实例标题。
	 */
	String SUBJECT = "subject_";
	/**
	 * 业务主键。
	 */
	String BUSINESS_KEY = "businessKey_";
	/**
	 * 业务系统编码。
	 */
	String SYS_CODE = "sysCode_";

	/**
	 * 发起人
	 */
	String START_USER = "startUser";

	/**
	 * bpm系统产生的实例ID.
	 */
	String PROCESS_INST_ID = "instanceId_";

	/**
	 * 任务执行人。
	 */
	String ASIGNEE = "assignee";

	/**
	 * 父实例ID。
	 */
	String PROCESS_PARENT_INST_ID = "parentInstanceId_";

	String PROCESS_DEF_ID = "processDefId_";

	// 是否子流程实例。
	String IS_SUB_PROCESS = "isSubProcess_";

	// 会签用户变量。
	String SIGN_USERIDS = "signUsers_";
	
	/**
	 * 流程会签结果。
	 */
	String SIGN_RESULT = "signResult_";
	
	/**
	 * 直接会签，需要手会签规则限制，
	 * 这个放到TaskFinishCmd的getTransitVars方法获取。
	 */
	String SIGN_DIRECT = "signDirect";

	/**
	 * 保存外部子流程变量。
	 */
	String CALL_ACTIVITI_VARS = "callActivityVars_";

	// 空用户。
	String EmptyUser = "0";

	/**
	 * 多实例常量。
	 */
	String MULTI_INSTANCE = "multiInstance";

	/**
	 * 多实例串行。
	 */
	String MULTI_INSTANCE_SEQUENTIAL = "sequential";

	/**
	 * 多实例并行。
	 */
	String MULTI_INSTANCE_PARALLEL = "parallel";
	/**
	 * 多实例的个数。
	 */
	String NUMBER_OF_INSTANCES = "nrOfInstances";
	/**
	 * 多实例活动的个数。
	 */
	String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
	/**
	 * 多实例完成的个数。
	 */
	String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";
	/**
	 * 流程实例顺序。
	 */
	String NUMBER_OF_LOOPCOUNTER = "loopCounter";

	/**
	 * 流程的EXECUTIONID。
	 */
	String BPMN_EXECUTION_ID = "bmpnExecutionId";

	/**
	 * 流程实例ID。
	 */
	String BPMN_INST_ID = "bmpnInstId";

	/**
	 * 流程KEY。
	 */
	String BPM_FLOW_KEY = "flowKey_";

	/**
	 * 表单标识。
	 */
	String BPM_FORM_IDENTITY = "form_identity_";

	/**
	 * 任务跳过类型，可以是相同执行人等等和跳过策略有关系。
	 */
	String BPM_SKIP_TYPE = "skipType_";

	/**
	 * 流程节点用户，用于存直接指定流程的人员。
	 */
	String BPM_NODE_USERS = "bpm_node_users_";
	
	/**
	 * 正常跳转 指定的流程人员
	 */
	String BPM_NEXT_NODE_USERS = "bpm_next_node_users_";

	/**
	 * 流程实例。
	 */
	String PROCESS_INST = "processInstance";

	/**
	 * local。
	 */
	String LOCAL = "local_";

	/**
	 * 流程任务。
	 */
	String BPM_TASK = "bpmTask_";
	/**
	 * 命名空间。
	 */
	String BPM_XMLNS = "http://www.jee-soft.cn/bpm";

	QName _Order_QNAME = new QName("http://www.jee-soft.cn/BPMN20EXT", "order");

	String TOKEN_NAME = "token_";

	String TOKEN_PRE = "T_";

	/**
	 * 回退模式。
	 */
	String BACK_HAND_MODE = "backHandMode";
	
	//系统用户Id(用于定时任务)
	String SYSTEM_USER_ID="-1";
	//系统用户(用于定时任务)
	String SYSTEM_USER_NAME="系统";
	
	
	/**
	 * bo实例
	 */
	String BO_INST = "bo_inst_";
	
	/**
	 * 父级堆栈ID。
	 */
	String PARENT_STACK="parentStack";
	
	/**
	 * 支持手机。
	 */
	String SUPPORT_MOBILE="supportMobile";
	/**
	 * 表单主版本
	 */
	String MAIN_VERSION="mainVersion";
	/**
	 * 流程发起时的表单版本
	 */
	String START_VERSION="startVersion";
	/**
	 * 流程启动时选择路径， （默认为跳过第一个节点，作为驳回时的发起人）
	 */
	String START_DESTINATION = "start_destiontion";
	
	/**
	 * 流程审批意见状态
	 */
	String BPM_OPINION_IS_DONE = "bpm_opinion_is_done";
	
	/**
	 * 解析人员的时候不每次请求uc。直接返回账号和id。解析完毕，批量请求
	 */
	String NOT_REQUEST_UC = "not_request_uc";
	
	/**
	 * 会签结果对象。分支条件判断会用到
	 */
	String TASK_SIGN_RESULT = "task_sign_result";
	
	/**
	 * 发起人组织id
	 */
	String START_ORG_ID = "start_org_id";

    /**
     * 催办人.
     */
    String PROMOTER = "promoter";

    /**
     * 被催办人.
     */
    String APPOINTEE = "appointee";
    
    /**
     * 前置cmd
     * 用户外部子流程结束后设置流程的targetNode
     */
	String PREVIOUS_CMD = "previous_cmd";

	/**
	 * 驳回到流程中的外部子流程节点 记录当前的节点
	 */
	String TARGET_NODE = "target_node";
	
	/**
	 * 记录上一个审批意见的id
	 */
	String PRE_BPM_CHECK_OPINION_ID = "pre_bpm_check_opinion_id";
	/**
	 * 状态
	 */
	String CUSTOM_SIGN_STATUS = "custom_sign_status";
	
	/**
	 * 会签串行创建任务时记录当前的bpmTask任务对象 
	 */
	String CREATE_BPM_TASK = "create_bpm_task";
	/**
	 * 审批意见的signtype 设置 当A 撤回An 时  signType 不是AfterSign  而是 BeforeSign
	 */
	String OPINION_SIGN_TYPE = "opinion_sign_type";
	
	/**
	 * 记录堆栈数据
	 */
	String RECORD_STACK = "record_stack";

	/**
	 * B 任务撤回标志
	 */
	String B_TASKS_REVOKE = "b_tasks_revoke";
	
	/**
	 * 需要跳过的节点
	 */
	String SKIP_NODE = "skip_node";
	
}
