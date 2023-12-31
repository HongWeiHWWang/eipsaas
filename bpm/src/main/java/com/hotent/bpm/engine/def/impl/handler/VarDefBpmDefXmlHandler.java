package com.hotent.bpm.engine.def.impl.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 流程变量保存。
 * 
 * <pre>
 * 这个处理器保存流程变量定义的XML。
 * defId:流程定义ID
 * nodeId:流程节点ID
 * List&lt;BpmVariableDef> 流程变量列表。
 * 流程变量如果nodeId为空则表示，这个变量为全局变量。否则为节点局部变量。
 *  
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-11-下午5:12:15
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class VarDefBpmDefXmlHandler extends AbstractBpmDefXmlHandler<List<BpmVariableDef>>
{

	@Override
	protected String getXml(String defId, String nodeId, List<BpmVariableDef> list) throws Exception
	{

		BpmDefinition def = bpmDefinitionManager.getById(defId);
		String defXml = def.getBpmnXml();

		String xParentPath = "//ext:extProcess";
		String xPath = "//ext:extProcess/ext:varDefs";

		// String rtnXml= DefXmlHandlerUtil.getXml(defXml, xml, xParentPath,
		// xPath);
		Map<String, List<BpmVariableDef>> map = getByVar(list);

		Map<String, String> xmlMap = getXml(map);

		String globalXml = xmlMap.get("global");

		if (globalXml == null)
			globalXml = "";

		String rtnXml = DefXmlHandlerUtil.getXml(defXml, globalXml, xParentPath, xPath);

		map.remove("global");
		xmlMap.remove("global");

		Set<String> set = xmlMap.keySet();
		BpmProcessDef<BpmProcessDefExt> defExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		List<BpmNodeDef> nodeDefs = defExt.getBpmnNodeDefs();

		for (BpmNodeDef node : nodeDefs)
		{
			String thisNodeId = node.getNodeId();
			String xml = "";
			if (set.contains(thisNodeId))
			{
				xml = xmlMap.get(thisNodeId);
			}

			String xNodePath = "//ext:*[@bpmnElement='" + thisNodeId + "']/ext:varDefs";
			String xNodeParentPath = "//ext:*[@bpmnElement='" + thisNodeId + "']";
			rtnXml = DefXmlHandlerUtil.getXml(rtnXml, xml, xNodeParentPath, xNodePath);
		}

		return rtnXml;
	}

	/**
	 * 按照节点生成流程的XML。
	 * 
	 * @param map
	 * @return Map&lt;String,String>
	 */
	private Map<String, String> getXml(Map<String, List<BpmVariableDef>> map)
	{
		Map<String, String> xmlMap = new HashMap<String, String>();

		Set<String> set = map.keySet();
		for (Iterator<String> it = set.iterator(); it.hasNext();)
		{
			String key = it.next();
			List<BpmVariableDef> varList = map.get(key);
			String xml = getByVarDef(varList);
			xmlMap.put(key, xml);
		}

		return xmlMap;
	}

	/**
	 * 将流程变量进行分组。
	 * 
	 * @param varDefList
	 * @return Map&lt;String,List&lt;BpmVariableDef>>
	 */
	private Map<String, List<BpmVariableDef>> getByVar(List<BpmVariableDef> varDefList)
	{
		Map<String, List<BpmVariableDef>> map = new HashMap<String, List<BpmVariableDef>>();

		for (BpmVariableDef def : varDefList)
		{
			if (StringUtil.isEmpty(def.getNodeId()))
			{
				List<BpmVariableDef> list = map.get("global");
				if (BeanUtils.isEmpty(list))
				{
					list = new ArrayList<BpmVariableDef>();
					list.add(def);
					map.put("global", list);
				} else
				{
					list.add(def);
				}
			} else
			{
				String nodeId = def.getNodeId();
				List<BpmVariableDef> list = map.get(nodeId);
				if (BeanUtils.isEmpty(list))
				{
					list = new ArrayList<BpmVariableDef>();
					list.add(def);
					map.put(nodeId, list);
				} else
				{
					list.add(def);
				}
			}
		}
		return map;
	}

	/**
	 * 根据流程变量生成XML
	 * 
	 * @param varDefList
	 * @return String
	 */
	private String getByVarDef(List<BpmVariableDef> varDefList)
	{
		if (BeanUtils.isEmpty(varDefList))
			return "";
		StringBuffer sb = new StringBuffer();
		try
		{

			sb.append("<ext:varDefs xmlns:ext=\"http://www.jee-soft.cn/bpm\">");

			for (BpmVariableDef varDef : varDefList)
			{
				XMLBuilder xmlBuilder = XMLBuilder.create("ext:variableDef").e("ext:name").t(varDef.getName()).up().e("ext:key").t(varDef.getVarKey()).up().e("ext:type").t(varDef.getDataType()).up().e("ext:description").t(varDef.getDescription()).up();

				xmlBuilder.e("ext:defaultVal").t(varDef.getDefaultVal().toString()).up();

				// if(!varDef.isRequired()){
				xmlBuilder.e("ext:isRequired").t(String.valueOf(varDef.isRequired())).up();
				// }

				sb.append(xmlBuilder.asString() + "\n");
			}
			sb.append("</ext:varDefs>");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

}
