package com.hotent.portal.constant;

public class TemplateConstants {
	/**
	 * 
	 * <pre> 
	 * 描述：模板分类，对于数据表的 type_key_ 字段
	 * @author zhaoxy
	 * @company 广州宏天软件股份有限公司
	 * @email zhxy@jee-soft.cn
	 * @date 2018-06-06 14:20
	 * </pre>
	 */
	public final static class TYPE_KEY{
		/**任务创建**/
		public final static String TASK_CREATE="taskCreate";
		/**任务驳回**/
		public final static String TASK_BACK="taskBack";
		/**任务完成**/
		public final static String TASK_COMPLETE="taskComplete";
		/**流程结束**/
		public final static String PROCESS_END="processEnd";
		/**审批**/
		public final static String BPMN_APPROVAL="bpmnApproval";
		/**驳回**/
		public final static String BPMN_BACK="bpmnBack";
		/**撤销**/
		public final static String BPMN_RECOVER="bpmnRecover";
		/**通知代理**/
		public final static String BPMN_AGENT="bpmnAgent";
		/**通知委托人**/
		public final static String BPMN_DELEGATE="bpmnDelegate";
		/**流转通知任务**/
		public final static String BPMN_TASK_TRANS="bpmnTaskTrans";
		/**流转通知任务反馈意见**/
		public final static String BPM_TRANS_FEEDBACK="bpmTransFeedBack";
		/**流转任务取消*/
		public final static String BPM_TRANS_CANCEL="bpmTransCancel";
		/**沟通通知*/
		public final static String BPM_COMMU_SEND="bpmCommuSend";
		/**沟通反馈通知*/
		public final static String BPM_COMMU_FEEDBACK="bpmCommuFeedBack";
		/**转交通知*/
		public final static String BPM_HAND_TO="bpmHandTo";
		/**加签通知*/
		public final static String BPM_ADD_SIGN_TASK = "addSignTask";
		/**终止流程通知*/
		public final static String BPM_END_PROCESS  = "bpmEndProcess";
		/**任务取消*/
		public final static String BPM_TASK_CANCEL  = "taskCancel";
		/**流程抄送**/
		public final static String COPY_TO="copyTo";
	}
	
	/**
	 * 模版变量名称。
	 * <pre> 
	 * 构建组：x5-sys-api
	 * 作者：ray
	 * 邮箱:zhangyg@jee-soft.cn
	 * 日期:2014-9-23-下午11:11:34
	 * 版权：广州宏天软件有限公司版权所有
	 * </pre>
	 */
	public final class TEMP_VAR{
		public static final String BASE_URL="baseUrl";
		//流程实例标题
		public static final String INST_SUBJECT="instSubject";
		//流程实例ID
		public static final String INST_ID="instId";
		//节点名称
		public static final String NODE_NAME="nodeName";
		//任务标题
		public static final String TASK_SUBJECT="taskSubject";
		//任务ID
		public static final String TASK_ID="taskId";
		//原因
		public static final String CAUSE="cause";
		//委托人
		public static final String DELEGATE="delegate";
		//代理人
		public static final String AGENT="agent";
		
		public static final String RECEIVERID="receiverId";
		
		public static final String RECEIVER="receiver";
		
		public static final String 	SENDERID = "senderId";
		
		public static final String SENDER="sender";
		
	}
}
