package com.hotent.bpm.persistence.util;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.defxml.entity.ext.BaseNode;
import com.hotent.bpm.defxml.entity.ext.Buttons;
import com.hotent.bpm.defxml.entity.ext.ExtPlugins;
import com.hotent.bpm.defxml.entity.ext.Form;
import com.hotent.bpm.defxml.entity.ext.FormInitSetting;
import com.hotent.bpm.defxml.entity.ext.Propers;
import com.hotent.bpm.defxml.entity.ext.Scripts;
import com.hotent.bpm.defxml.entity.ext.SignNode;
import com.hotent.bpm.defxml.entity.ext.SubProcessForm;
import com.hotent.bpm.defxml.entity.ext.SubTableRights;
import com.hotent.bpm.defxml.entity.ext.TransformRules;
import com.hotent.bpm.defxml.entity.ext.UserNode;
import com.hotent.bpm.defxml.entity.ext.VarDefs;

public class BpmDefAccessorUtil {
	
	/**
	 * 获取节点的节点ID
	 * @param obj
	 * @return  String
	 */
	public static String getNodeId(Object obj){
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			return baseNode.getBpmnElement();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getBpmnElement();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getBpmnElement();
		}
		return "";
	}
	
	/**
	 * 取得节点的流程变量。
	 * @param obj
	 * @return  VarDefs
	 */
	public static VarDefs getVarDefs(Object obj){
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			return baseNode.getVarDefs();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getVarDefs();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getVarDefs();
		}
		return null;
	}
	
	/**
	 * 获取节点表单。
	 * @param obj
	 * @return Form
	 */
	public static List<Form> getForm(Object obj){
		List<Form> list=new ArrayList<Form>();
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			Form frm= baseNode.getForm();
			Form mobileForm= baseNode.getMobileForm();
			if(frm!=null){
				list.add(frm);
			}
			if(mobileForm!=null){
				list.add(mobileForm);
			}
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			
			Form frm= userNode.getForm();
			Form mobileForm= userNode.getMobileForm();
			if(frm!=null){
				list.add(frm);
			}
			if(mobileForm!=null){
				list.add(mobileForm);
			}
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			Form frm= signNode.getForm();
			Form mobileForm= signNode.getMobileForm();
			if(frm!=null){
				list.add(frm);
			}
			if(mobileForm!=null){
				list.add(mobileForm);
			}
		}
		return list;
	}
	
	/**
	 * 取得子流程表单。
	 * @param obj
	 * @return SubProcessForm
	 */
	public static SubProcessForm getSubForm(Object obj){
		if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			
			return userNode.getSubProcessForm();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getSubProcessForm();
		}else if(obj instanceof BaseNode){
			BaseNode baseNode = (BaseNode)obj;
			return baseNode.getSubProcessForm();
		}
		return null;
	}
	
	/**
	 * 获取节点脚本。
	 * @param obj
	 * @return Scripts
	 */
	public static Scripts getScripts(Object obj){
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			return baseNode.getScripts();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getScripts();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getScripts();
		}
		return null;
	}
	
	
	
	/**
	 * 获取节点插件。
	 * @param obj
	 * @return 
	 * ExtPlugins
	 */
	public static ExtPlugins getNodeExtPlugins(Object obj){
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			
			return baseNode.getExtPlugins();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getExtPlugins();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getExtPlugins();
		}
		return null;
	}

	/**
	 * 取得流程节点按钮配置。
	 * @param obj
	 * @return 
	 * Buttons
	 */
	public static Buttons getButtons(Object obj){
		if(obj instanceof BaseNode){
			BaseNode userNode=(BaseNode)obj;
			return userNode.getButtons();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getButtons();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getButtons();
		}
		return null;
	}

	/**
	 * 获取节点跳转规则。
	 * @param obj
	 * @return TransformRules
	 */
	public static TransformRules getTransRules(Object obj){
		
		if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getTransformRules();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getTransformRules();
		}
		return null;
	}
	
	/**
	 * 获取节点的其他属性。
	 * @param obj
	 * @return  NodeProperties
	 */
	public static Propers getNodeProperties(Object obj){
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			return baseNode.getPropers();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getPropers();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getPropers();
		}
		return null;
	}
	
	/**
	 * 获取子表权限配置。
	 * @param obj
	 * @return 
	 * SubTableRights
	 */
	public static SubTableRights getSubTableRigths(Object obj){
		if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getSubTableRights();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getSubTableRights();
		}
		return null;
	}
	
	/**
	 * 取得表单数据设置。
	 * @param obj
	 * @return  FormInitSetting
	 */
	public static FormInitSetting getFormInitSetting(Object obj){
		if(obj instanceof BaseNode){
			BaseNode baseNode=(BaseNode)obj;
			return baseNode.getFormInitSetting();
		}
		else if(obj instanceof UserNode){
			UserNode userNode=(UserNode)obj;
			return userNode.getFormInitSetting();
		}
		else if(obj instanceof SignNode){
			SignNode signNode=(SignNode)obj;
			return signNode.getFormInitSetting();
		}
		return null;
	}
}
