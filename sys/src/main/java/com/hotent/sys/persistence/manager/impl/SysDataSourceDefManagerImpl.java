package com.hotent.sys.persistence.manager.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.QueryFilterHelper;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.sys.persistence.dao.SysDataSourceDefDao;
import com.hotent.sys.persistence.manager.SysDataSourceDefManager;
import com.hotent.sys.persistence.model.SysDataSourceDef;

@Service("sysDataSourceDefManager")
public class SysDataSourceDefManagerImpl extends  BaseManagerImpl<SysDataSourceDefDao, SysDataSourceDef>  implements SysDataSourceDefManager, QueryFilterHelper<SysDataSourceDef>  {
	/**
	 * 获取类名为classPath的所有有setting的字段
	 * 
	 * @param classPath
	 * @return List&lt;Field>
	 * @exception
	 * @since 1.0.0
	 */
	private List<Field> getHasSetterFields(String classPath) {
		List<Field> fields = new ArrayList<Field>();

		try {
			Class<?> _class = Class.forName(classPath);
			Field[] fs=_class.getDeclaredFields();
			for (Field field : fs) {
				if (checkHasSetter(_class, field)) {
					fields.add(field);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fields;
	}
	
	/**
	 * 
	 * 获取这个classPath的拥有setter的字段
	 * @param classPath
	 * @return 
	 * JSONArray
	 * @exception 
	 * @since  1.0.0
	 */
	@Override
	public List<Map<String,String>> getHasSetterFieldsJsonArray(String classPath) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for (Field field : getHasSetterFields(classPath)) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("name", field.getName());
			map.put("comment", field.getName());
			map.put("type", field.getType().getName());
			map.put("baseAttr", "0");
			list.add(map);
		}
		return list;
	}

	/**
	 * 检查资格字段在_class类中是否有setter
	 * 
	 * @param _class
	 * @param field
	 * @return boolean
	 * @exception
	 * @since 1.0.0
	 */
	private boolean checkHasSetter(Class<?> _class, Field field) {
		boolean b = false;
		
		for (Method method : _class.getMethods()) {
			if (!method.getName().startsWith("set")) continue;

			if (method.getName().replace("set", "").toUpperCase().equals(field.getName().toUpperCase())) {
				b = true;
			}
		}

		return b;
	}

}
