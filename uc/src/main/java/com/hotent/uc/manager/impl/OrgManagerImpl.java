package com.hotent.uc.manager.impl;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import poi.util.ExcelUtil;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.uc.dao.OrgDao;
import com.hotent.uc.dao.OrgJobDao;
import com.hotent.uc.dao.OrgPostDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.manager.OrgJobManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgParamsManager;
import com.hotent.uc.manager.OrgPostManager;
import com.hotent.uc.manager.OrgRoleManager;
import com.hotent.uc.manager.OrgUserManager;
import com.hotent.uc.manager.ParamsManager;
import com.hotent.uc.manager.PropertiesService;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserUnderManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgRole;
import com.hotent.uc.model.OrgTree;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserUnder;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.OrgAuthVo;
import com.hotent.uc.params.org.OrgPostVo;
import com.hotent.uc.params.org.OrgUserVo;
import com.hotent.uc.params.org.OrgVo;
import com.hotent.uc.params.orgRole.OrgRoleVo;
import com.hotent.uc.params.post.PostDueVo;
import com.hotent.uc.params.user.UserUnderVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.ContextUtil;
import com.hotent.uc.util.OrgUtil;

/**
 * 
 * <pre>
 *  
 * 描述：组织架构 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-28 15:13:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class OrgManagerImpl extends BaseManagerImpl<OrgDao, Org> implements OrgManager {
	protected static Logger logger = LoggerFactory.getLogger(OrgManagerImpl.class);
	@Autowired
	OrgParamsManager orgParamsService;
	@Autowired
	OrgUserManager orgUserService;
	@Autowired
	UserUnderManager userUnderService;
	@Autowired
	UserUnderManager userUnderManager;
	@Autowired
	UserManager userService;
	@Autowired
	OrgPostManager orgPostService;
	@Autowired
	OrgPostDao orgPostDao;
	@Autowired
	OrgRoleManager orgRoleService;
	@Autowired
	DemensionManager demensionService;
	@Autowired
	ParamsManager paramsSerivce;
	@Autowired
	OrgAuthManager orgAuthService;
	@Autowired
	OrgJobManager orgJobService;
	@Autowired
	OrgJobDao orgJobDao;
	@Autowired
	RoleManager roleService;
	@Autowired
	OrgRoleManager orgRoleManager;
	@Autowired
	DemensionManager DemensionManager;
	@Autowired
	PropertiesService propertiesService;

	// 子节点
	static List<Org> childOrg = new ArrayList<Org>();

	public Org getByCode(String code) {
		return baseMapper.getByCode(code);
	}

	public List<Org> getOrgListByUserId(String userId) {
		return baseMapper.getOrgListByUserId(userId);
	}

	public List<Org> getOrgListByAccount(String account) throws Exception {
		User user = userService.getByAccount(account);
		return baseMapper.getOrgListByUserId(user.getId());
	}

	@Override
	public Org getMainGroup(String userId, String demId) {
		if (BeanUtils.isNotEmpty(demId)) {
			if (BeanUtils.isEmpty(demensionService.get(demId))) {
				Demension sysDemension = demensionService.getByCode(demId);
				if (BeanUtils.isNotEmpty(sysDemension)) {
					demId = sysDemension.getId();
				}
			}
		} else {
			Demension sysDemension = demensionService.getDefaultDemension();
			if (BeanUtils.isNotEmpty(sysDemension)) {
				demId = sysDemension.getId();
			}
		}
		List<Org> list = baseMapper.getMainOrgListByUser(userId, demId);
		if (BeanUtils.isEmpty(list)) {
			list = baseMapper.getOrgListByUserId(userId);
		}
		return BeanUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
    @Transactional
	public void removeByIds(String... ids) {
		super.removeByIds(ids);
		for (String id : ids) {
			// 删除组织参数
			orgParamsService.removeByOrgId(id);
			// 删除组织岗位
			orgPostService.delByOrgId(id);
			// 删除组织用户
			orgUserService.delByOrgId(id);
			// 删除下属
			userUnderService.delByOrgId(id);
			// 删除分级组织管理
			orgAuthService.delByOrgId(id);
		}

		// 查找子节点
		List<Org> allList = this.getAll();
		childOrg.removeAll(childOrg);
		for (String id : ids) {
			treeOrgList(allList, id);
		}
		// 删除子节点
		String[] childIds = new String[childOrg.size()];
		for (int i = 0; i < childOrg.size(); i++) {
			String id = childOrg.get(i).getId();
			childIds[i] = id;
			this.remove(id);
		}

		// 子组织中相关数据的删除
		for (String childId : childIds) {
			orgParamsService.removeByOrgId(childId);
			orgPostService.delByOrgId(childId);
			orgUserService.delByOrgId(childId);
			userUnderService.delByOrgId(childId);
			orgAuthService.delByOrgId(childId);
		}
	}

	@Override
    @Transactional
	public void updateByOrg(Org org) throws SQLException {
		String extName = org.getPathName().substring(0, org.getPathName().indexOf("/" + org.getName()));
		String extId = org.getPath().substring(0, org.getPath().indexOf("." + org.getId()));
		this.update(org);

		String[] ids = { org.getId() };

		// 查找子节点
		List<Org> allList = this.getAll();
		List<Org> cList = new ArrayList<Org>();
		for (String id : ids) {
			getChild(allList, id, cList);
		}
		// 更新子节点
		String[] childIds = new String[cList.size()];
		// 子节点是递归往上找的数据，最孙的节点排在list前面，遍历时，先从一级子节点进行修改
		for (int i = cList.size() - 1; i >= 0; i--) {
			String id = cList.get(i).getId();
			childIds[i] = id;
			Org c = this.get(id);
			if (c.getParentId().equals(org.getId())) {// 一级子组织
				c.setPathName(org.getPathName() + "/" + c.getName());
				c.setPath(org.getPath() + c.getId() + ".");
				c.setDemId(org.getDemId());
			} else {// 二级及以下组织
				String pName = org.getName();
				String pId = org.getId();
				Org p = this.get(c.getParentId());
				String pathName = p.getPathName().substring(p.getPathName().indexOf("/" + pName),
						p.getPathName().length()) + "/" + c.getName();
				String path = p.getPath().substring(p.getPath().indexOf("." + pId), p.getPath().length()) + c.getId()
						+ ".";
				c.setPathName(extName + pathName);
				c.setPath(extId + path);
				c.setDemId(org.getDemId());
			}
			this.update(c);
		}

	}

	/**
	 * 获取某个父节点下面的所有子节点
	 * 
	 * @param orgList
	 * @param pid
	 * @return
	 */
	public static List<Org> treeOrgList(List<Org> orgList, String parentId) {
		for (Org org : orgList) {
			// 遍历出父id等于参数的id，add进子节点集合
			if (parentId.equals(org.getParentId())) {
				// 递归遍历下一级
				treeOrgList(orgList, org.getId());
				childOrg.add(org);
			}
		}
		return childOrg;
	}

	@Override
	public List<Org> getByParentId(String parentId) {
		List<Org> rtnList = new ArrayList<Org>();
		List<Org> allList = this.getAll();
		return getChild(allList, parentId, rtnList);
	}

	/**
	 * 获取某个父节点下面的所有子节点
	 * 
	 * @param orgList
	 * @param pid
	 * @return
	 */
	public static List<Org> getChild(List<Org> orgList, String parentId, List<Org> rtnList) {
		for (Org org : orgList) {
			// 遍历出父id等于参数的id，add进子节点集合
			if (parentId.equals(org.getParentId())) {
				// 递归遍历下一级
				getChild(orgList, org.getId(), rtnList);
				rtnList.add(org);
			}
		}
		return rtnList;
	}

	public List<Org> getByOrgName(String orgName) {
		return baseMapper.getByOrgName(orgName);
	}

	@Override
	public List<Org> getByPathName(String pathName) {
		return baseMapper.getByPathName(pathName);
	}

	@Override
	public List<Org> getByParentAndDem(QueryFilter queryFilter) {
		return baseMapper.getByParentAndDem(convert2Wrapper(queryFilter, currentModelClass()));
	}

	@Override
	public Org getByDemIdAndSonId(String demId, String sonId) {
		return baseMapper.getByDemIdAndSonId(demId, sonId);
	}

	@Override
	public List<Org> getMainOrgListByUser(String userId, String demId) {
		return baseMapper.getMainOrgListByUser(userId, demId);
	}

	@Override
	public List<Org> getOrgListByDemId(String demId) {
		return baseMapper.getOrgListByDemId(demId);
	}

	@Override
	public List<Org> getUserOrg(String userId, String demId, Boolean isMain) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (BeanUtils.isNotEmpty(isMain)) {
			if (isMain) {
				params.put("isMain", "1");
			} else {
				params.put("isMain", "0");
			}
		}
		if (StringUtil.isNotEmpty(demId)) {
			params.put("demId", demId);
		}
		params.put("userId", userId);
		return baseMapper.getUserOrg(params);
	}

	@Override
    @Transactional
	public CommonResult<String> addOrg(OrgVo orgVo) throws Exception {
		if (StringUtil.isEmpty(orgVo.getName())) {
			throw new RuntimeException("添加组织失败，组织名称【name】不能为空！");
		}
		if (StringUtil.isEmpty(orgVo.getCode())) {
			throw new RuntimeException("添加组织失败，组织编码【code】不能为空！");
		}
		if (orgVo.getCode().contains(",")) {
			throw new RuntimeException("组织编码中不能包含英文逗号‘,’");
		}
		if (StringUtil.isEmpty(orgVo.getDemId())) {
			throw new RuntimeException("添加组织失败，维度id【demId】不能为空！");
		}
		if (baseMapper.getCountByCode(orgVo.getCode())>0) {
			throw new RuntimeException("添加组织失败，组织编码[" + orgVo.getCode() + "]已存在！");
		}
		Demension dem = demensionService.get(orgVo.getDemId());
		if (BeanUtils.isEmpty(dem)) {
			throw new RuntimeException("添加组织失败，根据输入的demId[" + orgVo.getDemId() + "]没有找到对应的维度信息！");
		}
		Org pOrg = null;
		if (!"0".equals(orgVo.getParentId()) && StringUtil.isNotEmpty(orgVo.getParentId())) {
			pOrg = this.get(orgVo.getParentId());
			if (BeanUtils.isEmpty(pOrg)) {
				throw new RuntimeException("添加组织失败，根据输入的parentId[" + orgVo.getParentId() + "]没有找到对应的组织信息！");
			}
			if (BeanUtils.isEmpty(pOrg) && !pOrg.getDemId().equals(orgVo.getDemId())) {
				throw new RuntimeException("添加组织失败，根据输入demId与所输入的父组织所对应的维度id不一致！");
			}
		}
		Org o = new Org();
		o.setId(UniqueIdUtil.getSuid());
		o.setCode(orgVo.getCode());
		o.setName(orgVo.getName());
		o.setDemId(orgVo.getDemId());
		if (StringUtils.isEmpty(orgVo.getParentId())) {
			o.setParentId("0");
		} else {
			o.setParentId(orgVo.getParentId());
		}
		if (StringUtil.isNotEmpty(orgVo.getGrade())) {
			o.setGrade(orgVo.getGrade());
		}
		if (BeanUtils.isNotEmpty(orgVo.getOrderNo())) {
			o.setOrderNo(orgVo.getOrderNo());
		}
		if (BeanUtils.isEmpty(pOrg)) {
			o.setPathName("/" + orgVo.getName());
			o.setPath(orgVo.getDemId() + "." + o.getId() + ".");
		} else {
			o.setPath(pOrg.getPath() + o.getId() + ".");
			o.setPathName(pOrg.getPathName() + "/" + orgVo.getName());
		}
		o.setLimitNum(orgVo.getLimitNum().intValue());
		o.setExceedLimitNum(orgVo.getExceedLimitNum().intValue());
		this.create(o);
		return new CommonResult<String>(true, "添加组织成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> deleteOrg(String codes) throws Exception {
		if (StringUtils.isEmpty(codes)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：组织代码必填！");
		}
		String message = "";
		String[] codeArr = codes.split(",");
		for (String code : codeArr) {
			Org o = this.getByCode(code);
			if (BeanUtils.isEmpty(o)) {
				message += "编码为【" + code + "】的组织不存在；";
				continue;
			}
			String[] ids = { o.getId() };
			removeByIds(ids);

		}
		if (StringUtils.isEmpty(message)) {
			message = "删除组织成功！";
		} else {
			message = "部分删除成功，其中" + message;
		}
		return new CommonResult<String>(true, message, "");
	}

	@Override
    @Transactional
	public CommonResult<String> updateOrg(OrgVo orgVo) throws Exception {
		if (orgVo.getExceedLimitNum()==1){
			if(orgVo.getLimitNum() < orgVo.getNowNum()){
				throw new RuntimeException("更新组织失败，当前组织人数超出限编人数！");
			}
		}
		if (StringUtil.isEmpty(orgVo.getCode())) {
			throw new RuntimeException("更新组织失败，组织编码【code】必填！");
		}
		Org o = this.getByCode(orgVo.getCode());
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("更新组织失败，根据组织编码【" + orgVo.getCode() + "】没有找到对应的组织！");
		}
		Demension dem = demensionService.get(orgVo.getDemId());
		if (StringUtil.isNotEmpty(orgVo.getDemId()) && BeanUtils.isEmpty(dem)) {
			throw new RuntimeException("更新组织失败，根据输入的demId【" + orgVo.getDemId() + "】没有找到对应的维度信息！");
		}
		Org pOrg = null;
		if (StringUtil.isNotEmpty(orgVo.getParentId()) && !"0".equals(orgVo.getParentId())) {
			pOrg = this.get(orgVo.getParentId());
			if (BeanUtils.isEmpty(pOrg)) {
				throw new RuntimeException("更新组织失败，根据输入的parentId【" + orgVo.getDemId() + "】没有找到对应的组织信息！");
			}
			if (StringUtil.isNotEmpty(orgVo.getDemId()) && BeanUtils.isNotEmpty(pOrg)
					&& !pOrg.getDemId().equals(orgVo.getDemId())) {
				throw new RuntimeException("更新组织失败，根据输入demId与所输入的父组织所对应的维度id不一致！");
			}
		}
		if (StringUtil.isNotEmpty(orgVo.getName())) {
			o.setName(orgVo.getName());
		}
		if (StringUtil.isNotEmpty(orgVo.getGrade())) {
			o.setGrade(orgVo.getGrade());
		}
		if (BeanUtils.isNotEmpty(orgVo.getOrderNo())) {
			o.setOrderNo(orgVo.getOrderNo());
		}

		if (BeanUtils.isEmpty(pOrg)) {
			if (StringUtil.isNotEmpty(orgVo.getName())) {
				o.setPathName("/" + orgVo.getName());
			}
			if (StringUtil.isNotEmpty(orgVo.getDemId())) {
				o.setPath(orgVo.getDemId() + "." + o.getId() + ".");
				o.setDemId(orgVo.getDemId());
			}
		} else {
			o.setPath(pOrg.getPath() + o.getId() + ".");
			o.setPathName(pOrg.getPathName() + "/" + orgVo.getName());
			o.setDemId(orgVo.getDemId());
			o.setParentId(pOrg.getId());
		}
		o.setLimitNum(orgVo.getLimitNum().intValue());
		o.setExceedLimitNum(orgVo.getExceedLimitNum().intValue());
		this.updateByOrg(o);
		return new CommonResult<String>(true, "组织更新成功！", "");
	}

	@Override
	public Org getOrg(String code) throws Exception {
		if (StringUtils.isEmpty(code)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：组织编码code必填！");
		}
		Org o = this.getByCode(code);
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的组织编码没有找到对应的组织信息！");
		}
		o = this.get(o.getId());
		String pId = o.getParentId();
		String demId = o.getDemId();
		Demension d = demensionService.get(demId);
		if (d != null && !StringUtil.isEmpty(d.getDemCode())) {
			o.setDemCode(d.getDemCode());
		}
		if ("0".equals(pId) || StringUtil.isEmpty(pId)) {
			o.setParentOrgName(d.getDemName());
		} else {
			o.setParentOrgName(this.get(pId).getName());
		}
		List<OrgParams> params = this.getOrgParams(o.getCode());
		if (BeanUtils.isNotEmpty(params)) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (OrgParams param : params) {
				map.put(param.getAlias(), param.getValue());
			}
			o.setParams(map);
		}
		return o;
	}

	@Override
    @Transactional
	public CommonResult<String> saveOrgParams(String orgCode, List<ObjectNode> params) throws Exception {
		if (StringUtils.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：组织编码orgCode必填！");
		}
		Org org = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(org)) {
			throw new RuntimeException("根据输入的组织编码没有找到对应的组织信息！");
		}
		orgParamsService.removeByOrgId(org.getId());
		// 判断输入的key是否存在
		for (ObjectNode o : params) {
			String key = o.get("alias").asText();
			Params p = paramsSerivce.getByAlias(key);
			if (BeanUtils.isEmpty(p)) {
				throw new RuntimeException("【" + key + "】参数key不存在！");
			}
			if (!p.getType().equals("2")) {
				throw new RuntimeException("【" + key + "】对应的【用户组织扩展参数】类型不是【组织参数】类型！");
			}
		}
		try {
			orgParamsService.saveParams(org.getId(), params);
		} catch (Exception e) {
			System.out.println(e);
			if (e.getMessage().indexOf("ORA-12899") > -1) {
				throw new RuntimeException("保存失败，参数值过长");
			} else {
				throw e;
			}
			// TODO: handle exception
		}
		return new CommonResult<String>(true, "保存组织参数成功！", "");
	}

	@Override
	public List<OrgParams> getOrgParams(String orgCode) throws Exception {
		Org o = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的组织编码没有找到对应的组织信息！");
		}
		List<OrgParams> list = orgParamsService.getByOrgId(o.getId());
		return list;
	}

	@Override
	public CommonResult<OrgParams> getParamByAlias(String orgCode, String alias) throws Exception {
		Org o = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的组织编码没有找到对应的组织信息！");
		}
		OrgParams p = orgParamsService.getByOrgIdAndAlias(o.getId(), alias);
		if (BeanUtils.isEmpty(p)) {
			throw new RuntimeException("根据输入的参数别名【" + alias + "】没有找到对应的参数信息");
		}
		return new CommonResult<OrgParams>(true, "获取完毕！", p);
	}

	@Override
    @Transactional
	public CommonResult<String> addOrgUser(OrgUserVo orgUserVo) throws Exception {
		if (StringUtil.isEmpty(orgUserVo.getAccount())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：用户帐号account必填！");
		}
		if (StringUtil.isEmpty(orgUserVo.getOrgCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：组织编码orgCode必填！");
		}
		User u = userService.getByAccount(orgUserVo.getAccount());
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("没有找到对应的用户！");
		}
		if (BeanUtils.isNotEmpty(orgUserVo.getIsCharge())
				&& !(orgUserVo.getIsCharge() != 1 || orgUserVo.getIsCharge() != 0)) {
			throw new RuntimeException("isCharge只能为0或1");
		}
		if (BeanUtils.isNotEmpty(orgUserVo.getIsMaster())
				&& !(orgUserVo.getIsMaster() != 1 || orgUserVo.getIsMaster() != 0)) {
			throw new RuntimeException("isMaster只能为0或1");
		}
		Org o = this.getByCode(orgUserVo.getOrgCode());
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("没有找到对应的组织！");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", u.getId());
		map.put("orgId", o.getId());
		List<OrgUser> list = orgUserService.getByParms(map);
		if (BeanUtils.isEmpty(list)) {
			OrgUser orgUser = new OrgUser();
			orgUser.setId(UniqueIdUtil.getSuid());
			orgUser.setUserId(u.getId());
			orgUser.setOrgId(o.getId());
			orgUser.setIsRelActive(1);
			if (BeanUtils.isEmpty(orgUserVo.getIsCharge())) {
				orgUser.setIsCharge(0);
			} else {
				orgUser.setIsCharge(orgUserVo.getIsCharge());
			}
			if (BeanUtils.isEmpty(orgUserVo.getIsMaster())) {
				orgUser.setIsMaster(0);
			} else {
				orgUser.setIsMaster(orgUserVo.getIsMaster());
				if (orgUserVo.getIsMaster() == 1) {
					List<OrgUser> l = orgUserService.getOrgUserMaster(u.getId(), o.getDemId());
					if (BeanUtils.isNotEmpty(l)) {
						OrgUser ou = l.get(0);
						ou.setIsMaster(0);
						orgUserService.update(ou);
					}
				}
			}
			orgUserService.create(orgUser);
		} else {
			User user = userService.getByAccount(orgUserVo.getAccount());
			Org org = getByCode(orgUserVo.getOrgCode());
			throw new RuntimeException("用户【" + user.getFullname() + "("+orgUserVo.getAccount()+")】已存在组织【" + org.getName() + "("+orgUserVo.getOrgCode()+")】中");
		}
		return new CommonResult<String>(true, "加入用户成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> delOrgUser(String ids) throws Exception {
		if (StringUtil.isEmpty(ids)) {
			throw new RuntimeException("id值不能为空！");
		}
		String[] orgUserIds = ids.split(",");
		Boolean isDele = false;
		StringBuilder str = new StringBuilder();
		for (String id : orgUserIds) {// 将用户在该组织下的所有相关下属删除
			OrgUser obj = orgUserService.get(id);
			if (BeanUtils.isNotEmpty(obj)) {
				userUnderService.delByUserIdAndOrgId(obj.getUserId(), obj.getOrgId());
				isDele = true;
			} else {
				str.append(id);
				str.append("，");
			}
		}
		String msg = StringUtil.isEmpty(str.toString()) ? "用户取消加入成功！" : "部分用户取消加入成功，id为：" + str.toString() + "的数据不存在！";
		orgUserService.removeByIds(orgUserIds);
		return new CommonResult<String>(isDele, msg, "");
	}

	@Override
	public CommonResult<Boolean> getUserIsMaster(String account, String demCode) throws Exception {
		if (StringUtils.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：用户帐号account必填！");
		}
		User u = userService.getByAccount(account);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的account没有找到对应的用户信息");
		}
		if (StringUtils.isEmpty(demCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "：维度编码demCode必填！");
		}
		Demension d = demensionService.getByCode(demCode);
		if (BeanUtils.isEmpty(d)) {
			throw new RuntimeException("根据输入的demCode没有找到对应的维度信息");
		}
		List<OrgUser> orgUsers = orgUserService.getOrgUserMaster(u.getId(), d.getId());
		if (BeanUtils.isEmpty(orgUsers)) {
			return new CommonResult<Boolean>(true, "用户在【" + d.getDemCode() + "】下没有主组织", false);
		} else {
			return new CommonResult<Boolean>(true, "用户在【" + d.getDemCode() + "】下存在主组织", true);
		}
	}

	@Override
    @Transactional
	public CommonResult<String> setMaster(String account, String postCode) throws Exception {
		OrgPost post = orgPostService.getByCode(postCode);
		if (BeanUtils.isEmpty(post)) {
			throw new RuntimeException("根据输入的岗位编码没有找到对应的岗位信息");
		}
		User u = userService.getByAccount(account);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的用户帐号没有找到对应的用户");
		}
		OrgUser orgUser = orgUserService.getOrgUser(post.getOrgId(), u.getId(), post.getId());
		if (BeanUtils.isEmpty(orgUser)) {
			throw new RuntimeException("对不起，该用户目前还不是该岗位下的人员！");
		}
		orgUserService.setMaster(orgUser.getId());
		return new CommonResult<String>(true, "操作成功！", "");
	}

	@Override
	public List<OrgTree> getTreeDataByDem(String demCode, String pOrgCode) throws Exception {
		if (StringUtil.isEmpty(demCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":维度编码demCode必填");
		}
		Demension d = demensionService.getByCode(demCode);
		if (BeanUtils.isEmpty(d)) {
			throw new RuntimeException("根据输入的demCode没有找到对应的维度");
		}
		Org p = this.getByCode(pOrgCode);
		if (StringUtil.isNotEmpty(pOrgCode) && BeanUtils.isEmpty(p)) {
			throw new RuntimeException("根据输入的pOrgCode没有找到组织");
		}
		List<OrgTree> orgTree = new ArrayList<OrgTree>();
		String pId = "0";
		if (BeanUtils.isNotEmpty(p)) {
			pId = p.getId();
		}
		// DefaultQueryFilter filter = new DefaultQueryFilter();
		QueryFilter filter = QueryFilter.build();
		PageBean page = new PageBean();
		page.setPageSize(Integer.MAX_VALUE);
		filter.setPageBean(page);
		filter.addParams("demId", d.getId());
		// filter.addParamsFilter("parentId", pId);
		List<Org> groupList = this.getByParentAndDem(filter);
		List<Org> rtnList = new ArrayList<Org>();
		getChild(groupList, pId, rtnList);
		for (Org group : rtnList) {
			OrgTree groupTree = new OrgTree(group);
			orgTree.add(groupTree);
		}
		return orgTree;
	}

	@Override
    @Transactional
	public CommonResult<String> setOrgCharge(String account, String orgCode, Boolean isCharge) throws Exception {
		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		if (StringUtil.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":组织编码orgCode必填");
		}
		User u = userService.getByAccount(account);
		Org o = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的用户帐号没有获取到用户！");
		}
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的组织编码没有获取到对应组织！");
		}

		List<OrgUser> orgUserList = orgUserService.getListByOrgIdUserId(o.getId(), u.getId());
		if (BeanUtils.isEmpty(orgUserList)) {
			throw new RuntimeException("用户【" + account + "】与组织【" + orgCode + "】不存在关系");
		}
		orgUserService.setCharge(u.getId(), isCharge, o.getId());
		return new CommonResult<String>(true, "设置成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> addUserUnders(UserUnderVo userUnderObj) throws Exception {
		if (StringUtil.isEmpty(userUnderObj.getUnderAccounts())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":下属用户帐号underAccounts必填");
		}
		if (StringUtil.isEmpty(userUnderObj.getAccount())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		if (StringUtil.isEmpty(userUnderObj.getOrgCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":组织编码orgCode必填");
		}
		User u = userService.getByAccount(userUnderObj.getAccount());
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的account没有找到对应的用户！");
		}
		Org o = this.getByCode(userUnderObj.getOrgCode());
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的orgCode没有找到组织！");
		}

		if (BeanUtils.isEmpty(orgUserService.getListByOrgIdUserId(o.getId(), u.getId()))) {
			return new CommonResult<String>(false, "用户【" + u.getFullname() + "】与组织【" + o.getName() + "】不存在关系", "");
		}

		// 添加下属
		String[] underAccounts = userUnderObj.getUnderAccounts().split(",");
		for (String a : underAccounts) {
			User under = userService.getByAccount(a);
			if (BeanUtils.isEmpty(under)) {
				throw new RuntimeException("根据帐号【" + a + "】没有找到对应的用户！");
			}
			if (BeanUtils.isEmpty(orgUserService.getListByOrgIdUserId(o.getId(), under.getId()))) {
				return new CommonResult<String>(false, "用户【" + under.getFullname() + "】与组织【" + o.getName() + "】不存在关系",
						"");
			}
			if (userUnderObj.getAccount().equals(a)) {
				throw new RuntimeException("用户【" + a + "】不能将自己添加为自己的下属！");
			}

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userId", under.getId());
			m.put("orgId", o.getId());
			m.put("underUserId", u.getId());
			List<UserUnder> list = userUnderService.getUserUnder(m);// 判断是否设置互为上下级
			if (BeanUtils.isNotEmpty(list)) {
				throw new RuntimeException(
						"用户【" + under.getAccount() + "】在组织【" + o.getCode() + "】下是用户【" + u.getAccount() + "】的上级");
			}

			Map<String, Object> m1 = new HashMap<String, Object>();
			m1.put("noUserId", u.getId());
			m1.put("orgId", o.getId());
			m1.put("underUserId", under.getId());
			List<UserUnder> list1 = userUnderService.getUserUnder(m1);// 获取该组织下，不是当前上级用户的数据
			if (BeanUtils.isNotEmpty(list1)) {// 同一组织下，用户只能有一个上级
				User supperUser = userService.get(list1.get(0).getUserId());
				throw new RuntimeException(
						"用户【" + a + "】在当前组织中已有上级" + "【" + supperUser.getFullname() + "】，在当前组织中不能再设置其他上级。");
			} else {
				Map<String, Object> m2 = new HashMap<String, Object>();
				m2.put("userId", u.getId());
				m2.put("orgId", o.getId());
				m2.put("underUserId", under.getId());
				List<UserUnder> list2 = userUnderService.getUserUnder(m2);// 获取该组织下，是当前上级用户的数据
				if (BeanUtils.isNotEmpty(list2)) {//
					continue;
				} else {
					UserUnder userUnder = new UserUnder();
					userUnder.setId(UniqueIdUtil.getSuid());
					userUnder.setUserId(u.getId());
					userUnder.setUnderUserId(under.getId());
					userUnder.setUnderUserName(under.getFullname());
					userUnder.setOrgId(o.getId());
					userUnderService.create(userUnder);
				}
			}
		}
		return new CommonResult<String>(true, "添加下属成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> delUserUnders(String account, String orgCode) throws Exception {
		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		if (StringUtil.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":组织编码orgCode必填");
		}
		User u = userService.getByAccount(account);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的account没有找到对应的用户！");
		}
		Org o = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的orgCode没有找到组织！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", u.getId());
		params.put("orgId", o.getId());
		List<UserUnder> l = userUnderService.getUserUnder(params);
		if (BeanUtils.isEmpty(l)) {
			return new CommonResult<String>(false, "用户【" + u.getFullname() + "】在组织【" + o.getName() + "】下没有下级", "");
		}
		userUnderService.delByUserIdAndOrgId(u.getId(), o.getId());
		return new CommonResult<String>(true, "删除成功！", "");
	}

	@Override
	public List<UserVo> getUserUnders(String account, String orgCode) throws Exception {
		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		if (StringUtil.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":组织编码orgCode必填");
		}
		User u = userService.getByAccount(account);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的account没有找到对应的用户！");
		}
		Org o = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的orgCode没有找到组织！");
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userId", u.getId());
		m.put("orgId", o.getId());
		List<UserUnder> list = userUnderService.getUserUnder(m);
		List<User> userList = new ArrayList<User>();
		for (UserUnder under : list) {
			User user = userService.get(under.getUnderUserId());
			userList.add(user);
		}
		return OrgUtil.convertToUserVoList(userList);
	}

	@Override
    @Transactional
	public CommonResult<String> saveOrgAuth(OrgAuthVo orgAuthVo) throws Exception {
		if (StringUtil.isEmpty(orgAuthVo.getAccount())) {
			throw new RequiredException(HotentHttpStatus.REUIRED.description() + "：用户帐号必填！");
		}
		User u = userService.getByAccount(orgAuthVo.getAccount());
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("没有找到对应的用户！");
		}
		if (StringUtil.isEmpty(orgAuthVo.getOrgCode())) {
			throw new RequiredException(HotentHttpStatus.REUIRED.description() + "：组织代码必填！");
		}
		Org o = this.getByCode(orgAuthVo.getOrgCode());
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("没有找到对应的组织！");
		}
		if (StringUtil.isEmpty(orgAuthVo.getDemCode())) {
			throw new RequiredException(HotentHttpStatus.REUIRED.description() + "：维度编码必填！");
		}
		Demension d = demensionService.getByCode(orgAuthVo.getDemCode());
		if (BeanUtils.isEmpty(d)) {
			throw new RuntimeException("没有找到对应的组织维度！");
		}
		if (!o.getDemId().equals(d.getId())) {
			throw new RuntimeException("输入的组织所对应的维度与输入的维度不一致！");
		}
		if (StringUtil.isEmpty(orgAuthVo.getId())) {
			OrgAuth a = orgAuthService.getByOrgIdAndUserId(o.getId(), u.getId());
			if (BeanUtils.isNotEmpty(a)) {
				throw new RuntimeException("用户在该组织已经是分级管理员！");
			}
		} else {
			List<OrgAuth> list = orgAuthService.getListByOrgIdAndUserId(o.getId(), u.getId());
			if (list != null && list.size() > 1) {
				throw new RuntimeException("用户在该组织已经是分级管理员！");
			}
		}

		OrgAuth auth = new OrgAuth();
		if (StringUtil.isEmpty(orgAuthVo.getId())) {
			auth.setId(UniqueIdUtil.getSuid());
			auth.setOrgId(o.getId());
			auth.setUserId(u.getId());
			auth.setOrgPerms(orgAuthVo.getOrgPerms());
			auth.setUserPerms(orgAuthVo.getUserPerms());
			auth.setPosPerms(orgAuthVo.getPosPerms());
			auth.setOrgauthPerms(orgAuthVo.getOrgauthPerms());
			auth.setLayoutPerms(orgAuthVo.getLayoutPerms());
			auth.setDemId(d.getId());
			orgAuthService.create(auth);
			return new CommonResult<String>(true, "添加分级管理成功！", "");
		} else {
			auth.setId(orgAuthVo.getId());
			auth.setOrgId(o.getId());
			auth.setUserId(u.getId());
			auth.setOrgPerms(orgAuthVo.getOrgPerms());
			auth.setUserPerms(orgAuthVo.getUserPerms());
			auth.setPosPerms(orgAuthVo.getPosPerms());
			auth.setOrgauthPerms(orgAuthVo.getOrgauthPerms());
			auth.setLayoutPerms(orgAuthVo.getLayoutPerms());
			auth.setDemId(d.getId());
			orgAuthService.update(auth);
			return new CommonResult<String>(true, "更新分级管理成功！", "");
		}

	}

	@Override
    @Transactional
	public CommonResult<String> deleteOrgAuth(String ids) throws Exception {
		if (StringUtil.isEmpty(ids)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":分级组织管理id必填");
		}
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			if (BeanUtils.isEmpty(orgAuthService.get(id))) {
				throw new RuntimeException("根据输入的分级组织管理id没有找到对应数据！");
			}
			orgAuthService.remove(id);
		}
		return new CommonResult<String>(true, "删除成功！", "");
	}

	@Override
	public Page<OrgAuth> getOrgAuthList(String account, String orgCode, String demCode) throws Exception {
		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		User u = userService.getByAccount(account);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的用户帐号没有找到对应的用户！");
		}
		Org o = null;
		if (StringUtil.isNotEmpty(orgCode)) {
			o = this.getByCode(orgCode);
		}
		if (StringUtil.isNotEmpty(orgCode) && BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的组织代码没有找到对应的组织！");
		}
		Demension de = null;
		if (StringUtil.isNotEmpty(demCode)) {
			de = demensionService.getByCode(demCode);
		}
		if (StringUtil.isNotEmpty(demCode) && BeanUtils.isEmpty(de)) {
			throw new RuntimeException("根据输入的维度编码没有找到对应的维度！");
		}
		QueryFilter filter = QueryFilter.build();
		filter.addParams("userId", u.getId());
		if (BeanUtils.isNotEmpty(o)) {
			filter.addParams("orgId", o.getId());
		}
		if (BeanUtils.isNotEmpty(de)) {
			filter.addParams("demId", de.getId());
		}
		Page<OrgAuth> list = (Page<OrgAuth>) orgAuthService.getAllOrgAuth(filter);
		return list;
	}

	@Override
	public OrgAuth getOrgAuth(String id) throws Exception {
		if (StringUtil.isEmpty(id)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":分级组织管理id必填");
		}
		if (BeanUtils.isEmpty(orgAuthService.get(id))) {
			throw new RuntimeException("根据输入的id数据没有找到对应的信息");
		}
		OrgAuth orgAuth = orgAuthService.get(id);
		User user = userService.get(orgAuth.getUserId());
		orgAuth.setUserName(user.getFullname());
		orgAuth.setUserAccount(user.getAccount());
		return orgAuth;
	}

	@Override
	public List<Org> getUserOrgs(String account, String demCode, Boolean isMain) throws Exception {
		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		User u = userService.getByAccount(account);
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的用户帐号没有找到对应的用户！");
		}
		Demension de = null;
		if (StringUtil.isNotEmpty(demCode)) {
			de = demensionService.getByCode(demCode);
		}
		if (StringUtil.isNotEmpty(demCode) && BeanUtils.isEmpty(de)) {
			throw new RuntimeException("根据输入的维度编码没有找到对应的维度！");
		}
		List<Org> list = null;
		String demId = null;
		if (BeanUtils.isNotEmpty(de)) {
			demId = de.getId();
		}
		if (BeanUtils.isEmpty(isMain)) {
			list = this.getUserOrg(u.getId(), demId, null);
		} else {
			list = this.getUserOrg(u.getId(), demId, isMain);
		}
		return list;
	}

	@Override
	public List<UserVo> getUsersByOrgCodes(String orgCodes, Boolean isMain) throws Exception {
		if (StringUtil.isEmpty(orgCodes)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织代码orgCodes必填！");
		}
		List<User> list = new ArrayList<User>();
		String[] orgCodeArr = orgCodes.split(",");
		for (String orgCode : orgCodeArr) {
			Org o = this.getByCode(orgCode);
			if (BeanUtils.isEmpty(o)) {
				throw new RuntimeException("根据组织代码【" + orgCode + "】没有找到对应的组织信息！");
			}
			List<User> l = userService.getOrgUsers(o.getId(), isMain);
			if (BeanUtils.isNotEmpty(l)) {
				list.addAll(l);
			}
		}
		return OrgUtil.convertToUserVoList(list);
	}

	@Override
	public List<OrgPost> getPostsByOrgCodes(String orgCodes, Boolean isMain) throws Exception {
		if (StringUtil.isEmpty(orgCodes)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织代码orgCodes必填！");
		}
		List<OrgPost> list = new ArrayList<OrgPost>();
		String[] orgCodeArr = orgCodes.split(",");
		for (String orgCode : orgCodeArr) {
			Org o = this.getByCode(orgCode);
			if (BeanUtils.isEmpty(o)) {
				throw new RuntimeException("根据组织代码【" + orgCode + "】没有找到对应的组织信息！");
			}
			List<OrgPost> l = orgPostService.getRelCharge(o.getId(), isMain);
			if (BeanUtils.isNotEmpty(l)) {
				list.addAll(l);
			}
		}
		return list;
	}

	@Override
	public List<Org> getByLevel(String level, String demCode) throws Exception {
		if (StringUtil.isEmpty(level)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织级别level必填！");
		}
		Demension d = null;
		if (StringUtil.isNotEmpty(demCode)) {
			d = demensionService.getByCode(demCode);
		}
		if (StringUtil.isNotEmpty(demCode) && BeanUtils.isEmpty(d)) {
			throw new RuntimeException("根据输入的输入的维度编码demCode没有找到对应的维度！");
		}
		QueryFilter filter = QueryFilter.build();
		//filter.setClazz(Org.class);
		filter.addParams("grade_", level);
		if (BeanUtils.isNotEmpty(d)) {
			filter.addParams("dem_id_", d.getId());
		}
		return this.query(filter).getRows();
	}

	@Override
    @Transactional
	public CommonResult<String> saveUserPost(String accounts, String postCode) throws Exception {
		if (StringUtil.isEmpty(accounts)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "用户帐号accounts必填！");
		}
		if (StringUtil.isEmpty(postCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位代码postCode必填！");
		}
		String[] postCodeArr = postCode.split(",");
		String[] accountArr = accounts.split(",");
		for(String str : postCodeArr) {
			OrgPost post = orgPostService.getByCode(str);
			if (BeanUtils.isEmpty(post)) {
				throw new RuntimeException("根据输入岗位代码没有找到对应的岗位信息！");
			}
			Org org = this.get(post.getOrgId());
			if(postCode.indexOf(",") < 0) {
				if (org.getExceedLimitNum() == 1 && org.getLimitNum() != 0) {
					Map<String, Object> map = new HashMap<>();
					map.put("orgCode", org.getCode());
					map.put("group", "true");
					List<Map<String, Object>> list = orgUserService.getUserNumByOrgCode(map);
					if (org.getLimitNum() < list.size() + accountArr.length) {
						return new CommonResult<String>(false, "人数超出组织上限，不予添加", null);
					}
				}
			}
			for (String account : accountArr) {
				User u = userService.getByAccount(account);
				if (BeanUtils.isEmpty(u)) {
					throw new RuntimeException("根据输入的用户帐号【" + account + "】没有找到对应的用户");
				}
				OrgUser ou = orgUserService.getOrgUser(post.getOrgId(), u.getId(), post.getId());
				if (BeanUtils.isNotEmpty(ou)) {
					continue;
				}
				ou = orgUserService.getOrgUser(post.getOrgId(), u.getId(), "");
				if (BeanUtils.isNotEmpty(ou)) {
					ou.setRelId(post.getId());
					orgUserService.update(ou);
				} else {
					ou = new OrgUser();
					ou.setId(UniqueIdUtil.getSuid());
					ou.setUserId(u.getId());
					ou.setIsCharge(0);
					ou.setIsMaster(0);
					ou.setIsRelActive(1);
					ou.setOrgId(post.getOrgId());
					ou.setRelId(post.getId());
					orgUserService.create(ou);
				}
				// ou = new OrgUser();
				// ou.setId(UniqueIdUtil.getSuid());
				// ou.setUserId(u.getId());
				// ou.setIsCharge(0);
				// ou.setIsMaster(0);
				// ou.setIsRelActive(1);
				// ou.setOrgId(post.getOrgId());
				// ou.setRelId(post.getId());
				// orgUserService.create(ou);
			}
		}
		return new CommonResult<String>(true, "添加成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> saveUserPosts(String account, String postCodes) throws Exception {

		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "用户帐号accounts必填！");
		}
		if (StringUtil.isEmpty(postCodes)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位代码postCode必填！");
		}
		String[] postCodesArr = postCodes.split(",");

		for (String postCode : postCodesArr) {
			OrgPost post = orgPostService.getByCode(postCode);
			if (BeanUtils.isEmpty(post)) {
				throw new RuntimeException("根据输入岗位代码没有找到对应的岗位信息！");
			}
			Org org = this.get(post.getOrgId());
			if (org.getExceedLimitNum() == 1 && org.getLimitNum() != 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("orgCode", org.getCode());
				map.put("group", "true");
				List<Map<String, Object>> list = orgUserService.getUserNumByOrgCode(map);
				if (org.getLimitNum() <= list.size()) {
					return new CommonResult<String>(false, "人数超出组织上限，不予添加", null);
				}
			}
			User u = userService.getByAccount(account);
			if (BeanUtils.isEmpty(u)) {
				throw new RuntimeException("根据输入的用户帐号【" + account + "】没有找到对应的用户");
			}
			OrgUser ou = orgUserService.getOrgUser(post.getOrgId(), u.getId(), post.getId());
			QueryWrapper<OrgUser> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ORG_ID_",post.getOrgId());
			queryWrapper.eq("USER_ID_",u.getId());
			List<Map<String, Object>> list = orgUserService.listMaps(queryWrapper);
			if (BeanUtils.isNotEmpty(ou)) {
				throw new BaseException("当前用户已在【"+post.getName()+"】的岗位上！");
			}
			for (int i = 0; i < list.size(); i++) {
				if(ObjectUtils.isEmpty(list.get(i).get("relId"))){
					delOrgUser(list.get(i).get("ID_").toString());
					break;
				}
			}
			ou = new OrgUser();
			ou.setId(UniqueIdUtil.getSuid());
			ou.setUserId(u.getId());
			ou.setIsCharge(0);
			ou.setIsMaster(0);
			ou.setIsRelActive(1);
			ou.setOrgId(post.getOrgId());
			ou.setRelId(post.getId());
			orgUserService.create(ou);
		}
		return new CommonResult<String>(true, "添加成功", "");

	}

	@Override
    @Transactional
	public CommonResult<String> delUserPost(String accounts, String postCode) throws Exception {
		if (StringUtil.isEmpty(accounts)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "用户帐号accounts必填！");
		}
		if (StringUtil.isEmpty(postCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位代码postCode必填！");
		}
		OrgPost post = orgPostService.getByCode(postCode);
		if (BeanUtils.isEmpty(post)) {
			throw new RuntimeException("根据输入岗位代码没有找到对应的岗位信息！");
		}
		String[] accountArr = accounts.split(",");
		for (String account : accountArr) {
			User u = userService.getByAccount(account);
			if (BeanUtils.isEmpty(u)) {
				throw new RuntimeException("根据输入的用户帐号【" + account + "】没有找到对应的用户");
			}
			OrgUser ou = orgUserService.getOrgUser(post.getOrgId(), u.getId(), post.getId());
			if (BeanUtils.isEmpty(ou)) {
				// continue;
				throw new RuntimeException("用户【" + account + "】不在岗位【" + post.getCode() + "】下");
			}
			orgUserService.remove(ou.getId());
		}
		return new CommonResult<String>(true, "删除成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> saveOrgPost(OrgPostVo orgPostVo) throws Exception {
		if (StringUtil.isEmpty(orgPostVo.getCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位代码code必填！");
		}
		if (orgPostVo.getCode().contains(",")) {
			throw new RuntimeException("添加岗位失败，岗位编码不允许含英文逗号‘,’");
		}
		if (StringUtil.isEmpty(orgPostVo.getName())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位名称name必填！");
		}
		if (StringUtil.isEmpty(orgPostVo.getJobCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "职务代码jobCode必填！");
		}
		if (StringUtil.isEmpty(orgPostVo.getOrgCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织代码orgCode必填！");
		}
		Org o = this.getByCode(orgPostVo.getOrgCode());
		if (BeanUtils.isEmpty(o)) {
			throw new RuntimeException("根据输入的组织代码没有找到对应的组织信息！");
		}
		OrgJob job = orgJobService.getByCode(orgPostVo.getJobCode());
		if (BeanUtils.isEmpty(job)) {
			throw new RuntimeException("根据输入的职务编码没有找到对应的职务信息！");
		}
		if (orgPostService.getCountByCode(orgPostVo.getCode())>0) {
			throw new RuntimeException("岗位编码【" + orgPostVo.getCode() + "】已经存在系统中！");
		}

		OrgPost rel = new OrgPost();
		rel.setId(UniqueIdUtil.getSuid());
		rel.setName(orgPostVo.getName());
		rel.setCode(orgPostVo.getCode());
		rel.setRelDefId(job.getId());
		rel.setOrgId(o.getId());
		rel.setIsCharge(orgPostVo.getIsCharge());
		if (orgPostVo.getIsCharge() == 1) {
			List<OrgPost> l = orgPostService.getRelCharge(o.getId(), true);
			if (BeanUtils.isNotEmpty(l)) {
				OrgPost p = l.get(0);
				p.setIsCharge(0);
				orgPostService.update(p);
			}
		}

		orgPostService.create(rel);
		return new CommonResult<String>(true, "添加岗位成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> deleteOrgPost(String postCodes) throws Exception {
		if (StringUtil.isEmpty(postCodes)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位编码postCodes必填！");
		}
		String[] codeArr = postCodes.split(",");
		for (String code : codeArr) {
			OrgPost rel = orgPostService.getByCode(code);
			if (BeanUtils.isEmpty(rel)) {
				throw new RuntimeException("根据输入的岗位编码【" + code + "】没有找到对应的岗位信息！");
			}
			
			//判断岗位组是否存在人员
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgId", rel.getOrgId());
			map.put("relId", rel.getId());
			
			List<OrgUser> orgUsers = orgUserService.getByParms(map);
			if(BeanUtils.isNotEmpty(orgUsers)) {
				throw new RuntimeException("岗位【" + rel.getName() + "】下还存在分配用户，请删除用户在进行操作！");
			}
			// if (OrgUtil.checkUserGruopIsUserRel("pos", rel.getId())) {
			// throw new RuntimeException("编码为【"+code+"】的岗位为汇报节点不能删除！");
			// }
			String[] ids = { rel.getId() };
			orgPostService.removeByIds(ids);
		}
		return new CommonResult<String>(true, "删除岗位成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> setPostMaster(String postCode, Boolean isMain) throws Exception {
		if (StringUtil.isEmpty(postCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位编码postCode必填！");
		}
		if (BeanUtils.isEmpty(isMain)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "是否责任岗位isMain必填！");
		}
		OrgPost post = orgPostService.getByCode(postCode);
		if (BeanUtils.isEmpty(post)) {
			throw new RuntimeException("根据输入的岗位编码没有找到对应的岗位！");
		}
		if (isMain) {// 若为设置责任岗位，那么先将该组织中的责任岗位取消再设置
			orgPostService.cancelRelCharge(post.getOrgId());
		}
		orgPostService.setRelCharge(post.getId(), isMain);
		return new CommonResult<String>(true, "设置成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> setUserPostDueTime(PostDueVo postDueVo) throws Exception {
		if (StringUtil.isEmpty(postDueVo.getCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "岗位编码code必填！");
		}
		if (StringUtil.isEmpty(postDueVo.getAccount())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "用户帐号account必填！");
		}
		if (BeanUtils.isEmpty(postDueVo.getEndDate()) && BeanUtils.isEmpty(postDueVo.getStartDate())) {
			throw new RuntimeException("开始时间和结束时间至少填写一个！");
		}
		OrgPost post = orgPostService.getByCode(postDueVo.getCode());
		if (BeanUtils.isEmpty(post)) {
			throw new RuntimeException("根据输入的岗位编码没有找到对应的岗位信息！");
		}
		User u = userService.getByAccount(postDueVo.getAccount());
		if (BeanUtils.isEmpty(u)) {
			throw new RuntimeException("根据输入的用户帐号没有找到对应的用户信息！");
		}
		OrgUser orgUser = orgUserService.getOrgUser(post.getOrgId(), u.getId(), post.getId());
		if (BeanUtils.isEmpty(orgUser)) {
			throw new RuntimeException("用户与该岗位没关系，不能设置！");
		}
		if (StringUtil.isNotEmpty(postDueVo.getStartDate()) && StringUtil.isNotEmpty(postDueVo.getEndDate())) {
			LocalDateTime startDate = DateFormatUtil.parse(postDueVo.getStartDate());
			LocalDateTime endDate = DateFormatUtil.parse(postDueVo.getEndDate());
			if (DateUtil.getTime(startDate, endDate) < 0) {
				throw new RuntimeException("开始时间不能大于结束时间！");
			}
		}
		if (StringUtil.isNotEmpty(postDueVo.getStartDate())) {
			orgUser.setStartDate(DateFormatUtil.parse(postDueVo.getStartDate()));
		} else {
			orgUser.setStartDate(null);
		}
		if (StringUtil.isNotEmpty(postDueVo.getEndDate())) {
			orgUser.setEndDate(DateFormatUtil.parse(postDueVo.getEndDate()));
		} else {
			orgUser.setEndDate(null);
		}
		orgUserService.update(orgUser);
		return new CommonResult<String>(true, "设置成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> validOrgUser() throws Exception {
		orgUserService.syncValidOrgUser();
		return new CommonResult<String>(true, "校验成功！", "");
	}

	@Override
    @Transactional
	public CommonResult<String> addOrgRole(OrgRoleVo orgRoleVo) throws Exception {
		if (StringUtil.isEmpty(orgRoleVo.getOrgCode())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织编码orgCode必填！");
		}
		Org org = this.getByCode(orgRoleVo.getOrgCode());
		if (BeanUtils.isEmpty(org)) {
			throw new RuntimeException("根据输入的组织编码，没有找到对应的组织信息！");
		}
		if (StringUtil.isEmpty(orgRoleVo.getRoleCodes())) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "角色编码roleCodes必填！");
		}
		String[] roleCodes = orgRoleVo.getRoleCodes().split(",");
		for (String rCode : roleCodes) {
			Role r = roleService.getByAlias(rCode);

			if (BeanUtils.isEmpty(r)) {
				r = roleService.get(rCode);
				if (BeanUtils.isEmpty(r))
					throw new RuntimeException("根据输入的角色编码，没有找到对应的角色信息！");
			}
			if (orgRoleVo.getIsInherit() > 0) {
				orgRoleService.addOrgRole(org.getId(), r, 1);
			} else {
				orgRoleService.addOrgRole(org.getId(), r, 0);
			}
		}

		return new CommonResult<String>(true, "保存成功", "");
	}

	@Override
    @Transactional
	public void delOrgRoleByCode(String orgCode, String roleCodes) throws Exception {
		if (StringUtil.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织编码orgCode必填！");
		}
		Org org = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(org)) {
			throw new RuntimeException("根据输入的组织编码，没有找到对应的组织信息！");
		}
		if (StringUtil.isEmpty(roleCodes)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "角色编码roleCodes必填！");
		}
		String[] roleCodeArr = roleCodes.split(",");
		for (String rCode : roleCodeArr) {
			Role r = roleService.getByAlias(rCode);
			if (BeanUtils.isEmpty(r)) {
				throw new RuntimeException("根据输入的角色编码，没有找到对应的角色信息！");
			}
			orgRoleService.delByOrgIdAndRoleId(org.getId(), r.getId());
		}
	}

	@Override
    @Transactional
	public CommonResult<String> delOrgRoleById(String ids) throws Exception {
		if (StringUtil.isEmpty(ids)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织角色ID必填！");
		}
		String[] roleId = ids.split(",");
		for (String id : roleId) {
			orgRoleService.remove(id);
		}
		return new CommonResult<String>(true, "删除成功", "");
	}

	@Override
    @Transactional
	public void delAllOrgRole(String orgCode) throws Exception {
		if (StringUtil.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织编码orgCode必填！");
		}
		Org org = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(org)) {
			throw new RuntimeException("根据输入的组织编码，没有找到对应的组织信息！");
		}
		orgRoleService.delByOrgIdAndRoleId(org.getId(), null);
	}

	@Override
	public List<Role> getOrgRoleByCode(String orgCode) throws Exception {
		if (StringUtil.isEmpty(orgCode)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + "组织编码orgCode必填！");
		}
		Org org = this.getByCode(orgCode);
		if (BeanUtils.isEmpty(org)) {
			throw new RuntimeException("根据输入的组织编码，没有找到对应的组织信息！");
		}
		return getRolesByOrgId(org.getId());
	}

	public List<Role> getRolesByOrgId(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		List<Role> roleList = roleService.getOrgRoleList(map);// 获取自身所拥有角色
		// 根据组织id获取。
		if (BeanUtils.isNotEmpty(roleList))
			return roleList;
		// 沿着路径往上查找。
		roleList = new ArrayList<Role>();
		String path = getPathByOrgId(orgId);
		if (StringUtil.isEmpty(path))
			return roleList;
		String[] aryPath = path.split("[.]");
		// 从后往前找。
		for (int i = aryPath.length - 1; i >= 0; i--) {
			String tmpOrgId = aryPath[i];
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("orgId", tmpOrgId);
			m.put("isInherit", 1);
			roleList = roleService.getOrgRoleList(m);// 获取父组织可继承的角色
			// 如果找到了则停止循环。
			if (BeanUtils.isNotEmpty(roleList)) {
				break;
			}
		}
		return roleList;
	}

	/**
	 * 根据组织ID获取组织路径。
	 * 
	 * @param orgId
	 *            组织ID。
	 * @return
	 */
	private String getPathByOrgId(String orgId) {
		Org sysOrg = this.get(orgId);
		if (sysOrg == null)
			return "";
		String path = sysOrg.getPath();
		if (StringUtil.isEmpty(path))
			return "";
		// 去掉当前Id。
		path = path.replace("." + orgId + ".", "");
		// 去掉维度Id。
		int pos = path.indexOf(".");
		path = path.substring(pos + 1);
		return path;
	}

	@Override
    @Transactional
	public CommonResult<String> updateOrgPost(OrgPostVo postVo) throws Exception {
		if(StringUtils.isEmpty(postVo.getCode())){
			return new CommonResult<String>(false, "岗位编码必填", "");
		}
		if(StringUtils.isEmpty(postVo.getName())){
			return new CommonResult<String>(false, "岗位名称必填", "");
		}
		OrgPost p = orgPostService.getByCode(postVo.getCode());
		if(BeanUtils.isEmpty(p)){
			return new CommonResult<String>(false, "根据输入的编码没有找到对应的岗位", "");
		}
		if (StringUtil.isNotEmpty(postVo.getOrgCode())){
            Org o = this.getByCode(postVo.getOrgCode());
            //岗位所属组织发生了变化，
            if (!p.getOrgId().equals(o.getId())) {
            	p.setOrgId(o.getId());
            	//判断新组织是否有限编
            	if (o.getExceedLimitNum() ==1 && o.getLimitNum()!=0 ){
    				Map<String, Object> map = new HashMap<>();
    				map.put("orgCode", o.getCode());
    				map.put("group", "true");
    				//获取新组织已有人数
    				List<Map<String, Object>> list = orgUserService.getUserNumByOrgCode(map);
    				if (o.getLimitNum() <= list.size() ){
    					return new CommonResult<String>(false,"岗位组织所属人数超出上限，修改失败",null);
    				}else {
    					Set<String> userIds= new HashSet<>();
    					for (Map<String, Object> l : list) {
    						userIds.add((String)l.get("userId"));
						}
    					//查询当前岗位下的人数
    					QueryFilter queryFilter = QueryFilter.build().withPage(new PageBean(1,Integer.MAX_VALUE));
    					queryFilter.addFilter("pos_id_", p.getId(), QueryOP.EQUAL);
    					IPage<HashMap<String, Object>> userByGroup = orgUserService.getUserByGroup(queryFilter);
    					//List<Map<String, Object>> userByGroup = orgUserService.getUserByGroup(queryFilter);
    					Set<String> addUserIds= new HashSet<>();
    					if (BeanUtils.isNotEmpty(userByGroup)) {
							for (Map<String, Object> orgUser : userByGroup.getRecords()) {
								if (!userIds.contains(orgUser.get("userId"))) {
									addUserIds.add((String)orgUser.get("userId"));
								}
							}
						}
    					if (o.getLimitNum() <= (list.size()+addUserIds.size())) {
    						return new CommonResult<String>(false,"岗位所属组织人数超出上限，修改失败",null);
						}
					}
    			}
            	orgUserService.updateUserOrgByPostId(p.getId(), p.getOrgId());
			}
        }

		if(postVo.getIsCharge() == 1){
			p.setIsCharge(1);
			this.setPostMaster(p.getCode(), true);
		}

		p.setName(postVo.getName());
		orgPostService.update(p);
		return new CommonResult<String>(true, "更新成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> setUnderUsers(String orgId, String account, String underAccounts) throws Exception {
		User u = userService.getByAccount(account);
		Org o = this.get(orgId);
		// 添加下属
		String[] underAccountArr = underAccounts.split(",");
		for (String a : underAccountArr) {
			User under = userService.getByAccount(a);
			if (BeanUtils.isEmpty(under)) {
				throw new BaseException("根据帐号【" + a + "】没有找到对应的用户！");
			}
			if (account.equals(a)) {
				throw new BaseException("用户【" + under.getFullname() + "】不能将自己添加为自己的下属！");
			}

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userId", under.getId());
			m.put("orgId", o.getId());
			m.put("underUserId", u.getId());
			List<UserUnder> list = userUnderService.getUserUnder(m);// 判断是否设置互为上下级
			if (BeanUtils.isNotEmpty(list)) {
				throw new BaseException(
						"用户【" + under.getFullname() + "】在组织【" + o.getCode() + "】下是用户【" + u.getFullname() + "】的上级");
			}

			Map<String, Object> m1 = new HashMap<String, Object>();
			m1.put("noUserId", u.getId());
			m1.put("orgId", o.getId());
			m1.put("underUserId", under.getId());
			List<UserUnder> list1 = userUnderService.getUserUnder(m1);// 获取该组织下，不是当前上级用户的数据
			if (BeanUtils.isNotEmpty(list1)) {// 同一组织下，用户只能有一个上级
				User supperUser = userService.get(list1.get(0).getUserId());
				throw new BaseException("用户【" + under.getFullname() + "】在当前组织中已有上级" + "【" + supperUser.getFullname()
						+ "】，在当前组织中不能再设置其他上级。");
			} else {
				Map<String, Object> m2 = new HashMap<String, Object>();
				m2.put("userId", u.getId());
				m2.put("orgId", o.getId());
				m2.put("underUserId", under.getId());
				List<UserUnder> list2 = userUnderService.getUserUnder(m2);// 获取该组织下，是当前上级用户的数据
				if (BeanUtils.isNotEmpty(list2)) {//
					continue;
				} else {
					UserUnder userUnder = new UserUnder();
					userUnder.setId(UniqueIdUtil.getSuid());
					userUnder.setUserId(u.getId());
					userUnder.setUnderUserId(under.getId());
					userUnder.setUnderUserName(under.getFullname());
					userUnder.setOrgId(o.getId());
					userUnderService.create(userUnder);
				}
			}
			if (BeanUtils.isEmpty(orgUserService.getListByOrgIdUserId(o.getId(), under.getId()))) {// 若添加的下属还不属于该组织人员，那么将用户添为该组织人员
				OrgUser ou = new OrgUser();
				ou.setId(UniqueIdUtil.getSuid());
				ou.setUserId(under.getId());
				ou.setOrgId(orgId);
			}
		}
		return new CommonResult<String>(true, "添加下属成功", "");
	}

	@Override
    @Transactional
	public CommonResult<String> addUsersForOrg(String orgCode, String accounts) throws Exception {
		String[] accountArr = accounts.split(",");
		Org o = this.getByCode(orgCode);
		for (String a : accountArr) {
			User u = userService.getByAccount(a);
			if (BeanUtils.isEmpty(u)) {
				return new CommonResult<String>(false, "帐号【" + a + "】不存在", "");
			}
			List<OrgUser> l = orgUserService.getListByOrgIdUserId(o.getId(), u.getId());
			if (BeanUtils.isNotEmpty(l)) {
				continue;
			}
			OrgUser ou = new OrgUser();
			ou.setId(UniqueIdUtil.getSuid());
			ou.setIsCharge(0);
			ou.setIsMaster(0);
			ou.setOrgId(o.getId());
			ou.setUserId(u.getId());
			ou.setVersion(1);
			ou.setIsRelActive(1);
			orgUserService.create(ou);
		}
		return new CommonResult<String>(true, "加入成功", "");
	}

	@Override
	public List<Org> getOrgByTime(OrgExportObject exportObject) throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		List<Demension> list = new ArrayList<Demension>();
		if (StringUtil.isNotEmpty(exportObject.getDemCodes())) {
			QueryFilter filter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
			filter.addFilter("CODE_", exportObject.getDemCodes(), QueryOP.IN, FieldRelation.AND);
			list = DemensionManager.queryNoPage(filter);
		}
		if (BeanUtils.isNotEmpty(list)) {
			Set<String> demIds = new HashSet<String>();
			for (Demension demension : list) {
				demIds.add(demension.getId());
			}
			queryFilter.addFilter("DEM_ID_", new ArrayList<String>(demIds), QueryOP.IN, FieldRelation.AND);
		}

		if (StringUtil.isNotEmpty(exportObject.getOrgCodes())) {
			List<String> ids = getOrgAndChildrens(exportObject.getOrgCodes());
			if (BeanUtils.isNotEmpty(ids)) {
				queryFilter.addFilter("ID_", ids, QueryOP.IN, FieldRelation.AND);
			}
		}

		return this.queryNoPage(queryFilter);
	}

	/**
	 * 获取组织的子组织
	 * 
	 * @param orgCodes
	 * @return
	 */
	private List<String> getOrgAndChildrens(String orgCodes) {
		QueryFilter filter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		filter.setPageBean(new PageBean(1, Integer.MAX_VALUE));
		filter.addFilter("CODE_", orgCodes, QueryOP.IN, FieldRelation.AND);
		List<Org> porgs = this.queryNoPage(filter);
		if (BeanUtils.isNotEmpty(porgs)) {
			QueryFilter filter2 = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
			filter2.setPageBean(new PageBean(1, Integer.MAX_VALUE));
			for (Org org : porgs) {
				filter2.addFilter("PATH_", org.getPath(), QueryOP.RIGHT_LIKE, FieldRelation.OR);
			}
			List<Org> childrens = this.queryNoPage(filter2);
			if (BeanUtils.isNotEmpty(childrens)) {
				Set<String> ids = new HashSet<String>();
				for (Org org : childrens) {
					ids.add(org.getId());
				}
				List<String> list = new ArrayList<String>();
				list.addAll(ids);
				return list;
			}

		}
		return new ArrayList<String>();
	}

	/**
	 * 获取同步数据时，如果传了组织相关的参数，则添加过滤条件
	 * 
	 * @param queryFilter
	 * @param exportObject
	 * @throws Exception
	 */
    @Transactional
	private void addOrgFilter(QueryFilter queryFilter, OrgExportObject exportObject) throws Exception {
		if (StringUtil.isNotEmpty(exportObject.getDemCodes()) || StringUtil.isNotEmpty(exportObject.getOrgCodes())) {
			List<Org> orgs = getOrgByTime(exportObject);
			if (BeanUtils.isNotEmpty(orgs)) {
				List<String> orgIds = new ArrayList<String>();
				for (Org org : orgs) {
					orgIds.add(org.getId());
				}
				queryFilter.addFilter("ORG_ID_", orgIds, QueryOP.IN, FieldRelation.AND);
			}
		}
	}

	@Override
	public List<OrgParams> getOrgParamByTime(OrgExportObject exportObject) throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		return orgParamsService.queryNoPage(queryFilter);
	}

	@Override
	public List<OrgPost> getOrgPostByTime(OrgExportObject exportObject) throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		return orgPostService.queryNoPage(queryFilter);
	}

	@Override
	public List<OrgRole> getOrgRoleByTime(OrgExportObject exportObject) throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		addOrgFilter(queryFilter, exportObject);
		return orgRoleManager.queryNoPage(queryFilter);
	}

	@Override
	public List<OrgUser> getOrgUserByTime(OrgExportObject exportObject) throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		addOrgFilter(queryFilter, exportObject);
		return orgUserService.queryNoPage(queryFilter);
	}

	@Override
	public List<UserUnder> getUserUnderByTime(OrgExportObject exportObject) throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(exportObject.getBtime(), exportObject.getEtime());
		addOrgFilter(queryFilter, exportObject);
		return userUnderManager.queryNoPage(queryFilter);
	}

	@Override
	public List<Org> getOrgsByAccount(String account) throws Exception {
		if (StringUtil.isEmpty(account)) {
			throw new RuntimeException(HotentHttpStatus.REUIRED.description() + ":用户帐号account必填");
		}
		List<Org> list = baseMapper.justGetOrgsByAccount(account);
		for (Org org : list) {
			Demension dem = demensionService.get(org.getDemId());
			if (BeanUtils.isNotEmpty(dem)) {
				org.setDemName(dem.getDemName());
			}
		}
		OrgUtil.removeDuplicate(list, "id");
		return list;
	}

	@Override
    @Transactional
	public Integer removePostPhysical() {
		return orgPostDao.removePhysical();
	}

	@Override
    @Transactional
	public Integer removeOrgUserPhysical() {
		return orgUserService.removePhysical();
	}

	@Override
	public CommonResult<Boolean> isCodeExist(String code) throws Exception {
		Org org = baseMapper.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(org);
		return new CommonResult<Boolean>(isExist, isExist ? "该组织编码已存在！" : "", isExist);
	}

	@Override
	public CommonResult<Boolean> isPostCodeExist(String code) throws Exception {
		OrgPost post = orgPostDao.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(post);
		return new CommonResult<Boolean>(isExist, isExist ? "该岗位编码已存在！" : "", isExist);
	}

	@Override
    @Transactional
	public CommonResult<String> updateOrgPos(String orgId, String parentId) throws Exception {
		if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(parentId)) {
			throw new RequiredException("移动节点或目标节点id不能为空！");
		}
		Org org = this.get(orgId);
		if (BeanUtils.isEmpty(org)) {
			throw new RequiredException("根据移动节点id【" + orgId + "】未找到对应组织！");
		}
		org.setParentId(parentId);
		if ("0".equals(parentId)) {
			parentId = org.getDemId();
		}
		Org parentOrg = this.get(parentId);
		if (BeanUtils.isEmpty(parentOrg)) {
			org.setPath(parentId + "." + orgId + ".");
			org.setPathName("/" + org.getName());
		} else {
			org.setPath(parentOrg.getPath() + orgId + ".");
			org.setPathName(parentOrg.getPathName() + "/" + org.getName());
		}
		this.update(org);
		updateChildrenOrg(org);
		return new CommonResult<String>(true, "更新组织成功！", "");
	}

	/**
	 * 递归更新子组织的path和pathName
	 * 
	 * @param parentOrg
	 * @throws SQLException
	 */
    @Transactional
	private void updateChildrenOrg(Org parentOrg) throws SQLException {
		List<Org> childrens = this.getByParentId(parentOrg.getId());
		for (Org org : childrens) {
			org.setPath(parentOrg.getPath() + org.getId() + ".");
			org.setPathName(parentOrg.getPathName() + "/" + org.getName());
			this.update(org);
			updateChildrenOrg(org);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String exportData(String orgCode, boolean isChildre) throws Exception {
		if (StringUtil.isEmpty(orgCode)) {
			throw new RequiredException("组织编码【orgCode】不能为空！");
		}
		Org org = baseMapper.getByCode(orgCode);
		if (BeanUtils.isEmpty(org)) {
			throw new RequiredException("根据组织编码【" + orgCode + "】找不到对应组织！");
		}
		QueryFilter queryFilter = QueryFilter.build();
		queryFilter.getPageBean().setPageSize(Integer.MAX_VALUE);
		if (isChildre) {
			queryFilter.getParams().put("whereSql", " org.path_ like '" + org.getPath() + "%'");
		} else {
			queryFilter.getParams().put("orgId", org.getId());
		}
		String orgName = org.getName();
		String times = DateUtil.getCurrentTime("yyyymmddhhmmss");
		List list = orgUserService.getOrgUserData(queryFilter);
		String path = (FileUtil.getIoTmpdir() + "/attachFiles/tempZip/" + orgName + "/" + times).replace("/",
				File.separator);
		String excelPath = path + File.separator;
		String excelName = "组织【" + orgName + "】相关数据";
		exportExcel(list, orgName, excelName, excelPath);
		String filePath = excelPath + excelName;
		return filePath;
	}

	private <E> void exportExcel(List<E> list, String sheetName, String excelName, String excelPath) throws Exception {
		Map<String, String> exportMaps = new LinkedHashMap<String, String>();
		exportMaps.put("fullName", "用户姓名");
		exportMaps.put("account", "用户账号");
		exportMaps.put("userNumber", "工号");
		exportMaps.put("userId", "用户ID");
		exportMaps.put("status", "用户状态");
		exportMaps.put("mobile", "手机号码");
		exportMaps.put("email", "邮箱");
		exportMaps.put("orgName", "组织名称");
		exportMaps.put("orgCode", "组织编码");
		exportMaps.put("orgId", "组织ID");
		exportMaps.put("orgPath", "组织路径");
		exportMaps.put("orgPathName", "路径名称");
		exportMaps.put("isMaster", "主岗位");
		exportMaps.put("isCharge", "负责人");
		exportMaps.put("relName", "岗位名称");
		exportMaps.put("postCode", "岗位编码");
		exportMaps.put("relId", "岗位ID");
		exportMaps.put("isRelActive", "岗位是否有效");
		HSSFWorkbook book = ExcelUtil.exportExcel(sheetName, 16, exportMaps, list);
		ExcelUtil.saveExcel(book, excelName, excelPath);
	}

	@Override
	public List<Map<String, String>> getPathNames(List<String> userIds) {
		return baseMapper.getPathNames(userIds);
	}

	@Override
	public Map<String, Set<String>> getChildrenIds(Map<String, String> ids) {
		Map<String, Set<String>> resultMap = new HashMap<String, Set<String>>();
		if (BeanUtils.isNotEmpty(ids)) {
			for (String key : ids.keySet()) {
				String idStr = ids.get(key);
				if (StringUtil.isEmpty(idStr)) {
					continue;
				}
				String[] idArray = idStr.split(",");
				QueryFilter filter1 = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
				filter1.addFilter("ID_", idArray, QueryOP.IN);
				List<Org> orgs = this.queryNoPage(filter1);
				if (BeanUtils.isNotEmpty(orgs)) {
					Map<String, String> pathMap = new HashMap<String, String>();
					for (int i = 0; i < orgs.size(); i++) {
						pathMap.put(String.valueOf(i + 1), orgs.get(i).getPath());
					}
					List<String> childrenIds = baseMapper.getChildrenIds(pathMap);
					resultMap.put(key, new HashSet<>(childrenIds));
				}
			}
		}
		return resultMap;
	}

	@Override
	public Org getOrgMaster(String account) {
		List<Org> orglist = baseMapper.getOrgMaster(account);
		if (BeanUtils.isNotEmpty(orglist)) {
			return orglist.get(0);
		} else {
			return null;
		}
	}

	@Override
    @Transactional
	public CommonResult<String> addOrgFromExterUni(Org orgVo) {
		if (BeanUtils.isEmpty(orgVo.getName())) {
			throw new RuntimeException("添加组织失败，组织名称【name】不能为空！");
		}
		if (BeanUtils.isEmpty(orgVo.getCode())) {
			throw new RuntimeException("添加组织失败，组织编码【code】不能为空！");
		}
		if (orgVo.getCode().contains(",")) {
			throw new RuntimeException("组织编码中不能包含英文逗号‘,’");
		}
		if (BeanUtils.isEmpty(orgVo.getDemId())) {
			Demension defaultDem =  demensionService.getDefaultDemension();
			if(BeanUtils.isEmpty(defaultDem)) {
				throw new RuntimeException("添加组织失败，本系统无默认维度");
			}else {
				orgVo.setDemId(defaultDem.getId());
			}
		}else {
			Demension dem = demensionService.get(orgVo.getDemId());
			if (BeanUtils.isEmpty(dem)) {
				throw new RuntimeException("添加组织失败，根据输入的demId[" + orgVo.getDemId() + "]没有找到对应的维度信息！");
			}
		}
		
		Org o = this.getByCode(orgVo.getCode());
		if (BeanUtils.isNotEmpty(o)) {
			throw new RuntimeException("添加组织失败，组织编码[" + orgVo.getCode() + "]已存在！");
		}
		Org pOrg = null;
		if (!"0".equals(orgVo.getParentId()) && BeanUtils.isNotEmpty(orgVo.getParentId())) {
			pOrg = this.get(orgVo.getParentId());
			if (BeanUtils.isEmpty(pOrg)) {
				throw new RuntimeException("添加组织失败，根据输入的parentId[" + orgVo.getParentId() + "]没有找到对应的组织信息！");
			}
			if (BeanUtils.isEmpty(pOrg) && !pOrg.getDemId().equals(orgVo.getDemId())) {
				throw new RuntimeException("添加组织失败，根据输入demId与所输入的父组织所对应的维度id不一致！");
			}
		}
		o = new Org();
		o.setId(orgVo.getId());
		o.setCode(orgVo.getCode());
		o.setName(orgVo.getName());
		o.setDemId(orgVo.getDemId());
		o.setOrderNo(orgVo.getOrderNo());
		if (StringUtils.isEmpty(orgVo.getParentId())) {
			o.setParentId("0");
		} else {
			o.setParentId(orgVo.getParentId());
		}
		if (BeanUtils.isEmpty(pOrg)) {
			o.setPathName("/" + orgVo.getName());
			o.setPath(orgVo.getDemId() + "." + o.getId() + ".");
		} else {
			o.setPath(pOrg.getPath() + o.getId() + ".");
			o.setPathName(pOrg.getPathName() + "/" + orgVo.getName());
		}
		this.create(o);
		return new CommonResult<String>(true, "添加组织成功！", "");
	}

	@Override
	public List<Org> getOrgsByparentId(String parentId) {
		return baseMapper.getByParentId(parentId);
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
	
	@Override
	public Org get(Serializable id) {
		return baseMapper.get(id);
	}

	@Override
	public List<String> getSubOrgByIds(Set<String> parentIds) {
		if(parentIds.isEmpty()) {
			return new ArrayList<>();
		}
		return baseMapper.getSubOrgByIds(parentIds);
	}

	@Override
	public List<Map<String, Object>> getOrgIdMapByUserId(String userId) {
		return baseMapper.getOrgIdMapByUserId(userId);
	}
	
	@Override
	public CommonResult<Org> getFillOrg(String demId,String grade) {
		String userId = ContextUtil.getCurrentUserId();
		Org curOrg = this.getMainGroup(userId, demId);
		Org gradeOrg = null;
		if(BeanUtils.isNotEmpty(curOrg)){
			if(grade.equals(curOrg.getGrade())){
				gradeOrg = curOrg;
			}else{
				gradeOrg = getPOrgByGread(curOrg.getParentId(),grade);
			}
		}else{
			return new CommonResult<>(false, "获取当前用户所属主组织失败！");
		}
		if(BeanUtils.isEmpty(gradeOrg)){
			return new CommonResult<>(false, "未获取到当前用户填制单位！");
		}
		return new CommonResult<>(true, "获取成功！",gradeOrg);
	}
	
	private Org getPOrgByGread(String orgId,String grade){
		Org org = this.get(orgId);
		if(BeanUtils.isNotEmpty(org) && !grade.equals(org.getGrade())){
			return getPOrgByGread(org.getParentId(), grade);
		}
		return org;
	}
}
