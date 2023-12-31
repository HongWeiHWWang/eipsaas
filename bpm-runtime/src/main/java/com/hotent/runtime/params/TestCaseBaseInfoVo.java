package com.hotent.runtime.params;



import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * 测试用例设置基本信息vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="测试用例设置基本信息")
public class TestCaseBaseInfoVo {

	@ApiModelProperty(name="formId",notes="表单ID",required=true)
	private String formId;
	
	@ApiModelProperty(name="nodeInfo",notes="节点信息",required=true)
	private Map<String,List<BpmNodeDefVo>> nodeInfo;
	
	@ApiModelProperty(name="defKeys",notes="定义key")
	private ArrayNode defKeys;
	
	public TestCaseBaseInfoVo(){}
	
	public TestCaseBaseInfoVo(String formId,Map<String,List<BpmNodeDefVo>> nodeInfo,ArrayNode defKeys){
		this.formId = formId;
		this.nodeInfo = nodeInfo;
		this.defKeys = defKeys;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}


	public Map<String, List<BpmNodeDefVo>> getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(Map<String, List<BpmNodeDefVo>> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public ArrayNode getDefKeys() {
		return defKeys;
	}

	public void setDefKeys(ArrayNode defKeys) {
		this.defKeys = defKeys;
	}

}
