package com.hotent.uc.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.dao.UserRoleDao;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.manager.UserRoleManager;
import com.hotent.uc.model.UserRole;

/**
 * 
 * <pre> 
 * 描述：用户角色管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:28:34
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserRoleManagerImpl extends BaseManagerImpl <UserRoleDao, UserRole> implements UserRoleManager{
	@Resource
	RoleManager roleService;
	
	public UserRole getByRoleIdUserId(String roleId, String userId) {
		 return baseMapper.getByRoleIdUserId(roleId, userId);
	}
	public List<UserRole> getListByUserId(String userId) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("userId", userId);
		QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
		wrapper.getParamNameValuePairs();
		if(params!=null && params.size() > 0) {
			Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
			paramNameValuePairs.putAll(params);
		}
	    return baseMapper.queryByParams(wrapper);
	}
	
	public List<UserRole> getListByRoleId(String roleId) {
		 return baseMapper.getListByRoleId(roleId);
	}
	
	public List<UserRole> getListByAlias(String alias) {
		return baseMapper.getListByCode(alias);
	}
	
	@Override
    @Transactional
	public void saveUserRole(String account, String... roleCodes) throws Exception {
		List<UserRole> userRoles = baseMapper.getByAccount(account);
		Map<String, UserRole> map = new HashMap<String, UserRole>();
		//需要被删除的记录
		List<String> userRoleIds = new ArrayList<String>();
		for (UserRole userRole : userRoles) {
			userRoleIds.add(userRole.getId());
		}
		for (UserRole userRole : userRoles) {
			for (String code : roleCodes) {
				if(code.equals(userRole.getAlias())){
					map.put(code, userRole);
					userRoleIds.remove(userRole.getId());
				}
			}
		}
		if(userRoleIds.size() > 0){
			String[] ids = new String[userRoleIds.size()];
			userRoleIds.toArray(ids);
			// 删除多余的角色
			this.removeByIds(ids);
		}
		Iterator<String> keyIt = map.keySet().iterator();
		ArrayList<String> roleCodeList = new ArrayList<String>(Arrays.asList(roleCodes));
		while(keyIt.hasNext()){
			String code = keyIt.next();
			// 移除数据库中已存在的记录
			roleCodeList.remove(code);
			UserRole userRole = map.get(code);
			userRole.setIsDelete("0");
			// 更新仍然关联的角色
			this.update(userRole);
		}
		// 补齐缺少的角色
		for (String roleCode : roleCodeList) {
			roleService.saveUserRole(roleCode, account);
		}
	}
    @Override
    public PageList<UserRole> getUserRolePage(QueryFilter queryFilter) {
        PageBean pageBean = queryFilter.getPageBean();
        // 设置分页
      //  PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		copyQuerysInParams(queryFilter);
        IPage<UserRole> query = baseMapper.queryByParams(convert2IPage(pageBean),convert2Wrapper(queryFilter, currentModelClass()));
        return new PageList<UserRole>(query);
    }
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
	@Override
    @Transactional
	public void removeByUserId(String id, LocalDateTime now) {
		baseMapper.removeByUserId(id, now);
		
	}
	@Override
    @Transactional
	public void removeByRoleId(String roleId, LocalDateTime now) {
		baseMapper.removeByRoleId(roleId, now);
	}
}
