package com.hotent.bpm.engine.def.impl.update;

import java.util.List;

import org.dom4j.Element;

import com.hotent.bpm.engine.def.DefXmlUpdate;

/**
 * 节点插件XML更新。 
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-4-上午8:58:08
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NodeDefXmlUpdate extends DefXmlUpdate {

	@SuppressWarnings("rawtypes")
	@Override
	public void update(Element oldEl, Element newEl) {
		
		String xParentPath="//ext:extProcess/ext:extNodes";
		
		Element parentEl = (Element)newEl.selectSingleNode(xParentPath);
		
		List list = oldEl.selectNodes("//ext:*[@bpmnElement]");
		for(int i=0;i<list.size();i++){
			Element nodeEl = (Element)list.get(i);
			String nodeId = nodeEl.attributeValue("bpmnElement");
			
			Element newNodeEl = (Element)newEl.selectSingleNode("//ext:extNodes/child::*[@bpmnElement='" + nodeId + "']");
			if(newNodeEl!=null){
				parentEl.remove(newNodeEl);
				
				Element tempEl=cloneNode(nodeEl);
				
				parentEl.add(tempEl);
			}
			
		}

	}

}
