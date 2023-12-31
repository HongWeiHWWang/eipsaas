package com.hotent.i18n.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.i18n.persistence.model.I18nMessageType;

/**
 * <pre>
 * 描述：国际化资源支持的语言类型 DAO接口
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
public interface I18nMessageTypeDao extends BaseMapper<I18nMessageType> {
    /**
     * 根据type获取语言类型
     *
     * @param type
     * @return
     */
    I18nMessageType getByType(String type);
}
