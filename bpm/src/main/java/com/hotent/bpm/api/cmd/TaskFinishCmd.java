package com.hotent.bpm.api.cmd;

import com.hotent.bpm.api.constant.ActionType;



/**
 * 任务处理命令接口
 * @author csx
 *
 */
public interface TaskFinishCmd extends ActionCmd{
	
	
	/**
	 * 动作类型。
	 * @return 
	 * ActionType
	 */
	ActionType getActionType();
	
	
	/**
	 * 获取审批的意见
	 * @return
	 */
	String getApprovalOpinion();
	
	
	/**
	 * 流程实例ID。 
	 * @return		String
	 */
	String getBpmnInstId();
	
	/**
	 * 获取任务ID
	 * @return
	 */
	String getTaskId();
	
	/**
	 * 获取附件。
	 * <pre>
	 * 附件存放格式。
	 * [{fileId:"",name:""},{fileId:"",name:""}]
	 * </pre>
	 * @return  String
	 */
	String getFiles();

    /**
     * 获取正文。
     * <pre>
     * 正文存放格式。
     * [{fileId:"",name:""},{fileId:"",name:""}]
     * </pre>
     * @return  String
     */
    String getZfiles();
	
	/**
	 * 获取意见标识，如果不为空则表示这个意见为表单意见，这个值为表单名称。
	 * @return String
	 */
	String getOpinionIdentity();
	
	/**
	 * 是否干预。
	 * @return  boolean
	 */
	boolean isInterpose();
	
	/**
	 * 仅完成当前任务。
	 * @return  boolean
	 */
	boolean isOnlyFinishTask();
	
	/**
	 * 管理员干预意见
	 * @return
	 */
    String getInterPoseOpinion();

    //普通用户任务加签审批动作 agreeTrans（同意流转）opposeTrans（反对流转）
    String getAddSignAction();

    //普通用户任务加签后任务ID的父任务ID
    String getRejectTaskId();

    //普通用户任务加签后加签任务的任务ID
    String getAddSignTaskId();

}
