package com.hotent.bo.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.core.BoTestCase;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.persistence.manager.BoAttributeManager;
import com.hotent.table.model.Column;

public class BoAttributeManagerTest extends BoTestCase{
	@Resource
	BoAttributeManager boAttributeManager;

	@Before
	public void runBeforeTestMethod() {
		System.out.println("测试开始");
	}

	@After
	public void runAfterTestMethod() {
		System.out.println("测试完成");
	}

	@Test
	public void testCurd(){
		String suid = UniqueIdUtil.getSuid();
		String name = "name";
		BoAttribute boAttribute = new BoAttribute();
		boAttribute.setId(suid);
		boAttribute.setName("name");
		boAttribute.setDesc("名称");
		boAttribute.setFieldName("name_");
		boAttribute.setComment("名称");
		boAttribute.setDataType(Column.COLUMN_TYPE_VARCHAR);
		boAttribute.setColumnType(Column.COLUMN_TYPE_VARCHAR);
		// 添加
		boAttributeManager.create(boAttribute);
		// 查询
		BoAttribute boAttr = boAttributeManager.get(suid);
		assertEquals(name, boAttr.getName());

		String newDesc = "新的名称";
		boAttr.setDesc(newDesc);
		// 更新
		boAttributeManager.update(boAttr);

		BoAttribute boAt = boAttributeManager.get(suid);
		assertEquals(newDesc, boAt.getDesc());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		String entId = UniqueIdUtil.getSuid();
		BoAttribute boAttr1 = new BoAttribute("sex", "性别", Column.COLUMN_TYPE_INT);
		boAttr1.setId(suid2);
		boAttr1.setEntId(entId);
		BoAttribute boAttr2 = new BoAttribute("born", "出生日期", Column.COLUMN_TYPE_DATE);
		boAttr2.setFormat("yyyy-DD-mm");
		boAttr2.setId(suid3);
		boAttributeManager.create(boAttr1);
		boAttributeManager.create(boAttr2);

		// 通过ID列表批量查询
		List<BoAttribute> byIds = boAttributeManager.listByIds(Arrays.asList(new String[]{suid2,suid3}));
		assertEquals(2, byIds.size());

		QueryFilter<BoAttribute> queryFilter = QueryFilter.<BoAttribute>build()
				.withDefaultPage()
				.withQuery(new QueryField("name", "sex"));
		// 通过通用查询条件查询
		PageList<BoAttribute> query = boAttributeManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BoAttribute bab = boAttributeManager.getOne(Wrappers.<BoAttribute>lambdaQuery().eq(BoAttribute::getName, name));
		assertEquals(name, bab.getName());

		// 查询所有数据
		List<BoAttribute> all = boAttributeManager.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<BoAttribute> allByPage = boAttributeManager.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过实体ID查询
		List<BoAttribute> byEntId = boAttributeManager.getByEntId(entId);
		assertEquals(1, byEntId.size());
		assertEquals(suid2, byEntId.get(0).getId());

		// 通过实体ID删除
		boAttributeManager.removeByEntId(entId);
		List<BoAttribute> byEntId2 = boAttributeManager.getByEntId(entId);
		assertEquals(0, byEntId2.size());

		// 通过ID删除数据
		boAttributeManager.remove(suid);
		BoAttribute sstd = boAttributeManager.getOne(Wrappers.<BoAttribute>lambdaQuery().eq(BoAttribute::getName, name));
		assertTrue(sstd==null);

		// 通过ID集合批量删除数据
		boAttributeManager.removeByIds(Arrays.asList(new String[]{suid, suid2, suid3}));
		List<BoAttribute> nowAll = boAttributeManager.list();
		assertEquals(0, nowAll.size());
	}
}
