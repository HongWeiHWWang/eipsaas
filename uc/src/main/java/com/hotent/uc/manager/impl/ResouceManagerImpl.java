package com.hotent.uc.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.ResouceDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgRoleManager;
import com.hotent.uc.manager.ResouceManager;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgRole;
import com.hotent.uc.model.Resouce;
import com.hotent.uc.model.Role;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.resouce.ResouceVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre>
 *  
 * 描述：维度管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-19 15:30:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class ResouceManagerImpl extends BaseManagerImpl <ResouceDao, Resouce> implements ResouceManager {
	

	@Override
    @Transactional
	public CommonResult<String> saveResouce(ResouceVo resouceVo) {
		if (BeanUtils.isEmpty(resouceVo.getRoleCode())) {
			throw new RequiredException(HotentHttpStatus.REUIRED.description() + "：角色编码必填！");
		}
		RoleManager roleService = AppUtil.getBean(RoleManager.class);
		Role role = roleService.getByAlias(resouceVo.getRoleCode());
		if (BeanUtils.isEmpty(role)) {
			throw new RuntimeException("编码为【" + resouceVo.getRoleCode() + "】的角色不存在!");
		}
		QueryFilter queryFilter = QueryFilter.build();
		//queryFilter.setClazz(Resouce.class);
		queryFilter.addFilter("ROLE_ID_", role.getId(), QueryOP.EQUAL, FieldRelation.AND);
		List<Resouce> list = this.query(queryFilter.getParams());
		Resouce resouce = null;
		if (list.size() > 0) {
			resouce = list.get(0);
			resouce.setResouce(resouceVo.getResouce());
			this.update(resouce);

		} else {
			resouce = new Resouce();
			resouce.setId(UniqueIdUtil.getSuid());
			resouce.setResouce(resouceVo.getResouce());
			resouce.setRoleId(role.getId());
			this.create(resouce);
		}
		return new CommonResult<String>(true, "保存成功！", "");
	}

	@Override
	public Resouce getByRoleCode(String roleCode) {
		if (BeanUtils.isEmpty(roleCode)) {
			throw new RequiredException(HotentHttpStatus.REUIRED.description() + "：角色编码必填！");
		}
		RoleManager roleService = AppUtil.getBean(RoleManager.class);
		Role role = roleService.getByAlias(roleCode);
		if (BeanUtils.isEmpty(role)) {
			throw new RuntimeException("编码为【" + roleCode + "】的角色不存在!");
		}
		QueryFilter queryFilter = QueryFilter.build();
		//queryFilter.setClazz(Resouce.class);
		queryFilter.addFilter("ROLE_ID_", role.getId(), QueryOP.EQUAL, FieldRelation.AND);
		List<Resouce> list = this.query(queryFilter.getParams());
		return list.size() > 0 ? list.get(0) : new Resouce();
	}
	
	/**
	 * 通用查询
	 */
	public List<Resouce> query(Map<String, Object> Params){
		List<Resouce> query = this.query(Params);
		return query;
	}
   
	@Override
	public String getResouceByAccount(String account) {
		List<Resouce> list = baseMapper.getResouceByAccount(account);
		OrgManager service = AppUtil.getBean(OrgManager.class);
		OrgRoleManager orgRoleService = AppUtil.getBean(OrgRoleManager.class);
		Set<String> roleIdSet = new HashSet<String>();
		try {
			List<Org> orglist = service.getOrgListByAccount(account);
			PageBean pageBean = new PageBean(1, 1000);
			for (Org org : orglist) {
				if (BeanUtils.isNotEmpty(org)) {
					QueryFilter queryFilter = QueryFilter.build();
					//queryFilter.setClazz(OrgRole.class);
					queryFilter.addParams("path", org.getPath());
					queryFilter.setPageBean(pageBean);
					List<OrgRole> orgRoles = orgRoleService.query(queryFilter).getRows();
					for (OrgRole orgRole : orgRoles) {
						if (BeanUtils.isNotEmpty(orgRole))
							roleIdSet.add(orgRole.getRoleId());
					}
				}
			}
			if (roleIdSet.size() > 0) {
				QueryFilter rQueryFilter = QueryFilter.build();
				//rQueryFilter.setClazz(Resouce.class);
				List<String> roleIdlist = new ArrayList<String>(roleIdSet);// Fixed-size
				rQueryFilter.addFilter("ROLE_ID_", roleIdlist, QueryOP.IN, FieldRelation.AND);
				Map<String, Object> map=rQueryFilter.getParams();
				List<Resouce> list1 = this.query(map);
				list.addAll(list1);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Set<String> resouceSet = new HashSet<String>();
		for (Resouce resouce : list) {
			if (BeanUtils.isNotEmpty(resouce.getResouce())) {
				String[] resouceArr = resouce.getResouce().split(",");
				for (String string : resouceArr) {
					resouceSet.add(string);
				}
			}
		}
		List<String> resoucelist = new ArrayList<String>(resouceSet);
		String resouceStr = StringUtil.join(resoucelist, ",");
		return resouceStr;

	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}

}
