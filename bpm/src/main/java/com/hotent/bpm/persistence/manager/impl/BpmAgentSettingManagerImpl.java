package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.dao.BpmAgentConditionDao;
import com.hotent.bpm.persistence.dao.BpmAgentDefDao;
import com.hotent.bpm.persistence.dao.BpmAgentSettingDao;
import com.hotent.bpm.persistence.dao.BpmDefinitionDao;
import com.hotent.bpm.persistence.manager.BpmAgentConditionManager;
import com.hotent.bpm.persistence.manager.BpmAgentSettingManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.BpmAgentCondition;
import com.hotent.bpm.persistence.model.BpmAgentDef;
import com.hotent.bpm.persistence.model.BpmAgentSetting;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmAgentSettingManager")
public class BpmAgentSettingManagerImpl extends BaseManagerImpl<BpmAgentSettingDao, BpmAgentSetting> implements BpmAgentSettingManager{
	
	@Resource
	BpmAgentDefDao bpmAgentDefDao;
	
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	
	@Resource
	BpmAgentConditionManager bpmAgentConditionManager;
	@Resource
	IUserService userServiceImpl;
	
	@Override
    @Transactional
	public void removeByIds(String... ids) {
		if(ids!=null){
            for(String pk:ids){
            	bpmAgentDefDao.removeBySettingId(pk);
            	bpmAgentConditionManager.removeBySettingId(pk);
            }
        }
		super.removeByIds(ids);
	}



	@Override
	public BpmAgentSetting getById(String id) {
		BpmAgentSetting bpmAgentSetting = super.get(id);
		
		if(bpmAgentSetting!=null){
			if(BpmAgentSetting.TYPE_CONDITION.shortValue() == bpmAgentSetting.getType().shortValue()){
				DefaultBpmDefinition bpmDefinition  = bpmDefinitionManager.getMainByDefKey(bpmAgentSetting.getFlowKey());
				if(BeanUtils.isNotEmpty(bpmDefinition))
					bpmAgentSetting.setFlowName(bpmDefinition.getName());
				List<BpmAgentCondition> conditionList=bpmAgentConditionManager.getBySettingId(id);
				for (BpmAgentCondition bpmAgentCondition : conditionList) {
					IUser user =userServiceImpl.getUserById(bpmAgentCondition.getAgentId());
					if(BeanUtils.isNotEmpty(user))
						bpmAgentCondition.setAgentName(user.getFullname());
				}
				bpmAgentSetting.setConditionList(conditionList);
			}else{
				List<BpmAgentDef> defList=bpmAgentDefDao.getBySettingId(id);
				for (BpmAgentDef bpmAgentDef : defList) {
					DefaultBpmDefinition bpmDefinition  =bpmDefinitionManager.getMainByDefKey(bpmAgentDef.getFlowKey());
					if(BeanUtils.isNotEmpty(bpmDefinition))
						bpmAgentDef.setFlowName(bpmDefinition.getName());
				}
				bpmAgentSetting.setDefList(defList);
			}
		}
		
		return bpmAgentSetting;
		
	}



	@Override
    @Transactional
	public void create(BpmAgentSetting entity){
		if(BeanUtils.isEmpty(entity.getId())){
			String settingId=UniqueIdUtil.getSuid();
			entity.setId(settingId);
		}
		super.create(entity);
		//保存流程定义和流程条件
		saveSub(entity);
	}
	
	/**
	 * 保存部分代理流程定义和条件代理。 
	 * @param entity 
	 * void
	 */
	private void saveSub(BpmAgentSetting entity){
		String settingId=entity.getId();
		//流程定义
		List<BpmAgentDef> defList= entity.getDefList();
		if(BeanUtils.isNotEmpty(defList)){
			for(BpmAgentDef def:defList){
				def.setId(UniqueIdUtil.getSuid());
				def.setSettingId(settingId);
				bpmAgentDefDao.insert(def);
			}
		}
		
		//流程条件
		List<BpmAgentCondition> conditionList= entity.getConditionList();
		if(BeanUtils.isNotEmpty(conditionList)){
			for(BpmAgentCondition condition:conditionList){
				condition.setId(UniqueIdUtil.getSuid());
				condition.setSettingId(settingId);
				bpmAgentConditionManager.create(condition);
			}
		}
	}
	
	@Override
    @Transactional
	public void update(BpmAgentSetting entity){
		String settingId=entity.getId();
		super.update(entity);
		//删除条件
		bpmAgentConditionManager.removeBySettingId(settingId);
		//删除流程定义
		bpmAgentDefDao.removeBySettingId(settingId);
		//保存流程定义和流程条件
		saveSub(entity);
		
		
	}
	
	
	/**
	 * 根据授权人和流程定义ID获取流程代理设定。
	 * @param authId
	 * @param flowKey
	 * @return BpmAgentSetting
	 */
	@Override
	public BpmAgentSetting getSettingByFlowAndAuthidAndDate(String authId,String flowKey){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("authid", authId);
		params.put("flowkey", flowKey);
		params.put("date", LocalDateTime.now());
		List<BpmAgentSetting> list= baseMapper.getSettingByFlowAndAuthidAndDate(authId,flowKey,LocalDateTime.now());
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 检查冲突。
	 *  1.首先检查全权代理是否冲突。
	 *  	如果存在直接退出检查。
	 * 
	 * 1.如果当前添加的是全权代理。
	 * 	那么需要检查这个代理和当前用户和时间是否有冲突。
	 * 2.如果部分代理。
	 * 	1.需要检查这个代理是否和目前设置的有时间冲突。
	 * 	2.如果没有冲突则直接返回，如果有冲突，需要检查流程定义是否有冲突。
	 * 
	 * 3.如果当前是条件代理。
	 * 	1.检查当前是否和目前配置的时间有冲突，如果没有则直接返回。
	 * 	2.检查当前流程定义有冲突则检查是否和部分代理有冲突，没有则通过。否则返回冲突的流程。
	 * 
	 */
	@Override
	public ResultMessage checkConflict(BpmAgentSetting setting) {
		String settingId=setting.getId();
		String authId=setting.getAuthId();
		LocalDateTime startDate=setting.getStartDate();
		LocalDateTime endDate=setting.getEndDate();
		short type=setting.getType();
		
		ResultMessage result=ResultMessage. getSuccess();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("settingId", settingId);
		params.put("authid", authId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		Integer rtn= baseMapper.getByAuthAndDate(params);
		
		if(rtn>0)
			return result=ResultMessage.getFail("此时间段内与已有的有效代理冲突!");
		switch (type) {
			case 1:
				result=ResultMessage. getSuccess();;
				break;
			case 2:
				String msg=checkPart(setting);
				if(StringUtil.isNotEmpty(msg)){
					result=ResultMessage.getFail(msg+ "流程冲突!");
				}
				break;
			case 3:
				boolean isConflict=checkCondition(setting);
				if(isConflict){
					result=ResultMessage.getFail("条件代理有冲突!");
				}
				break;
		}
		
		return result;
	}
	
	/**
	 * 检查条件代理。
	 * @param setting
	 * @return  boolean
	 */
	private boolean checkCondition(BpmAgentSetting setting){
		boolean rtn=false;
		String settingId=setting.getId();
		String authId=setting.getAuthId();
		LocalDateTime startDate=setting.getStartDate();
		LocalDateTime endDate=setting.getEndDate();
		String flowKey=setting.getFlowKey();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("flowKey", flowKey);
		params.put("authid", authId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("settingId", StringUtil.isEmpty(settingId)?null:settingId);
		
		Integer i= baseMapper.getForCondition(params);
		if(i>0) return true;
		i= baseMapper.getByAuthDateFlowKey(params);
		if(i>0) return true;
		
		return rtn;
		
	}

	/**
	 * 检查部分代理。
	 * @param setting
	 * @return 
	 * String
	 */
	private String checkPart(BpmAgentSetting setting){
		String settingId=setting.getId();
		String authId=setting.getAuthId();
		LocalDateTime startDate=setting.getStartDate();
		LocalDateTime endDate=setting.getEndDate();
		
		StringBuffer sb=new StringBuffer();
		List<BpmAgentDef> defList=setting.getDefList();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("authid", authId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("settingId", StringUtil.isEmpty(settingId)?null:settingId);
		for(BpmAgentDef def:defList){
			params.put("flowKey", def.getFlowKey());
			Integer i= baseMapper.getByAuthDateFlowKey(params);
			if(i>0){
				sb.append(def.getFlowName() +",");
			}
		}
		return sb.toString();
		
	}
}
