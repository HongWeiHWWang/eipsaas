package com.hotent.uc.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.OrgDao;
import com.hotent.uc.dao.OrgParamsDao;
import com.hotent.uc.dao.ParamsDao;
import com.hotent.uc.dao.UserDao;
import com.hotent.uc.dao.UserParamsDao;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.ParamsManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserParams;
import com.hotent.uc.params.params.ParamVo;
import com.hotent.uc.util.OrgUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织参数 处理实现类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年1月4日
 */
@Service
public class ParamsManagerImpl extends BaseManagerImpl <ParamsDao, Params> implements ParamsManager{
	@Autowired
	UserParamsDao userParamsDao;
	@Autowired
	OrgParamsDao orgParamsDao;
	@Autowired
	UserManager userManager;
	@Autowired
	OrgManager orgManager;
	
	private static final List<String> types = Arrays.asList("1","2","3");
	private static final List<String> ctrTypes = Arrays.asList("input","select","checkbox","radio","date","number");
	
	@Override
	public Params getByAlias(String alias) {
		if(StringUtil.isEmpty(alias)){
			throw new RequiredException("参数编码【code】不能为空！");
		}
		Params param = baseMapper.getByCode(alias);
		if(BeanUtils.isEmpty(param)){
			throw new RuntimeException("编码为【"+alias+"】的参数不存在!");
		}
		return param;
	}
	
	@Override
	public List<Params> getByType(String type) {
		return baseMapper.getByType(type);
	}

	@Override
    @Transactional
	public void removeByIds(String... ids)  {
		for(String id : ids){
			Params params = this.get(id);
			userParamsDao.removeByAlias(params.getCode(),LocalDateTime.now());
			orgParamsDao.removeByAlias(params.getCode(),LocalDateTime.now());
		}
		super.removeByIds(ids);
	}

	@Override
    @Transactional
	public CommonResult<String> addParams(ParamVo param) throws Exception {
		if(baseMapper.getCountByCode(param.getCode())>0){
			throw new RequiredException("添加参数失败，参数编码【"+param.getCode()+"】在系统中已存在，不能重复！");
		}
		if(StringUtil.isEmpty(param.getCode())){
			throw new RequiredException("添加参数失败，参数编码【code】必填！");
		}
		if(StringUtil.isEmpty(param.getName())){
			throw new RequiredException("添加参数失败，参数名称【name】必填！");
		}
		if(StringUtil.isEmpty(param.getCtrType())){
			throw new RequiredException("添加参数失败，参数控件类型【ctrType】必填！");
		}
		if(StringUtil.isEmpty(param.getType())){
			throw new RequiredException("添加参数失败，参数类型【type】必填！");
		}
		String ctyType = param.getCtrType().trim();
		if(!ctrTypes.contains(ctyType)){
			throw new RequiredException("添加参数失败，ctrType 参数只能为：input：手动输入；   select：下拉框； checkbox：复选框；  radio：单选按钮；  date：日期；  number：数字；");
		}
		if(("select".equals(ctyType)||"checkbox".equals(ctyType)||"radio".equals(ctyType))){
			Iterator<JsonNode> elements = param.getJson().elements();
			while (elements.hasNext()){
				JsonNode next = elements.next();
				if (next.size()<2){
					throw new RequiredException("添加参数失败，ctrType 参数类型为：select（下拉框）、 checkbox（复选框）、  radio（单选按钮）时，json参数不能为空！");
				}
			}
		}
		Params params = ParamVo.parse(param);
		params.setId(UniqueIdUtil.getSuid());
		if(types.contains(params.getType())){
            List<Params> list = new ArrayList<>();
            if(BeanUtils.isNotEmpty(params.getTenantTypeId())){
                String str[]=params.getTenantTypeId().split(",");
                for(int i=0;i<str.length;i++){
                    Params entity = new Params();
                    entity = (Params)BeanUtils.cloneBean(params);
                    entity.setId(UniqueIdUtil.getSuid());
                    entity.setTenantTypeId(str[i]);
                    list.add(entity);
                }
            }else{
                list.add(params);
            }
			this.saveBatch(list);
			return new CommonResult<String>(true, "添加参数成功！", "");
		}else{
			return new CommonResult<String>(false, "添加参数失败！参数类型不对，请在(用户参数：1),(组织参数 :2)选择其中一个数字", "");
		}
	}

	@Override
    @Transactional
	public CommonResult<String> deleteParams(String codes) throws Exception {
		if(StringUtil.isEmpty(codes)){
			throw new RequiredException("参数编码不能为空！");
		}
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		List<String> idArray = new ArrayList<String>();
		boolean isTrue = false;
		for (String code : codeArray) {
			Params params = baseMapper.getByCode(code);
			if(BeanUtils.isNotEmpty(params)){
				idArray.add(params.getId());
				isTrue = true;
			}else{
				str.append(code);
				str.append("，");
			}
		}
		if(BeanUtils.isNotEmpty(idArray)){
			removeByIds(OrgUtil.toStringArray(idArray));
		}
		String msg = StringUtil.isEmpty(str.toString())?"删除参数成功！":"部分删除失败，参数编码："+str.toString()+"不存在！";
		return new CommonResult<String>(isTrue, msg, str.toString());
	}
	
	@Override
    @Transactional
	public CommonResult<String> deleteParamsByIds(String ids) {
		if(StringUtil.isEmpty(ids)){
			throw new RequiredException("参数id不能为空！");
		}
		String[] idArray = ids.split(",");
		StringBuilder str = new StringBuilder();
		List<String> idA = new ArrayList<String>();
		boolean isTrue = false;
		for (String id : idArray) {
			Params params = this.get(id);
			if(BeanUtils.isNotEmpty(params)){
				idA.add(params.getId());
				isTrue = true;
			}else{
				str.append(id);
				str.append("，");
			}
		}
		if(BeanUtils.isNotEmpty(idA)){
			removeByIds(OrgUtil.toStringArray(idA));
		}
		String msg = StringUtil.isEmpty(str.toString())?"删除参数成功！":"部分删除失败，参数编码："+str.toString()+"不存在！";
		return new CommonResult<String>(isTrue, msg, str.toString());
	}

	@Override
    @Transactional
	public CommonResult<String> updateParams(ParamVo paramVo) throws Exception {
		if(BeanUtils.isEmpty(paramVo.getCode())){
			throw new RequiredException("更新参数失败，参数编码【code】必填！");
		}
		Params params = baseMapper.getByCode(paramVo.getCode());
		if(BeanUtils.isEmpty(params)){
			return new CommonResult<String>(false, "更新参数失败，参数编码【"+paramVo.getCode()+"】不存在！", "");
		}
		if(StringUtil.isNotEmpty(paramVo.getName())){
			params.setName(paramVo.getName());
		}
		if(StringUtil.isNotEmpty(paramVo.getTenantTypeId())){
			params.setTenantTypeId(paramVo.getTenantTypeId());
		}
		if(StringUtil.isNotEmpty(paramVo.getCtrType())){
			params.setCtlType(paramVo.getCtrType());
			String ctyType = paramVo.getCtrType().trim();
			if(("select".equals(ctyType)||"checkbox".equals(ctyType)||"radio".equals(ctyType))){
				Iterator<JsonNode> elements = paramVo.getJson().elements();
				while (elements.hasNext()){
					JsonNode next = elements.next();
					if (next.size()<2){
						throw new RequiredException("添加参数失败，ctrType 参数类型为：select（下拉框）、 checkbox（复选框）、  radio（单选按钮）时，json参数不能为空！");
					}
				}
			}
			params.setJson( JsonUtil.toJson(paramVo.getJson()));
		}
		this.update(params);
		return new CommonResult<String>(true, "更新参数成功！", "");
	}
	
	// 通过用户和参数代码查询用户参数
	private UserParams getUserParamsByUser(User user, String code) {
		Params param = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(param)||(BeanUtils.isNotEmpty(param)&&!"1".equals(param.getType()))){
			throw new RequiredException("用户参数编码【"+code+"】不存在！");
		}
		UserParams userParams = userParamsDao.getByUserIdAndCode(user.getId(), code);
		if(BeanUtils.isEmpty(userParams)){
			userParams = new UserParams();
			userParams.setUserId(user.getId());
			userParams.setAlias(code);
		}
		return userParams;
	}

	@Override
	public UserParams getUserParamsByCode(String account, String code) throws Exception {
		if(StringUtil.isEmpty(account)||StringUtil.isEmpty(code)){
			throw new RequiredException("用户账号或用户参数编码不能为空！");
		}
		User user = userManager.getByAccount(account);
		if(BeanUtils.isEmpty(user)){
			throw new RequiredException("用户账号【"+account+"】不存在！");
		}
		return getUserParamsByUser(user, code);
	}
	
	@Override
	public UserParams getUserParamsById(String userId, String code) throws Exception {
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(code)){
			throw new RequiredException("用户ID或用户参数编码不能为空！");
		}
		User user = userManager.get(userId);
		if(BeanUtils.isEmpty(user)){
			throw new RequiredException(String.format("ID为【%s】的用户不存在！", userId));
		}
		return getUserParamsByUser(user, code);
	}
	
	// 通过组织和参数代码查询组织参数
	private OrgParams getOrgParamsByOrg(Org org, String code) {
		Params param = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(param)||(BeanUtils.isNotEmpty(param)&&!"2".equals(param.getType()))){
			throw new RequiredException("组织参数编码【"+code+"】不存在！");
		}
		OrgParams orgParams = orgParamsDao.getByOrgIdAndAlias(org.getId(), code);
		if(BeanUtils.isEmpty(orgParams)){
			orgParams = new OrgParams();
			orgParams.setOrgId(org.getId());
			orgParams.setAlias(code);
		}
		return BeanUtils.isEmpty(orgParams)?new OrgParams():orgParams;
	}

	@Override
	public OrgParams getOrgParamsByCode(String orgCode, String code)
			throws Exception {
		if(StringUtil.isEmpty(orgCode)||StringUtil.isEmpty(code)){
			throw new RequiredException("组织编码或组织参数编码不能为空！");
		}
		Org org = orgManager.getByCode(orgCode);
		if(BeanUtils.isEmpty(org)){
			throw new RequiredException("组织编码【"+orgCode+"】不存在！");
		}
		return getOrgParamsByOrg(org, code);
	}
	
	@Override
	public OrgParams getOrgParamsById(String orgId, String code) throws Exception {
		if(StringUtil.isEmpty(orgId)||StringUtil.isEmpty(code)){
			throw new RequiredException("组织ID或组织参数编码不能为空！");
		}
		Org org = orgManager.get(orgId);
		if(BeanUtils.isEmpty(org)){
			throw new RequiredException(String.format("ID为【%s】的组织不存在！", orgId));
		}
		return getOrgParamsByOrg(org, code);
	}

	@Override
	public List<Params> getParamsByTime(String btime, String etime)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return this.queryNoPage(queryFilter);
	}
	
	@Override
	public CommonResult<Boolean> isCodeExist(String code) throws Exception {
		Params params = baseMapper.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(params);
		return new CommonResult<Boolean>(isExist, isExist?"该用户组织参数编码已存在！":"", isExist);
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}

	@Override
	public List<Params> getByTenantTypeId(String tenantTypeId) {
		return baseMapper.getByTenantTypeId(tenantTypeId);
	}

	@Override
	public PageList queryWithType(QueryFilter queryFilter) {
		return new PageList(baseMapper.queryWithType(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter,currentModelClass())));
	}
}
