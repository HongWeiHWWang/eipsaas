package com.hotent.uc.manager.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.ServerRejectException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.DemensionDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.User;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.demension.DemensionVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.OrgUtil;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * <pre> 
 * 描述：维度管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-19 15:30:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class DemensionManagerImpl extends BaseManagerImpl<DemensionDao,Demension> implements DemensionManager{
	
	@Autowired
	OrgManager orgService;
	@Autowired
	UserManager userService;
	
	
	@Override
    @Transactional
	public void removeByIds(String... ids)  {
		for (String demid : ids) {
			this.remove(demid);
			String[] orgIds = findOrgIdByDemid(demid);
			if(BeanUtils.isNotEmpty(orgIds)){
				orgService.removeByIds(orgIds);
			}
		}
	}
	
	private String[] findOrgIdByDemid(String demid){
		List<String> strs = new ArrayList<String>();  
		QueryFilter queryFilter=QueryFilter.build() ;
		queryFilter.addFilter("demId", demid, QueryOP.EQUAL,FieldRelation.AND);
		PageList<Org> list = orgService.query(queryFilter);
		for (Org org : list.getRows()) {
			strs.add(org.getId());
		}
		if(BeanUtils.isNotEmpty(strs)){
			return strs.toArray(new String[strs.size()]);
		}
		return null;
	}

	@Override
	public Demension getByCode(String code) {
		if(StringUtil.isEmpty(code)){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：code维度编码必填！");
		}
		Demension dem = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(dem)){
			throw new RuntimeException("编码为【"+code+"】的维度不存在!");
		}
		return dem;
	}

	@Override
	public Demension getDefaultDemension() {
		return baseMapper.getDefaultDemension();
	}

	@Override
    @Transactional
	public void setDefaultDemension(String id) throws SQLException {
		if(StringUtil.isNotEmpty(id)){
			Demension demension = this.get(id);
			if(BeanUtils.isEmpty(demension)){
				demension = baseMapper.getByCode(id);
			}
			if(BeanUtils.isNotEmpty(demension)){
				baseMapper.setNotDefaultDemension(LocalDateTime.now());
				demension.setIsDefault(1);
				demension.setUpdateTime(LocalDateTime.now());
				this.update(demension);
			}
		}
	}


	@Override
    @Transactional
	public CommonResult<String> addDem(DemensionVo demVo)
			throws Exception {
		if(StringUtil.isEmpty(demVo.getName())){
			throw new RequiredException("添加维度信息失败，维度名称【name】必填！");
		}
		if(StringUtil.isEmpty(demVo.getCode())){
			throw new RequiredException("添加维度信息失败，维度编码【code】必填！");
		}
		if(baseMapper.getCountByCode(demVo.getCode())>0){
			return new CommonResult<String>(false, "添加维度失败，维度编码【"+demVo.getCode()+"】在系统中已存在！", "");
		}
		Demension dem = new Demension();
		dem.setDemName(demVo.getName());
		dem.setDemCode(demVo.getCode());
		dem.setDemDesc(demVo.getDescription());
		dem.setId(UniqueIdUtil.getSuid());
		if(BeanUtils.isNotEmpty(demVo.getIsDefault())){
			if(demVo.getIsDefault()==1){
				Demension defaulDem = baseMapper.getDefaultDemension();
				if(BeanUtils.isNotEmpty(defaulDem)){
					baseMapper.setNotDefaultDemension(LocalDateTime.now());
				}
				dem.setIsDefault(1);
			}
		}
		this.create(dem);
		return new CommonResult<String>(true, "添加维度成功！", "");
	}
	
	@Override
    @Transactional
	public CommonResult<String> deleteDemByIds(String ids) throws Exception {
		if(StringUtil.isEmpty(ids)){
			throw new RuntimeException("删除维度失败，维度ids必填");
		}
		String[] idArray = ids.split(",");
		StringBuilder str = new StringBuilder();
		String message = "";
		boolean isTrue = false;
		for (String id : idArray) {
			Demension dem = baseMapper.selectById(id);
			if(BeanUtils.isNotEmpty(dem)){
				List<Org> l = orgService.getOrgListByDemId(dem.getId());
				if(BeanUtils.isEmpty(l) && !"1".equals(dem.getIsDefault())){
					this.remove(dem.getId());
					isTrue = true;
				}else if(BeanUtils.isEmpty(l) && "1".equals(dem.getIsDefault())){
					message += "【"+dem.getDemName()+"("+dem.getDemCode()+")】的维度是默认维度，不允许删除 ";
//					throw new RuntimeException("编码为【"+dem.getDemCode()+"】的维度下存在组织，不允许删除");
				}else if(BeanUtils.isNotEmpty(l)){
					message += "【"+dem.getDemName()+"("+dem.getDemCode()+")】的维度下存在组织，不允许删除 ";
				}
			}else{
				str.append(dem.getDemName());
				str.append("，");
			}
		}
		String rtn = StringUtil.isEmpty(str.toString())&&StringUtil.isEmpty(message)?"删除维度成功！":"";
		if(StringUtil.isEmpty(str.toString()) && StringUtil.isNotEmpty(message)){
			rtn = rtn + message;
		}
		if(StringUtil.isNotEmpty(str.toString()) && StringUtil.isEmpty(message)){
			rtn = rtn +"维度："+str.toString()+"不存在！";
		}
		if(StringUtil.isNotEmpty(str.toString()) && StringUtil.isNotEmpty(message)){
			rtn = rtn +"维度："+str.toString()+"不存在！"+message;
		}
		return new CommonResult<String>(isTrue, rtn, "");
	}


	@Override
    @Transactional
	public CommonResult<String> deleteDem(String codes) throws Exception {
		if(StringUtil.isEmpty(codes)){
			throw new RuntimeException("删除维度失败，维度编码【"+codes+"】必填");
		}
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		String message = "";
		boolean isTrue = false;
		for (String code : codeArray) {
			Demension dem = baseMapper.getByCode(code);
			if(BeanUtils.isNotEmpty(dem)){
				List<Org> l = orgService.getOrgListByDemId(dem.getId());
				if(BeanUtils.isEmpty(l) && !"1".equals(dem.getIsDefault())){
					this.remove(dem.getId());
					isTrue = true;
				}else if(BeanUtils.isEmpty(l) && "1".equals(dem.getIsDefault())){
					message += "编码为【"+dem.getDemCode()+"】的维度是默认维度，不允许删除；";
//					throw new RuntimeException("编码为【"+dem.getDemCode()+"】的维度下存在组织，不允许删除");
				}else if(BeanUtils.isNotEmpty(l)){
					message += "编码为【"+dem.getDemCode()+"】的维度下存在组织，不允许删除；";
				}
			}else{
				str.append(code);
				str.append("，");
			}
		}
		String rtn = StringUtil.isEmpty(str.toString())&&StringUtil.isEmpty(message)?"删除维度成功！":"部分删除失败，";
		if(StringUtil.isEmpty(str.toString()) && StringUtil.isNotEmpty(message)){
			rtn = rtn + message;
		}
		if(StringUtil.isNotEmpty(str.toString()) && StringUtil.isEmpty(message)){
			rtn = rtn +"部分删除失败，维度编码："+str.toString()+"不存在！";
		}
		if(StringUtil.isNotEmpty(str.toString()) && StringUtil.isNotEmpty(message)){
			rtn = rtn +"部分删除失败，维度编码："+str.toString()+"不存在！"+message;
		}
//		String msg = StringUtil.isEmpty(str.toString())?"删除维度成功！":"部分删除失败，维度编码："+str.toString()+"不存在！";
		return new CommonResult<String>(isTrue, rtn, "");
	}


	@Override
    @Transactional
	public CommonResult<String> updateDem(DemensionVo demVo)
			throws Exception {
		if(StringUtil.isEmpty(demVo.getCode())){
			throw new RequiredException("更新维度信息失败，维度编码【code】必填！");
		}
		Demension dem = baseMapper.getByCode(demVo.getCode());
		if(BeanUtils.isEmpty(dem)){
			return new CommonResult<String>(false, "更新维度信息失败，维度编码【"+demVo.getCode()+"】不存在！", "");
		}
		if(StringUtil.isNotEmpty(demVo.getName())){
			dem.setDemName(demVo.getName());
		}
		if(demVo.getDescription()!=null){
			dem.setDemDesc(demVo.getDescription());
		}
		if(BeanUtils.isNotEmpty(demVo.getIsDefault())){
			if(demVo.getIsDefault()==1){
				Demension defaulDem = baseMapper.getDefaultDemension();
				if(BeanUtils.isNotEmpty(defaulDem)&&!defaulDem.getDemCode().equals(dem.getDemCode())){
					baseMapper.setNotDefaultDemension(LocalDateTime.now());
				}
				dem.setIsDefault(1);
			}else{
				dem.setIsDefault(0);
			}
		}
		update(dem);
		return new CommonResult<String>(true, "更新维度成功！", "");
	}


	@Override
	public List<UserVo> getUsersByDem(String code) throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RequiredException("维度编码必填！");
		}
		List<User> list = new ArrayList<User>();
		Demension dem = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(dem)){
			throw new RequiredException("维度编码【"+code+"】不存在！");
		}
		List<Org> orgs = orgService.getOrgListByDemId(dem.getId());
		for (Org org : orgs) {
			List<User> users = userService.getUserListByOrgId(org.getId());
			if(BeanUtils.isNotEmpty(users)){
				list.addAll(users);
			}
		}
		OrgUtil.removeDuplicate(list);
		return OrgUtil.convertToUserVoList(list);
	}

	@Override
	public List<Org> getOrgsByDem(String code) throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RequiredException("维度编码必填！");
		}
		Demension dem = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(dem)){
			throw new RequiredException("维度编码【"+code+"】不存在！");
		}
		return orgService.getOrgListByDemId(dem.getId());
	}

	@Override
	public CommonResult<String> setDefaultDem(String code) throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RequiredException("设置默认维度失败，维度编码不能为空！");
		}
		Demension dem = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(dem)){
			throw new RequiredException("设置默认维度失败，维度编码【"+code+"】不存在！");
		}
		setDefaultDemension(dem.getId());
		return new CommonResult<String>(true, "设置默认维度成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> cancelDefaultDem(String code) throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RequiredException("取消默认维度失败，维度编码不能为空！");
		}
		Demension dem = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(dem)){
			throw new RequiredException("取消默认维度失败，维度编码【"+code+"】不存在！");
		}
		if(dem.getIsDefault()!=1){
			return new CommonResult<String>(false, "该维度非默认维度，不需要取消设置！", "");
		}else{
			baseMapper.setNotDefaultDemension(LocalDateTime.now());
			return new CommonResult<String>(true, "取消默认维度成功！", "");
		}
	}

	@Override
	public List<Demension> getDemByTime(OrgExportObject orgExportObject)
			throws Exception {
		QueryFilter<Demension> queryFilter = OrgUtil.getDataByTimeFilter(orgExportObject.getBtime(), orgExportObject.getEtime());
		
		if(StringUtil.isNotEmpty(orgExportObject.getDemCodes())){
			queryFilter.addFilter("CODE_", orgExportObject.getDemCodes(), QueryOP.IN, FieldRelation.AND);
		}
		return this.queryNoPage(queryFilter);
	}
	
	@Override
	public CommonResult<Boolean> isCodeExist(String code) throws Exception {
		Demension dem = baseMapper.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(dem);
		return new CommonResult<Boolean>(isExist, isExist?"该维度编码已存在！":"", isExist);
	}

	@Override
	public ObjectNode getOrgSelectListInit(String code) throws Exception {
//		QueryFilter demFilter = QueryFilter.build();
//		demFilter.getPageBean().setPageSize(10000);
//		demFilter.setClazz(Demension.class);
		List<ObjectNode> objects=new ArrayList<>();
		Org org=orgService.get(code);
		String[] keyArr=new String[2];
		if (BeanUtils.isEmpty(org)) {
			org=orgService.getByCode(code);
		}
		if (BeanUtils.isNotEmpty(org)) {
			 Demension dem=this.get(org.getDemId());
			 if (BeanUtils.isNotEmpty(dem)) {
				 keyArr[1]=dem.getId();
			 }
		  }
		ObjectNode initObj=JsonUtil.getMapper().createObjectNode();
		initObj.set("list", JsonUtil.toJsonNode(objects));
		initObj.set("initGanAndDem",JsonUtil.toJsonNode( keyArr));
		return initObj;
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}


}
