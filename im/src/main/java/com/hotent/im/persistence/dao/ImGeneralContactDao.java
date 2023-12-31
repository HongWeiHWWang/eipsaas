package com.hotent.im.persistence.dao;
import java.util.List;
import java.util.Map;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.im.persistence.model.ImGeneralContact;

/**
 * 
 * <pre> 
 * 描述：im_general_contact DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-03-23 10:00:00
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface ImGeneralContactDao extends MyBatisDao<String, ImGeneralContact> {

	List<ImGeneralContact> getGeneralContactInfo(Map<String, Object> params);

	List<ImGeneralContact> getByOwnerAndAccount(Map<String, Object> params);
}
