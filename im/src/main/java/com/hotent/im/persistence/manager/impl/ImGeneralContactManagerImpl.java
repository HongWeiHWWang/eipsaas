package com.hotent.im.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.base.manager.impl.AbstractManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.im.persistence.dao.ImGeneralContactDao;
import com.hotent.im.persistence.manager.ImGeneralContactManager;
import com.hotent.im.persistence.model.ImGeneralContact;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：im_general_contact 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-03-23 10:00:00
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("imGeneralContactManager")
public class ImGeneralContactManagerImpl extends AbstractManagerImpl<String, ImGeneralContact> implements ImGeneralContactManager{
	@Resource
	ImGeneralContactDao imGeneralContactDao;
	
	@Override
	protected MyBatisDao<String, ImGeneralContact> getDao() {
		return imGeneralContactDao;
	}
	
	@Override
	public void createContact(IUser currentUser, IUser target) {
		ImGeneralContact  imGeneralContact = getByOwnerAndAccount(currentUser.getAccount(),target.getAccount());
		if(imGeneralContact == null){
			imGeneralContact = new ImGeneralContact();
			imGeneralContact.setId(UniqueIdUtil.getSuid());
			imGeneralContact.setOwner(currentUser.getAccount());
			imGeneralContact.setAccount(target.getAccount());
			imGeneralContact.setCreateTime(LocalDateTime.now());
			imGeneralContactDao.create(imGeneralContact);
		}		
	}
	
	public ImGeneralContact getByOwnerAndAccount(String owner,String account){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("owner", owner);
		param.put("account", account);
		List<ImGeneralContact> imGeneralContacts = imGeneralContactDao.getByOwnerAndAccount(param);
		if(imGeneralContacts.size() > 0){
			return imGeneralContacts.get(0);
		}
		return null;
	}
	
	@Override
	public List<ImGeneralContact> getGeneralContactInfo(QueryFilter queryFilter) {
		return imGeneralContactDao.getGeneralContactInfo(queryFilter.getParams());
	}
}
