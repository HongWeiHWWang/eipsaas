package com.hotent.bpm.api.model.process.inst;

/**
 * 轨迹坐标点
 * @author heyifan
 */
public class BpmTrackPoint {
	private Double x;	/*横坐标*/
	private Double y;	/*纵坐标*/
	
	public BpmTrackPoint(){}
	
	public BpmTrackPoint(Double x, Double y){
		this.x = x;
		this.y = y;
	}
	
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
}