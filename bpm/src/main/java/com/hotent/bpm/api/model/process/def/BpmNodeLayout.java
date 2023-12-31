package com.hotent.bpm.api.model.process.def;

import com.hotent.bpm.api.constant.NodeType;

/**
 * 节点布局类，定义流程节点的坐标及类型。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-4-下午2:05:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmNodeLayout {
	
	/**
	 * 节点ID
	 */
	private String nodeId="";

	/**
	 * 节点名称
	 */
	private String name="";

	/**
	 * 节点类型
	 */
	private NodeType nodeType;
	
	/**
	 * x坐标
	 */
	private float x=0;
	
	/**
	 * y坐标
	 */
	private float y=0;
	
	/**
	 * 宽度
	 */
	private float width=0;
	
	/**
	 * 高度
	 */
	private float height=0;
	
	public BpmNodeLayout(String nodeId, String name, NodeType nodeType, 
			float x, float y, float width, float height){
		this.nodeId = nodeId;
		this.name = name;
		this.nodeType = nodeType;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	
	
	
}
