package com.hotent.bpm.plugin.usercalc.grouprel.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;
import com.hotent.bpm.plugin.usercalc.grouprel.def.GroupRelPluginDef;
import com.hotent.bpm.plugin.usercalc.grouprel.runtime.GroupRelPlugin;

public class GroupRelPluginContext extends AbstractUserCalcPluginContext {
	
//	<?xml version="1.0" encoding="UTF-8"?>
//	<groupRel xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/groupRel"
//	    groupType=""  source="" logicCal="" extract="">
//	    <groupVar name=""/>
//	    <groups groupKey="" groupName=""/>
//	    <userVar name=""/>
//	    
//	</groupRel>


	/**
	 * 
	 */
	private static final long serialVersionUID = -6084686546165511275L;

	@Override
	public String getDescription() {
		GroupRelPluginDef def=(GroupRelPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		String source=def.getSource();
		StringBuffer sb=new StringBuffer();
		if("start".equals(source)){
			sb.append("发起人");
		}
		else if("prev".equals(source)){
			sb.append("上一步执行人");
		}
		else if("var".equals(source)){
			sb.append(def.getVar().getSource()+"["+def.getVar().getExecutorType()+":"+def.getVar().getName()+"]");
		}
		else if("spec".equals(source)){
			sb.append("指定用户组");
			sb.append("[");
			sb.append(def.getGroupName());
			sb.append("]");
		}
		if(def.isSupportRelation()){
			sb.append("关系类型[");
			sb.append(def.getRelationTypeName());
			sb.append("]");
		}
		
		return sb.toString();
	}

	@Override
	public String getTitle() {
		return "组关系";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return GroupRelPlugin.class;
	}
	
//	<?xml version="1.0" encoding="UTF-8"?>
//	<groupRel xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/groupRel"
//	    groupType="" source="" logicCal="" extract=""  relationType="" supportRelation="true">
//	    <groupVar name=""/>
//	    <groups groupKey="" groupName=""/>
//	    <userVar name=""/>
//	    
//	</groupRel>

	@Override
	public String getPluginXml() {
		
		GroupRelPluginDef def=(GroupRelPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		StringBuffer sb=new StringBuffer();
		sb.append("<groupRel xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/groupRel\" ");
		sb.append("   source=\""+ def.getSource() 
				+"\"  groupType=\""+ def.getGroupType() 
				+"\"  groupTypeName=\""+ def.getGroupTypeName()
				+"\" relationType=\""+def.getRelationType()
				+"\" relationTypeName=\""+def.getRelationTypeName()
				+"\" logicCal=\""+ def.getLogicCal().getKey()
				+"\" supportRelation=\""+(def.isSupportRelation() ?"true":"false") 
				+"\" extract=\""+def.getExtract().getKey()+"\"> ");
		if("spec".equals(def.getSource())){
			sb.append("<groups groupKey=\""+ def.getGroupKey() +"\" groupName=\""+ def.getGroupName()+"\"/>");
		}
		
		if("var".equals(def.getSource())){
			sb.append("<var source=\""+def.getVar().getSource()+"\" name=\""+def.getVar().getName()+"\"   " +
					" executorType=\""+def.getVar().getExecutorType()+"\" userValType=\""+def.getVar().getUserValType()+"\" " +
					" groupValType=\""+def.getVar().getGroupValType()+"\"	dimension=\""+def.getVar().getDimension()+"\" />  ");
		}

		sb.append("</groupRel>");
		
		return sb.toString();
		
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		GroupRelPluginDef def=new GroupRelPluginDef();
		
		String source=element.getAttribute("source");
		String groupType=element.getAttribute("groupType");
		String groupTypeName=element.getAttribute("groupTypeName");
		String supportRelation=element.getAttribute("supportRelation");
		
		def.setSource(source);
		def.setGroupType(groupType);
		def.setGroupTypeName(groupTypeName);
		
		
		if(StringUtil.isNotEmpty(supportRelation)){
			boolean blnSupportRelation="true".equals(supportRelation);
			def.setSupportRelation(blnSupportRelation);
			if(blnSupportRelation){
				String relationType=element.getAttribute("relationType");
				String relationTypeName=element.getAttribute("relationTypeName");
				def.setRelationType(relationType);
				def.setRelationTypeName(relationTypeName);
			}
		}
		
		if("spec".equals(def.getSource())){
			Element groupEl=XmlUtil.getChildNodeByName(element, "groups");
			String groupKey=groupEl.getAttribute("groupKey");
			String groupName=groupEl.getAttribute("groupName");
			def.setGroupKey(groupKey);
			def.setGroupName(groupName);
		}
		
		if("var".equals(source)){
			Element varEl=XmlUtil.getChildNodeByName(element, "var");
			String name=varEl.getAttribute("name");
			String source1=varEl.getAttribute("source");
			String executorType=varEl.getAttribute("executorType");
			String userValType=varEl.getAttribute("userValType");
			String groupValType=varEl.getAttribute("groupValType");
			String dimension=varEl.getAttribute("dimension");
			ExecutorVar executorVar = new ExecutorVar(source1, name, executorType,userValType,groupValType, dimension);
			def.setExecutorVar(executorVar); 
		}
		
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) throws Exception {
		//{"extractType":"EXACT_NOEXACT","groupKey":"","groupName":"","groupType":"","groupVar":"","logicType":"OR",
		//"pluginName":"","relationType":"","source":"","supportRelation":false,"userVar":""}
		GroupRelPluginDef def=new GroupRelPluginDef();
		String source=JsonUtil.getString(pluginJson, "source");
		String groupType=JsonUtil.getString(pluginJson, "groupType");
		String groupTypeName=JsonUtil.getString(pluginJson, "groupTypeName");
		
		def.setSource(source);
		def.setGroupType(groupType);
		def.setGroupTypeName(groupTypeName);
		//指定的组
		if("spec".equals(source)){
			String groupKey=JsonUtil.getString(pluginJson, "groupKey");
			String groupName=JsonUtil.getString(pluginJson, "groupName");
			def.setGroupKey(groupKey);
			def.setGroupName(groupName);
		}
		//指定的变量
		if("var".equals(source)){
			ExecutorVar executorVar = (ExecutorVar) JsonUtil.toBean(pluginJson.get("var").asText(),ExecutorVar.class);
			def.setExecutorVar(executorVar);
		}
		
		boolean supportRelation=JsonUtil.getBoolean(pluginJson, "supportRelation"); 
		def.setSupportRelation(supportRelation);
		
		//支持关系类型。
		if(supportRelation){
			String relationType=JsonUtil.getString(pluginJson, "relationType");
			String relationTypeName=JsonUtil.getString(pluginJson, "relationTypeName");
			def.setRelationTypeName(relationTypeName);
			def.setRelationType(relationType);
		}
		
		return def;
	}
}
