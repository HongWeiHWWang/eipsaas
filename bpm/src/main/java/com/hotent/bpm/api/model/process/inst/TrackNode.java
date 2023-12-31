package com.hotent.bpm.api.model.process.inst;

/**
 * 轨迹节点对象
 * @author heyifan
 */
public class TrackNode {
	/**
	 * 节点类型：线条
	 */
	public final static String TYPE_EDGE = "edge";
	/**
	 * 节点类型：网关
	 */
	public final static String TYPE_SHAP = "shap";
	
	private String id;
	private TrackNode parent;
	private String type = "edge";
	private int depth = 1;
	
	public TrackNode(){}
	
	public TrackNode(String id){
		this.id = id;
	}
	
	public TrackNode(String id, TrackNode parent){
		this.id = id;
		this.parent = parent;
		this.depth = parent.depth + 1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TrackNode getParent() {
		return parent;
	}

	public void setParent(TrackNode parent) {
		this.parent = parent;
		this.depth = parent.depth + 1;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 是否线条
	 * @return
	 */
	public Boolean isEdge(){
		return TYPE_EDGE.equals(this.type);
	}

	/**
	 * 获取深度
	 * @return
	 */
	public int getDepth(){
		return depth;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getId());
		TrackNode pre = this.getParent();
		while(pre!=null){
			sb.insert(0, ",");
			sb.insert(0, pre.getId());
			pre = pre.getParent();
		}
		return sb.toString();
	}
}
