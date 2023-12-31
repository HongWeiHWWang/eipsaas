package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.ExtProperty;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 处理属性。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-11-下午3:59:08
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class PropertiesBpmDefXmlHandler extends AbstractBpmDefXmlHandler<BpmDefExtProperties> {

	protected String getXml(String defId, String nodeId, BpmDefExtProperties properties) {
		String xml=getByProperties(properties);
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		String xParentPath="//ext:extProcess";
		String xPath="//ext:extProcess/ext:extProperties";
		 
		String rtnXml= DefXmlHandlerUtil.getXml(defXml, xml, xParentPath, xPath);
	
		return rtnXml;
	}

	private String getByProperties(BpmDefExtProperties ext){
		String xml="";
		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("ext:extProperties")
					.a("xmlns:ext", BpmConstants.BPM_XMLNS)
					.e("ext:subjectRule").d(ext.getSubjectRule()).up()
					.e("ext:description").d(ext.getDescription()).up()
					.e("ext:startNotifyType").t(ext.getStartNotifyType()).up()
					.e("ext:archiveNotifyType").t(ext.getArchiveNotifyType()==null?"":ext.getArchiveNotifyType()).up()
					.e("ext:notifyType").t(ext.getNotifyType()).up()
					.e("ext:skipFirstNode").t(ext.isSkipFirstNode()?"true":"false").up()
					.e("ext:firstNodeUserAssign").t(ext.isFirstNodeUserAssign()?"true":"false").up()
					.e("ext:skipSameUser").t(ext.isSkipSameUser()?"true":"false").up()
					.e("ext:allowCopyTo").t(ext.isAllowCopyTo()?"true":"false").up()
					.e("ext:allowTransTo").t(ext.isAllowTransTo()?"true":"false").up()
					.e("ext:useMainForm").t(ext.getUseMainForm().toString()).up()
					.e("ext:doneDataVersion").t(ext.getDoneDataVersion().toString()).up()
					.e("ext:allowReference").t(ext.isAllowReference()?"true":"false").up()
					.e("ext:allowRefCounts").t(String.valueOf(ext.getAllowRefCounts())).up()
					.a("allowExecutorEmpty", ext.isAllowExecutorEmpty()?"true":"false")
					.a("skipExecutorEmpty", ext.isSkipExecutorEmpty()?"true":"false")
					.a("testNotifyType", ext.getTestNotifyType())
					.a("skipRules", ext.getSkipRules())
					.a("dateType",ext.getDateType())
					.a("dueTime",String.valueOf(ext.getDueTime()));
			
			
			List<ExtProperty> extList= ext.getExtProperty();
			for(ExtProperty property:extList){
				xmlBuilder.e("ext:extProperty").attr("name", property.getName() )
					.attr("value", property.getValue() ).up();
			}
			
			Properties outputProperties = new Properties(); 
			outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes"); 
		
		
			xml=xmlBuilder.asString(outputProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
}
