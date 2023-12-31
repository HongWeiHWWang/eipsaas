package com.hotent.sys.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.sys.persistence.model.ISysType;


@Primary
@Service
public class SysTypeService  implements ISysTypeService{
	/*@Resource
	SysTypeDao sysTypeDao;*/
	/*@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	DataDictManager dataDictManager;*/
	/*
	@Override
	protected MyBatisDao<String, ISysType> getDao() {
		return sysTypeDao;
	}*/
	
	/**
	 * 根据parentId获取
	 */
	@Override
	public List<ISysType> getByParentId(Long parentId) {
		return null;
		//return sysTypeDao.getByParentId(parentId);
	}

	
	/**
	 * 取得初始分类类型。
	 * @param isRoot	是否根节点。
	 * @param parentId	父节点。
	 * @return
	 * @throws Exception 
	 */
	@Override
	public ISysType getInitSysType(int isRoot, String parentId) {
		/*ISysType ISysType=new ISysType();
		String typeId=UniqueIdUtil.getSuid();
		//如果是根节点，则从SysCategory获取数据构建分类类型
		if (isRoot==1) {
			SysCategory sysCategory =sysCategoryDao.get(parentId);
			ISysType.setTypeKey(sysCategory.getGroupKey());
			ISysType.setTypeGroupKey(sysCategory.getGroupKey());
			ISysType.setParentId(parentId);
			ISysType.setStruType(sysCategory.getType());
			ISysType.setPath(parentId+"."+typeId+".");
			ISysType.setName(sysCategory.getName());
		}else {
			//获取父类构建分类类型。
			ISysType=sysTypeDao.get(parentId);
			String path=ISysType.getPath();
			ISysType.setPath(path +typeId +".");
		}
		ISysType.setId(typeId);*/
		return null;
	}

	@Override
	public boolean isKeyExist(String id, String typeGroupKey, String typeKey) {
		return false;
		//return sysTypeDao.isKeyExist(id,typeGroupKey,typeKey);
	}

	/**
	 * 通过分类组业务主键获取所有的公共分类和属于当前人的私有分类
	 * @param groupKey
	 * @param currUserId
	 * @return
	 */
	@Override
	public List<ISysType> getByGroupKey(String groupKey,String currUserId) {
		return null;
		
		//return sysTypeDao.getByGroupKey( groupKey, currUserId);
	}

	/**
	 * 根据Id删除节点和其所有的子节点
	 * 如果是数据字典，删除字典项
	 * @param id
	 */
	@Override
	public void delByIds(String id) {
		/*if(BeanUtils.isEmpty(id)) return;
		//如果是数据字典则、删除数据字典项
		ISysType ISysType = sysTypeDao.get(id);
		boolean isDict = ISysType.getTypeGroupKey().equals(CategoryConstants.CAT_DIC.key());
		//根据其path获取其子节点
		List<ISysType> sysTypes=sysTypeDao.getByPath(ISysType.getPath());
		sysTypeDao.remove(id);
		if(isDict) dataDictManager.delByDictTypeId(id);
		for(ISysType sysTypeTempSysType : sysTypes){
			String Id=sysTypeTempSysType.getId();
			if(isDict) dataDictManager.delByDictTypeId(Id);
			sysTypeDao.remove(Id);
			//删除数据字典。
		}*/
		
	}

	@Override
	public List<ISysType> getPrivByPartId(String parentId, String userId) {
		return null;
		
		//return sysTypeDao.getPrivByPartId( parentId, userId);
	}

	/**
	 * 更新排序  sn
	 * @param typeId
	 * @param sn
	 */
	@Override
	public void updSn(String typeId, int sn) {
	  //sysTypeDao.updSn(typeId, sn);
	}

	@Override
	public List<ISysType> getRootTypeByCategoryKey(String groupKey) {
		/*SysCategory sysCategory =  sysCategoryDao.getByKey(groupKey);
		if(sysCategory == null) return Collections.emptyList();
		return sysTypeDao.getTypesByParentId(groupKey,sysCategory.getId());*/
		return null;
	}

	@Override
	public List<ISysType> getChildByTypeKey(String typeKey) {
		return null;
		//ISysType ISysType=sysTypeDao.getByTypeKey(typeKey);
		//if(ISysType == null) return Collections.emptyList();
		
		//return sysTypeDao.getByPath(ISysType.getPath());
	}

	@Override
	public ISysType getByKey(String typeKey) {
		return null;
		//return sysTypeDao.getByTypeKey(typeKey);
		 
	}

	@Override
	public String getXmlByKey(String groupKey,String currUserId) {
		/*SysCategory category= sysCategoryDao.getByKey(groupKey);
		List<ISysType> sysTypes= getByGroupKey( groupKey, currUserId);
		StringBuffer sb = new StringBuffer("<folder id='0' label='全部'>");
		
		contructXml(sysTypes,category.getId(),sb);
		
		sb.append("</folder>");
		return sb.toString();*/
		return null;
	}
	
	private void contructXml(List<ISysType> sysTypes,String parentId,StringBuffer sb){
		if(BeanUtils.isEmpty(sysTypes)) return;
		for(ISysType type:sysTypes){
			if(!parentId.equals( type.getParentId())) continue; 
			sb.append("<folder id='"+type.getId()  +"' label='"+ type.getName() +"'>");
			contructXml(sysTypes, type.getId(), sb);
			sb.append("</folder>");
		}
	}

	@Override
	public ISysType getByTypeKeyAndGroupKey(String groupKey, String typeKey) {
		return null;
		// TODO Auto-generated method stub
		//return sysTypeDao.getByTypeKeyAndGroupKey(groupKey,typeKey);
	}


	@Override
	public ISysType getById(String typeId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ISysType> query(QueryFilter queryFilter2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
