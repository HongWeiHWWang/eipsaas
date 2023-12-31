package com.hotent.runtime.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.IUserScript;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IParamService;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;

/**
 * 人员脚本 作用：可用于节点处理人
 * @author Administrator
 */
@Service
public class BpmUserScript implements IUserScript {
	@Resource
	IUserService userService;
	@Resource
	IUserGroupService userGroupService;
	@Resource 
	UCFeignService ucFeignService;
	@Resource
	IParamService paramService;

	/**
	 * 将用户列表转换成BpmIdentity列表
	 * @param list
	 * @return
	 */
	private Set<BpmIdentity> convertUserList(List<IUser> list){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		for (IUser iUser : list) {
			if(BeanUtils.isNotEmpty(iUser) && BeanUtils.isNotEmpty(iUser.getStatus()) && iUser.getStatus()==1){
				DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
				bpmIdentity.setId(iUser.getUserId());
				bpmIdentity.setName(iUser.getFullname());
				bpmIdentity.setType(BpmIdentity.TYPE_USER);
				identitys.add(bpmIdentity);
			}
		}
		return identitys;
	}
	
	/**
	 * 根据角色编码获取人员列表
	 * @param roleId
	 * @return
	 */
	public Set<BpmIdentity> getListUserByRoleId(String roleCode) {
		IGroup role = userGroupService.getGroupByIdOrCode(GroupTypeConstant.ROLE.key(), roleCode);
		List<IUser> list = userService.getUserListByGroup(GroupTypeConstant.ROLE.key(), role.getGroupId());
		return convertUserList(list);
	}
	/**
	 * 根据岗位编码获取人员列表
	 * @param roleId
	 * @return
	 */
	public Set<BpmIdentity> getListUserByRelCode(String relCode) {
		ArrayNode array = ucFeignService.getUserByPost(relCode);
		List<IUser> list = new ArrayList<IUser>();
		if(BeanUtils.isNotEmpty(array)){
			for (Object node : array) {
				try {
					ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(node);
					UserFacade user = new  UserFacade();
					user.setId(obj.get("id").asText());
					user.setUserId(obj.get("id").asText());
					user.setAccount(obj.get("account").asText());
					user.setFullname(obj.get("fullname").asText());
					user.setStatus(BeanUtils.isNotEmpty(obj.get("status"))?obj.get("status").asInt():0);
					list.add(user);
				} catch (IOException e) {}
			}
		}
		return convertUserList(list);
	}
	
	/**
	 * 获取当前用户上级部门中指角色编码的人员
	 * @param roleId
	 * @return
	 */
	public Set<BpmIdentity> getListUserByParentOrgRoleCode(String roleCode) {
		IGroup role = userGroupService.getGroupByIdOrCode(GroupTypeConstant.ROLE.key(), roleCode);
		List<IUser> list = userService.getUserListByGroup(GroupTypeConstant.ROLE.key(), role.getGroupId());
		// 求解出当前用户主组织的上级部门的人员
		return convertUserList(list);
	}
	
	/**
	 * 获取申请人所在主组织的副总（配置组织参数：副总裁实现）
	 * @param roleId
	 * @return
	 */
	public Set<BpmIdentity> getUserByOrgParamsAlias(String alias) {
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		ObjectNode org =  ucFeignService.getMainGroup(userId); 
		if(org==null){
			 return new LinkedHashSet<BpmIdentity>();
		}
		String value = paramService.getParamByGroup(org.get("groupId").asText(), alias)+"";
		List<IUser> list = new ArrayList<IUser>();
		IUser iUser = userService.getUserById(value);
		list.add(iUser);
		Set<BpmIdentity> set = convertUserList(list);
		return set;
	}
	
}
