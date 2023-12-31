package com.hotent.activiti.def.graph;

import java.awt.geom.Point2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.hotent.activiti.def.graph.ilog.activiti.BPMNEdge;
import com.hotent.activiti.def.graph.ilog.activiti.BPMNShap;
import com.hotent.activiti.def.graph.ilog.activiti.ProcessDiagramGenerator;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmDefLayout;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmNodeLayout;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.DiagramService;
import com.hotent.bpm.natapi.graph.NatProcessImageService;
	
@Service("diagramService")
public class DefaultDiagramService implements DiagramService
{
	@Resource
	NatProcessImageService natProcessImageService;

	@Resource
	BpmDefinitionService  bpmDefinitionService;

	/**
	 * BPM的XML的命名空间
	 */
	private final static String BPM_XML_NS = "xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"";
	
	@Override
	public InputStream getDiagramByBpmnDefId(String defId)
	{
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefId(defId);
		return natProcessImageService.getProcessImageByBpmnXml(bpmDefinition.getBpmnXml());
	}

	@Override
	public InputStream getDiagramByDefId(String defId, Map<String, String> colourMap)
	{
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefId(defId);
			return natProcessImageService.getProcessImageByBpmnXml(bpmDefinition.getBpmnXml(), colourMap);
	}

	@Override
	public BpmDefLayout getLayoutByDefId(String defId)
	{
		// 获取流程定义
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefId(defId);
		String bpmnXml = bpmDefinition.getBpmnXml();

		// 计算流程图节点坐标
		BpmDefLayout bpmDefLayout = new BpmDefLayout();
		List<BpmNodeLayout> nodeLayoutlist = new ArrayList<BpmNodeLayout>();

		List<BPMNShap> shaps = ProcessDiagramGenerator.extractBPMNShap(bpmnXml);
		List<BPMNEdge> edges = ProcessDiagramGenerator.extractBPMNEdge(bpmnXml);
		Point2D.Double[] points = ProcessDiagramGenerator.caculateCanvasSize(shaps, edges);
		double shiftX = points[0].getX() < 0 ? points[0].getX() : 0;
		double shiftY = points[0].getY() < 0 ? points[0].getY() : 0;
		float width = (float) Math.round((points[1].getX() + 10 - shiftX));
		float height = (float) Math.round((points[1].getY() + 10 - shiftY));
		float minX = (float) Math.round((points[0].getX() - shiftX));
		float minY = (float) Math.round((points[0].getY() - shiftY));
		minX = (minX <= 5 ? 5 : minX);
		minY = (minY <= 5 ? 5 : minY);

		bpmnXml = bpmnXml.replace(BPM_XML_NS, "");
		Document doc = Dom4jUtil.loadXml(bpmnXml);
		Element root = doc.getRootElement();
		List<?> sequenceFlows = root.selectNodes("//sequenceFlow");
		Map<String, String> seqIdandName = new HashMap<String, String>();

		for (Object node : sequenceFlows){
			String id = ((Element) node).attributeValue("id");
			String name = ((Element) node).attributeValue("name");
			seqIdandName.put(id, name);
		}
		List<?> list = root.selectNodes("//bpmndi:BPMNShape");
		int subProcessNum = 1;// 内嵌子流程的层数
		Map<String, Integer> parentZIndexes = new HashMap<String, Integer>();// 存放父节点的Zindex
																				// ，key值为父节点的ID
		for (int i = 0; i < list.size(); i++){
			Element el = (Element) list.get(i);

			// Exclude Pool and Lane components
			String id = el.attributeValue("bpmnElement");
			Element component = (Element) root.selectSingleNode("//*[@id='" + id + "']");

			if (component == null || component.getName().equalsIgnoreCase("participant") || component.getName().equalsIgnoreCase("lane"))
			{
				continue;
			}

			Element tmp = (Element) el.selectSingleNode("omgdc:Bounds");
			float x = Float.parseFloat(tmp.attributeValue("x"));
			float y = Float.parseFloat(tmp.attributeValue("y"));

			float w = Float.parseFloat(tmp.attributeValue("width"));
			float h = Float.parseFloat(tmp.attributeValue("height"));
			x = (float) (x - minX + 5 - shiftX);
			y = (float) (y - minY + 5 - shiftY);

			Element procEl = (Element) root.selectSingleNode("//process/descendant::*[@id='" + id + "']");
			if (procEl != null){
				String type = procEl.getName();
				if (type.equals("endEvent")){
					type = NodeType.END.getKey();
				}
				if (type.equals("startEvent")){
					type = NodeType.START.getKey();
				}
				if (!"subProcess".equals(type) && !"callActivity".equals(type)){
					Element multiObj = procEl.element("multiInstanceLoopCharacteristics");
					String signType = procEl.attributeValue("signType");
					if (multiObj != null && !"subProcess".equals(type))
						type = NodeType.SIGNTASK.getKey();
					if(StringUtil.isNotEmpty(signType)) {
						type = NodeType.CUSTOMSIGNTASK.getKey();
					}
				}

				Element parent = procEl.getParent();

				String name = procEl.attributeValue("name");

				int zIndex = 10;
				String parentName = parent.getName();
				// 父节点为子流程的情况，zindex 设为父节点ZIndex+1，开始事件类型修改为subStartEvent。
				if (parentName.equals("subProcess")){
					if (parent.getParent().getName().equals("subProcess")){
						subProcessNum++;
					}
					if (type.equalsIgnoreCase("subProcess")){
						zIndex = parentZIndexes.get(parent.attributeValue("id")) + 1;
						parentZIndexes.put(id, zIndex);
					} 
					else if (type.equalsIgnoreCase("startEvent")){
						// type = "subStartEvent";
					} 
					else if (type.equalsIgnoreCase("endEvent")){
						// type = "subEndEvent";
					} 
					else{
						zIndex = 10 + subProcessNum;
					}
				} 
				else{
					if (type.equalsIgnoreCase("subProcess")){
						parentZIndexes.put(id, zIndex);
					}
				}

				BpmNodeLayout bpmNodeLayout = new BpmNodeLayout(id, name, NodeType.fromKey(type), x, y, w, h);
				if (!type.equalsIgnoreCase("subProcess"))
					nodeLayoutlist.add(bpmNodeLayout);
			}
		}

		bpmDefLayout.setDefId(defId);
		bpmDefLayout.setName(bpmDefinition.getName());
		bpmDefLayout.setWidth(width);
		bpmDefLayout.setHeight(height);
		bpmDefLayout.setListLayout(nodeLayoutlist);

		return bpmDefLayout;

	}
}
