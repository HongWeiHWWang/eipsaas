package com.hotent.sys.persistence.model;
import java.util.List;



/**
 * 对象功能:总分类表。用于显示平级或树层次结构的分类，可以允许任何层次结构。 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-05-08 14:12:26
 */
public interface ISysType {
	
	
	 void setId(String id) ;
	/**
	 * 返回 分类ID
	 * @return
	 */
	 String getId() ;
	
	 void setTypeGroupKey(String typeGroupKey) ;
	/**
	 * 返回 所属分类组业务主键
	 * @return
	 */
	 String getTypeGroupKey() ;
	 void setName(String name) ;
	
	/**
	 * 返回 分类名称
	 * @return
	 */
	 String getName() ;

	 void setTypeKey(String typeKey) ;
	
	/**
	 * 返回 节点的分类Key
	 * @return
	 */
	 String getTypeKey() ;
	
	 void setStruType(Short struType) ;
	
	/**
	 * 返回 0=平铺结构；1=树型结构
	 * @return
	 */
	 Short getStruType() ;
	
	 void setParentId(String parentId) ;
	
	/**
	 * 返回 父节点
	 * @return
	 */
	 String getParentId() ;
	
	 void setDepth(Integer depth) ;
	
	/**
	 * 返回 层次
	 * @return
	 */
	 Integer getDepth() ;
	
	 void setPath(String path) ;
	/**
	 * 返回 路径
	 * @return
	 */
	 String getPath() ;
	
	 void setIsLeaf(char isLeaf) ;
	
	/**
	 * 返回 是否叶子节点。Y=是；N=否
	 * @return
	 */
	 char getIsLeaf() ;
	
	 void setOwnerId(String ownerId) ;
	
	/**
	 * 返回 所属人ID
	 * @return
	 */
	 String getOwnerId() ;
	
	 void setSn(Integer sn) ;
	
	/**
	 * 返回 序号
	 * @return
	 */
	 Integer getSn() ;
		
	
	 List getChildren();
	 void setChildren(List children);
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	 String toString() ;
	
	 String getText();
	
}