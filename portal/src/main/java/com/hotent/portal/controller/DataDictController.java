package com.hotent.portal.controller;


import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.constants.CategoryConstants;
import com.hotent.sys.persistence.manager.DataDictManager;
import com.hotent.sys.persistence.manager.SysTypeManager;
import com.hotent.sys.persistence.model.DataDict;
import com.hotent.sys.persistence.model.SysType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * <pre> 
 * 描述：数据字典管理
 * 构建组：x5-bpmx-platform
 * 作者:miao
 * 邮箱:miao@jee-soft.cn
 * 日期:2014-1-10-下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */

@RestController
@RequestMapping("/sys/dataDict/v1")
@Api(tags="数据字典")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class DataDictController extends BaseController<DataDictManager,DataDict>{
	@Resource
	DataDictManager dataDictManager;
	@Resource
	SysTypeManager sysTypeManager;
	@Resource
	IdGenerator idGenerator;
	
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "字典列表", httpMethod = "POST", notes = "字典列表")
	@ResponseBody
	public PageList<DataDict> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<DataDict> queryFilter) throws Exception{
		return dataDictManager.query(queryFilter);
	}
	
	@RequestMapping(value="dataDictEdit", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "编辑数据字典信息页面", httpMethod = "GET", notes = "编辑数据字典信息页面")
	public Map<String,Object> edit(
			@ApiParam(name="id",value="主键")@RequestParam String id,
			@ApiParam(name="isAdd",value="是否是添加")@RequestParam int isAdd,
			@ApiParam(name="isRoot",value="是否是根节点",required = false)@RequestParam int isRoot
			) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isAdd", isAdd);
		DataDict dataDict=null;
		// 根节点的id = typeId
		if(isRoot == 1 && isAdd == 1){
			map.put("typeId", id);
			map.put("parentId", id);
			return map;
		}//普通节点添加
		else if(isAdd == 1 && StringUtil.isNotEmpty(id)){
			map.put("parentId", id);
			dataDict=dataDictManager.get(id);
			map.put("typeId", dataDict.getTypeId());
			return map;
		}// 编辑情况
		else if(StringUtil.isNotEmpty(id)){
			dataDict=dataDictManager.get(id);
			map.put("dataDict", dataDict);
			return map;
		}
		return map; 
	}
	
	@RequestMapping(value="getByTypeId", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据分类id获取字典", httpMethod = "POST", notes = "根据分类id获取字典")
	public @ResponseBody List<DataDict> getByTypeId(@ApiParam(name="typeId",value="分类id",required = true)@RequestBody String typeId) throws Exception{
		if(StringUtil.isEmpty(typeId)) return null;
		SysType dictType = sysTypeManager.get(typeId);
		return getDataDict(dictType,true);
	}
	
	
	@RequestMapping(value="getByTypeKey", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据分类key获取字典", httpMethod = "GET", notes = "根据分类key获取字典")
	public List<DataDict> getByTypeKey(@ApiParam(name="typeKey",value="分类id",required = true)@RequestParam String typeKey) throws Exception{
		if(StringUtil.isEmpty(typeKey)) return null;
		SysType dictType = sysTypeManager.getByKey(typeKey);
		return getDataDict(dictType,false); 
	}
	
	@RequestMapping(value="getByTypeIdForComBo", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据分类id获取字典(ComBo)", httpMethod = "POST", notes = "根据分类id获取字典(ComBo)")
	public List<DataDict> getByTypeIdForComBo(@ApiParam(name="typeId",value="分类id",required = true )@RequestBody String typeId) throws Exception
	{
		if(StringUtil.isEmpty(typeId)) return null;
		SysType dictType = sysTypeManager.get(typeId);
		List<DataDict> list= getDataDict(dictType,false);
		List<DataDict> rtnList=BeanUtils.listToTree(list);
		return rtnList;
	}
	
	@RequestMapping(value="getByTypeKeyForComBo", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "通过groupKey、typeKey获取数据字典", httpMethod = "POST", notes = "通过groupKey、typeKey获取数据字典")
	public List<DataDict> getByTypeKeyForComBo(@ApiParam(name="typeKey",value="分类key",required = true)@RequestParam String typeKey) throws Exception
	{
        if(StringUtil.isEmpty(typeKey)) return null;
        SysType dictType = sysTypeManager.getByTypeKeyAndGroupKey(CategoryConstants.CAT_DIC.key(), typeKey);
        List<DataDict> list= getDataDict(dictType,false);
        List<DataDict> rtnList=BeanUtils.listToTree(list);
        return rtnList;
        //todo 修复4465 BUG 注释下面代码
		//if(StringUtil.isEmpty(typeKey)) return null;
		//SysType dictType = sysTypeManager.getByTypeKeyAndGroupKey(CategoryConstants.CAT_DIC.key(), typeKey);
		//if(BeanUtils.isEmpty(dictType))return null;
		//String typeId = dictType.getId();
		//List<DataDict> dataDictList=dataDictManager.getByTypeId(typeId);
		//boolean isChildren=dictType.getStruType()==0;
		//List<DataDict> list = new ArrayList<>();
		//for(DataDict entity : dataDictList){
		//	DataDict dataDicts = new DataDict();
		//	if(isChildren && !entity.getParentId().equals(typeId)){
		//		continue;
		//	}
		//	dataDicts = entity;
		//	dataDicts.setOpen("true");
		//	List<DataDict> listDataDict = dataDictManager.getFirstChilsByParentId(dataDicts.getId());
		//	if(listDataDict!=null && listDataDict.size()==0){
		//		dataDicts.setIsParent("false");
		//	}
		//	list.add(dataDicts);
		//}
		//List<DataDict> rtnList=BeanUtils.listToTree(list);
		//return rtnList;
	}
	
	@RequestMapping(value="getMoibleComBoByTypeKey", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "通过typeKey获取数据字典", httpMethod = "GET", notes = "通过typeKey获取数据字典")
	public Map<String,Object> getMoibleComBoByTypeKey(@ApiParam(name="typeKey",value="分类key",required = true)@RequestParam String typeKey) throws Exception
	{
		if(StringUtil.isEmpty(typeKey)) return null;
		SysType dictType = sysTypeManager.getByTypeKeyAndGroupKey(CategoryConstants.CAT_DIC.key(), typeKey);
		List<DataDict> list= getDataDict(dictType,false);
		List<DataDict> rtnList=BeanUtils.listToTree(list);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dataDictList", rtnList);
		map.put("dictType", dictType);
		return map;
	}
	
	
	
	/**
	 * 根据数据字典分类获取数据字典值
	 * @param dictType
	 * @param tileNeedRoot 平铺结构的数据字典是否需要返回根节点（在管理数据字典值得时候需要返回，在使用数据字典的时候不需要返回）
	 * @return
	 */
	private List<DataDict> getDataDict(SysType dictType,Boolean tileNeedRoot){
		if(BeanUtils.isEmpty(dictType))return null;
		String typeId = dictType.getId();
		List<DataDict> dataDictList=dataDictManager.getByTypeId(typeId);
		List<DataDict> list = new ArrayList<>();
		for(DataDict entity : dataDictList){
			DataDict dataDicts = new DataDict();
			dataDicts = entity;
			dataDicts.setOpen("true");
			List<DataDict> listDataDict = dataDictManager.getFirstChilsByParentId(dataDicts.getId());
			if(listDataDict!=null && listDataDict.size()==0){
				dataDicts.setIsParent("false");
			}
			list.add(dataDicts);
		}
		//树形结构，将根节点添加到数据项中
		if(tileNeedRoot){
			//根节点
			DataDict dict = new DataDict();
			dict.setId(dictType.getId());
			dict.setParentId("-1");
			dict.setName(dictType.getName());
			dict.setTypeId(typeId);
			dict.setKey("");
			dict.setOpen("true");
			list.add(dict);
		}
		return list;
	}
	
	@RequestMapping(value="dataDictGet", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "数据字典明细页面", httpMethod = "POST", notes = "数据字典明细页面")
	public DataDict get(@ApiParam(name="id",value="主键",required = true)@RequestBody String id) throws Exception{
		DataDict dataDict=new DataDict();
		if(StringUtil.isNotEmpty(id)){
			dataDict=dataDictManager.get(id);
		}
		return dataDict;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存数据字典信息", httpMethod = "POST", notes = "保存数据字典信息")
	public CommonResult<String> save(@ApiParam(name="dataDict", value="字典", required = true)@RequestBody DataDict dataDict) throws Exception{
		String id=dataDict.getId();
		try {
			if(StringUtil.isEmpty(id)){
				// 验证字典key 是否已经存在
				DataDict dict= dataDictManager.getByDictKey(dataDict.getTypeId(),dataDict.getKey());
				if(dict != null){
					return new CommonResult<String>(false, "该字典项值已经存在");
				}
				// 如果是root节点添加。typeId = parentId 
				dataDict.setId(idGenerator.getSuid());
				dataDictManager.create(dataDict);
			}else{
				 // 如果改变了key ，验证改字典key 中是否已经存在
				if(!dataDictManager.get(id).getKey().equals(dataDict.getKey())){
					DataDict dict= dataDictManager.getByDictKey(dataDict.getTypeId(),dataDict.getKey());
					if(dict != null){
						return new CommonResult<String>(false, "该字典项值已经存在");
					}
				}
				dataDictManager.update(dataDict);
			}
			return new CommonResult<String>(true, "保存成功");
		} catch (Exception e) {
			return new CommonResult<String>(true, "保存失败");
		}
	}
	
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除数据字典", httpMethod = "DELETE", notes = "删除数据字典")
	public CommonResult<String> remove(@ApiParam(name="id", value="字典", required = true)@RequestParam String id) throws Exception{
		try {
			String[] aryIds=StringUtil.getStringAryByStr(id);
			dataDictManager.removeByIds(aryIds);
			return new CommonResult<String>(true, "删除成功");
		} catch (Exception e) {
			return new CommonResult<String>(true, "删除失败");
		}
	}
	
	
	
	
	@RequestMapping(value="getDataDictByTypeId", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据分类数据字典", httpMethod = "GET")
	public  List<DataDict> getDataDictByType(@ApiParam(name="typeId", value="字典", required = true)@RequestBody String typeId) throws Exception{
		List<DataDict> dataDictList=dataDictManager.getByTypeId(typeId);
		List<DataDict> rtnList=BeanUtils.listToTree(dataDictList);
		return rtnList;
	}
	
	
	@RequestMapping(value="sortList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "排序列表页面", httpMethod = "POST")
	public List<DataDict> sortList(@ApiParam(name="id", value="字典", required = false,defaultValue="-1")@RequestBody String id) throws Exception {
		List<DataDict> dataDictList = dataDictManager.getFirstChilsByParentId(id);
		return dataDictList;
	}

	@RequestMapping(value="sort", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "排序", httpMethod = "POST")
	public CommonResult<String> sort(@ApiParam(name="dicIds", value="字典", required = true)@RequestBody String[] dicIds) throws Exception {
		try {
			if (BeanUtils.isNotEmpty(dicIds)) {
				for (int i = 0; i < dicIds.length; i++) {
					String dicId = dicIds[i];
					int sn = i + 1;
					dataDictManager.updSn(dicId, sn);
				}
			}
		} catch (Exception e) {
			return new CommonResult<String>(true, "排序失败");
		}
		return new CommonResult<String>(true, "排序成功");
	}
	
	@RequestMapping(value="removeByTypeId", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据分类id删除分类下的字典", httpMethod = "GET")
	public CommonResult<String> removeByTypeIds(@ApiParam(name="typeIds", value="分类id", required = true)@RequestParam String typeIds) throws Exception {
		return dataDictManager.removeByTypeIds(typeIds);
	}
	
	@RequestMapping(value="import", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "数据字典导入", httpMethod = "POST", notes = "数据字典导入")
	public CommonResult<String> importData(@ApiParam(name = "files", value = "上传的文件流") @RequestBody List<MultipartFile> file, @ApiParam(name="typeId", value="数据字典分类id", required = true)@RequestParam String typeId) throws Exception{
		dataDictManager.importData(file, typeId);
		return new CommonResult<String>(true, "导入数据字典成功");
	}
}
