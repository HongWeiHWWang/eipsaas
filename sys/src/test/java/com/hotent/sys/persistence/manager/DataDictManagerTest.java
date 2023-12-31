package com.hotent.sys.persistence.manager;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.DataDict;

import static org.junit.Assert.assertTrue;

public class DataDictManagerTest extends SysTestCase{
	
	@Resource
	DataDictManager dataDictManager;
	
	@Test
	public void testCur(){
		DataDict dataDict = new DataDict();
		dataDict.setId(idGenerator.getSuid());
		dataDict.setTypeId(idGenerator.getSuid());
		dataDict.setKey("test");
		dataDict.setName("测试");
		dataDict.setParentId("-1");
		dataDict.setSn(121);
		
		dataDictManager.create(dataDict);
		
		assertTrue(BeanUtils.isNotEmpty(dataDictManager.getByTypeId(dataDict.getTypeId())));
		
		assertTrue(BeanUtils.isNotEmpty(dataDictManager.getByDictKey(dataDict.getTypeId(),dataDict.getKey())));
		
		assertTrue(BeanUtils.isNotEmpty(dataDictManager.getChildrenByParentId(dataDict.getParentId())));
		
		dataDictManager.updSn(dataDict.getId(), 121);
		
		assertTrue(BeanUtils.isNotEmpty(dataDictManager.getFirstChilsByParentId(dataDict.getParentId())));
		
		dataDictManager.delByDictTypeId(dataDict.getTypeId());
	}

}