package com.hotent.mail.persistence.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.SQLUtil;
import com.hotent.mail.model.MailLinkman;
import com.hotent.mail.persistence.dao.MailLinkmanDao;
import com.hotent.mail.persistence.manager.MailLinkmanManager;


/**
 * 外部邮件最近联系 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@Service("mailLinkmanManager")
public class MailLinkmanManagerImpl extends BaseManagerImpl<MailLinkmanDao,MailLinkman> implements MailLinkmanManager{

	@Override
	public MailLinkman findLinkMan(String address, String userId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("mailId", address);
		params.put("userId", userId);
		return baseMapper.findLinkMan(params);
	}

	@Override
	public List<MailLinkman> getAllByUserId(String userId, String condition) {
		Map params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("condition", condition);
		String db = SQLUtil.getDbType();
		if(db.equals("db2")){
		return baseMapper.getAllByUserIdDb2(params);
		}else if(db.equals("oracle")){
		return baseMapper.getAllByUserIdOracl(params);	
		}else if(db.equals("h2")){
		return baseMapper.getAllByUserIdH2(params);
		}else if(db.equals("mssql")){
		return baseMapper.getAllByUserIdMssql(params);
		}else if(db.equals("mysql")){
		return baseMapper.getAllByUserIdMysql(params);
		}else{
		return baseMapper.getAllByUserIdDm(params);	
		}		
	}

	@Override
	public List<MailLinkman> queryByAll(String query) {
		return baseMapper.queryByAll(query);
	}
}
