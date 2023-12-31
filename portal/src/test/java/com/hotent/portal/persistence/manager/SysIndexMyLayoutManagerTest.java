/**
 * 
 */
package com.hotent.portal.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexMyLayout;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysIndexMyLayoutManagerTest extends PortalTestCase{
	@Resource
	SysIndexMyLayoutManager sml;

	@Test
	public void testCRUD() {
		String suid = UniqueIdUtil.getSuid();
		String alias = "faker";
		SysIndexMyLayout sdm = new SysIndexMyLayout();
		sdm.setId(suid);
		sdm.setDesignHtml(alias);

		// 添加
		sml.create(sdm);
		// 查询
		SysIndexMyLayout bodef = sml.get(suid);
		assertEquals(alias, bodef.getDesignHtml());

		String newDesc = "leo";
		bodef.setDesignHtml(newDesc);
		// 更新
		sml.update(bodef);

		SysIndexMyLayout bd = sml.get(suid);
		assertEquals(newDesc, bd.getDesignHtml());

		String suid2 = UniqueIdUtil.getSuid();
		String name = "bang";
		SysIndexMyLayout st = new SysIndexMyLayout();
		st.setId(suid2);
		st.setDesignHtml(name);

		String suid3 = UniqueIdUtil.getSuid();
		String newName = "thal";
		SysIndexMyLayout stm = new SysIndexMyLayout();
		stm.setId(suid3);
		stm.setDesignHtml(newName);

		sml.create(st);
		sml.create(stm);

		// 通过ID列表批量查询
		List<SysIndexMyLayout> byIds = sml.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("designHtml", newDesc));
		// 通过通用查询条件查询
		PageList<SysIndexMyLayout> query = sml.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysIndexMyLayout bab = sml.getOne(Wrappers.<SysIndexMyLayout>lambdaQuery().eq(SysIndexMyLayout::getDesignHtml, newDesc));
		assertEquals(newDesc, bab.getDesignHtml());

		// 查询所有数据
		List<SysIndexMyLayout> all = sml.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysIndexMyLayout> allByPage = sml.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		sml.remove(suid);
		SysIndexMyLayout sstd = sml.getOne(Wrappers.<SysIndexMyLayout>lambdaQuery().eq(SysIndexMyLayout::getDesignHtml, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		sml.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysIndexMyLayout> nowAll = sml.list();
		assertEquals(0, nowAll.size());

	}

	@Test
	public void testSysTypeManager() {
		String id = idGenerator.getSuid();
		String currentUserId="2222";
		String designHtml = "aaas";
		String temp="炸点";
		SysIndexMyLayout siml = new SysIndexMyLayout();
		siml.setId(id);
		siml.setTemplateHtml(temp);
		siml.setUserId(currentUserId);
		siml.setDesignHtml(designHtml);
		sml.create(siml);
		
		// 根据当前用户id获取我的布局
		SysIndexMyLayout byUser = sml.getByUser(currentUserId);
		assertEquals(designHtml, byUser.getDesignHtml());

		List<SysIndexColumn> columnList = new ArrayList<>();
		// 返回我的布局实体
		SysIndexMyLayout list = sml.getLayoutList(currentUserId , columnList );
		assertTrue(list!=null);

		// 获取首页我的数据
		String data = sml.obtainIndexMyData(id);
		System.out.println(data);

		// 获取我的大首页数据
		String myIndexData = sml.obtainMyIndexData(currentUserId);
		System.out.println(myIndexData);
	}
}
