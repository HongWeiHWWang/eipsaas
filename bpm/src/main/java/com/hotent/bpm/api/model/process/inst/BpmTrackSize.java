package com.hotent.bpm.api.model.process.inst;

/**
 * 轨迹元素大小
 * @author heyifan
 */
public class BpmTrackSize {
	private Double width;	/*宽度*/
	private Double height;	/*高度*/
	
	public BpmTrackSize(){}
	
	public BpmTrackSize(Double width, Double height){
		this.width = width;
		this.height = height;
	}
	
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
}
