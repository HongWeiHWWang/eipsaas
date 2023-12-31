package com.hotent.bpm.engine.task.skip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.inst.ISkipCondition;

/**
 * 跳过任务策略工具类。
 * @author ray
 *
 */

@SuppressWarnings("unchecked")
public class SkipConditionUtil {
	
	/**
	 * 返回跳过策略列表。
	 * @return
	 */
	public static List<ISkipCondition> getSkipConditions(){
		List<ISkipCondition> list=(List<ISkipCondition>)AppUtil.getBean("skipRules");
		return list;
	}
	
	private static Map<String, ISkipCondition> conditionMap=new HashMap<String, ISkipCondition>();
	
	static {
		List<ISkipCondition> list=(List<ISkipCondition>)AppUtil.getBean("skipRules");
		for(ISkipCondition condition:list){
			conditionMap.put(condition.getType().toLowerCase(), condition);
		}
	}
	
	/**
	 * 根据跳转规则类型获取跳转条件。
	 * @param type
	 * @return
	 */
	public static ISkipCondition getSkipConditionByType(String type){
		return conditionMap.get(type.toLowerCase());
	}

	/**
	 * 根据跳过类型
	 * @param type
	 * @return
	 */
	public static String getTitleByType(String type){
		ISkipCondition condition= conditionMap.get(type.toLowerCase());
		return condition.getTitle();
	}
}
