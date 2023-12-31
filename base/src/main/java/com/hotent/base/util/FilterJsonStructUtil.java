package com.hotent.base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.FilterJsonStruct;

/**
 * 
 * <pre> 
 * 描述：转为自定义SQL查询的过滤条件中的条件脚本的解释
 * 构建组：x5-sys-core
 * 作者：aschs
 * 邮箱:liyj@jee-soft.cn
 * 日期:2016年7月6日-上午11:26:14
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class FilterJsonStructUtil {
	public static String CONDITION_AND = " and ";

	public static String getSql(String filterJson, String dbType) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtil.isEmpty(filterJson))
			return "";
		List<Map<String, Object>> operatorList = new ArrayList<Map<String, Object>>();
		List<FilterJsonStruct> filters = new ArrayList<FilterJsonStruct>();
		ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(filterJson);
		for (Object json : array) {
			FilterJsonStruct filterJsonStruct = JsonUtil.toBean(JsonUtil.toJson(json), FilterJsonStruct.class);
			filters.add(filterJsonStruct);
		}
		// 转换成SQL
		getFilterResult(filters, operatorList, dbType);

		return executeOperator(operatorList);
	}

	/**
	 * 过滤所有的条件
	 * 
	 * @param filters
	 * @param tableMap
	 * @param relationMap
	 * @param operatorList
	 */
	private static void getFilterResult(List<FilterJsonStruct> filters, List<Map<String, Object>> operatorList, String dbType) {
		for (FilterJsonStruct filter : filters) {
			// 组合条件
			if (filter.getBranch()) {
				List<Map<String, Object>> branchResultList = new ArrayList<Map<String, Object>>();
				getFilterResult(filter.getSub(), branchResultList, dbType);
				String branchResult = executeOperator(branchResultList);
				Map<String, Object> resultMap = getResultMap(filter.getCompType(), branchResult);
				operatorList.add(resultMap);
			}
			// 普通条件
			else {
				getNormalFilterResult(filter, operatorList, dbType);
			}
		}
	}

	/**
	 * 获取SQL运算结果
	 * 
	 * @param operatorList
	 * @return
	 */
	private static String executeOperator(List<Map<String, Object>> operatorList) {
		if (operatorList.size() == 0)
			return "";
		String returnVal = (String) operatorList.get(0).get("result");
		if (operatorList.size() == 1)
			return returnVal;
		int size = operatorList.size();
		for (int k = 1; k < size; k++) {
			Map<String, Object> resultMap = operatorList.get(k);
			String operator = resultMap.get("operator").toString();
			if ("or".equals(operator)) { // 或运算
				returnVal = "(" + returnVal + ") OR (" + resultMap.get("result") + ")";
			} else if ("and".equals(operator)) { // 与运算
				returnVal = "(" + returnVal + ") AND (" + resultMap.get("result") + ")";
			}
		}
		if (StringUtils.isNotEmpty(returnVal))
			returnVal = "(" + returnVal + ")";
		return returnVal;
	}

	private static Map<String, Object> getResultMap(String operator, String result) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("operator", operator);
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 获取普通条件的结果
	 * 
	 * @param filter
	 * @param tableMap
	 * @param relationMap
	 * @param operatorList
	 */
	private static void getNormalFilterResult(FilterJsonStruct filter, List<Map<String, Object>> operatorList, String dbType) {
		String flowvarKey = filter.getFlowvarKey();
		String judgeVal1 = getJudgeVal(filter, true);
		String judgeVal2 = getJudgeVal(filter, false);

		int optType = filter.getOptType();

		String script = "";
		switch (optType) {
		// 数字
		case 1:
			// 条件一
			if (StringUtils.isNotEmpty(judgeVal1)) {
				script = getCompareScript(filter.getJudgeCon1(), flowvarKey, judgeVal1, filter.getOptType());
			}
			// 条件二
			if (StringUtils.isNotEmpty(judgeVal2)) {
				String moreScript = getCompareScript(filter.getJudgeCon2(), flowvarKey, judgeVal2, filter.getOptType());
				if (StringUtils.isNotEmpty(script))
					script = script + CONDITION_AND;
				script = script + moreScript;
			}
			break;
		// 字符串
		case 2:
			// 条件一
			if (StringUtils.isNotEmpty(judgeVal1)) {
				script = getCompareScript(filter.getJudgeCon1(), flowvarKey, judgeVal1, filter.getOptType());
			}
			break;
		// 日期
		case 3:
			// 条件一
			if (StringUtils.isNotEmpty(judgeVal1)) {
				String val1 = SqlDateFormatUtil.convertDateFormat(filter.getDatefmt(), judgeVal1, dbType);
				script = getCompareScript(filter.getJudgeCon1(), flowvarKey, val1, filter.getOptType());
			}
			// 条件二
			if (StringUtils.isNotEmpty(judgeVal2)) {
				String val2 = SqlDateFormatUtil.convertDateFormat(filter.getDatefmt(), judgeVal1, dbType);
				String moreScript = getCompareScript(filter.getJudgeCon2(), flowvarKey, val2, filter.getOptType());
				if (StringUtils.isNotEmpty(script))
					script = script + CONDITION_AND;
				script = script + moreScript;
			}
			break;
		// 字典
		case 4:
			String[] vals = judgeVal1.split("&&");
			for (String val : vals) {
				if (StringUtils.isNotEmpty(script)) {
					script += CONDITION_AND;
				}
				script += getCompareScript(filter.getJudgeCon1(), flowvarKey, val, filter.getOptType());
			}
			break;
		// 角色、组织、岗位选择器
		case 5:
			String judgeCon = filter.getJudgeCon1();
			String[] ids = judgeVal1.split("&&");
			if (ids.length == 2) {
				script = getCompareScript(judgeCon, filter.getFlowvarKey(), ids[0], filter.getOptType());
			} else {// 特殊类型的
				if ("3".equalsIgnoreCase(judgeCon) || "4".equalsIgnoreCase(judgeCon)) {
					script = getCompareScript(judgeCon, filter.getFlowvarKey(), judgeVal1, filter.getOptType());
				}
			}
			break;
		}

		if (StringUtil.isEmpty(script))
			return;
		// 执行结果记录到operatorList中
		Map<String, Object> resultMap = getResultMap(filter.getCompType(), script);
		operatorList.add(resultMap);
	}

	/**
	 * 获取判断值。
	 * 
	 * @param filter
	 * @param isJudgeVal1
	 * @return
	 */
	private static String getJudgeVal(FilterJsonStruct filter, boolean isJudgeVal1) {
		String val = isJudgeVal1 ? filter.getJudgeVal1() : filter.getJudgeVal2();
		int ruleType = filter.getRuleType();
		if (ruleType == FilterJsonStruct.RULE_TYPE_NORMAL)
			return val;
		GroovyScriptEngine groovyScriptEngine = AppUtil.getBean(GroovyScriptEngine.class);
		// 脚本计算。
		Object object = groovyScriptEngine.executeString(val, null);
		return object.toString();
	}

	/**
	 * 获取根据条件组合的脚本
	 * 
	 * @param judgeCon
	 * @param fieldName
	 * @param judgeVal
	 * @param type
	 * @return
	 */
	private static String getCompareScript(String judgeCon, String fieldName, String judgeVal, int type) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> varMap =new HashMap<>();
		varMap.put("curUserId", AuthenticationUtil.getCurrentUserId());
		varMap.put("curUserAccount", AuthenticationUtil.getCurrentUsername());
		switch (type) {
		case 1:// 数值
		case 3:// 日期
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append("=").append(judgeVal);
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append("!=").append(judgeVal);
			} else if ("3".equals(judgeCon)) {
				sb.append(fieldName).append(">").append(judgeVal);
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(">=").append(judgeVal);
			} else if ("5".equals(judgeCon)) {
				sb.append(fieldName).append("<").append(judgeVal);
			} else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append("<=").append(judgeVal);
			} else if ("7".equals(judgeCon)) {
				sb.append(fieldName).append(" = :").append("").append(judgeVal).append("");
			} else if ("8".equals(judgeCon)) {
				sb.append(fieldName).append(" != :").append("").append(judgeVal).append("");
			}
			break;
		case 2:// 字符串
		case 4:// 字典
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append("=").append("'").append(judgeVal).append("'");
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append("!=").append("'").append(judgeVal).append("'");
			} else if ("3".equals(judgeCon)) {
				sb.append("UPPER(").append(fieldName).append(")=").append(" UPPER('").append(judgeVal).append("')");
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%").append(judgeVal).append("%'");
			} else if ("5".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '").append(judgeVal).append("%'");
			} else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%").append(judgeVal).append("'");
			} else if ("7".equals(judgeCon)) {
				sb.append(fieldName).append(" = '").append(varMap.get(judgeVal)).append("'");
			} else if ("8".equals(judgeCon)) {
				sb.append(fieldName).append(" != '").append(varMap.get(judgeVal)).append("'");
			}
			break;
		case 5:// 人员选择器
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append(" in (").append("").append(judgeVal).append(")");
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append(" not in (").append("").append(judgeVal).append(")");
			} else if ("3".equals(judgeCon)) {
				sb.append(fieldName).append(" = :").append("").append(judgeVal).append("");
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" != :").append("").append(judgeVal).append("");
			}
			break;
		}
		return sb.toString();
	}
}
