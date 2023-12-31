package com.hotent.uc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.dao.TenantAuthDao;
import com.hotent.uc.manager.TenantAuthManager;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.manager.TenantTypeManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.TenantAuth;
import com.hotent.uc.model.User;
import com.hotent.uc.params.tenant.TenantAuthAddObject;

/**
 * 
 * <pre> 
 * 描述：租户管理员 处理实现类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:55:39
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("tenantAuthManager")
public class TenantAuthManagerImpl extends BaseManagerImpl<TenantAuthDao, TenantAuth> implements TenantAuthManager{

	@Resource
	TenantTypeManager tenantTypeManager;
	@Resource
	TenantManageManager tenantManager;
	@Resource
	UserManager userManager;
	
	@Override
	public List<TenantAuth> getByTypeAndTenantId(String typeId, String tenantId) {
		return baseMapper.getByTypeAndTenantId(typeId, tenantId);
	}

	@Override
	@Transactional
	public void deleteByTypeId(String typeId) {
		baseMapper.deleteByTypeId(typeId);
	}

	@Override
	@Transactional
	public void deleteByTenantId(String tenantId) {
		baseMapper.deleteByTenantId(tenantId);
	}

	@Override
    @Transactional
	public CommonResult<String> saveTenantAuth(TenantAuthAddObject authAddObject) throws Exception {
		if(StringUtil.isEmpty(authAddObject.getTypeId())){
			return new CommonResult<>(false, "设置租户管理员失败：租户类型id不能为空。");
		}
		if(StringUtil.isEmpty(authAddObject.getAccounts())){
			return new CommonResult<>(false, "设置租户管理员失败：用户账号不能为空。");
		}
		if(BeanUtils.isEmpty(tenantTypeManager.get(authAddObject.getTypeId()))){
			return new CommonResult<>(false, "设置租户管理员失败：租户类型id【"+authAddObject.getTypeId()+"】不存在。");
		}
		if(StringUtil.isNotEmpty(authAddObject.getTenantId()) && BeanUtils.isEmpty(tenantManager.get(authAddObject.getTenantId()))){
			return new CommonResult<>(false, "设置租户管理员失败：租户id【"+authAddObject.getTenantId()+"】不存在。");
		}
		String[] accounts = authAddObject.getAccounts().split(",");
		List<TenantAuth> auths = new ArrayList<TenantAuth>();
		for (String account : accounts) {
			User u = userManager.getByAccount(account);
			if(BeanUtils.isEmpty(u)){
				return new CommonResult<>(false, "设置租户管理员失败：用户账号【"+account+"】不存在。");
			}
			List<TenantAuth> oldAuths = baseMapper.getByUserId(authAddObject.getTypeId(), StringUtil.isNotEmpty(authAddObject.getTenantId())?authAddObject.getTenantId():null, u.getUserId());
			if(BeanUtils.isEmpty(oldAuths)){
				TenantAuth auth= new TenantAuth();
				auth.setTypeId(authAddObject.getTypeId());
				auth.setUserId(u.getId());
				auth.setUserName(u.getFullname());
				auth.setTenantId(StringUtil.isNotEmpty(authAddObject.getTenantId())?authAddObject.getTenantId():"");
				auths.add(auth);
			}
		}
		if(BeanUtils.isNotEmpty(auths)){
			super.saveBatch(auths);
		}else{
			return new CommonResult<>(true, "设置租户管理员操作成功，但未添加任何管理员数据。");
		}
		return new CommonResult<>(true, "设置租户管理员成功。");
	}

	@Override
    @Transactional
	public CommonResult<String> removeTenantAuth(String typeId,String tenantId,String userIds) throws Exception {
		if(StringUtil.isEmpty(typeId)){
			return new CommonResult<>(false, "删除租户管理员失败：租户类型id不能为空。");
		}
		if(StringUtil.isEmpty(userIds)){
			return new CommonResult<>(false, "删除租户管理员失败：用户id不能为空。");
		}
		if(BeanUtils.isEmpty(tenantTypeManager.get(typeId))){
			return new CommonResult<>(false, "删除租户管理员失败：租户类型id【"+typeId+"】不存在。");
		}
		if(StringUtil.isNotEmpty(tenantId) && BeanUtils.isEmpty(tenantManager.get(tenantId))){
			return new CommonResult<>(false, "删除租户管理员失败：租户id【"+tenantId+"】不存在。");
		}
		String[] ids = userIds.split(",");
		List<String> auths = new ArrayList<String>();
		for (String userId : ids) {
			List<TenantAuth> oldAuths = baseMapper.getByUserId(typeId, tenantId, userId);
			if(BeanUtils.isNotEmpty(oldAuths)){
				for (TenantAuth tenantAuth : oldAuths) {
					auths.add(tenantAuth.getId());
				}
			}
		}
		if(BeanUtils.isNotEmpty(auths)){
			super.removeByIds(auths);
		}else{
			return new CommonResult<>(true, "删除租户管理员操作成功，但未删除任何管理员数据。");
		}
		return new CommonResult<>(true, "删除租户管理员成功。");
	}

	@Override
	public List<TenantAuth> getByUserId(String typeId, String tenantId,
			String userId) {
		return baseMapper.getByUserId(typeId, tenantId, userId);
	}

	@Override
	public PageList<TenantAuth> queryByTypeAndTenantId(QueryFilter queryFilter) {
		this.copyQuerysInParams(queryFilter);
    	PageBean pageBean = queryFilter.getPageBean();
    	if(BeanUtils.isEmpty(pageBean)){
    		pageBean = new PageBean(1, Integer.MAX_VALUE, false);
    	}
    	IPage<TenantAuth> query = baseMapper.queryByTypeAndTenantId(convert2IPage(pageBean),convert2Wrapper(queryFilter, currentModelClass()));
        return new PageList<TenantAuth>(query);
	}

    /**
     * 根据用户ID删除租户管理员
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public void delByUserId(@Param("userId")String userId){
        baseMapper.delByUserId(userId);
    }
	
}
