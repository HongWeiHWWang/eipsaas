package com.hotent.bpm.helper.identity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityBuilder;
import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;


@Service
public class BpmIdentityExtractServiceImpl implements BpmIdentityExtractService{

	@Resource
	BpmIdentityConverter bpmIdentityConverter;
	
	@Resource
	BpmIdentityBuilder bpmIdentityBuilder;
	
	@Resource
	IUserService userServiceImpl;
	
	@Override
	public List<BpmIdentity> extractUserGroup(List<BpmIdentity> bpmIdentities) {
		List<BpmIdentity> results = new ArrayList<BpmIdentity>();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbName = new StringBuffer();
		for(BpmIdentity bpmIdentity:results){
			if(bpmIdentity.getType().equals(BpmIdentity.TYPE_USER)){
				sb.append(bpmIdentity.getId()).append(",");
				sbName.append(bpmIdentity.getName()).append(",");
			}
		}			
		String userGroup = sb.toString().substring(0, sb.length()-1);
		String userGroupName = sbName.toString().substring(0, sbName.length()-1);
		
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setId(userGroup);
		bpmIdentity.setType(BpmIdentity.TYPE_GROUP_USER);
		bpmIdentity.setName(userGroupName);
	
		results.add(bpmIdentity);
		return results;
	}

	@Override
	public List<BpmIdentity> extractBpmIdentity(List<BpmIdentity> bpmIdentities) {
		List<BpmIdentity> results = new ArrayList<BpmIdentity>();
		for(BpmIdentity bpmIdentity:bpmIdentities){			
			
			//用户组
			if(bpmIdentity.getType().equals(BpmIdentity.TYPE_GROUP)){	
				String id = StringUtil.isNotEmpty(bpmIdentity.getId())? bpmIdentity.getId() :bpmIdentity.getCode();
				List<IUser> users = userServiceImpl.getUserListByGroup(bpmIdentity.getGroupType(), id);
				List<BpmIdentity> tempBpmIdentities =bpmIdentityConverter.convertUserList(users);
				results.addAll(tempBpmIdentities);
			}
			//用户
			else if(BpmIdentity.TYPE_USER.equals(bpmIdentity.getType()) ) {
				results.add(bpmIdentity);
			}
			
		}
		return results;
	}


	@Override
	public List<IUser> extractUser(List<BpmIdentity> bpmIdentities) {
		List<IUser> results = new ArrayList<IUser>();
		for(BpmIdentity bpmIdentity:bpmIdentities){			
			if(bpmIdentity.getType().equals(BpmIdentity.TYPE_GROUP)){				
				List<IUser> users = userServiceImpl.getUserListByGroup(bpmIdentity.getGroupType(), bpmIdentity.getId());  //
				results.addAll(users);
			}else if(BpmIdentity.TYPE_USER.equals(bpmIdentity.getType()) ){
				IUser user=userServiceImpl.getUserById(bpmIdentity.getId());
				results.add(user);
			}
			else if(bpmIdentity.getType().equals(BpmIdentity.TYPE_GROUP_USER)){
				String[] aryUserId=bpmIdentity.getId().split(",");
				for(int i=0;i<aryUserId.length;i++){
					IUser user=userServiceImpl.getUserById(aryUserId[i]);
					results.add(user);
				}
			}
		}
		return results;
	}

}
