package com.hotent.runtime.script;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.params.InstFormAndBoVo;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.activiti.ext.model.BpmDelegateTaskImpl;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.IUserScript;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.manager.BpmDefUserManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.runtime.manager.MeetingRoomAppointmentManager;
import com.hotent.runtime.manager.MeetingRoomManager;
import com.hotent.runtime.model.MeetingRoom;
import com.hotent.runtime.model.MeetingRoomAppointment;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.model.Org;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IParamService;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;
@Component
public class UserScript implements IUserScript {
	@Resource
	IUserService userService;
	@Resource
	BpmDefUserManager bpmDefUserManager;
	@Resource
	MeetingRoomManager meetingRoomManager;
	@Resource
	MeetingRoomAppointmentManager meetingRoomAppointmentManager;
	@Resource  
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource  
	UCFeignService uCFeignService;
	@Resource
	IUserGroupService userGroupService;
	@Resource
	IParamService paramService;
	@Resource
	BpmIdentityConverter bpmIdentityConverter;
    @Resource
    IFlowManager iFlowService;
	
	/**
	 * 将用户列表转换成BpmIdentity列表
	 * @param list
	 * @return
	 */
	private Set<BpmIdentity> convertUserList(List<IUser> list){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		for (IUser iUser : list) {
			if(BeanUtils.isNotEmpty(iUser) && iUser.getStatus()==1){
				DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
				bpmIdentity.setId(iUser.getUserId());
				bpmIdentity.setName(iUser.getFullname());
				bpmIdentity.setType(BpmIdentity.TYPE_USER);
				identitys.add(bpmIdentity);
			}
		}
		return identitys;
	}
	
	public Set<BpmIdentity> getUser(){
	
		Set<BpmIdentity> set = new LinkedHashSet<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId("1234");
		bpmIdentity.setName("demoUser");
		set.add(bpmIdentity);
		
		bpmIdentity.setId("5678");
		bpmIdentity.setName("demoUser2");
		set.add(bpmIdentity);
		
		return set;
	}
	
	
	/**
	 * 更新或新增预约
	 * status=0,新增，status=1.更新
	 */
	public void updateAppointMent(String hysId,BpmDelegateTaskImpl task,String meetingName,String hostessName,String appointBenTime,String appointEndTime,String status){
		MeetingRoomAppointment meetingAppoint =null;
		String meetingId=(String) task.getVariable(BpmConstants.PROCESS_INST_ID);
		try {
			
			if(status=="0" ){
				meetingAppoint =new MeetingRoomAppointment();
				meetingAppoint.setId(UniqueIdUtil.getSuid());
				meetingAppoint.setMeetingroomId(hysId);
				meetingAppoint.setMeetingId(meetingId);
				meetingAppoint.setMeetingName(meetingName);
				meetingAppoint.setHostessName(hostessName);
				meetingAppoint.setAppointmentBegTime(TimeUtil.convertString(appointBenTime));
				meetingAppoint.setAppointmentEndTime(TimeUtil.convertString(appointBenTime));
				meetingAppoint.setAppointmentStatus(0);
				meetingRoomAppointmentManager.create(meetingAppoint);
			}else{
				QueryFilter queryFilter=QueryFilter.build();
				queryFilter.addFilter("MEETING_ID_", meetingId, QueryOP.EQUAL);
				List<MeetingRoomAppointment> list= meetingRoomAppointmentManager.query(queryFilter).getRows();
				meetingAppoint=list.get(0);
				meetingAppoint.setMeetingroomId(hysId);
				meetingAppoint.setMeetingId(meetingId);
				meetingAppoint.setMeetingName(meetingName);
				meetingAppoint.setHostessName(hostessName);
				meetingAppoint.setAppointmentBegTime(TimeUtil.convertString(appointBenTime));
				meetingAppoint.setAppointmentEndTime(TimeUtil.convertString(appointBenTime));
				meetingAppoint.setAppointmentStatus(1);
				meetingRoomAppointmentManager.update(meetingAppoint);
			}
		} catch (Exception e) {
		}
	}	
	
	/**
	 * 根据会议室id获取审批人
	 * ids "12,34"
	 */
	public Set<BpmIdentity> getPendingUsersByMeetingId(String id){
		MeetingRoom meetingroom=meetingRoomManager.get(id);
		String ids=meetingroom.getPendingUserId();
		Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
		List<IUser> iUsers = userService.getUserByAccounts(ids);
		set = convertUserList(iUsers);
		return set;
	}	

	/**
	 * 根据用户账号获取审批人
	 * ids "12,34"
	 */
	public Set<BpmIdentity> getUsersByAccount(String ids){
		Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
		if(StringUtil.isEmpty(ids)) return set;
		List<IUser> iUsers = userService.getUserByAccounts(ids);
		set = convertUserList(iUsers);
		return set;
	}
	
	public String getHasRightObjs(String objType) {
		List<String> idsList = bpmDefUserManager.getAuthorizeIdsByUserMap(objType);
		return StringUtil.join(idsList.toArray(new String[]{}), ",");
	}
	
	
	
	
	/**
	 * 根据组织id查询组织作为候选处理人（不抽取）。
	 * ids "12,34"
	 */
	public Set<BpmIdentity> getOrgById(String ids){
		Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
		if(StringUtil.isEmpty(ids)) return set;
		String[] idsArr = ids.split(",");
		for (String id : idsArr) {
			ObjectNode org =  uCFeignService.getOrgByIdOrCode(id);
		    BpmIdentity identity=new DefaultBpmIdentity(org.get("id").asText(),org.get("name").asText(),BpmIdentity.TYPE_GROUP);
		    identity.setGroupType(BpmIdentity.GROUP_TYPE_ORG);
		    identity.setExtractType(ExtractType.EXACT_NOEXACT);
		    set.add(identity);
		}
		return set;
	}
	
	/**
	 * List<BoData> + key
	 * type:"org","role","user"
	 * mainKey
	 * @return
	 */
	public Set<BpmIdentity> getUserById(String mainKey, List<ObjectNode> list,
			String subKey) {
		Set<BpmIdentity> set = new LinkedHashSet<BpmIdentity>();
		if (BeanUtils.isNotEmpty(mainKey)) {
			String[] idsArr = mainKey.split(",");
			for (String userId : idsArr) {
				UserFacade user = (UserFacade) userService.getUserById(userId);
				if (BeanUtils.isNotEmpty(user) && user.getStatus()==1) {
					BpmIdentity bpmIdentity = new DefaultBpmIdentity();
					bpmIdentity.setType(IdentityType.USER);
					bpmIdentity.setId(user.getId());
					bpmIdentity.setName(user.getFullname());
					set.add(bpmIdentity);
				}
			}
		}
		if (BeanUtils.isNotEmpty(list)) {
			for (ObjectNode boData : list) {
				String userId = (String) boData.get(subKey).asText();
				UserFacade user = (UserFacade) userService.getUserById(userId);
				if (BeanUtils.isNotEmpty(user)&& user.getStatus()==1) {
					BpmIdentity bpmIdentity = new DefaultBpmIdentity();
					bpmIdentity.setType(IdentityType.USER);
					bpmIdentity.setId(user.getId());
					bpmIdentity.setName(user.getFullname());
					set.add(bpmIdentity);
				}
			}
		}

		return set;
	}
	
	/**
	 * 获取组织负责人（根据组织参数获取）
	 * @param orgId 组织ID
	 * @param alias 组织参数别名
	 * @return
	 */
	public Set<BpmIdentity> getLeanderByOrgId(String orgId, String alias){
		if(StringUtil.isEmpty(orgId)||StringUtil.isEmpty(alias)) return new LinkedHashSet<BpmIdentity>();
		IGroup group = userGroupService.getGroupByIdOrCode(GroupTypeConstant.ORG.key(), orgId);
		if(BeanUtils.isEmpty(group) || !(group instanceof Org)) {
			return new LinkedHashSet<BpmIdentity>();
		}
		String value = paramService.getParamByGroup(group.getGroupId(), alias)+"";
		List<IUser> list = new ArrayList<IUser>();
		IUser iUser = userService.getUserById(value);
		list.add(iUser);
		List<BpmIdentity> listIdentity = bpmIdentityConverter.convertUserList(list);
		Set<BpmIdentity> set = new LinkedHashSet<BpmIdentity>(listIdentity);
		return set;
	}
	
	/**
	 * 获取上级组织的负责人（根据组织参数获取）
	 * @param orgId 组织ID
	 * @param alias 组织参数别名
	 * @return
	 */
	public Set<BpmIdentity> getUpLeanderByOrgId(String orgId,String alias){
		if(StringUtil.isEmpty(orgId)||StringUtil.isEmpty(alias)) return new LinkedHashSet<BpmIdentity>();
		IGroup group =  userGroupService.getGroupByIdOrCode(GroupTypeConstant.ORG.key(), orgId);
		if(BeanUtils.isEmpty(group) || !(group instanceof Org)) {
			return new LinkedHashSet<BpmIdentity>();
		}
		Org org = (Org)group;
		
		IGroup upGroup =  userGroupService.getGroupByIdOrCode(GroupTypeConstant.ORG.key(), org.getParentId());
		if(BeanUtils.isEmpty(upGroup) || !(upGroup instanceof Org)){
			 return new LinkedHashSet<BpmIdentity>();
		}
		String value = paramService.getParamByGroup(upGroup.getGroupId(), alias)+"";
		List<IUser> list = new ArrayList<IUser>();
		IUser iUser = userService.getUserById(value);
		list.add(iUser);
		List<BpmIdentity> listIdentity = bpmIdentityConverter.convertUserList(list);
		Set<BpmIdentity> set = new LinkedHashSet<BpmIdentity>(listIdentity);
		return set;
	}
	
	/**
	 * 根据组织ID获取该组织中为主组织的人员列表
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Set<BpmIdentity> getMainByOrgId(String orgId){
		Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
		if(StringUtil.isEmpty(orgId)) return set;
		String[] aryOrgIds=orgId.split(",");
		List<String> userIds=new ArrayList<>();
		for(String tmp:aryOrgIds){
			QueryFilter queryFilter = QueryFilter.build();
			queryFilter.addFilter("org.ID_", tmp, QueryOP.EQUAL);
			List list = uCFeignService.queryOrgUserRel(queryFilter);
			if(BeanUtils.isNotEmpty(list)){
				for(int i=0;i<list.size();i++){
					Map map = (Map) list.get(i);
					if(map.get("isMaster").toString().equals("1") && map.get("orgId").toString().equals(tmp)){
						userIds.add(map.get("userId").toString());
					}
				}
			}
		}
		ArrayNode userByIds = uCFeignService.getUserByIdsOrAccounts(StringUtil.join(userIds, ","));
		if (BeanUtils.isEmpty(userByIds)) {
			return set;
		}
		for (JsonNode jsonNode : userByIds) {
			BpmIdentity identity=new DefaultBpmIdentity();
			identity.setId(jsonNode.get("id").asText());
			identity.setName(jsonNode.get("fullname").asText());
			identity.setType(BpmIdentity.TYPE_USER);
			set.add(identity);
		}
		return set;
	}
	
	/**
	 * 根据所传维度下主组织参数查询执行人
	 * @param alias
	 * @param demId
	 * @return
	 */
	public Set<BpmIdentity> getLeanderByPreNode(String alias,String demId){
		if(StringUtil.isEmpty(alias)) return new LinkedHashSet<BpmIdentity>();
		ObjectNode orgNode =  uCFeignService.getMainGroup(ContextUtil.getCurrentUser().getUserId(),demId); 
		if(BeanUtils.isEmpty(orgNode)){
			 return new LinkedHashSet<BpmIdentity>();
		}
		String value = paramService.getParamByGroup(orgNode.get("id").asText(), alias)+"";
		List<IUser> list = new ArrayList<IUser>();
		IUser iUser = userService.getUserById(value);
		list.add(iUser);
		Set<BpmIdentity> set = convertUserList(list);
		return set;
	}
	
	/**
	 * 根据角色别名获取角色。
	 * @param alias
	 * @return
	 */
	public Set<BpmIdentity> getRoleByAlias(String alias){
		Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
		if(StringUtil.isEmpty(alias)) return set;
		String[] aryAlias=alias.split(",");
		for(String tmp:aryAlias){
			IGroup group = userGroupService.getGroupByIdOrCode(GroupTypeConstant.ROLE.key(), tmp);
			BpmIdentity identity=new DefaultBpmIdentity(group.getGroupId(),group.getName(),BpmIdentity.TYPE_GROUP);
			identity.setType(BpmIdentity.TYPE_GROUP);
			identity.setExtractType(ExtractType.EXACT_NOEXACT);
			identity.setGroupType( GroupTypeConstant.ROLE.key());
			set.add(identity);
		}
		
		return set;
	}

    /**
     * 根据岗位别名获取岗位。
     * @param alias
     * @return
     */
    public Set<BpmIdentity> getByPostAlias(String alias){
        Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
        if(StringUtil.isEmpty(alias)) return set;
        String[] aryAlias=alias.split(",");
        for(String tmp:aryAlias){
            IGroup group = userGroupService.getGroupByIdOrCode(GroupTypeConstant.POSITION.key(), tmp);
            BpmIdentity identity=new DefaultBpmIdentity(group.getGroupId(),group.getName(),BpmIdentity.TYPE_GROUP);
            identity.setType(BpmIdentity.TYPE_GROUP);
            identity.setExtractType(ExtractType.EXACT_NOEXACT);
            identity.setGroupType( GroupTypeConstant.POSITION.key());
            set.add(identity);
        }

        return set;
    }


    /**
     * 从子表数据中提取节点审批人
     * @param boDefAlias
     * @param subTableName
     * @param subIdField
     * @param subNameFiled
     * @return
     * @throws Exception
     */
    public Set<BpmIdentity> getUserFromSubData(String boDefAlias,String subTableName,String subIdField,String subNameFiled) throws Exception{
        Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
        ActionCmd cmd =  ContextThreadUtil.getActionCmd();
        ObjectNode  jsonObject = JsonUtil.getMapper().createObjectNode();
        if(BeanUtils.isEmpty(cmd.getBusData())){
            InstFormAndBoVo instFormAndBoVo = iFlowService.getInstFormAndBO(cmd.getVariables().get(BpmConstants.PROCESS_INST_ID).toString(),"",null, FormType.PC,true,true);
            jsonObject = instFormAndBoVo.getData();
        }else{
            jsonObject = (ObjectNode) JsonUtil.toJsonNode(cmd.getBusData());
        }
        if(BeanUtils.isNotEmpty(jsonObject) && jsonObject.get(boDefAlias).get(subTableName).size()>0){
            String userIds = "";
            for (int i = 0; i < jsonObject.get(boDefAlias).get(subTableName).size(); i++) {
                String userId = "";
                String userName = "";
                if(BeanUtils.isNotEmpty(jsonObject.get(boDefAlias).get(subTableName).get(i).get(subIdField))){
                    userId = jsonObject.get(boDefAlias).get(subTableName).get(i).get(subIdField).asText();
                }
                if(StringUtil.isNotEmpty(subNameFiled)){
                    if(BeanUtils.isNotEmpty(jsonObject.get(boDefAlias).get(subTableName).get(i).get(subNameFiled))){
                        userName = jsonObject.get(boDefAlias).get(subTableName).get(i).get(subNameFiled).asText();
                        DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
                        bpmIdentity.setId(userId);
                        bpmIdentity.setName(userName);
                        bpmIdentity.setType(BpmIdentity.TYPE_USER);
                        set.add(bpmIdentity);
                    }
                }else{
                    if(StringUtil.isNotEmpty(userId)){
                        if(StringUtil.isNotEmpty(userIds)){
                            userIds += ",";
                        }
                        userIds += userId;
                    }
                }

            }
            if(StringUtil.isEmpty(subNameFiled) && StringUtil.isNotEmpty(userIds)){
                ArrayNode iUsers = uCFeignService.getUserByIds(userIds);
                for (JsonNode userNode : iUsers) {
                    DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
                    bpmIdentity.setId(userNode.get("id").asText());
                    bpmIdentity.setName(userNode.get("fullname").asText());
                    bpmIdentity.setType(BpmIdentity.TYPE_USER);
                    set.add(bpmIdentity);
                }
            }
        }
        return set;
    }

}
