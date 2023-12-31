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
import com.hotent.sys.persistence.manager.SysTypeManager;
import com.hotent.sys.persistence.model.SysType;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysTypeManagerTest extends PortalTestCase{
	@Resource
	SysTypeManager stm;
	
	@Test
	public void testCRUD(){
		String suid = UniqueIdUtil.getSuid();
		String alias = "faker";
		SysType sdm = new SysType();
		int sn = 1;
		String typeGroupKey="NEW";
		String typeKey = "bus";
		sdm.setId(suid);
		sdm.setName(alias);
		sdm.setTypeGroupKey(typeGroupKey);
		sdm.setTypeKey(typeKey);
		sdm.setStruType((short) 0);
		sdm.setSn(sn);
		
		// 添加
		stm.create(sdm);
		// 查询
		SysType bodef = stm.get(suid);
		assertEquals(alias, bodef.getName());

		String newDesc = "leo";
		bodef.setName(newDesc);
		// 更新
		stm.update(bodef);

		SysType bd = stm.get(suid);
		assertEquals(newDesc, bd.getName());

		String suid2 = UniqueIdUtil.getSuid();
		String name = "bang";
		SysType stp = new SysType();
		int sn1 = 2;
		String typeGroupKey1="now";
		String typeKey1 = "buss";
		SysType st = new SysType();
		st.setId(suid2);
		st.setTypeGroupKey(typeGroupKey1);
		st.setSn(sn1);
		st.setTypeKey(typeKey1);
		st.setStruType((short) 1);
		st.setName(name);

		String suid3 = UniqueIdUtil.getSuid();
		String newName = "thal";
		int sn2 = 3;
		String typeGroupKey2="nowo";
		String typeKey2 = "bussye";
		SysType sbm = new SysType();
		sbm.setTypeGroupKey(typeGroupKey2);
		sbm.setId(suid3);
		sbm.setTypeKey(typeKey2);
		sbm.setSn(sn2);
		sbm.setStruType((short) 1);
		sbm.setName(newName);

		stm.create(st);
		stm.create(sbm);

		// 通过ID列表批量查询
		List<SysType> byIds = stm.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("name", newDesc));
		// 通过通用查询条件查询
		PageList<SysType> query = stm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysType bab = stm.getOne(Wrappers.<SysType>lambdaQuery().eq(SysType::getName, newDesc));
		assertEquals(newDesc, bab.getName());

		// 查询所有数据
		List<SysType> all = stm.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysType> allByPage = stm.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		stm.remove(suid);
		SysType sstd = stm.getOne(Wrappers.<SysType>lambdaQuery().eq(SysType::getName, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		stm.removeByIds(new String[] { suid, suid2, suid3 });
		List<SysType> nowAll = stm.list();
		assertEquals(0, nowAll.size());	
	
	}
	
	@Test
	public void testSysTypeManager(){
		
		String id="";
		//根据Id删除节点和其所有的子节点
		stm.delByIds(id);
		
		String currUserId="";
		String groupKey="";
		//通过分类组业务主键获取所有的公共分类和属于当前人的私有分类
		List<SysType> list = stm.getByGroupKey(groupKey);
	
		String typeKey="";
		//根据key获取对象
		SysType key = stm.getByKey(typeKey);
		
		Long parentId = (long) 0;
		//通过父节点获取系统类型
		List<SysType> byParentId = stm.getByParentId(parentId.toString());
		
		//通过类型key和分组key获取系统类型
		SysType type = stm.getByTypeKeyAndGroupKey(groupKey, typeKey);
		
		//通过typeKey获取下级
		List<SysType> childByTypeKey = stm.getChildByTypeKey(typeKey);
		
		int isRoot = 0;
		String ty = "";
		//取得初始分类类型。
		SysType sysType = stm.getInitSysType(isRoot, ty);
		
		String userId="";
		String partId="";
		//根据节点Id和当前用户Id获取下一级的私有分类和公共分类
		List<SysType> byPartId = stm.getPrivByPartId(partId, userId);
		
		int sn = 0;
		String typeId="";
		//更新排序 sn
		stm.updSn(typeId, sn);
		
		String typeGroupKey="";
		//是否存在键值
		boolean exist = stm.isKeyExist(id, typeGroupKey, typeKey);
		
		//根据键值获取xml格式数据。
		String xmlByKey = stm.getXmlByKey(typeKey);
	}
}
