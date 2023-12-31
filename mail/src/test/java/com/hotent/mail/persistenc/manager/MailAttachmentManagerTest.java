package com.hotent.mail.persistenc.manager;

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
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.MailTestCase;
import com.hotent.mail.model.MailAttachment;
import com.hotent.mail.persistence.manager.MailAttachmentManager;

/**
 * MailAttachmentManager测试类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月12日
 */
public class MailAttachmentManagerTest extends MailTestCase {

	@Resource
	MailAttachmentManager mat;

	@Test
	public void testCRUD() {
		String suid = UniqueIdUtil.getSuid();
		String name = "附件1";
		MailAttachment m = new MailAttachment();
		m.setId(suid);
		m.setFileName(name);

		// 添加
		mat.create(m);

		// 查询
		MailAttachment mailAttachment = mat.get(suid);
		assertEquals(name, mailAttachment.getFileName());

		String newName = "新的附件";
		m.setFileName(newName);
		// 更新
		mat.update(m);

		MailAttachment attachment = mat.get(suid);
		assertEquals(newName, attachment.getFileName());

		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();

		MailAttachment m1 = new MailAttachment();
		m1.setFileName("附件2");
		m1.setId(suid2);

		MailAttachment m2 = new MailAttachment();
		m2.setId(suid3);
		m2.setFileName("附件3");

		mat.create(m1);
		mat.create(m2);

		// 通过id列表批量查询
		List<MailAttachment> byIds = mat.listByIds(Arrays.asList(new String[] { suid2, suid3 }));
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField("fileName", newName));
		// 通过通用查询条件查询
		PageList<MailAttachment> query = mat.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		MailAttachment unique = mat.getOne(Wrappers.<MailAttachment>lambdaQuery().eq(MailAttachment::getFileName, newName));
		assertEquals(newName, unique.getFileName());

		// 查询所有数据
		List<MailAttachment> all = mat.list();
		assertEquals(3, all.size());

		// 查询所有数据(分页)
		PageList<MailAttachment> allByPage = mat.page(new PageBean(1, 2));
		assertEquals(3, allByPage.getTotal());
		assertEquals(2, allByPage.getRows().size());

		// 通过id删除数据
		mat.remove(suid);
		MailAttachment mc = mat.getOne(Wrappers.<MailAttachment>lambdaQuery().eq(MailAttachment::getFileName, newName));
		assertTrue(mc == null);

		// 通过id集合批量删除数据
		mat.removeByIds(new String[] { suid2, suid3 });
		List<MailAttachment> list = mat.list();
		assertEquals(0, list.size());
	}

	@Test
	public void testManage() throws Exception {
		MailAttachment m = new MailAttachment();
		String mailId = "1";
		String fileName = "test";
		String filepath = "D:/developerSoft/apache-maven-3.5.3/conf";
		m.setMailId(mailId);
		m.setId(UniqueIdUtil.getSuid());
		m.setFileName(fileName);
		m.setFilePath(filepath);
		mat.create(m);
		
		//根据邮箱ID获取邮箱附件
		List<MailAttachment> list = mat.getByMailId(mailId);
		assertEquals(1, list.size());
		
		String newfilePath = "D:/";
		//更新附件文件路径
		mat.updateFilePath(fileName, mailId, newfilePath);
		List<MailAttachment> s = mat.getByMailId(mailId);
		assertEquals(1, list.size());
		assertEquals(newfilePath, s.get(0).getFilePath());
		
		//根据OutMail实体的fileIds获取附件集合
		String filds = "1";
		List<MailAttachment> ids = mat.getByOutMailFileIds(filds);
		assertEquals(0, ids.size());
		
		//根据邮件ID删除附件
		mat.delByMailId(mailId);
		List<MailAttachment> byMailId = mat.getByMailId(mailId);
		assertEquals(0, byMailId.size());
		
	}
}
