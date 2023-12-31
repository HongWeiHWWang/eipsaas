package com.hotent.im.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.Manager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.im.persistence.model.ImGeneralContact;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：im_general_contact 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-03-23 10:00:00
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface ImGeneralContactManager extends Manager<String, ImGeneralContact>{

	void createContact(IUser currentUser, IUser target);

	List<ImGeneralContact> getGeneralContactInfo(QueryFilter queryFilter);
	
}
