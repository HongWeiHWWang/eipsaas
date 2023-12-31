
package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import com.hotent.base.query.*;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.model.process.task.BpmTaskTurn;
import com.hotent.bpm.persistence.dao.BpmTaskTurnDao;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.manager.TaskTurnAssignManager;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.bpm.persistence.model.TaskTurnAssign;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmTaskTurnManager")
public class BpmTaskTurnManagerImpl extends BaseManagerImpl<BpmTaskTurnDao, DefaultBpmTaskTurn> implements BpmTaskTurnManager{

	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	TaskTurnAssignManager taskTurnAssignManager;

	
	
	/**
	* 根据流程实例列表删除任务。
	* @param instList 
	* void
	*/
    @Transactional
	public void delByInstList(List<String> instList){
		baseMapper.delByInstList(instList);
	}


	@Override
    @Transactional
	public void updComplete(String taskId, IUser user) {
		//TODO  对办理人更改信息
		String execUserId = BeanUtils.isNotEmpty(user)?user.getUserId():"-1";
		String execUserName = BeanUtils.isNotEmpty(user)?user.getFullname():"系统执行人";
		baseMapper.updComplete(taskId, execUserId, execUserName, LocalDateTime.now());
	}


	@Override
	public BpmTaskTurn getByTaskId(String taskId) {
		return baseMapper.getByTaskId(taskId);
	}


	@Override
	public IPage<DefaultBpmTaskTurn> getMyDelegate(String userId, QueryFilter queryFilter) {
		if(BeanUtils.isEmpty(queryFilter)){
			queryFilter = QueryFilter.build().withDefaultPage();
		}
		queryFilter.addFilter("owner_id_", userId, QueryOP.EQUAL, FieldRelation.OR);
		queryFilter.addFilter("from_user_id_", userId, QueryOP.EQUAL, FieldRelation.OR);
		queryFilter.setDefaultSort("a.create_time_", Direction.DESC);
        //JAVA 8
        List<QueryField> boys = queryFilter.getQuerys();
        String defKey = "";
        Optional<QueryField> queryFieldOptional= boys.stream().filter(s->s.getProperty().equals("hi.proc_def_key_")).findFirst();
        if (queryFieldOptional.isPresent()) {// 判断是否存在 hi.proc_def_key_ 这个查询条件
            QueryField queryField = queryFieldOptional.get();
            defKey = queryField.getValue().toString();
            //删除通过流程定义key获取已阅任务的参数
            queryFilter.getQuerys().remove(queryFieldOptional.get());
        }
		IPage<DefaultBpmTaskTurn> taskTurn =baseMapper.getMyDelegate(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter, currentModelClass()),defKey);
		return taskTurn;
	}

	@Override
	public List<Map<String, Object>> getMyDelegateCount(String userId) {
		return baseMapper.getMyDelegateCount(userId);
	}
	@Override
	public Long getMyDelegateCountByUserId(String userId) {
		return baseMapper.getMyDelegateCountByUserId(userId);
	}

	@Override
	public List<TaskTurnAssign> getTurnAssignByTaskTurnId(String taskTurnId) {
		return taskTurnAssignManager.getByTaskTurnId(taskTurnId,true);
	}


	@Override
    @Transactional
	public void add(DefaultBpmTask bpmTask, IUser owner, IUser agent,String opinion,String type) {
		DefaultBpmTaskTurn taskTurn=new DefaultBpmTaskTurn();
		taskTurn.setId(UniqueIdUtil.getSuid());
		taskTurn.setTaskId(bpmTask.getId());
		taskTurn.setTaskName(bpmTask.getName());
		taskTurn.setTaskSubject(bpmTask.getSubject());
		taskTurn.setNodeId(bpmTask.getNodeId());
		taskTurn.setProcInstId(bpmTask.getProcInstId());
		taskTurn.setOwnerId(owner.getUserId()); 
		taskTurn.setOwnerName(owner.getFullname());
		taskTurn.setAssigneeId(agent.getUserId());
		taskTurn.setAssigneeName(agent.getFullname());
		taskTurn.setStatus(BpmTaskTurn.STATUS_RUNNING);
		taskTurn.setTurnType(type);
		taskTurn.setCreateTime(LocalDateTime.now());
		taskTurn.setTypeId(bpmTask.getTypeId());
		super.create(taskTurn);
		
		addTurnAssign(taskTurn.getId(),agent,opinion);
			
	}

	@Override
    @Transactional
	public void addTurnAssign(String turnId, IUser user, String opinion) {
		IUser fromUser= ContextUtil.getCurrentUser();
		TaskTurnAssign taskTurnAssign = new TaskTurnAssign();
		taskTurnAssign.setId(UniqueIdUtil.getSuid()); 
		taskTurnAssign.setTaskTurnId(turnId);
		taskTurnAssign.setFromUser(fromUser.getFullname());
		taskTurnAssign.setFromUserId(fromUser.getUserId());
		taskTurnAssign.setReceiverId(user.getUserId());
		taskTurnAssign.setReceiver(user.getFullname());
		taskTurnAssign.setComment(opinion);
		taskTurnAssignManager.create(taskTurnAssign);
	}

    @Override
    @Transactional
    public void delByTaskId(String taskId) {
        baseMapper.delByTaskId(taskId);
    }

    @Override
	public List<DefaultBpmTaskTurn> getByTaskIdAndAssigneeId(String taskId,
			String assigneeId) {
		return baseMapper.getByTaskIdAndAssigneeId(taskId, assigneeId);
	}
}
