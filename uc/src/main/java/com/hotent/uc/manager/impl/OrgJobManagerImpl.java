package com.hotent.uc.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.OrgJobDao;
import com.hotent.uc.dao.OrgPostDao;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.OrgJobManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.User;
import com.hotent.uc.params.job.JobVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.OrgUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：组织关系定义 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-29 18:00:43
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class OrgJobManagerImpl extends BaseManagerImpl<OrgJobDao, OrgJob> implements OrgJobManager{
	
	
	@Autowired
	OrgPostDao orgPostDao;
	@Autowired
	UserManager userService;
	
	public OrgJob getByCode(String code) {
    	return  baseMapper.getByCode(code);
	}

	@Override
	public List<OrgJob> getByName(String name) {
		return baseMapper.getByName(name);
	}

	@Override
    @Transactional
	public void removeByIds(String... ids) {
		for (String id : ids) {
			List<OrgPost> relList = orgPostDao.getByReldefId(id);
			OrgJob reldef = this.getById(id);
			if(BeanUtils.isNotEmpty(relList)){
				ThreadMsgUtil.addMsg(reldef.getName()+"已与岗位关联，不能删除！");
				continue;
			}
		}
		if(StringUtil.isEmpty(ThreadMsgUtil.getMessage(false))){
			super.removeByIds(ids);
		}
	}
	@Override
	public List<OrgJob> getListByUserId(String userId) {
		List<OrgJob> OrgJobs = new ArrayList<OrgJob>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<OrgPost> orgPosts = orgPostDao.getRelListByParam(map);
		if(BeanUtils.isEmpty(orgPosts)) return OrgJobs;
		for (OrgPost orgPost : orgPosts) {
			OrgJob OrgJob = this.get(orgPost.getRelDefId());
			OrgJobs.add(OrgJob);
		}
		OrgUtil.removeDuplicate(OrgJobs,"code");
		return OrgJobs;
	}
	
	@Override
	public List<OrgJob> getListByAccount(String account) {
		List<OrgJob> OrgJobs = new ArrayList<OrgJob>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("account", account);
		List<OrgPost> orgPosts = orgPostDao.getRelListByParam(map);
		if(BeanUtils.isEmpty(orgPosts)) return OrgJobs;
		for (OrgPost orgPost : orgPosts) {
			OrgJob OrgJob = this.get(orgPost.getRelDefId());
			OrgJobs.add(OrgJob);
		}
		return OrgJobs;
	}

	@Override
    @Transactional
	public CommonResult<String> addJob(JobVo jobVo)
			throws Exception {
		if(StringUtil.isEmpty(jobVo.getName())){
			throw new RequiredException("添加职务失败，职务名称【name】必填！");
		}
		if(StringUtil.isEmpty(jobVo.getCode())){
			throw new RequiredException("添加职务失败，职务编码【code】必填！");
		}
		if(jobVo.getCode().contains(",")){
			throw new RuntimeException("职务编码不能包含英文逗号‘,’");
		}
		if(baseMapper.getCountByCode(jobVo.getCode())>0){
			return new CommonResult<String>(false, "添加职务失败，职务编码【"+jobVo.getCode()+"】在系统中已存在！", "");
		}
		OrgJob job = new OrgJob();
		job.setName(jobVo.getName());
		job.setCode(jobVo.getCode());
		job.setDescription(jobVo.getDescription());
		job.setId(UniqueIdUtil.getSuid());
		job.setPostLevel(jobVo.getPostLevel());
		this.create(job);
		return new CommonResult<String>(true, "添加职务成功！", "");
	}


	@Override
    @Transactional
	public CommonResult<String> deleteJob(String codes) throws Exception {
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		StringBuilder posStr = new StringBuilder();
		boolean isTrue = true;
		for (String code : codeArray) {
			OrgJob job = getByCode(code);
			if(BeanUtils.isNotEmpty(job)){
				List<OrgPost> orgPosts = orgPostDao.getByReldefId(job.getId());
				if(BeanUtils.isEmpty(orgPosts)){
					remove(job.getId());
				}else{
					posStr.append(code);
					posStr.append("，");
					isTrue = false;
				}
			}else{
				isTrue = false;
				str.append(code);
				str.append("，");
			}
		}
		String msg = isTrue?"删除职务成功！":"部分删除失败，";
		if (!isTrue) {
			if(str.length()>0) msg+="职务编码【"+str.toString().substring(0, str.length()-1)+"】不存在！";
			if(posStr.length()>0) msg+="职务编码【"+posStr.toString().substring(0, posStr.length()-1)+"】下存在岗位，不允许删除！";;
		}
		return new CommonResult<String>(isTrue, msg, str.toString());
	}

	@Override
    @Transactional
	public CommonResult<String> deleteJobByIds(String ids) {
		String[] idArray = ids.split(",");
		StringBuilder str = new StringBuilder();
		StringBuilder posStr = new StringBuilder();
		boolean isTrue = true;
		for (String id : idArray) {
			OrgJob job = get(id);
			if(BeanUtils.isNotEmpty(job)){
				List<OrgPost> orgPosts = orgPostDao.getByReldefId(job.getId());
				if(BeanUtils.isEmpty(orgPosts)){
					remove(job.getId());
				}else{
					posStr.append(job.getName());
					posStr.append("，");
					isTrue = false;
				}
			}else{
				isTrue = false;
				str.append(job.getName());
				str.append("，");
			}
		}
		String msg = isTrue?"删除职务成功！":"部分删除失败，";
		if (!isTrue) {
			if(str.length()>0) msg+="职务编码【"+str.toString().substring(0, str.length()-1)+"】不存在！";
			if(posStr.length()>0) msg+="职务编码【"+posStr.toString().substring(0, posStr.length()-1)+"】下存在岗位，不允许删除！";;
		}
		return new CommonResult<String>(isTrue, msg, str.toString());
	}

	@Override
    @Transactional
	public CommonResult<String> updateJob(JobVo jobVo)
			throws Exception {
		if(StringUtil.isEmpty(jobVo.getCode())){
			throw new RequiredException("更新职务失败，职务编码【code】必填！");
		}
		OrgJob job = getByCode(jobVo.getCode());
		if(BeanUtils.isEmpty(job)){
			return new CommonResult<String>(false, "更新职务失败，职务编码【"+jobVo.getCode()+"】不存在！", "");
		}
		if(StringUtil.isNotEmpty(jobVo.getName())){
			job.setName(jobVo.getName());
		}
		if(jobVo.getDescription()!=null){
			job.setDescription(jobVo.getDescription());
		}
		if(jobVo.getPostLevel() !=null){
			job.setPostLevel(jobVo.getPostLevel());
		}
		this.update(job);
		return new CommonResult<String>(true, "更新职务成功！", "");
	}


	@Override
	public List<UserVo> getUsersByJob(String codes) throws Exception {
		List<User> list = new ArrayList<User>();
		String[] codeArray = codes.split(",");
		for(String code : codeArray){
			OrgJob job = getByCode(code);
			String jobId = "";
			if(BeanUtils.isNotEmpty(job)){
				jobId = job.getId();
			}else{
				jobId = code;
			}
			List<User> users = userService.getListByJobId(jobId);
			if(BeanUtils.isNotEmpty(users)){
				list.addAll(users);
			}
		}
		OrgUtil.removeDuplicate(list);
		return OrgUtil.convertToUserVoList(list);
	}
	
	@Override
	public List<OrgJob> getJobByTime(String btime, String etime)
			throws Exception {
		QueryFilter<OrgJob> queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return this.queryNoPage(queryFilter);
	}
	
	@Override
	public CommonResult<Boolean> isCodeExist(String code) throws Exception {
		OrgJob job = baseMapper.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(job);
		return new CommonResult<Boolean>(isExist, isExist?"该职务编码已存在！":"", isExist);
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
	
}
