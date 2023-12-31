/**
 * 
 */
package com.hotent.portal.persistence.manager;

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
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.SysLayoutTools;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysLayoutToolsManagerTest extends PortalTestCase{
	@Resource
	SysLayoutToolsManager sltm;
	
	@Test
	public void testCRUD(){
		String suid = UniqueIdUtil.getSuid();
		String type = "酷炫风格";
		SysLayoutTools sdm = new SysLayoutTools();
		sdm.setId(suid);
		sdm.setToolsType(type);

		// 添加
		sltm.create(sdm);
		// 查询
		SysLayoutTools bodef = sltm.get(suid);
		assertEquals(type, bodef.getToolsType());

		String newType = "麦兜风格";
		bodef.setToolsType(newType);
		// 更新
		sltm.update(bodef);

		SysLayoutTools bd = sltm.get(suid);
		assertEquals(newType, bd.getToolsType());

		String suid2 = UniqueIdUtil.getSuid();
		String type1 = "bang风格";
		SysLayoutTools st = new SysLayoutTools();
		st.setId(suid2);
		st.setToolsType(type1);

		String suid3 = UniqueIdUtil.getSuid();
		String type2 = "thal风格";
		SysLayoutTools stm = new SysLayoutTools();
		stm.setId(suid3);
		stm.setToolsType(type2);

		sltm.create(st);
		sltm.create(stm);

		// 通过ID列表批量查询
		List<SysLayoutTools> byIds = sltm.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("toolsType", newType));
		// 通过通用查询条件查询
		PageList<SysLayoutTools> query = sltm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysLayoutTools bab = sltm.getOne(Wrappers.<SysLayoutTools>lambdaQuery().eq(SysLayoutTools::getToolsType, newType));
		assertEquals(newType, bab.getToolsType());

		// 查询所有数据
		List<SysLayoutTools> all = sltm.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysLayoutTools> allByPage = sltm.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		sltm.remove(suid);
		SysLayoutTools sstd = sltm.getOne(Wrappers.<SysLayoutTools>lambdaQuery().eq(SysLayoutTools::getToolsType, newType));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		sltm.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysLayoutTools> nowAll = sltm.list();
		assertEquals(0, nowAll.size());	
	
	}
	
	@Test
	public void testSysTypeManager(){
		String id = idGenerator.getSuid();
		String toolsType = "newtype";
		String layoutId = "333";
		SysLayoutTools slts = new SysLayoutTools();
		slts.setId(id);
		slts.setLayoutId(layoutId);
		slts.setToolsType(toolsType);
		sltm.create(slts);
		
		//返回布局工具
		SysLayoutTools byLayoutID = sltm.getByLayoutID(layoutId, toolsType);
		assertEquals(toolsType, byLayoutID.getToolsType());
	}
}
