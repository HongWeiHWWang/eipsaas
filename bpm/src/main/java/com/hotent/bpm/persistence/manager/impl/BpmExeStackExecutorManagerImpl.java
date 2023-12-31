package com.hotent.bpm.persistence.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.dao.BpmExeStackExecutorDao;
import com.hotent.bpm.persistence.manager.BpmExeStackExecutorManager;
import com.hotent.bpm.persistence.model.BpmExeStackExecutor;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;
import org.springframework.transaction.annotation.Transactional;


@Service("bpmExeStackExecutorManager")
public class BpmExeStackExecutorManagerImpl extends BaseManagerImpl<BpmExeStackExecutorDao, BpmExeStackExecutor> implements BpmExeStackExecutorManager{
	
	
	@Override
	public BpmExeStackExecutor getByTaskId(String taskId) {
		return baseMapper.getByTaskId(taskId);
	}


	@Override
	public List<BpmExeStackExecutor> getByStackId(String exeStackId) {
		return baseMapper.getByStackId(exeStackId);
	}

	@Override
    @Transactional
	public void deleteByStackId(String stackId){
		baseMapper.deleteByStackId(stackId);
	}
	
	@Override
    @Transactional
	public void deleteByStackPath(String stackPath){
		String [] stackIds = stackPath.split("\\.");
		if(BeanUtils.isEmpty(stackIds)) return ;
		baseMapper.deleteByStackIds(stackIds);
	}


	@Override
	public List<BpmIdentity> getBpmIdentitysByStackId(String exeStackId) {
		List<BpmExeStackExecutor> executors = getByStackId(exeStackId);
		
		if(BeanUtils.isNotEmpty(executors)){
			List<BpmIdentity> identitys = new ArrayList<BpmIdentity>();
			for(BpmExeStackExecutor executor : executors){
				if(StringUtil.isZeroEmpty(executor.getAssigneeId())) continue;
				String assigneeId = executor.getAssigneeId();
				String assigneeName = ""; 
				IUserService userService = AppUtil.getBean(IUserService.class);
				IUser user = userService.getUserById(assigneeId);
				if(BeanUtils.isNotEmpty(user)){
					assigneeName = user.getFullname();
				}
				DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity(assigneeId, assigneeName, BpmIdentity.TYPE_USER);
				identitys.add(bpmIdentity);
			}
			return identitys;
			
		}
		return Collections.emptyList();
	}
	
	
}
