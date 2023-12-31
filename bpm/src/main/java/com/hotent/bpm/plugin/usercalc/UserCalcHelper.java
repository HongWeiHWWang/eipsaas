package com.hotent.bpm.plugin.usercalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.exception.UserCalcException;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IOrgService;

/**
 * 用户计算帮助类
 * @author miao
 *
 */
public class UserCalcHelper {
	/**
	 * 计算流程变量值       将参数和值进行匹配 （将流程变量的值抽取出来）
	 * @param executorVar
	 * @param pluginSession
	 * @param turnKeys2Ids 是否将key转成 Id ，account类型的转成id返回
	 * @return  userIds  or  groupIds
	 */
	public static List<String> calcVarValue(ExecutorVar executorVar,BpmUserCalcPluginSession pluginSession,boolean turnKeys2Ids){
		Map<String, Object> vars= pluginSession.getVariables();
		IOrgService orgEngine= pluginSession.getOrgEngine();
		
		List<String> ids = new ArrayList<String>();  /// 需要返回的 id  List
		
		//预览模式     (所有参数都是ID)
		if(AbstractUserCalcPlugin.isPreviewMode()){
			String Id = (String) vars.get(executorVar.getName());
			ids.add(Id);
			return ids;
		}
		
		// 非预览模式
		String PK = ""; //从流程变量，或者bo中获取值 中间变量
		
		//非预览模式
		if(ExecutorVar.SOURCE_BO.equals(executorVar.getSource())) {
            String[] BOData = executorVar.getName().split("\\.");
            if (BOData.length != 2 && BOData.length != 3) throw new UserCalcException("BO[" + executorVar.getName() + "]数据 格式不合法");

            Map<String, ObjectNode> boMap = BpmContextUtil.getBoFromContext();
            if (BeanUtils.isNotEmpty(boMap)) {
                ObjectNode boData = boMap.get(BOData[0]);
                if (BOData.length == 2){
                	PK = boData.get(BOData[1]).asText();
                }else if (BOData.length == 3){
                	try {
						if(BeanUtils.isNotEmpty(boData.get("subMap"))){
							String[] subnames = BOData[1].split("sub_");
							ArrayNode subs = (ArrayNode) boData.get("subMap").get(subnames[1]);
							for (JsonNode jsonNode : subs) {
								if(BeanUtils.isNotEmpty(jsonNode.get(BOData[2])) && !"null".equals(jsonNode.get(BOData[2]).asText())){
									if(StringUtil.isNotEmpty(PK)){
										PK += ",";
									}
									PK += jsonNode.get(BOData[2]).asText();
								}
							}
						}
					} catch (Exception e) {
						throw new UserCalcException("BO[" + executorVar.getName() + "]数据 格式不合法");
					}
                	
                }
            }else if(BeanUtils.isEmpty(boMap) && vars.containsKey(BpmConstants.PROCESS_INST_ID)) {
                try {
                    String instId = vars.get(BpmConstants.PROCESS_INST_ID).toString();
                    BpmProcessInstanceManager bpmProcessInstanceManager = AppUtil.getBean(BpmProcessInstanceManager.class);
                    DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
                    if (BeanUtils.isNotEmpty(instance)) {
                        BoDataService boDataService = AppUtil.getBean(BoDataService.class);
                        List<ObjectNode> list = boDataService.getDataByInst(instance);
                        if (BeanUtils.isNotEmpty(list)) {
                            PK = list.get(0).get("data").get(BOData[1]).asText();
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }else if(ExecutorVar.SOURCE_FLOW_VAR.equals(executorVar.getSource())){  //流程变量
			PK =(String) vars.get(executorVar.getName());
		}
		if(StringUtil.isEmpty(PK)) return Collections.emptyList(); 
		String [] PKs=PK.split(",");
		
		//如果是固定值，返回值
		if(executorVar.getExecutorType().equals("fixed")){
			ids.addAll(Arrays.asList(PKs));
			return ids;
		}
		
		//用户参数
		if(ExecutorVar.EXECUTOR_TYPE_USER.equals(executorVar.getExecutorType())){
			if("userId".equals(executorVar.getUserValType()) || !turnKeys2Ids){
				ids.addAll(Arrays.asList(PKs));
			} //为账号且需要将账号转换成ID
			else {
				for(String account : PKs){
					IUser u = orgEngine.getUserService().getUserByAccount(account);
					if(u!=null) ids.add(u.getUserId());
				}
			}
		 //组参数
		 }else{
			//id的形式的数据
			if("groupId".equals(executorVar.getGroupValType()) || !turnKeys2Ids)
				ids.addAll(Arrays.asList(PKs));
			//key并且需要将key转换成ID
			else{
				String dimension = executorVar.getDimension();
				for(String groupKey : PKs){
					IGroup group = orgEngine.getUserGroupService().getGroupByIdOrCode(dimension,groupKey);
					if(group!=null) ids.add(group.getGroupId());
				}
			}
		 }
		
		return ids;
	}
}
