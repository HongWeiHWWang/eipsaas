package com.hotent.portal.persistence.manager;

import static org.junit.Assert.*;

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
import com.hotent.portal.model.SysIndexLayoutManage;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月20日
 */
public class SysIndexLayoutManageManagerTest extends PortalTestCase{
	
	@Resource
	SysIndexLayoutManageManager slmm;

	@Test
	public void testCRUD() {
		String suid = idGenerator.getSuid();
		String alias = "student";
		SysIndexLayoutManage sdm = new SysIndexLayoutManage();
		sdm.setId(suid);
		sdm.setName(alias);
		
		// 添加
		slmm.create(sdm);
		// 查询
		SysIndexLayoutManage bodef = slmm.get(suid);
		assertEquals(alias, bodef.getName());

		String newDesc = "新的学生信息";
		bodef.setName(newDesc);
		// 更新
		slmm.update(bodef);

		SysIndexLayoutManage bd = slmm.get(suid);
		assertEquals(newDesc, bd.getName());

		String suid2 = idGenerator.getSuid();
		String name = "我的首页";
		SysIndexLayoutManage st = new SysIndexLayoutManage();
		st.setId(suid2);
		st.setName(name);
		
		String suid3 = idGenerator.getSuid();
		String newName = "首页";
		SysIndexLayoutManage stm = new SysIndexLayoutManage();
		stm.setId(suid3);
		stm.setName(newName);

		slmm.create(st);
		slmm.create(stm);

		// 通过ID列表批量查询
		List<SysIndexLayoutManage> byIds = slmm.listByIds(Arrays.asList(new String[]{ suid2, suid3}));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("name", newDesc));
		// 通过通用查询条件查询
		PageList<SysIndexLayoutManage> query = slmm.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		SysIndexLayoutManage bab = slmm.getOne(Wrappers.<SysIndexLayoutManage>lambdaQuery().eq(SysIndexLayoutManage::getName, newDesc));
		assertEquals(newDesc, bab.getName());

		// 查询所有数据
		List<SysIndexLayoutManage> all = slmm.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<SysIndexLayoutManage> allByPage = slmm.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		slmm.remove(suid);
		SysIndexLayoutManage sstd = slmm.getOne(Wrappers.<SysIndexLayoutManage>lambdaQuery().eq(SysIndexLayoutManage::getName, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		slmm.removeByIds(Arrays.asList(new String[] { suid, suid2, suid3 }));
		List<SysIndexLayoutManage> nowAll = slmm.list();
		assertEquals(0, nowAll.size());

	}

	@Test
	public void testSysTypeManager() {
		String id = idGenerator.getSuid();
		String name = "my";
		Short layoutType=0;
		String orgId="222";
		String defaultHtml = "SSS";
		String temp = "aaa";
		String userId = "12";
		SysIndexLayoutManage sm = new SysIndexLayoutManage();
		sm.setId(id);
		sm.setName(name);
		sm.setLayoutType(layoutType);
		sm.setTemplateHtml(temp);
		sm.setDesignHtml(defaultHtml);
		sm.setOrgId(orgId);
		sm.setIsDef((short) 1);
		slmm.create(sm);
		
		//通过组织Id获取默认布局
		SysIndexLayoutManage byOrgId = null;//slmm.getEnableByOrgIdAndType(orgId);
		assertTrue(byOrgId.getLayoutType()==layoutType);
	
		//通过组织和布局类型获取实体
		SysIndexLayoutManage layoutManage = null;//slmm.getByOrgIdAndLayoutType(orgId, layoutType);
		assertTrue(layoutType!=null);
		
		//获取默认的设计模版
		String html = slmm.getDefaultDesignHtml();
		System.out.println(html);
		
		//获得布局实体
		SysIndexLayoutManage list = slmm.getLayoutList(userId, null, layoutType);
		
		//系统管理员的默认布局
//		String managerLayout = slmm.getManagerLayout();
		
		//找自己拥有权限的管理布局
//		String layout = slmm.getMyHasRightsLayout();
		
		//判断布局名称是否重复
		Boolean existName = slmm.isExistName(name);
		assertTrue(existName);
		
		short layoutId=1;
		sm.setLayoutType(layoutId);
		slmm.update(sm);
		//获取手机的首页布局
		String obtainIndexManageMobileData = slmm.obtainIndexManageMobileData(id);
		System.out.println(obtainIndexManageMobileData);
		
		//获取主页管理数据
		String data = slmm.obtainIndexManageData(id);
		System.out.println(data);
		
		//取消所有默认当前布局类型
		slmm.cancelOrgIsDef(orgId, layoutType);
	}
}
