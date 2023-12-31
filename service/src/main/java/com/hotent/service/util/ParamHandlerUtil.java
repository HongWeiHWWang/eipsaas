package com.hotent.service.util;

import java.io.IOException;
import java.util.Map;

import javax.xml.ws.WebServiceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.service.exception.InvokeException;

/**
 * 参数处理工具类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public class ParamHandlerUtil {
    public static final String INPUT = "input";
    public static final String FLOW_OUTPUT = "flowOutput";
    /** 参数类型 ：FLOW_INPUT ,FLOW_OUTPUT ,非流程 **/
    private static ThreadLocal < String > jsonType = new ThreadLocal < String > ();
    private static ThreadLocal < Map < String, Object >> params = new ThreadLocal < Map < String, Object >> ();
    /*** 流程中需要webService返回参数的时候， 需要 解析OutPutParam json将 参数的name 返回 */
    private static ThreadLocal < String > flowOutPutKey = new ThreadLocal < String > ();

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object buildJsonParam(ArrayNode jarray, JsonNode jsonParamObj, Map paramMap, String type) {
        // 递归 解析参数所需的变量放在线程里（参数传来传去的， 参数列表太长）
        jsonType.set(type);
        params.set(paramMap);
        flowOutPutKey.set("");
        try {
            buildJsonParams(jarray, jsonParamObj);

            if (FLOW_OUTPUT.equals(type))
                return flowOutPutKey.get();
            return null;
        } catch (Exception e) {
            throw new WebServiceException("webService " + type + "Json 解析出错！");
        } finally {
            jsonType.remove();
            params.remove();
            flowOutPutKey.remove();
        }
    }

    /**
     * 将 符合规则的webservice inputJson 转换成交互所定义的JSON类型
     * 
     * @param jarray
     *            所返回的json
     * @param jsonParamObj
     *            输出参数的值
     * @param params
     *            值
     * @throws IOException 
     * @jsonType 类型
     */
    // 递归构建jsonParam
    private static void buildJsonParams(ArrayNode jarray,JsonNode jsonParamObj) throws IOException {
        for (JsonNode jelement: jarray) {
            if (!jelement.isObject())
                continue;

            ObjectNode jobject = (ObjectNode)jelement;
            String key = jobject.get("key").asText();
            String type = jobject.get("type").asText();
            JsonNode bind = jobject.get("bind"); // bind json 对象
            JsonNode genericsValue = jobject.get("generics"); // 泛型

            Boolean generics = false;
            if (BeanUtils.isNotEmpty(genericsValue)) {
                generics = genericsValue.asBoolean();
            }
            JsonNode paramJson = null;
            if ((!"Bean".equals(type) && !generics) || jsonType.get().equals("flowOutput")) { // 普通的input
                // 或者output
                paramJson = new TextNode("");
                paramJson = handlerBind(bind, paramJson);
            } else if ("Bean".equals(type)) {
                ArrayNode children = (ArrayNode)jobject.get("children");
                paramJson = JsonUtil.getMapper().createObjectNode();

                paramJson = handlerBind(bind, paramJson);
                buildJsonParams(children, paramJson);
            } else if (generics) {
                ArrayNode children = (ArrayNode)jobject.get("children");
                int count = children.size();
                // 一种类型的泛型为 集合 两种类型的泛型为 对象
                paramJson = count == 1 ? JsonUtil.getMapper().createArrayNode() : JsonUtil.getMapper().createObjectNode();

                paramJson = handlerBind(bind, paramJson);
                handlerGenerics(children, paramJson);
            }

            if (jsonParamObj.isObject()) {
                ((ObjectNode)jsonParamObj).set(key, paramJson);
            }
        }
    }

    // 处理参数绑定
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static JsonNode handlerBind(JsonNode bindElement, JsonNode jsonElement) throws IOException {
        Map < String, Object > param = params.get();
        if (BeanUtils.isEmpty(bindElement))
            return jsonElement;
        ObjectNode bind = (ObjectNode)bindElement;

        int type = bind.get("type").asInt();
        String value = bind.get("value").asText();

        JsonNode jsonVal = null;
        switch (type) {
            // 固定值
            case 1:
                jsonVal = new TextNode(value);
                break;
                // 流程常量 或者 参数列表
            case 2:
                if (jsonType.get().equals(FLOW_OUTPUT)) {
                    flowOutPutKey.set(value); // output将key设置进去
                } else {
                    jsonVal = new TextNode(param.get(value).toString());
                }
                break;
                // 脚本
            case 3:
                Map map = params.get();
                Object obj = AppUtil.getBean(GroovyScriptEngine.class).executeObject(value, map);
                jsonVal = JsonUtil.toJsonNode(obj);
                break;
                // 表单变量，在map中放了bo主表的
            case 4:
//                String[] BODataKey = value.split("\\.");
//                if (BODataKey.length != 2)
//                    throw new WebServiceException("BO[" + value + "]数据 格式不合法");
//                
//                BoData databoject = (BoData) param.get(BODataKey[0]);
//                if (databoject == null)
//                    throw new WebServiceException("BO ”code:" + BODataKey[0] + "“ 丢失");
//               String result =databoject.getString(BODataKey[1]);
//               jsonVal = new TextNode(result);
            	throw new InvokeException("无法获取BoData格式的数据");
        }
        if (BeanUtils.isEmpty(jsonVal))
            return jsonElement;
        if (jsonElement.isArray()) {
            if (!jsonVal.isArray()) {
                ArrayNode jarray = JsonUtil.getMapper().createArrayNode();
                jarray.add(jsonVal);
                jsonVal = jarray;
            }
        }
        return jsonVal;
    }

    // 处理泛型
    private static void handlerGenerics(ArrayNode childrenAry, JsonNode jsonElement) throws IOException {
        JsonNode jelement;
        if (jsonElement.isArray()) {
            jelement = childrenAry.get(0);
        } else {
            jelement = childrenAry.get(1);
        }
        if (!jelement.isObject())
            return;
        ObjectNode jobject = (ObjectNode)jelement;

        String type = jobject.get("type").asText();
        Boolean isGenerics = false;
        JsonNode genericsValue = jobject.get("generics");
        if (BeanUtils.isNotEmpty(genericsValue)) {
            isGenerics = genericsValue.asBoolean();
        }

        JsonNode generics;

        if ("Bean".equals(type)) {
            generics = JsonUtil.getMapper().createObjectNode();
            ArrayNode children = (ArrayNode)jobject.get("children");
            // 继续构建jsonParam
            buildJsonParams(children, generics);
        }
        // 泛型
        else if (isGenerics) {
            generics = JsonUtil.getMapper().createObjectNode();
            ArrayNode children = (ArrayNode)jobject.get("children");
            // 继续构建jsonParam
            handlerGenerics(children, generics);
        } else {
            generics = new TextNode("");
        }

        if (jsonElement.isArray()) {
            ArrayNode ja = (ArrayNode)jsonElement;
            if (ja.size() == 0) {
                ja.add(generics);
            }
        } else {
            String key = "";
            JsonNode jsonKey = childrenAry.get(0);
            if (jsonKey.isArray() || jsonKey.isObject()) {
                key = jsonKey.toString();
            } 
            else if (jsonKey.isTextual()) {
                key = jsonKey.asText();
            }
            ((ObjectNode)jsonElement).set(key, generics);
        }
    }
}
