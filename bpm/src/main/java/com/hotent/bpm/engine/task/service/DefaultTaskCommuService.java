package com.hotent.bpm.engine.task.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.IGlobalRestfulPluginDef;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginFactory;
import com.hotent.bpm.api.plugin.core.runtime.BpmExecutionPlugin;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.RestfulService;
import com.hotent.bpm.api.service.TaskCommuService;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCommuReceiverManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskCommuManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.model.BpmCommuReceiver;
import com.hotent.bpm.persistence.model.BpmTaskCommu;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IUserService;
@Service
public class DefaultTaskCommuService implements TaskCommuService{
	
	@Resource
	BpmTaskCommuManager bpmTaskCommuManager;
	@Resource
	BpmCommuReceiverManager bpmCommuReceiverManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmPluginFactory bpmPluginFactory;
	@Resource
	RestfulService restfulService;
    @Resource
    BpmProcessInstanceManager bpmProcessInstanceMapper;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    IUserService userServiceImpl;

    /**
     * 获取任务通知对象  按流程实例。
     * @param instId
     * @param opinion
     * @return
     * BpmTaskCommu
     */
    private BpmTaskCommu getByTasknew(String instId,String opinion){
        IUser user=ContextUtil.getCurrentUser();

        BpmTaskCommu taskCommu=bpmTaskCommuManager.getByInstId(instId);
        if(taskCommu!=null) return taskCommu;

        taskCommu=new BpmTaskCommu();

        taskCommu.setId(UniqueIdUtil.getSuid());
        taskCommu.setInstanceId(instId);
        taskCommu.setNodeName("沟通任务");
		/*taskCommu.setNodeId(task.getNodeId());

		taskCommu.setTaskId(task.getId());*/

        taskCommu.setSenderId(user.getUserId());
        taskCommu.setSender(user.getFullname());
        taskCommu.setCreatetime(LocalDateTime.now());

        taskCommu.setOpinion(opinion);

        bpmTaskCommuManager.create(taskCommu);
        return taskCommu;
    }




	/**
	 * 添加沟通任务，给每一位通知者发送沟通消息。
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void addCommuTask(String instId,String notifyType,String opinion,List<IUser> users,String files,String defId) throws Exception {
        DefaultBpmProcessInstance processInstance=bpmProcessInstanceMapper.get(instId);
        BpmTaskCommu commu=getByTasknew(instId,opinion);
        String commuId=commu.getId();
        //将发起沟通消息加入审批历史
        addTranCheckOpinion(processInstance,OpinionStatus.START_COMMU, ContextUtil.getCurrentUserId(), opinion,files,"");
        for(IUser user:users){
            //可以对同一个人沟通多次，这个判断去掉if(bpmCommuReceiverManager.checkHasCommued(commuId,user.getUserId())) continue;
            //添加沟通人
            BpmCommuReceiver receiver= getCommuReceiver(commuId,user);
            bpmCommuReceiverManager.create(receiver);
            //添加知会任务
            BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
            BpmTaskNotice notice = new BpmTaskNotice();
            notice.setStatus(TaskType.COMMU.getKey());
            notice.setId(UniqueIdUtil.getSuid());
            notice.setName("沟通任务");
            notice.setProcInstId(instId);
            notice.setAssigneeId(user.getUserId());
            notice.setAssigneeName(user.getFullname());
            notice.setOwnerId(user.getUserId());
            notice.setOwnerName(user.getFullname());
            notice.setSubject(processInstance.getSubject());
            notice.setProcDefId(processInstance.getProcDefId());
            notice.setProcDefName(processInstance.getProcDefName());
            notice.setSupportMobile(processInstance.getSupportMobile());
            noticeManager.create(notice);
            //将沟通消息加入审批历史
            addTranCheckOpinion(processInstance,OpinionStatus.AWAITING_FEEDBACK, user.getUserId(), "","",notice.getId());

        }
        //发送沟通消息
        //MessageUtil.sendMsgnew(notifyType, users, OpinionStatus.AWAITING_FEEDBACK.toString(), opinion);
	}
	
	/**
	 * 获取沟通人。
	 * @param commuId
	 * @param user
	 * @return 
	 * BpmCommuReceiver
	 */
	private BpmCommuReceiver getCommuReceiver(String commuId,IUser user){
		BpmCommuReceiver receiver=new BpmCommuReceiver();
		
		receiver.setId(UniqueIdUtil.getSuid());
		receiver.setCommuId(commuId);
		receiver.setReceiver(user.getFullname());
		receiver.setReceiverId(user.getUserId());
		receiver.setStatus(BpmCommuReceiver.COMMU_NO);
		
		return receiver;
	}
	
	/**
	 * 获取任务通知对象。
	 * @param task
	 * @param opinion
	 * @return 
	 * BpmTaskCommu
	 */
	private BpmTaskCommu getByTask(DefaultBpmTask task,String opinion){
		IUser user=ContextUtil.getCurrentUser();
		
		BpmTaskCommu taskCommu=bpmTaskCommuManager.getByTaskId(task.getId());
		if(taskCommu!=null) return taskCommu;
		
		taskCommu=new BpmTaskCommu();
		
		taskCommu.setId(UniqueIdUtil.getSuid());
		taskCommu.setInstanceId(task.getProcInstId());
		taskCommu.setNodeId(task.getNodeId());
		taskCommu.setNodeName(task.getName());
		taskCommu.setTaskId(task.getId());
		
		taskCommu.setSenderId(user.getUserId());
		taskCommu.setSender(user.getFullname());
		taskCommu.setCreatetime(LocalDateTime.now());
		
		taskCommu.setOpinion(opinion);
		
		bpmTaskCommuManager.create(taskCommu);
		return taskCommu;
	}
	
	
	
	@Override
    @Transactional
	public void completeTask(String taskId, String notifyType, String opinion) throws Exception {
		//删除沟通任务
		DefaultBpmTask defaultBpmTask=bpmTaskManager.get(taskId);
		
		BpmTaskCommu taskCommu = bpmTaskCommuManager.getByTaskId(defaultBpmTask.getParentId());
		//通讯接收人。
		BpmCommuReceiver commuReceiver = bpmCommuReceiverManager.getByCommuUser(taskCommu.getId(), defaultBpmTask.getAssigneeId());
		
		commuReceiver.setStatus(BpmCommuReceiver.COMMU_FEEDBACK);
		
		if(commuReceiver.getReceiveTime()==null){
			commuReceiver.setReceiveTime(LocalDateTime.now());
		}
		commuReceiver.setFeedbackTime(LocalDateTime.now());
		
		commuReceiver.setOpinion(opinion);
		
		bpmCommuReceiverManager.update(commuReceiver);
		//将沟通反馈信息加入审批历史
		updOpinionComplete(taskId, OpinionStatus.FEEDBACK,ContextUtil.getCurrentUserId(), opinion);
		/**
		 * 发送通知。
		 */
		IUser user=BpmUtil.getUser(taskCommu.getSenderId(), taskCommu.getSender());
		
		MessageUtil.notify(defaultBpmTask, opinion, user, notifyType, TemplateConstants.TYPE_KEY.BPM_COMMU_FEEDBACK);
		
		//触发流程全局事件中的任务结束事件
		defaultBpmTask.setAssigneeId(ContextUtil.getCurrentUserId());
		restfulPluginExecut(defaultBpmTask,EventType.TASK_COMPLETE_EVENT);
		
		bpmTaskManager.remove(taskId);
		
	}

	/**
	 * 删除沟通任务。
	 */
	@Override
	public void finishTask(String parentId) {
		bpmTaskManager.delByParentId(parentId);
	}
	
	/**
	 * 添加沟通意见。
	 * 
	 * @param def
	 * @param opinionStatus
	 *            void
	 * @param commuUser
	 *
	 */
	private void addTranCheckOpinion(DefaultBpmProcessInstance def,OpinionStatus opinionStatus, String commuUser, String opinion, String files,String noticeId){
        IUser user = userServiceImpl.getUserById(commuUser);
        List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
        BpmIdentity bpmIdentity = new DefaultBpmIdentity();
        bpmIdentity.setType(IdentityType.USER);
        bpmIdentity.setId(user.getUserId());
        bpmIdentity.setName(user.getFullname());
        identityList.add(bpmIdentity);

        String bpmnInstId = def.getBpmnInstId();
        String superInstId = (String) natProInstanceService.getSuperVariable(bpmnInstId, BpmConstants.PROCESS_INST_ID);

		DefaultBpmCheckOpinion checkOpinion = new DefaultBpmCheckOpinion();
		checkOpinion.setId(UniqueIdUtil.getSuid());
		checkOpinion.setProcDefId(def.getBpmnDefId());
		checkOpinion.setSupInstId(superInstId);
		checkOpinion.setProcInstId(def.getId());
		checkOpinion.setTaskId(noticeId);
		checkOpinion.setTaskKey("");
		checkOpinion.setTaskName("沟通任务");
		checkOpinion.setFiles(files);
		checkOpinion.setStatus(opinionStatus.getKey());
		checkOpinion.setCreateTime(LocalDateTime.now());
		checkOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
		checkOpinion.setQualfiedNames(user.getFullname());
		checkOpinion.setOpinion(opinion);
		if(opinionStatus.equals(OpinionStatus.START_COMMU)){
			checkOpinion.setCompleteTime(LocalDateTime.now());
			long durMs =TimeUtil.getTime (LocalDateTime.now(),checkOpinion.getCreateTime());
			checkOpinion.setDurMs(durMs);
			checkOpinion.setAuditor(user.getUserId());
			checkOpinion.setAuditorName(user.getFullname());
		}
        checkOpinion.setIsRead(1);
		bpmCheckOpinionManager.create(checkOpinion);
	}
	
	//更新 任务的意见
	@SuppressWarnings("unchecked")
    @Transactional
	private void updOpinionComplete(String taskId, OpinionStatus opinionStatus, String commuUser, String opinion){
		QueryFilter queryFilter =QueryFilter.build().withDefaultPage();
		queryFilter.addFilter("task_id_", taskId, QueryOP.EQUAL);
		queryFilter.addFilter("status_", OpinionStatus.AWAITING_FEEDBACK, QueryOP.EQUAL);
		PageList<DefaultBpmCheckOpinion> opinions = bpmCheckOpinionManager.query(queryFilter);
		if(BeanUtils.isNotEmpty(opinions)){
			DefaultBpmCheckOpinion checkOpinion = opinions.getRows().get(0);
			IUser user = BpmUtil.getUser(commuUser);
			checkOpinion.setAuditor(user.getUserId());
			checkOpinion.setAuditorName(user.getFullname());
			checkOpinion.setOpinion(opinion);
			checkOpinion.setStatus(opinionStatus.getKey());
			checkOpinion.setCompleteTime(LocalDateTime.now());
			long durMs =  TimeUtil.getTime (LocalDateTime.now(),checkOpinion.getCreateTime());
			checkOpinion.setDurMs(durMs);
			bpmCheckOpinionManager.update(checkOpinion);
		}
	}
	
	private void restfulPluginExecut(DefaultBpmTask task,EventType eventType) throws Exception{
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(task.getProcDefId());
		List<BpmPluginContext> pluginContextList=bpmProcessDef.getProcessDefExt().getBpmPluginContexts();
		if(BeanUtils.isNotEmpty(pluginContextList)){
			
			for(BpmPluginContext bpmPluginContext:pluginContextList){
				BpmPluginDef bpmPluginDef =bpmPluginContext.getBpmPluginDef();
				if(bpmPluginDef instanceof BpmExecutionPluginDef){
					BpmExecutionPluginDef bpmExecutionPluginDef = (BpmExecutionPluginDef)bpmPluginDef;
					BpmExecutionPlugin bpmExecutionPlugin = bpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
					if(bpmExecutionPlugin!=null){
						if(bpmPluginContext.getEventTypes().contains(eventType)){
							if(bpmExecutionPluginDef instanceof IGlobalRestfulPluginDef){
								IGlobalRestfulPluginDef restfulPluginDef = (IGlobalRestfulPluginDef) bpmExecutionPluginDef;
								List<Restful> restfuls = restfulPluginDef.getRestfulList();
								if(BeanUtils.isNotEmpty(restfuls)){
									restfulService.outTaskPluginExecute(task, restfuls,eventType);
								}
							}
						}
					}	
				}
			}
		}
	}

}
