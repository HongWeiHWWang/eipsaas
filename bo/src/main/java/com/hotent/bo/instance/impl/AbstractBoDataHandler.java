package com.hotent.bo.instance.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.instance.BoDataHandler;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.bo.persistence.manager.BoDefManager;

/**
 * bo数据处理器的抽象类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public abstract class AbstractBoDataHandler implements BoDataHandler {
	@Resource
	BoDefManager boDefManager;

	public void setBoDefAlias(List<BoResult> resultList,String bodefAlias){
		for(BoResult result:resultList){
			result.setBoAlias(bodefAlias);
			//result.setModifyDetail("");
		}
	}

	@Override
	public BoData getByBoDefAlias(String bodefAlias) {
		BoDef boDef= boDefManager.getByAlias(bodefAlias);
		if(BeanUtils.isEmpty(boDef)) {
			return null;
		}
		BoData boData=new BoData();
		boData.setBoDef(boDef);
		BoEnt boEnt = boDef.getBoEnt();
		Map<String,Object> row= getMapByBOEnt( boEnt);
		boData.setData(row);
		getCascadeByEnt(boEnt,boData);
		return boData;
	}

	/**
	 * 获取默认的数据
	 * @param boEnt	bo实体
	 * @return		默认数据
	 */
	public  Map<String,Object> getMapByBOEnt(BoEnt boEnt){
		Map<String,Object> map=new HashMap<String, Object>();

		List<BoAttribute> list= boEnt.getBoAttrList();
		for(BoAttribute attr:list){
			String val=attr.getDefaultValue();
			if(StringUtil.isEmpty(val)) {
				val="";
			}
			map.put(attr.getName(), val);
		}
		return map;
	}

	/**
	 * 递归获取初始数据
	 * @param boEnt		bo实体
	 * @param boData	bo数据
	 */
	@SuppressWarnings("unchecked")
	private void getCascadeByEnt(BoEnt boEnt,BoData boData){
		//子表处理
		List<BoEnt> childEntList= boEnt.getChildEntList();

		if(BeanUtils.isEmpty(childEntList)) return;
		/**
		 * 子表处理
		 */
		for(BoEnt childEnt : childEntList){
			String key=childEnt.getName();
			
			if(BoConstants.RELATION_MAIN.equals(boEnt.getType())){
				//初始化数据。
				boData.addInitDataMap(key, childEnt.getInitData());
				//添加子表空数据。
				boData.setSubList(key,new ArrayList<BoData>());
			}else{
				Map<String,Object> subInitMap = boData.getInitDataMap().get(boEnt.getName());
				Map<String,Map<String, Object>> subInitDataMap = new HashMap<String, Map<String,Object>>();
				if(BeanUtils.isNotEmpty(subInitMap) && subInitMap.containsKey("initData")){
					subInitDataMap = (Map<String,Map<String, Object>>)subInitMap.get("initData");
				}
				subInitDataMap.put(childEnt.getName(), childEnt.getInitData());
				Map<String,Object> subboEntMap = new HashMap<String, Object>();
				subboEntMap.put("initData", subInitDataMap);
				subboEntMap.put("sub_"+childEnt.getName(), new ArrayList<>());
				boData.getInitDataMap().get(boEnt.getName()).putAll(subboEntMap);
			}
			//递归
			getCascadeByEnt(childEnt,boData);
		}
	}
	@Override
	public BoData getByBoDefCode(String bodefCode) {
		BoDef boDef= boDefManager.getByAlias(bodefCode);

		BoData boData=new BoData();
		
		boData.setBoDef(boDef);
		boData.setBoDefAlias(boDef.getAlias());
		BoEnt boEnt= boDef.getBoEnt();
		
		Map<String,Object> row= getMapByBOEnt( boEnt);
		
		boData.setData(row);
		
		getCascadeByEnt(boEnt,boData);
		
		return boData;
	}
}
