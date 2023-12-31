package com.hotent.portal.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.PortalTestCase;
import com.hotent.portal.model.SysIndexTools;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysIndexToolsManagerTest extends PortalTestCase{
	@Resource
	SysIndexToolsManager sitm;

	@Test
	public void testCRUD() {
		String suid = UniqueIdUtil.getSuid();
		String alias = "faker";
		SysIndexTools sdm = new SysIndexTools();
		sdm.setId(suid);
		sdm.setName(alias);

		// 添加
		sitm.create(sdm);
		// 查询
		SysIndexTools bodef = sitm.get(suid);
		assertEquals(alias, bodef.getName());

		String newDesc = "leo";
		bodef.setName(newDesc);
		// 更新
		sitm.update(bodef);

		SysIndexTools bd = sitm.get(suid);
		assertEquals(newDesc, bd.getName());

		String suid2 = UniqueIdUtil.getSuid();
		String name = "bang";
		SysIndexTools st = new SysIndexTools();
		st.setId(suid2);
		st.setName(name);

		String suid3 = UniqueIdUtil.getSuid();
		String newName = "thal";
		SysIndexTools stm = new SysIndexTools();
		stm.setId(suid3);
		stm.setName(newName);

		sitm.create(st);
		sitm.create(stm);

		// 通过ID列表批量查询
		List<SysIndexTools> byIds = sitm.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("name", newDesc));
		// 通过通用查询条件查询
		PageList<SysIndexTools> query = sitm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysIndexTools bab = sitm.getOne(Wrappers.<SysIndexTools>lambdaQuery().eq(SysIndexTools::getName, newDesc));
		assertEquals(newDesc, bab.getName());

		// 查询所有数据
		List<SysIndexTools> all = sitm.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysIndexTools> allByPage = sitm.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		sitm.remove(suid);
		SysIndexTools sstd = sitm.getOne(Wrappers.<SysIndexTools>lambdaQuery().eq(SysIndexTools::getName, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		sitm.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysIndexTools> nowAll = sitm.list();
		assertEquals(0, nowAll.size());

	}

	@Test
	public void testSysTypeManager() throws Exception {
		String id = idGenerator.getSuid();
		String name = "boby";
		SysIndexTools ss = new SysIndexTools();
		ss.setId(id);
		ss.setName(name);
		sitm.create(ss);
		
		Map<String, Object> params = new HashMap<>();
		String dataParam ="name=小明, type=1, model=2, value=3";
		dataParam = JsonUtil.toJson(JsonUtil.toJsonNode(dataParam));
		Object[] param = sitm.getDataParam(dataParam, params);
		
		Class<?>[] parameterTypes=null ;
		Object[] args = null;
		String handler = null;
		//获取实体通过处理机制
		Object object = sitm.getModelByHandler(handler, args, parameterTypes);
		
		//通过类型获取参数
		Class<?>[] types = sitm.getParameterTypes(dataParam, params);
		
		List<String> toolsIds = new ArrayList<>();
		toolsIds.add(id);
		//根据ids获取列表
		List<SysIndexTools> toolsByIds = sitm.getToolsByIds(toolsIds );
		assertEquals(1, toolsByIds.size());
	}
}
