package com.hotent.base.feign;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.conf.FeignConfig;
import com.hotent.base.feign.impl.FormFeignServiceImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;

/**
 * 
 * @author liyg
 *
 */
@FeignClient(name="form-eureka",fallback=FormFeignServiceImpl.class, configuration=FeignConfig.class)
public interface FormFeignService {
	
	
	/**
	 * 调用form模块的restful接口。根据业务对象别名或id获取主BoEnt
	 * 
	 * @param alias
	 * @param defId
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @returnBpmForm boDefService.getByName
	 * 返回 主BoEnt对象
	 */
	@RequestMapping(value="/form/formServiceController/v1/getMainBOEntByDefAliasOrId",method=RequestMethod.GET)
	public ObjectNode getMainBOEntByDefAliasOrId(@RequestParam(value="alias", required=true)String alias,@RequestParam(value="defId", required=true)String defId) ;


	/**
	 * 根据别名获取bo数据的保存方式
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/bo/def/v1/getSupportDb",method=RequestMethod.GET)
	public boolean getSupportDb(@RequestParam(value="alias", required=true)String alias) ;


	/**
     * 调用form模块接口处理bo数据
     * @param id  boid。空为新增。不为空则更新  
     * @param defId bo定义id
     * @param boData 业务数据
     * @param saveType 保存类型。1，database 。2，boObject
     * @return
     * 需先根据 saveType用boInstanceFactory.getBySaveType(saveType);获取对应的handler。然后调用handler的handSaveData。返回List<BoResult>
     * @ 
     */
	@RequestMapping(value="/form/formServiceController/v1/handlerBoData",method=RequestMethod.POST)
	public List<ObjectNode> handlerBoData(@RequestBody(required=true)ObjectNode param) ;
	
	/** 
	 *  对应bodatahandler.getByBoDefCode
	 * @param saveMode 保存方式
	 * @param code  bocode
	 * @return 返回值不变
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getBodataByDefCode",method=RequestMethod.GET)
	public ObjectNode getBodataByDefCode(@RequestParam(value="saveMode", required=true)String saveMode,@RequestParam(value="code", required=true) String code) ;
	
	
	/**
	 *  对应handler.getById
	 *  根据实例ID和bo定义code获取BODATA，只返回两层。
	 * 1.根据bodefCode获取bo定义。
	 * 2.根据bo定义获取数据。
	 * @param saveMode
	 * @param id
	 * @param code
	 * @return 返回值不变
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getBodataById",method=RequestMethod.POST)
	public ObjectNode getBodataById(@RequestBody(required=true)ObjectNode param) ;
	
	
	/**
	 * 调用form模块的restful接口。根据formkey获取表单
	 * 
	 * @param formKey
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @returnBpmForm  返回form对象
	 */
	@RequestMapping(value="/form/formServiceController/v1/getByFormKey",method=RequestMethod.GET)
	public ObjectNode getByFormKey(@RequestParam(value="formKey", required=true)String formKey) ;
	
	/**
	 *   根据formKey 导出表单
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getFormExportXml",method=RequestMethod.POST)
	public String getFormExportXml(@RequestBody(required=true)String string) throws IOException;
	
	/**
	 *   根据bodef获得导出用的xml文件
	 * @param formKeys
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getBoDefExportXml",method=RequestMethod.POST)
	public String getBoDefExportXml(@RequestBody(required=true)ObjectNode bodef) ;
	
	/**
	 *   根据FormRight获得导出用的xml文件
	 * @param formKeys
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getFormRightExportXml",method=RequestMethod.POST)
	public String getFormRightExportXml(@RequestBody(required=true)ObjectNode bodef) ;
	
	/**
	 *   导入 bo
	 * @param formKeys
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/importBo",method=RequestMethod.GET)
	public ObjectNode importBo(@RequestParam(value="bodefXml", required=true)String bodefXml) ;
	
	/**
	 *   导入bodef对象
	 * @param bos
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/importBoDef",method=RequestMethod.POST)
	public ObjectNode importBoDef(@RequestBody(required=true)List<ObjectNode> bos);
	
	/**
	 *   导入form
	 * @param formfXml
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/importForm",method=RequestMethod.POST)
	public ObjectNode importForm(@RequestBody(required=true)String formfXml) ;
	
	/**
	 *   导入formRigths
	 * @param formfXml
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/importFormRights",method=RequestMethod.POST)
	public ObjectNode importFormRights(@RequestBody(required=true)String formRightsXml) ;
	
	/**
	 * 根据表单ID取得表单对象。
	 * @param formId
	 * @return ObjectNode FormService.getByFormId
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getByFormId",method=RequestMethod.GET)
	public ObjectNode getByFormId(@RequestParam(value="formId", required=true)String formId) ;
	
	/**
	 * 获取流程实例表单的权限。
	 * <pre>
	 * {
	 * 	field：{"NAME": "w", "SEX": "r"}
	 * 	table：{"TABLE1": "r", "TABLE2": "w"}
	 * 	opinion：{"领导意见": "w", "部门意见": "r"}
	 * }
	 * </pre>
	 * @param formKey	表单KEY 对应BPM_FROM key字段。
	 * @param userId
	 * @param flowKey
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @ 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getInstPermission",method=RequestMethod.POST)
	public String getInstPermission(@RequestBody(required=true)ObjectNode param);
	
	/**
	 * 获取流程启动时的表单权限
	 * @param formKey
	 * @param flowKey
	 * @param nodeId 节点id
	 * @param nextNodeId 下一个节点id
	 * @return
	 * @ 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getStartPermission",method=RequestMethod.POST)
	public String getStartPermission(@RequestBody(required=true)ObjectNode param) ;
	
	/**
	 * 获取表单权限
	 * <pre>
	 * {
	 * 	field：{"NAME": "w", "SEX": "r"}
	 * 	table：{"TABLE1": "r", "TABLE2": "w"}
	 * 	opinion：{"领导意见": "w", "部门意见": "r"}
	 * }
	 * </pre>
	 * @param formKey 表单KEY 对应BPM_FROM key字段。
	 * @param userId 用户ID
	 * @param flowKey 流程KEY
	 * @param nodeId 节点ID
	 * @return
	 * @ 
	 */
	@RequestMapping(value="/form/formServiceController/v1/getPermission",method=RequestMethod.POST)
	public String getPermission(@RequestBody(required=true)ObjectNode param) ;
	
	/**
	 * 根据表单key获得权限列表。
	 * @param formId
	 * @return ObjectNode FormService.getByFormId
	 */
	@RequestMapping(value="/form/formServiceController/v1/getFormRigthListByFlowKey",method=RequestMethod.GET)
	public List<ObjectNode> getFormRigthListByFlowKey(@RequestParam(value="formId", required=true)String formId) ;
	
	/**
	 * 删除表单权限
	 * 包括bpmFormRightManager.removeInst(flowKey);bpmFormRightManager.remove(flowKey, parentFlowKey);2个方法
	 * @param flowKey
	 * @param parentFlowKey
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/removeFormRights",method=RequestMethod.GET)
	public void removeFormRights(@RequestParam(value="flowKey", required=true)String flowKey,@RequestParam(value="parentFlowKey", required=true)String  parentFlowKey) ;
	
	/**
	 * 通过别名获取bo定义  bODefManager.getByAlias(boDef.getKey());
	 * <pre>
	 * 获取bo定义并构建关联数据
	 * </pre>
	 * 
	 * @param alias	别名
	 * @return		bo定义
	 */
	@RequestMapping(value="/form/formServiceController/v1/getBodefByAlias",method=RequestMethod.GET)
	public ObjectNode getBodefByAlias(@RequestParam(value="alias", required=true)String alias) ;
	
	/**
	 * 通过bo定义id获取bo的json格式定义 bODefManager.getBOJson(def.get("id"));
	 * @param id	bo定义id
	 * @return		json格式定义数据
	 */
	@RequestMapping(value="/form/formServiceController/v1/getBoJosn",method=RequestMethod.GET)
	public ObjectNode getBoJosn(@RequestParam(value="id", required=true)String id) ;
	
	/**
	 * 通过bo实例name获取boent  boDefService.getEntByName
	 * @param id	bo实体name
	 * @return		json格式定义数据
	 */
	@RequestMapping(value="/form/formServiceController/v1/getBoEntByName",method=RequestMethod.GET)
	public ObjectNode getBoEntByName(@RequestParam(value="name", required=true)String name) ;
	
	/**
	 *删除表单权限
	 * @param flowKey  流程定义key
	 * @param parentFlowKey 父流程定义key 
	 * @param permissionType 权限类型
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/removeFormRightByFlowKey",method=RequestMethod.POST)
	public void removeFormRightByFlowKey (@RequestBody(required=true)ObjectNode param) ;
	
	/**
	 * 新增表单权限
	 * @param bpmFormRight
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/createFormRight",method=RequestMethod.POST)
	public void createFormRight (@RequestBody(required=true)ObjectNode bpmFormRight) ;
	
	/**
	 * 查询表单权限
	 * @param queryFilter
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/queryFormRight",method=RequestMethod.POST)
	public List<ObjectNode> queryFormRight (@RequestBody(required=true)QueryFilter queryFilter) ;
	
	/**
	 * 根据表单key获取主版本的表单对象数据。  bpmFormManager.getMainByFormKey
	 * @param formKey	表单key
	 * @return		BpmForm的json格式数据
	 */
	
	@RequestMapping(value="/form/formServiceController/v1/getFormBoLists",method=RequestMethod.GET)
	public List<ObjectNode> getFormBoLists(@RequestParam(value="formKey", required=true)String formKey) ;
	/**
	 * 表单相关导出接口
	 * @param obj
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/getFormAndBoExportXml",method=RequestMethod.POST)
	public Map<String,String> getFormAndBoExportXml(@RequestBody(required=true)ObjectNode obj);
	
	/**
	 * 导入表单
	 * @param obj
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/importFormAndBo",method=RequestMethod.POST)
	public CommonResult<String> importFormAndBo(ObjectNode obj);
	/**
	 * 根据业务数据关联对象清除流程相关数据
	 * @param links
	 * @return
	 */
	@RequestMapping(value="/form/formServiceController/v1/removeDataByBusLink",method=RequestMethod.POST)
	public void removeDataByBusLink(@RequestBody(required=true)JsonNode links) ;
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/form/customQuery/v1/getQueryPage", method = RequestMethod.GET)
	public PageList getQueryPage(@RequestParam(value="alias",required=true) String alias);
	
	@RequestMapping(value = "/form/customDialog/v1/getAll", method = RequestMethod.POST)
	public List getCustomDialogs();

	
	@RequestMapping(value="/form/fieldAuth/v1/getByClassName",method=RequestMethod.GET)
	public ObjectNode getByClassName(@RequestParam(value="className", required=true)String className) ;
	
	@RequestMapping(value="/form/customQuery/v1/doQuery",method=RequestMethod.POST)
	public PageList doQuery(@RequestParam(value="alias", required=false)String alias,@RequestParam(value="page", required=false)Integer page,@RequestBody(required=true)String queryData) ;

	@RequestMapping(value="/form/form/v1/getFormData",method=RequestMethod.GET)
	public Map<String,Object> getFormData(@RequestParam(value="pcAlias", required=false)String pcAlias,@RequestParam(value="mobileAlias", required=false)String mobileAlias) ;

}   
