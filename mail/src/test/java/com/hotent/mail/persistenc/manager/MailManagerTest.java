package com.hotent.mail.persistenc.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.MailTestCase;
import com.hotent.mail.model.Mail;
import com.hotent.mail.model.MailLinkman;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.persistence.manager.MailManager;
import com.hotent.mail.persistence.manager.MailSettingManager;

/**
 * MailManager测试类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月12日
 */
public class MailManagerTest extends MailTestCase {

	@Resource
	MailManager mg;
	@Resource
	MailSettingManager ms;

	@org.junit.Test
	public void testCRUD() {
		String suid = UniqueIdUtil.getSuid();
		String name = "gzht";
		String email = "gzht@163.com";
		Mail m = new Mail();
		m.setId(suid);
		m.setSenderName(name);
		m.setSenderAddress(email);
		m.setReceiverAddresses("gzht@qq.com");
		m.setSubject("测试");
		m.setContent("test1111111111");
		// 添加
		mg.create(m);

		// 查询
		Mail mail = mg.get(suid);
		assertEquals(name, mail.getSenderName());

		String newDesc = "alibaba";
		mail.setSenderName(newDesc);
		// 更新
		mg.update(mail);

		Mail m2 = mg.get(suid);
		assertEquals(newDesc, m2.getSenderName());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();

		Mail m3 = new Mail();
		m3.setId(suid2);
		m3.setSenderName("qq");

		Mail mq = new Mail();
		mq.setId(suid3);
		mq.setSenderName("wy");

		mg.create(m3);
		mg.create(mq);

		// 通过ID列表批量查询
		List<Mail> byIds = mg.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage()
				.withQuery(new QueryField("senderName", newDesc));
		// 通过通用查询条件查询
		PageList<Mail> query = mg.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		Mail bab = mg.getOne(Wrappers.<Mail>lambdaQuery().eq(Mail::getSenderName, newDesc));
		assertEquals(newDesc, bab.getSenderName());

		// 通过email查询
		String byAlias = mg.getNameByEmail(email);
		System.out.println(byAlias);

		// 查询所有数据
		List<Mail> all = mg.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<Mail> allByPage = mg.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过ID删除数据
		mg.remove(suid);
		Mail sstd = mg.getOne(Wrappers.<Mail>lambdaQuery().eq(Mail::getSenderName, newDesc));
		assertTrue(sstd == null);

		// 通过ID集合批量删除数据
		mg.removeByIds(new String[]{suid, suid2, suid3});
		List<Mail> nowAll = mg.list();
		assertEquals(0, nowAll.size());
	}
	
	@org.junit.Test
	public void testManage() throws Exception{
		MailSetting m = new MailSetting();
		String popHost = "pop.163.com";
		String popPort = "110";
		String mailAddress = "mww_a_yjj@163.com";
		String smtpHost = "smtp.163.com";
		String smtpPort = "25";
		String password = "mww123456";
		String uid = "3";
		String userId = "4";
		String protocal = MailSetting.POP3_PROTOCAL;
		m.setPassword(password);
		m.setSmtpHost(smtpHost);
		m.setSmtpPort(smtpPort);
		m.setPopPort(popPort);
		m.setPopHost(popHost);
		m.setReceiveHost(popHost);
		m.setReceivePort(popPort);
		m.setMailAddress(mailAddress);
		m.setUserId(userId);
		m.setId(uid);
		m.setProtocal(protocal);
		ms.create(m);
		
		Mail mi = new Mail();
		String suid = "1";
		Short isReply = 1;
		Short read = 1;
		Short type = 1;
		String content = "我的朋友";
		String subject = "生日祝賀！";
		String rd = "1353669924@qq.com";
		mi.setId(suid);
		mi.setSetId(uid);
		mi.setIsReply(isReply);
		mi.setIsRead(read);
		mi.setType(type);
		mi.setUserId(userId);
		mi.setSubject(subject);
		mi.setContent(content);
		mi.setReceiverAddresses(rd);
		mg.create(mi);
		
		//根据邮箱设定获取邮件列表
		List<Mail> lists = mg.getMailListBySetting(m);
		
		//保存邮件至垃圾箱
		String[] lAryId = new String[]{suid};
		mg.addDump(lAryId);
		
		//通过setId删除
		mg.delBySetId(uid);
		Mail m1 = mg.get(suid);
		assertTrue(m1==null);
		
		//浏览邮件
		mg.emailRead(mi);
		Short isRead = mi.getIsRead();
		assertTrue(isRead == (short)1);
		
		//得到用于回复页面显示信息
		mg.create(mi);
		Mail reply = mg.getMailReply(suid);
		assertTrue(reply!=null);
		
		//邮箱树形列表的json数据
		List<MailSetting> list = mg.getMailTreeData(uid);
		System.out.println(list.size());
		
		//通过邮件获取名称
		String email = "add";
		String nameByEmail = mg.getNameByEmail(email);
		assertTrue(nameByEmail!=null);
		
		//发送邮件,保存邮件信息至本地,添加/更新最近联系人
		String p = MailSetting.SMTP_PROTOCAL;
		m.setProtocal(p);
		m.setSendHost(smtpHost);
		m.setSendPort(smtpPort);
		ms.update(m);
		String mail = mg.sendMail(mi, null, null, 0, null, null);
		System.out.println(mail);
		
		//开始执行分页查询 
		mg.page(new PageBean(0,2));
	}
}
