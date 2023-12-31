package com.hotent.bo.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.core.BoTestCase;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;

public class BoDefManagerTest extends BoTestCase{
	@Resource
	BoDefManager boDefManager;
	
	private BoDef build(String id, String alias, String desc, String categoryId){
		BoDef boDef = new BoDef();
		boDef.setId(id);
		boDef.setAlias(alias);
		boDef.setDescription(desc);
		boDef.setCategoryId(categoryId);
		return boDef;
	}
	
	//生成一些测试数据
	//@Test
	public void generatorData() {
		String[] aliases = new String[]{"teacher", "student", "security", "chief", "cleaner" };
		String[] descs = new String[]{"教师信息", "学生信息", "保安信息", "厨师长信息", "保洁员信息" };
		String[] categoryIds = new String[]{"1", "2", "3", "4", "5"};
		for(int i = 0; i < 200; i++) {
			Random rnd = new Random();
			int n = rnd.nextInt(5);
			String suid = UniqueIdUtil.getSuid();
			boDefManager.create(build(suid, aliases[n] + suid, descs[n] + suid, categoryIds[n]));
		}
	}
	
	@Test
	public void testBodefSave() throws Exception {
		String json = FileUtil.readByClassPath("json/bodef-simple.json");
		assertTrue(StringUtil.isNotEmpty(json));
		boDefManager.saveFormData(json);
		BoDef boDef = boDefManager.getByAlias("qjdx");
		BoEnt boEnt = boDef.getBoEnt();
		List<BoAttribute> boAttrList = boEnt.getBoAttrList();
		assertTrue(boAttrList.size() > 0);
	}
	
//	@Test
	public void testCrud(){
		String suid = UniqueIdUtil.getSuid();
		String alias = "student";
		// 添加
		boDefManager.create(build(suid, alias, "学生信息", "1001"));
		// 查询
		BoDef bodef = boDefManager.get(suid);
		assertEquals(alias, bodef.getAlias());
		assertFalse(bodef.isSupportDb());
		assertFalse(bodef.isDeployed());

		String newDesc = "新的学生信息";
		bodef.setDescription(newDesc);
		// 更新
		boDefManager.update(bodef);

		BoDef bd = boDefManager.get(suid);
		assertEquals(newDesc, bd.getDescription());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		
		boDefManager.create(build(suid2, "school", "学校信息", "1001"));
		boDefManager.create(build(suid3, "class", "班级信息", "1001"));

		// 通过ID列表批量查询
		List<BoDef> byIds = boDefManager.listByIds(Arrays.asList(new String[]{suid2,suid3}));
		assertEquals(2, byIds.size());

		QueryFilter<BoDef> queryFilter = QueryFilter.<BoDef>build()
											 .withDefaultPage()
											 .withQuery(new QueryField("alias", alias));
		// 通过通用查询条件查询
		PageList<BoDef> query = boDefManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BoDef bab = boDefManager.getOne(Wrappers.<BoDef>lambdaQuery().eq(BoDef::getAlias, alias));
		assertEquals(alias, bab.getAlias());
		
		// 通过别名查询
		BoDef byAlias = boDefManager.getByAlias(alias);
		assertEquals(alias, byAlias.getAlias());

		// 查询所有数据
		List<BoDef> all = boDefManager.list();
		assertEquals(3, all.size());
		
		// 查询所有数据(分页)
		PageList<BoDef> allByPage = boDefManager.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());
		
		// 通过ID删除数据
		boDefManager.remove(suid);
		BoDef sstd = boDefManager.getOne(Wrappers.<BoDef>lambdaQuery().eq(BoDef::getAlias, alias));
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		boDefManager.removeByIds(new String[]{suid, suid2, suid3});
		List<BoDef> nowAll = boDefManager.list();
		assertEquals(0, nowAll.size());
	}
}
