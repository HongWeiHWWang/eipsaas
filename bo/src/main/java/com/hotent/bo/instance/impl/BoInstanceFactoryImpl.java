package com.hotent.bo.instance.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hotent.bo.instance.BoDataHandler;
import com.hotent.bo.instance.BoInstanceFactory;


/**
 * 获取BoDataHandler 工厂类实现。
 * @author ray
 */
@Service("boInstanceFactory")
public class BoInstanceFactoryImpl implements BoInstanceFactory {
	@Autowired
	ApplicationContext context;
	private Map<String, BoDataHandler> handlerMap=new HashMap<String, BoDataHandler>();


	/**
	 * handlerList 属性。
	 * @param list
	 */
	public void setHandlerList(List<BoDataHandler> list){
		for(BoDataHandler handler:list){
			handlerMap.put(handler.saveType(), handler);
		}
	}

	/**
	 * 获取保存类型。
	 */
	@Override
	public BoDataHandler getBySaveType(String saveType) {
		if(handlerMap.size()==0){
			handlerMap=context.getBeansOfType(BoDataHandler.class);
		}
		for (String in : handlerMap.keySet()) {
			BoDataHandler handler = handlerMap.get(in);//得到每个key多对用value的值
			if(handler.saveType().equals(saveType)){
				return handler;
			}
		}
		return null;
	}

}
