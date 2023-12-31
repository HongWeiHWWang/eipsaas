package com.hotent.runtime.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeForm;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.service.BpmFormService;
import com.hotent.bpm.api.service.BpmTaskService;
import com.hotent.bpm.engine.form.BpmFormFactory;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.DefaultBpmDefinitionAccessor;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.util.PublishAutoTestEventUtil;
import com.hotent.runtime.constant.SysObjTypeConstants;
import com.hotent.runtime.dao.BpmTestCaseDao;
import com.hotent.runtime.manager.BpmTestCaseManager;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.model.BpmTestCase;
import com.hotent.runtime.params.BpmNodeDefVo;
import com.hotent.runtime.params.StartFlowParamObject;
import com.hotent.runtime.params.StartResult;
import com.hotent.runtime.params.TestCaseBaseInfoVo;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 
 * <pre> 
 * 描述：流程的测试用例设置 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-01-15 16:39:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTestCaseManager")
public class BpmTestCaseManagerImpl extends BaseManagerImpl<BpmTestCaseDao, BpmTestCase> implements BpmTestCaseManager{
	
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	IProcessManager processService;
	@Resource
	BpmTaskService bpmTaskService;
	@Resource
	IUserService userService;
	@Resource
	DefaultBpmDefinitionAccessor defaultBpmDefinitionAccessor;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	
	@Override
	@Transactional
	public void startTest(String ids) throws Exception {
		QueryFilter<BpmTestCase> queryFilter = QueryFilter.<BpmTestCase>build()
											 .withDefaultPage()
											 .withQuery(new QueryField("id_", Arrays.asList(ids.split(",")), QueryOP.IN));
		PageList<BpmTestCase> pageList = super.query(queryFilter);
		for (BpmTestCase bpmTestCase : pageList.getRows()) {
			startFlow(bpmTestCase);
		}
	}
	
	
	@Override
	@Transactional
	public void doNext(String id) {
		ContextThreadUtil.putCommonVars("skipDebugger", true);
		PublishAutoTestEventUtil.publishAutoTestEvent(id);
	}
	
	@Override
	public TestCaseBaseInfoVo getBaseInfo(String defKey) throws Exception {
	
		DefaultBpmDefinition bpmDefinition = bpmDefinitionManager.getMainByDefKey(defKey);
		String defId = bpmDefinition.getId();
		
		Map<String, List<BpmNodeDefVo>> jo = new HashMap<String, List<BpmNodeDefVo>>();
		
		ArrayNode defKeys = JsonUtil.getMapper().createArrayNode();
		
		getAllNodeId(jo,defKeys,defKey);
		
		BpmFormService  bpmFormService =BpmFormFactory.getFormService(FormType.PC);
		@SuppressWarnings("unused")
		BpmNodeForm nodeForm = bpmFormService.getByDefId(defId);
		
		//mark 调接口
//		BpmForm bpmForm = bpmFormManager.getMainByFormKey(nodeForm.getForm().getFormValue());
		String formId = nodeForm.getForm().getId();
//		if(BeanUtils.isNotEmpty(bpmForm)){
//			formId = bpmForm.getId();
//		}
		
		
		return new TestCaseBaseInfoVo(formId, jo, defKeys);
	}
	
	@Override
	public ObjectNode getReportData(String ids) throws Exception {
		QueryFilter filter = QueryFilter.<BpmTestCase>build()
										.withDefaultPage()
										.withQuery(new QueryField("id_", Arrays.asList(ids.split(",")), QueryOP.IN));
		PageList<BpmTestCase> pageList = super.query(filter);
		
		ObjectNode jo = JsonUtil.getMapper().createObjectNode();
		
		ObjectNode procInstJo = JsonUtil.getMapper().createObjectNode();
		
		for (BpmTestCase bpmTestCase : pageList.getRows()) {
			List<String> entityIds = new ArrayList<String>();
			entityIds.add(SysObjTypeConstants.BPMX_AUTO_TEST+bpmTestCase.getId());
			QueryFilter queryFilter = QueryFilter.<DefaultBpmProcessInstance>build().withQuery(new QueryField("sys_code_", entityIds, QueryOP.IN));
			PageList<DefaultBpmProcessInstance> query = bpmProcessInstanceManager.query(queryFilter);
			List<DefaultBpmProcessInstance> instances = query.getRows();
			ObjectNode _tmp = JsonUtil.getMapper().createObjectNode();
			for (DefaultBpmProcessInstance defaultBpmProcessInstance : instances) {
				
				if("end".equals(defaultBpmProcessInstance.getStatus())){
					_tmp.put("end", JsonUtil.getInt(_tmp, "end",0)+1);
				}
				
				if("manualend".equals(defaultBpmProcessInstance.getStatus())){
					_tmp.put("endProcess", JsonUtil.getInt(_tmp, "endProcess",0)+1);
				}
				if("running".equals(defaultBpmProcessInstance.getStatus())){
					_tmp.put("unend", JsonUtil.getInt(_tmp, "unend",0)+1);
				}
				
			}
			procInstJo.set(bpmTestCase.getId(), _tmp);
			
		}
		ArrayNode xAxisJa = JsonUtil.getMapper().createArrayNode();
		ArrayNode endJa = JsonUtil.getMapper().createArrayNode();
		ArrayNode unendJa = JsonUtil.getMapper().createArrayNode();
		ArrayNode endProcessJa = JsonUtil.getMapper().createArrayNode();
		
		for (BpmTestCase bpmTestCase : pageList.getRows()) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(procInstJo.get(bpmTestCase.getId()));
			xAxisJa.add(bpmTestCase.getName());
			endJa.add(JsonUtil.getInt(obj,"end",0));
			unendJa.add(JsonUtil.getInt(obj,"unend",0));
			endProcessJa.add(JsonUtil.getInt(obj,"endProcess",0));
		}
		
		jo.set("xAxis", xAxisJa);
		jo.set("end", endJa);
		jo.set("unend", unendJa);
		jo.set("endProcess", endProcessJa);
		
		return jo;
	}
	
	private void getAllNodeId(Map<String, List<BpmNodeDefVo>> jo,ArrayNode defKeys, String defKey) throws Exception {
		
		DefaultBpmDefinition bpmDefinition = bpmDefinitionManager.getMainByDefKey(defKey);
		String defId = bpmDefinition.getId();
		List<BpmNodeDef> allNodeDef = defaultBpmDefinitionAccessor.getAllNodeDef(defId);
		
		
		List<BpmNodeDefVo> ja = new ArrayList<BpmNodeDefVo>();
		ObjectNode defInfo = JsonUtil.getMapper().createObjectNode();
		defInfo.put("defKey", defKey);
		defInfo.put("defName", bpmDefinition.getName());
		defKeys.add(defInfo);
		
		for (BpmNodeDef bpmNodeDef : allNodeDef) {
			if(bpmNodeDef instanceof UserTaskNodeDef || bpmNodeDef instanceof SignNodeDef){
				BpmNodeDefVo vo =  BpmNodeDefVo.parseVo(bpmNodeDef);
				ja.add(vo);
			}
			
			if( bpmNodeDef instanceof CallActivityNodeDef ){
				CallActivityNodeDef callActivityNodeDef = (CallActivityNodeDef) bpmNodeDef;
				getAllNodeId(jo,defKeys, callActivityNodeDef.getFlowKey()); 
			}
		}
		jo.put(defKey, ja);
		
	}
	
	private Set<String> getStartors(String startor) throws Exception{
		Set<String> set = new HashSet<String>();
		if(StringUtil.isEmpty(startor)) return set;
		ArrayNode parseArray = (ArrayNode) JsonUtil.toJsonNode(startor);
		
		List<IUser> userList = new ArrayList<IUser>();
		
		for (Object object : parseArray) {
			ObjectNode jo = (ObjectNode) JsonUtil.toJsonNode(object);
			if(!jo.has("id")) continue;
			String ids = jo.get("id").asText();
			String[] split = ids.split(",");
			for (String id : split) {
				if("user".equals(jo.get("type").asText())){
					IUser user = userService.getUserById(id);
					if(BeanUtils.isEmpty(user)) continue;
					userList.add(user);
				}else{
					List<IUser> users = userService.getUserListByGroup(jo.get("type").asText(), id);
					if(BeanUtils.isEmpty(users)) continue;
					userList.addAll(users);
				}
			}
			
		}
		
		for (IUser iUser : userList) {
			if(BeanUtils.isEmpty(iUser) || StringUtil.isEmpty(iUser.getAccount()) ) continue;
			set.add(iUser.getAccount());
		}
		
		return set;
	}
	
	// 根据测试用例启动流程
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void startFlow(BpmTestCase bpmTestCase) throws Exception {
		String account= bpmTestCase.getStartorAccount();
		 
		 String startor = bpmTestCase.getStartor();
		 Set<String> startors = getStartors(startor);
		 if(StringUtil.isNotEmpty(account)){
			 startors.add(account);
		 }
		 if(BeanUtils.isEmpty(startors)){
			 throw new RuntimeException("请设置流程启动人员");
		 }
		 
		 // 创建一个线程池  
	     ExecutorService pool = Executors.newFixedThreadPool(startors.size());  
	     // 创建多个有返回值的任务  
	     List<Future> list = new ArrayList<Future>();  
	     for (String _account : startors) {
		      Callable c = new MyCallable(bpmTestCase,_account,SecurityContextHolder.getContext().getAuthentication());  
		      // 执行任务并获取Future对象  
		      Future f = pool.submit(c);  
		      list.add(f);  
	     }  

		 // 获取所有并发任务的运行结果  
		 for (Future f : list) {  
		   // 从Future对象上获取任务的返回值，并输出到控制台  
		   String instId = f.get().toString();
		   System.out.println(">>>" + f.get().toString());  
		   if(BeanUtils.isEmpty(instId))continue;
		   
	       PublishAutoTestEventUtil.publishAutoTestEvent(instId);
		 } 
		 // 关闭线程池  
		 pool.shutdown(); 
		 
	}
	
}


class MyCallable implements Callable<Object> {  
	private Logger logger = LoggerFactory.getLogger(BpmTestCaseManagerImpl.class);
	private BpmTestCase bpmTestCase;  
	private String account;
	Authentication authentication;
	
	MyCallable(BpmTestCase bpmTestCase,String account,Authentication authentication) {  
	  this.bpmTestCase = bpmTestCase;  
	  this.account = account;
	  this.authentication = authentication;
	}  
	
	public Object call() throws Exception {  
	  logger.info(">>>" + account + "启动流程仿真");  
	  LocalDateTime dateTmp1 = LocalDateTime.now();  
	  String instId = startFlow(); 
	  Thread.sleep(200);
	  LocalDateTime dateTmp2 = LocalDateTime.now();  
	  long time = TimeUtil.getTimeMillis(dateTmp2) - TimeUtil.getTimeMillis(dateTmp1);  
	  logger.info(">>>" + account + "任务终止; 花费时间为： " + time);  
	  return instId;  
	}
	
	
	private String startFlow(){

		 StartFlowParamObject startFlowParamObject = new StartFlowParamObject();
		 startFlowParamObject.setAccount(account);
		 startFlowParamObject.setFlowKey(bpmTestCase.getDefKey());
		 startFlowParamObject.setSubject("流程仿真测试启动流程["+bpmTestCase.getName()+"]");
		 try {
			startFlowParamObject.setData(Base64.getBase64(bpmTestCase.getBoFormData()));
			startFlowParamObject.setSysCode(SysObjTypeConstants.BPMX_AUTO_TEST+bpmTestCase.getId());
			IProcessManager processService = AppUtil.getBean(IProcessManager.class);
			StartResult start = new StartResult("");
			SecurityContextHolder.getContext().setAuthentication(authentication);
			start = processService.start(startFlowParamObject);
			if(start.getState() && StringUtil.isNotEmpty(start.getInstId()) ){
				logger.debug("instId:"+start.getInstId());
				
				PublishAutoTestEventUtil.publishAutoTestEvent(start.getInstId());
				
				return start.getInstId();
			}else{
				throw new RuntimeException("启动流程仿真测试失败");
			}
		} catch ( Exception e1) {
			e1.printStackTrace();
		}
		return "";
	
	}
	
}