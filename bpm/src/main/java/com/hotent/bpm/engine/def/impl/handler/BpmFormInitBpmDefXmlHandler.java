package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;

import com.hotent.base.util.Dom4jUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmFormInit;
import com.hotent.bpm.api.model.process.def.FieldInitSetting;
import com.hotent.bpm.api.model.process.def.FormInitItem;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.jamesmurty.utils.XMLBuilder;


/**
 * BO 初始化配置。
 * <pre>
 * 包括表单初始配置和保存配置。
 * </pre>
 * @author ray
 */
@Service
public class BpmFormInitBpmDefXmlHandler extends AbstractBpmDefXmlHandler<BpmFormInit> {


	
	/**
	 * 获取表单初始化项。
	 * @param formInitItem
	 * @return String
	 */
	private  String getFormInit(FormInitItem formInitItem){
		List<FieldInitSetting> showFieldsSettings= formInitItem.getShowFieldsSetting();
		List<FieldInitSetting> saveFieldsSettings= formInitItem.getSaveFieldsSetting();
		if(BeanUtils.isEmpty(showFieldsSettings) && BeanUtils.isEmpty(saveFieldsSettings)) return "";

		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("ext:initItem")
					.a("xmlns:ext", BpmConstants.BPM_XMLNS)
					.a("parentDefKey", formInitItem.getParentDefKey());
			
			if(BeanUtils.isNotEmpty(showFieldsSettings)){
				xmlBuilder=xmlBuilder.e("ext:prevSetting");
				for(FieldInitSetting setting:showFieldsSettings){
					xmlBuilder.e("ext:fieldSetting")
					.a("description", setting.getDescription())
					.e("ext:setting").d(setting.getSetting());
				}
				xmlBuilder=xmlBuilder.up();
			}
			
			if(BeanUtils.isNotEmpty(saveFieldsSettings)){
				xmlBuilder=xmlBuilder.e("ext:saveSetting");
				for(FieldInitSetting setting:saveFieldsSettings){
					xmlBuilder.e("ext:fieldSetting")
					.a("description", setting.getDescription())
					.e("ext:setting").d(setting.getSetting());
				}
			}
			return xmlBuilder.asString();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
		 
	}


	@Override
	protected String getXml(String defId, String node, BpmFormInit formInit) throws Exception {
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		Document doc=Dom4jUtil.loadXml(defXml);
		Element root=doc.getRootElement();
		
		List<BpmNodeDef> list=super.getNodeList(defId);
		Map<String, FormInitItem> map=formInit.getFormInitItemMap();
		for(BpmNodeDef nodeDef:list){
			//if(NodeType.Start.equals( nodeDef.getType())) continue;
			
			String nodeId=nodeDef.getNodeId();
			boolean isContain=map.containsKey(nodeId);
			
			String xml="";
			if(isContain){
				xml=getFormInit(map.get(nodeId));
			}
			String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:formInitSetting";
			String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']";
			Node el= root.selectSingleNode(xPath);
			Element elParent= (Element) root.selectSingleNode(xParentPath);
			if(el==null){
				String propxml="<ext:formInitSetting xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\" />";
				Document tempDoc= Dom4jUtil.loadXml(propxml);
				elParent.add(tempDoc.getRootElement());
			}
			
			String xPathItem="//ext:*[@bpmnElement='"+nodeId+"']/ext:formInitSetting/ext:initItem[@parentDefKey='"+formInit.getParentDefKey()+"']";
			
			DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
		}
		return root.asXML();
	}

}
