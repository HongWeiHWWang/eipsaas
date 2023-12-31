package com.hotent.uc.params.echarts;

import io.swagger.annotations.ApiModelProperty;

 /**
 * 
 * <pre> 
 * 描述：echarts 简单关系网络 节点关系类
 * 构建组：htuc
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2018-01-19 09:21:02
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class ChartLink{
	
	/**
	* 关系名称
	*/
	@ApiModelProperty(name="name",notes="关系名称")
	protected String name;
	
	/**
	* 来源
	*/
	@ApiModelProperty(name="source",notes="来源")
	protected String source; 
	
	/**
	 * 目标
	 */
	@ApiModelProperty(name="target",notes="目标")
	protected String target;
	
	/**
	* 权重
	*/
	@ApiModelProperty(name="weight",notes="权重")
	protected int weight;
	
	public ChartLink(){
		
	}
	
	public ChartLink(String name,String source,String target,int weight){
		this.name = name;
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "chartLink [name=" + name + ", source=" + source + ", target="
				+ target + ", weight=" + weight + "]";
	} 
}