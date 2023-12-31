package com.hotent.sys.persistence.manager.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import com.hotent.activemq.model.JmsSysTypeChangeMessage;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.dao.SysTypeDao;
import com.hotent.sys.persistence.manager.DataDictManager;
import com.hotent.sys.persistence.manager.SysCategoryManager;
import com.hotent.sys.persistence.manager.SysTypeManager;
import com.hotent.sys.persistence.model.SysCategory;
import com.hotent.sys.persistence.model.SysType;
import org.springframework.transaction.annotation.Transactional;

@Service("sysTypeManager")
public class SysTypeManagerImpl extends BaseManagerImpl<SysTypeDao, SysType> implements SysTypeManager{
	@Resource
	DataDictManager dataDictManager;
	@Resource
	SysCategoryManager sysCategoryManager;
	@Resource
	JmsProducer jmsProducer;
	
	/**
	 * 根据parentId获取
	 */
	@Override
	public List<SysType> getByParentId(String parentId) {
		return baseMapper.getByParentId(parentId);
	}

	
	/**
	 * 取得初始分类类型。
	 * @param isRoot	是否根节点。
	 * @param parentId	父节点。
	 * @return
	 * @throws Exception 
	 */
	@Override
	public SysType getInitSysType(int isRoot, String parentId) {
		SysType sysType=new SysType();
		String typeId=UniqueIdUtil.getSuid();
		//如果是根节点，则从SysCategory获取数据构建分类类型
		if (isRoot==1) {
			SysCategory sysCategory =sysCategoryManager.get(parentId);
			sysType.setTypeKey(sysCategory.getGroupKey());
			sysType.setTypeGroupKey(sysCategory.getGroupKey());
			sysType.setParentId(parentId);
			sysType.setStruType(sysCategory.getType());
			sysType.setPath(parentId+"."+typeId+".");
			sysType.setName(sysCategory.getName());
		}else {
			//获取父类构建分类类型。
			sysType=this.get(parentId);
			String path=sysType.getPath();
			sysType.setPath(path +typeId +".");
		}
		sysType.setId(typeId);
		return sysType;
	}

	@Override
	public boolean isKeyExist(String id, String typeGroupKey, String typeKey) {
		Map<String, Object> params = new HashMap<>();
		params.put("typeKey", typeKey);
		params.put("typeGroupKey", typeGroupKey);
		params.put("id", id);
		return (baseMapper.isKeyExist(params) > 0);
	}

	/**
	 * 通过分类组业务主键获取所有分类
	 * @param groupKey
	 * @return
	 */
	@Override
	public List<SysType> getByGroupKey(String groupKey) {
		return baseMapper.getByGroupKey(groupKey);
	}

	/**
	 * 根据Id删除节点和其所有的子节点
	 * 如果是数据字典，删除字典项
	 * @param id
	 */
	@Override
	public void delByIds(String id) {
		if(BeanUtils.isEmpty(id)) return;
		//如果是数据字典则、删除数据字典项
		SysType sysType = this.get(id);
//		boolean isDict = sysType.getTypeGroupKey().equals(CategoryConstants.CAT_DIC.key());
		//根据其path获取其子节点
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", sysType.getPath());
		List<SysType> sysTypes=baseMapper.getByPath(params);
		this.remove(id);
		jmsProducer.sendToTopic(new JmsSysTypeChangeMessage(sysType.getTypeGroupKey(),sysType.getId(),sysType.getName(),sysType.getName(),2));
//		if(isDict) dataDictManager.delByDictTypeId(id);
		for(SysType sType : sysTypes){
			String Id=sType.getId();
//			if(isDict) dataDictManager.delByDictTypeId(Id);
			this.remove(Id);
			jmsProducer.sendToTopic(new JmsSysTypeChangeMessage(sType.getTypeGroupKey(),sType.getId(),sType.getName(),sType.getName(),2));
		}
		
	}

	@Override
	public List<SysType> getPrivByPartId(String parentId, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("parentId", parentId);
		params.put("userId", userId);
		return baseMapper.getPrivByPartId(params);
	}

	/**
	 * 更新排序  sn
	 * @param typeId
	 * @param sn
	 */
	@Override
	public void updSn(String typeId, int sn) {
		Map<String, Object> params = new HashMap<>();
		params.put("typeId", typeId);
		params.put("sn", sn);
		baseMapper.updSn(params);
	}

//	@Override
//	public List<SysType> getRootTypeByCategoryKey(String groupKey) {
//		SysCategory sysCategory =  sysCategoryDao.getByKey(groupKey);
//		if(sysCategory == null) return Collections.emptyList();
//		return sysTypeDao.getTypesByParentId(groupKey,sysCategory.getId());
//	}

	@Override
	public List<SysType> getChildByTypeKey(String typeKey) {
		SysType sysType=baseMapper.getByTypeKey(typeKey);
		if(sysType == null) return Collections.emptyList();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", sysType.getPath());
		return baseMapper.getByPath(params);
	}

	@Override
	public SysType getByKey(String typeKey) {
		return baseMapper.getByTypeKey(typeKey);
		 
	}

	@Override
	public String getXmlByKey(String groupKey) {
		List<SysType> sysTypes= getByGroupKey(groupKey);
		StringBuffer sb = new StringBuffer("<folder id='0' label='全部'>");
		sb.append("</folder>");
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	private void contructXml(List<SysType> sysTypes,String parentId,StringBuffer sb){
		if(BeanUtils.isEmpty(sysTypes)) return;
		for(SysType type:sysTypes){
			if(!parentId.equals( type.getParentId())) continue; 
			sb.append("<folder id='"+type.getId()  +"' label='"+ type.getName() +"'>");
			contructXml(sysTypes, type.getId(), sb);
			sb.append("</folder>");
		}
	}

	@Override
	public SysType getByTypeKeyAndGroupKey(String groupKey, String typeKey) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<>();
		params.put("groupKey", groupKey);
		params.put("typeKey", typeKey);
		return baseMapper.getByTypeKeyAndGroupKey(params);
	}

	@Override
	@Transactional
	public List<SysType> getChildByTypeId(String typeId) {
		SysType sysType = baseMapper.selectById(typeId);
		if(sysType == null) return Collections.emptyList();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", sysType.getPath());
		return baseMapper.getByPath(params);
	}

	
}
