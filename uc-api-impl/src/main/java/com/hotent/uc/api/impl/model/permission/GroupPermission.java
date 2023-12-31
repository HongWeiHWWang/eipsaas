package com.hotent.uc.api.impl.model.permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserGroupService;

/**
 * 类 {@code GroupPermission} 用户组权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class GroupPermission extends AbstarctPermission {
	
	@Resource
	IUserGroupService defaultUserGroupService;
	@Override
	public String getTitle() {
		return GroupTypeConstant.ORG.name();
	}

	@Override
	public String getType() {
		return GroupTypeConstant.ORG.key();
	}

	

	@Override
	public Set<String> getCurrentProfile() {
		IUser user=ContextUtil.getCurrentUser();
		Map<String, List<IGroup>> groups = defaultUserGroupService.getGroupsMapUserIdOrAccount(user.getUserId());
		if(BeanUtils.isEmpty(groups)) return null;
		Collection<List<IGroup>> list=groups.values();
		Set<String> set=new HashSet<String>();
		for(List<IGroup> listGroup : list){
			List<IGroup> temps=listGroup;
			for(IGroup temp:temps){
				set.add(temp.getGroupId());
			}
		}
		return set;
	}
	


}
