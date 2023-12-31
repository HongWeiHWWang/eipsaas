package com.hotent.bpm.helper.identity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;
@Service
public class DefaultBpmIdentityConverter implements BpmIdentityConverter{
	

	@Resource
	IUserService userServiceImpl;
	@Resource
	IUserGroupService userGroupService;

	@Override
	public BpmIdentity convertUser(IUser user) {
		if(user == null) return null;
		
		DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		bpmIdentity.setCode(user.getAccount());
		bpmIdentity.setType(BpmIdentity.TYPE_USER);
		return bpmIdentity;		
	}


	@Override
	public List<BpmIdentity> convertUserList(List<IUser> userList) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();

		for(IUser user:userList){
			if(user==null) continue;
			BpmIdentity bpmIdentity = convertUser(user); 
			bpmIdentities.add(bpmIdentity);
		}
		return bpmIdentities;
	}

	@Override
	public BpmIdentity convertGroup(IGroup group) {
		if(group == null) return null;
		
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setId(group.getGroupId());
		bpmIdentity.setName(group.getName());
		bpmIdentity.setCode(group.getGroupCode());
		bpmIdentity.setType(BpmIdentity.TYPE_GROUP);
		bpmIdentity.setGroupType(group.getGroupType());
		return bpmIdentity;
	}

	@Override
	public List<BpmIdentity> convertGroupList(List<IGroup> groupList) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		for(IGroup group:groupList){
			if(group ==null) continue;
			BpmIdentity bpmIdentity = convertGroup(group);
			bpmIdentities.add(bpmIdentity);
		}
		return bpmIdentities;
	}


	@Override
	public BpmIdentity convert(String type, String id) {
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setId(id);
		bpmIdentity.setGroupType(type);
		bpmIdentity.setType(type);
		
		if(BpmIdentity.GROUP_TYPE_JOB.equals(type) || 
		   BpmIdentity.GROUP_TYPE_ROLE.equals(type) || 
		   BpmIdentity.GROUP_TYPE_ORG.equals(type) || 
		   BpmIdentity.GROUP_TYPE_ORG.equals(type)){
			bpmIdentity.setType(BpmIdentity.TYPE_GROUP);
		}
		
		return bpmIdentity;
	}


	@Override
	public BpmIdentity convertValue(String type, String id) {
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		if(BpmIdentity.TYPE_USER.equalsIgnoreCase(type)){
			IUser user = userServiceImpl.getUserById(id);
			bpmIdentity.setType(type);
			if(user != null)
				bpmIdentity.setName(user.getFullname());
		}else{
			IGroup group= userGroupService.getGroupByIdOrCode(type, id);
			bpmIdentity.setType(IdentityType.GROUP);
			bpmIdentity.setGroupType(group.getGroupType());
			bpmIdentity.setName(group.getName());
		}
		bpmIdentity.setId(id);
		return bpmIdentity;
	}
}
