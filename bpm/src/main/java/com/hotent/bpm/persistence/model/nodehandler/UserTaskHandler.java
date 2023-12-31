package com.hotent.bpm.persistence.model.nodehandler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.DefaultJumpRule;
import com.hotent.bpm.defxml.entity.ext.RightsItem;
import com.hotent.bpm.defxml.entity.ext.SubTableRights;
import com.hotent.bpm.defxml.entity.ext.TransformRule;
import com.hotent.bpm.defxml.entity.ext.TransformRules;
import com.hotent.bpm.defxml.entity.ext.VarDefs;
import com.hotent.bpm.defxml.entity.ext.VariableDef;
import com.hotent.bpm.model.var.DefaultBpmVariableDef;
import com.hotent.bpm.persistence.model.NodeHandler;
import com.hotent.bpm.persistence.util.BpmDefAccessorUtil;

public class UserTaskHandler implements NodeHandler {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(UserTaskHandler.class);

	@Override
	public void handNode(BaseBpmNodeDef nodeDef,Object baseNode) throws Exception {
		UserTaskNodeDef userNodeDef=(UserTaskNodeDef)nodeDef;
		//处理插件
		PluginContextUtil.handBaseNode(userNodeDef, baseNode);
		//处理子流程表单
		PluginContextUtil.handSubForm(userNodeDef, baseNode);
		//处理变量
		handVarDef(userNodeDef,baseNode);
		//跳转规则
		handJumpRule(userNodeDef,baseNode);
	
		
		//子表权限。
		handSubTableRights(userNodeDef,baseNode);
	}
	
	//子表权限。
	private void handSubTableRights(UserTaskNodeDef userNodeDef,Object baseNode){
		SubTableRights rights= BpmDefAccessorUtil.getSubTableRigths(baseNode);
		if(rights==null) return;
		List<RightsItem> rightsItems= rights.getRightsItem();
		if(BeanUtils.isEmpty(rightsItems)) return;
		for(RightsItem rightsItem:rightsItems){
			BpmSubTableRight right=new BpmSubTableRight();
			right.setNodeId(userNodeDef.getNodeId());
			right.setParentDefKey(rightsItem.getParentDefKey());
			right.setRightType(rightsItem.getRightType().value());
			right.setTableName(rightsItem.getTableName());
			right.setScript(rightsItem.getScript());
			
			userNodeDef.addBpmSubTableRight(right);
		}
	}

	
	
	
	/**
	 * 跳转规则
	 * @param userNodeDef
	 * @param userTask 
	 * void
	 */
	private void handJumpRule(UserTaskNodeDef userNodeDef,Object baseNode){
		List<DefaultJumpRule> jumpRuleList=new ArrayList<DefaultJumpRule>();
		TransformRules rules=BpmDefAccessorUtil.getTransRules(baseNode) ;
		if(rules==null) return ;

		List<TransformRule> list= rules.getRule();
		for(TransformRule rule:list){
			
			DefaultJumpRule jumpRule=new DefaultJumpRule(rule.getName(), rule.getTargetnode(),rule.getCondition());
			jumpRuleList.add(jumpRule);
		}
		userNodeDef.setJumpRuleList(jumpRuleList);
	}
	
	/**
	 * 处理流程变量定义。
	 * @param userNodeDef
	 * @param userTask 
	 * void
	 */
	private void handVarDef(UserTaskNodeDef userNodeDef,Object baseNode){
		List<BpmVariableDef> list=new ArrayList<BpmVariableDef>();
		
		VarDefs defs=  BpmDefAccessorUtil.getVarDefs(baseNode);
		if(defs==null) return;
		
		List<VariableDef> defList= defs.getVariableDef();		
		
		if(defList==null) return;
		
		for(VariableDef v:defList){
			BpmVariableDef varDef=new  DefaultBpmVariableDef();
			varDef.setName(v.getName());
			varDef.setNodeId(userNodeDef.getNodeId());
			varDef.setVarKey(v.getKey());
			varDef.setDataType(v.getType().value());
			Object val=DefaultBpmVariableDef.getValue(v.getType().value(), v.getDefaultVal());
			varDef.setDefaultVal(val);
			Boolean isRequired=v.isIsRequired();
			//默认为true。
			if(isRequired==null) isRequired=true;
			varDef.setRequired(isRequired);
			varDef.setDescription(v.getDescription());
			list.add(varDef);
		}
		userNodeDef.setVariableList(list);
	}
	
	
	

}
