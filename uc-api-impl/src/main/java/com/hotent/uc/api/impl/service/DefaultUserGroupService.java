package com.hotent.uc.api.impl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.model.Org;
import com.hotent.uc.api.impl.model.OrgJob;
import com.hotent.uc.api.impl.model.OrgRel;
import com.hotent.uc.api.impl.model.Role;
import com.hotent.uc.api.model.Group;
import com.hotent.uc.api.model.GroupType;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.service.IUserGroupService;


/**
 * 类 {@code DefaultUserGroupService} 用户与组关系的实现：通过用户找组，通过组找人等
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年1月4日
 */
@Primary
@Service
public class DefaultUserGroupService implements IUserGroupService {
	@Resource
	UCFeignService ucFeignService;

	@Override
	public List<GroupType> getGroupTypes() {
		List<GroupType> list = new ArrayList<GroupType>();
		for (GroupTypeConstant e : GroupTypeConstant.values()) {
			GroupType type = new GroupType(e.key(), e.label());
			list.add(type);
		}
		return list;
	}

	@Override
	public IGroup getGroupByIdOrCode(String groupType, String code) {
		ObjectNode result= JsonUtil.getMapper().createObjectNode();
		try {
			if (groupType.equals(GroupTypeConstant.ORG.key())) {
				result = ucFeignService.getOrgByIdOrCode(code);
				if(BeanUtils.isNotEmpty(result))
				return JsonUtil.toBean(result, Org.class);
			}
			if (groupType.equals(GroupTypeConstant.ROLE.key())) {
				CommonResult<ObjectNode> commonResult  = ucFeignService.getRoleByIdOrCode(code);
				if(BeanUtils.isNotEmpty(commonResult.getValue()))
				return JsonUtil.toBean(commonResult.getValue(), Role.class);
			}
			if (groupType.equals(GroupTypeConstant.POSITION.key())) {
				CommonResult<ObjectNode> commonResult  = ucFeignService.getPostByIdOrCode(code);
				if(BeanUtils.isNotEmpty(commonResult.getValue()))
				return JsonUtil.toBean(commonResult.getValue(), OrgRel.class);
			}
			if (groupType.equals(GroupTypeConstant.JOB.key())) {
				CommonResult<ObjectNode> commonResult = ucFeignService.getJobByOrgCode(code);
				if(BeanUtils.isNotEmpty(commonResult.getValue()))
				return JsonUtil.toBean(commonResult.getValue(), OrgJob.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<IGroup> getGroupsByUserIdOrAccount(String groupType, String userId) {
		List<IGroup> listMap = new ArrayList<IGroup>();
		List<Group> groupIdsByUserId = ucFeignService.getGroupsByUidAndType(userId,groupType);
		for (Group group : groupIdsByUserId) {
			listMap.add((IGroup)group);
		}
		return listMap;
	}

	@Override
	public List<IGroup> getGroupsByUserIdOrAccount(String account) {
        List<IGroup> listMap = new ArrayList<IGroup>();
        List<Group> groupIdsByUserId = ucFeignService.getGroupsByUidAndType(account, "all");
        for (Group group : groupIdsByUserId) {
            listMap.add((IGroup) group);
        }
        return listMap;
    }

	@Override
	public Map<String, List<IGroup>> getGroupsMapUserIdOrAccount(String id) {
		List<IGroup> groups = this.getGroupsByUserIdOrAccount(id);
		Map<String,List<IGroup>> map=new HashMap<String, List<IGroup>>();
		if(BeanUtils.isEmpty(groups)) return map;
		for(IGroup group:groups){
			String type=group.getGroupType();
			List<IGroup> list = null;
			if(map.containsKey(type)){
				list=map.get(type);
			}else{
				list = new ArrayList<>();
			}
			list.add(group);
			map.put(type,list);
		}
		return map;
	}
}
