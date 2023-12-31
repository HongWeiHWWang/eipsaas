package com.hotent.form.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.form.model.CustomDialog;
import com.hotent.form.persistence.manager.CustomDialogManager;

public class FreeMakerUtil
{
	private static final String DATE_COMPARE_VALID_NAME="daterangestart,daterangeend,datemorethan,datelessthan,";
	// 指令，如果为空则不添加
	public String getAttrs(String attrNames,Object f) throws IOException{
		StringBuffer sb = new StringBuffer();
		JsonNode field = null;
		try {
			field = JsonUtil.toJsonNode(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonNode option = field.get("options");

		if(BeanUtils.isEmpty(option)){
			return "";
		}

		String [] attrs = attrNames.split(",");

		String refId = "";
		for (String attr : attrs) {
			String attrStr = "" ;
			if("htfuncexp".equals(attr) && option.has("statFun")){
				attrStr = option.get("statFun").asText();
			}
			else if(":linkage".equals(attr) && option.has("linkage")) {
				JsonNode linkageObj;
				ObjectMapper mapper = new ObjectMapper();
				ArrayNode arrayNode2=mapper.createArrayNode();
				try {
					linkageObj = JsonUtil.toJsonNode(option.get("linkage").toString());
					if(BeanUtils.isNotEmpty(linkageObj)) {
						for (JsonNode jsonNode : linkageObj) {
							ObjectNode linkage = (ObjectNode) jsonNode;
							if(linkage.has("effect")){
								ArrayNode arrayNode=mapper.createArrayNode();
								JsonNode effectNode=linkage.get("effect");
								for (JsonNode jsonNode2 : effectNode) {
									if(jsonNode2.has("rules")) {
										ObjectNode validate =(ObjectNode) jsonNode2;
										ArrayNode array =(ArrayNode) validate.get("rules");
										validate.remove("rules");

										ObjectNode _validate = JsonUtil.getMapper().createObjectNode();
										changeValidate(array,_validate);
										Map<Object,String> map = getValidateStr(_validate);
                                        String validateStr =map.get("validateStr");
										validate.put("validate", validateStr);
										arrayNode.add(validate);
									}else{
										arrayNode.add(jsonNode2);
									}
								}
								if(BeanUtils.isNotEmpty(arrayNode)) {
									linkage.remove("effect");
									linkage.set("effect", arrayNode);
								}
							}
							arrayNode2.add(linkage);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				attrStr = arrayNode2.toString();
			}
			else if("selectquery".equals(attr)){
				attrStr =BeanUtils.isNotEmpty(option.get("jlsz"))?option.get("jlsz").asText(""):"";
			}
			else if("ht-office-plugin".equals(attr) ){
				sb.append(" style='width:"+option.get("width").asText()+"px;height:"+option.get("height").asText()+"px' ");

				if(option.has("doctype")) sb.append(" doctype='"+option.get("doctype").asText()+"'  ");

				sb.append(" ht-office-plugin ");
			}
			// 下拉框是否多选
			else if(":multiple".equals(attr)){
				if(field.has("isMultiple")&&field.get("isMultiple").asBoolean()){
					attrStr = "true";
				}else{
					attrStr = "false";
				}
			}
			// 下拉框是否支持搜索
			else if(":filterable".equals(attr)){
				if(option.has("filterable")&&option.get("filterable").asBoolean()){
					attrStr = "true";
				}else{
					attrStr = "false";
				}
			}
			// 下拉框是否支持搜索
			else if(":allowCreate".equals(attr)){
				if(option.has("filterable")&&option.get("filterable").asBoolean() && option.has("allowCreate") && option.get("allowCreate").asBoolean() ){
					attrStr = "true";
				}else{
					attrStr = "false";
				}
			}
			// 绑定关联查询(例如：用户选择下拉框时触发关联查询)
			else if(":related-query".equals(attr)){
				if(option.has("customQuery")) {
					attrStr = getRelatedQuery(option);
				}
			}
			else if( "tooltipplacement".equals(attr) ) {
				if(field.has("tooltipplacement")){
					attrStr = field.get("tooltipplacement").asText();
				}
			}else if( "propConf".equals(attr) ) {
				if(option.has("propConf")){
					attrStr = JsonUtil.toJson(option.get("propConf"));
				}
			}else if( "accept".equals(attr) ) {
				JsonNode file = option.get("file");
				if(BeanUtils.isNotEmpty(file)) {
					String acceptType = "custom";
					if(file.has("acceptType")){
						acceptType = file.get("acceptType").asText();
					}
					if("custom".equals(acceptType)){
						if(file.has("accept")){
							if(file.get("accept").isArray()) {
								ArrayNode array=(ArrayNode) file.get("accept");
								List files=new ArrayList();
								for (int i = 0; i < array.size(); i++) {
									files.add(array.get(i).asText());
								}
								attrStr=StringUtils.join(files, ",");
							}
						}
						String acceptStr = "";
						if(file.has("acceptStr")){
							acceptStr = file.get("acceptStr").asText();
						}
						if(StringUtil.isNotEmpty(acceptStr)){
							if(StringUtil.isNotEmpty(attrStr)){
								attrStr += ",";
							}
							attrStr += acceptStr;
						}
					}
				}
			}
			// eg: ht-number-format='{formatJson} '
			if(StringUtil.isNotEmpty(attrStr)){
				sb.append(attr).append("='").append(attrStr).append("' ");
			}
		}
		if(StringUtil.isNotEmpty(refId)){
			sb.append("name").append("=\"").append(refId).append("\" ");

		}

		return sb.toString();
	}
	
	// 构建绑定关联查询
	private String getRelatedQuery(JsonNode option) throws IOException {
		String result = "";
		JsonNode customQuery = JsonUtil.toJsonNode(option.get("customQuery"));
		if(customQuery!=null && customQuery.has("custQueryJson")) {
			JsonNode custQueryJson = JsonUtil.toJsonNode(customQuery.get("custQueryJson"));
			if(custQueryJson!=null && custQueryJson.isArray()) {
				ArrayNode custQuery = (ArrayNode)custQueryJson;
				if(custQuery!=null && custQuery.size() > 0) {
					ArrayNode ary = JsonUtil.getMapper().createArrayNode();
					custQuery.forEach(q -> {
						if(q!=null && q.isObject()) {
							try {
								ObjectNode oldObj = (ObjectNode)q;
								ObjectNode newObj = JsonUtil.getMapper().createObjectNode();
								newObj.put("alias", JsonUtil.getString(oldObj, "alias"));
								JsonNode conditionfield = JsonUtil.toJsonNode(oldObj.get("conditionfield"));
								if(conditionfield!=null && conditionfield.isArray()) {
									ArrayNode conditionAry = (ArrayNode)conditionfield;
									if(conditionAry!=null && conditionAry.size() > 0) {
										ObjectNode newCaObj = JsonUtil.getMapper().createObjectNode();
										conditionAry.forEach(ca -> {
											if(ca!=null && ca.isObject()) {
												ObjectNode caObj = (ObjectNode)ca;
												newCaObj.put(JsonUtil.getString(caObj, "field"), JsonUtil.getString(caObj, "fieldPath"));
											}
										});
										newObj.set("condition", newCaObj);
									}
								}
								JsonNode resultfield = JsonUtil.toJsonNode(oldObj.get("resultfield"));
								if(resultfield!=null && resultfield.isArray()) {
									ArrayNode resultAry = (ArrayNode)resultfield;
									if(resultAry!=null && resultAry.size() > 0) {
										ObjectNode newRtObj = JsonUtil.getMapper().createObjectNode();
										resultAry.forEach(rt -> {
											if(rt!=null && rt.isObject()) {
												ObjectNode rtObj = (ObjectNode)rt;
												newRtObj.put(JsonUtil.getString(rtObj, "field"), JsonUtil.getString(rtObj, "fieldPath"));
											}
										});
										newObj.set("result", newRtObj);
									}
								}
								ary.add(newObj);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					result = JsonUtil.toJson(ary);
				}
			}
		}
		return result;
	}

	//单行文本高级属性配置
	public  String getInputAdvancedAttributes(Object str) throws IOException {
		JsonNode obj=JsonUtil.toJsonNode(str);
		ObjectNode node=JsonUtil.getMapper().createObjectNode();
		List<String> list=new ArrayList<>();
		if(obj.has("isInputEdit")&& obj.get("isInputEdit").asBoolean()){
			node.put("isInputEdit",true);
			list.add("isInputEdit:true");
		}
		if(obj.has("inputType") &&obj.get("inputType").asBoolean()){
			list.add("isPassword:true");
		}
		if(obj.has("isBindIdentity") && obj.get("isBindIdentity").asBoolean()&&obj.get("bindIdentityjson").has("alias")){
			list.add("bindIdentityAlias:'"+obj.get("bindIdentityjson").get("alias").asText()+"'");
		}
		if(list.size()==0){
			return  "{}";
		}else{
			return  "{"+StringUtils.join(list,",")+"}";
		}
	}

	//联动设置
	public String getLinkage(Object obj) throws IOException{
		JsonNode options = JsonUtil.toJsonNode(obj);
		Map<String,List<JsonNode>> map=new HashMap<>();
		if(options.has("linkage")){
			JsonNode linkage=options.get("linkage");
			linkage.forEach(item->{
				String key=item.get("value").asText();
				JsonNode effect=item.get("effect");
				List<JsonNode> listNode=new ArrayList<>();
				if(map.containsKey(key)){
					listNode=map.get(key);
				}
				for (int i = 0; i <effect.size() ; i++) {
					listNode.add(effect.get(i));
				}
				map.put(key,listNode);
			});
		}
		List<String> jsonStr=new ArrayList<>();
		map.forEach((key,val)->{
			List<String> effectList=new ArrayList<>();
			val.forEach(item->{
				if(BeanUtils.isNotEmpty(item.get("target"))) {
                    String[] target= item.get("target").asText().split("\\|");
                    String validate = item.get("validateObj").get("options").get("validate").asText();
                    if (StringUtil.isNotEmpty(validate)) {
                        effectList.add("{ref: 'data." + target[0] + "', type: '+',value: " + validate + "}");
                    }
                    effectList.add("{target: 'permission.fields." + target[1] + "', type: '=',value: '" + item.get("type").asText() + "'}");
                }
                //控制整个子表的显示/隐藏/必填(必须有一行)
                if(BeanUtils.isNotEmpty(item.get("targetSub"))) {
                    String[] targetSub = item.get("targetSub").asText().split("\\|");
                    if(item.get("type").asText().equals("n")){
                        effectList.add("{target: 'permission.table." + targetSub[0] + ".hidden', type: '=',value: true}");
                    }else if(item.get("type").asText().equals("w")){
                        effectList.add("{target: 'permission.table." + targetSub[0] + ".hidden', type: '=',value: false}");
                    }else if(item.get("type").asText().equals("b")){
                        effectList.add("{target: 'permission.table." + targetSub[0] + ".required', type: '=',value: true}");
                    }

                }
			});
			jsonStr.add("{value:'"+key+"',effect:["+StringUtils.join(effectList,",")+"]}");
		});
		if(jsonStr.size()>0){
			String linkage="["+StringUtils.join(jsonStr,",")+"]";
			return  ":linkage=\""+linkage+"\"";
		}
	 	return "";
	}

	//  转换为 "required|email|between:2,10"
	//如果包含confirmed验证两次输入字符是否一致，值不能带双引号，做特殊处理
	private Map<Object,String> getValidateStr(ObjectNode validate) {
        Map<Object,String> map = new HashMap<>();
		String validateStr = "";
		StringBuffer validatesb=new StringBuffer();
		validatesb.append("");
        //数据是否一致校验单独抽取处理加在最后
		String confirmedKey = "";
        String confirmedVal = "";
		for (Iterator<Entry<String, JsonNode>> iterator = validate.fields(); iterator.hasNext();) {
			Entry<String, JsonNode> ent =  iterator.next();
            JsonNode value = ent.getValue();
            if("false".equals(value.asText())){
                continue;
            }
			if("confirmed".equals(ent.getKey())) {
                confirmedKey = ent.getKey()+":";
                confirmedVal = "data."+value.asText();
			}else {
				if("backendValidate".equals(ent.getKey())){
					boolean submitFormData = false;
					if(value.has("submitFormData" )){
						submitFormData = value.get("submitFormData").asBoolean();
					}
					validatesb.append(ent.getKey()+":"+value.get("url").asText()+","+submitFormData);
				}else{

					validatesb.append(ent.getKey()+":"+value.asText());
				}
			}
			if(iterator.hasNext()) {
				validatesb.append("|");
			}
		}
		validateStr =validatesb.toString()+confirmedKey;
        map.put("validateStr", validateStr);
        map.put("confirmedVal", confirmedVal);
		return map;
	}

	private void changeValidate(ArrayNode array, ObjectNode validate) {
		for (JsonNode rule : array) {
			if(validate.has(rule.get("text").asText())) {
				validate.remove(rule.get("text").asText());
			}
			if(rule.has("value")) {
				String value = rule.get("value").asText();
				if("backendValidate".equals(rule.get("text").asText())){
					ObjectNode createObjectNode = JsonUtil.getMapper().createObjectNode();
					createObjectNode.put("url", value);
					createObjectNode.put("submitFormData", rule.has("submitFormData")? rule.get("submitFormData").asBoolean():false);
					validate.set(rule.get("text").asText(),createObjectNode);
				}else{
					validate.put(rule.get("text").asText(),value);
				}

			}else {
				validate.put(rule.get("text").asText(),true);
			}
		}
	}

	public  String getDatecalc(JsonNode obj){
        String ref=   "{\"start\":"+obj.get("start").asText()+",\"diffType\":\""+obj.get("diffType").asText()+"\",\"end\":"+obj.get("end").asText()+"}";
        return ref;
    }
	public String getCtrlDate(Object field){
		JsonNode tmp = null;
		try {
			tmp = JsonUtil.toJsonNode(field);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		JsonNode option = tmp.get("options");
		String attrStr =option.get("dataFormat").asText();
		if(StringUtil.isEmpty(attrStr)){
			return "mobiscrollDate=date'";
		}
		//{"dataFormat":"yyyy-MM-dd HH:mm:ss"}
		if("yyyy-MM-dd HH:mm:ss".equals(attrStr)){
			return "mobiscrollDate='datetime'";
		}

		if("HH:mm:ss".equals(attrStr)){
			return "mobiscrollDate='time'";
		}

		return "mobiscrollDate='date'";
	}



	/**
	 * 通过json字符串获取attr属性
	 *
	 * @param o
	 * @param path
	 * @return
	 */
	public String getJsonByPath(Object o, String path)
	{

        return getJsonByPath(o,path,"");
	}

    /**
     * 通过json字符串获取attr属性
     *
     * @param o
     * @param path
     * @param defaultVal
     * @return
     */
    public String getJsonByPath(Object o, String path,String defaultVal)
    {
    	try {
            if(BeanUtils.isEmpty(o)){
                return defaultVal;
            }

            JsonNode jsonObject = JsonUtil.toJsonNode(o);

            String[] pathList = path.split("\\.");
            if (pathList.length > 1 && jsonObject.has(pathList[0]) )
            {
                String tempJson = jsonObject.get(pathList[0]).asText();
                if(StringUtil.isEmpty(tempJson)){
                	return getJsonByPath(jsonObject.get(pathList[0]), StringUtils.join(ArrayUtils.remove(pathList, 0), "."));
                }
                return getJsonByPath(tempJson, StringUtils.join(ArrayUtils.remove(pathList, 0), "."));
            } else if (jsonObject.has(path)){
                JsonNode jsonNode = jsonObject.get(path);
                if(jsonNode.isValueNode()){
                    String asText = jsonNode.asText();
                    return asText;
                }
                else if(jsonNode.isArray()){
                    String json = JsonUtil.toJson(jsonNode);
                    return json;
                }else if(jsonNode.isObject()){
                	String json = JsonUtil.toJson(jsonNode);
                    return json;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException("模板解析异常。");
        }
        return defaultVal;
    }

    /**
     * 根据路径获取属性中的数据列表
     * @param o
     * @param path
     * @return
     * @throws IOException
     */
    public List getListByPath(Object o, String path) throws IOException{
    	List list = new ArrayList<>();
    	String jsonStr = getJsonByPath(o, path);
    	if(StringUtil.isNotEmpty(jsonStr)){
    		if(jsonStr.indexOf("\"")==0){
    			jsonStr = jsonStr.replaceFirst("\"", "");
    			jsonStr = jsonStr.replaceAll("]\"", "]");
    			jsonStr = jsonStr.replaceAll("\\\\","");
    		}
    		ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(jsonStr);
    		for (JsonNode node : array) {
    			list.add(JsonUtil.toMap(JsonUtil.toJson(node)));
			}
    	}
    	return list;
    }




	public String getSubList(String jsonList, int begin, int end)
	{
		String[] array = jsonList.split(",");
		String rtn = "";
		for (int i = 0; i < array.length && (i >= begin && i <= end); i++)
		{
			rtn += array[i] + ",";
		}
		return rtn.substring(0, rtn.length() - 1);
	}



	/**
	 * 解析表单字段的option字段，生成ht-complex指令的配置json(selector类型)
	 *
	 * @param json
	 * @return
	 */
	public String getHtSelector(Object option,Boolean tag)
	{
		if(BeanUtils.isEmpty(option))
			return "{}";
		ObjectNode fromObject = null;
		try {
			fromObject = (ObjectNode) JsonUtil.toJsonNode(option);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(tag == null) tag=false;

		ObjectNode returnObj = JsonUtil.getMapper().createObjectNode();

		JsonNode selectorObj = fromObject.get("selector");

		if (BeanUtils.isNotEmpty(selectorObj))
		{
			returnObj.put("isSingle", selectorObj.has("isSingle")? selectorObj.get("isSingle").asBoolean():false);
			returnObj.put("selectCurrent", selectorObj.has("selectCurrent")? selectorObj.get("selectCurrent").asBoolean():false);

			JsonNode typeObj =  null;

			if(selectorObj.has("type")){
				typeObj = selectorObj.get("type");
			}

			if (BeanUtils.isNotEmpty(typeObj))
			{
				returnObj.put("type",typeObj.has("alias")? typeObj.get("alias").asText():"");
			}
		}
		ArrayNode bindAry = null;
		if (fromObject.has("bind"))
			bindAry = (ArrayNode) fromObject.get("bind");
		ObjectNode bindObj = JsonUtil.getMapper().createObjectNode();
		if(BeanUtils.isNotEmpty(bindAry)){
			for (JsonNode jobject : bindAry) {
				String key =  jobject.has("key")? jobject.get("key").asText():"";
				if(!jobject.has("jsonPath")) {
					continue;
				}
				String jsonPath = jobject.get("jsonPath").asText();
				String path = "data.";
				// 子表
				if (tag)
				{
					path = "data.";
				}
				path += jsonPath;
				bindObj.put(key, path);
			}
		}

		returnObj.set("bind", bindObj);
		String returnStr = returnObj.toString();
		if (StringUtil.isEmpty(returnStr))
			returnStr = "";
		return returnStr.replaceAll("\"", "'");
	}

	/**
	 *
	 * @param option
	 * @param tag
	 * @return
	 */
	public String getHtSelectorBind(Object option,Boolean tag)
	{
		ObjectNode fromObject = null;
		try {
			fromObject = (ObjectNode) JsonUtil.toJsonNode(option);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayNode bindAry = null;
		if (fromObject.has("bind"))
			bindAry = (ArrayNode) fromObject.get("bind");
		ObjectNode bindObj = JsonUtil.getMapper().createObjectNode();
		if(BeanUtils.isNotEmpty(bindAry)){
			for (JsonNode jobject : bindAry) {
				String key =  jobject.has("key")? jobject.get("key").asText():"";
				if(!jobject.has("jsonPath")) {
					continue;
				}
				String jsonPath = jobject.get("jsonPath").asText();
				String path = "data.";
				// 子表
				if (tag)
				{
					path = "data.";
				}
				path += jsonPath;
				bindObj.put(key, path);
			}
		}
		return bindObj.toString().replaceAll("\"", "'");

	}
	/**
	 * 解析表单字段的option字段，生成ht-complex指令的配置json(selector类型)
	 *
	 * @param json
	 * @return
	 */
	public String getHtSelectorType(Object option,Boolean tag)
	{
		if(BeanUtils.isEmpty(option))
			return "{}";
		ObjectNode fromObject = null;
		try {
			fromObject = (ObjectNode) JsonUtil.toJsonNode(option);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(tag == null) tag=false;

		ObjectNode returnObj = JsonUtil.getMapper().createObjectNode();

		JsonNode selectorObj = fromObject.get("selector");

		if (BeanUtils.isNotEmpty(selectorObj)){
			JsonNode typeObj =  null;

			if(selectorObj.has("type")){
				typeObj = selectorObj.get("type");
			}

			if (BeanUtils.isNotEmpty(typeObj))
			{
				returnObj.put("type",typeObj.has("alias")? typeObj.get("alias").asText():"");
			}
		}

		if(BeanUtils.isEmpty(returnObj.get("type"))) {
			return "{}";
		}
		return returnObj.get("type").asText();
	}


	/**
	 * 解析表单字段的option字段
	 *
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public String getSelectQuery(Object option ,Boolean isSub) throws IOException
	{
		if(isSub == null) isSub=false;
		if (BeanUtils.isEmpty(option))
			return "{}";
		ObjectNode returnObj = JsonUtil.getMapper().createObjectNode();
		ObjectNode fromObject = null;
		try {
			fromObject = (ObjectNode) JsonUtil.toJsonNode(option);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectNode customQuery = null;
		if(fromObject.has("customQuery")){
			customQuery = (ObjectNode) fromObject.get("customQuery");
		}

		if (BeanUtils.isNotEmpty(customQuery))
		{
			returnObj.put("alias", customQuery.has("alias")? customQuery.get("alias").asText():"");
			returnObj.put("valueBind",customQuery.has("valueBind")? customQuery.get("valueBind").asText():"");
			returnObj.put("noInit", customQuery.has("noInit")? customQuery.get("noInit").asText():"");
			returnObj.put("labelBind", customQuery.has("labelBind")? customQuery.get("labelBind").asText():"");
			if(fromObject.has("gangedBind")){
				returnObj.put("gangedBind", fromObject.has("gangedBind")? JsonUtil.toJson(fromObject.get("gangedBind")):"");
			}
		}

		ArrayNode bindAry = null;
		if(fromObject.has("bind")){
			bindAry = (ArrayNode) fromObject.get("bind");
		}

		ObjectNode bindObj = JsonUtil.getMapper().createObjectNode();
		if(bindAry != null)
			for (JsonNode jobject : bindAry) {
				ObjectNode target = null;
				if(jobject.has("json")){
					target = (ObjectNode) jobject.get("json");
				}
				String key = jobject.has("field")?  jobject.get("field").asText():"";
				if (BeanUtils.isEmpty(target)){
					String fieldPath = jobject.has("fieldPath")?  jobject.get("fieldPath").asText():"";
					bindObj.put(key, fieldPath);
					continue;
				}

				String path = "data.";
				if(isSub){
					path = "item.";
				}
				else{
					path +=  target.has("path")? target.get("path").asText():"";
					path += ".";
				}

				path += target.has("name")? target.get("name").asText():"";
				bindObj.put(key, path);
			}
		returnObj.set("bind", bindObj);
		String returnStr = returnObj.toString();
		if (StringUtil.isEmpty(returnStr))
			returnStr = "";
		return returnStr.replaceAll("\"", "'");
	}

	/**
	 * 获取字段联动信息
	 * @param ptName
	 * @param gangedStr
	 * @return
	 */
	public String getFieldGanged(String ptName,String gangedStr){
		String returnStr = "";
		try {
			ArrayNode returnArray = JsonUtil.getMapper().createArrayNode();
			ArrayNode array =(ArrayNode) JsonUtil.toJsonNode(gangedStr);
			if(BeanUtils.isNotEmpty(array)){
				for (JsonNode obj : array) {
					ArrayNode chooseFields = (ArrayNode) obj.get("chooseField");
					for (JsonNode chooseField : chooseFields) {
						if(ptName.equals(chooseField.get("pathName").asText())){
							returnArray.add(obj);
						}
					}

				}
			}
			if(BeanUtils.isNotEmpty(returnArray)){
				returnStr = "ht-ganged=\""+returnArray.toString().replaceAll("\"", "'")+"\"";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	public String getSeparator(String separator){
		String returnStr = "";
		try {
			if(StringUtil.isNotEmpty(separator)){
				returnStr = "group='"+separator+"'";
			}
		} catch (Exception e) {}
		return returnStr;
	}

	//获取字段显示权限（主要是处理流程字段）
	public String getPermissionNgif(Object field){
		try {
			JsonNode fieldJson = JsonUtil.toJsonNode(field);
			if(fieldJson.has("boAttrId")){
				return "ng-if=\"permission.fields."+fieldJson.get("tableName").asText()+"."+fieldJson.get("name").asText()+"!='n'\"";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ng-if='true'";
	}

	//获取字段显示权限（主要是处理流程字段）
	public String getStringConf(Object o){
		if(BeanUtils.isEmpty(o)) return "";
		try {
			return JsonUtil.toJson(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getIncludeFiles(String files) throws Exception{
		//if(StringUtil.isNotEmpty(files)){
		//	String jsonStr = Base64.getFromBase64(files);
		//	if(StringUtil.isNotEmpty(jsonStr.trim())){
		//		StringBuilder html = new StringBuilder();
		//		ObjectNode node = (ObjectNode) JsonUtil.toJsonNode(jsonStr);
        //        String diyFile = " ";
        //        String diyCss = " ";
        //        String diyJs = " ";
		//		if(BeanUtils.isNotEmpty(node.get("diyFile"))){
        //            diyFile = node.get("diyFile").asText();
        //        }else if(BeanUtils.isNotEmpty(node.get("diyCss"))){
        //            diyCss = node.get("diyCss").asText();
        //        }else if(BeanUtils.isNotEmpty(node.get("diyJs"))){
        //            diyJs = node.get("diyJs").asText();
        //        }
		//		if(StringUtil.isNotEmpty(diyFile.trim())){
		//			html.append(diyFile);
		//		}
		//		if(StringUtil.isNotEmpty(diyCss.trim())){
		//			html.append("<style type=\"text/css\">");
		//			html.append(diyCss);
		//			html.append("</style>");
		//		}
		//		if(StringUtil.isNotEmpty(diyJs.trim())){
		//			html.append("<script type=\"text/javascript\">");
		//			html.append(diyJs);
		//			html.append("</script>");
		//		}
		//		return html.toString();
		//	}
		//}
		return "";
	}

	public String getCustDialogAttr(String colPrefix,Object f) throws IOException{
		JsonNode field = null;
		try {
			field = JsonUtil.toJsonNode(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonNode controlContent = field.get("controlContent");
		if (controlContent instanceof TextNode) {
			controlContent = JsonUtil.toJsonNode(controlContent.asText());
		}
		if(BeanUtils.isNotEmpty(controlContent)){
			String alias = controlContent.get("alias").asText();
			CustomDialog customDialog = null;
			if (StringUtil.isNotEmpty(alias)) {
				try {
					CustomDialogManager customDialogManager = AppUtil.getBean(CustomDialogManager.class);
					customDialog = customDialogManager.getByAlias(alias);
					if(BeanUtils.isNotEmpty(customDialog)){
						String resultField  = "";
						if (controlContent.hasNonNull("resultField")) {
							resultField = controlContent.get("resultField").asText();
						}else if (controlContent.hasNonNull("resultfield")) {
							resultField = controlContent.get("resultfield").asText();
						}
						ObjectNode custDialog = JsonUtil.getMapper().createObjectNode();
						custDialog.put("alias", alias);
						custDialog.put("type", "custDialog");
						custDialog.set("conditions", JsonUtil.getMapper().createArrayNode());
						custDialog.set("resultField", JsonUtil.toJsonNode(customDialog.getResultfield()));
						ArrayNode mappingConf = JsonUtil.getMapper().createArrayNode();
						ArrayNode mappingConfs = JsonUtil.getMapper().createArrayNode();
						ObjectNode dialogConf = JsonUtil.getMapper().createObjectNode();
						ObjectNode mapping = JsonUtil.getMapper().createObjectNode();
						mapping.put("from", resultField);
						if(colPrefix == null){
							colPrefix = "";
						}
						if (field.hasNonNull("na")) {
							mapping.set("target", mappingConfs.add(colPrefix+field.get("na").asText()));
						}
						mappingConf.add(mapping);
						custDialog.set("mappingConf", mappingConf);
						dialogConf.set("custDialog", custDialog);
						dialogConf.put("name", "选择");
						dialogConf.put("forSearch", true);
						return JsonUtil.toJson(dialogConf);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		return "";
	}

	public String getStyleBold(Object field,String attr){
		String sty = "normal";
		JsonNode fieldJson = null;
		try {
			fieldJson = JsonUtil.toJsonNode(field);
			if(BeanUtils.isNotEmpty(fieldJson.get(attr))){
				sty = fieldJson.get(attr).asBoolean()?"bold":"normal";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sty;
	}

	public String getStyles(Object field,String name,String attr){
		String sty = "";
		JsonNode fieldJson = null;
		try {
			fieldJson = JsonUtil.toJsonNode(field);
			if(BeanUtils.isNotEmpty(fieldJson.get(attr)) && !fieldJson.get(attr).isNull()){
				if("textSize".equals(attr)) {
					sty = name + ":" + fieldJson.get(attr).asText()+"px;";
				}else {
					sty = name + ":" + fieldJson.get(attr).asText()+";";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sty;
	}

	public String getAttrText(Object field,String options,String attr){
		String sty = "";
		JsonNode fieldJson = null;
		try {
			fieldJson = JsonUtil.toJsonNode(field);
			if(BeanUtils.isNotEmpty(fieldJson.get(options).get(attr)) && !fieldJson.get(options).get(attr).isNull()){
				sty = fieldJson.get(options).get(attr).asText();
			}
			if("customWidth".equals(attr) ||"customHeight".equals(attr)   ) {
				sty = "100%";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sty;
	}

	//固定文本字体大小样式
    public String getTextFixedStyles(Object field,String name,String attr){
        String sty = "";
        JsonNode fieldJson = null;
        try {
            fieldJson = JsonUtil.toJsonNode(field);
            if(BeanUtils.isNotEmpty(fieldJson.get(attr)) && !fieldJson.get(attr).isNull()){
                if("textFixedSize".equals(attr)) {
                    sty = name + ":" + fieldJson.get(attr).asText()+"px;";
                }else {
                    sty = name + ":" + fieldJson.get(attr).asText()+";";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sty;
    }

	public boolean getRequired(Object field){
		boolean isRequired = false;
		try {
			JsonNode fieldJson = JsonUtil.toJsonNode(field);
			if(BeanUtils.isNotEmpty(fieldJson.get("validRule"))){
				ObjectNode validate = (ObjectNode) fieldJson.get("validRule");
				if(validate.has("required")){
					isRequired = validate.get("required").asBoolean();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isRequired;
	}

	public String getWidth(Integer maxCol, Integer col) {
		if(maxCol==1) {
			return "80%";
		}
		else if(col == 2){
			return "40%";
		}
		else if(col == 3) {
			return "23%";
		}
		else if(col == 4) {
			return "15%";
		}
		else {
			return "";
		}
	}

	/**
	 * 根据当前行中最大列及索引获取colspan
	 * @param maxCol	当前表单最多列值
	 * @param col		当前行最多列值
	 * @param index		当前是第几列
	 * @return
	 */
	public String getColspan(Integer maxCol, Integer col, Integer index, Boolean isText) {
		String result = "";
        //switch(col){
        //    case 1:
        //        result = "colspan=\"23\"";
        //        if(isText) {
        //            result = "colspan=\"24\"";
        //        }
        //        break;
        //    case 2:
        //        result = "colspan=\"11\"";
        //        if(isText) {
        //            result = "colspan=\"12\"";
        //        }
        //        break;
        //    case 3:
        //        result = "colspan=\"7\"";
        //        if(isText) {
        //            result = "colspan=\"8\"";
        //        }
        //        break;
        //    case 4:
        //        result = "colspan=\"5\"";
        //        if(isText) {
        //            result = "colspan=\"6\"";
        //        }
        //        break;
        //}
		if(maxCol < 1 || col < 1 || index < 1 || maxCol <= col || index > col || maxCol > 4) {
			return result;
		}
		switch(maxCol-col) {
			case 1:
				if(col==index) {
					result = "colspan=\"3\"";
					if(isText) {
						result = "colspan=\"4\"";
					}
				}
				break;
			case 2:
				if(maxCol==3 && index==col) {
					result = "colspan=\"7\"";
					if(isText) {
						result = "colspan=\"6\"";
					}
				}
				else if(maxCol==4){
					result = "colspan=\"3\"";
					if(isText) {
						result = "colspan=\"4\"";
					}
				}
				break;
			case 3:
				result = "colspan=\"7\"";
				if(isText) {
					result = "colspan=\"8\"";
				}
				break;
		}

		return result;
	}

	/**
	 * 将字符串base64加密
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getBase64(String str) throws UnsupportedEncodingException{
		if(StringUtil.isNotEmpty(str)){
			return Base64.getBase64(str);
		}
		return str;
	}

	/**
	 * 将base64加密字符串解密
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String decodeBase64(String str) throws UnsupportedEncodingException{
		if(StringUtil.isNotEmpty(str)){
			return Base64.getFromBase64(str);
		}
		return str;
	}

	/**
	 *
	 * @param field
	 * @param attr
	 * @return "{width:20px,height:20%}"
	 */
	public String getMapString(Object field,String attr){
		String result = "{}";
		try {
			JsonNode fieldJson = JsonUtil.toJsonNode(field);
			if(BeanUtils.isNotEmpty(fieldJson.has(attr))){
				JsonNode jsonNode = fieldJson.get(attr);
				if(BeanUtils.isEmpty(jsonNode)){
				    return result;
                }
				result = jsonNode.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;


	}
	/**
	 * 转json 字符串
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public String objectToJsonString(Object obj) throws IOException{
			 return JsonUtil.toJson(obj);
	}
	/**
	 * 标题字体样式,1.加粗2.隐藏3.颜色
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public String getFieldStyle(Object field) throws IOException{
		ObjectNode fieldJson = (ObjectNode) JsonUtil.toJsonNode(field);
		boolean boldLable=JsonUtil.getBoolean(fieldJson, "boldLable", false);
		String lableColor = JsonUtil.getString(fieldJson, "lableColor", "");
		StringBuffer str = new StringBuffer();
		if(boldLable){
			str.append("font-weight:bolder;");
			str.append("font-size:small;");
		}
		if(StringUtil.isNotEmpty(lableColor)){
			str.append("color:");
			str.append(lableColor+";");
		}
        str.append("margin-left: 5px;");
		return str.toString();
	}

	/**
	 * 获取孙表数据路径
	 * @param path
	 * @param indexStr
	 * @return
	 * @throws IOException
	 */
	public String getSunTablePath(String path,String indexStr) throws IOException{
		if(StringUtil.isNotEmpty(path)){
			String[] paths = path.split("\\.");
			if(paths.length==3){
				return paths[0]+"."+paths[1]+"["+indexStr+"]"+"."+paths[2];
			}
		}
		return path;
	}

	/**
	 * 获取孙表的数据路径，index为v-for中的index值而不是index字符串
	 * @param path
	 * @param indexStr
	 * @return
	 * @throws IOException
	 */
	public String getSubName(String path,String indexStr) throws IOException{
		if (StringUtil.isNotEmpty(path)){
			String[] paths = path.split("\\.");
			if (paths.length==3){
				return paths[0]+"."+paths[1]+"['+"+indexStr+"+']"+"."+paths[2];
			}
		}
		return path;
	}

	public String toJsonStr(Object object) throws IOException {
		String str = JsonUtil.toJson(object);
		str=Base64.getBase64(str);
		return str;
	}


	//获取子表里面的孙表BO实体集合
    public String getSunBos(List<ObjectNode> list) throws IOException {
        String str = "";
        if (BeanUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (("suntable").equals(JsonUtil.toJsonNode(JsonUtil.toJson(list.get(i))).get("ctrlType").asText())) {
                    str = str+JsonUtil.toJsonNode(JsonUtil.toJson(list.get(i))).get("name").asText()+",";
                }
            }
        }
        if(StringUtil.isNotEmpty(str)){
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    
    /**
	 * 获取合并单元格数量
	 * @param options
	 * @return
	 * @throws IOException
	 */
	public int getMergeCellSize(Object options) throws IOException {
		int size = 0;
		try {
			if(BeanUtils.isNotEmpty(options)){
				JsonNode optionsNode = JsonUtil.toJsonNode(options);
				if(BeanUtils.isNotEmpty(optionsNode.get("colHeadersRelations"))){
					ArrayNode colHeadersRelations = (ArrayNode)optionsNode.get("colHeadersRelations");
					for (JsonNode jsonNode : colHeadersRelations) {
						if(BeanUtils.isNotEmpty(jsonNode.get("column"))){
							ObjectNode node = (ObjectNode) jsonNode.get("column");
							if(BeanUtils.isNotEmpty(node.get("mergeCell")) && node.get("mergeCell").asBoolean()){
								size++;
							}
						}
					}
				}
			}
		} catch (Exception e) {}
		return size;
	}
	
	/**
	 * 获取是否包含计算公式
	 * @param options
	 * @return
	 * @throws IOException
	 */
	public int getHasMathExp(Object options) throws IOException {
		int is = 0;
		try {
			if(BeanUtils.isNotEmpty(options)){
				JsonNode optionsNode = JsonUtil.toJsonNode(options);
				if(BeanUtils.isNotEmpty(optionsNode.get("colHeadersRelations"))){
					ArrayNode colHeadersRelations = (ArrayNode)optionsNode.get("colHeadersRelations");
					for (JsonNode jsonNode : colHeadersRelations) {
						if(BeanUtils.isNotEmpty(jsonNode.get("column"))){
							ObjectNode node = (ObjectNode) jsonNode.get("column");
							if(BeanUtils.isNotEmpty(node.get("mathExp")) && StringUtil.isNotEmpty(node.get("mathExp").asText())){
								is = 1;
								break;
							}else if(BeanUtils.isNotEmpty(node.get("rowMathExp")) && StringUtil.isNotEmpty(node.get("rowMathExp").asText())){
								ArrayNode array = (ArrayNode) node.get("rowMathExp");
								if(array.size()>0){
									is = 1;
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {}
		return is;
	}
	
	/**
	 * 获取是否包含计算公式
	 * @param options
	 * @return
	 * @throws IOException
	 */
	public int getHasCrossMapping(Object options) throws IOException {
		int is = 0;
		try {
			if(BeanUtils.isNotEmpty(options)){
				JsonNode optionsNode = JsonUtil.toJsonNode(options);
				if(BeanUtils.isNotEmpty(optionsNode.get("crossMapping"))){
					ArrayNode crossMapping = (ArrayNode)optionsNode.get("crossMapping");
					if(crossMapping.size()>0){
						is = 1;
					}
				}
			}
		} catch (Exception e) {}
		return is;
	}
    
    /**
	 * 获取boDefAlias
	 * @param field
	 * @return
	 * @throws IOException
	 */
	public String getBoDefAlias(Object field) throws IOException {
		String bDefAlias = "";
		try {
			if(BeanUtils.isNotEmpty(field)){
				JsonNode fieldNode = JsonUtil.toJsonNode(field);
				if(BeanUtils.isNotEmpty(fieldNode.get("boDefAlias"))){
					bDefAlias = fieldNode.get("boDefAlias").asText();
				}else if(BeanUtils.isNotEmpty(fieldNode.get("options")) && BeanUtils.isNotEmpty(fieldNode.get("options").get("boDefAlias")) ){
					bDefAlias = fieldNode.get("options").get("boDefAlias").asText();
				}else if(BeanUtils.isNotEmpty(fieldNode.get("fieldPath"))){
					String[] fieldPaths = fieldNode.get("fieldPath").asText().split("\\.");
					bDefAlias = fieldPaths[0];
				}
			}
		} catch (Exception e) {}
		return bDefAlias;
	}
	
	/**
	 * 获取转义后的json字符串
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public String getJsonStr(String str) throws IOException{
		if(StringUtil.isNotEmpty(str)){
			return JsonUtil.toJson(str);
		}
		return "";
	}
}
