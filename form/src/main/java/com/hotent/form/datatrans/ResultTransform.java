package com.hotent.form.datatrans;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.hotent.base.util.BeanUtils;


/**
 * <pre>
 *  描述：数据转换。
 *  使用方法:
 *  
 *  TypeConvertConfig config=new TypeConvertConfig();
 *  
 *  config.regeisterConvert(Date.class, new ITypeConvert() {
 * 	@Override
 * 	public Object processValue(Object obj) {
 * 		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
 * 			return format.format(obj);
 * 	}
 * });
 *  List list=;
 *  ResultTransform.transform(list,config);
 *  
 *  2：
 *  ResultTransform.transform(pageList, new ITypeConvert() {
 *
 *	@Override
 *	public Object processValue(Object obj) {
 *			Map<String, Object> map = (Map<String, Object>) obj;
 *			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
 *			map.put("birthday", sdf.format(map.get("birthday")));
 *			map.put("新增列", "new");//插入新数据..这里可以对map随意操作，任何增删查改
 *			return map;
 *		}
 *	});
 *  
 *  构建组：x5-bpmx-platform
 *  作者：lyj
 *  邮箱:liyj@jee-soft.cn
 *  日期:2014-7-22-上午9:39:34
 *  版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ResultTransform {
	/**
	 * 从转化器注册器中匹配转换器
	 * @param list
	 * @param config 
	 * void
	 */
	
	public static void transform(List list, TypeConvertConfig config) {

		Map<Class, ITypeConvert> convertMap = config.getConverts();
		if (BeanUtils.isEmpty(list))
			return;

		Object obj = list.get(0);
		if (Map.class.isAssignableFrom(obj.getClass())) {
			handlerMap(list, convertMap);
		} else {
			handRowObject(list, convertMap);
		}
	}
	
	/**
	 * 明确告诉使用哪个转换器
	 * @param list	：数据
	 * @param convert 	：转换器
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void transform(List list, ITypeConvert convert) {
		if (convert == null)
			return;

		List rtnList = new ArrayList();
		for (Object tmp : list) {
			Object rtnObj = convert.processValue(tmp);
			rtnList.add(rtnObj);
		}
		list.clear();
		list.addAll(rtnList);
	}

	private static void handRowObject(List list, Map<Class, ITypeConvert> convertMap) {
		Object obj = list.get(0);
		ITypeConvert convert = getConvert(convertMap, obj);
		if (convert == null)
			return;

		List rtnList = new ArrayList();
		for (Object tmp : list) {
			Object rtnObj = convert.processValue(tmp);
			rtnList.add(rtnObj);
		}
		list.clear();
		list.addAll(rtnList);
	}

	private static void handlerMap(List list, Map<Class, ITypeConvert> map) {
		Map<String, Object> rowMap = (Map<String, Object>) list.get(0);
		Map<String, ITypeConvert> convertMap = getByRow(rowMap, map);

		List rtnList = new ArrayList();

		for (Object obj : list) {
			Map<String, Object> rtnRowMap = new HashMap<String, Object>();

			Map<String, Object> dataRow = (Map<String, Object>) obj;
			Set<Entry<String, Object>> set = dataRow.entrySet();
			for (Iterator<Entry<String, Object>> it = set.iterator(); it.hasNext();) {
				Entry<String, Object> ent = it.next();
				String key = ent.getKey();
				Object tmp = ent.getValue();
				ITypeConvert convert = convertMap.get(key);
				if (convert == null) {
					rtnRowMap.put(key, tmp);
				} else {
					Object rtnObj = convert.processValue(tmp);
					rtnRowMap.put(key, rtnObj);
				}
			}
			rtnList.add(rtnRowMap);
		}
		list.clear();
		list.addAll(rtnList);

	}

	private static Map<String, ITypeConvert> getByRow(Map<String, Object> rowMap, Map<Class, ITypeConvert> convertMap) {
		Map<String, ITypeConvert> fieldConverMap = new HashMap<String, ITypeConvert>();

		Set<Entry<String, Object>> set = rowMap.entrySet();
		for (Iterator<Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Entry<String, Object> ent = it.next();
			String key = ent.getKey();
			Object obj = ent.getValue();
			ITypeConvert convert = getConvert(convertMap, obj);
			if (convert == null)
				continue;
			fieldConverMap.put(key, convert);
		}
		return fieldConverMap;
	}

	private static ITypeConvert getConvert(Map<Class, ITypeConvert> map, Object obj) {
		Class cls = obj.getClass();
		ITypeConvert convert = map.get(cls);
		if (convert != null)
			return convert;

		Set<Entry<Class, ITypeConvert>> set = map.entrySet();
		for (Iterator<Entry<Class, ITypeConvert>> it = set.iterator(); it.hasNext();) {
			Entry<Class, ITypeConvert> ent = it.next();
			if (ent.getKey().isAssignableFrom(cls)) {
				return ent.getValue();
			}
		}
		return null;
	}
}
