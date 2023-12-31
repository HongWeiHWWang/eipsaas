package com.hotent.uc.api.impl.model.permission;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IPermission;

/**
 * 类 {@code OrgPermission} 组织权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class OrgPermission  extends GroupPermission  implements IPermission {

	@Resource
	UCFeignService ucFeignService;
	
	@Override
	public String getTitle() {
		
		return GroupTypeConstant.ORG.label();
	}


	@Override
	public String getType() {
		return GroupTypeConstant.ORG.key();
	}
	
	@Override
	public Set<String> getCurrentProfile() {
		// 只取登录用户的当前部门ID
		Set<String> userSet=new HashSet<String>();
		ArrayNode array = ucFeignService.getOrgListByUserId(ContextUtil.getCurrentUserId());
		if(BeanUtils.isNotEmpty(array)){
			for (JsonNode jsonNode : array) {
				userSet.add(jsonNode.get("id").asText());
			}
		}
		return userSet;
	}

}
