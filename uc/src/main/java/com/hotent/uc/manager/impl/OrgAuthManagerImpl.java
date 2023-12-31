package com.hotent.uc.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.OrgAuthDao;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.User;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.OrgAuthVo;
import com.hotent.uc.util.OrgUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * <pre> 
 * 描述：分级组织管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class OrgAuthManagerImpl extends BaseManagerImpl <OrgAuthDao, OrgAuth> implements OrgAuthManager{
	@Autowired
	OrgManager orgService;
	@Autowired
	UserManager userService;
	@Autowired
	DemensionManager demService;
	
	
	@Override
	public IPage<OrgAuth> getAllOrgAuth(QueryFilter<OrgAuth> queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		IPage<OrgAuth> page = new Page<OrgAuth>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
    	queryFilter.addFilter("a.IS_DELE_", "1", QueryOP.NOT_EQUAL);
    	queryFilter.addFilter("b.IS_DELE_", "1", QueryOP.NOT_EQUAL);
    	queryFilter.addFilter("c.IS_DELE_", "1", QueryOP.NOT_EQUAL);
    	queryFilter.addFilter("d.IS_DELE_", "1", QueryOP.NOT_EQUAL);
    	return  baseMapper.getAllOrgAuth(page,convert2Wrapper(queryFilter, currentModelClass()));
	}
	
	
	@Override
	public OrgAuth getByOrgIdAndUserId(String orgId,String userId) {
		return baseMapper.getByOrgIdAndUserId(orgId,userId);
	}
	@Override
	public List<OrgAuth> getListByOrgIdAndUserId(String orgId,String userId){
		return baseMapper.getListByOrgIdAndUserId(orgId,userId);
	}
	@Override
	public List<OrgAuth> getLayoutOrgAuth(String userId) {
		return baseMapper.getLayoutOrgAuth(userId);
	}
	@Override
	public List<OrgAuth> getByUserId(String userId) {
		List<OrgAuth> groupAuthList = baseMapper.getByUserId(userId);
		List<OrgAuth> authList = new ArrayList<OrgAuth>();
		//若分级管理中，两者对应的组织是父子关系，且父子分配的管理权限一致，那么子组织不返回前台
		for(OrgAuth auth : groupAuthList){
			boolean isChild = false;
			for(OrgAuth groupAuth : groupAuthList){
				//判断是父子关系
				boolean flag1 = (auth.getId()!= groupAuth.getId()) && auth.getDemId().equals(groupAuth.getDemId()) && auth.getOrgPath().startsWith(groupAuth.getOrgPath());
				//判断父子权限是否一致
//				boolean flag2 = false;
//				if (groupAuth.getOrgPerms().equals(auth.getOrgPerms())
//						&& groupAuth.getUserPerms().equals(auth.getUserPerms())
//						&& groupAuth.getOrgauthPerms().equals(auth.getOrgauthPerms())
//						&& groupAuth.getPosPerms().equals(auth.getPosPerms())){
//					flag2 = true;
//				}
				if(flag1)
					isChild = true;
			}
			if(!isChild)authList.add(auth); 
		}
		
		return authList;
	}
	@Override
    @Transactional
	public CommonResult<String> addOrgAuth(OrgAuthVo orgAuthVo)
			throws Exception {
		if(StringUtil.isEmpty(orgAuthVo.getAccount())){
			throw new RequiredException("添加分级组织失败，用户账号【account】必填！");
		}
		if(StringUtil.isEmpty(orgAuthVo.getOrgCode())){
			throw new RequiredException("添加分级组织失败，组织编码【orgCode】必填！");
		}
		String[] accounts = orgAuthVo.getAccount().split(",");
		for (String account : accounts) {
			OrgAuth orgAuth = getOrgAuthCheck(account, orgAuthVo.getOrgCode(),orgAuthVo,"");
			if(BeanUtils.isNotEmpty(orgAuth)){
				orgAuth.setId(UniqueIdUtil.getSuid());
				this.create(orgAuth);
			}
		}
		return new CommonResult<String>(true, "添加分级组织成功！", "");
	}
	@Override
    @Transactional
	public CommonResult<String> updateOrgAuth(OrgAuthVo orgAuthVo)
			throws Exception {
		if(StringUtil.isEmpty(orgAuthVo.getAccount())){
			throw new RequiredException("更新分级组织失败，用户账号【account】必填！");
		}
		if(StringUtil.isEmpty(orgAuthVo.getOrgCode())){
			throw new RequiredException("更新分级组织失败，组织编码【orgCode】必填！");
		}
		OrgAuth orgAuth = getOrgAuthCheck(orgAuthVo.getAccount(), orgAuthVo.getOrgCode(),null,orgAuthVo.getNewAccount());
		orgAuth.setOrgPerms(orgAuthVo.getOrgPerms());
		orgAuth.setUserPerms(orgAuthVo.getUserPerms());
		orgAuth.setPosPerms(orgAuthVo.getPosPerms());
		orgAuth.setOrgauthPerms(orgAuthVo.getOrgauthPerms());
		orgAuth.setLayoutPerms(orgAuthVo.getLayoutPerms());
		this.update(orgAuth);
		return new CommonResult<String>(true, "更新分级组织成功！", "");
	}
	@Override
    @Transactional
	public CommonResult<String> delOrgAuth(String account, String orgCode) throws Exception {
		OrgAuth orgAuth = getOrgAuthCheck(account, orgCode,null,"");
		this.remove(orgAuth.getId());
		return new CommonResult<String>(true, "删除分级组织成功！", "");
	}
	@Override
	public OrgAuth getOrgAuth(String account, String orgCode) throws Exception {
		OrgAuth orgAuth = getOrgAuthCheck(account, orgCode,null,"");
		return orgAuth;
	}
	
	private OrgAuth getOrgAuthCheck(String account, String orgCode,OrgAuthVo orgAuthVo,String newAccount) throws Exception{
		User user = userService.getByAccount(account);
		if(BeanUtils.isEmpty(user)){
			throw new RequiredException("用户账号【"+account+"】不存在！");
		}
		Org org = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(org)){
			throw new RequiredException("组织编码【"+orgCode+"】不存在！");
		}
		OrgAuth orgAuth = baseMapper.getByOrgIdAndUserId(org.getId(), user.getId());
		if(BeanUtils.isNotEmpty(orgAuthVo)){
			if(BeanUtils.isNotEmpty(orgAuth)){
				//throw new RequiredException("用户账号【"+account+"】，组织编码【"+orgCode+"】的分级组织已存在！");
				return null;
			}
			Demension dem = demService.getByCode(orgAuthVo.getDemCode());
			if(BeanUtils.isEmpty(dem)){
				throw new RequiredException("编码【"+orgAuthVo.getDemCode()+"】的维度不存在！");
			}
			if(!org.getDemId().equals(dem.getId())){
				throw new RequiredException("输入的维度与组织所对应的维度不一致！");
			}
			orgAuth = OrgAuthVo.parse(orgAuthVo);
			orgAuth.setOrgId(org.getId());		
			orgAuth.setDemId(org.getDemId());
			orgAuth.setUserId(user.getId());
		}else{
			if(BeanUtils.isEmpty(orgAuth)){
				throw new RequiredException("根据用户账号【"+account+"】，组织编码【"+orgCode+"】未找到对应分级组织！");
			}
			if (StringUtil.isNotEmpty(newAccount) && !newAccount.equals(account)) {
				User newuser = userService.getByAccount(newAccount);
				if(BeanUtils.isEmpty(newuser)){
					throw new RequiredException("用户账号【"+newAccount+"】不存在！");
				}
				OrgAuth orgAuth1 = baseMapper.getByOrgIdAndUserId(org.getId(), newuser.getId());
				if(BeanUtils.isNotEmpty(orgAuth1)){
					throw new RequiredException("用户账号【"+newAccount+"】，组织编码【"+orgCode+"】的分级组织已存在，请选择呢其它用户！");
				}
				orgAuth.setUserAccount(newAccount);
				orgAuth.setUserId(newuser.getId());
				orgAuth.setUserName(newuser.getFullname());
			}
		}
		return orgAuth;
	}
//	@Override
//	public List<OrgAuth> getAllOrgAuth(Map<String, Object> params) {
//		IPage<OrgAuth> page = new Page<OrgAuth>(0, Integer.MAX_VALUE);
//		return baseMapper.getAllOrgAuth(page,params);
//	}

	
	@Override
	public List<OrgAuth> getOrgAuthListByDemAndUser(String demCode, String account) throws Exception {
		if(StringUtil.isEmpty(demCode)||StringUtil.isEmpty(account)){
			throw new RequiredException("用户账号或维度编码不能为空！");
		}
		return baseMapper.getOrgAuthListByDemAndUser(demCode, account);
	}
	
	@Override
	public List<OrgAuth> getOrgAuthByTime(OrgExportObject exportObject)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		return baseMapper.queryOnSync(convert2Wrapper(queryFilter, currentModelClass()));
	}
	@Override
    @Transactional
	public void delByOrgId(String orgId) {
		baseMapper.delByOrgId(orgId,LocalDateTime.now());
	}
	@Override
	public PageList<OrgAuth> queryOrgAuth(QueryFilter<OrgAuth> filter) {
		// 设置分页
		PageList<OrgAuth> query = this.query(filter);
		return query;
	}


	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageList<OrgAuth> query(QueryFilter<OrgAuth> queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		IPage<OrgAuth> result=baseMapper.query(convert2IPage(pageBean), convert2Wrapper(queryFilter, currentModelClass()));
		return new PageList<OrgAuth>(result);
	}
}
