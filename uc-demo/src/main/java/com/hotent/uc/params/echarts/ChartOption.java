package com.hotent.uc.params.echarts;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;


 /**
 * 
 * <pre> 
 * 描述：echarts 简单关系网络封装类
 * 构建组：x5-bpmx-platform
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2018-01-19 08:31:05
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class ChartOption{
	
	/**
	* 标题
	*/
	@ApiModelProperty(name="text",notes="标题")
	protected String text; 
	
	/**
	 * 小标题
	 */
	@ApiModelProperty(name="subtext",notes="小标题")
	protected String subtext;
	
	/**
	* 图例
	*/
	@ApiModelProperty(name="legend",notes="图例")
	protected List<String> legend; 
	
	/**
	* 描述
	*/
	@ApiModelProperty(name="seriesName",notes="系列名称")
	protected String seriesName; 
	
	/**
	 * 系列类型
	 */
	@ApiModelProperty(name="categories",notes="系列类型：[{name: '类型'},{name: '组织领导'},{name:'汇报线'}]")
	protected ArrayNode categories;
	
	/**
	 * 节点
	 */
	@ApiModelProperty(name="nodes",notes="节点")
	protected List<ChartNode> nodes;
	
	/**
	 * 节点关系
	 */
	@ApiModelProperty(name="links",notes="节点关系")
	protected List<ChartLink> links;
	
	public ChartOption(String text,String subtext,List<String> legend,String seriesName,
			ArrayNode categories){
		this.text = text;
		this.subtext = subtext;
		this.legend = legend;
		this.seriesName = seriesName;
		this.categories = categories;
	}
	
	public ChartOption(String text,String subtext,List<String> legend,String seriesName,
			ArrayNode categories,List<ChartNode> nodes,List<ChartLink> links){
		this.text = text;
		this.subtext = subtext;
		this.legend = legend;
		this.seriesName = seriesName;
		this.categories = categories;
		this.nodes = nodes;
		this.links = links;
	}
	
	public ChartOption(){
		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubtext() {
		return subtext;
	}

	public void setSubtext(String subtext) {
		this.subtext = subtext;
	}

	public List<String> getLegend() {
		return legend;
	}

	public void setLegend(List<String> legend) {
		this.legend = legend;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public ArrayNode getCategories() {
		return categories;
	}

	public void setCategories(ArrayNode categories) {
		this.categories = categories;
	}

	public List<ChartNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<ChartNode> nodes) {
		this.nodes = nodes;
	}

	public List<ChartLink> getLinks() {
		return links;
	}

	public void setLinks(List<ChartLink> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "chartOption [text=" + text + ", subtext=" + subtext
				+ ", legend=" + legend + ", seriesName=" + seriesName
				+ ", categories=" + categories + ", nodes=" + nodes
				+ ", links=" + links + "]";
	}
}