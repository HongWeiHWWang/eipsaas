package com.hotent.bpm.engine.def;

import org.dom4j.Document;
import org.dom4j.Element;

import com.hotent.base.util.Dom4jUtil;

/**
 * 流程定义在更新的时候，我们是设计流程定义XML文件。
 * <pre>
 * 1.设计完成后，我们将这个文件转换成bpmn2.0的xml文件。
 * 2.我们需要将原来的XML文件中的配置copy到新的xml文件当中。
 * 3.当前类就是更新新增的XML文件。
 * 
 * BoDefXmlUpdate : 更新bo数据。
 * GlobalFormDefXmlUpdate : 全局表单的设置。
 * InstFormDefXmlUpdate : 流程实例表单。
 * NodeDefXmlUpdate : 节点内配置文件。
 * PluginsDefXmlUpdate : 全局插件的更新。
 * PropertiesDefXmlUpdate  : 全局属性的更新。
 * VarDefXmlUpdate : 全局变量配置更新。
 * 
 * 新开发的更新需要继承此类，这个类需要做下配置。
 * 配置文件 x5-bpmx-def.xml
 * 
 *  &lt;bean id="xmlUpdateList" class="java.util.ArrayList">
  		 &lt;constructor-arg>
   			 &lt;list>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.BoDefXmlUpdate">&lt;/bean>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.GlobalFormDefXmlUpdate">&lt;/bean>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.InstFormDefXmlUpdate">&lt;/bean>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.NodeDefXmlUpdate">&lt;/bean>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.PluginsDefXmlUpdate">&lt;/bean>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.PropertiesDefXmlUpdate">&lt;/bean>
    			 &lt;bean class="com.hotent.runtime.core.engine.def.impl.update.VarDefXmlUpdate">&lt;/bean>
    			
   			 &lt;/list>
  		 &lt;/constructor-arg>
 	 &lt;/bean>
 * </pre>
 * @author ray
 *
 */
public abstract class DefXmlUpdate {

	
	public abstract void update(Element oldEl,Element newEl);
	
	protected void handCommon(Element oldEl, Element newEl, String xParentPath,
			String xPath) {
		Element oldTmpEl= (Element) oldEl.selectSingleNode(xPath);
		if(oldTmpEl==null) return;
		
		Element tempEl=cloneNode(oldTmpEl);
		
		Element parentEl= (Element) newEl.selectSingleNode(xParentPath);
		
		Element newTmpEl= (Element) newEl.selectSingleNode(xPath);
		if(newTmpEl!=null){
			parentEl.remove(newTmpEl);
		}
		
		parentEl.add(tempEl);
	}
	
	/**
	 * 克隆XML节点。
	 * @param el
	 * @return  Element
	 */
	protected Element cloneNode(Element el){
		String xml=el.asXML();
		Document doc= Dom4jUtil.loadXml(xml);
		
		return doc.getRootElement();
	}
}
