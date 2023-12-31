package com.hotent.portal.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.MsgTemplate;

/**
* 对象功能:消息模版 dao
* @author zhaoxy
* @company 广州宏天软件股份有限公司
* @email zhxy@jee-soft.cn
* @date 2018-06-06 14:20
*/
public interface MsgTemplateDao extends BaseMapper<MsgTemplate> {
	/**
	 * 根据类型获取默认的模版
	 * @param typeKey
	 * @return  MsgTemplate
	 */
	public MsgTemplate getDefault(String typeKey);

	/**
	 * 根据键获取模版。
	 * @param key
	 * @return MsgTemplate
	 */
	public MsgTemplate getByKey(String key);
	/**
	 * 将某类型的模板均设置为非默认
	 * @param key
	 */
	void setNotDefaultByType(String key);
	/**
	 * 设置指定模板为默认模板
	 * @param id
	 */
	void setDefault(String id);
	/**
	 * 通过ids集合查询模板集合
	 * @param ids
	 * @return
	 */
	List<MsgTemplate> getByIds(List<String> ids);
	/**
	 * 删除指定类型的所有模板
	 * @param delTypeKey
	 */
	void delByTypeKey(List<String> delTypeKey);
	/**
	 * 判断指定key、类型的模板是否存在
	 * @param key
	 * @param typeKey
	 * @return
	 */
	boolean isExistByKeyAndTypeKey(@Param("key")String key, @Param("typeKey")String typeKey);

	/**
	 * 根据ID将模板设置为非默认
	 * @param id
	 */
	void setNotDefaultById(String id);
}
