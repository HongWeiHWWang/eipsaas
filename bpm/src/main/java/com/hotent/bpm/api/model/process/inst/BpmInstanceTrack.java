package com.hotent.bpm.api.model.process.inst;

import java.util.List;

/**
 * 
 * @author heyifan
 */
public class BpmInstanceTrack {
	/**
	 * 事件类型
	 */
	public final static String TYPE_EVENT = "event";
	/**
	 * 连线类型
	 */
	public final static String TYPE_LINE = "line";
	/**
	 * 矩形类型
	 */
	public final static String TYPE_RECT = "rect";
	/**
	 * 菱形类型
	 */
	public final static String TYPE_DIAMOND = "diamond";
	
	private String type;				/*类型*/
	private String taskKey;				/*节点ID*/
	private String status;				/*状态*/
	private Double radius;				/*半径*/
	private Double length;				/*边长*/
	private BpmTrackPoint point;		/*点*/
	private List<BpmTrackPoint> points;	/*线*/
	private BpmTrackSize size;			/*尺寸*/
	private Integer sn;					/*排序号*/
	private Long duration;				/*审批用时*/
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskKey() {
		return taskKey;
	}
	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public BpmTrackPoint getPoint() {
		return point;
	}
	public void setPoint(BpmTrackPoint point) {
		this.point = point;
	}
	public List<BpmTrackPoint> getPoints() {
		return points;
	}
	public void setPoints(List<BpmTrackPoint> points) {
		this.points = points;
	}
	public BpmTrackSize getSize() {
		return size;
	}
	public void setSize(BpmTrackSize size) {
		this.size = size;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public static void main(String[] args) {
		TrackNode p = new TrackNode("a");
		TrackNode a1 = new TrackNode("1", p);
		TrackNode a1B = new TrackNode("B", a1);
		TrackNode a1B5 = new TrackNode("5", a1B);
		
		System.out.println(a1B5.toString());
		
		TrackNode preNode = a1B5;
		int size = a1B5.getDepth();
		int index = 0;
		do{
			index++;
			preNode = preNode.getParent();
			if(index>=size-1) continue;
			System.out.println(preNode.getId());
		}
		while(preNode!=null);
		
	}
}