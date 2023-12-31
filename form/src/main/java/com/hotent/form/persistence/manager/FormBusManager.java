package com.hotent.form.persistence.manager;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.bo.model.BoData;
import com.hotent.form.model.FormBusSet;



/**
 * 表单数据处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormBusManager extends BaseManager<FormBusSet>{
	/**
	 * 通过 boKey，id获取bo数据
	 * @param boKey
	 * @param id
	 * @return
	 */
	BoData getBoData(String boKey, String id) throws IOException;
	
	/**
	 *  通过formKey json 保存bo数据
	 * @param formKey
	 * @param json
	 * @throws IOException 
	 */
	void saveData(String formKey, String json) throws IOException;
	
	/**
	 * 通过formKey 删除业务数据
	 * @param aryIds
	 * @param formKey
	 */
	void removeByIds(String[] aryIds, String formKey);
	
	/**
	 * 通过 formKey 获取业务数据
	 * @param formKey
	 * @param param
	 * @return
	 * @throws IOException 
	 */
	JsonNode getList(String formKey, Map<String, Object> param) throws IOException;
}
