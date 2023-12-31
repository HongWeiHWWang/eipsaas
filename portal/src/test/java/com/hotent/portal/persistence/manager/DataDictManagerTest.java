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
import com.hotent.portal.PortalTestCase;
import com.hotent.sys.persistence.manager.DataDictManager;
import com.hotent.sys.persistence.model.DataDict;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class DataDictManagerTest extends PortalTestCase{
	@Resource
	DataDictManager dm;
	
	@Test
	public void testCRUD(){
		String suid = idGenerator.getSuid();
		DataDict sc = new DataDict();
		String alias = "22233";
		sc.setId(suid);
		sc.setName(alias);

		// 添加
		dm.create(sc);

		// 查询
		DataDict column = dm.get(suid);
		assertEquals(alias, column.getName());

		String newDesc = "新的学生信息";
		sc.setName(newDesc);
		
		// 更新
		dm.update(sc);

		DataDict dataDict = dm.get(suid);
		assertEquals(newDesc, dataDict.getName());

		DataDict s = new DataDict();
		String suid2 = idGenerator.getSuid();
		String name = "首页栏目";
		s.setId(suid2);
		s.setName(name);

		DataDict sys = new DataDict();
		String suid3 = idGenerator.getSuid();
		String newAlias = "门户栏目";
		sys.setId(suid3);
		sys.setName(newAlias);

		dm.create(s);
		dm.create(sys);

		// 通过ID列表批量查询
		List<DataDict> byIds = dm.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("name", newDesc));
		// 通过通用查询条件查询
		PageList<DataDict> query = dm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		DataDict bab = dm.getOne(Wrappers.<DataDict>lambdaQuery().eq(DataDict::getName, newDesc));
		assertEquals(newDesc, bab.getName());

		// 查询所有数据
		 List<DataDict> all = dm.list();
		 assertEquals(3, all.size());

		// 查询所有数据(分页)
		 PageList<DataDict> allByPage = dm.page(new PageBean(1, 2));
		 assertEquals(3, allByPage.getTotal());
		 assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		dm.remove(suid);
		DataDict sstd = dm.getOne(Wrappers.<DataDict>lambdaQuery().eq(DataDict::getName, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		dm.removeByIds(new String[] { suid, suid2, suid3 });
		List<DataDict> nowAll = dm.list();
		assertEquals(0, nowAll.size());	
	
	}
	
	@Test
	public void testSysTypeManager(){
		String id = idGenerator.getSuid();
		String typeId = "22";
		String key = "12312";
		String parentId = "2213";
		int sn = 21;
		DataDict sc = new DataDict();
		sc.setTypeId(typeId);
		sc.setKey(key);
		sc.setParentId(parentId);
		sc.setSn(sn);
		dm.create(sc);
		
		//通过 类型，和字典key 获取字典项
		DataDict dictKey = dm.getByDictKey(typeId, key);
		assertEquals(key, dictKey.getKey());
		
		//通过数据字典类别查询所有的数据字典项
		List<DataDict> list = dm.getByTypeId(typeId);
		assertEquals(1, list.size());
		
		//通过父节点获取子节点（包含二级子节点）
		List<DataDict> childrenByParentId = dm.getChildrenByParentId(parentId);
		assertEquals(1, childrenByParentId.size());
		
		//通过父节点ID获取一级子节点
		List<DataDict> byParentId = dm.getFirstChilsByParentId(parentId);
		assertEquals(1, byParentId.size());
		
		//更新排序
		dm.updSn(id, sn);

	}
}
