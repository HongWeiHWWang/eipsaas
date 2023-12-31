package com.hotent.bpm.engine.def;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.hotent.base.util.Dom4jUtil;


/**
 * 将原来的xml的配置数据copy到新的流程定义文件中。
 * <pre>
 * 在配置文件 x5-bpmx-bean.xml中。
 * &lt;bean id="defXmlTransForm" class="com.hotent.runtime.core.engine.def.DefXmlTransForm">
		&lt;property name="xmlUpdateList" ref="xmlUpdateList">&lt;/property>
	&lt;/bean>
 * </pre>
 * @author ray
 *
 */
public class DefXmlTransForm {
	
	private List<DefXmlUpdate> xmlUpdateList=new ArrayList<DefXmlUpdate>();

	

	public void setXmlUpdateList(List<DefXmlUpdate> xmlUpdateList) {
		this.xmlUpdateList = xmlUpdateList;
	}
	
	public String transform(String oldXml,String newXml){
		
		Document oldDoc = Dom4jUtil.loadXml(oldXml);
		Document newDoc = Dom4jUtil.loadXml(newXml);
		
		Element oldRoot=oldDoc.getRootElement();
		Element newRoot=newDoc.getRootElement();
		
		for(DefXmlUpdate handler:xmlUpdateList){
			handler.update(oldRoot, newRoot);
		}
		
		return newRoot.asXML();
		
	}
	
	
	
}
