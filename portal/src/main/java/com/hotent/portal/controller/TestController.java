package com.hotent.portal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre>
 * 描述: 用于测试
 * 构建组：x7
 * 作者: jason
 * 邮箱:
 * 日期:2019-09-12
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/sys/test/v1")
@Api(tags="TestController")
public class TestController{


	@RequestMapping(value="gettreeselectdata", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "系统分类明细页面", httpMethod = "GET", notes = "系统分类")
	public Object gettreeselectdata() throws Exception {
		ArrayNode createArrayNode = JsonUtil.getMapper().createArrayNode();
		ObjectNode createObjectNode = JsonUtil.getMapper().createObjectNode();
		createObjectNode.put("key", "shuiguo");
		createObjectNode.put("value", "水果");
		
		ArrayNode children = JsonUtil.getMapper().createArrayNode();
		ObjectNode childrenNode = JsonUtil.getMapper().createObjectNode();
		childrenNode.put("key", "pingguo");
		childrenNode.put("value", "苹果");
		children.add(childrenNode);
		
		childrenNode = JsonUtil.getMapper().createObjectNode();
		childrenNode.put("key", "xinagjiao");
		childrenNode.put("value", "香蕉");
		children.add(childrenNode);

		ObjectNode createObjectNode1 = JsonUtil.getMapper().createObjectNode();
		createObjectNode1.put("key", "animal");
		createObjectNode1.put("value", "动物");

		ArrayNode children1 = JsonUtil.getMapper().createArrayNode();
		ObjectNode childrenNode1 = JsonUtil.getMapper().createObjectNode();
		childrenNode1.put("key", "dog");
		childrenNode1.put("value", "狗");
		children1.add(childrenNode1);

		childrenNode1 = JsonUtil.getMapper().createObjectNode();
		childrenNode1.put("key", "cat");
		childrenNode1.put("value", "猫");
		children1.add(childrenNode1);

		ObjectNode createObjectNode2 = JsonUtil.getMapper().createObjectNode();
		createObjectNode2.put("key", "brand");
		createObjectNode2.put("value", "品牌");

		ArrayNode children2 = JsonUtil.getMapper().createArrayNode();
		ObjectNode childrenNode2 = JsonUtil.getMapper().createObjectNode();
		childrenNode2.put("key", "361");
		childrenNode2.put("value", "361");
		children2.add(childrenNode2);

		childrenNode2 = JsonUtil.getMapper().createObjectNode();
		childrenNode2.put("key", "adidas");
		childrenNode2.put("value", "阿迪达斯");
		children2.add(childrenNode2);
		
		createObjectNode.set("children", children);
		createObjectNode1.set("children", children1);
		createObjectNode2.set("children", children2);
		createArrayNode.add(createObjectNode);
		createArrayNode.add(createObjectNode1);
		createArrayNode.add(createObjectNode2);
		return createArrayNode;
	}

	@RequestMapping(value="getCascaderData", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "级联选择", httpMethod = "GET", notes = "级联选择")
	public Object getCascaderData( @ApiParam(name = "value", value = "",required=false) 
	@RequestParam(required=false) String value) throws Exception {
		ArrayNode createArrayNode = JsonUtil.getMapper().createArrayNode();
		ObjectNode createObjectNode = JsonUtil.getMapper().createObjectNode();
		if(StringUtil.isEmpty(value)){
			value = "0";
		}
		switch (value) {
		case "1":
			createObjectNode.put("value", "11");
			createObjectNode.put("label", "广州市");
			createObjectNode.put("leaf", true);
			createArrayNode.add(createObjectNode);
			
			createObjectNode = JsonUtil.getMapper().createObjectNode();
			createObjectNode.put("value", "12");
			createObjectNode.put("label", "阳江市");
			createObjectNode.put("leaf", true);
			createArrayNode.add(createObjectNode);
			break;
		case "2":
			createObjectNode.put("value", "21");
			createObjectNode.put("label", "东城区");
			createObjectNode.put("leaf", true);
			createArrayNode.add(createObjectNode);
			
			createObjectNode = JsonUtil.getMapper().createObjectNode();
			createObjectNode.put("value", "22"+value);
			createObjectNode.put("label", "西城区"+value);
			createObjectNode.put("leaf", true);
			createArrayNode.add(createObjectNode);
			break;

		default:
			createObjectNode.put("value", "1");
			createObjectNode.put("label", "广东省");
			createObjectNode.put("leaf", false);
			createArrayNode.add(createObjectNode);
			
			createObjectNode = JsonUtil.getMapper().createObjectNode();
			createObjectNode.put("value", "2");
			createObjectNode.put("label", "北京市");
			createObjectNode.put("leaf", false);
			createArrayNode.add(createObjectNode);
			break;
		}
		
		return createArrayNode;
	}

	
}
