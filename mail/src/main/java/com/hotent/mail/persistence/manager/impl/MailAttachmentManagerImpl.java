package com.hotent.mail.persistence.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.util.AppFileUtil;
import com.hotent.mail.model.MailAttachment;
import com.hotent.mail.persistence.dao.MailAttachmentDao;
import com.hotent.mail.persistence.manager.MailAttachmentManager;


/**
 * 外部邮件附件表 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@Service("mailAttachmentManager")
public class MailAttachmentManagerImpl extends BaseManagerImpl<MailAttachmentDao, MailAttachment> implements MailAttachmentManager{
	@Resource
	FileManager fileManager;
	
	@Override
	public List<MailAttachment> getByMailId(String mailId) {
		return baseMapper.getByMailId(mailId);
	}
	
	@Override
	public void updateFilePath(String fileName, String mailId, String filePath) {
		baseMapper.updateFilePath(fileName, mailId, filePath);
	}
	
	@Override
	public List<MailAttachment> getByOutMailFileIds(String fileIds) throws Exception {
		List<MailAttachment> result = new ArrayList<MailAttachment>();
		if(StringUtil.isEmpty(fileIds)) return result;
		JsonNode jsonNode = JsonUtil.toJsonNode(fileIds);
		for(Object obj:jsonNode){
			ObjectNode json = (ObjectNode)obj;
			String id = json.get("id").textValue();
			DefaultFile file= fileManager.get(id);
			String filePath = AppFileUtil.getBasePath()+File.separator+file.getFilePath();
			MailAttachment attachment = new MailAttachment();
			attachment.setId(id);
			attachment.setFileName(json.get("name").textValue());
			attachment.setFilePath(filePath);
			result.add(attachment);
		}
		return result;
	}
	
	@Override
	public void delByMailId(String mailId) {
		baseMapper.delByMailId(mailId);
	}
}
