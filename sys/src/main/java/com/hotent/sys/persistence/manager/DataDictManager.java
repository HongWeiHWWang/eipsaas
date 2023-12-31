package com.hotent.sys.persistence.manager;



import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.sys.persistence.model.DataDict;

/**
 * 字典接口管理层
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月21日
 */
public interface DataDictManager extends  BaseManager<DataDict>{
	
	/**
	 * 通过数据字典类别查询所有的数据字典项
	 * @param typeId
	 */
	List<DataDict> getByTypeId(String typeId);

	/**
	 * 通过 类型，和字典key 获取字典项
	 * @param typeId 字典类型id
	 * @param key 	   字典key
	 * @return
	 */
	DataDict getByDictKey(String typeId, String key);
	
	/**
	 * 通过父节点获取子节点（包含二级子节点）
	 * @param parentId 父节点
	 * @return
	 */
	List<DataDict> getChildrenByParentId(String parentId);
	
	/**
	 * 通过类型id删除字典
	 * @param id 类型id
	 */
	void delByDictTypeId(String id);
	

	/**
	 * 更新排序  sn
	 * @param dicId 字典id
	 * @param sn	排序
	 */
	void updSn(String dicId, int sn);

	/**
	 * 通过父节点ID获取一级子节点
	 * @param parentId 父节点
	 * @return
	 */
	List<DataDict> getFirstChilsByParentId(String parentId);

	CommonResult<String> removeByTypeIds(String typeIds);
	
	void importData(List<MultipartFile> files, String typeId) throws Exception;	
}
