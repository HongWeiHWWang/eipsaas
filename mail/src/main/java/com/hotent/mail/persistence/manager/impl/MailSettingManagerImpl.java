package com.hotent.mail.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.exception.NotFoundException;
import com.hotent.base.exception.ServerRejectException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.persistence.dao.MailSettingDao;
import com.hotent.mail.persistence.manager.MailManager;
import com.hotent.mail.persistence.manager.MailSettingManager;
import com.hotent.mail.util.ExchangeMailUtil;
import com.hotent.mail.util.MailUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IUserService;


/**
 * 外部邮件用户设置 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author liyg
 * @email liyg@jee-soft.cn
 * @date 2018年6月22日
 */
@Service("mailSettingManager")
public class MailSettingManagerImpl extends BaseManagerImpl<MailSettingDao, MailSetting> implements MailSettingManager{
	@Resource
	MailManager mailManager;
	@Resource
	IUserService userService;

	@Override
	public void testConnection(String setId) throws Exception {
		MailSetting mailSetting = this.get(setId);
		if(BeanUtils.isEmpty(mailSetting)) {
			throw new NotFoundException("未找到对应的邮箱账号");
		}
		MailSetting seting = getBymailSetting(mailSetting);
		test(seting);
	}

	@Override
	public void testConnection(MailSetting mailSetting, boolean isOriginPwd) throws Exception {
		String id = mailSetting.getId();
		if(isOriginPwd || StringUtil.isEmpty(id)) {
			String password = mailSetting.getPassword();
			mailSetting.setPassword(EncryptUtil.encrypt(password));
		}
		MailSetting seting = getBymailSetting(mailSetting);
		test(seting);
	}
	
	private void test(MailSetting mailSetting) throws Exception {
		String mailType = mailSetting.getMailType();
		if(MailSetting.EXCHANGE_MAIL_TYPE.equals(mailType)){
			String sendPort = mailSetting.getSendPort();
			ExchangeMailUtil ma = new ExchangeMailUtil(sendPort, mailSetting.getMailAddress(), mailSetting.getPassword());
			ma.receive(1);
		}
		else {
			MailUtil mailUtil = new MailUtil(mailSetting);
			mailUtil.connectSmtpAndReceiver();
		}
	}
	
	/**
	 * 根据mailSetting对象，得到MailSeting对象<br/>
	 * 注：mailSetting对象中的密码字段应为使用EncryptUtil.encrypt方法加密后的密文
	 * @param mailSetting
	 * @return
	 * @throws Exception
	 */           
	public MailSetting getBymailSetting(MailSetting mailSetting) throws Exception{
		MailSetting seting = new MailSetting();
		String protocal = mailSetting.getMailType();
		seting.setSendHost(mailSetting.getSmtpHost());
		seting.setSendPort(mailSetting.getSmtpPort());
		seting.setProtocal(protocal);
		seting.setMailAddress(mailSetting.getMailAddress());
		seting.setPassword(EncryptUtil.decrypt(mailSetting.getPassword()));
		seting.setNickName(mailSetting.getNickName());
		seting.setSSL(mailSetting.getSSL());
		seting.setValidate(mailSetting.getValidate());
		seting.setIsDeleteRemote(mailSetting.getIsDeleteRemote());
		seting.setIsHandleAttach(mailSetting.getIsHandleAttach());
		seting.setMailType(mailSetting.getMailType());
		if("pop3".equals(protocal)){
			seting.setReceiveHost(mailSetting.getPopHost());
			seting.setReceivePort(mailSetting.getPopPort());
		}else{
			seting.setReceiveHost(mailSetting.getImapHost());
			seting.setReceivePort(mailSetting.getImapPort());
		}
		return seting ;
	}
	
	/**
	 * 设置默认邮箱
	 * @param mailSetting
	 * @throws Exception
	 */
	public void setDefault(MailSetting mailSetting, String currentUserId)throws Exception{
		MailSetting mail=baseMapper.getByIsDefault(currentUserId);
		if (BeanUtils.isNotEmpty(mail)) {
			mail.setIsDefault((short) 0);
			baseMapper.updateDefault(mail);
		}
		baseMapper.updateDefault(mailSetting);
	}
	
	/**
	 * 验证设置的邮箱地址的唯一性
	 * @param mailSetting
	 * @return
	 * @throws Exception
	 */
	public boolean isExistMail(MailSetting mailSetting)throws Exception{
		String id = mailSetting.getId();
		String address = mailSetting.getMailAddress();
		if(StringUtil.isNotEmpty(id)) {
			return false;
		}
		else {
			int result = baseMapper.getCountByAddress(address);
			return result > 0;
		}
	}
	
	/**
	 * 根据邮箱地址返回相应的邮箱配置实体
	 * @param address
	 * @return
	 */
	public MailSetting getMailByAddress(String address) {
		return baseMapper.getMailByAddress(address);
	}
	
	/**
	 * 获取用户的默认邮箱
	 * @param userId
	 * @return
	 */
	public MailSetting getByIsDefault(String userId) {
		return baseMapper.getByIsDefault(userId);
	}	
	
	/**
	 * 获取当前用户的邮箱列表
	 * @param userId
	 * @return
	 */
	public List<MailSetting> getMailByUserId(String userId) {
		return baseMapper.getMailByUserId(userId);
	}
	
	/**
	 * 获取当前用户的邮箱分页列表
	 * @param queryFilter
	 * @return
	 */
	public List<MailSetting> getAllByUserId(QueryFilter queryFilter) {
		Map<String, Object> params = queryFilter.getParams();
		return baseMapper.getAllByUserId(params);
	}

	/**
	 * 获取用户的邮件数
	 * @param userId
	 * @return
	 */
	public int getCountByUserId(String userId) {
		return baseMapper.getCountByUserId(userId);
	}

	public void delAllByIds(String[] lAryId) {
		for(String setId:lAryId){
			remove(setId);
			mailManager.remove(setId);
		}
		
	}

	@Override
	public void updateLastEnvelop(String id, String messageId, LocalDateTime lastReceiveTime) {
		baseMapper.updateLastEnvelop(id, messageId, lastReceiveTime);
	}

	@Override
	public void saveSetting(MailSetting mailSetting, boolean isOriginPwd, String userAccount) throws Exception {
		if(isExistMail( mailSetting)){
			throw new ServerRejectException("该邮箱地址已经设置过，不能重复设置");
		}
		String id= mailSetting.getId();
		// 新增
		if(StringUtil.isEmpty(id)){
			mailSetting.setId(UniqueIdUtil.getSuid());
			String userId = ContextUtil.getCurrentUserId();
			mailSetting.setUserId(userId);
			//设置密码加密
			mailSetting.setProtocal(mailSetting.getMailType());
			mailSetting.setSendHost(mailSetting.getSmtpHost());
			mailSetting.setSendPort(mailSetting.getSmtpPort());
			if(mailSetting.getMailType().equals("pop3")){
				mailSetting.setReceiveHost(mailSetting.getPopHost());
				mailSetting.setReceivePort(mailSetting.getPopPort());
			}else{
				mailSetting.setReceiveHost(mailSetting.getImapHost());
				mailSetting.setReceivePort(mailSetting.getImapPort());
			}
			mailSetting.setPassword(EncryptUtil.encrypt(mailSetting.getPassword()));
			int count = getCountByUserId(userId);
			if(count==0){
				mailSetting.setIsDefault((short)1);
			}else{
				mailSetting.setIsDefault((short)0);
			}
			create(mailSetting);
		}
		else{
			String mailPass = mailSetting.getPassword();
			//如果是原始密码，需要设置密码加密
			if(isOriginPwd) {
				mailSetting.setPassword(EncryptUtil.encrypt(mailPass));
			}
			update(mailSetting);
		}
	}
}