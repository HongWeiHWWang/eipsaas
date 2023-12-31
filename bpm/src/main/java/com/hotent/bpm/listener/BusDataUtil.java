package com.hotent.bpm.listener;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.DataType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.SkipResult;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.DataObjectHandler;
import com.hotent.bpm.engine.def.BpmDefUtil;
import com.hotent.bpm.exception.HandlerException;
import com.hotent.bpm.model.BoDataModifyRecord;
import com.hotent.bpm.persistence.manager.BoDataModifyRecordManager;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.util.BoDataUtil;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.bpm.util.HandlerUtil;
import com.hotent.table.operator.ITableOperator;
import com.hotent.table.operator.impl.mysql.MySQLTableOperator;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

public class BusDataUtil {


	/**
	 * 处理bo数据到业务中间关联表。
	 *
	 * @param model
	 * @param boResult
	 *            void
	 */
	public static void handlerBusinessLink(BpmProcessInstance instance,List<ObjectNode> boResults,String saveMode) {
		BpmBusLinkManager bpmBusLinkManager =AppUtil.getBean(BpmBusLinkManager.class);


		for(ObjectNode result:boResults){

			ObjectNode boEnt=(ObjectNode) result.get("boEnt");

			boolean isNumber= BeanUtils.isNotEmpty(boEnt.get("pkType"))&&"number".equals(boEnt.get("pkType").asText());

			String action=result.get("action").asText();
			if("add".equals(action)){

				BpmBusLink busLink = BpmUtil.buildBusLink(instance,result,saveMode);
				//创建分区。
				createPartition(busLink.getFormIdentify());

				bpmBusLinkManager.create(busLink);
			}

			else if("del".equals(action)) {

				bpmBusLinkManager.delByBusinesKey(result.get("pk").asText(), result.get("boEnt").get("name").asText(), isNumber);
			}
			//更新暂不做处理。
			else if("upd".equals(action)){
				// 用已有的表单数据启动流程
				BpmBusLink bbl = bpmBusLinkManager.getByBusinesKey(result.get("pk").asText(), isNumber);
				if(BeanUtils.isEmpty(bbl)){
					BpmBusLink busLink = BpmUtil.buildBusLink(instance,result,saveMode);
					//创建分区。
					createPartition(busLink.getFormIdentify());
					bpmBusLinkManager.create(busLink);
				}

			}
		}
	}

	/**
	 * 是否支持分区。
	 */
	private static int  supportPart=-1;

	private static Set<String> partions= Collections.synchronizedSet(new HashSet<String>());

	private static final String tableName="BPM_BUS_LINK";

	/**
	 * 创建分区。
	 * @param partName
	 */
	private static void createPartition(String partName){
		if(StringUtil.isEmpty(partName)) return;

		ITableOperator tableOperator=(ITableOperator)AppUtil.getBean("tableOperator");
		if(tableOperator==null) return;
		// TODO MySQL动态创建分区的语句有误，在MySQL中不能对字符串类型的字段创建 LIST分区
		if(tableOperator instanceof MySQLTableOperator){
			return;
		}
		if(supportPart==-1){
			//表是否支持分区。
			boolean isSupport=tableOperator.supportPartition(tableName);
			supportPart=isSupport?1:0;
		}

		if(supportPart==0) return;
		//是否存在指定的分区。
		if(partions.contains(partName)) return;

		boolean isPartExist=tableOperator.isExsitPartition(tableName, partName);

		//添加缓存partions
		partions.add(partName);

		if(isPartExist) return;

		tableOperator.createPartition(tableName, partName);
	}



	/**
	 * 处理多个业务主键的业务中间表数据问题。
	 * @param cmd
	 */
	public static void handExt(ActionCmd cmd){
		Map<String,String> pairs= cmd.getDataPair();
		if(BeanUtils.isNotEmpty(pairs)) return ;
		//添加中间表。
		addBusLink(cmd);
	}


	//构建BPM_BUS_LINK;
	private static void addBusLink(ActionCmd cmd){
		BpmProcessInstanceManager bpmProcessInstanceManager=AppUtil.getBean(BpmProcessInstanceManager.class);

		BpmBusLinkManager bpmBusLinkManager=AppUtil.getBean(BpmBusLinkManager.class);

		String instId=cmd.getInstId();

		Map<String,String> pairs=cmd.getDataPair();

		DataType dataType= cmd.getPkDataType();

		BpmProcessInstance instance= bpmProcessInstanceManager.get(instId);

		boolean isNumber=!DataType.STRING.equals(dataType);

		for (Map.Entry<String, String> entry : pairs.entrySet()) {
			String key=entry.getKey();
			String val=entry.getValue();

			BpmBusLink bpmBusLink = bpmBusLinkManager.getByBusinesKey(val, key, isNumber);
			if(bpmBusLink!=null) continue;

			BpmBusLink busLink=BpmUtil.buildBusLink(instance);
			busLink.setIsMain(1);
			busLink.setFormIdentify(key);

			if(!isNumber){
				busLink.setBusinesskeyStr(val);
			}
			else{
				busLink.setBusinesskey(Long.parseLong( val));
			}
			//添加分区。
			createPartition(key);

			bpmBusLinkManager.create(busLink);
		}
	}

	/**
	 * 支持处理器。
	 * @param properties
	 * @param actionCmd
	 * @param isBefore
	 */
	public static void executeHandler(NodeProperties properties,ActionCmd actionCmd,boolean isBefore ){
		if(properties==null) return;


		String handler=isBefore?properties.getPrevHandler():properties.getPostHandler();
		if(StringUtil.isEmpty(handler)) return;

		try{
			HandlerUtil.invokeHandler(actionCmd,handler );
		}
		catch(Exception ex){
			throw new HandlerException(ex.getMessage(), ex.getCause());
		}
	}

	/**
	 * 通过流程实例ID更新BO数据
	 * @param instanceId 流程实例ID
	 * @param boDatas bo实例
	 */
	public static void updateBoData(String instanceId, ObjectNode...boDatas){
		BpmBusLinkManager bpmBusLinkManager = AppUtil.getBean(BpmBusLinkManager.class);
		FormFeignService formRestfulService=AppUtil.getBean(FormFeignService.class);

		for (ObjectNode boData : boDatas) {
			ObjectNode boDef = (ObjectNode) boData.get("boDef");
			ObjectNode boEnt = (ObjectNode) boData.get("boEnt");
			String pkKey = boEnt.get("pkKey").asText();
			String pkType = boEnt.get("pkType").asText();
			Object pkValue = boData.get(pkKey).asText();
			if(BeanUtils.isEmpty(pkValue)) throw new RuntimeException("要更新的bo没有主键数据");
			String pk = pkValue.toString();

			BpmBusLink businesKey = bpmBusLinkManager.getByBusinesKey(pk, "number".equals(pkType));
			String saveMode = businesKey.getSaveMode();

			ObjectNode formRestParams=JsonUtil.getMapper().createObjectNode();
			formRestParams.put("boid", pk);
			formRestParams.put("defId", boDef.get("id").asText());
			formRestParams.set("boData", boData);
			formRestParams.put("saveType", saveMode);
			formRestParams.put("flowDefId", (String) ContextThreadUtil.getCommuVar("defId",""));
			formRestParams.put("nodeId",(String) ContextThreadUtil.getCommuVar("nodeId",""));
			formRestParams.put("parentDefKey",(String) ContextThreadUtil.getCommuVar("parentDefKey", BpmConstants.LOCAL));

			List<ObjectNode> boResult = formRestfulService.handlerBoData(formRestParams);
			BusDataUtil.handlerBodataModify(boResult);
		}
	}

	private  static void handlerBodataModify(List<ObjectNode> boResult ){
		String modifyRes = "";
		String refId = "";
		String data = "";
		for (ObjectNode objectNode : boResult) {
			if (objectNode.hasNonNull("modifyDetail") && StringUtil.isNotEmpty(objectNode.get("modifyDetail").asText())) {
				modifyRes = objectNode.get("modifyDetail").asText();
				data = objectNode.get("data").asText();
				refId = objectNode.get("pk").asText();
				break;
			}
		}
		if (StringUtil.isEmpty(modifyRes)) {
			return;
		}
		BaseActionCmd actionCmd = (BaseActionCmd) ContextThreadUtil.getActionCmd();
		if(BeanUtils.isEmpty(actionCmd)){
			return;
		}
		String skipType =(String) actionCmd.getTransitVars(BpmConstants.BPM_SKIP_TYPE);
		//如果是跳过则不记录数据变动
		if (BeanUtils.isNotEmpty(skipType)) {
			return;
		}
		BoDataModifyRecordManager boDataModifyRecordManager = AppUtil.getBean(BoDataModifyRecordManager.class);
		BoDataModifyRecord record = new BoDataModifyRecord();
		//添加外键
		if(StringUtil.isNotEmpty(refId)) {
			record.setRefId(refId);
		}
		if (actionCmd instanceof TaskFinishCmd) {
			record.setReason(((TaskFinishCmd)actionCmd).getApprovalOpinion());
		}
		record.setInstId(actionCmd.getInstId());
		record.setModifyTime(LocalDateTime.now());
		IUser currentUser = ContextUtil.getCurrentUser();
		record.setUserId(currentUser.getUserId());
		record.setUserName(currentUser.getFullname());
		DefaultBpmTask task =(DefaultBpmTask) actionCmd.getTransitVars(BpmConstants.BPM_TASK);
		if (BeanUtils.isNotEmpty(task)) {
			record.setTaskId(task.getTaskId());
			record.setNodeId(task.getNodeId());
			record.setTaskName(task.getName());
		}
		record.setDetail(modifyRes);
		record.setData(data);
		boDataModifyRecordManager.create(record);
	}


	/**
	 * 处理bo数据。
	 * @param instance
	 * @param cmd
	 * @throws Exception
	 */
	public static void handSaveBoData(BpmProcessInstance instance,  ActionCmd cmd) throws Exception  {
		String skipType = (String) cmd.getTransitVars().get(BpmConstants.BPM_SKIP_TYPE);
		if(SkipResult.SKIP_ALL.equals(skipType) || SkipResult.SKIP_SAME_USER.equals(skipType)){//无条件跳过 或者相同执行人跳过不执行更新数据的方法
			return;
		}
		Object taskObj = cmd.getTransitVars(BpmConstants.BPM_TASK);
		BpmTask bpmTask = null;

		if(BeanUtils.isNotEmpty(taskObj) && taskObj instanceof BpmTask) {
			bpmTask = (BpmTask)taskObj;
			// 如果是启动流程时跳过第一个节点时 不再处理bo数据
			if(bpmTask.getSkipResult().isSkipTask()) {
				return;
			}
		}
		String boJson=cmd.getBusData();
		FormFeignService formRestfulService=AppUtil.getBean(FormFeignService.class);
		if(StringUtil.isEmpty(boJson)) {
			BoDataService boDataService = AppUtil.getBean(BoDataService.class);
			List<ObjectNode> boDatas = boDataService.getDataByInst(instance);
			Map<String,ObjectNode> boMap=new HashMap<String, ObjectNode>();
			for(ObjectNode data:boDatas){
				String code=data.get("boDefAlias").asText();
				ObjectNode bodefByAlias = formRestfulService.getBodefByAlias(code);
				if(BeanUtils.isNotEmpty(bodefByAlias) && "forbidden".equals(bodefByAlias.get("status").asText())) throw new BaseException("该流程绑定的表单所对应的业务建模已被禁用，无法启动。");
				boMap.put(code, data);
			}
			// 将BO放入cmd上下文中。
			cmd.getTransitVars().put(BpmConstants.BO_INST, boMap);
			return;
		}

		BpmBusLinkManager  bpmBusLinkManager=AppUtil.getBean(BpmBusLinkManager.class);
		DataObjectHandler dataObjectHandler=AppUtil.getBean(DataObjectHandler.class);

		DefaultBpmProcessDefExt bpmProcessDefExt = BpmDefUtil.getProcessExt(instance);

		ObjectNode jsonObj = (ObjectNode) JsonUtil.toJsonNode(boJson);
		// 验证BO数据。
		BoDataUtil.validBo(bpmProcessDefExt, jsonObj);
		// BO Map数据。
		Map<String, ObjectNode> jsonMap = BoDataUtil.getMap(jsonObj);
		Set<String> set=jsonMap.keySet();
		String alias="";
		for (String str:set) {
			alias=str;
		}
		String saveType="database";
		if(StringUtil.isNotEmpty(alias)){
			boolean ref=formRestfulService.getSupportDb(alias);
			if(!ref){
				saveType="boObject";
			}
		}
		List<ProcBoDef> list = bpmProcessDefExt.getBoDefList();

		String instId=instance.getId();

		Map<String, ObjectNode> boMap = new HashMap<String, ObjectNode>();

		List<ObjectNode> boDatas=new ArrayList<ObjectNode>();

		Map<String,BpmBusLink> linkMap=bpmBusLinkManager.getMapByInstId(instId);

		for (ProcBoDef boDef : list) {
			String key = boDef.getKey();
			ObjectNode bodefByAlias = formRestfulService.getBodefByAlias(key);
			if(BeanUtils.isNotEmpty(bodefByAlias) && "forbidden".equals(bodefByAlias.get("status").asText())) throw new BaseException("该流程绑定的表单所对应的业务建模已被禁用，无法启动。");
			if(!jsonMap.containsKey(key)) continue;
			ObjectNode json = jsonMap.get(key);

			ObjectNode boEnt=formRestfulService.getMainBOEntByDefAliasOrId("",bodefByAlias.get("id").asText());
			//BO数据。
			ObjectNode curData= (ObjectNode) BoDataUtil.transJSON(json);
			curData.set("boDef", bodefByAlias);
			curData.set("boEnt", boEnt);
			curData.set("data", json);

			boDatas.add(curData);
		}

		if(BeanUtils.isNotEmpty(bpmTask)){
			dataObjectHandler.handSaveData(instance,bpmTask.getNodeId(), boDatas);
		}else{
			dataObjectHandler.handSaveData(instance, boDatas);
		}

		for (ObjectNode boData : boDatas) {
			ObjectNode boEnt =(ObjectNode) boData.get("boEnt");
			ObjectNode def= (ObjectNode) boData.get("boDef");
			String id="";
			//新增
			if(BeanUtils.isNotEmpty(linkMap)){
				BpmBusLink busLink=linkMap.get(def.get("alias").asText());
				//取得主键。
				if(busLink!= null){
					boolean isNumber="number".equals(boEnt.get("pkType").asText());
					id=isNumber? busLink.getBusinesskey().toString():  busLink.getBusinesskeyStr();
				}
			}

			ObjectNode formRestParams=JsonUtil.getMapper().createObjectNode();
			formRestParams.put("boid", id);
			formRestParams.put("defId", def.get("id").asText());
			formRestParams.set("boData", boData);
			formRestParams.put("saveType", saveType);
			formRestParams.put("flowDefId", BeanUtils.isNotEmpty(instance)?instance.getProcDefId():"");
			formRestParams.put("nodeId", BeanUtils.isNotEmpty(bpmTask)?bpmTask.getNodeId():"");
			formRestParams.put("parentDefKey",(String) ContextThreadUtil.getCommuVar("parentDefKey", BpmConstants.LOCAL));

			List<ObjectNode> boResults= formRestfulService.handlerBoData(formRestParams);
			//处理业务中间表数据信息。
			BusDataUtil.handlerBodataModify(boResults);
			BusDataUtil.handlerBusinessLink(instance, boResults,saveType);
			boMap.put(def.get("alias").asText(), boData);
		}
		// 将BO放入cmd上下文中。
		cmd.getTransitVars().put(BpmConstants.BO_INST, boMap);
	}

	/**
	 * 将前端传入的表单数据转化成List<BoData>
	 * @param bpmProcessDefExt 流程定义扩展类
	 * @param boJson 表单数据
	 * @return
	 * @throws IOException
	 */
	public static List<ObjectNode> transFormDataToBoData(DefaultBpmProcessDefExt  bpmProcessDefExt,  String boJson) throws IOException  {

		FormFeignService formRestfulService=AppUtil.getBean(FormFeignService.class);

		ObjectNode jsonObj = (ObjectNode) JsonUtil.toJsonNode(boJson);
		// 验证BO数据。
		BoDataUtil.validBo(bpmProcessDefExt, jsonObj);
		// BO Map数据。
		Map<String, ObjectNode> jsonMap = BoDataUtil.getMap(jsonObj);

		List<ProcBoDef> list = bpmProcessDefExt.getBoDefList();

		List<ObjectNode> boDatas=new ArrayList<ObjectNode>();

		for (ProcBoDef boDef : list) {
			String key = boDef.getKey();
			ObjectNode boDefByAlias = formRestfulService.getBodefByAlias(key);
			if(BeanUtils.isNotEmpty(boDef) && "forbidden".equals(boDefByAlias.get("status").asText())) throw new BaseException("该流程绑定的表单所对应的业务建模已被禁用，无法启动。");
			if(!jsonMap.containsKey(key)) continue;
			ObjectNode json = jsonMap.get(key);
			ObjectNode boEnt=formRestfulService.getMainBOEntByDefAliasOrId("",boDefByAlias.get("id").asText());
			//BO数据。
			ObjectNode curData= (ObjectNode) BoDataUtil.transJSON(json);
			curData.set("boDef", boDefByAlias);
			curData.put("boDefAlias", boDefByAlias.get("alias").asText());
			curData.set("boEnt", boEnt);
			curData.set("data", json);
			boDatas.add(curData);
		}
		return boDatas;
	}
}
