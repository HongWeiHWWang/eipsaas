package com.hotent.mail.persistenc.manager;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.MailTestCase;
import com.hotent.mail.model.Mail;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.persistence.manager.MailSettingManager;

import org.junit.Test;

/**
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月12日
 */
public class MailSettingManagerTest extends MailTestCase{
	@Resource 
	MailSettingManager ms;
	
	@Test
	public void testCRUD(){
		 String suid = UniqueIdUtil.getSuid();
	     String name = "小明";
	     String mailAddress = "gzht@163.com";
	     MailSetting mt = new MailSetting();
	     mt.setId(suid);
	     mt.setNickName(name);
	     mt.setMailAddress(mailAddress);
	     // 添加
	     ms.create(mt);
	     // 查询
	     MailSetting bodef = ms.get(suid);
	     
	     String newDesc = "小红";
	     bodef.setNickName(newDesc);
	     // 更新
	     ms.update(bodef);

	     MailSetting bd = ms.get(suid);
	     assertEquals(newDesc, bd.getNickName());

	     String suid2 = UniqueIdUtil.getSuid();
	     String suid3 = UniqueIdUtil.getSuid();
	     
	     MailSetting mail = new MailSetting();
	     mail.setId(suid2);
	     mail.setNickName("阿狗");
	     
	     MailSetting me = new MailSetting();
	     me.setId(suid3);
	     me.setNickName("阿猫");
	     
	     ms.create(mail);
	     ms.create(me);

	     // 通过ID列表批量查询
	     List<MailSetting> byIds = ms.listByIds(Arrays.asList(new String[]{suid2,suid3}));
	     assertEquals(2, byIds.size());

	     QueryFilter queryFilter = QueryFilter.build()
	                                          .withDefaultPage()
	                                          .withQuery(new QueryField("mailAddress", mailAddress));
	     // 通过通用查询条件查询
	     PageList<MailSetting> query = ms.query(queryFilter);
	     assertEquals(1, query.getTotal());

	     // 通过指定列查询唯一记录
	     MailSetting bab = ms.getOne(Wrappers.<MailSetting>lambdaQuery().eq(MailSetting::getMailAddress, mailAddress));
	     assertEquals(mailAddress, bab.getMailAddress());

	     // 通过别名查询
	     MailSetting byAlias = ms.getMailByAddress(mailAddress);
	     assertEquals(mailAddress, byAlias.getMailAddress());

	     // 查询所有数据
	     List<MailSetting> all = ms.list();
	     assertEquals(3, all.size());

	     // 查询所有数据(分页)
	     PageList<MailSetting> allByPage = ms.page(new PageBean(1, 2));
	     assertEquals(3, allByPage.getTotal());
	     assertEquals(2, allByPage.getRows().size());

	     // 通过ID删除数据
	     ms.remove(suid);
	     MailSetting sstd = ms.getOne(Wrappers.<MailSetting>lambdaQuery().eq(MailSetting::getMailAddress, mailAddress));
	     assertTrue(sstd==null);

	     // 通过ID集合批量删除数据
	     ms.removeByIds(new String[]{suid, suid2, suid3});
	     List<MailSetting> nowAll = ms.list();
	     assertEquals(0, nowAll.size());
	}
	
	@Test
	public void testManage() throws Exception{
		MailSetting m = new MailSetting();
		String id = UniqueIdUtil.getSuid();
		String name = "mww";
		String popHost = "pop.163.com";
		String popPort = "110";
		String mailAddress = "mww_a_yjj@163.com";
		String smtpHost = "smtp.163.com";
		String smtpPort = "25";
		String password = EncryptUtil.encrypt(("mww123456"));
		String uid = "3";
		String por = MailSetting.POP3_PROTOCAL;
		Short isDefault = 1;
		m.setId(id);
		m.setPassword(password);
		m.setSmtpHost(smtpHost);
		m.setSmtpPort(smtpPort);
		m.setPopPort(popPort);
		m.setPopHost(popHost);
		m.setMailAddress(mailAddress);
		m.setNickName(name);
		m.setUserId(uid);
		m.setIsDefault(isDefault);
		m.setMailType(por);
		ms.create(m);
		
		QueryFilter queryFilter = QueryFilter.build();
		List<MailSetting> list = ms.query(queryFilter).getRows();
		assertEquals(1, list.size());
		
		//获取用户的默认邮箱
		MailSetting mls = ms.getByIsDefault(uid);
		assertEquals(mailAddress, mls.getMailAddress());
		
		//获取用户的邮件数
		int countByUserId = ms.getCountByUserId(uid);
		assertEquals(1, countByUserId);
		
		//根据邮箱地址返回相应的邮箱配置实体
		MailSetting mailByAddress = ms.getMailByAddress(mailAddress);
		assertEquals(mailAddress, mailByAddress.getMailAddress());
		
		//获取当前用户的邮箱列表
		List<MailSetting> mailByUserId = ms.getMailByUserId(uid);
		assertEquals(1, mailByUserId.size());
		
		//验证设置的邮箱地址的唯一性
		boolean b = ms.isExistMail(m);
		System.out.println(b);
		
		//测试连接
		ms.testConnection(m, false);
		
		//设置默认邮箱
		ms.setDefault(m, uid);
		MailSetting mas = ms.get(id);
		assertTrue((short)1==mas.getIsDefault());
	}
}
