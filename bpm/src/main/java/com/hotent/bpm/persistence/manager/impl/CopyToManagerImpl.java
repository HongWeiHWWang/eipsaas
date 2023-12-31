package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.ObjectName;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.persistence.dao.CopyToDao;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCptoReceiverManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.manager.CopyToManager;
import com.hotent.bpm.persistence.model.BpmCptoReceiver;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.CopyTo;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.bpm.util.PortalDataUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;
import org.springframework.transaction.annotation.Transactional;

@Service("copyToManager")
public class CopyToManagerImpl extends BaseManagerImpl<CopyToDao, CopyTo> implements CopyToManager{

	@Resource
	BpmProcessInstanceManagerImpl bpmProcessInstanceManager;
	@Resource
	BpmCptoReceiverManager bpmCptoReceiverManager;
	@Resource
	IUserService userServiceImpl;
    @Resource
    BpmTaskManager bpmTaskManager;
    @Resource
    BpmCheckOpinionManager bpmCheckOpinionManager;
	
	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		baseMapper.delByInstList(instList);
	}
	
	
	@Override
    @Transactional
	public void create(CopyTo copyTo) {
		super.create(copyTo);
		//添加接收人
		List<BpmCptoReceiver> receivers=copyTo.getReceivers();
		for(BpmCptoReceiver receiver:receivers){
			bpmCptoReceiverManager.create(receiver);
		}
	}


	@Override
	public List<CopyTo> getReceiverCopyTo(String userId,
			QueryFilter queryFilter) {
		if(BeanUtils.isEmpty(queryFilter)){
			queryFilter = QueryFilter.build().withDefaultPage();
		}
		queryFilter.withParam("userId", userId);
		return baseMapper.getReceiverCopyTo(convert2IPage(queryFilter.getPageBean()),queryFilter.getParams());
	}


	@Override
	public List<CopyTo> getMyCopyTo(String userId, QueryFilter filter) {
		if(BeanUtils.isEmpty(filter)){
			filter = QueryFilter.build().withDefaultPage();
		}
		filter.withParam("userId", userId);
		return baseMapper.getMyCopyTo(convert2IPage(filter.getPageBean()),filter.getParams());
	}

    @Transactional
	private void trans(String instanceId, String userId, String messageType,String opinion,String copyToType,String taskId,String files,String selectNodeId) throws Exception{
		BpmProcessInstance instance = bpmProcessInstanceManager.get(instanceId);
		IUser user = userServiceImpl.getUserById(userId);
		IUser currentUser = userServiceImpl.getUserById(ContextUtil.getCurrentUserId());
		//创建一条CopyTo
		CopyTo copyTo = new CopyTo();
		if(copyToType.equals("0")){
			copyTo.setType("copyto");
		}else{
			copyTo.setType("trans");
		}
		if(StringUtil.isNotEmpty(selectNodeId)){
            copyTo.setNodeId(selectNodeId);
        }else{
            copyTo.setNodeId(ContextThreadUtil.getCommuVar("nodeId", "").toString());
        }
		copyTo.setOpinion(opinion);
		copyTo.setId(UniqueIdUtil.getUId().toString());
		copyTo.setBpmnInstId(instance.getBpmnInstId());
		copyTo.setInstId(instance.getId());
		copyTo.setSubject(instance.getSubject());
		copyTo.setStartor(currentUser.getFullname());
		copyTo.setStartorId(currentUser.getUserId());
		copyTo.setTypeId(instance.getTypeId());
		copyTo.setCreateTime(LocalDateTime.now());
		super.create(copyTo);
		//创建接收人对象
		BpmCptoReceiver bpmCptoReceiver = new BpmCptoReceiver();
		bpmCptoReceiver.setId(UniqueIdUtil.getUId().toString());
		bpmCptoReceiver.setReceiverId(userId);
		bpmCptoReceiver.setReceiver(user.getFullname());
		bpmCptoReceiver.setCptoId(copyTo.getId());
		bpmCptoReceiverManager.create(bpmCptoReceiver);
        //添加知会任务
        BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
        BpmTaskNotice taskNotice = new BpmTaskNotice("传阅任务", instance.getSubject(), instance.getId(), instance.getProcDefId(), instance.getProcDefName(), userId, user.getFullname(), TaskType.COPYTO.getKey(),((DefaultBpmProcessInstance) instance).getSupportMobile(),currentUser.getFullname(),currentUser.getUserId(),0,BeanUtils.isEmpty(taskId)?null:taskId,BeanUtils.isEmpty(selectNodeId)?null:selectNodeId);
        noticeManager.create(taskNotice);
        //消息接收人
		List<IUser> userList = new ArrayList<IUser>();
		userList.add(user);
		//发送消息通知相关人员
		Map<String,Object> vars = getVar(instance, opinion);
		MessageUtil.sendMsg(copyToType.equals("1")?TemplateConstants.TYPE_KEY.BPM_HAND_TO:TemplateConstants.TYPE_KEY.COPY_TO, messageType, userList, vars);
	}
	
	@Override
    @Transactional
	public void transToMore(String instanceId, List<String> userIds, String messageType,String opinion,String copyToType,String taskId,String files,String selectNodeId) throws Exception{
		for(String userId:userIds){
			trans(instanceId, userId, messageType, opinion,copyToType,taskId,files,selectNodeId);
		}
		
		if (copyToType.equals("0")) {
			UCFeignService ucFeignService = AppUtil.getBean(UCFeignService.class);
			ArrayNode users= ucFeignService.getUserByIds(StringUtil.join(userIds, ","));
			List<String> userNames = new ArrayList<>();
			if (BeanUtils.isNotEmpty(users)) {
				for (JsonNode user : users) {
					ObjectNode userObj = (ObjectNode) user;
					userNames.add(userObj.get("fullname").asText());
				}
			}
			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instanceId);
            DefaultBpmCheckOpinion defaultBpmCheckOpinion = new DefaultBpmCheckOpinion();
            defaultBpmCheckOpinion.setId(UniqueIdUtil.getSuid());
            defaultBpmCheckOpinion.setProcDefId(instance.getProcDefId());
            defaultBpmCheckOpinion.setProcInstId(instanceId);
            defaultBpmCheckOpinion.setTaskId("");
            defaultBpmCheckOpinion.setTaskKey("");
            defaultBpmCheckOpinion.setTaskName("传阅任务");
            defaultBpmCheckOpinion.setStatus(OpinionStatus.COPYTO.getKey());
            defaultBpmCheckOpinion.setCreateTime(LocalDateTime.now());
            defaultBpmCheckOpinion.setOpinion(opinion);
            defaultBpmCheckOpinion.setQualfiedNames(StringUtil.join(userNames,","));
            defaultBpmCheckOpinion.setCompleteTime(LocalDateTime.now());
            defaultBpmCheckOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(defaultBpmCheckOpinion.getCreateTime()));
            defaultBpmCheckOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
            defaultBpmCheckOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
            defaultBpmCheckOpinion.setFiles(files);
            bpmCheckOpinionManager.create(defaultBpmCheckOpinion);
		}
	}
	
	private Map<String,Object> getVar(BpmProcessInstance bpmProcessInstance,String opinion){
		String baseUrl = PortalDataUtil.getPropertyByAlias(TemplateConstants.TEMP_VAR.BASE_URL);
		IUser user = ContextUtil.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		// 转发人
		map.put(TemplateConstants.TEMP_VAR.DELEGATE, user.getFullname());
		// 任务标题
		map.put(TemplateConstants.TEMP_VAR.TASK_SUBJECT, bpmProcessInstance.getSubject());
		map.put(TemplateConstants.TEMP_VAR.BASE_URL, baseUrl);
		map.put(TemplateConstants.TEMP_VAR.TASK_ID, bpmProcessInstance.getId());
		// 意见
		map.put(TemplateConstants.TEMP_VAR.CAUSE, opinion);

		return map;
	}



}
