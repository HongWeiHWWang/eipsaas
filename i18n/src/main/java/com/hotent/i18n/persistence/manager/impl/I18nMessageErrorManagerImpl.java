package com.hotent.i18n.persistence.manager.impl;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.i18n.persistence.dao.I18nMessageErrorDao;
import com.hotent.i18n.persistence.manager.I18nMessageErrorManager;
import com.hotent.i18n.persistence.model.I18nMessageError;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 
 * <pre> 
 * 描述：国际化资源异常日志 处理实现类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@Service("i18nMessageErrorManager")
public class I18nMessageErrorManagerImpl extends BaseManagerImpl<I18nMessageErrorDao, I18nMessageError> implements I18nMessageErrorManager{
	@Resource
	I18nMessageErrorDao i18nMessageErrorDao;

}
