package com.hotent.bo.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.core.BoTestCase;
import com.hotent.bo.model.BoEntRel;
import com.hotent.bo.persistence.manager.BoEntRelManager;

/**
 * BoEntRelManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class BoEntRelManagerTest extends BoTestCase{
	@Resource
	BoEntRelManager boEntRelManager;
	
	private BoEntRel build(String id, String boDefid, String parentId, String refEntId, String type){
		BoEntRel boEntRel = new BoEntRel();
		boEntRel.setId(id);
		boEntRel.setBoDefid(boDefid);
		boEntRel.setParentId(parentId);
		boEntRel.setRefEntId(refEntId);
		boEntRel.setType(type);
		return boEntRel;
	}
	
	@Test
	public void testCurd(){
		String suid = UniqueIdUtil.getSuid();
		String boDefId = "1002";
		String parentId = "1";
		String refEntId = "10000023";
		// 添加
		boEntRelManager.create(build(suid, boDefId, parentId, refEntId, BoConstants.RELATION_MAIN));
		// 查询
		BoEntRel boent = boEntRelManager.get(suid);
		assertEquals(boDefId, boent.getBoDefid());

		String newParentId = "1";
		boent.setParentId(newParentId);
		boent.setType(BoConstants.RELATION_MANY_TO_MANY);
		// 更新
		boEntRelManager.update(boent);

		BoEntRel bd = boEntRelManager.get(suid);
		assertEquals(newParentId, bd.getParentId());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		
		boEntRelManager.create(build(suid2, boDefId, suid, refEntId, BoConstants.RELATION_MANY_TO_MANY));
		boEntRelManager.create(build(suid3, boDefId, suid, refEntId, BoConstants.RELATION_ONE_TO_MANY));

		// 通过ID列表批量查询
		List<BoEntRel> byIds = boEntRelManager.listByIds(Arrays.asList(new String[]{suid2, suid3}));
		assertEquals(2, byIds.size());

		QueryFilter<BoEntRel> queryFilter = QueryFilter.<BoEntRel>build()
											 .withDefaultPage()
											 .withQuery(new QueryField("boDefid", boDefId));
		// 通过通用查询条件查询
		PageList<BoEntRel> query = boEntRelManager.query(queryFilter);
		assertEquals(3, query.getTotal());

		// 通过指定列查询唯一记录
		BoEntRel bab = boEntRelManager.getOne(Wrappers.<BoEntRel>lambdaQuery().eq(BoEntRel::getParentId, parentId));
		assertEquals(refEntId, bab.getRefEntId());
		
		// 通过bo定义ID查询
		List<BoEntRel> byDefId = boEntRelManager.getByDefId(boDefId);
		assertEquals(3, byDefId.size());
		
		// 通过实体ID查询
		List<BoEntRel> byEntId = boEntRelManager.getByEntId(refEntId);
		assertEquals(3, byEntId.size());

		// 查询所有数据
		List<BoEntRel> all = boEntRelManager.list();
		assertEquals(3, all.size());
		
		// 查询所有数据(分页)
		PageList<BoEntRel> allByPage = boEntRelManager.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());
		
		// 通过ID删除数据
		boEntRelManager.remove(suid);
		BoEntRel sstd = boEntRelManager.getOne(Wrappers.<BoEntRel>lambdaQuery().eq(BoEntRel::getParentId, newParentId));
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		boEntRelManager.removeByIds(Arrays.asList(new String[]{suid2}));
		List<BoEntRel> nowAll = boEntRelManager.list();
		assertEquals(1, nowAll.size());
		
		// 通过定义ID删除数据
		boEntRelManager.removeByDefId(boDefId);
		List<BoEntRel> nowAll2 = boEntRelManager.list();
		assertEquals(0, nowAll2.size());
	}
}
