package com.hotent.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.activemq.model.JmsSysTypeChangeMessage;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.NotFoundException;
import com.hotent.base.exception.RequiredException;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.constants.CategoryConstants;
import com.hotent.sys.persistence.manager.DataDictManager;
import com.hotent.sys.persistence.manager.SysCategoryManager;
import com.hotent.sys.persistence.manager.SysTypeManager;
import com.hotent.sys.persistence.model.SysCategory;
import com.hotent.sys.persistence.model.SysType;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre>
 * 描述：总分类表。用于显示平级或树层次结构的分类，可以允许任何层次结构。管理
 * 构建组：x5-bpmx-platform
 * 作者:zyp
 * 邮箱:zyp@jee-soft.cn
 * 日期:2014-1-10-下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysType/v1")
@Api(tags="系统分类")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysTypeController extends BaseController<SysTypeManager, SysType>{
	@Resource
	SysTypeManager sysTypeManager;
	@Resource
	SysCategoryManager sysCategoryManager;
	@Resource
	IUserService iUserService;
	@Resource
	JmsProducer jmsProducer;
	@Resource
	DataDictManager dataDictManager;

	@RequestMapping(value="list", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "系统分类", httpMethod = "POST", notes = "系统分类")
	public PageList<SysType> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysType> queryFilter) throws Exception {
		return sysTypeManager.query(queryFilter);
	}

	@RequestMapping(value="editJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "编辑总分类表。用于显示平级或树层次结构的分类，可以允许任何层次结构", httpMethod = "GET", notes = "系统分类")
	public Map<String,Object> editJson(
			@ApiParam(name="id", value="主键", required = false)@RequestParam String id,
			@ApiParam(name="isRoot", value="是否是根节点，1=根节点，0=其他节点", required = false)@RequestParam int isRoot,
			@ApiParam(name="parentId", value="父节点ID", required = false)@RequestParam String parentId,
			@ApiParam(name="isPriNode", value="是否是私有的节点，1=私有节点，0=普通节点", required = false)@RequestParam int isPriNode
		) throws Exception {
		
		Map<String,Object> map = new HashMap<String,Object>();
		String parentName = "";
		SysType sysType = null;
		boolean isAdd = false;
		// 是否是数据字典
		boolean isDict = false;
		if (StringUtil.isNotEmpty(id)) {
			sysType = sysTypeManager.get(id);
			parentId = sysType.getParentId();
			isDict = CategoryConstants.CAT_DIC.key().equals(sysType.getTypeGroupKey());
			if (!"0".equals(sysType.getOwnerId())) {
				isPriNode = 1;
			}
		} else {
			SysType sysTypeTemp = sysTypeManager.getInitSysType(isRoot, parentId);
			parentName = sysTypeTemp.getName();
			sysType = new SysType();
			sysType.setStruType(sysTypeTemp.getStruType());
			sysType.setTypeGroupKey(sysTypeTemp.getTypeGroupKey());
			isDict = CategoryConstants.CAT_DIC.key().equals(sysTypeTemp.getTypeGroupKey());
			isAdd = true;
		}
		 map.put("sysType", sysType);
		 map.put("isAdd", isAdd);
		 map.put("isRoot", isRoot);
		 map.put("isDict", isDict);
		 map.put("parentId", parentId);
		 map.put("parentName", parentName);
		 map.put("isPriNode", isPriNode);
		 map.put("isPriNode", isPriNode);
		 return map;
	}

	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "系统分类明细页面", httpMethod = "GET", notes = "系统分类")
	public SysType getJson(@ApiParam(name="id", value="主键", required = false)@RequestParam String id) throws Exception {
		SysType sysType = new SysType();
		if (StringUtil.isNotEmpty(id)) {
			sysType = sysTypeManager.get(id);
			if(BeanUtils.isEmpty(sysType)){
				sysType = sysTypeManager.getByKey(id);
			}
		}
		return sysType;
	}

	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存分类信息", httpMethod = "POST", notes = "保存系统属性信息")
	public CommonResult<String> save(
			@ApiParam(name="sysType", value="分类", required = true)@RequestBody SysType sysType,
			@ApiParam(name="parentId", value="父节点", required = true)@RequestParam String parentId,
			@ApiParam(name="isRoot", value="是否根节点", required = false)@RequestParam int isRoot,
			@ApiParam(name="isPriNode", value="是否是私有的分类", required = false)@RequestParam int isPriNode
			) throws Exception {
		
		String resultMsg = null;
		String id = sysType.getId();
		String typeKey = sysType.getTypeKey();
		String typeGroupKey = sysType.getTypeGroupKey();
		String curUserId = ContextUtil.getCurrentUserId();
		if (StringUtil.isEmpty(id)) {
			id = null;
		}
		boolean isKeyExist = sysTypeManager.isKeyExist(id, typeGroupKey, typeKey);
		if (isKeyExist) {
			resultMsg = "输入的分类key【" + typeKey + "】已存在!";
			return new CommonResult<String>(false, resultMsg);
		} else {
			try {
				if (StringUtil.isEmpty(id)) {
					// 如果不是根节点，则需要更新分类的叶子
					if (isRoot != 1) {
						SysType parentSysType = sysTypeManager.get(parentId);
						if (BeanUtils.isNotEmpty(parentSysType)) {
							parentSysType.setIsLeaf(SysCategory.IS_LEAF_N);
							sysTypeManager.update(parentSysType);
						}
					}

					SysType sysTypeTemp = sysTypeManager.getInitSysType(isRoot, parentId);
					// 分类key不为数据字典的情况
					if (!typeGroupKey.equals(CategoryConstants.CAT_DIC.key())) {
						sysType.setStruType(sysTypeTemp.getStruType());
					}
					// 设置是否是私有的分类 0 表示公共的分类，当前用户Id表示私有的分类
					if (isPriNode == 1) {
						sysType.setOwnerId(curUserId);
					} else {
						sysType.setOwnerId("0");
					}
					sysType.setTypeGroupKey(typeGroupKey);
					sysType.setTypeKey(typeKey);
					sysType.setPath(sysTypeTemp.getPath());
					sysType.setParentId(parentId);
					sysType.setId(sysTypeTemp.getId());
					sysType.setDepth(1);
					sysType.setSn(0);
					sysType.setIsLeaf(SysCategory.IS_LEAF_Y);
					sysTypeManager.create(sysType);
					resultMsg = "添加分类成功";
					return new CommonResult<String>(true, resultMsg);
				} else {
					sysType.setSn(0);
					SysType oldType = sysTypeManager.get(sysType.getId());
					sysTypeManager.update(sysType);
					jmsProducer.sendToTopic(new JmsSysTypeChangeMessage(typeGroupKey,sysType.getId(),sysType.getName(),oldType.getName(),1));
					resultMsg = "更新分类成功";
					return new CommonResult<String>(true, resultMsg);
				}
			} catch (Exception e) {
				return new CommonResult<String>(false, "更新失败");
			}
		}
	}
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除系统分类", httpMethod = "DELETE", notes = "批量删除系统分类")
	public CommonResult<String> remove(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		try {
			dataDictManager.delByDictTypeId(id);
			sysTypeManager.delByIds(id);
			return new CommonResult<String>(true, "删除成功");
		} catch (Exception e) {
			return new CommonResult<String>(false, "删除失败");
		}
	}

	@RequestMapping(value="sysTypeTree", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "分类树", httpMethod = "POST", notes = "分类树")
	public Map<String,Object> tree() throws Exception {
		List<SysCategory> list = sysCategoryManager.list();
		SysCategory sysCategory = list.get(0);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sysCategoryList", list);
		map.put("sysCategory", sysCategory);
		return map;
	}
	
	@RequestMapping(value="getByParentId", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据父节点获取分类", httpMethod = "GET", notes = "根据父节点获取分类")
	public List<SysType> getByParentId(@ApiParam(name="catId", value="分类id", required = true,defaultValue="0")@RequestParam String catId) throws Exception {
		List<SysType> listSysType = new ArrayList<>();
		SysCategory sysCategory = sysCategoryManager.get(catId.toString());
		if(BeanUtils.isEmpty(sysCategory)) {
			return listSysType;
		}
		SysType sysType = new SysType();
		sysType.setId(sysCategory.getId());
		sysType.setName(sysCategory.getName());
		sysType.setParentId("0");
		sysType.setOpen("true");
		sysType.setTypeKey(sysCategory.getGroupKey());
		List<SysType> list = sysTypeManager.getByGroupKey(sysCategory.getGroupKey());
		for(SysType entity : list){
			SysType sysTypes = new SysType();
			sysTypes = entity;
			sysTypes.setOpen("true");
			List<SysType> sys_Type = sysTypeManager.getByParentId(sysTypes.getId());
			if(sys_Type!=null && sys_Type.size()==0){
				sysTypes.setIsParent("false");
			}
			listSysType.add(entity);
		}
		listSysType.add(0, sysType);
		return listSysType;
	}
	
	
	@RequestMapping(value="updateModelType", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "更新", httpMethod = "POST", notes = "更新")
	public CommonResult<String> updateModelType(
			@ApiParam(name="typeId", value="分类id", required = true)@RequestParam String typeId,
			@ApiParam(name="typeName", value="分类名称", required = true)@RequestParam String typeName,
			@ApiParam(name="ids", value="主键", required = true)@RequestParam String[] ids,
			@ApiParam(name="tableName", value="表名", required = true)@RequestParam String tableName,
			@ApiParam(name="idKey", value="主键字段", required = false,defaultValue="id_")@RequestParam String idKey,
			@ApiParam(name="typeIdKey", value="分类id字段", required = false,defaultValue="type_id_")@RequestParam String typeIdKey,
			@ApiParam(name="typeNameKey", value="分类名称字段", required = false)@RequestParam String typeNameKey
			) throws Exception {
		
		String idStr ="";
		for (int i = 0; i < ids.length; i++) {
			idStr =idStr + "'"+ids[i]+"'";
			if(i != ids.length-1) idStr+="," ;
		}
		try {
			JdbcTemplate template =(JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			StringBuffer sb = new StringBuffer("update "+tableName+" set ");
			sb.append(typeIdKey+" = '"+typeId+"'");
			if(StringUtil.isNotEmpty(typeNameKey)){
				sb.append(","+typeNameKey + " = '"+typeName+"' ");
			}
			
			sb.append("where "+idKey+" in("+idStr+")");
		  template.execute(sb.toString());
		  try {
			  //清除缓存
			  if(tableName.equals("bpm_definition")){
				  for(String defId:ids){
					  //先注释,因为模块包还不完整 by dengyg 2018-07-05
					  //String key = BpmDefinition.BPM_DEFINITION + defId;
					  //iCache.delByKey(key);
				  }
			  }
		} catch (Exception e) {
			return new CommonResult<String>(false, "更新失败");
		}
		} catch (Exception e) {
			return new CommonResult<String>(false, "更新失败");
		}
		return new CommonResult<String>(true, "更新成功");
	}
	
	@RequestMapping(value="sysTypeSortList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "排序列表页面", httpMethod = "POST", notes = "排序列表页面")
	public List<SysType> sortList(@ApiParam(name="id", value="主键", required = true,defaultValue="-1")@RequestParam String id) throws Exception {
		String curUserId = ContextUtil.getCurrentUserId();		
		List<SysType> sysTypes = sysTypeManager.getPrivByPartId(id, curUserId);
		return sysTypes;
	}

	@RequestMapping(value="sort", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "排序", httpMethod = "POST", notes = "排序")
	public CommonResult<String> sort(@ApiParam(name="typeIds", value="分类id", required = true)@RequestParam String[] typeIds) throws Exception {
		try {
			if (BeanUtils.isNotEmpty(typeIds)) {
				for (int i = 0; i < typeIds.length; i++) {
					String typeId = typeIds[i];
					int sn = i + 1;
					sysTypeManager.updSn(typeId, sn);
				}
			}
		} catch (Exception e) {
			return new CommonResult<String>(false, "排序失败");
		}
		return new CommonResult<String>(true, "排序完成");
	}

	
	@RequestMapping(value="getTypesByKey", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据可以key获取分类", httpMethod = "GET", notes = "根据可以key获取分类")
	public List<SysType> getTypesByKey(@ApiParam(name="typeKey", value="分类key", required = true)@RequestParam String typeKey) throws Exception {
		SysCategory sysCategory = sysCategoryManager.getByTypeKey(typeKey);
		List<SysType> listSysType = new ArrayList<>();
		if(BeanUtils.isEmpty(sysCategory)) {
			return listSysType;
		}
		// 根节点parentId = 0； 标记
		SysType sysType = new SysType();
		sysType.setId(sysCategory.getId());
		sysType.setName(sysCategory.getName());
		sysType.setParentId("0");
		sysType.setOpen("true");
		sysType.setTypeKey(sysCategory.getGroupKey());
		List<SysType> list = sysTypeManager.getByGroupKey(sysCategory.getGroupKey());
		listSysType.addAll(list);
		listSysType.add(sysType);
		List<SysType> rtnList= BeanUtils.listToTree(listSysType);

		return rtnList;
	}
	
	@RequestMapping(value="getTreeDateByTypeKey", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据可以key获取分类树", httpMethod = "GET", notes = "根据可以key获取分类树")
	public List<SysType> getTreeDateByTypeKey(@ApiParam(name="typeKey", value="分类key", required = true)@RequestParam String typeKey) throws Exception {
		List<SysType> groupTypes = null;
		if (StringUtil.isNotEmpty(typeKey)) {
			groupTypes = sysTypeManager.getChildByTypeKey(typeKey);
		}
		List<SysType> rtnList= BeanUtils.listToTree(groupTypes);
		return rtnList;
	}

	@RequestMapping(value="getByGroupKey", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据可以组key获取分类", httpMethod = "GET", notes = "根据可以组key获取分类")
	public List<SysType> getByGroupKey(@ApiParam(name="groupKey", value="分类组key", required = true)@RequestParam String groupKey) throws Exception {
		List<SysType> types = null;
		if (StringUtil.isNotEmpty(groupKey)) {
			types = sysTypeManager.getByGroupKey(groupKey);
		}
		List<SysType> rtnList= BeanUtils.listToTree(types);
		return rtnList;
	}
	
	@RequestMapping(value="updateEntitySysType", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "更新对象的分类信息", httpMethod = "GET", notes = "更新对象的分类信息")
	public CommonResult<String> updateEntitySysType(
			@ApiParam(name="typeID", value="要设置为的分类id", required = true)@RequestParam String typeID,
			@ApiParam(name="entityIds", value="要更新的实体id", required = true)@RequestParam String  entityIds
			) throws Exception {
		SysType type = sysTypeManager.get(typeID);
		if (StringUtil.isEmpty(entityIds)) {
			throw new RequiredException("请传入要更新的实体id");
		}
		
		if (BeanUtils.isEmpty(type)) {
			throw new NotFoundException("根据所传分类ID未找到分类");
		}
		jmsProducer.sendToTopic(new JmsSysTypeChangeMessage(type.getTypeGroupKey(),type.getId(),type.getName(),3,entityIds));
		return new CommonResult<String>("更新分类成功");
	}
}
