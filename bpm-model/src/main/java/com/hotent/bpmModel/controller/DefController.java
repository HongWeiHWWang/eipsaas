package com.hotent.bpmModel.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bpm.api.constant.DesignerType;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.inst.ISkipCondition;
import com.hotent.bpm.api.model.process.def.BpmBoDef;
import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;
import com.hotent.bpm.api.model.process.def.BpmDefSetting;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmFormInit;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDefComparator;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.plugin.core.context.PluginParse;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmTaskService;
import com.hotent.bpm.api.service.DiagramService;
import com.hotent.bpm.engine.def.BpmDefUtil;
import com.hotent.bpm.engine.def.impl.handler.BoBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.BpmDefSettingBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.BpmFormInitBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.ButtonsBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.PropertiesBpmDefXmlHandler;
import com.hotent.bpm.engine.task.skip.SkipConditionUtil;
import com.hotent.bpm.model.def.BpmDefXml;
import com.hotent.bpm.model.def.BpmDefXmlList;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProBoManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType;
import com.hotent.bpm.persistence.model.BpmProBo;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.plugin.task.userassign.context.UserAssignPluginContext;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.bpmModel.manager.BpmDefTransform;
import com.hotent.bpmModel.params.DefBoSetVo;
import com.hotent.bpmModel.params.DefBtnsSaveVo;
import com.hotent.bpmModel.params.DefPropSaveVo;
import com.hotent.bpmModel.params.DefaultBpmDefinitionVo;
import com.hotent.uc.api.impl.util.ContextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 描述：流程定义管理
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/def/v1/")
@Api(tags="流程定义")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class DefController extends BaseController<BpmDefinitionManager, DefaultBpmDefinition> {
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	DiagramService diagramService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	PropertiesBpmDefXmlHandler propertiesBpmDefXmlHandler;
	@Resource
	PortalFeignService PortalFeignService;
	@Resource
	BpmDefAuthorizeManager bpmDefAuthorizeManager;
	@Resource
	BpmDefTransform bpmDefTransform;
	@Resource
	BoBpmDefXmlHandler boBpmDefXmlHandler;
	@Resource
	BpmFormInitBpmDefXmlHandler bpmFormInitBpmDefXmlHandler;
	@Resource
	BpmProBoManager bpmProBoManager;
	@Resource
	ButtonsBpmDefXmlHandler buttonsBpmDefXmlHandler;
	@Resource
	BpmTaskService bpmTaskService;
	@SuppressWarnings("unused")
	private final static String ROOT_PATH = "attachFiles" + File.separator + "tempZip"; // 导入和导出的文件操作根目录
	
	/**
	 * 返回流程设计生成的BPMNxml
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="bpmnXml",method=RequestMethod.GET, produces={"application/xml; charset=UTF-8"})
	@ApiOperation(value = "返回流程设计生成的BPMNxml", httpMethod = "GET", notes = "返回流程设计生成的BPMNxml")
	public Object bpmnXml(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception {
		if (StringUtils.isEmpty(defId)) {
			return "no def input";
		}

		DefaultBpmDefinition po = bpmDefinitionManager.getById(defId);
		String bpmnXml = po.getBpmnXml();
		return bpmnXml;
	}

	/**
	 * 返回流程设计的xml
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="designXml",method=RequestMethod.GET, produces={"application/xml; charset=UTF-8"})
	@ApiOperation(value = "返回流程设计的xml", httpMethod = "GET", notes = "返回流程设计的xml")
	public Object designXml(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception {
		if (StringUtils.isEmpty(defId)) {
			return "no def input";
		}

		DefaultBpmDefinition po = bpmDefinitionManager.getById(defId);
		String bpmnXml = po.getDefXml();
		return bpmnXml;
	}

	@RequestMapping(value="getJson",method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程定义Key获取流程定义对象", httpMethod = "GET", notes = "根据流程定义Key获取流程定义对象")
	public DefaultBpmDefinition getJson(@ApiParam(name="defKey",value="流程定义Key", required = true) @RequestParam String defKey){
		DefaultBpmDefinition po = bpmDefinitionManager.getMainByDefKey(defKey);
		po.setBpmnXml("");
		return  po;
	}



	/**
	 * 通过h5来设计一个流程
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@RequestMapping(value="webDefDesign",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "返回流程设计的xml", httpMethod = "GET", notes = "返回流程设计的xml")
	public Object webDefDesign(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws ClientProtocolException, IOException{
		Map<String,Object> map = new HashMap<String,Object>();
		DefaultBpmDefinition def = bpmDefinitionManager.getById(defId);
		if (BeanUtils.isNotEmpty(def)) {
			map.put("defId", def.getDefId());
			map.put("name", def.getName());
			map.put("desc", def.getDesc());
			map.put("defKey", def.getDefKey());
			map.put("modelId", def.getDefId());
			map.put("model", def.getDefJson());
			map.put("reason", def.getReason());
			map.put("version", def.getVersion());
			map.put("showUrgentState", def.getShowUrgentState());
			map.put("showModifyRecord", def.getShowModifyRecord());
            map.put("isReadRevoke", def.getIsReadRevoke());
            map.put("urgentMailTel", def.getUrgentMailTel());
            map.put("urgentSmsTel", def.getUrgentSmsTel());
			ObjectNode type=null;
			if(BeanUtils.isNotEmpty(def.getTypeId())) type =  PortalFeignService.getSysTypeById(def.getTypeId());
			if(BeanUtils.isNotEmpty(type)){
				map.put("typeId", def.getTypeId());
				map.put("typeName", type.get("name").asText());
			}	
		} 
		return map;
	}
	
	/**
	 * web流程设计器保存
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="webDefSave",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "web流程设计器保存", httpMethod = "POST", notes = "web流程设计器保存")
    public CommonResult<String> webDefSave(
            @ApiParam(name="bpmDefinitionVo",value="保存流程对象", required = true) @RequestBody DefaultBpmDefinitionVo bpmDefinitionVo) throws Exception{

        Boolean isDeploy = bpmDefinitionVo.getIsdeploy();
        DefaultBpmDefinition bpmDefinition= getWebDesignFromRequest(bpmDefinitionVo);
        String resultMsg = "";
        try {
            List<DefaultBpmDefinition> oldDefs = bpmDefinitionManager.queryByDefKey(bpmDefinition.getDefKey());
            if (StringUtils.isEmpty(bpmDefinition.getDefId())) {
                if (BeanUtils.isNotEmpty(oldDefs)) {
                    resultMsg = "流程定义KEY“" + bpmDefinition.getDefKey() + "” 已经存在于：" + oldDefs.get(0).getName();
                    return new CommonResult<String>(false,resultMsg,"");
                }
            }
            if (isDeploy) {
                bpmDefinition.setUpdateTime(LocalDateTime.now());
                if(!bpmDefinitionService.deploy(bpmDefinition)){
                    return new CommonResult<String>(false,"流程发布失败","");
                }
            } else {
                if (StringUtils.isNotEmpty(bpmDefinition.getDefId())) {
                    bpmDefinitionService.updateBpmDefinition(bpmDefinition);
                } else {
                    bpmDefinitionService.saveDraft(bpmDefinition);
                }
            }
            //oldDefs有数据，则表明为发布新版或者修改。更新之前版本的分类id保持和新版一致。
            if (BeanUtils.isNotEmpty(oldDefs) && oldDefs.size()>0) {
                String oldTypeId = oldDefs.get(0).getTypeId();
                //如果分类id变了。1,更新之前版本的流程的分类id.2,更新流程实例和审批历史的分类id
                if (!oldTypeId.equals(bpmDefinition.getTypeId())) {
                    bpmDefinitionManager.updateTypeIdByDefKey(bpmDefinition.getDefKey(), bpmDefinition.getTypeId());
                }
            }
            return new CommonResult<String>(true,"流程发布成功",bpmDefinition.getDefId());
        } catch (Exception ex) {
            ex.printStackTrace();
            String rootCauseMessage = ExceptionUtils.getRootCauseMessage(ex);
            return new CommonResult<String>(false,"流程发布失败："+rootCauseMessage,"");
        }
    }
	
	
	/**
	 * flex，保存发布流程信息。
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value="flexDefSave",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "flex流程设计器保存", httpMethod = "POST", notes = "flex流程设计器保存")
	public CommonResult<String> flexDefSave(
			@ApiParam(name="bpmDefinitionVo",value="保存流程对象", required = true) @RequestBody DefaultBpmDefinitionVo bpmDefinitionVo) throws Exception {
		Boolean isDeploy = bpmDefinitionVo.getIsdeploy();
		DefaultBpmDefinition bpmDefinition = this.getFromRequest(bpmDefinitionVo);
		String resultMsg = "";
		try {
			if (StringUtils.isEmpty(bpmDefinition.getDefId())) {
				List<DefaultBpmDefinition> oldDefs = bpmDefinitionManager.queryByDefKey(bpmDefinition.getDefKey());
				if (BeanUtils.isNotEmpty(oldDefs)) {
					resultMsg = "流程定义KEY“" + bpmDefinition.getDefKey() + "” 已经存在于：" + oldDefs.get(0).getName();
					return new CommonResult<String>(false,resultMsg,"");
				}
			}
			if (isDeploy) {
				//bpmDefinition.setUpdateTime(LocalDateTime.now());
				bpmDefinitionService.deploy(bpmDefinition);
			} else {
				if (StringUtils.isNotEmpty(bpmDefinition.getDefId())) {
					bpmDefinitionService.updateBpmDefinition(bpmDefinition);
				} else {
					bpmDefinitionService.saveDraft(bpmDefinition);
				}
			}
			return new CommonResult<String>(true,"流程发布成功","");
		} catch (Exception ex) {
			String rootCauseMessage = ExceptionUtils.getRootCauseMessage(ex);
			return new CommonResult<String>(false,"流程发布失败："+rootCauseMessage,"");
		}
	}

	/**
	 * 流程定义列表(分页条件查询)数据
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 *             PageJson
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程定义列表(分页条件查询)数据", httpMethod = "POST", notes = "流程定义列表(分页条件查询)数据")
	public PageList<DefaultBpmDefinition> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<DefaultBpmDefinition> queryFilter) throws Exception {
		queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL,FieldRelation.AND,"isMain");
		// 查询列表
		PageList<DefaultBpmDefinition> bpmDefinitionList = bpmDefinitionManager.queryList(queryFilter);
		return bpmDefinitionList;
	}
	
	/**
	 * 绑定指定表单的流程定义列表(分页条件查询)数据
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 *             PageJson
	 */
	@RequestMapping(value="formDeflist", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "绑定指定表单的流程定义列表(分页条件查询)数据", httpMethod = "POST", notes = "流程定义列表(分页条件查询)数据")
	public PageList<DefaultBpmDefinition> formDeflist(@ApiParam(name="boCode",value="boCode",required=true)@RequestParam String boCode,
													  @ApiParam(name="formKey",value="formKey",required=true)@RequestParam String formKey,
													  @ApiParam(name="queryFilter",value="通用查询对象") @RequestBody QueryFilter<DefaultBpmDefinition> queryFilter) throws Exception {
		List<BpmProBo> proBos = bpmProBoManager.getByBoCode(boCode);
		if(BeanUtils.isNotEmpty(proBos)){
			Set<String> defIds = new HashSet<String>();
			for (BpmProBo proBo : proBos) {
				defIds.add(proBo.getProcessId());
			}
			queryFilter.addFilter("defId", defIds.toArray(new String[defIds.size()]), QueryOP.IN);
			queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL,FieldRelation.AND,"isMain");
			// 查询列表
			PageList<DefaultBpmDefinition> bpmDefinitionList = bpmDefinitionManager.queryList(queryFilter);
			for(String str : defIds) {
				DefaultBpmDefinition result = bpmDefinitionManager.getById(str);
				if(BeanUtils.isEmpty(result)){
					continue;
				}
				BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(str);
				if(BeanUtils.isNotEmpty(bpmProcessDef)){
					DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
					FormExt globalForm = defExt.getGlobalForm();
					FormExt globalMobileForm = defExt.getGlobalMobileForm();
					boolean form = globalForm != null ? !formKey.equals(globalForm.getFormValue()):true;
					boolean mobile = globalMobileForm != null ? !formKey.equals(globalMobileForm.getFormValue()):true;
					int i = 0;
					List<Integer> list = null;
					for(DefaultBpmDefinition def : bpmDefinitionList.getRows()){
						if(def.getDefKey().equals(bpmProcessDef.getDefKey())) {
							if (form && mobile) {
								list = new ArrayList<>();
								list.add(i);
							}
						}
						i++;
					}
					if(list != null){
						for(int j : list){
							bpmDefinitionList.getRows().remove(j);
							bpmDefinitionList.setTotal(bpmDefinitionList.getTotal()-1);
						}
					}
				}
			}
			return bpmDefinitionList;
		}else{
			return new PageList<DefaultBpmDefinition>(new Page<DefaultBpmDefinition>(1,queryFilter.getPageBean().getPageSize()));
		}
	}
	
	/**
	 * 流程定义分类id查询数据
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 *             PageJson
	 */
	@RequestMapping(value="getByTypeId", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程定义分类id查询数据", httpMethod = "GET", notes = "流程定义分类id查询数据")
	public PageList<DefaultBpmDefinition> getByTypeId(@ApiParam(name="typeId",value="分类id")@RequestParam String typeId) throws Exception {
		QueryFilter<DefaultBpmDefinition> queryFilter =QueryFilter.build();
		queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
		queryFilter.addFilter("type_id_", typeId, QueryOP.EQUAL);
		return bpmDefinitionManager.queryList(queryFilter);
	}

	/**
	 * 根据流程定义id获取流程信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value="defGet",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程定义id获取流程信息", httpMethod = "GET", notes = "根据流程定义id获取流程信息")
	public DefaultBpmDefinition defGet(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId)  throws Exception {
		DefaultBpmDefinition bpmDefinition = null;

		if (StringUtil.isNotEmpty(defId)) {
			bpmDefinition = bpmDefinitionManager.getById(defId);
		}
		return bpmDefinition;
	}

	private InputStream getDiagramByInstance(DiagramService diagramService,String bpmnInstId) {
		BpmInstService bpmInstService = (BpmInstService) AppUtil.getBean(BpmInstService.class);
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstanceByBpmnInstId(bpmnInstId);
		BpmProStatusManager  bpmProStatusManager=AppUtil.getBean(BpmProStatusManager.class);
		Map<String, String> colorMap = bpmProStatusManager.getProcessInstanceStatus(bpmProcessInstance.getId());
		return diagramService.getDiagramByDefId(bpmProcessInstance.getProcDefId(), colorMap);
	}

	/**
	 * 流程图
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="image",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程图", httpMethod = "GET", notes = "获取流程图")
	public void image(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="bpmnInstId",value="bpmn实例id", required = false) @RequestParam String bpmnInstId,
			@ApiParam(name="taskId",value="任务idid", required = false) @RequestParam String taskId,
			HttpServletResponse response) throws Exception {
		InputStream is = null;
		if (StringUtils.isNotEmpty(defId)) {
			is = diagramService.getDiagramByBpmnDefId(defId);
		} else if (StringUtils.isNotEmpty(bpmnInstId)) {
			is = getDiagramByInstance(diagramService,  bpmnInstId);
		} else if (StringUtils.isNotEmpty(taskId)) {
			BpmTask bpmTask = bpmTaskService.getByTaskId(taskId);
			is = getDiagramByInstance(diagramService,  bpmTask.getBpmnInstId());
		}
		if (is == null) return;
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		FileUtil.writeInput(is, out);
	}
   
	/**
	 * 保存流程定义。
	 * 
	 * @param request
	 * @param response
	 * @param bpmDefinition
	 * @throws Exception
	 *             void
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存流程定义。", httpMethod = "POST", notes = "保存流程定义。")
	public Object save(
			@ApiParam(name="bpmVo",value="流程保存对象", required = true) @RequestParam DefaultBpmDefinitionVo bpmVo) throws Exception {
		String resultMsg = null;
		String isDeploy = String.valueOf(bpmVo.getIsdeploy());
		String isSave = String.valueOf(bpmVo.getIsSave());
		String defXml = bpmVo.getDefXml();
        DefaultBpmDefinition bpmDefinition=bpmVo.getDefaultBpmDefinition();
		MultipartFile fileLoad = bpmVo.getFile();
		if (fileLoad == null) {// 有文件就优先取文件内容
			bpmDefinition.setDefXml(defXml);
		} else {
			bpmDefinition.setDefXml(FileUtil.inputStream2String(fileLoad.getInputStream()));
		}

		bpmDefinition.setDesigner(DesignerType.ECLIPSE.name());
		try {
			if ("true".equals(isDeploy)) {// deploy
				if (StringUtils.isNotEmpty(bpmDefinition.getDefId())) {
					DefaultBpmDefinition oldBpmDefinition = bpmDefinitionManager.getById(bpmDefinition.getDefId());
					BeanUtils.copyNotNullProperties(oldBpmDefinition, bpmDefinition);
					//oldBpmDefinition.setUpdateTime(LocalDateTime.now());
					bpmDefinitionService.deploy(oldBpmDefinition);
					resultMsg = "成功发布新版本流程定义！";
				} else {
					bpmDefinitionService.deploy(bpmDefinition);
					resultMsg = "成功发布流程定义！";
				}
			} else if ("true".equals(isSave) && StringUtils.isNotEmpty(bpmDefinition.getDefId())) {
				DefaultBpmDefinition oldBpmDefinition = bpmDefinitionManager.getById(bpmDefinition.getDefId());
				BeanUtils.copyNotNullProperties(oldBpmDefinition, bpmDefinition);
				bpmDefinitionService.updateBpmDefinition(oldBpmDefinition);
				resultMsg = "成功更新流程定义！";
			} else {// 保存草稿
				bpmDefinitionService.saveDraft(bpmDefinition);
				resultMsg = "成功保存流程定义草稿！";
			}
			return new CommonResult<String>(true,resultMsg,"");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false," 操作出错：" + e.getMessage(),"");
		}

	}

	/**
	 * 批量删除流程定义
	 * <pre>
	 * 这个方法是通过流程定义ID删除
	 * </pre>
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 */
	@RequestMapping(value="removeByDefIds",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除流程定义", httpMethod = "DELETE", notes = "批量删除流程定义")
	public CommonResult<String> removeByDefIds(
			@ApiParam(name="ids",value="流程定义id字符串", required = true) @RequestParam String ids, 
			@ApiParam(name="cascade",value="是否级联删除该流程所有版本", required = true) @RequestParam Optional<Boolean> cascade,
            @ApiParam(name="isVersion",value="是否是从版本管理删除", required = true) @RequestParam Optional<Boolean> isVersion) throws Exception {
		try{
			String[] aryIds = ids.split(",");
			bpmDefinitionManager.removeDefIds(cascade.orElse(false), isVersion.orElse(false),aryIds);

			return new CommonResult<String>(true,"删除流程定义成功！","");
		} catch (Exception e) {
			String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
			return new CommonResult<String>(false,"删除流程定义失败:" + rootCauseMessage,"");
		}
	}
	
	/**
	 * 保存流程设置的BO设置
	 * 
	 * <pre>
	 * 1.保存BO设定。
	 * 2.保存表单初始化数据。
	 * 
	 * {
	 * 	    "formInitItems": [
	 * 	        {
	 * 	            "nodeId": "userTask1",
	 * 	            "parentDefKey": "qingjia",
	 * 	            "saveFieldsSetting": [
	 * 	                {
	 * 	                    "boDefCode": "code1",
	 * 	                    "fieldDesc": "描述",
	 * 	                    "setting": "return \"1\";"
	 * 	                }
	 * 	            ],
	 * 	            "showFieldsSetting": [
	 * 	                {
	 * 	                    "boDefCode": "code1",
	 * 	                    "fieldDesc": "描述",
	 * 	                    "setting": "return \"1\";"
	 * 	                }
	 * 	            ]
	 * 	        },
	 * 	        {
	 * 	            "nodeId": "userTask1",
	 * 	            "parentDefKey": "local",
	 * 	            "saveFieldsSetting": [
	 * 	                {
	 * 	                    "boDefCode": "code1",
	 * 	                    "fieldDesc": "描述",
	 * 	                    "setting": "return \"1\";"
	 * 	                }
	 * 	            ],
	 * 	            "showFieldsSetting": [
	 * 	                {
	 * 	                    "boDefCode": "code1",
	 * 	                    "fieldDesc": "描述",
	 * 	                    "setting": "return \"1\";"
	 * 	                }
	 * 	            ]
	 * 	        }
	 * 	    ],
	 * 	    bodef:{"boSaveMode":"db","boDefs":[{"required":false,"key":"bbb","name":"a"}]}
	 * 	}
	 * 
	 * </pre>
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 *             ResultMessage
	 */
	@RequestMapping(value="saveSetBos",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存流程设置的BO设置", httpMethod = "POST", notes = "保存流程设置的BO设置")
	public CommonResult<String> saveSetBos(
			@ApiParam(name="defBoSetVo",value="流程bo设置对象", required = true) @RequestBody DefBoSetVo defBoSetVo) throws Exception {
		CommonResult<String> msg = new CommonResult<String>(false, "BO设置内容为空!", "内容为空!");
		
		String userId = ContextUtil.getCurrentUser().getUserId();

		String json = defBoSetVo.getJson();

		String topDefKey = defBoSetVo.getTopDefKey();

		String flowId = defBoSetVo.getFlowId();
		
		Boolean isClearForm = defBoSetVo.getIsClearForm();
		
		BpmDefinition def= bpmDefinitionManager.getById(flowId);

		ObjectNode jsonObj = (ObjectNode)JsonUtil.toJsonNode(json);

		BpmFormInit formInit = new BpmFormInit();

		if (jsonObj.findValue("formInitItems") != null) {
			formInit = JsonUtil.toBean(json, BpmFormInit.class);
			formInit.setParentDefKey(topDefKey);
		}
		// 保存表单初始化数据。
		bpmFormInitBpmDefXmlHandler.saveNodeXml(flowId, "", formInit);

		ObjectNode bodefJson = (ObjectNode) jsonObj.get("bodef");

		BpmBoDef bpmBoDef = new BpmBoDef();

		if (bodefJson != null) {
			bpmBoDef = JsonUtil.toBean(bodefJson, BpmBoDef.class);
			saveBpmProBoList(userId, flowId, def.getDefKey(),topDefKey, bpmBoDef,isClearForm);
		}

		boBpmDefXmlHandler.saveNodeXml(flowId, "", bpmBoDef);

		msg.setState(true);
		msg.setMessage("保存成功！");
		msg.setValue("内容完整！");
		return msg;
	}

	private void saveBpmProBoList(String userId, String flowId, String flowKey,String parentFlowKey, BpmBoDef bpmBoDef, Boolean isClearForm) throws Exception {

		// 清除之前的流程和BO的绑定数据
		boolean mark = true;
		List<BpmProBo> dbProBos = bpmProBoManager.getByProcessId(flowId);//数据库中的bodef和表单数据
		if (StringUtil.isNotEmpty(flowId)) {
			bpmProBoManager.removeByProcessId(flowId);
			mark = false;
		}
		if (mark && StringUtil.isNotEmpty(flowKey)) {
			bpmProBoManager.removeByProcessKey(flowKey);
		}
		
		// 封装数据到绑定表
		List<BpmProBo> bpmProBoList = new ArrayList<BpmProBo>();
		List<ProcBoDef> boDefs = bpmBoDef.getBoDefs();

		for (ProcBoDef procBoDef : boDefs) {
			BpmProBo bpmProBo = new BpmProBo();
			bpmProBo.setProcessId(flowId);
			bpmProBo.setProcessKey(flowKey);
			bpmProBo.setBoCode(procBoDef.getKey());
			bpmProBo.setBoName(procBoDef.getName());
			bpmProBo.setCreatorId(userId);
			bpmProBoList.add(bpmProBo);
		}
		bpmProBoManager.createByBpmProBoList(bpmProBoList);
		
		//数据库的关系表跟新的关系表比较，用list.equals只需要重载equals就行- -
		if(BeanUtils.isNotEmpty(boDefs)&&dbProBos.containsAll(bpmProBoList)){
			return;
		}
		//bo不相等了 需要做下面操作
		if(isClearForm){
			FormFeignService formService=AppUtil.getBean(FormFeignService.class);
			//1 删除表单的所有相关权限
			formService.removeFormRights(flowKey, parentFlowKey);
			//2 删除所有表单配置
			BpmDefSetting bpmDefSetting = new BpmDefSetting();
			BpmDefSettingBpmDefXmlHandler bpmDefSettingBpmDefXmlHandler = AppUtil.getBean(BpmDefSettingBpmDefXmlHandler.class);
			bpmDefSettingBpmDefXmlHandler.saveNodeXml(flowId, null, bpmDefSetting);
		}
	}
	
	/**
	 * 根据从web设计器提交的数据构建流程定义对象。
	 * 
	 * @param request
	 * @return
	 */
	private DefaultBpmDefinition getWebDesignFromRequest(DefaultBpmDefinitionVo vo)throws Exception{

		DefaultBpmDefinition defaultBpmDef=vo.getDefaultBpmDefinition();
		String typeId = defaultBpmDef.getTypeId(); // 流程分类
		String name = defaultBpmDef.getName(); // 流程标题
		String defKey = defaultBpmDef.getDefKey(); // 流程key
		String descp = defaultBpmDef.getDesc(); // description
		String defJson = defaultBpmDef.getDefJson(); // defXml
		Boolean isDeploy = vo.getIsdeploy();
		String reason = defaultBpmDef.getReason();// reason
		String defId = defaultBpmDef.getDefId();
		
		DefaultBpmDefinition bpmDefinition = null;
		if (StringUtils.isNotEmpty(defId)) {
			if (isDeploy) {
				bpmDefinition = bpmDefinitionManager.getById(defId);
			} else {
				bpmDefinition = bpmDefinitionManager.getById(defId);
			}
		}
		if (bpmDefinition == null) {
			bpmDefinition = new DefaultBpmDefinition();
			if (StringUtils.isNotEmpty(defKey)) {
				bpmDefinition.setDefKey(defKey);
			}
		}
		// 设置属性值
		if (StringUtils.isNotEmpty(typeId)) {
			bpmDefinition.setTypeId(typeId);
			bpmDefinition.setTypeName(defaultBpmDef.getTypeName());
		}
		if (StringUtils.isNotEmpty(name)) {
			bpmDefinition.setName(name);
		}
		if (StringUtils.isNotEmpty(descp)) {
			bpmDefinition.setDesc(descp);
		}
		/*else {
			bpmDefinition.setDesc(name);
		}*/
		if(StringUtils.isNotEmpty(defJson)){
			bpmDefinition.setDefJson(defJson);
		}
		bpmDefinition.setDesigner(DesignerType.WEB.name());
		bpmDefinition.setReason(reason);
		bpmDefinition.setCreateBy(ContextUtil.getCurrentUser().getUserId());

		return bpmDefinition;
	}
	
	/**
	 * 根据从flex提交的数据构建流程定义对象。
	 * 
	 * @param request
	 * @return
	 */
	private DefaultBpmDefinition getFromRequest(DefaultBpmDefinitionVo vo) throws Exception {
		DefaultBpmDefinition defaultBpmDef=vo.getDefaultBpmDefinition();
		String typeId = defaultBpmDef.getTypeId(); // 流程分类
		String subject = defaultBpmDef.getName(); // 流程标题
		String defKey = defaultBpmDef.getDefKey(); // 流程key
		String descp = defaultBpmDef.getDesc(); // description
		String defXml = defaultBpmDef.getDefXml(); // defXml
		String defJson = defaultBpmDef.getDefJson(); // defXml
		Boolean isDeploy = vo.getIsdeploy();
		defXml = defXml.replace("''", "'");

		String reason = defaultBpmDef.getReason();// reason
		String defId = defaultBpmDef.getDefId();

		DefaultBpmDefinition bpmDefinition = null;
		if (StringUtils.isNotEmpty(defId)) {
			if (isDeploy) {
				bpmDefinition = bpmDefinitionManager.getById(defId);
			} else {
				bpmDefinition = bpmDefinitionManager.getById(defId);
			}
		}
		if (bpmDefinition == null) {
			bpmDefinition = new DefaultBpmDefinition();
			if (StringUtils.isNotEmpty(defKey)) {
				bpmDefinition.setDefKey(defKey);
			}
		}
		// 设置属性值
		if (StringUtils.isNotEmpty(typeId)) {
			bpmDefinition.setTypeId(typeId);
		}
		if (StringUtils.isNotEmpty(subject)) {
			bpmDefinition.setName(subject);
		}
		if (StringUtils.isNotEmpty(descp)) {
			bpmDefinition.setDesc(descp);
		} else {
			bpmDefinition.setDesc(subject);
		}
		if (StringUtils.isNotEmpty(defXml)) {
			bpmDefinition.setDefXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + defXml);
			bpmDefinition.setDesigner(DesignerType.FLASH.name());
		}
		if(StringUtils.isNotEmpty(defJson)){
			bpmDefinition.setDefJson(defJson);
			bpmDefinition.setDesigner(DesignerType.WEB.name());
		}

		bpmDefinition.setReason(reason);

		return bpmDefinition;
	}

	/**
	 * 在流程在线设计中获取分类的所有流程列表。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getFlowListByTypeId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存代理信息", httpMethod = "GET", notes = "保存代理信息")
	public void getFlowListByTypeId(
			@ApiParam(name="typeId",value="分类id", required = true) @RequestParam String typeId,
			@ApiParam(name="word",value="查询关键字", required = true) @RequestParam String word,
			HttpServletResponse response) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtil.isNotEmpty(typeId)) {
			params.put("typeId", typeId);
		}

		if (StringUtil.isNotEmpty(word)) {
			word = "%" + word + "%";
			params.put(typeId, typeId);
		}

		List<DefaultBpmDefinition> list = bpmDefinitionManager.queryListByMap(params);

		StringBuffer msg = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
		for (DefaultBpmDefinition bpmDefinition : list) {
			msg.append("<item name=\"" + bpmDefinition.getName() + "\" key=\"" + bpmDefinition.getDefKey() + "\" type=\"" + bpmDefinition.getTypeId() + "\"></item>");
		}
		msg.append("</Result>");
		PrintWriter out = response.getWriter();
		out.println(msg.toString());
	}

	/**
	 * 流程在线设计，根据defId获取流程对应的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="flexGet",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程在线设计，根据defId获取流程对应的详细信息", httpMethod = "GET", notes = "流程在线设计，根据defId获取流程对应的详细信息")
	public void flexGet(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId, HttpServletResponse response) throws Exception {
		DefaultBpmDefinition bpmDefinition = null;
		if (StringUtil.isNotEmpty(defId)) {
			bpmDefinition = bpmDefinitionManager.getById(defId);
		} else {
			bpmDefinition = new DefaultBpmDefinition();
		}
		StringBuffer msg = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
		msg.append("<defId>" + bpmDefinition.getDefId() + "</defId>");
		msg.append("<defXml>" + bpmDefinition.getDefXml() + "</defXml>");

		if (bpmDefinition.getTypeId() != null) {
			ObjectNode proType = PortalFeignService.getSysTypeById(bpmDefinition.getTypeId());
			msg.append("<typeName>" + proType.get("name").asText() + "</typeName>");
			msg.append("<typeId>" + proType.get("id").asText() + "</typeId>");
		}

		msg.append("<subject>" + bpmDefinition.getName() + "</subject>");
		msg.append("<defKey>" + bpmDefinition.getDefKey() + "</defKey>");
		msg.append("<descp>" + bpmDefinition.getDesc() + "</descp>");
		msg.append("<versionNo>" + bpmDefinition.getVersion() + "</versionNo>");
		msg.append("</Result>");
		PrintWriter out = response.getWriter();
		out.println(msg.toString());
	}

	/**
	 * 获取流程其他属性的参数。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             BpmDefExtProperties
	 */
	@RequestMapping(value="getOtherParam",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程其他属性的参数", httpMethod = "GET", notes = "获取流程其他属性的参数")
	public Map<String, Object> getOtherParam(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception {

		BpmDefinition bpmDefinition = (BpmDefinition) bpmDefinitionManager.getById(defId);

		BpmProcessDef<BpmProcessDefExt> procDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		Map<String, Object>  resMap=new HashMap<>();  
		BpmDefExtProperties prop = procDef.getProcessDefExt().getExtProperties();
        Map<String, String> handlerTypes = MessageUtil.getHandlerTypes();
		
		List<ISkipCondition> skipConditionList=SkipConditionUtil.getSkipConditions();
		// 这样做不是最好的，应该在bpmnxml生成的时候给ext:description初始化。。但这太难跟了- -
		if (StringUtil.isEmpty(prop.getDescription())) {
			prop.setDescription(bpmDefinition.getDesc());
		}
		prop.setTestStatus(bpmDefinition.getTestStatus());
		prop.setStatus(bpmDefinition.getStatus());
		prop.setShowUrgentState(bpmDefinition.getShowUrgentState() ==1?true:false);
		prop.setShowModifyRecord(bpmDefinition.getShowModifyRecord() ==1?true:false);
        prop.setReadRevoke(bpmDefinition.getIsReadRevoke().equals("true")?true:false);
        prop.setUrgentMailTel(bpmDefinition.getUrgentMailTel());
        prop.setUrgentSmsTel(bpmDefinition.getUrgentSmsTel());
        prop.setUseMainForm(BeanUtils.isNotEmpty(prop.getUseMainForm())?prop.getUseMainForm():"mainVersion");
        prop.setDoneDataVersion(BeanUtils.isNotEmpty(prop.getDoneDataVersion())?prop.getDoneDataVersion():"history");
		resMap.put("prop", prop);
		resMap.put("handlerTypes", handlerTypes);
		resMap.put("skipConditionList", skipConditionList);
		
		return resMap;
	}

	/**
	 * 保存流程的其他属性。 注意:这里有几个操作没有使用事务。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 */
	@RequestMapping(value="saveProp",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程其他属性的参数", httpMethod = "POST", notes = "获取流程其他属性的参数")
	public CommonResult<String> saveProp(
			@ApiParam(name="defPropVo",value="流程其他参数保存对象", required = true) @RequestBody DefPropSaveVo defPropVo) throws Exception {
		try {
			String defId = defPropVo.getDefId();
			String description = defPropVo.getDescription();
            Map<String,Object> map = new HashMap<>();
            map.put("id", defId);
            map.put("rev", defPropVo.getRev());
            DefaultBpmDefinition defaultBpmDefinition1 = bpmDefinitionManager.getBpmDefinitionByRev(map);
            if(BeanUtils.isNotEmpty(defaultBpmDefinition1)) {
                BpmDefExtProperties prop = defPropVo.getBpmProp();

                String status = prop.getStatus();

                propertiesBpmDefXmlHandler.saveNodeXml(defId, "", prop);
                // 更新测试状态
                DefaultBpmDefinition bpmDefinition = bpmDefinitionManager.getById(defId);

                // 更新状态
                String oldStatus = bpmDefinition.getStatus();

                bpmDefinition.setTestStatus(prop.getTestStatus());
                bpmDefinition.setStatus(status);
                bpmDefinition.setDesc(description);
                bpmDefinition.setShowUrgentState(prop.isShowUrgentState()?1:0);
                bpmDefinition.setShowModifyRecord(prop.isShowModifyRecord()?1:0);
                bpmDefinition.setIsReadRevoke(prop.isReadRevoke()?"true":"false");
                String urgentMail = prop.getUrgentMailTel();
                if(StringUtil.isNotEmpty(urgentMail)){
                    urgentMail = urgentMail.replace("<p>","" );
                    urgentMail = urgentMail.replace("</p>","" );
                }
                String urgentSms = prop.getUrgentSmsTel();
                if(StringUtil.isNotEmpty(urgentSms)){
                    urgentSms = urgentSms.replace("<p>","" );
                    urgentSms = urgentSms.replace("</p>","" );
                }
                bpmDefinition.setUrgentMailTel(urgentMail);
                bpmDefinition.setUrgentSmsTel(urgentSms);
                bpmDefinitionManager.update(bpmDefinition);

                bpmDefinitionManager.updBpmDefinitionStatus(bpmDefinition, oldStatus);

                bpmDefinitionAccessor.clean(defId);

                return new CommonResult<String>(true, "保存流程参数成功!", "");
            }else{
                return  new CommonResult<String>(false,"此流程定义不是最新版本，请重新获取再修改");
            }
		} catch (Exception e) {
			return new CommonResult<String>(false,"保存流程参数失败!","");
		}

	}


	@RequestMapping(value="nodeBos",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "展示流程节点BO设置", httpMethod = "GET", notes = "获取流程其他属性的参数")
	public Object nodeBos(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="topDefKey",value="流程定义key", required = true) @RequestParam String topDefKey) throws Exception {
		
		
		DefaultBpmDefinition bpmDefinition = bpmDefinitionManager.getById(defId);
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		
		BpmBoDef boDef =null;
		//在子流程中做配置，获取顶级流程的bo设定。
		if(StringUtil.isNotEmpty(topDefKey)){
			DefaultBpmDefinition bpmDef= bpmDefinitionManager.getMainByDefKey(topDefKey, false);
			BpmProcessDef<BpmProcessDefExt> topProcessExt = bpmDefinitionAccessor.getBpmProcessDef(bpmDef.getDefId());
			boDef = BpmDefUtil.getBpmBoDef(topProcessExt);
		}
		else{
			boDef = BpmDefUtil.getBpmBoDef(bpmProcessDefExt);
		}
		
		List<BpmNodeDef> nodeDefList = BpmDefUtil.getNodeDefs(bpmProcessDefExt);

		BpmFormInit formInit = BpmDefUtil.getBpmFormInit(bpmProcessDefExt,topDefKey);

		ObjectNode jsonObj = (ObjectNode) JsonUtil.toJsonNode(formInit);

		ObjectNode boDefJson = (ObjectNode) JsonUtil.toJsonNode(boDef);

		jsonObj.set("bodef", boDefJson);

		ArrayNode jry =JsonUtil.getMapper().createArrayNode();
		for (BpmNodeDef def : nodeDefList) {
			ObjectNode jo = JsonUtil.getMapper().createObjectNode();
			jo.put("nodeId", def.getNodeId());
			jo.put("name", def.getName());
			jry.add(jo);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bpmDefinition", bpmDefinition);
		map.put("json", jsonObj);
		map.put("nodeDefList", jry);
		return map;

	}
	
	@RequestMapping(value="exportXml" ,method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "导出流程定义xml", httpMethod = "GET", notes = "导出流程定义xml")
	public void exportXml( HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name="bpmDefId",value="流程定义id", required = true) @RequestParam String bpmDefId) throws Exception {
		response.setContentType("APPLICATION/OCTET-STREAM");
		if (BeanUtils.isNotEmpty(bpmDefId)) {
			String[] bpmDefIds = bpmDefId.split(",");
			List<String> defList = Arrays.asList(bpmDefIds);
			String zipName = "ht_flow_"+DateFormatUtil.format(LocalDateTime.now(), "yyyy_MMdd_HHmmss");
			// 写XML
			Map<String, String> strXml = bpmDefTransform.exportDef(defList);
			HttpUtil.downLoadFile(request, response, strXml,zipName);
		}
	}


	
	@SuppressWarnings("unchecked")
	public static void checkXmlFormat(String xml) throws Exception {
		String firstName = "bpmlist";
		String nextName = "bpmDef";
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();
		String msg = "导入文件格式不对";
		if (!root.getName().equals(firstName))
			throw new Exception(msg);
		List<Element> itemLists = root.elements();
		for (Element elm : itemLists) {
			if (!elm.getName().equals(nextName))
				throw new Exception(msg);
		}

	}

	/**
	 * 根据xmlStr获取BpmDefinition对象，是根据xml中的defKey
	 * 
	 * @param xmlStr
	 * @return BpmDefinition
	 * @exception
	 * @since 1.0.0
	 */
	public BpmDefinition getBpmDefByXml(String xmlStr) {
		String key = "";
		Pattern pattern = Pattern.compile("defKey=\"(.*?)\"");
		Matcher matcher = pattern.matcher(xmlStr);
		while (matcher.find()) {
			key = matcher.group(1);
		}
		BpmDefinition bpmDefinition = (BpmDefinition) bpmDefinitionManager.getMainByDefKey(key);
		return bpmDefinition;
	}

	@RequestMapping(value="importSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程导入，根据传入的文件id从缓存中取出xml文件导入，并清除缓存", httpMethod = "POST", notes = "附件上传操作")
	public Object importSave(@ApiParam(name="confirmImport",value="确认导入",required=false) @RequestParam Optional<Boolean> confirmImport,
			@ApiParam(name="typeId",value="表单标识",required=false) @RequestParam Optional<String> typeId,
			@ApiParam(name="cacheFileId",value="缓存的流程文件id",required=false) @RequestParam Optional<String> cacheFileId) throws Exception {
		
		CommonResult<String> message = null;
		try {
			if (confirmImport.orElse(false)) {
				String byKey = baseService.getImportFileFromCache(cacheFileId.get());
				if(StringUtil.isEmpty(byKey)) {
					return new CommonResult<String>(false, "导入失败:上传的文件已从缓存中清除，请重新导入。");
				}
				ObjectNode objectNode = (ObjectNode) JsonUtil.toJsonNode(byKey);
				message = bpmDefTransform.importDef(objectNode,typeId.orElse(""));
			}
			baseService.delImportFileFromCache(cacheFileId.orElse(""));
		} catch (Exception e) {
			message = new CommonResult<String>(false, "导入失败:" + ExceptionUtils.getRootCauseMessage(e));
		} 
		return message;
	}
	
	@RequestMapping(value="importCheck", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程导入前校验,通过校验则直接导入，如有重复流程，则返回流程xml的缓存key，待用户确认覆盖后再次导入", httpMethod = "POST", notes = "流程导入前校验")
	public Object importCheck(MultipartHttpServletRequest request, HttpServletResponse response,
			@ApiParam(name="typeId",value="表单标识",required=false) @RequestParam Optional<String> typeId) throws Exception {
		Map<String, MultipartFile> fileMaps = request.getFileMap();
		Iterator<MultipartFile> it = fileMaps.values().iterator();
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		while (it.hasNext()) {
			files.add(it.next());
		}
		MultipartFile fileLoad = files.get(0);
		String unZipFilePath = "";
		CommonResult<String> message = null;
		try {
			String fileDir = StringUtil.substringBeforeLast(fileLoad.getOriginalFilename().toString(), ".");
			String rootRealPath = (FileUtil.getIoTmpdir() +"/attachFiles/unZip/").replace("/", File.separator);
			FileUtil.createFolder(rootRealPath, true);
			// 解压文件
			ZipUtil.unZipFile(fileLoad, rootRealPath); 
		    unZipFilePath = rootRealPath + File.separator + fileDir; // 解压后文件的真正路径
		    
			String bpmdefsXml = FileUtil.readFile( unZipFilePath + "/bpmdefs.flow.xml");
			String formXmlStr = FileUtil.readFile(unZipFilePath + "/form.xml");
			String boXmlStr = FileUtil.readFile(unZipFilePath +  "/bo.xml");
			String formRightsXml = FileUtil.readFile(unZipFilePath +  "/formrights.xml");

			if (StringUtils.isEmpty(bpmdefsXml)) throw new Exception("导入的未按指定的格式");

			checkXmlFormat(bpmdefsXml);
			
			BpmDefXmlList defList=(BpmDefXmlList) JAXBUtil.unmarshall(bpmdefsXml, BpmDefXmlList.class);
			List<BpmDefXml> list= defList.getBpmList();
			List<String> names = new ArrayList<String>();
			for(BpmDefXml defXml:list){
				DefaultBpmDefinition def = defXml.getBpmDefinition();
				if(BeanUtils.isNotEmpty(def)){
					DefaultBpmDefinition odef = bpmDefinitionManager.getMainByDefKey(def.getDefKey());
					if(odef != null){
						names.add(def.getName()+"（"+def.getDefKey()+"）");
					}
				}
			}
			ObjectNode obj = JsonUtil.getMapper().createObjectNode();
			obj.put("bpmdefsXml", bpmdefsXml);
			obj.put("formXmlStr", formXmlStr);
			obj.put("boXmlStr", boXmlStr);
			obj.put("formRightsXml", formRightsXml);
			if(BeanUtils.isEmpty(names)){
				message = bpmDefTransform.importDef(obj,typeId.orElse(""));
			}else{
				String cacheFileId = UniqueIdUtil.getSuid();
				baseService.putImportFileInCache(cacheFileId, JsonUtil.toJson(obj));
				message = new CommonResult<String>(false, "导入失败，流程【" + String.join("，",names)+"】在系统中已存在，是否继续为其新增版本？",cacheFileId);
			}
			
		} catch (Exception e) {
			message = new CommonResult<String>(false, "导入失败:" + e.getMessage());
		} finally {
			File boDir = new File(unZipFilePath);
			if (boDir.exists()) {
				FileUtil.deleteDir(boDir); // 删除解压后的目录
			}
		}
		return message;
	}

	/**
	 * 流程定义历史版本
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="versions", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程定义历史版本", httpMethod = "POST", notes = "流程定义历史版本")
	public PageList<DefaultBpmDefinition> versions(
			@ApiParam(name="defId",value="流程定义id")@RequestParam String defId,
			@ApiParam(name="defKey",value="流程定义key")@RequestParam String defKey,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<DefaultBpmDefinition> queryFilter) throws Exception {
		DefaultBpmDefinition defaultBpmDefinition = null;
		if (StringUtil.isEmpty(defKey) && StringUtil.isNotEmpty(defId)) {
			defaultBpmDefinition = bpmDefinitionManager.getById(defId);
			if (defaultBpmDefinition != null) {
				queryFilter.addFilter("def_key_", defaultBpmDefinition.getDefKey(), QueryOP.EQUAL);
			} else {
				queryFilter.addFilter("def_key_", "", QueryOP.EQUAL);
			}
		} else {
			queryFilter.addFilter("def_key_", defKey, QueryOP.EQUAL);
		}
		return bpmDefinitionManager.query(queryFilter);
	}

	/**
	 * 设置历史版本的流程定义为主版本
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("switchMainVersion")
	@ApiOperation(value = "设置历史版本的流程定义为主版本", httpMethod = "GET", notes = "设置历史版本的流程定义为主版本")
	public CommonResult<String> switchMainVersion(@ApiParam(name="defId",value="流程定义id")@RequestParam String defId) throws Exception {
		try {
			bpmDefinitionService.switchMainVersion(defId);
			return new CommonResult<String>(true,"设置成功","");
		} catch (Exception e) {
			return new CommonResult<String>(false,"设置失败:"+e.getMessage(),"");
		}
	}


	/**
	 * 设置外部子流程
	 * **/
	@RequestMapping(value="subFlowDetail", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "设置外部子流程", httpMethod = "GET", notes = "设置外部子流程")
	public Map<String,Object>  subFlowDetail(
			@ApiParam(name="defId",value="流程定义id")@RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam String nodeId )throws Exception {
		Map<String,Object> resMap=new HashMap<>();
		BpmNodeDef node = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		if (node.getType() != NodeType.CALLACTIVITY)
			throw new RuntimeException("这不是一个外部子流程节点！");
		resMap.put("topDefKey", node.getBpmProcessDef().getDefKey());
		CallActivityNodeDef callActivityNode = (CallActivityNodeDef) node;
		String childFlowKey = callActivityNode.getFlowKey();
		BpmDefinition subProcessDef = bpmDefinitionManager.getMainByDefKey(childFlowKey);
		if(BeanUtils.isEmpty(subProcessDef)) throw new RuntimeException("根据绑定的子流程key未找到对应流程！");
		resMap.put("defId", subProcessDef.getDefId());
		return resMap;
	}
	
	/**
	 * 得到全部节点自定义按钮
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="getNodeBtns", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程代理设置列表(分页条件查询)数据", httpMethod = "POST", notes = "流程代理设置列表(分页条件查询)数据")
	public PageList<BpmNodeDef> getNodeBtns(@ApiParam(name="defId",value="流程定义id")@RequestBody String defId) throws Exception {
		List<BpmNodeDef> nodeDefs = bpmDefinitionAccessor.getSignUserNode(defId);
		List list = new ArrayList();
		if (nodeDefs != null && !nodeDefs.isEmpty()) {
			for (BpmNodeDef bnd : nodeDefs) {
				Map map = new HashMap();
				map.put("nodeId", bnd.getNodeId());
				map.put("name", bnd.getName());
				map.put("type", bnd.getType());
				map.put("btns",JsonUtil.toJson(bnd.getButtons())); 
				list.add(map);
			}
		}
		return new PageList<BpmNodeDef>(list);
	}

	/**
	 * 得到某个节点自定义按钮
	 * 
	 * action : 0:获取默认初始化的按钮,1:获取配置的按钮,2:获取默认不初始化的按钮
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getNodeSet", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "得到某个节点自定义按钮", httpMethod = "POST", notes = "得到某个节点自定义按钮")
	public List<Button> getNodeSet(
			@ApiParam(name="defId",value="流程定义id")@RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam String nodeId,
			@ApiParam(name="action",value="0:获取默认初始化的按钮,1:获取配置的按钮,2:获取默认不初始化的按钮")@RequestParam int action) throws Exception {
		//0:获取默认初始化的按钮,1:获取配置的按钮,2:获取默认不初始化的按钮
		
		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<Button> nodeBtns = nodeDef.getButtons();
		
		List<Button> defaultBtns = nodeDef.getButtonsByType(true);
		
		if(action==0) {
			return defaultBtns;
		}
		else if(action==2){
			List<Button> notInitBtns = nodeDef.getButtonsByType(false);
			return notInitBtns;
		}
		
		// supportScript 要修改buttonXml 定义 而且仅仅在编辑页面游泳 so 如此处理吧。快点
		for (Button btn : nodeBtns) {
			btn.setSupportScript(true);
			for (Button b : defaultBtns) {
				if(btn.getAlias().equals(b.getAlias())){
					if(b.getSupportScript()==false) {
						btn.setSupportScript(false);
					} 
				}
			}
		}
		
		return nodeBtns;
	}

	/**
	 * 保存节点的按钮
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="saveNodeBtns", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存节点的按钮", httpMethod = "POST", notes = "保存节点的按钮")
	public CommonResult<List<Button>> saveNodeBtns(
			@ApiParam(name="btnsVo",value="流程节点按钮对象")@RequestBody DefBtnsSaveVo btnsVo) throws Exception {
		try {
			String defId = btnsVo.getDefId();
			String nodeId =btnsVo.getNodeId();
			List<Button> btns =btnsVo.getBtns();
			
			buttonsBpmDefXmlHandler.saveNodeXml(defId, nodeId, btns);
			return new CommonResult<List<Button>>(true,"按钮设置成功",btns);
		} catch (Exception e) {
			return new CommonResult<List<Button>>(false,"按钮设置失败",null);
		}
	}

	
	
	/**
	 * 清除测试状态流程的测试数据
	 * @throws Exception
	 */
	@RequestMapping(value="cleanData", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "清除测试状态流程的测试数据", httpMethod = "POST", notes = "清除测试状态流程的测试数据")
	public CommonResult<String> cleanData(
			@ApiParam(name="defId",value="流程定义id")@RequestParam String defId) throws Exception {
		try {
			bpmDefinitionService.cleanData(defId);
			return new CommonResult<String>(true,"清除数据成功","");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"清除数据失败："+e.getMessage(),"");
		}
	}

	/**
	 * 发布
	 * 
	 */
	@RequestMapping(value="deploy", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "发布流程定义", httpMethod = "POST", notes = "发布流程定义")
	public CommonResult<String> deploy(
			@ApiParam(name="defId",value="流程定义id")@RequestParam String defId) throws Exception {
		try {
			BpmDefinition def = (BpmDefinition) bpmDefinitionService.getBpmDefinitionByDefId(defId);
			bpmDefinitionService.deploy(def);
			return new CommonResult<String>(true,"发布流程定义成功！","");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"发布流程定义失败："+e.getMessage(),"");
		}
	}


	@RequestMapping(value="getBindRelation", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单绑定关系。", httpMethod = "GET", notes = "根据组件别名获取所有子实体。")
	public Map<String,Object> getBindRelation(@ApiParam(name="defId",value="表单defId")@RequestParam String defId) throws Exception {
		return bpmDefinitionManager.getBindRelation(defId);
	}

	/**
	 * 节点概要
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping("nodeSummary")mark
	public ModelAndView nodeSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String defId = RequestUtil.getString(request, "defId");
		// String parentActDefId = RequestUtil.getString(request,
		// "parentActDefId", "");
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		List<BpmNodeDef> nodeDefList = bpmProcessDefExt.getBpmnNodeDefs();

		Map<String, Boolean> startScriptMap = new HashMap<String, Boolean>();
		Map<String, Boolean> endScriptMap = new HashMap<String, Boolean>();
		Map<String, Boolean> preScriptMap = new HashMap<String, Boolean>();
		Map<String, Boolean> afterScriptMap = new HashMap<String, Boolean>();
		Map<String, Boolean> assignScriptMap = new HashMap<String, Boolean>();

		Map<String, Boolean> nodeRulesMap = new HashMap<String, Boolean>();
		Map<String, Boolean> nodeButtonMap = new HashMap<String, Boolean>();
		Map<String, Boolean> taskReminderMap = new HashMap<String, Boolean>();

		Map<String, Boolean> nodeFormMap = new HashMap<String, Boolean>();
		boolean globalFormFlag = false;
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		Form globalForm = defExt.getGlobalForm();
		if (globalForm != null)
			globalFormFlag = true;

		Map<String, Boolean> nodeUserMap = new HashMap<String, Boolean>();

		for (BpmNodeDef bpmNodeDef : nodeDefList) {
			String nodeId = bpmNodeDef.getNodeId();
			// 用户设置
			this.getNodeUserMap(bpmNodeDef, nodeId, nodeUserMap);

			// 流程事件(脚本)
			this.getNodeScriptMap(bpmNodeDef, nodeId, startScriptMap, endScriptMap, preScriptMap, afterScriptMap, assignScriptMap);
			// 流程节点规则
			this.getNodeRulesMap(bpmNodeDef, nodeId, nodeRulesMap);

			// 操作按钮
			this.getNodeButtonMap(bpmNodeDef, nodeId, nodeButtonMap);

			// 催办信息
		//	this.getTaskReminderMap(bpmNodeDef, nodeId, defId, taskReminderMap);
		}

		this.orderNodeDefList(nodeDefList);
		return this.getAutoView().addObject("bpmDefinition", bpmProcessDefExt).addObject("defId", defId).addObject("nodeDefList", nodeDefList).addObject("startScriptMap", startScriptMap).addObject("endScriptMap", endScriptMap).addObject("preScriptMap", preScriptMap).addObject("afterScriptMap", afterScriptMap).addObject("assignScriptMap", assignScriptMap).addObject("nodeRulesMap", nodeRulesMap).addObject("nodeUserMap", nodeUserMap).addObject("nodeFormMap", nodeFormMap).addObject("nodeButtonMap", nodeButtonMap).addObject("taskReminderMap", taskReminderMap).addObject("globalFormFlag", globalFormFlag);

	}*/


	/**
	 * 操作按钮
	 * 
	 * @param bpmNodeDef
	 * @param nodeId
	 * @param nodeButtonMap
	 */
	@SuppressWarnings("unused")
	private void getNodeButtonMap(BpmNodeDef bpmNodeDef, String nodeId, Map<String, Boolean> nodeButtonMap) {
		if (BeanUtils.isNotEmpty(bpmNodeDef.getButtons()))
			nodeButtonMap.put(nodeId, true);
		else
			nodeButtonMap.put(nodeId, false);
	}

	/**
	 * 流程节点规则
	 * 
	 * @param bpmNodeDef
	 * @param nodeId
	 * @param nodeRulesMap
	 */
	@SuppressWarnings("unused")
	private void getNodeRulesMap(BpmNodeDef bpmNodeDef, String nodeId, Map<String, Boolean> nodeRulesMap) {
		if (! (bpmNodeDef instanceof UserTaskNodeDef) ) return ;
		
		UserTaskNodeDef userTaskNodeDef = (UserTaskNodeDef) bpmNodeDef;
		if (BeanUtils.isNotEmpty(userTaskNodeDef.getJumpRuleList()))
			nodeRulesMap.put(nodeId, true);
		else
			nodeRulesMap.put(nodeId, false);

	}

	/**
	 * 流程事件(脚本)
	 * 
	 * @param bpmNodeDef
	 * @param nodeId
	 * @param startScriptMap
	 * @param endScriptMap
	 * @param preScriptMap
	 * @param afterScriptMap
	 * @param assignScriptMap
	 */
	@SuppressWarnings("unused")
	private void getNodeScriptMap(BpmNodeDef bpmNodeDef, String nodeId, Map<String, Boolean> startScriptMap, Map<String, Boolean> endScriptMap, Map<String, Boolean> preScriptMap, Map<String, Boolean> afterScriptMap, Map<String, Boolean> assignScriptMap) {
		Map<ScriptType, String> scriptMap = bpmNodeDef.getScripts();
		for (Map.Entry<ScriptType, String> entry : scriptMap.entrySet()) {
			if (StringUtil.isEmpty(entry.getValue())) continue;
			
			switch (entry.getKey()) {
				case START:
					startScriptMap.put(nodeId, true);
					break;
				case END:
					endScriptMap.put(nodeId, true);
					break;
				case CREATE:
					preScriptMap.put(nodeId, true);
					break;
				case COMPLETE:
					afterScriptMap.put(nodeId, true);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * 节点排序
	 * 
	 * @param nodeDefList
	 */
	@SuppressWarnings("unused")
	private void orderNodeDefList(List<BpmNodeDef> nodeDefList) {
		for (BpmNodeDef bpmNodeDef : nodeDefList) {
			if (bpmNodeDef.getType() == NodeType.START)
				bpmNodeDef.setOrder(bpmNodeDef.getOrder());
			else if (bpmNodeDef.getType() == NodeType.END)
				bpmNodeDef.setOrder(bpmNodeDef.getOrder() + 100);
			else
				bpmNodeDef.setOrder(bpmNodeDef.getOrder() + 1000);
		}
		// 节点排序
		Collections.sort(nodeDefList, new BpmNodeDefComparator());
	}

	/**
	 * 设置节点人员
	 * 
	 * @param bpmNodeDef
	 * @param nodeId
	 * @param nodeUserMap
	 * @throws Exception 
	 * @throws IOException 
	 */
	@SuppressWarnings("unused")
	private void getNodeUserMap(BpmNodeDef bpmNodeDef, String nodeId, Map<String, Boolean> nodeUserMap) throws  Exception {
		// 节点类型是用户节点和会签才有人员设置
		if (bpmNodeDef.getType() == NodeType.USERTASK || bpmNodeDef.getType() == NodeType.SIGNTASK) {
			PluginParse userPluginContext = (PluginParse) bpmNodeDef.getPluginContext(UserAssignPluginContext.class);
			if (userPluginContext != null && userPluginContext.getJson() != null) {
				nodeUserMap.put(nodeId, true);
			}

		}
	}

	
	/**
	 * 初始化所有按钮
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="initNodeBtn",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "初始化所有按钮", httpMethod = "GET", notes = "初始化所有按钮")
	public CommonResult<String> initNodeBtn(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId) throws Exception {
		String resultMsg = "";
		try {
			//如果nodeId为空则表示初始化全部按钮。
			List<BpmNodeDef> nodeDefs = bpmDefinitionAccessor.getSignUserNode(defId);
		
			if (BeanUtils.isNotEmpty(nodeDefs) ) {
				//初始化全部按钮
				if (StringUtil.isEmpty(nodeId)) {
					for (BpmNodeDef bnd : nodeDefs) {
						String key=bnd.getType().getKey();
						if (key.equals("signTask") || key.equals("userTask") || key.equals("start")) {
							buttonsBpmDefXmlHandler.saveNodeXml(defId, bnd.getNodeId(), bnd.getButtonsByType(true));
						}
					}
				}
				// 初始化某个节点按钮。
				else {
					for (BpmNodeDef bnd : nodeDefs) {
						if (nodeId.equals(bnd.getNodeId())) {
							buttonsBpmDefXmlHandler.saveNodeXml(defId, nodeId, bnd.getButtonsByType(true));
						}
					}
				}
			}
			resultMsg =  "初始化成功";
			return  new CommonResult<String>(true,resultMsg,"");
		} catch (Exception e) {
			resultMsg =  "初始化失败";
			return  new CommonResult<String>(false,resultMsg+":"+e.getMessage()  ,"");
		}
	}

	@RequestMapping(value="copyDef",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "复制流程", httpMethod = "GET", notes = "复制流程")
	public CommonResult<String> del(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="name",value="流程名称", required = true) @RequestParam String name,
			@ApiParam(name="defKey",value="流程定义key", required = true) @RequestParam String defKey) throws IOException{
		
		BpmDefinition bpmDefinition = (BpmDefinition) bpmDefinitionManager.getMainByDefKey(defKey, false);
		if(bpmDefinition!=null){
			return new CommonResult<String>(false,"流程key已经存在","");
		}
		try{
			bpmDefinitionManager.copyDef(defId,name,defKey);
		}catch(Exception ex){
			return new CommonResult<String>(false,ex.getMessage(),"");
		}
		return new CommonResult<String>(true,"复制成功吗 ","");
	}
	
	/**
	 *根据流程key获取最新的流程定义id
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getBpmDefId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程key获取最新的流程定义id", httpMethod = "GET", notes = "根据流程key获取最新的流程定义id")
	public String getBpmDefId(@ApiParam(name="defKey",value="流程定义key", required = true) @RequestParam String defKey) throws Exception {
		List<DefaultBpmDefinition> defs = bpmDefinitionManager.queryByDefKey(defKey);
		String  defId = ""; 
	       if(defs.size()>0 && BeanUtils.isNotEmpty(defs.get(defs.size()-1))) defId= defs.get(defs.size()-1).getDefId();
		return defId;
	}
	
	/**
	 *根据流程key获取最新的流程定义id
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="isBoBindFlowCheck",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "检测bo是否已绑定流程", httpMethod = "GET", notes = "检测bo是否已绑定流程")
	public CommonResult<Boolean> isBoBindFlowCheck(@ApiParam(name="boCode",value="bo定义别名", required = true) @RequestParam String boCode,@ApiParam(name="formKey",value="表单key", required = true) @RequestParam String formKey) throws Exception {
		List<BpmProBo> bpmProBos=bpmProBoManager.getByBoCode(boCode);
		for(BpmProBo bpmProBo:bpmProBos){
			DefaultBpmDefinition  bpmDef = bpmDefinitionManager.getById(bpmProBo.getProcessId());
			if(BeanUtils.isEmpty(bpmDef)) continue;

			String defId = bpmDef.getDefId();

			boolean isFormExist=isFormExists(defId,formKey);
			if(isFormExist){
				return new CommonResult<Boolean>(true, "已绑定流程【"+bpmDef.getName()+"】",true);
			}
		}
		return new CommonResult<Boolean>(true, "检测成功！",false);
	}
	
	
	/**
	 * 检查表单是否被使用过。
	 * @param defId
	 * @param formKey
	 * @return
	 * @throws Exception 
	 */
	private boolean isFormExists(String defId,String formKey) throws Exception{
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		Form frm = defExt.getGlobalForm();//获取全局表单
		Form mobileFrm = defExt.getGlobalMobileForm();//全局手机表单
		Form instFrm = defExt.getInstForm();//获取全局实例表单
		Form instMobileFrm = defExt.getInstMobileForm();//获取全局实例手机表单
		if((frm != null && frm.getFormValue().equals(formKey)) || (mobileFrm != null && mobileFrm.getFormValue().equals(formKey))||
				(instFrm != null && instFrm.getFormValue().equals(formKey)) || (instMobileFrm != null && instMobileFrm.getFormValue().equals(formKey))){//不同版本的表单共用一个formKey ,因此不用考虑不同版本的表单绑定了流程
			return true;
		}else{//如果全局表单里面没有设置则会去找节点表单
			List<BpmNodeDef> nodeList = bpmDefinitionAccessor.getSignUserNode(defId);//全局表单没有，则会找节点表单
			for(BpmNodeDef bpmNodeDef:nodeList){
				frm = bpmNodeDef.getForm();
				if(frm != null && frm.getFormValue().equals(formKey)){
					return true;
				}
				frm = bpmNodeDef.getMobileForm();
				if(frm != null && frm.getFormValue().equals(formKey)){
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 *根据流程key获取最新的流程定义id
	 * x
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getBpmdefByDefId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程定义id获取流程定义", httpMethod = "GET", notes = "根据流程定义id获取流程定义")
	public DefaultBpmDefinition getBpmdefByDefId(@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception {
		return bpmDefinitionManager.getById(defId);
	}
	
	/**
	 *根据流程key获取最新的流程定义id
	 * x
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="defSetCategory",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设置流程分类", httpMethod = "POST", notes = "设置流程分类")
	public CommonResult<String> defSetCategory(@ApiParam(name="jNode",value="设置分类参数对象", required = true) @RequestBody ObjectNode jNode) throws Exception {
		List<String> defIds = Arrays.asList(jNode.get("defIds").asText().split(","));
		String typeName = jNode.get("typeName").asText();
		String typeId = jNode.get("typeId").asText();
		return bpmDefinitionManager.setDefType(typeName,typeId,defIds);
	}

	@RequestMapping(value = "bpmDefinitionData",method = RequestMethod.GET)
	@ApiOperation(value = "获取流程",httpMethod = "GET",notes = "获取流程")
	public List<Map<String, String>> bpmDefinitionData(String alias){
		return bpmDefinitionManager.bpmDefinitionData(alias);
	}
	/**
	 * 判断用户是否有启动流程权限
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="flowHasStartRights",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "判断用户是否有启动流程权限", httpMethod = "GET", notes = "判断用户是否有启动流程权限")
	public CommonResult<Boolean> flowHasStartRights(@ApiParam(required = false, name = "defKey", value = "defKey") @RequestParam String defKey) throws IOException{
		ObjectNode reslt = bpmDefAuthorizeManager.getRight(defKey, BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE.START);
		return new CommonResult<>(true, "",BeanUtils.isNotEmpty(reslt));
	}
}
