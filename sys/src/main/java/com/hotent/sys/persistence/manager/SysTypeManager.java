package com.hotent.sys.persistence.manager;



import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysType;


/**
 * 系统类型处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月21日
 */
public interface SysTypeManager extends  BaseManager<SysType>{
	
	/**
	 * 通过父节点获取系统类型
	 * 
	 * @param parentId
	 * @return
	 */
	List<SysType> getByParentId(String parentId);
	
	/**
	 * 取得初始分类类型。
	 * @param isRoot 是否根节点
	 * @param parentId 父节点
	 * @return
	 */
	SysType getInitSysType(int isRoot, String parentId);
	
	/**
	 * 是否存在键值
	 * 
	 * @param id 主键
	 * @param typeGroupKey 组织类型key
	 * @param typeKey      key类型
	 * @return
	 */
	boolean isKeyExist(String id,String typeGroupKey, String typeKey);

	/**
	 * 通过分类组业务主键获取所有的分类
	 * @param groupKey 分类组业务主键
	 * @return
	 */
	List<SysType> getByGroupKey(String groupKey);

	/**
	 * 根据Id删除节点和其所有的子节点
	 * @param id 主键
	 */
	void delByIds(String id);

	/**
	 * 根据节点Id和当前用户Id获取下一级的私有分类和公共分类
	 * @param parentId 节点id
	 * @param userId   用户id
	 * @return
	 */
	List<SysType> getPrivByPartId(String parentId, String userId);

	/**
	 * 更新排序  sn
	 * @param typeId 类型
	 * @param sn	   排序参数
	 */
	void updSn(String typeId, int sn);
	
	/**
	 * 通过分类组Key 获取 分类组的 所有分类  
	 * @param string 目录key
	 * @return
	 */
//	List<SysType> getRootTypeByCategoryKey(String string);
	
	/**
	 * 通过typeKey获取下级
	 * @param typeKey  类型key
	 * @return
	 */
	List<SysType> getChildByTypeKey(String typeKey);
	
	/**
	 * 根据key获取对象
	 * @param typeKey 类型key
	 * @return
	 */
	SysType getByKey(String typeKey);
	
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
	String getXmlByKey(String typeKey);
	
	/**
	 * 通过类型key和分组key获取系统类型
	 * @param groupKey
	 * @param typeKey
	 * @return
	 */
	SysType getByTypeKeyAndGroupKey(String groupKey, String typeKey);

	/**
	 * 通过typeId获取下级
	 * @param typeId  类型Id
	 * @return
	 */
	List<SysType> getChildByTypeId(String typeId);
}
