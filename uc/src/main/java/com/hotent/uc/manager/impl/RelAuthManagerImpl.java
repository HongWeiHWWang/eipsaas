package com.hotent.uc.manager.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.RelAuthDao;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.RelAuthManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserRelManager;
import com.hotent.uc.model.RelAuth;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.RelAuthVo;
import com.hotent.uc.util.OrgUtil;

/**
 * 
 * <pre> 
 * 描述：分级汇报线管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class RelAuthManagerImpl extends BaseManagerImpl <RelAuthDao, RelAuth> implements RelAuthManager{
	
	
	@Autowired
	OrgManager orgService;
	@Autowired
	UserManager userService;
	@Autowired
	DemensionManager demService;
	@Autowired
	UserRelManager userRelService;
	@Autowired
	PortalFeignService portalFeignService;
	
	@Override
	public List<RelAuth> getAllRelAuth(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		//PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		IPage<RelAuth> page = convert2IPage(pageBean);
		
		return baseMapper.getAllRelAuth(page,convert2Wrapper(queryFilter, currentModelClass()));
	}
	@Override
	public RelAuth getByRelIdAndUserId(String orgId,String userId) {
		return baseMapper.getByRelIdAndUserId(orgId,userId);
	}
	
	@Override
	public List<RelAuth> getByUserId(String userId) {
		List<RelAuth> groupAuthList = baseMapper.getByUserId(userId);
		List<RelAuth> authList = new ArrayList<RelAuth>();
		//若分级管理中，两者对应的汇报线是父子关系，且父子分配的管理权限一致，那么子汇报线不返回前台
		for(RelAuth auth : groupAuthList){
			boolean isChild = false;
			for(RelAuth groupAuth : groupAuthList){
				//判断是父子关系
				boolean flag1 = (auth.getId()!= groupAuth.getId()) && auth.getTypeId().equals(groupAuth.getTypeId()) && auth.getRelPath().startsWith(groupAuth.getRelPath());
				//判断父子权限是否一致
				boolean flag2 = false;
				if(flag1 && flag2)
					isChild = true;
			}
			if(!isChild)authList.add(auth); 
		}
		
		return authList;
	}
	@Override
    @Transactional
	public CommonResult<String> addRelAuth(RelAuthVo relAuthVo)
			throws Exception {
		if(StringUtil.isEmpty(relAuthVo.getAccount())){
			throw new RequiredException("添加分级汇报线失败，用户账号【account】必填！");
		}
		if(StringUtil.isEmpty(relAuthVo.getRelCode())){
			throw new RequiredException("添加分级汇报线失败，汇报线编码【orgCode】必填！");
		}
		RelAuth relAuth = getRelAuthCheck(relAuthVo.getAccount(), relAuthVo.getRelCode(),relAuthVo,"");
		relAuth.setId(UniqueIdUtil.getSuid());
		this.create(relAuth);
		return new CommonResult<String>(true, "添加分级汇报线成功！", "");
	}
	
	@Override
    @Transactional
	public CommonResult<String> addRelAuths(String code,String accounts)
			throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RequiredException("添加分级汇报线管理员失败，汇报节点编码【code】必填！");
		}
		if(StringUtil.isEmpty(accounts)){
			throw new RequiredException("添加分级汇报线管理员失败，管理员账号【accounts】必填！");
		}
		UserRel rel = userRelService.getByAlias(code);
		if(BeanUtils.isEmpty(rel)){
			throw new RequiredException("添加分级汇报线管理员失败，编码为【"+code+"】的汇报节点不存在！");
		}
		String[] accountList = accounts.split(",");
		for (String account : accountList) {
			User user = userService.getByAccount(account);
			if(BeanUtils.isNotEmpty(account)){
				if(BeanUtils.isEmpty(this.getByRelIdAndUserId(rel.getId(), user.getId()))){
					RelAuth relAuth = new RelAuth();
					relAuth.setId(UniqueIdUtil.getSuid());
					relAuth.setRelId(rel.getId());
					relAuth.setUserId(user.getId());
					relAuth.setTypeId(rel.getTypeId());
					this.create(relAuth);
				}
			}
		}
		return new CommonResult<String>(true, "添加分级汇报线管理员成功！", "");
	}
	
	@Override
    @Transactional
	public CommonResult<String> updateRelAuth(RelAuthVo relAuthVo)
			throws Exception {
		if(StringUtil.isEmpty(relAuthVo.getAccount())){
			throw new RequiredException("更新分级汇报线失败，用户账号【account】必填！");
		}
		if(StringUtil.isEmpty(relAuthVo.getRelCode())){
			throw new RequiredException("更新分级汇报线失败，汇报线编码【relCode】必填！");
		}
		RelAuth relAuth = getRelAuthCheck(relAuthVo.getAccount(), relAuthVo.getRelCode(),null,relAuthVo.getNewAccount());
		this.update(relAuth);
		return new CommonResult<String>(true, "更新分级汇报线成功！", "");
	}
	@Override
    @Transactional
	public CommonResult<String> delRelAuth(String relCode,String accounts) throws Exception {
		if(StringUtil.isEmpty(relCode)){
			throw new RequiredException("删除分级汇报线管理员失败，汇报线编码【relCode】必填！");
		}
		if(StringUtil.isEmpty(accounts)){
			throw new RequiredException("删除分级汇报线管理员失败，用户账号【accounts】必填！");
		}
		String[] accountList = accounts.split(",");
		for (String account : accountList) {
			RelAuth relAuth = getRelAuthCheck(account, relCode,null,"");
			this.remove(relAuth.getId());
		}
		return new CommonResult<String>(true, "删除分级汇报线管理员成功！", "");
	}
	@Override
	public RelAuth getRelAuth(String account, String relCode) throws Exception {
		RelAuth relAuth = getRelAuthCheck(account, relCode,null,"");
		return relAuth;
	}
	
	private RelAuth getRelAuthCheck(String account, String relCode,RelAuthVo relAuthVo,String newAccount) throws Exception{
		User user = userService.getByAccount(account);
		if(BeanUtils.isEmpty(user)){
			throw new RequiredException("用户账号【"+account+"】不存在！");
		}
		UserRel rel = userRelService.getByAlias(relCode);
		if(BeanUtils.isEmpty(rel)){
			throw new RequiredException("汇报线节点编码【"+relCode+"】不存在！");
		}
		RelAuth relAuth = baseMapper.getByRelIdAndUserId(rel.getId(), user.getId());
		if(BeanUtils.isNotEmpty(relAuthVo)){
			if(BeanUtils.isNotEmpty(relAuth)){
				throw new RequiredException("用户账号【"+account+"】，汇报线编码【"+relCode+"】的分级汇报线已存在！");
			}
			ObjectNode proType = portalFeignService.getSysTypeById(relAuthVo.getTypeCode());
			if(BeanUtils.isEmpty(proType)){
				throw new RequiredException("编码或id为【"+relAuthVo.getTypeCode()+"】的汇报线分类不存在！");
			}
			if(!rel.getTypeId().equals(proType.get("typeId").asText())){
				throw new RequiredException("输入的维度与汇报线所对应的维度不一致！");
			}
			relAuth = new RelAuth();
			relAuth.setRelId(rel.getId());		
			relAuth.setTypeId(rel.getTypeId());
			relAuth.setUserId(user.getId());
		}else{
			if(BeanUtils.isEmpty(relAuth)){
				throw new RequiredException("根据用户账号【"+account+"】，汇报线编码【"+relCode+"】未找到对应分级汇报线！");
			}
			if (StringUtil.isNotEmpty(newAccount) && !newAccount.equals(account)) {
				User newuser = userService.getByAccount(newAccount);
				if(BeanUtils.isEmpty(newuser)){
					throw new RequiredException("用户账号【"+newAccount+"】不存在！");
				}
				RelAuth relAuth1 = baseMapper.getByRelIdAndUserId(rel.getId(), newuser.getId());
				if(BeanUtils.isNotEmpty(relAuth1)){
					throw new RequiredException("用户账号【"+newAccount+"】，汇报线编码【"+relCode+"】的分级汇报线已存在，请选择呢其它用户！");
				}
				relAuth.setUserId(newuser.getId());
				relAuth.setAccount(newuser.getAccount());
				relAuth.setFullname(newuser.getFullname());
			}
		}
		return relAuth;
	}
	
	@Override
	public List<RelAuth> getRelAuthsByTypeAndUser(String typeId, String userId) throws Exception {
		if(StringUtil.isEmpty(typeId)||StringUtil.isEmpty(userId)){
			throw new RequiredException("用户id和分类id不能为空！");
		}
		return baseMapper.getRelAuthsByTypeAndUser(typeId,userId);
	}
	
	@Override
	public List<RelAuth> getRelAuthByTime(OrgExportObject exportObject)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		return baseMapper.queryOnSync(convert2Wrapper(queryFilter, currentModelClass()));
	}
	@Override
    @Transactional
	public void delByRelId(String relId) {
		baseMapper.delByRelId(relId,LocalDateTime.now());
	}
	@Override
	public PageList<RelAuth> queryRelAuth(QueryFilter filter) {
		//PageBean pageBean = filter.getPageBean();
		// 设置分页
		//PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		PageList<RelAuth> orgAuthList=this.query(filter);
		return orgAuthList;
	}
	@Override
	public List<RelAuth> getLayoutRelAuth(String userId) {
		return baseMapper.getLayoutRelAuth(userId);
	}
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageList<RelAuth> query(QueryFilter<RelAuth> queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		queryFilter.addFilter("a.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("b.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("c.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		
		IPage<RelAuth> result=baseMapper.query(convert2IPage(pageBean), convert2Wrapper(queryFilter, currentModelClass()));
		return new PageList<RelAuth>(result);
	}
	
	@Override
	public RelAuth get(Serializable id) {
		return baseMapper.get(id);
	}
}
