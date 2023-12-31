package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.hotent.base.util.Dom4jUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.PrivilegeItem;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 会签特权保存。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-25-下午10:49:00
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class PrivilegeBpmDefXmlHandler extends AbstractBpmDefXmlHandler<List<PrivilegeItem> > {

	@Override
	protected String getXml(String defId, String nodeId, List<PrivilegeItem> privilegeList) {
		String xml=getXml(privilegeList);
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		Document doc=Dom4jUtil.loadXml(defXml);
		Element root=doc.getRootElement();
		
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:signSetting/ext:privilege" ;
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:signSetting" ;
		
		String rtnXml=DefXmlHandlerUtil.getXml(root, xml, xParentPath, xPath);
		
		return rtnXml;
	}

	
	private String getXml(List<PrivilegeItem> privilegeList){
		XMLBuilder xmlBuilder;
		try {
			xmlBuilder = XMLBuilder.create("ext:privilege")
					.a("xmlns:ext", BpmConstants.BPM_XMLNS);
			
			for(PrivilegeItem item:privilegeList){
				xmlBuilder=xmlBuilder.e("ext:item").a("mode", item.getPrivilegeMode().getKey())
				.e("ext:members");
				
				UserAssignRuleParser.handXmlBulider(xmlBuilder, item.getUserRuleList());
				xmlBuilder=xmlBuilder.up().up();
			}
			String xml=xmlBuilder.asString();
			return xml;
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}

}
