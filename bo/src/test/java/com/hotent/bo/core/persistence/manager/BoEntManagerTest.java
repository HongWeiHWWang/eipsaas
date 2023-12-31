package com.hotent.bo.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
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
import com.hotent.bo.core.BoTestCase;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.table.operator.ITableOperator;

/**
 * BoEntManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class BoEntManagerTest extends BoTestCase{
	@Resource
	BoEntManager boEntManager;
	@Resource
	ITableOperator tableOperator;
	
	private BoEnt build(String id, String name, String desc, String packageId){
		BoEnt boEnt = new BoEnt();
		boEnt.setId(id);
		boEnt.setName(name);
		boEnt.setDesc(desc);
		boEnt.setPackageId(packageId);
		return boEnt;
	}
	
	@Test
	public void testCurd() throws SQLException{
		String suid = UniqueIdUtil.getSuid();
		String name = "student";
		// 添加
		boEntManager.create(build(suid, name, "学生信息", "1001"));
		// 查询
		BoEnt boent = boEntManager.get(suid);
		assertEquals(name, boent.getName());
		assertFalse(boent.isExternal());
		assertFalse(boent.isCreatedTable());
		// 创建物理表
		boEntManager.createTable(boent);
		// 验证数据库中是否已经生成物理表
		boolean tableExist = tableOperator.isTableExist(boent.getTableName());
		assertTrue(tableExist);
		
		String newDesc = "新的学生信息";
		boent.setDesc(newDesc);
		// 更新
		boEntManager.update(boent);

		BoEnt bd = boEntManager.get(suid);
		assertEquals(newDesc, bd.getDesc());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		
		boEntManager.create(build(suid2, "school", "学校信息", "1001"));
		boEntManager.create(build(suid3, "class", "班级信息", "1001"));

		// 通过ID列表批量查询
		List<BoEnt> byIds = boEntManager.listByIds(Arrays.asList(new String[]{suid2,suid3}));
		assertEquals(2, byIds.size());

		QueryFilter<BoEnt> queryFilter = QueryFilter.<BoEnt>build()
											 .withDefaultPage()
											 .withQuery(new QueryField("name", name));
		// 通过通用查询条件查询
		PageList<BoEnt> query = boEntManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BoEnt bab = boEntManager.getOne(Wrappers.<BoEnt>lambdaQuery().eq(BoEnt::getName, name));
		assertEquals(name, bab.getName());
		
		// 通过别名查询
		BoEnt byName = boEntManager.getByName(name);
		assertEquals(name, byName.getName());

		// 查询所有数据
		List<BoEnt> all = boEntManager.list();
		assertEquals(3, all.size());
		
		// 查询所有数据(分页)
		PageList<BoEnt> allByPage = boEntManager.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());
		
		// 通过ID删除数据
		boEntManager.remove(suid);
		BoEnt sstd = boEntManager.getOne(Wrappers.<BoEnt>lambdaQuery().eq(BoEnt::getName, name));
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		boEntManager.removeByIds(Arrays.asList(new String[]{suid, suid2, suid3}));
		List<BoEnt> nowAll = boEntManager.list();
		assertEquals(0, nowAll.size());
	}
}
