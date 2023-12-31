package com.hotent.activiti.def.graph.ilog.activiti;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;

import com.hotent.activiti.util.FontUtil;
import com.hotent.base.util.Dom4jUtil;

/**
 * 此类用于能过bpmn的xml文档生成相应的图片。此类中主要使用静态方法。
 * 
 * @author Raise
 */
public class ProcessDiagramDivGenerator {

	private static FontMetrics getFontMetrics(){
		BufferedImage processDiagram = new BufferedImage(2, 2, 2);
		Graphics2D g = processDiagram.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(Color.black);
		Font font = FontUtil.getFont("宋体", 1, 12);
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		return fontMetrics;
	}

	/**
	 * 根据所有将绘画的图形组件和连线，计算所需画布的大小。
	 * 返回用数组类型<code>Point2D.Double[]<code>表示的画布大小。Point2D.Double[0]<code>中的x,y分别表示所有将绘画的图形组件和连线
	 * 中的最小坐标。Point2D.Double[1]<code>中的x,y分别表示所有将绘画的图形组件和连线中的最大坐标。
	 * @param shaps 所有将绘画的图形
	 * @param edges 所有将绘画的连线
	 * @return 画布的大小。
	 */
	public static Point2D.Double[] caculateCanvasSize(List<BPMNShap> shaps, List<BPMNEdge> edges, boolean isEclipseDesigner) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = 0;
		double maxY = 0;

		for (BPMNShap shap : shaps) {
			if (shap.getX() < minX) {
				minX = shap.getX();
			}
			if (shap.getY() < minY) {
				minY = shap.getY();
			}

			if (shap.getX()+shap.getWidth() > maxX) {
				maxX = shap.getX()+shap.getWidth();
			}

			if (shap.getY()+shap.getHeight() > maxY) {
				maxY = shap.getY()+shap.getHeight();
			}
		}

		for (BPMNEdge edge : edges) {
			for (Point2D.Double point : edge.getPoints()) {
				if (point.getX() < minX) {
					minX = point.getX();
				}
				if (point.getY() < minY) {
					minY = point.getY();
				}

				if (point.getX() > maxX) {
					maxX = point.getX();
				}

				if (point.getY() > maxY) {
					maxY = point.getY();
				}
			}
			
			//计算线上标签的最大最小坐标
			double labelMinX = minX;
			double labelMinY = minY;
			double labelMaxX = maxX;
			double labelMaxY = maxY;
			
			//eclipse designer
			if(isEclipseDesigner){
				if(edge.getLabelWidth()>0 && !(edge.labelX < 0 && edge.labelY==0)){
					labelMinX = edge.getLabelX();
					labelMinY = edge.getLabelY();
					labelMaxX = edge.getLabelX() + edge.getLabelWidth();
					labelMaxY = edge.getLabelY() + edge.getLabelHeight();
				}
			}else{//flash designer
				String label = edge.getName()==null?"":edge.getName();
				if(!"".equals(label)){
					Point2D.Double midPoint = edge.getMidpoint();
					DirectionType directionType=edge.getDirection();
					FontMetrics fontMetrics = getFontMetrics();
					
					if(directionType==DirectionType.UpToDown){
						labelMinX = midPoint.getX() + fontMetrics.getHeight() / 2;
						labelMinY = midPoint.getY();
					}else if(directionType==DirectionType.DownToUp){
						labelMinX = midPoint.getX() - fontMetrics.stringWidth(label) - fontMetrics.getHeight() / 2;
						labelMinY = midPoint.getY() - fontMetrics.getHeight() / 2 - fontMetrics.getHeight();
					}else if(directionType==DirectionType.LeftToRight){
						labelMinX = midPoint.getX() - fontMetrics.stringWidth(label) / 2;
						labelMinY = midPoint.getY();
					}else{
						labelMinX = fontMetrics.stringWidth(label) / 2;
						labelMinY = midPoint.getY()+ fontMetrics.getHeight() - fontMetrics.getHeight();
					}
					
					labelMaxX=labelMinX+fontMetrics.stringWidth(label);
					labelMaxY=labelMinY+fontMetrics.getHeight();
				}
			}
			
			if(labelMinX<minX){
				minX=labelMinX;
			}
			if(labelMinY<minY){
				minY=labelMinY;
			}
			if(labelMaxX>maxX){
				maxX=labelMaxX;
			}
			if(labelMaxY>maxY){
				maxY=labelMaxY;
			}
			
		}
		
		return new Point2D.Double[]{
			new Point2D.Double(minX, minY),
			new Point2D.Double(maxX, maxY)
		};
	}

	
	/**
	 * 获取定义流程的xml文档中的所有图形组件。
	 * @param bpmnXml 定义流程的xml文档
	 * @return 从xml文档中取得的组件的列表
	 */
	@SuppressWarnings("unchecked")
	public static List<BPMNShap> extractBPMNShap(String bpmnXml) {
		bpmnXml = bpmnXml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document doc = Dom4jUtil.loadXml(bpmnXml);
		Element root = doc.getRootElement();

		List<Element> shaps = root.selectNodes("//bpmndi:BPMNShape");
		List<BPMNShap> bpmnShaps = new ArrayList<BPMNShap>();
		for (Element shap : shaps) {
			BPMNShap bpmnShap = new BPMNShap();

			// BPMNShap节点属性
			bpmnShap.setBpmnElement(shap.attributeValue("bpmnElement"));
			bpmnShap.setChoreographyActivityShape(shap.attributeValue("choreographyActivityShape"));
			bpmnShap.setHorizontal(getBooleanAttr(shap, "isHorizontal"));
			bpmnShap.setExpanded(getBooleanAttr(shap, "isExpanded"));
			bpmnShap.setMarkerVisible(getBooleanAttr(shap, "isMarkerVisible"));
			bpmnShap.setMessageVisible(getBooleanAttr(shap, "isMessageVisible"));
			bpmnShap.setParticipantBandKind(shap.attributeValue("participantBandKind"));

			// Bounds节点属性，坐标点
			Element bound = (Element) shap.selectSingleNode("./omgdc:Bounds");
			bpmnShap.setX(Double.parseDouble(bound.attributeValue("x")));
			bpmnShap.setY(Double.parseDouble(bound.attributeValue("y")));
			bpmnShap.setWidth(Double.parseDouble(bound.attributeValue("width")));
			bpmnShap.setHeight(Double.parseDouble(bound.attributeValue("height")));

			// 组件类型
			Element component = (Element) root.selectSingleNode("//*[@id='" + bpmnShap.getBpmnElement() + "']");
			if(component==null) continue;
			BPMNShapType type = getBPMNShapType(component);
			
			bpmnShap.setType(type);
			// 组件标签名字
			bpmnShap.setName(component.attributeValue("name"));
			// 设置其它属性
			setBPMNShapProperties(component, bpmnShap);
			bpmnShaps.add(bpmnShap);
		}
		return bpmnShaps;
	}

	/**
	 * 获取xml文件中定义的所有连线。
	 * @param bpmnXml 定义流程的xml文档
	 * @return 从xml文档中取得的连线的列表
	 */
	@SuppressWarnings("unchecked")
	public static List<BPMNEdge> extractBPMNEdge(String bpmnXml) {
		List<BPMNEdge> bpmnEdges = new ArrayList<BPMNEdge>();

		bpmnXml = bpmnXml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document doc = Dom4jUtil.loadXml(bpmnXml);
		Element root = doc.getRootElement();

		List<Element> edges = root.selectNodes("//bpmndi:BPMNEdge");
		for (Element edge : edges) {
			BPMNEdge bpmnEdge = new BPMNEdge();
			// 取得所有点
			List<Point2D.Double> points = new ArrayList<Point2D.Double>();
			List<Element> waypoints = edge.selectNodes("./omgdi:waypoint");
			for (Element waypoint : waypoints) {
				double x = Double.parseDouble(waypoint.attributeValue("x"));
				double y = Double.parseDouble(waypoint.attributeValue("y"));
				Point2D.Double point = new Point2D.Double(x, y);
				points.add(point);
			}
			bpmnEdge.setPoints(points);
			
			//
			Element label = (Element)edge.selectSingleNode("./bpmndi:BPMNLabel/omgdc:Bounds");
			if(label!=null){
				float labelX = Float.parseFloat(label.attributeValue("x"));
				float labelY = Float.parseFloat(label.attributeValue("y"));
				float labelWidth = Float.parseFloat(label.attributeValue("width"));
				float labelHeight = Float.parseFloat(label.attributeValue("height"));
				bpmnEdge.setLabelX(labelX);
				bpmnEdge.setLabelY(labelY);
				bpmnEdge.setLabelWidth(labelWidth);
				bpmnEdge.setLabelHeight(labelHeight);
			}
			
			// 取得标签名字
			String bpmnElement = edge.attributeValue("bpmnElement");
			Element component = (Element) root.selectSingleNode("//sequenceFlow[@id='" + bpmnElement + "']");
			bpmnEdge.setName(component.attributeValue("name"));
			// 计算中点
			double x = 0, y = 0;
			DirectionType directionType;
			List<Double> lens = new ArrayList<Double>();
			for (int i = 1; i < points.size(); i++) {
				lens.add(Math.abs(points.get(i - 1).getX() - points.get(i).getX()) + Math.abs(points.get(i - 1).getY() - points.get(i).getY()));
			}
			double halfLen = 0;
			for (double len : lens) {
				halfLen += len;
			}
			halfLen = halfLen / 2;

			double accumulativeLen = 0;

			int i;
			for (i = 0; i < lens.size(); i++) {
				accumulativeLen += lens.get(i);
				if (accumulativeLen > halfLen) {
					break;
				}
			}

			// x坐标相等，
			if (points.get(i).getX() == points.get(i + 1).getX()) {
				// 前一个点在后一个点的上边
				if (points.get(i).getY() < points.get(i + 1).getY()) {
					// accumulativeLen-halfLen=points.get(i+1).getY()-y;
					y = halfLen - accumulativeLen + points.get(i + 1).getY();
					directionType = DirectionType.UpToDown;
				} else {
					// accumulativeLen-halfLen=y-points.get(i+1).getY();
					y = accumulativeLen - halfLen + points.get(i + 1).getY();
					directionType = DirectionType.DownToUp;
				}
				x = points.get(i).getX();

			} else {// y坐标相等，
					// 前一个点在后一个点的左边
				if (points.get(i).getX() < points.get(i + 1).getX()) {
					// accumulativeLen-halfLen=points.get(i+1).getX()-x;
					x = halfLen - accumulativeLen + points.get(i + 1).getX();
					directionType = DirectionType.LeftToRight;
				} else {
					// accumulativeLen-halfLen=x-points.get(i+1).getX();
					x = accumulativeLen - halfLen + points.get(i + 1).getX();
					directionType = DirectionType.RightToLef;
				}
				y = points.get(i).getY();
			}
			Point2D.Double midpoint = new Point2D.Double(x, y);

			// 取得中点
			bpmnEdge.setMidpoint(midpoint);
			// 取得中点所有直线的方向
			bpmnEdge.setDirection(directionType);

			bpmnEdges.add(bpmnEdge);

		}

		return bpmnEdges;

	}

	/**
	 * 设置BPMNShap的properties属性。properties中存放的是与特定类型的图形组件相关的图形组件的属性。
	 * @param component 流程定义的xml文档中与<code>bpmnShap</code>对应的节点元素
	 * @param bpmnShap 将对其进行操作的图形组件
	 * @return 对其进行properties属性设置后的图形组件
	 */
	private static BPMNShap setBPMNShapProperties(Element component, BPMNShap bpmnShap) {
		BPMNShapType type = bpmnShap.getType();
		Properties properties = bpmnShap.getProperties();
		if (properties == null) {
			properties = new Properties();
		}

		if (type == BPMNShapType.Task 
				|| type == BPMNShapType.ScriptTask 
				|| type == BPMNShapType.ServiceTask 
				|| type == BPMNShapType.BusinessRuleTask
				|| type == BPMNShapType.ManualTask 
				|| type == BPMNShapType.UserTask
				|| type == BPMNShapType.CallActivity
				|| type == BPMNShapType.SubProcess) {
			Element multiInstanceLoopCharacteristics = (Element) component.selectSingleNode("./multiInstanceLoopCharacteristics");
			if (multiInstanceLoopCharacteristics != null) {
				String isSequential = multiInstanceLoopCharacteristics.attributeValue("isSequential");
				properties.put("isSequential", isSequential);
			}
		}
		
		if (type == BPMNShapType.ErrorEvent){
			Element errorEventDefinition=(Element) component.selectSingleNode("errorEventDefinition");
			String errorRef =errorEventDefinition.attributeValue("errorRef");
			properties.put("errorRef", errorRef);
		}

		bpmnShap.setProperties(properties);
		return bpmnShap;
	}

	/**
	 * 获取xml文档元素的Boolean类型的属性。
	 * @param element xml文档元素
	 * @param attr xml文档元素的属性
	 * @return
	 */
	private static Boolean getBooleanAttr(Element element, String attr) {
		String attrVal = element.attributeValue(attr);
		if (attrVal != null) {
			return attrVal.equalsIgnoreCase("true") ? true : false;
		} else {
			return null;
		}
	}

	/**
	 * 根据流程定义的xml文档中节点图形节点元素，获取相应的图形节点的类型。
	 * @param component 流程定义的xml文档中节点图形节点元素
	 * @return 图形节点元素的类型
	 */
	public static BPMNShapType getBPMNShapType(Element component) {
		BPMNShapType retVal = BPMNShapType.UnknowType;
		
		if (component.getName().equals("startEvent")) {
			retVal = BPMNShapType.StartEvent;
		} else if (component.getName().equals("endEvent")) {
			Element errorEventDefinition=(Element) component.selectSingleNode("errorEventDefinition");
			if(errorEventDefinition==null){
				retVal = BPMNShapType.EndEvent;
			}else{
				retVal = BPMNShapType.ErrorEvent;
			}
		} else if (component.getName().equals("exclusiveGateway")) {
			retVal = BPMNShapType.ExclusiveGateway;
		} else if (component.getName().equals("inclusiveGateway")) {
			retVal = BPMNShapType.InclusiveGateway;
		} else if (component.getName().equals("parallelGateway")) {
			retVal = BPMNShapType.ParallelGateway;
		} else if (component.getName().equals("scriptTask")) {
			retVal = BPMNShapType.ScriptTask;
		} else if (component.getName().equals("serviceTask")) {
			retVal = BPMNShapType.ServiceTask;
		} else if (component.getName().equals("businessRuleTask")) {
			retVal = BPMNShapType.BusinessRuleTask;
		} else if (component.getName().equals("task")) {
			retVal = BPMNShapType.Task;
		} else if (component.getName().equals("manualTask")) {
			retVal = BPMNShapType.ManualTask;
		} else if (component.getName().equals("userTask")) {
			retVal = BPMNShapType.UserTask;
		} else if (component.getName().equals("sendTask")) {
			retVal = BPMNShapType.SendTask;
		} else if (component.getName().equals("receiveTask")) {
			retVal = BPMNShapType.ReceiveTask;
		} else if (component.getName().equals("subProcess")) {
			retVal = BPMNShapType.SubProcess;
		} else if (component.getName().equals("callActivity")) {
			retVal = BPMNShapType.CallActivity;
		} else if (component.getName().equals("intermediateCatchEvent")) {
			retVal = BPMNShapType.IntermediateCatchEvent;
		} else if (component.getName().equals("adHocSubProcess")) {
			retVal = BPMNShapType.ComplexGateway;
		} else if (component.getName().equals("eventBasedGateway")) {
			retVal = BPMNShapType.EventBasedGateway;
		} else if (component.getName().equals("transaction")) {
			retVal = BPMNShapType.Transaction;
		} else if (component.getName().equals("participant")) {
			String id = component.attributeValue("id");
			String processRef = component.attributeValue("processRef");
			Element root = (Element) component.getDocument().getRootElement();
			Element process = (Element) root.selectSingleNode("//*[@id='" + processRef + "']");
			if (process.element("laneSet") != null) {
				Element shap = (Element) root.selectSingleNode("//*[@bpmnElement='" + id + "']");
				String isHorizontal = shap.attributeValue("isHorizontal");
				if (isHorizontal != null && isHorizontal.equalsIgnoreCase("false")) {
					retVal = BPMNShapType.VPool;
				} else {
					retVal = BPMNShapType.HPool;
				}
			}
		} else if (component.getName().equals("lane")) {
			String id = component.attributeValue("id");
			Element root = (Element) component.getDocument().getRootElement();
			Element shap = (Element) root.selectSingleNode("//*[@bpmnElement='" + id + "']");
			String isHorizontal = shap.attributeValue("isHorizontal");
			if (isHorizontal != null && isHorizontal.equalsIgnoreCase("false")) {
				retVal = BPMNShapType.VLane;
			} else {
				retVal = BPMNShapType.HLane;
			}
		}
		return retVal;
	}
}
