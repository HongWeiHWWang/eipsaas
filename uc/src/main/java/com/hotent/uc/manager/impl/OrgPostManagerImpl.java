package com.hotent.uc.manager.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.dao.OrgPostDao;
import com.hotent.uc.dao.OrgUserDao;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgJobManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgPostManager;
import com.hotent.uc.manager.OrgUserManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：组织关联关系 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class OrgPostManagerImpl extends BaseManagerImpl <OrgPostDao, OrgPost> implements OrgPostManager{

	
	@Autowired
	OrgUserDao orgUserDao;
	@Autowired
	OrgUserManager orgUserService;
	@Autowired
	OrgManager orgService;
	@Autowired
	OrgJobManager orgJobService;
	@Autowired
	DemensionManager demensionService;
	
	public OrgPost getByCode(String code) {
		return this.baseMapper.getByCode(code);
	}
	public List<OrgPost> getListByOrgId(String orgId) {
		return this.baseMapper.getListByOrgId(orgId);
	}
	public List<OrgPost> queryInfoList(QueryFilter queryFilter) {
		return this.baseMapper.queryInfoList(queryFilter);
	}
	public OrgPost getByOrgIdRelDefId(String orgId, String relDefId) {
		return this.baseMapper.getByOrgIdRelDefId(orgId, relDefId);
	}
	
	public List<OrgPost> getListByUserId(String userId,String demId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		if(StringUtil.isNotEmpty(demId)){
			map.put("demId", demId);
		}
		return this.baseMapper.getRelListByParam(map);
	}
	public List<OrgPost> getListByAccount(String account,String demId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("account", account);
		map.put("demId", demId);
		List<OrgPost> orgPosts = baseMapper.getRelListByParam(map);
		for (OrgPost orgPost : orgPosts) {
			Org org = orgService.get(orgPost.getOrgId());
			if(BeanUtils.isNotEmpty(org)){
				orgPost.setOrgName(org.getName());
				Demension dem = demensionService.get(org.getDemId());
				if(BeanUtils.isNotEmpty(dem)){
					orgPost.setDemName(dem.getDemName());
				}
			}
			OrgJob orgJob = orgJobService.get(orgPost.getRelDefId());
			if(BeanUtils.isNotEmpty(orgJob)){
				orgPost.setJobName(orgJob.getName());
			}
		}
		return orgPosts;
	}
	
	@Override
    @Transactional
	 public void removeByIds(String ...ids)  {
		for(String id : ids){
			OrgPost rel = this.get(id);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgId", rel.getOrgId());
			List<OrgUser> orgUserList = orgUserDao.getByParms(map);
			if(BeanUtils.isNotEmpty(orgUserList)){
				removeOrgUser(rel,orgUserList);
			}else{
				this.remove(rel.getId());
			}
		}
	}
	
	/**
	 * 判断组织人员关系是否可删除，并作删除和更新操作
	 * @param rel
	 * @param orgUserList
	 * @throws SQLException 
	 */
    @Transactional
	public void removeOrgUser(OrgPost rel ,List<OrgUser> orgUserList) {
		for(OrgUser user : orgUserList){
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("orgId", rel.getOrgId());
			params.put("userId", user.getUserId());
			List<OrgUser> list = orgUserDao.getByParms(params);//获取组织对应的人员
			Map<String, Object> map=new  HashMap<String,Object>();
			map.put("userId", user.getUserId());
			map.put("relId", rel.getId());
			if(list.size() == 1 && rel.getId().equals(list.get(0).getRelId())){//组织下该人只有一条数据，且刚好是与该岗位关联的情况
				OrgUser orgUser = orgUserDao.getByParms(map).get(0);
				orgUser.setRelId(null);//置空岗位字段数据
				orgUserService.update(orgUser);
			}else if(list.size() > 1){//有多条数据，那直接将该岗位对应的人员数据删掉即可
				if(BeanUtils.isNotEmpty(orgUserDao.getByParms(map))){
					OrgUser orgUser = orgUserDao.getByParms(map).get(0);
					orgUserService.remove(orgUser.getId());
				}
			}
			this.remove(rel.getId());
		}
	}
	@Override
	public List<OrgPost> getByRelDefId(String relDefId) {
		return baseMapper.getByReldefId(relDefId);
	}

	@Override
    @Transactional
	public boolean setRelCharge(String id, boolean isCharge) {
		int charge = isCharge?1:0;
		baseMapper.updateRelCharge(id,charge,LocalDateTime.now());
		return true;
	}

	@Override
    @Transactional
	public boolean cancelRelCharge(String orgId) {
		baseMapper.cancelRelCharge(orgId,LocalDateTime.now());
		return true;
	}
	@Override
	public List<OrgPost> getRelCharge(String orgId, Boolean isCharge) {
//		Integer charge = isCharge?1:0;
		if(BeanUtils.isEmpty(isCharge)){
			return baseMapper.getRelChargeByOrgId(orgId, null);
		}else{
			Integer charge = isCharge?1:0;
			return baseMapper.getRelChargeByOrgId(orgId, charge);
		}
	}

	@Override
    @Transactional
	public void delByOrgId(String orgId) {
		baseMapper.delByOrgId(orgId,LocalDateTime.now());
	}

	@Override
	public PageList<OrgPost> getOrgPost(QueryFilter filter) {
		PageBean pageBean = filter.getPageBean();
		IPage<OrgPost> page = new Page<OrgPost>(0, Integer.MAX_VALUE);
		if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
		filter.addFilter("p.IS_DELE_", "0", QueryOP.EQUAL);
		IPage<OrgPost> orgPost = baseMapper.getOrgPost(page,convert2Wrapper(filter, currentModelClass()));
		return new PageList<OrgPost>(orgPost);
	}

    @Override
    public List<Map<String,Object>> getFullname(String postId) {
        return baseMapper.getFullname(postId);
    }

    @Override
    public List<Map<String,Object>> getPostByJobId(String postId) {
        return baseMapper.getPostByJobId(postId);
    }

    @Override
    public List<Map<String,Object>> getUserByUserId(String userId) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        mapList = baseMapper.getUserByUserId(userId);
        Map<String,Object> map = new HashMap<>();
        List<Map<String, Object>> userJobByUserId = baseMapper.getUserJobByUserId(userId);
        List<String> roleNames = new ArrayList<>();
        for (Map<String, Object> map2 : userJobByUserId) {
        	for (Iterator<Entry<String, Object>> iterator = map2.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Object> ent = iterator.next();
				roleNames.add(ent.getValue().toString());
			}
		}
        ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
        objectNode.put("roleName", StringUtil.join(roleNames, "|"));
        map.put("roleName",objectNode);
        mapList.add(map);
        return mapList;
    }
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}

	@Override
	public List<OrgPost> getByReldefId(String jobId) {
		return baseMapper.getByReldefId(jobId);
	}

	@Override
	public Integer getCountByCode(String code) {
		return baseMapper.getCountByCode(code);
	}

	@Override
	public OrgPost get(Serializable id) {
		return baseMapper.get(id);
	}
	
}
