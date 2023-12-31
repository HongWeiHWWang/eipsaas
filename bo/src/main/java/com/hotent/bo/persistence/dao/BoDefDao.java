package com.hotent.bo.persistence.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bo.model.BoDef;

/**
 * 流程定义处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date Apr 28, 2018
 */
public interface BoDefDao extends BaseMapper<BoDef> {
	/**
	 * 根据别名获取定义
	 * @param alias	别名
	 * @return		返回bo定义
	 */
	BoDef getByAlias(String alias);
	
	/**
	 * 通过别名删除定义
	 * @param alias	别名
	 * @return		返回删除了几个定义
	 */
	int removeByAlias(String alias);

	List<BoDef> getByFormKey(String formKey);

    /**
     * 更加建模ID、关联锁版本查询建模信息
     * @param map
     * @return
     */
    public BoDef getBoDefByRev(Map<String,Object> map);

	/**
	 * 获取业务表单
	 *
	 * @param id
	 * @return
	 */
	List<Map<String,String>> getFormDifinitionData(String id);

	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	List<Map<String,String>> getEntData(String id);
}
