package com.hotent.runtime.manager.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.bpm.api.plugin.core.execution.sign.SignResult;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.constant.VoteType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.exception.ApproveTaskException;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.runtime.constant.SignLineStatus;
import com.hotent.runtime.constant.SignSequenceStatus;
import com.hotent.runtime.manager.BpmTaskSignLineManager;
import com.hotent.runtime.manager.BpmTaskSignSequenceManager;
import com.hotent.runtime.manager.BpmTaskTransManager;
import com.hotent.runtime.manager.BpmTaskTransRecordManager;
import com.hotent.runtime.manager.BpmTransReceiverManager;
import com.hotent.runtime.manager.TaskTransService;
import com.hotent.runtime.model.BpmTaskSignLine;
import com.hotent.runtime.model.BpmTaskSignSequence;
import com.hotent.runtime.model.BpmTaskTrans;
import com.hotent.runtime.model.BpmTaskTransRecord;
import com.hotent.runtime.model.BpmTransReceiver;
import com.hotent.runtime.model.TaskTrans;
import com.hotent.runtime.params.RevokeSignLineParamObject;
import com.hotent.runtime.params.RevokeTransParamObject;
import com.hotent.runtime.params.TaskTransParamObject;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IUserService;

@Service
public class DefaultTaskTransService implements TaskTransService
{
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmTaskTransManager bpmTaskTransManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmTaskActionService bpmTaskActionService;
	@Resource
	BpmOpinionService bpmOpinionService;
	@Resource
	BpmTaskTransRecordManager bpmTaskTransRecordManager;
	@Resource
	BpmTransReceiverManager transReceiverManager;
	@Resource
	BpmTaskSignSequenceManager signSequenceManager;
	@Resource
	BpmTaskSignLineManager signLineManager;
	@Resource
	UCFeignService ucFeignService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;

    //更新 任务的意见
    private void updOpinionVue(String taskId, OpinionStatus opinionStatus,String trunsAction, String transUser, String opinion,String files,String zFiles){
        DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskId);
        //返回的情况,将原来checkOpinion 设置为awaiting_check，再次处理任务才可以更新意见
        if(BpmTaskTrans.SIGN_ACTION_BACK.equals(trunsAction)){
            checkOpinion.setStatus(OpinionStatus.AWAITING_CHECK.getKey());
        }//普通更新意见
        else{
            if(!(opinionStatus.getKey().equals(OpinionStatus.SIGN_PASS_CANCEL.getKey())||opinionStatus.getKey().equals(OpinionStatus.SIGN_NOPASS_CANCEL.getKey()))){
                IUser user = BpmUtil.getUser(transUser);
                checkOpinion.setAuditor(user.getUserId());
                checkOpinion.setAuditorName(user.getFullname());
                checkOpinion.setOpinion(opinion);
                checkOpinion.setFiles(files);
                checkOpinion.setZfiles(zFiles);
            }
            checkOpinion.setStatus(opinionStatus.getKey());
            checkOpinion.setCompleteTime(LocalDateTime.now());
            long durMs = TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime());
            checkOpinion.setDurMs(durMs);
        }

        bpmCheckOpinionManager.update(checkOpinion);
    }

    /**
     * 计算征询结果。
     *
     * @param bpmTaskTrans
     * @return SignResult
     */
    private SignResult calcVote(TaskTrans bpmTaskTrans){
        SignResult result = new SignResult();
        BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(bpmTaskTrans.getTaskId());
        Short totalAmount = transRecord.getTotalAmount();
        short agreeAmount = transRecord.getAgreeAmount();
        boolean isFinished = totalAmount == agreeAmount;
        if(isFinished){
            result.setComplete(true);
            result.setDecideType(DecideType.AGREE);
        }else{
            result.setComplete(false);
            result.setDecideType(DecideType.AGREE);
        }

        //修改流转记录中的数据
        updTransRecord((BpmTaskTrans)bpmTaskTrans);
        return result;
    }

    @Override
    @Transactional
    public void taskToInquReply(String taskId, String actionName, String notifyType, String opinion,boolean isIntervene,String files,String zFiles) throws Exception
    {
        List<DefaultBpmTask> list = getList(taskId);

        // 流转的那个任务,为意见归属
        DefaultBpmTask tranTask = bpmTaskManager.get(taskId);
        if(BeanUtils.isEmpty(tranTask)){
            throw new ApproveTaskException("当前任务已办理，不可重复办理，");
        }
        // 添加流转任务意见。
        OpinionStatus opinionStatus = DecideType.AGREE.getKey().equals(actionName) ? OpinionStatus.INQU_REPLY : OpinionStatus.TRANS_OPPOSE;

        updOpinionVue(taskId, opinionStatus, actionName,ContextUtil.getCurrentUser().getUserId(),opinion,files,zFiles);

        //设置任务流转记录相关数据
        BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(tranTask.getParentId());
        dealWithTransRecord(opinionStatus,transRecord,tranTask,opinion);


        bpmTaskManager.remove(taskId);
        for (int i = 0; i < list.size(); i++){
            DefaultBpmTask bpmTask = list.get(i);
            String id = bpmTask.getId();
            BpmTaskTrans taskTrans = bpmTaskTransManager.getByTaskId(id);

            updTaskTrans(actionName, taskTrans);

            bpmTaskTransManager.update(taskTrans);
            // 计算征询结果。
            SignResult result = calcVote(taskTrans);
            // 是否完成。
            if (result.isComplete()){
                OpinionStatus resultOpinionStatus = DecideType.AGREE.equals(result.getDecideType()) ? OpinionStatus.SIGN_PASS_CANCEL : OpinionStatus.SIGN_NOPASS_CANCEL;
                //未记录意见的流转者
                List<DefaultBpmTask> tasks =bpmTaskManager.getChildsByTaskId(bpmTask.getId());
                for (DefaultBpmTask task : tasks) {
                    updOpinionVue(BeanUtils.isEmpty(task.getTaskId())?task.getId():task.getTaskId(), resultOpinionStatus, actionName ,null ,DecideType.AGREE.getKey().equals(actionName)?"流转自动同意[系统]":"流转自动反对[系统]",files,zFiles);
                }

                boolean isStop = handComplete(taskTrans, bpmTask, list, result, i, opinion, notifyType, tranTask.getAssigneeId());
                if (isStop){
                    //如果返回则添加一条审批意见，否则不添加
                    if(BpmTaskTrans.SIGN_ACTION_BACK.equals(taskTrans.getAction())){
                        //bpmTask.setCreateTime(LocalDateTime.now());
                        addCheckOpinion(bpmTask, OpinionStatus.AWAITING_CHECK, "", "",false);
                    }
                    break;
                }
            }
            // 任务未完成直接结束。
            else{
                handNotComplete(bpmTask, taskTrans, notifyType, opinion);
                break;
            }
        }
    }

    /**
	 * 结束流转任务。
	 * 
	 * <pre>
	 * 	1.删除本任务。
	 *  2.发送通知给发送人。
	 * 	2.根据父任务ID修改票数，同意和反对的票数。
	 * 	3.判断流程是否完成。
	 * 		如果完成执行是否返回或或提交。
	 * 		如果是再派生的。
	 * 			1.如果为返回修改这个父任务状态为trans。
	 * 			2.如果提交则根据提交的结果，对父任务进行投票。
	 *  4.如果未完成那么判断流程是否是并行，如果是串行，那么取得下一个执行人，并产生任务。
	 * 
	 * </pre>
	 * 
	 * @param taskId
	 *            任务ID
	 * @param actionName
	 *            审批的意见同意或反对
	 * @param opinion
	 *            void
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void completeTask(String taskId, String actionName, String notifyType, String opinion,String addSignAction) throws Exception
	{
		List<DefaultBpmTask> list = getList(taskId);

		// 流转的那个任务,为意见归属
		DefaultBpmTask tranTask = bpmTaskManager.get(taskId);
		// 添加流转任务意见。
        OpinionStatus opinionStatus = null;

        if(StringUtil.isNotEmpty(addSignAction)){//判断是否是普通用户任务加签审批动作 agreeTrans（同意流转）opposeTrans（反对流转）
            opinionStatus = DecideType.AGREE.getKey().equals(actionName) ? OpinionStatus.ADDSIGN_AGREE : OpinionStatus.ADDSIGN_OPPOSE;
        }else{
            opinionStatus = DecideType.AGREE.getKey().equals(actionName) ? OpinionStatus.TRANS_AGREE : OpinionStatus.TRANS_OPPOSE;
        }
		if(TaskType.APPROVELINEED.getKey().equals(tranTask.getStatus())){
			opinionStatus = DecideType.AGREE.getKey().equals(actionName) ? OpinionStatus.APPROVE_LINEING_AGREE : OpinionStatus.APPROVE_LINEING_OPPOSE;
		}
		if(TaskType.SIGNSEQUENCEED.getKey().equals(tranTask.getStatus())){
			opinionStatus = DecideType.AGREE.getKey().equals(actionName) ? OpinionStatus.SIGNSEQUENCE_AGREE : OpinionStatus.SIGNSEQUENCE_OPPOSE;
		}
		if(TaskType.SIGNLINEED.getKey().equals(tranTask.getStatus())){
			opinionStatus = DecideType.AGREE.getKey().equals(actionName) ? OpinionStatus.SIGNLINE_AGREE : OpinionStatus.SIGNLINE_OPPOSE;
		}
		
		updOpinionComplete(taskId, opinionStatus, actionName,ContextUtil.getCurrentUser().getUserId(),opinion);
		
		bpmTaskManager.remove(taskId);
		if(TaskType.SIGNSEQUENCEED.getKey().equals(tranTask.getStatus())){
			// 顺序签署任务处理
			handleSignSequence(tranTask,taskId,actionName,notifyType,opinion);
			return;
		}
		
		if(TaskType.SIGNLINEED.getKey().equals(tranTask.getStatus())){
			// 并行签署任务
			handleSignLine(tranTask,taskId,actionName,notifyType,opinion);
			return;
		}
		
		//设置任务流转记录相关数据
		BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(tranTask.getParentId());
		dealWithTransRecord(opinionStatus,transRecord,tranTask,opinion);
		
		for (int i = 0; i < list.size(); i++){
			DefaultBpmTask bpmTask = list.get(i);
			String id = bpmTask.getId();
			BpmTaskTrans taskTrans = bpmTaskTransManager.getByTaskId(id);

			updTaskTrans(actionName, taskTrans);

			bpmTaskTransManager.update(taskTrans);
			// 计算投票结果。
			SignResult result = calcResult(taskTrans);
			// 是否完成。
			if (result.isComplete()){
				OpinionStatus resultOpinionStatus = DecideType.AGREE.equals(result.getDecideType()) ? OpinionStatus.SIGN_PASS_CANCEL : OpinionStatus.SIGN_NOPASS_CANCEL;
				//未记录意见的流转者
				List<DefaultBpmTask> tasks =bpmTaskManager.getChildsByTaskId(bpmTask.getId());
				for (DefaultBpmTask task : tasks) {
					updOpinionComplete(BeanUtils.isEmpty(task.getTaskId())?task.getId():task.getTaskId(), resultOpinionStatus, actionName ,null ,DecideType.AGREE.getKey().equals(actionName)?"流转自动同意[系统]":"流转自动反对[系统]");
				}
				
				boolean isStop = handComplete(taskTrans, bpmTask, list, result, i, opinion, notifyType, tranTask.getAssigneeId());
				if (isStop){
					//将流转结果作为意见记录
					//bpmTask.setCreateTime(LocalDateTime.now());
					//addCheckOpinion(bpmTask, resultOpinionStatus, "", "流转结束[系统]",true);
					
					//如果需要返回。将任务状态更新
					//如果不需要返回，则更新任务意见状态
					//updOpinionComplete(bpmTask, resultOpinionStatus,taskTrans.getAction());
					
					//如果返回则添加一条审批意见，否则不添加
					if(BpmTaskTrans.SIGN_ACTION_BACK.equals(taskTrans.getAction())){
						//bpmTask.setCreateTime(LocalDateTime.now());
						addCheckOpinion(bpmTask, OpinionStatus.AWAITING_CHECK, "", "",false);
					}
					break;
				}
			}
			// 任务未完成直接结束。
			else{
				handNotComplete(bpmTask, taskTrans, notifyType, opinion);
				break;
			}
		}
	}
	
	private void handleSignLine(DefaultBpmTask tranTask, String taskId,
			String actionName, String notifyType, String opinion) throws Exception {
		
		BpmTaskSignLine signLine = signLineManager.getByTaskId(taskId);
		signLine.setStatus(SignLineStatus.COMPLETE.getKey());
		signLineManager.update(signLine);
		
		// A任务处理
		handSignLineA(signLine,actionName,opinion,notifyType);
	}

	private void handSignLineA(BpmTaskSignLine signLine,String actionName,String opinion,String notifyType) throws Exception {
		String instanceId = signLine.getInstanceId();
		// 为流程引擎任务。
		String rootTaskId = signLine.getPath().split("\\.")[0];
		String status = SignSequenceStatus.INAPPROVAL.getKey();
		String nodeId = signLine.getNodeId();
		List<BpmTaskSignLine> signLines = signLineManager.getByInstNodeIdAndStatus(instanceId,rootTaskId,nodeId,status);
		if(BeanUtils.isEmpty(signLines)){
			
			if("submit".equals(signLine.getAction())){
				actionName = "agree";
				DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
				cmd.setTaskId(rootTaskId);
				cmd.setActionName(actionName);
				cmd.setApprovalOpinion(opinion);
				DefaultBpmTask defaultBpmTask = bpmTaskManager.get(rootTaskId);
				IUser user = BpmUtil.getUser(defaultBpmTask.getAssigneeId());
				UsernamePasswordAuthenticationToken usernamePwdAuth = new UsernamePasswordAuthenticationToken(user, null);
				SecurityContextHolder.getContext().setAuthentication(usernamePwdAuth);
				bpmTaskActionService.finishTask(cmd);
			}else{
				// back
				DefaultBpmTask defaultBpmTask = bpmTaskManager.get(rootTaskId);
				defaultBpmTask.setStatus(TaskType.NORMAL.getKey());
				bpmTaskManager.update(defaultBpmTask);
				//生成审批意见
				addTranCheckOpinion(defaultBpmTask, OpinionStatus.AWAITING_CHECK, defaultBpmTask.getAssigneeId(), "", "");
				// 发送通知。
				MessageUtil.notify(defaultBpmTask, opinion, BpmUtil.getUser(defaultBpmTask.getAssigneeId(), defaultBpmTask.getAssigneeName()), notifyType, TemplateConstants.TYPE_KEY.BPMN_TASK_SIGN_LINE);
			}
			/**
			 * 并行签署任务回到自身处理 需要清空记录 记录已经没有用了
			 */
			signLineManager.removeByInstIdNodeId(instanceId, rootTaskId, nodeId);
		}
		
	}

	/**
	 * 顺序签署同意处理
	 * @param tranTask
	 * @param taskId
	 * @param actionName
	 * @param notifyType
	 * @param opinion
	 * @throws Exception
	 */
	private void handleSignSequence(DefaultBpmTask tranTask, String taskId,
		String actionName, String notifyType, String opinion) throws Exception {
		// 获取下一执行人 
		Map<String, String> map = signSequenceManager.getNextExecutor(taskId);
		
		// 如果为空则完成当前任务
		if(BeanUtils.isEmpty(map)){
			DefaultBpmTask defaultBpmTask = bpmTaskManager.get(tranTask.getParentId());
			// 为流程引擎任务。
			actionName = "agree";
			DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
			cmd.setTaskId(tranTask.getParentId());
			cmd.setActionName(actionName);
			cmd.setApprovalOpinion(opinion);
			IUser user = BpmUtil.getUser(defaultBpmTask.getAssigneeId());
			UsernamePasswordAuthenticationToken usernamePwdAuth = new UsernamePasswordAuthenticationToken(user, null);
			SecurityContextHolder.getContext().setAuthentication(usernamePwdAuth);
			bpmTaskActionService.finishTask(cmd);
			// 清空顺序签署数据
			signSequenceManager.removeByInstNodeId(defaultBpmTask.getProcInstId(), tranTask.getParentId(), defaultBpmTask.getNodeId());
		}else{
			IUser user = BpmUtil.getUser(map.get("id"), map.get("name"));
			// 如果有签署执行人，创建新的虚拟任务
			DefaultBpmTask task = BpmUtil.convertTask(tranTask, tranTask.getParentId(), TaskType.SIGNSEQUENCEED, user);
			task.setId(map.get("taskId"));
			bpmTaskManager.create(task);
			//生成审批意见
			addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", tranTask.getParentId());
			// 发送通知。
			MessageUtil.notify(task, opinion, user, notifyType, TemplateConstants.TYPE_KEY.BPMN_TASK_SIGN_SEQUENCE);
		}
	}

	/**
	 * 获取任务列表。
	 * 
	 * @param taskId
	 * @return List&lt;DefaultBpmTask>
	 */
	private List<DefaultBpmTask> getList(String taskId){
		List<DefaultBpmTask> list = new ArrayList<DefaultBpmTask>();
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
		while (BeanUtils.isNotEmpty(bpmTask) && StringUtil.isNotZeroEmpty(bpmTask.getParentId())){
			String parentId = bpmTask.getParentId();
			bpmTask = bpmTaskManager.get(parentId);
			list.add(bpmTask);
		}
		return list;
	}

	/**
	 * 如果任务没有完成。 此任务是串行任务，则取出执行人产生串签任务。
	 * 
	 * @param parentTask
	 * @param bpmTaskTrans
	 *            void
	 * @throws Exception 
	 */
	private void handNotComplete(DefaultBpmTask parentTask, BpmTaskTrans bpmTaskTrans, String notifyType, String opinion) throws Exception{
		if (BpmTaskTrans.SIGN_TYPE_PARALLEL.equals(bpmTaskTrans.getSignType())) return;

		IUser user = bpmTaskTrans.getUserByIndex(bpmTaskTrans.getSeq());

		DefaultBpmTask task = BpmUtil.convertTask(parentTask, parentTask.getId(), TaskType.TRANSFORMED, user);
		bpmTaskManager.create(task);
		//生成审批意见
		addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTask.getId());

		// 发送通知。
		MessageUtil.notify(task, opinion, user, notifyType, TemplateConstants.TYPE_KEY.BPMN_TASK_TRANS);
	}

	/**
	 * 处理任务完成。
	 * 
	 * @param taskTrans
	 * @param bpmTask
	 * @param list
	 * @param result
	 * @param index
	 * @param transUser
	 *            此次流转处理人
	 * @return boolean
	 * @throws Exception 
	 */
	private boolean handComplete(TaskTrans taskTrans, DefaultBpmTask bpmTask, List<DefaultBpmTask> list,
			SignResult result, int index, String opinion, String notifyType, String transUser) throws Exception{
		boolean isStop = false;
		String taskId = bpmTask.getId();

		bpmTaskTransManager.remove(taskTrans.getId());
		// 删除子任务。把子任务也记录意见
		bpmTaskManager.delByParentId(taskId);
		
		// 如果任务完成，需要返回
		if (BpmTaskTrans.SIGN_ACTION_BACK.equals(taskTrans.getAction()))
		{
			// 更新任务 状态，普通任务，
			String status = bpmTask.isBpmnTask() ? TaskType.NORMAL.name() : TaskType.TRANSFORMED.name();
			bpmTask.setStatus(status);
            bpmTask.setCreateTime(LocalDateTime.now());
			bpmTaskManager.update(bpmTask);
			
			//更新流转记录状态
			BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(taskTrans.getTaskId());
			transRecord.setStatus((short)1);
			bpmTaskTransRecordManager.update(transRecord);

			// 发送通知。(反对时不发送通知)(暂不发送，没有消息模板)
			//IUser user = BpmUtil.getUser(bpmTask.getAssigneeId());
			//MessageUtil.notify(bpmTask, opinion, user, notifyType, TemplateConstants.TYPE_KEY.BPM_TRANS_FEEDBACK);
			

			isStop = true;
		}
		// 提交
		else{
			// 为流程引擎任务。
			if (bpmTask.isBpmnTask()){

				String actionName = result.getDecideType().equals(DecideType.AGREE) ? "agree" : "oppose";
				DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
				cmd.setTaskId(bpmTask.getId());
				cmd.setActionName(actionName);
				cmd.setApprovalOpinion(opinion);
				bpmTaskActionService.finishTask(cmd);
			}
			// 为流转任务
			else{
				// 删除任务ID,并更新下一个任务的状态数据。
				bpmTaskManager.remove(taskId);
				DefaultBpmTask nextBpmTask = list.get(index + 1);
				BpmTaskTrans nextTaskTrans = bpmTaskTransManager.getByTaskId(nextBpmTask.getId());
				changeVoteAmount(result, nextTaskTrans);
				bpmTaskTransManager.update(nextTaskTrans);

				// 发送反馈通知。
				IUser user = BpmUtil.getUser(nextBpmTask.getAssigneeId());
				MessageUtil.notify(nextBpmTask, opinion, user, notifyType, TemplateConstants.TYPE_KEY.BPM_TRANS_FEEDBACK);

				// 添加意见。
				OpinionStatus opinionStatus = result.getDecideType().equals(DecideType.AGREE) ? OpinionStatus.TRANS_AGREE : OpinionStatus.TRANS_OPPOSE;
				addCheckOpinion(bpmTask, opinionStatus, transUser, opinion,true);
			}
			
			//更新流转记录状态
			BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(taskTrans.getTaskId());
			transRecord.setStatus((short)1);
			bpmTaskTransRecordManager.update(transRecord);
		}
		return isStop;
	}
	
	//更新 任务的意见
	private void updOpinionComplete(String taskId, OpinionStatus opinionStatus,String trunsAction, String transUser, String opinion){
		DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskId);
		TaskFinishCmd cmd = (TaskFinishCmd) ContextThreadUtil.getActionCmd();
		//返回的情况,将原来checkOpinion 设置为awaiting_check，再次处理任务才可以更新意见
		if(BpmTaskTrans.SIGN_ACTION_BACK.equals(trunsAction)){
			checkOpinion.setStatus(OpinionStatus.AWAITING_CHECK.getKey());
		}//普通更新意见
		else{
			if(!(opinionStatus.getKey().equals(OpinionStatus.SIGN_PASS_CANCEL.getKey())||opinionStatus.getKey().equals(OpinionStatus.SIGN_NOPASS_CANCEL.getKey()))){
				IUser user = BpmUtil.getUser(transUser);
				checkOpinion.setAuditor(user.getUserId());
				checkOpinion.setAuditorName(user.getFullname());
				checkOpinion.setOpinion(opinion);
				checkOpinion.setFormName(cmd.getOpinionIdentity());
			}
			checkOpinion.setStatus(opinionStatus.getKey());
			checkOpinion.setCompleteTime(LocalDateTime.now());
			long durMs = TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime());
			checkOpinion.setDurMs(durMs);
		}
		if (BeanUtils.isNotEmpty(cmd.getBusData())) {
			try {
				checkOpinion.setFormData(Base64.getBase64(cmd.getBusData()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		bpmCheckOpinionManager.update(checkOpinion);
	}

	/**
	 * 修改流程票数。
	 * 
	 * @param actionName
	 * @param bpmTaskTrans
	 *            void
	 */
	private void updTaskTrans(String actionName, BpmTaskTrans bpmTaskTrans){
		short agreeAmount = bpmTaskTrans.getAgreeAmount();
		short oppseAmount = bpmTaskTrans.getOpposeAmount();
		if (OpinionStatus.AGREE.getKey().equals(actionName)){
			agreeAmount++;
		} 
		else{
			oppseAmount++;
		}
		bpmTaskTrans.setAgreeAmount(agreeAmount);
		bpmTaskTrans.setOpposeAmount(oppseAmount);

		if (BpmTaskTrans.SIGN_TYPE_SEQ.equals(bpmTaskTrans.getSignType())){
			short seq = (short) (bpmTaskTrans.getSeq() + 1);
			bpmTaskTrans.setSeq(seq);
		}
		//修改流转记录中的数据
		updTransRecord(bpmTaskTrans);
	}

	private void changeVoteAmount(SignResult result, BpmTaskTrans bpmTaskTrans){
		short agreeAmount = bpmTaskTrans.getAgreeAmount();
		short oppseAmount = bpmTaskTrans.getOpposeAmount();
		if (OpinionStatus.AGREE.getKey().equals(result.getDecideType().getKey())){
			agreeAmount++;
		} 
		else{
			oppseAmount++;
		}
		bpmTaskTrans.setAgreeAmount(agreeAmount);
		bpmTaskTrans.setOpposeAmount(oppseAmount);
	}

	/**
	 * 计算投票结果。
	 * 
	 * @param bpmTaskTrans
	 * @return SignResult
	 */
	private SignResult calcResult(TaskTrans bpmTaskTrans){
		SignResult result = new SignResult();
		int totalAmount = bpmTaskTrans.getTotalAmount();
		// 投票次数。
		short voteAmount = bpmTaskTrans.getVoteAmount();
		// 决策类型 agree,oppose
		String decideType = bpmTaskTrans.getDecideType();
		// 投票类型
		String voteType = bpmTaskTrans.getVoteType();

		short agreeAmount = bpmTaskTrans.getAgreeAmount();
		short oppseAmount = bpmTaskTrans.getOpposeAmount();

		boolean isFinished = totalAmount == agreeAmount + oppseAmount;

		// 投票决策方式为通过。
		if (DecideType.AGREE.getKey().equals(decideType)){
			if (VoteType.PERCENT.getKey().equals(voteType)){
				agreeAmount = (short) ((float) agreeAmount / totalAmount * 100);
			}
			// 如果投票完成，但是同意票数没有达到设定票数，则认为不通过。
			if (agreeAmount >= voteAmount){
				result.setComplete(true);
				result.setDecideType(DecideType.AGREE);
			} 
			else if (isFinished){
				result.setComplete(true);
				result.setDecideType(DecideType.REFUSE);
			}
		}
		// 决策方式不通过
		else{
			if (VoteType.PERCENT.getKey().equals(decideType)){
				oppseAmount = (short) ((float) oppseAmount / totalAmount * 100);
			}
			// 如果投票完成，但是同意票数没有达到设定票数，则认为不通过。
			if (oppseAmount >= voteAmount){
				result.setComplete(true);
				result.setDecideType(DecideType.REFUSE);
			} 
			else if (isFinished){
				result.setComplete(true);
				result.setDecideType(DecideType.AGREE);
			}
		}
		//修改流转记录中的数据
		updTransRecord((BpmTaskTrans)bpmTaskTrans);
		return result;
	}

	@Override
	@Transactional
	public void addTransTask(TaskTrans taskTrans, List<IUser> listUsers, String notifyType, String opinion,String formData,String files,Boolean addSignUser) throws Exception
	{
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskTrans.getTaskId());
        BpmTaskTrans trans = (BpmTaskTrans) taskTrans;

        if(addSignUser) {
            String parentTaskId = "";//加签任务的父任务ID
            //判断此任务是否普通用户任务加签后的任务
            if("0".equals(bpmTask.getParentId())){//0：是
                // 1.插入流转记录
                addTaskTrans(trans, bpmTask, listUsers);
                // 2.更新当前任务类型为流转类型。
                bpmTask.setStatus(TaskType.TRANSFORMING.name());
                bpmTaskManager.update(bpmTask);
                parentTaskId = trans.getTaskId();
            }else{//不是
                // 1.插入流转记录
                // 1.1 获取最初始没加签之前的任务详情
                DefaultBpmTask parentBpmTask = bpmTaskManager.get(bpmTask.getParentId());
                addTaskTrans(trans, parentBpmTask, listUsers);
                // 2.删除普通用户任务加签后的任务
                bpmTaskManager.remove(bpmTask);
                parentTaskId = bpmTask.getParentId();
            }
            // 3.添加加签任务
            addTaskByUsers(parentTaskId, bpmTask, listUsers, trans.getSignType(), opinion, notifyType,
                    TemplateConstants.TYPE_KEY.BPM_ADD_SIGN_TASK,addSignUser);
        }else{
            // 1.插入流转记录
            addTaskTrans(trans, bpmTask, listUsers);
            // 2.更新当前任务类型为流转类型。
            bpmTask.setStatus(TaskType.TRANSFORMING.name());
            bpmTaskManager.update(bpmTask);
            // 3.添加流转任务
            addTaskByUsers(trans.getTaskId(), bpmTask, listUsers, trans.getSignType(), opinion, notifyType,
                    TemplateConstants.TYPE_KEY.BPMN_TASK_TRANS,addSignUser);
        }

		DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(bpmTask.getId());
        if(addSignUser){
            //更新任务的审核意见状态为加签
            checkOpinion.setStatus(OpinionStatus.ADDSIGN.getKey());
        }else{
            //更新任务的审核意见状态为流转
            checkOpinion.setStatus(OpinionStatus.TRANS_FORMING.getKey());
        }

		checkOpinion.setOpinion(opinion);
		checkOpinion.setCompleteTime(LocalDateTime.now());
		checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
		checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
		checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
		checkOpinion.setFiles(files);
		if (StringUtil.isNotEmpty(formData)) {
			checkOpinion.setFormData(formData);
		}
		bpmCheckOpinionManager.update(checkOpinion);

		
	}

	/**
	 * 添加流转意见。
	 * 
	 * @param bpmTask
	 * @param opinionStatus
	 *            void
	 * @param transUser
	 *            处理流转任务的那个人
	 */
	private void addTranCheckOpinion(DefaultBpmTask bpmTask, OpinionStatus opinionStatus, String transUser, String opinion,String parentTaskId){
		String bpmnInstId = bpmTask.getBpmnInstId();
		String superInstId = (String) natProInstanceService.getSuperVariable(bpmnInstId, BpmConstants.PROCESS_INST_ID);

		// 如果是流转中的人添加意见，则办理人为那个人
		IUser user = BpmUtil.getUser(StringUtil.isEmpty(transUser) ? bpmTask.getAssigneeId() : transUser);

		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		identityList.add(bpmIdentity);

		DefaultBpmCheckOpinion checkOpinion = new DefaultBpmCheckOpinion();
		checkOpinion.setId(UniqueIdUtil.getSuid());
		checkOpinion.setProcDefId(bpmTask.getBpmnDefId());
		checkOpinion.setSupInstId(superInstId);
		checkOpinion.setProcInstId(bpmTask.getProcInstId());
		checkOpinion.setTaskId(BeanUtils.isEmpty(bpmTask.getTaskId())?bpmTask.getId():bpmTask.getTaskId());
		checkOpinion.setTaskKey(bpmTask.getNodeId());
		checkOpinion.setTaskName(bpmTask.getName());
		checkOpinion.setStatus(opinionStatus.getKey());
		checkOpinion.setCreateTime(bpmTask.getCreateTime()); 
		checkOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
		checkOpinion.setQualfiedNames(user.getFullname());
		if(StringUtil.isNotEmpty(parentTaskId)){
			checkOpinion.setParentTaskId(parentTaskId);
		}
		bpmCheckOpinionManager.create(checkOpinion);
	}

	/**
	 * 添加意见。
	 * 
	 * @param bpmTask
	 * @param opinionStatus
	 *            void
	 * @param transUser
	 *            处理流转任务的那个人
	 */
	@Override
	@Transactional
	public void addCheckOpinion(DefaultBpmTask bpmTask, OpinionStatus opinionStatus, String transUser,
			String opinion, boolean isCompleted){
		String bpmnInstId = bpmTask.getBpmnInstId();
		String superInstId = (String) natProInstanceService.getSuperVariable(bpmnInstId, BpmConstants.PROCESS_INST_ID);

		// 如果是流转中的人添加意见，则办理人为那个人
		IUser user = BpmUtil.getUser(StringUtil.isEmpty(transUser) ? bpmTask.getAssigneeId() : transUser);
		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		identityList.add(bpmIdentity);
		
		DefaultBpmCheckOpinion checkOpinion = new DefaultBpmCheckOpinion();
		checkOpinion.setId(UniqueIdUtil.getSuid());
		checkOpinion.setProcDefId(bpmTask.getBpmnDefId());
		checkOpinion.setSupInstId(superInstId);
		checkOpinion.setProcInstId(bpmTask.getProcInstId());
		checkOpinion.setTaskId(bpmTask.getTaskId());
		checkOpinion.setTaskKey(bpmTask.getNodeId());
		checkOpinion.setTaskName(bpmTask.getName());
		checkOpinion.setStatus(opinionStatus.getKey());
		
		//Date transDate =bpmTask.getTransDate()==null?bpmTask.getCreateTime():bpmTask.getTransDate();
		checkOpinion.setCreateTime(LocalDateTime.now());
		
		checkOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
		checkOpinion.setQualfiedNames(user.getFullname());

		if (isCompleted){
			checkOpinion.setAuditor(user.getUserId());
			checkOpinion.setAuditorName(user.getFullname());
			checkOpinion.setCompleteTime(LocalDateTime.now());
			checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis()-TimeUtil.getTimeMillis(bpmTask.getCreateTime()));
			checkOpinion.setOpinion(opinion);
		}
		bpmCheckOpinionManager.create(checkOpinion);
	}

	/**
	 * 添加流转任务。
	 * 
	 * @param parentTaskId
	 * @param bpmTask
	 * @param listUsers
	 *            void
	 * @throws Exception 
	 */
	private void addTaskByUsers(String parentTaskId, DefaultBpmTask bpmTask, List<IUser> listUsers, 
			String signType, String opinion, String notifyType, String typeKey,Boolean addSignUser) throws Exception{
		// 并行处理任务。
		if (BpmTaskTrans.SIGN_TYPE_PARALLEL.equals(signType)){
			for (IUser user : listUsers){
                DefaultBpmTask task = null;
			    if(addSignUser){
			        //普通任务加签
			        task = BpmUtil.convertTask(bpmTask, parentTaskId, TaskType.NORMAL, user);
                }else{
			        //流转任务
                    task = BpmUtil.convertTask(bpmTask, parentTaskId, TaskType.TRANSFORMED, user);
                }
				bpmTaskManager.create(task);
				//生成审批意见
				addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTaskId);
				// 发送通知
				MessageUtil.notify(task, opinion, user, notifyType, typeKey);
			}
		} 
		else{
			IUser user = listUsers.get(0);
			DefaultBpmTask task = BpmUtil.convertTask(bpmTask, parentTaskId, TaskType.TRANSFORMED, user);
			bpmTaskManager.create(task);
			//生成审批意见
			addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTaskId);
			// 发送通知。
			MessageUtil.notify(bpmTask, opinion, user, notifyType, typeKey);
		}

	}

	/**
	 * 添加流转配置。
	 * @throws IOException 
	 **/
	private void addTaskTrans(BpmTaskTrans trans, DefaultBpmTask bpmTask, List<IUser> users) throws IOException{
		IUser user = ContextUtil.getCurrentUser();

		//先删除之前由我创建的流转（防止多次流转问题）
		BpmTaskTrans oleTrans=bpmTaskTransManager.getByTaskId(bpmTask.getId());
		if(oleTrans!=null)
			bpmTaskTransManager.remove(oleTrans.getId());
		
		trans.setId(UniqueIdUtil.getSuid());
		trans.setInstanceId(bpmTask.getProcInstId());
		trans.setTaskId(bpmTask.getId());

		// 如果当前任务办理人、为空，则设置当前用户
		if (StringUtil.isZeroEmpty(bpmTask.getAssigneeId())){
			bpmTask.setAssigneeId(user.getUserId());
		}

		trans.setCreator(user.getFullname());
		trans.setCreateTime(LocalDateTime.now());
		trans.setTotalAmount((short) users.size());

		trans.setAgreeAmount((short) 0);
		trans.setOpposeAmount((short) 0);
		trans.setSeq((short) 0);
		short allowFormEdit = trans.getAllowFormEdit();
		trans.setAllowFormEdit(allowFormEdit);
		String signType = trans.getSignType();

		ArrayNode jArray = (ArrayNode) JsonUtil.toJsonNode(users);
		if (BpmTaskTrans.SIGN_TYPE_SEQ.equals(signType)){
			trans.setUserJson(jArray.toString());
		}
		bpmTaskTransManager.create(trans);
		//创建一条流转记录，记录流转
		createTransRecord(trans,bpmTask,users);
	}

	@Override
	public PageList<DefaultBpmTask> getMyTransTask(String userId, QueryFilter queryFilter){
		return (PageList<DefaultBpmTask>) bpmTaskManager.getMyTransTask(userId, queryFilter);
	}

	@Override
	@Transactional
	public void withDraw(String taskId, String notifyType, String opinion) throws Exception{
		toWithDraw(taskId, notifyType, opinion, TemplateConstants.TYPE_KEY.BPM_TRANS_CANCEL);
	}
	
	@Override
	@Transactional
	public void withDraw(String taskId, String notifyType, String opinion,
			String msgTemplate) throws Exception {
		toWithDraw(taskId, notifyType, opinion, msgTemplate);
	}
	
	private void toWithDraw(String taskId, String notifyType, String opinion,
			String msgTemplate) throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
		
		//更新审批历史状态
		List<DefaultBpmCheckOpinion> opinions = bpmCheckOpinionManager.getByInstNodeId(bpmTask.getProcInstId(), bpmTask.getNodeId());
		for(DefaultBpmCheckOpinion checkOpinion:opinions){
			if(checkOpinion.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey())){
				checkOpinion.setStatus(OpinionStatus.SIGN_RECOVER_CANCEL.getKey());
				checkOpinion.setCompleteTime(LocalDateTime.now());
				long durMs = (TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
				checkOpinion.setDurMs(durMs);
				checkOpinion.setOpinion(opinion);
				checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
				checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
				bpmCheckOpinionManager.update(checkOpinion);
			}
		}
		
		// 增加撤消审批意见记录
		//addCheckOpinion(bpmTask, OpinionStatus.TRANS_REVOKER, "", opinion,true);

		// 添加撤消回到原任务的待审批意见记录
		addCheckOpinion(bpmTask, OpinionStatus.AWAITING_CHECK, "", opinion,false);
		// 更新当前任务状态。
		bpmTask.setStatus(bpmTask.isBpmnTask() ? TaskType.NORMAL.name() : TaskType.TRANSFORMED.name());
		bpmTaskManager.update(bpmTask);
		
		//更新流转记录数据
		BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(bpmTask.getTaskId());
		transRecord.setStatus((short)2);//撤销状态
		bpmTaskTransRecordManager.update(transRecord);

		IUserService userService = AppUtil.getBean(IUserService.class);
		// 获取该任务的子任务。
		List<DefaultBpmTask> childTasks = bpmTaskManager.getChildsByTaskId(taskId);
		// 发送通知。
		for (DefaultBpmTask task : childTasks){
			IUser receiver = userService.getUserById(task.getAssigneeId());
			// 发送通知。
			MessageUtil.notify(task, opinion, receiver, notifyType, msgTemplate);
			// 删除
			bpmTaskManager.remove(task.getId());
		}
	}

	

	class SignResult{
		boolean isComplete = false;
		DecideType decideType = DecideType.AGREE;

		public SignResult()
		{
		}

		public SignResult(boolean isComplete, DecideType decideType){
			this.decideType = decideType;
			this.isComplete = isComplete;
		}

		public boolean isComplete(){
			return isComplete;
		}

		public void setComplete(boolean isComplete){
			this.isComplete = isComplete;
		}

		public DecideType getDecideType(){
			return decideType;
		}

		public void setDecideType(DecideType decideType){
			this.decideType = decideType;
		}

	}

	/*
	 * 获取流转信息实体
	 */
	@Override
	public BpmTaskTrans getTransTaskByTaskId(String taskId)
	{
		return bpmTaskTransManager.getByTaskId(taskId);
	}
	
	/**
	 * 新增一条流转记录，用于记录流转任务
	 * @param taskTrans 流转任务（流转源）位于bpm_task_trans中
	 * @param bpmTask bpm_task
	 * @param users 流转人员
	 */
	@Transactional
	public void createTransRecord(BpmTaskTrans taskTrans, DefaultBpmTask bpmTask, List<IUser> users){
	    //先删除之前由我创建的流转（防止多次流转问题）
        BpmTaskTransRecord bpmTaskTransRecord = bpmTaskTransRecordManager.getByTaskId(bpmTask.getId());
        if(bpmTaskTransRecord!=null) {
            bpmTaskTransRecordManager.remove(bpmTaskTransRecord.getId());
        }
		IUser user = ContextUtil.getCurrentUser();
		BpmTaskTransRecord transRecord = new BpmTaskTransRecord();
		transRecord.setId(UniqueIdUtil.getSuid());
		transRecord.setCreator(user.getFullname());
		transRecord.setTaskName(bpmTask.getName());
		transRecord.setTaskSubject(bpmTask.getSubject());
		transRecord.setStatus((short)0);//流转中
		transRecord.setTransOwner(bpmTask.getAssigneeId());//流转任务所属人
		transRecord.setTransTime(LocalDateTime.now());
		transRecord.setDefName(bpmTask.getProcDefName());
		transRecord.setProcInstId(bpmTask.getProcInstId());
		//复制流转任务中的一些数据
		transRecord.setAction(taskTrans.getAction());
		transRecord.setAgreeAmount(taskTrans.getAgreeAmount());
		transRecord.setDecideType(taskTrans.getDecideType());
		transRecord.setVoteAmount(taskTrans.getVoteAmount());
		transRecord.setVoteType(taskTrans.getVoteType());
		transRecord.setSignType(taskTrans.getSignType());
		transRecord.setTotalAmount(taskTrans.getTotalAmount());
		transRecord.setAgreeAmount(taskTrans.getAgreeAmount());
		transRecord.setOpposeAmount(taskTrans.getOpposeAmount());
		transRecord.setTaskId(taskTrans.getTaskId());
		String transUsers = "";
		String transUserIds = "";
		for(IUser u : users){
			transUsers = transUsers +"【"+u.getFullname()+"】 ";
			transUserIds = transUserIds +u.getUserId() +",";
			//接收人
			BpmTransReceiver transReceiver = new BpmTransReceiver();
			transReceiver.setId(UniqueIdUtil.getSuid());
			transReceiver.setTransRecordid(transRecord.getId());
			transReceiver.setReceiver(u.getFullname());
			transReceiver.setReceiverId(u.getUserId());
			transReceiver.setStatus((short)0);
			transReceiver.setCheckType((short)0);//尚未审批
			transReceiver.setReceiverTime(LocalDateTime.now());
			transReceiverManager.create(transReceiver);
		}
		transUserIds.substring(0, transUserIds.length()-1);
		transRecord.setTransUsers(transUsers);
		transRecord.setTransUserIds(transUserIds);
		bpmTaskTransRecordManager.create(transRecord);
	}
	
	@Transactional
	public void updTransRecord(BpmTaskTrans taskTrans){
		BpmTaskTransRecord transRecord = bpmTaskTransRecordManager.getByTaskId(taskTrans.getTaskId());
		transRecord.setAgreeAmount(taskTrans.getAgreeAmount());
		transRecord.setDecideType(taskTrans.getDecideType());
		transRecord.setVoteAmount(taskTrans.getVoteAmount());
		transRecord.setVoteType(taskTrans.getVoteType());
		transRecord.setSignType(taskTrans.getSignType());
		transRecord.setTotalAmount(taskTrans.getTotalAmount());
		transRecord.setAgreeAmount(taskTrans.getAgreeAmount());
		transRecord.setOpposeAmount(taskTrans.getOpposeAmount());
		bpmTaskTransRecordManager.update(transRecord);
	}
	
	/**
	 * 设置任务流转记录相关数据
	 * @param opinionStatus
	 * @param transRecord
	 * @param tranTask
	 * @param opinion
	 */
	@Transactional
	public void dealWithTransRecord(OpinionStatus opinionStatus,BpmTaskTransRecord transRecord,DefaultBpmTask tranTask,String opinion){
		String transOpinion = transRecord.getTransOpinion();
		IUser user = BpmUtil.getUser(tranTask.getAssigneeId());
		if(BeanUtils.isEmpty(transOpinion)){
			transOpinion = "";
		}
		//
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", tranTask.getOwnerId());  //  由于原来的流转的任务更改了处理人 导致   transReceiver 为 null
		params.put("transRecordid", transRecord.getId());
		BpmTransReceiver transReceiver = transReceiverManager.getByTransRecordAndUserId(params);
		transReceiver.setCheckTime(LocalDateTime.now());
		transReceiver.setOpinion(opinion);
		transReceiver.setStatus((short)1);//已审核
		
		if(OpinionStatus.TRANS_AGREE.getKey().equals(opinionStatus)){//同意流转
			transOpinion +="【"+user.getFullname()+"】：同意；";
			transReceiver.setCheckType((short)1);
		}else if(OpinionStatus.TRANS_OPPOSE.getKey().equals(opinionStatus)){//反对流转
			transOpinion +="【"+user.getFullname()+"】：反对；";
			transReceiver.setCheckType((short)2);
		}else if(OpinionStatus.ADDSIGN_AGREE.getKey().equals(opinionStatus)){//同意加签
            transOpinion +="【"+user.getFullname()+"】：同意；";
            transReceiver.setCheckType((short)1);
        }else if(OpinionStatus.ADDSIGN_OPPOSE.getKey().equals(opinionStatus)){//反对加签
            transOpinion +="【"+user.getFullname()+"】：反对；";
            transReceiver.setCheckType((short)2);
        }
		transRecord.setTransOpinion(transOpinion);
		bpmTaskTransRecordManager.update(transRecord);
		transReceiverManager.update(transReceiver);
	}

	@Override
	public CommonResult<String> addSign(TaskTransParamObject taskTransParamObject) throws Exception
	{
		return null;
		
	}


    @Override
    @Transactional
    public void addTaskToInqu(TaskTrans taskTrans, List<IUser> listUsers, String notifyType, String opinion, String files) throws Exception
    {
        DefaultBpmTask bpmTask = bpmTaskManager.get(taskTrans.getTaskId());

        BpmTaskTrans trans = (BpmTaskTrans) taskTrans;
        // 插入流转记录
        addTaskTrans(trans, bpmTask, listUsers);
        // 更新当前任务类型为征询类型。
        bpmTask.setStatus(TaskType.TRANSFORMING.name());
        bpmTaskManager.update(bpmTask);
        // 添加征询任务
        addTaskInquByUsers(trans.getTaskId(), bpmTask, listUsers, opinion,null,null);
        // 更新任务的审核意见状态为流转中。
        DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskTrans.getTaskId());
        if(BeanUtils.isEmpty(checkOpinion)){
            DefaultBpmCheckOpinion defaultBpmCheckOpinion = new DefaultBpmCheckOpinion();
            defaultBpmCheckOpinion.setId(UniqueIdUtil.getSuid());
            defaultBpmCheckOpinion.setProcDefId(bpmTask.getBpmnDefId());
            defaultBpmCheckOpinion.setProcInstId(bpmTask.getProcInstId());
            defaultBpmCheckOpinion.setTaskId(BeanUtils.isEmpty(bpmTask.getTaskId())?bpmTask.getId():bpmTask.getTaskId());
            defaultBpmCheckOpinion.setTaskKey(bpmTask.getNodeId());
            defaultBpmCheckOpinion.setTaskName(bpmTask.getName());
            defaultBpmCheckOpinion.setStatus(OpinionStatus.INQU.getKey());
            defaultBpmCheckOpinion.setCreateTime(bpmTask.getCreateTime());
            defaultBpmCheckOpinion.setOpinion(opinion);
            defaultBpmCheckOpinion.setCompleteTime(LocalDateTime.now());
            defaultBpmCheckOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(defaultBpmCheckOpinion.getCreateTime()));
            defaultBpmCheckOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
            defaultBpmCheckOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
            defaultBpmCheckOpinion.setFiles(files);
            bpmCheckOpinionManager.create(defaultBpmCheckOpinion);
        }else{
            checkOpinion.setStatus(OpinionStatus.INQU.getKey());
            checkOpinion.setOpinion(opinion);
            checkOpinion.setCompleteTime(LocalDateTime.now());
            checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
            checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
            checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
            checkOpinion.setFiles(files);
            bpmCheckOpinionManager.update(checkOpinion);
        }
    }

    /**
     * 添加征询任务。
     *
     * @param parentTaskId
     * @param bpmTask
     * @param listUsers
     *            void
     * @throws Exception
     */
    private void addTaskInquByUsers(String parentTaskId, DefaultBpmTask bpmTask, List<IUser> listUsers, String opinion ,TaskType taskType,String type) throws Exception{
        if (BeanUtils.isEmpty(type)) {
            type = "inqu";
        }

        if (BeanUtils.isEmpty(taskType)) {
            taskType = TaskType.TRANSFORMEDINQU;
        }
        for (IUser user : listUsers){
            DefaultBpmTask task = BpmUtil.convertTask(bpmTask, parentTaskId, taskType, user);
            TaskEntity   delegateTask = new TaskEntity();
            delegateTask.setId(task.getId());
            delegateTask.setAssignee(task.getAssigneeId());
            delegateTask.setOwner(task.getOwnerId());
            delegateTask.setTaskDefinitionKey(task.getNodeId());
            delegateTask.setProcessDefinitionId(task.getBpmnDefId());
            delegateTask.setName(task.getName());
            delegateTask.setProcessInstanceId(task.getBpmnInstId());
            delegateTask.setSuspensionState(task.getSuspendState());
            delegateTask.setPriority(task.getPriority().intValue());
            delegateTask.setCreateTime(new Date());
            //TaskService taskService = AppUtil.getBean(TaskService.class);
            //taskService.saveTask(delegateTask);
            task.setTaskId(delegateTask.getId());
            task.setCreatorId(ContextUtil.getCurrentUserId());
            bpmTaskManager.create(task);
            //生成审批意见
            addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTaskId);
        }
    }

	@Override
	@Transactional
	public void addApproveLineTask(TaskTrans taskTrans, List<IUser> listUsers, String notifyType, String opinion)
			throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskTrans.getTaskId());

		BpmTaskTrans trans = (BpmTaskTrans) taskTrans;
		// 插入流转记录
		addTaskTrans(trans, bpmTask, listUsers);
		// 更新当前任务类型为并行审批类型。
		bpmTask.setStatus(TaskType.APPROVELINEING.name());
		bpmTaskManager.update(bpmTask);
		// 添加流转任务
		addApproveLineTaskByUsers(trans.getTaskId(), bpmTask, listUsers, trans.getSignType(), opinion, notifyType, 
				TemplateConstants.TYPE_KEY.BPMN_TASK_APPROVE_LINE);

		// 更新任务的审核意见状态为流转中。
		DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskTrans.getTaskId());
		checkOpinion.setStatus(OpinionStatus.APPROVE_LINEING.getKey());
		checkOpinion.setOpinion(opinion);
		checkOpinion.setCompleteTime(LocalDateTime.now());
		checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
		checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
		checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
		bpmCheckOpinionManager.update(checkOpinion);
	}
	
	/**
	 * 添加并行审批任务。
	 * 
	 * @param parentTaskId
	 * @param bpmTask
	 * @param listUsers
	 *            void
	 * @throws Exception 
	 */
	private void addApproveLineTaskByUsers(String parentTaskId, DefaultBpmTask bpmTask, List<IUser> listUsers, 
			String signType, String opinion, String notifyType, String typeKey) throws Exception{
		// 并行处理任务。
		if (BpmTaskTrans.SIGN_TYPE_PARALLEL.equals(signType)){
			for (IUser user : listUsers){
				DefaultBpmTask task = BpmUtil.convertTask(bpmTask, parentTaskId, TaskType.APPROVELINEED, user);
				bpmTaskManager.create(task);
				//生成审批意见
				addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTaskId);
				// 发送通知
				MessageUtil.notify(task, opinion, user, notifyType, typeKey);
			}
		} 
		else{
			IUser user = listUsers.get(0);
			DefaultBpmTask task = BpmUtil.convertTask(bpmTask, parentTaskId, TaskType.APPROVELINEED, user);
			bpmTaskManager.create(task);
			//生成审批意见
			addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTaskId);
			// 发送通知。
			MessageUtil.notify(bpmTask, opinion, user, notifyType, typeKey);
		}

	}
	
	/**
	 * 添加顺序签署任务 (基于流转功能)
	 */
	@Override
	@Transactional
	public void addSignSequenceTask(BpmTaskTrans taskTrans,
			List<IUser> userList, String notifyType, String opinion) throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskTrans.getTaskId());
		
		BpmTaskTrans trans = (BpmTaskTrans) taskTrans;
		// 插入流转记录
		// addTaskTrans(trans, bpmTask, userList);
		// 更新当前任务类型为顺序签署源任务
		bpmTask.setStatus(TaskType.SIGNSEQUENCEING.name());
		bpmTaskManager.update(bpmTask);
		// 添加流转任务
		addSignSequenceTaskByUsers(trans.getTaskId(), bpmTask, userList, trans.getSignType(), opinion, notifyType, 
				TemplateConstants.TYPE_KEY.BPMN_TASK_SIGN_SEQUENCE);

		// 更新任务的审核意见状态为流转中。
		DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskTrans.getTaskId());
		checkOpinion.setStatus(OpinionStatus.SIGNSEQUENCEING.getKey());
		checkOpinion.setOpinion(opinion);
		checkOpinion.setCompleteTime(LocalDateTime.now());
		checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
		checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
		checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
		bpmCheckOpinionManager.update(checkOpinion);
	}
	
	/**
	 * 添加并行审批任务。
	 * 
	 * @param parentTaskId
	 * @param bpmTask
	 * @param listUsers
	 *            void
	 * @throws Exception 
	 */
	private void addSignSequenceTaskByUsers(String parentTaskId, DefaultBpmTask bpmTask, List<IUser> listUsers, 
			String signType, String opinion, String notifyType, String typeKey) throws Exception{
		IUser user = listUsers.get(0);
		String parentId = bpmTask.getParentId();
		if("0".equals(parentId)){
			parentId = bpmTask.getId();
		}
		// 二次顺签时 需要删除任务
		if(!bpmTask.isBpmnTask()){
			bpmTaskManager.remove(bpmTask.getId());
		}
		DefaultBpmTask task = BpmUtil.convertTask(bpmTask, parentId, TaskType.SIGNSEQUENCEED, user);
		bpmTaskManager.create(task);
		//生成审批意见
		addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "",task.getParentId());
		// 发送通知。
		MessageUtil.notify(bpmTask, opinion, user, notifyType, typeKey);
		String newTaskId = task.getId();
		BpmTaskSignSequence signSequence = signSequenceManager.getByTaskId(bpmTask.getId());
		String path = bpmTask.getId();
		if(BeanUtils.isNotEmpty(signSequence)){
			path = signSequence.getPath() + StringPool.DOT + path;
			signSequence.setStatus(SignSequenceStatus.HALF.getKey());
			signSequence.setNextTaskId(task.getId());
			signSequenceManager.update(signSequence);
		}
		// 顺序签署人员记录
		for (int i = 0; i < listUsers.size(); i++) {
			BpmTaskSignSequence entity = new BpmTaskSignSequence();
			parentId = bpmTask.getId();
			entity.setParentId(parentId);
			entity.setInstanceId(bpmTask.getProcInstId());
			Map<String,String> userMap = new HashMap<String, String>();
			// {id:"1",type:"user",name:"超级管理员"}
			userMap.put("id", listUsers.get(i).getUserId());
			userMap.put("type","user");
			userMap.put("name", listUsers.get(i).getFullname());
			entity.setExecutor(JsonUtil.toJson(userMap));
			String status = SignSequenceStatus.WAITINGFORGENERATIONSIGNATURETASK.getKey();
			if(i==0){
				entity.setTaskId(newTaskId);
				status = SignSequenceStatus.INAPPROVAL.getKey();
			}
			entity.setStatus(status);
			entity.setSeq((short) i);
			entity.setPath(path);
			entity.setNodeId(bpmTask.getNodeId());
			signSequenceManager.create(entity);
		}

	}

	@Override
	@Transactional
	public void addRevokeTask(DefaultBpmTask bpmTask, IUser user,String messageType) throws Exception {
		//更新流转记录中的处理信息
		BpmTaskTransRecord taskTransRecord = bpmTaskTransRecordManager.getByTaskId(bpmTask.getTaskId());
		if(BeanUtils.isEmpty(taskTransRecord)){
			throw new WorkFlowException("未找到流转记录！");
		}
		//非流转中的不允许撤回
		if(!taskTransRecord.getStatus().equals((short)0)){
			throw new WorkFlowException("当前节点已不支持撤回！");
		}
		taskTransRecord.setAgreeAmount((short) (taskTransRecord.getAgreeAmount()-1));
		bpmTaskTransRecordManager.update(taskTransRecord);
		BpmTaskTrans trans = bpmTaskTransManager.getByTaskId(bpmTask.getTaskId());
		trans.setAgreeAmount((short) (trans.getAgreeAmount()-1));
		bpmTaskTransManager.update(trans);
		//更新流转接收人相关信息
		Map<String, String> params = new HashMap<String,String>();
		params.put("transRecordid", taskTransRecord.getId());
		params.put("userId", user.getUserId());
		BpmTransReceiver receiver = transReceiverManager.getByTransRecordAndUserId(params);
		receiver.setStatus(Short.valueOf("0"));
		receiver.setOpinion(null);
		receiver.setCheckTime(null);
		transReceiverManager.update(receiver);
		//创建新的流转任务
		DefaultBpmTask task = BpmUtil.convertTask(bpmTask, bpmTask.getTaskId(), TaskType.APPROVELINEED, user);
		bpmTaskManager.create(task);
		//生成审批意见
		addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", bpmTask.getTaskId());
		// 发送通知
		MessageUtil.notify(task, "", user, messageType, TemplateConstants.TYPE_KEY.BPMN_TASK_APPROVE_LINE);
	}

	@Override
	@Transactional
	public void revokeSignSequence(RevokeTransParamObject revokeTransParamObject) throws Exception {
		BpmTaskSignSequence revokeTaskSignSeq = signSequenceManager.getByTaskId(revokeTransParamObject.getTaskId());
		String rootTaskId = revokeTransParamObject.getTaskId();
		if(BeanUtils.isNotEmpty(revokeTaskSignSeq)) {
			rootTaskId = revokeTaskSignSeq.getPath().split("\\.")[0];
		}
		BpmTaskSignSequence inApproval = signSequenceManager.getInApprovalByInstNodeId(revokeTransParamObject.getInstanceId(),rootTaskId,revokeTransParamObject.getNodeId());
		String revokeTaskId = revokeTransParamObject.getTaskId();
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(revokeTransParamObject.getInstanceId());
		DefaultBpmDefinition def = bpmDefinitionManager.getById(instance.getProcDefId());
		// A 撤回 
		if(BeanUtils.isEmpty(revokeTaskSignSeq)){
			// 撤回第一个任务  A1 审批中 inApproval 
			if(BeanUtils.isNotEmpty(inApproval) &&
			   revokeTaskId.equals(inApproval.getParentId()) && 
			   0 == inApproval.getSeq() && 
			   ("true".equals(def.getIsReadRevoke()) || inApproval.isNotRead())){
				revokeSignSequenceA(inApproval,revokeTransParamObject,revokeTaskId);
				return ;
			}
			//  撤回第一个任务的下级任务 A11 A12  A121 审批中 inApproval
			if(BeanUtils.isNotEmpty(inApproval) &&
					inApproval.getPath().contains(".") &&
					("true".equals(def.getIsReadRevoke()) || inApproval.isNotRead())){
				String firstTaskId = inApproval.getPath().split("\\.")[1];
				BpmTaskSignSequence firstTaskSignSeq = signSequenceManager.getByTaskId(firstTaskId);
				if(BeanUtils.isNotEmpty(firstTaskSignSeq) && 0 == firstTaskSignSeq.getSeq()){
					revokeSignSequenceA(inApproval,revokeTransParamObject,revokeTaskId);
					return ;
				}
			}
		}else{
			String nextTaskId = revokeTaskSignSeq.getNextTaskId();
			revokeTaskSignSeq.setStatus(SignSequenceStatus.INAPPROVAL.getKey());
			revokeTaskSignSeq.setNextTaskId("");
			revokeTaskSignSeq.setTaskId(UniqueIdUtil.getSuid());
			signSequenceManager.update(revokeTaskSignSeq);
			// A1 撤回
			// 撤回下级任务 A11 A12 A121 审批中  
			if(BeanUtils.isNotEmpty(inApproval) &&
					inApproval.getPath().contains(revokeTaskSignSeq.getPath()+"."+revokeTaskId) &&
					("true".equals(def.getIsReadRevoke()) || inApproval.isNotRead())){
				createSignSequenceTask(revokeTransParamObject,revokeTaskSignSeq,inApproval.getTaskId());
				updateInApprovalOpinion(inApproval.getTaskId(),revokeTransParamObject.getCause());
				signSequenceManager.removeByPath(revokeTaskSignSeq.getPath()+"."+revokeTaskId);
				return ;
			}
			// 撤回同级任务 A2
			if(BeanUtils.isNotEmpty(inApproval) &&  
					inApproval.getPath().equals(revokeTaskSignSeq.getPath()) &&
					inApproval.getTaskId().equals(nextTaskId) &&
					("true".equals(def.getIsReadRevoke()) || inApproval.isNotRead())){
				inApproval.setStatus(SignSequenceStatus.WAITINGFORGENERATIONSIGNATURETASK.getKey());
				String inApprovalTaskId = inApproval.getTaskId();
				inApproval.setTaskId("");
				signSequenceManager.update(inApproval);
				createSignSequenceTask(revokeTransParamObject,revokeTaskSignSeq,inApprovalTaskId);
				// 更新任务的审核意见状态为revokeTransParamObject.getCause(。
				updateInApprovalOpinion(inApprovalTaskId,revokeTransParamObject.getCause());
				return ;
			}
			
		}
		throw new WorkFlowException("当前节点已不支持撤回！");
	}
	
	private void revokeSignSequenceA(BpmTaskSignSequence inApproval,
			RevokeTransParamObject revokeTransParamObject,String taskId) {
		String rootTaskId = inApproval.getPath().split("\\.")[0];
		// inApproval
		bpmTaskManager.remove(inApproval.getTaskId());
		updateInApprovalOpinion(inApproval.getTaskId(),revokeTransParamObject.getCause());
		signSequenceManager.removeByInstNodeId(inApproval.getInstanceId(),rootTaskId,inApproval.getNodeId());
		DefaultBpmTask defaultBpmTask = bpmTaskManager.get(taskId);
		defaultBpmTask.setStatus(TaskType.NORMAL.getKey());
		bpmTaskManager.update(defaultBpmTask);
		//生成审批意见
		addTranCheckOpinion(defaultBpmTask, OpinionStatus.AWAITING_CHECK, ContextUtil.getCurrentUserId(), "", defaultBpmTask.getId());
		
	}

	/**
	 * 根据将被撤回的任务产生新的任务作为撤回的任务
	 * @param revokeTransParamObject
	 * @param revokeSignSeq
	 * @param inApprovalTaskId
	 * @throws Exception
	 */
	private void createSignSequenceTask(RevokeTransParamObject revokeTransParamObject, BpmTaskSignSequence revokeSignSeq, String inApprovalTaskId) throws Exception {
		DefaultBpmTask defaultBpmTask = bpmTaskManager.get(inApprovalTaskId);
		String executor = revokeSignSeq.getExecutor();
		JsonNode jsonNode = JsonUtil.toJsonNode(executor);
		IUser user = BpmUtil.getUser(jsonNode.get("id").asText(), jsonNode.get("name").asText());
		DefaultBpmTask convertTask = BpmUtil.convertTask(defaultBpmTask, defaultBpmTask.getParentId(), TaskType.SIGNSEQUENCEED, user);
		bpmTaskManager.remove(defaultBpmTask.getId());
		convertTask.setId(revokeSignSeq.getTaskId());
		bpmTaskManager.create(convertTask);
		//生成审批意见
		addTranCheckOpinion(convertTask, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", convertTask.getParentId());
		// 发送通知。
		MessageUtil.notify(convertTask, revokeTransParamObject.getCause(), user, revokeTransParamObject.getMessageType(), TemplateConstants.TYPE_KEY.BPMN_TASK_SIGN_SEQUENCE);
	}

	private void updateInApprovalOpinion(String taskId,String opinion) {
		updateInApprovalOpinion(taskId,opinion,OpinionStatus.SIGN_RECOVER_CANCEL.getKey());
	}
	
	private void updateInApprovalOpinion(String taskId,String opinion,String status) {
		DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskId);
		if(BeanUtils.isEmpty(checkOpinion)) {
			throw new BaseException("撤回的任务已经被处理，请刷新页面重新获取。");
		}
		checkOpinion.setStatus(status);
		checkOpinion.setOpinion(opinion);
		checkOpinion.setCompleteTime(LocalDateTime.now());
		checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
		checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
		checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
		bpmCheckOpinionManager.update(checkOpinion);
		
	}

	@Override
	@Transactional
	public void addSignLineTask(BpmTaskTrans taskTrans, List<IUser> listUsers,
			String notifyType, String opinion) throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskTrans.getTaskId());

		BpmTaskTrans trans = (BpmTaskTrans) taskTrans;
		
		// 更新当前任务类型为流转类型。
		bpmTask.setStatus(TaskType.SIGNLINEING.name());
		bpmTaskManager.update(bpmTask);
		// 插入并行签署记录
		addSignLine(trans, bpmTask, listUsers,opinion,notifyType,taskTrans.getAction());

		// 更新任务的审核意见状态为流转中。
		DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTaskId(taskTrans.getTaskId());
		checkOpinion.setStatus(OpinionStatus.SIGNLINEING.getKey());
		checkOpinion.setOpinion(opinion);
		checkOpinion.setCompleteTime(LocalDateTime.now());
		checkOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(checkOpinion.getCreateTime()));
		checkOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
		checkOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
		bpmCheckOpinionManager.update(checkOpinion);
	}

	/**
	 * 添加并行签署任务和记录
	 * @param trans
	 * @param bpmTask
	 * @param listUsers
	 * @param opinion
	 * @param notifyType
	 * @throws Exception 
	 */
	private void addSignLine(BpmTaskTrans trans, DefaultBpmTask bpmTask,
			List<IUser> listUsers,String opinion,String notifyType,String action) throws Exception {
		String parentTaskId = trans.getTaskId();
		String rootTaskId = parentTaskId;
		BpmTaskSignLine signLine = signLineManager.getByTaskId(trans.getTaskId());
		String path = parentTaskId;
		if(BeanUtils.isNotEmpty(signLine)){
			path = String.format("%s.%s", signLine.getPath(),parentTaskId);
			rootTaskId = path.split("\\.")[0];
			// updateStatus 
			signLine.setStatus(SignLineStatus.COMPLETE.getKey());
			signLineManager.update(signLine);
			action =  signLine.getAction();
		}
		for (IUser user : listUsers){
			DefaultBpmTask task = BpmUtil.convertTask(bpmTask, rootTaskId, TaskType.SIGNLINEED, user);
			bpmTaskManager.create(task);
			String taskId = task.getId();
			
			//生成审批意见
			addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, user.getUserId(), "", parentTaskId);
			// 发送通知
			MessageUtil.notify(task, opinion, user, notifyType, TemplateConstants.TYPE_KEY.BPMN_TASK_SIGN_LINE);
		
			// 记录signLine
			BpmTaskSignLine newSignLine = new BpmTaskSignLine();
			newSignLine.setTaskId(taskId);
			//newSignLine.setParentTaskId(parentTaskId);
			newSignLine.setInstanceId(task.getProcInstId());
			newSignLine.setPath(path);
			newSignLine.setStatus(SignLineStatus.INAPPROVAL.getKey());
			// {id:"1",type:"user",name:"超级管理员"}
			ObjectNode mainGroup = ucFeignService.getMainGroup(user.getUserId());
			String orgName = "";
			HashMap<String, String> userMap = new HashMap<String, String>();
			if(BeanUtils.isNotEmpty(mainGroup) && mainGroup.has("name") ){
				orgName = mainGroup.get("name").asText();
			}
			userMap.put("orgName", orgName);
			userMap.put("id", user.getUserId());
			userMap.put("type","user");
			userMap.put("account", user.getAccount());
			userMap.put("name", user.getFullname());
			newSignLine.setExecutor(JsonUtil.toJson(userMap));
			newSignLine.setAction(action);
			newSignLine.setNodeId(task.getNodeId());
			signLineManager.create(newSignLine);
			
		}
	}

	@Override
	@Transactional
	public void revokeSignLine(RevokeSignLineParamObject revokeParamObject)
			throws Exception {
		BpmTaskSignLine revokeSignLine = signLineManager.getByTaskId(revokeParamObject.getTaskId());
		String rootTaskId = revokeParamObject.getTaskId();
		if(BeanUtils.isNotEmpty(revokeSignLine)) {			
			rootTaskId = revokeSignLine.getPath().split("\\.")[0];
		}
		List<BpmTaskSignLine> signLines = signLineManager.getByInstNodeIdAndStatus(revokeParamObject.getInstanceId(),rootTaskId,revokeParamObject.getNodeId(), SignLineStatus.INAPPROVAL.getKey());
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(revokeParamObject.getInstanceId());
		DefaultBpmDefinition def = bpmDefinitionManager.getById(instance.getProcDefId());
		String isReadRevoke = def.getIsReadRevoke();
		// 已阅的不能撤回
		boolean isNotRead = false;
		if("false".equals(isReadRevoke)){
			for (BpmTaskSignLine bpmTaskSignLine : signLines) {
				if(bpmTaskSignLine.isNotRead()){
					isNotRead = true;
					break;
				}
			}
		}else{
			isNotRead = true;
		}
		// 判断A是否可以撤回
		if(BeanUtils.isEmpty(revokeSignLine) &&
				BeanUtils.isNotEmpty(signLines) &&
				isNotRead){
			String inApprovalTaskIds = revokeParamObject.getInApprovalTaskIds();
			String[] taskIds = inApprovalTaskIds.split(",");
			for (String taskId : taskIds) {
				updateInApprovalOpinion(taskId, revokeParamObject.getCause(),OpinionStatus.SIGN_LINE_RETRACTED.getKey());
			}
			bpmTaskManager.removeByIds(taskIds);
			String status = SignLineStatus.RETRACTED.getKey();
			signLineManager.updateStatusByTaskIds(status,taskIds);
			//signLineManager.removeByTaskIds(taskIds);
			
			// 判断是否可以生成A的任务
			signLines = signLineManager.getByInstNodeIdAndStatus(revokeParamObject.getInstanceId(),rootTaskId,revokeParamObject.getNodeId(), SignLineStatus.INAPPROVAL.getKey());
			if(BeanUtils.isEmpty(signLines)){
				DefaultBpmTask defaultBpmTask = bpmTaskManager.get(rootTaskId);
				defaultBpmTask.setStatus(TaskType.NORMAL.getKey());
				bpmTaskManager.update(defaultBpmTask);
				signLineManager.removeByInstIdNodeId(revokeParamObject.getInstanceId(),rootTaskId,revokeParamObject.getNodeId());
				addTranCheckOpinion(defaultBpmTask, OpinionStatus.AWAITING_CHECK, ContextUtil.getCurrentUserId(), "", defaultBpmTask.getId());
			}
			return;
		}
		
		if(BeanUtils.isNotEmpty(revokeSignLine)){
			DefaultBpmTask defaultBpmTask = bpmTaskManager.get(rootTaskId);
			List<BpmTaskSignLine> pathChildSignLines = signLineManager.getByPathChildAndStatus(String.format("%s.%s", revokeSignLine.getPath(),revokeSignLine.getTaskId()), null);
			// An、Ann  撤回
			// 并签未结束   An 无下级 生成An的待办
			if(BeanUtils.isEmpty(pathChildSignLines) && BeanUtils.isNotEmpty(signLines) &&
					SignLineStatus.COMPLETE.getKey().equals(revokeSignLine.getStatus()) ){
				//return true;
				DefaultBpmTask task = BpmUtil.convertTask(defaultBpmTask, rootTaskId, TaskType.SIGNLINEED, ContextUtil.getCurrentUser());
				bpmTaskManager.create(task);
				
				// 添加An 撤回审批意见 
				addCheckOpinion(task, OpinionStatus.SIGN_LINE_RETRACTED, ContextUtil.getCurrentUserId(), revokeParamObject.getCause(), true);
				
				//生成审批意见
				addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, ContextUtil.getCurrentUserId(), "", defaultBpmTask.getId());
				
				
				revokeSignLine.setStatus(SignLineStatus.WITHDRAWALOFAPPROVAL.getKey());
				signLineManager.update(revokeSignLine);
				
				revokeSignLine.setTaskId(task.getId());
				revokeSignLine.setStatus(SignLineStatus.INAPPROVAL.getKey());
				revokeSignLine.setId(UniqueIdUtil.getSuid());
				signLineManager.create((revokeSignLine));
				
				return;
			}
			// 并签结束 A有待办 重新产生An Ann 待办
			if(BeanUtils.isEmpty(pathChildSignLines) &&  
					BeanUtils.isNotEmpty(defaultBpmTask) && 
					TaskType.NORMAL.getKey().equals(defaultBpmTask.getStatus()) &&
					SignLineStatus.COMPLETE.getKey().equals(revokeSignLine.getStatus()) ){
				//return true;
				DefaultBpmTask task = BpmUtil.convertTask(defaultBpmTask, rootTaskId, TaskType.SIGNLINEED, ContextUtil.getCurrentUser());
				bpmTaskManager.create(task);
				//生成审批意见
				addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, ContextUtil.getCurrentUserId(), "", defaultBpmTask.getId());
				defaultBpmTask.setStatus(TaskType.SIGNLINEING.getKey());
				bpmTaskManager.update(defaultBpmTask);
				updateInApprovalOpinion(defaultBpmTask.getTaskId(), revokeParamObject.getCause(),OpinionStatus.SIGN_LINE_RETRACTED.getKey());
				
				revokeSignLine.setStatus(SignLineStatus.WITHDRAWALOFAPPROVAL.getKey());
				signLineManager.update(revokeSignLine);
				
				revokeSignLine.setTaskId(task.getId());
				revokeSignLine.setStatus(SignLineStatus.INAPPROVAL.getKey());
				revokeSignLine.setId(UniqueIdUtil.getSuid());
				signLineManager.create((revokeSignLine));
				return;
			}
			
			pathChildSignLines = signLineManager.getByPathChildAndStatus(String.format("%s.%s", revokeSignLine.getPath(),revokeSignLine.getTaskId()), SignLineStatus.INAPPROVAL.getKey());
			if("false".equals(isReadRevoke)){
				for (BpmTaskSignLine bpmTaskSignLine : pathChildSignLines) {
					if(bpmTaskSignLine.isNotRead()){
						isNotRead = true;
						break;
					}
				}
			}else{
				isNotRead = true;
			}
			//  An 撤回 Ann
			if(BeanUtils.isNotEmpty(pathChildSignLines) && 
					isNotRead){
				String inApprovalTaskIds = revokeParamObject.getInApprovalTaskIds();
				String[] taskIds = inApprovalTaskIds.split(",");
				for (String taskId : taskIds) {
					updateInApprovalOpinion(taskId, revokeParamObject.getCause(),OpinionStatus.SIGN_LINE_RETRACTED.getKey());
				}
				bpmTaskManager.removeByIds(taskIds);
				String status = SignLineStatus.RETRACTED.getKey();
				signLineManager.updateStatusByTaskIds(status,taskIds);
				
				// 判断是否可以生成An的任务 如果全部 Ann都没有处理过，且都被An撤回就生成 An的待办
				List<BpmTaskSignLine> pathChildAndStatus = signLineManager.getByPathChildAndStatus(String.format("%s.%s", revokeSignLine.getPath(),revokeSignLine.getTaskId()), null);
				boolean isCreateAn = true;
				for (BpmTaskSignLine bpmTaskSignLine : pathChildAndStatus) {
					if(SignLineStatus.COMPLETE.getKey().equals(bpmTaskSignLine.getStatus()) || SignLineStatus.INAPPROVAL.getKey().equals(bpmTaskSignLine.getStatus()) ){
						isCreateAn = false;
						break;
					}
				}
				
				if(isCreateAn){
					DefaultBpmTask task = BpmUtil.convertTask(defaultBpmTask, rootTaskId, TaskType.SIGNLINEED, ContextUtil.getCurrentUser());
					bpmTaskManager.create(task);
					//生成审批意见
					addTranCheckOpinion(task, OpinionStatus.AWAITING_CHECK, ContextUtil.getCurrentUserId(), "", defaultBpmTask.getId());
					revokeSignLine.setTaskId(task.getId());
					revokeSignLine.setStatus(SignLineStatus.INAPPROVAL.getKey());
					//revokeSignLine.setId(UniqueIdUtil.getSuid());
					signLineManager.update(revokeSignLine);
				}else{
					// 撤回后不产生An 但是并行签署的任务都处理完成的话 
					handSignLineA(revokeSignLine, "", revokeParamObject.getCause(), revokeParamObject.getMessageType());
				}
				
				return;
			}
		}
		throw new WorkFlowException("当前节点已不支持撤回！");
	}
}
