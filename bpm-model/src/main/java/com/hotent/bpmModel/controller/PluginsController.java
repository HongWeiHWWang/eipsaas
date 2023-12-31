package com.hotent.bpmModel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.engine.def.impl.handler.PluginsBpmDefXmlHandler;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.plugin.execution.procnotify.context.ProcNotifyPluginContext;
import com.hotent.bpm.plugin.task.reminders.context.RemindersPluginContext;
import com.hotent.bpm.plugin.task.test.context.TestPluginContext;
import com.hotent.bpm.util.MessageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 *  描述：流程插件管理    
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/plugins/v1/")
@Api(tags="流程插件")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class PluginsController {
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor ;

	@RequestMapping(value="procNotifyEdit",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取编辑结束抄送页面数据", httpMethod = "GET", notes = "获取编辑结束抄送页面数据")
	public String procNotifyEdit(@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception{
		// 获取插件
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt= (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		
		ProcNotifyPluginContext procNotifyPluginContext = (ProcNotifyPluginContext) defExt.getBpmPluginContext(ProcNotifyPluginContext.class);
		String procNotifyJson = null;
		if(procNotifyPluginContext != null) {
			procNotifyJson = procNotifyPluginContext.getJson();
		}
		
		return procNotifyJson;
	}
	
	/**
	 * 保存结束抄送
	 * @exception 
	 * @since  1.0.0
	 */
	@RequestMapping(value="procNotifySave",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存结束抄送", httpMethod = "POST", notes = "保存结束抄送")
	public CommonResult<String> procNotifySave(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="procNotifyJson",value="办结抄送的json数据", required = true) @RequestBody String procNotifyJson) throws Exception{
		String resultMsg=null;
		
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt= (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		try {  
			if(StringUtil.isNotEmpty(defId)){ 
				ProcNotifyPluginContext context = new ProcNotifyPluginContext();
				context.parse(procNotifyJson);
				List<BpmPluginContext> plugins = changeOnePluginContextForSave(defExt.getBpmPluginContexts(),context);
				
				PluginsBpmDefXmlHandler bpmDefXmlHandler = (PluginsBpmDefXmlHandler) AppUtil.getBean(PluginsBpmDefXmlHandler.class);
				bpmDefXmlHandler.saveNodeXml(defId, null, plugins);
				resultMsg="办结抄送添加成功！";
			}
			return new CommonResult<String>(resultMsg);
		} catch (Exception e) {
			return new CommonResult<String>(false,"办结抄送设置失败！"+e.getMessage());
		}
	}
	
	
	/**
	 * 获取催办json
	 */
	@RequestMapping(value="remindersJson", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取催办json", httpMethod = "GET", notes = "获取催办json")
	public Map<String,Object> remindersJson(@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId	) throws Exception{
		
		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		RemindersPluginContext remindersPluginContext=nodeDef.getPluginContext(RemindersPluginContext.class);
		
		Map<String,Object> json =new HashMap<String,Object>();
		Map<String, String> handlerList=MessageUtil.getHandlerTypes();
		json.put("reminders",remindersPluginContext!= null?remindersPluginContext.getBpmPluginDef() :  JsonUtil.toJsonNode("{\"reminderList\":[]}"));
		json.put("warnSetting","[{name:'蓝色预警',color:'blue',level:51},{name:'黄色预警',color:'yellow',level:52},{name:'红色预警',color:'red',level:53}]");
		json.put("handlerList",handlerList);
		return json;
	}
	
	/**
	 * 保存结束抄送
	 * @exception 
	 * @since  1.0.0
	 */
	@RequestMapping(value="remindersSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存结束抄送", httpMethod = "POST", notes = "保存结束抄送")
	public CommonResult<String> remindersSave(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId,
			@ApiParam(name="remindersJson",value="抄送json", required = true) @RequestBody String remindersJson) throws Exception{
		try {  
			BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
			
			RemindersPluginContext context = new RemindersPluginContext();
			context.parse(remindersJson);
			List<BpmPluginContext> plugins = changeOnePluginContextForSave(nodeDef.getBpmPluginContexts(),context);
			
			PluginsBpmDefXmlHandler bpmDefXmlHandler = (PluginsBpmDefXmlHandler) AppUtil.getBean(PluginsBpmDefXmlHandler.class);
			bpmDefXmlHandler.saveNodeXml(defId, nodeId, plugins);
			return new CommonResult<String>("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"保存失败："+e.getMessage());
		}
	}
	
	/**
	 * 替换要保存的插件，
	 * @return List<BpmPluginContext>
	 */
	private List<BpmPluginContext> changeOnePluginContextForSave(List<BpmPluginContext> contexts,BpmPluginContext pluginContext){
		List<BpmPluginContext> bpmPluginContexts = new ArrayList<BpmPluginContext>();
		bpmPluginContexts.add(pluginContext);
		
		if(BeanUtils.isEmpty(contexts)) return bpmPluginContexts;
		
		for(BpmPluginContext context : contexts){
			if(!context.getClass().isAssignableFrom(pluginContext.getClass())){
				bpmPluginContexts.add(context);
			}
		}
		return bpmPluginContexts;
	}


	/**
	 * 获取测试插件json
	 */
	@RequestMapping(value="getTestPlugin", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取测试插件json", httpMethod = "GET", notes = "获取测试插件json")
	public String getTestPlugin(@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId	) throws Exception{
		
		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		TestPluginContext remindersPluginContext=nodeDef.getPluginContext(TestPluginContext.class);
		if (BeanUtils.isEmpty(remindersPluginContext)) {
			return "";
		}
		return remindersPluginContext.getJson();
	}
	
	/**
	 * 保存测试插件
	 * @exception 
	 * @since  1.0.0
	 */
	@RequestMapping(value="saveTestPlugin", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存测试插件", httpMethod = "POST", notes = "保存测试插件")
	public CommonResult<String> saveTestPlugin(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId,
			@ApiParam(name="json",value="json", required = true) @RequestBody String json) throws Exception{
		try {  
			BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
			
			TestPluginContext context = new TestPluginContext();
			context.parse(json);
			List<BpmPluginContext> plugins = changeOnePluginContextForSave(nodeDef.getBpmPluginContexts(),context);
			PluginsBpmDefXmlHandler bpmDefXmlHandler = (PluginsBpmDefXmlHandler) AppUtil.getBean(PluginsBpmDefXmlHandler.class);
			bpmDefXmlHandler.saveNodeXml(defId, nodeId, plugins);
			return new CommonResult<String>("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"保存失败："+e.getMessage());
		}
	}
	
}
