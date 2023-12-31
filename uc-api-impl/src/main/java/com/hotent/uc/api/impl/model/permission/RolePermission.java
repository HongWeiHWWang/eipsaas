package com.hotent.uc.api.impl.model.permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IPermission;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserGroupService;

/**
 * 类 {@code RolePermission} 角色权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class RolePermission  extends GroupPermission  implements IPermission {

	@Resource
	IUserGroupService defaultUserGroupService;
	
	@Resource
	UCFeignService ucFeignService;
	
	@Override
	public String getTitle() {
		return GroupTypeConstant.ROLE.label();
	}
	@Override
	public String getType() {
		return GroupTypeConstant.ROLE.key();
	}
	
	@Override
	public Set<String> getCurrentProfile() {
		IUser user=ContextUtil.getCurrentUser();
		// 获取当前用户拥有的所有角色
		Set<String> set=new HashSet<String>();
		List<ObjectNode> list = ucFeignService.getRoleListByAccount(user.getAccount());
		if(BeanUtils.isNotEmpty(list)){
			for (ObjectNode objectNode : list) {
				set.add(objectNode.get("id").asText());
			}
		}
		return set;
	}
}
