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
 * 类 {@code PosPermission} 岗位权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class PosPermission extends GroupPermission implements IPermission {

	@Resource
	IUserGroupService defaultUserGroupService;
	@Resource
	UCFeignService ucFeignService;
	
	@Override
	public String getTitle() {
		return GroupTypeConstant.POSITION.label();
	}
	@Override
	public String getType() {
		return "pos";
	}
	
	@Override
	public Set<String> getCurrentProfile() {
		IUser user=ContextUtil.getCurrentUser();
		Set<String> set=new HashSet<String>();
		List<ObjectNode> list = ucFeignService.getPosListByAccount(user.getAccount());
		if(BeanUtils.isNotEmpty(list)){
			for (ObjectNode node : list) {
				set.add(node.get("id").asText());
			}
		}
		return set;
	}
}
