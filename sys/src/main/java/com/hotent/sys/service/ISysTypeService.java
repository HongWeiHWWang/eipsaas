package com.hotent.sys.service;

import java.util.List;

import com.hotent.base.query.QueryFilter;
import com.hotent.sys.persistence.model.ISysType;




public interface ISysTypeService {
	
	/**
	 * 根据ID获取分类对象数据。
	 * @param typeId
	 * @return
	 */
	ISysType getById(String typeId);
	
	List<ISysType> getByParentId(Long parentId);

	ISysType getInitSysType(int isRoot, String parentId);

	boolean isKeyExist(String id,String typeGroupKey, String typeKey);

	/**
	 * 通过分类组业务主键获取所有的公共分类和属于当前人的私有分类
	 * @param groupKey
	 * @param currUserId
	 * @return
	 */
	List<ISysType> getByGroupKey(String groupKey,String currUserId);

	/**
	 * 根据Id删除节点和其所有的子节点
	 * @param id
	 */
	void delByIds(String id);

	/**
	 * 根据节点Id和当前用户Id获取下一级的私有分类和公共分类
	 * @param parentId
	 * @param userId
	 * @return
	 */
	List<ISysType> getPrivByPartId(String parentId, String userId);

	/**
	 * 更新排序  sn
	 * @param typeId
	 * @param sn
	 */
	void updSn(String typeId, int sn);
	/**
	 * 通过分类组Key 获取 分类组的 所有分类  
	 * @param string
	 * @return
	 */
	List<ISysType> getRootTypeByCategoryKey(String string);
	/**
	 * 通过typeKey获取下级
	 * @param typeKey
	 * @return
	 */
	List<ISysType> getChildByTypeKey(String typeKey);
	/**
	 * 根据key获取对象
	 * @param typeKey
	 * @return
	 */
	ISysType getByKey(String typeKey);
	
	/**
	 * 根据键值获取xml格式数据。
	 * <pre>
	 * &lt;folder id='0' label='全部'>
	 * 	&lt;folder id='10000001994002' label='采购'>
	 * 		&lt;folder id='10000017790020' label='采购1'/>
	 * 		&lt;folder id='10000017790022' label='采购1'/>
	 * 	&lt;/folder>
	 * &lt;/folder>
	 * </pre>
	 * @param typeKey
	 * @return String
	 */
	String getXmlByKey(String typeKey,String currUserId);
	/**
	 */
	ISysType getByTypeKeyAndGroupKey(String groupKey, String typeKey);

	List<ISysType> query(QueryFilter queryFilter2);
}
