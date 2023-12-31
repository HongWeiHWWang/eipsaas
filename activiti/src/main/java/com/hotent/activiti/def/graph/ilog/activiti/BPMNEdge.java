package com.hotent.activiti.def.graph.ilog.activiti;


import java.awt.geom.Point2D;
import java.util.List;


public class BPMNEdge {
	private List<Point2D.Double> points;
	private String id;
	private String name;
	private Point2D.Double midpoint;
	private DirectionType direction;
	private String sourceRef;
	private String targetRef;
	
	float labelX = 0;
	float labelY = 0;
	float labelWidth = 0;
	float labelHeight = 0;
	private String parentRef;
	
	public String getParentRef() {
		return parentRef;
	}
	public void setParentRef(String parentRef) {
		this.parentRef = parentRef;
	}
	public List<Point2D.Double> getPoints() {
		return points;
	}
	public void setPoints(List<Point2D.Double> points) {
		this.points = points;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Point2D.Double getMidpoint() {
		return midpoint;
	}
	public void setMidpoint(Point2D.Double midpoint) {
		this.midpoint = midpoint;
	}
	public DirectionType getDirection() {
		return direction;
	}
	public void setDirection(DirectionType direction) {
		this.direction = direction;
	}
	public String getSourceRef() {
		return sourceRef;
	}
	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}
	public String getTargetRef() {
		return targetRef;
	}
	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}
	public float getLabelX() {
		return labelX;
	}
	public void setLabelX(float labelX) {
		this.labelX = labelX;
	}
	public float getLabelY() {
		return labelY;
	}
	public void setLabelY(float labelY) {
		this.labelY = labelY;
	}
	public float getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(float labelWidth) {
		this.labelWidth = labelWidth;
	}
	public float getLabelHeight() {
		return labelHeight;
	}
	public void setLabelHeight(float labelHeight) {
		this.labelHeight = labelHeight;
	}
	@Override
	public String toString() {
		String str="";
		for(Point2D.Double point:points){
			str=str+point.getX()+":"+point.getY()+"  ";
		}
		
		return "BPMNEdge [points=" + 
				str + ", name=" + name + " <midpoint="
				+ midpoint.getX()+":"+midpoint.getY() + ">]";
	}
	
	
}
