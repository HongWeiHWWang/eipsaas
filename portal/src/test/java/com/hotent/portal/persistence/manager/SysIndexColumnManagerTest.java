package com.hotent.portal.persistence.manager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.FileUtil;
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.SysIndexColumn;


/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysIndexColumnManagerTest extends PortalTestCase{
	@Resource
	SysIndexColumnManager sic;

	@Test
	public void testCRUD() {
		String suid = "1314";
		SysIndexColumn sc = new SysIndexColumn();
		String alias = "student";
		sc.setAlias(alias);
		sc.setId(suid);
		
		// 添加
		sic.create(sc);
		
		// 查询
		SysIndexColumn column = sic.get(suid);
		assertEquals(alias, column.getAlias());

		String newDesc = "新的学生信息";
		sc.setAlias(newDesc);;
		// 更新
		sic.update(sc);

		SysIndexColumn sysIndexColumn = sic.get(suid);
		assertEquals(newDesc, sysIndexColumn.getAlias());

		SysIndexColumn s = new SysIndexColumn();
		String suid2 = "1312";
		String name = "首页栏目";
		s.setId(suid2);
		s.setAlias(name);
		
		SysIndexColumn sys = new SysIndexColumn();
		String suid3 = "1315";
		String newAlias = "门户栏目";
		sys.setId(suid3);
		sys.setAlias(newAlias);
	
		sic.create(s);
		sic.create(sys);

		// 通过ID列表批量查询
		List<SysIndexColumn> byIds = sic.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("alias", newDesc));
		// 通过通用查询条件查询
		PageList<SysIndexColumn> query = sic.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysIndexColumn bab = sic.getOne(Wrappers.<SysIndexColumn>lambdaQuery().eq(SysIndexColumn::getAlias, newDesc));
		assertEquals(newDesc, bab.getAlias());

		// 通过别名查询
		SysIndexColumn byAlias = sic.getByColumnAlias(newDesc);
		assertEquals(newDesc, byAlias.getAlias());

		// 查询所有数据
		List<SysIndexColumn> all = sic.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysIndexColumn> allByPage = sic.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		sic.remove(suid);
		SysIndexColumn sstd = sic.getOne(Wrappers.<SysIndexColumn>lambdaQuery().eq(SysIndexColumn::getAlias, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		sic.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysIndexColumn> nowAll = sic.list();
		assertEquals(0, nowAll.size());

	}

	@Test
	public void testSysTypeManager() throws Exception {
		String alias="我的";
		SysIndexColumn sc = new SysIndexColumn();
		String id = "7";
		String designHtml = FileUtil.readFile("D:/x7/x7/portal/src/test/resources/temp/test.html");
		sc.setAlias(alias);
		sc.setId(id);
		sc.setTemplateHtml(designHtml);
		Map<String, Object> params = new HashMap<>();
		//通过别名获取模板
		String byColumnAlias = sic.getHtmlByColumnAlias(alias, params);
		
		
		short type = 0;
		Boolean isParse = false;
		QueryFilter filter = QueryFilter.build();
		filter.addFilter("alias", alias, QueryOP.EQUAL);
		//获取有权限的栏目
//		List<SysIndexColumn> hashRightColumnList = sic.getHashRightColumnList(filter , params, isParse, type, null);
		
		//通过别名获取模板
		String htmlByColumnAlias = sic.getHtmlByColumnAlias(alias, params);
		System.out.println(htmlByColumnAlias);
		
		//是否存在别名
		Boolean existAlias = sic.isExistAlias(alias, id );
		assertFalse(existAlias);
		
		List<SysIndexColumn> columnList = new ArrayList<>();
		//解析设计模版的html
		String parserDesignHtml = sic.parserDesignHtml(designHtml, columnList);
		System.out.println(parserDesignHtml);
	}
}
