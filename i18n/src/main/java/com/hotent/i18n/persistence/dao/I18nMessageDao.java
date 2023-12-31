package com.hotent.i18n.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.i18n.persistence.model.I18nMessage;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 描述：国际化资源 DAO接口
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
public interface I18nMessageDao extends BaseMapper<I18nMessage> {

    /**
     * 获取国际化资源列表数据
     *
     * @param params
     * @return
     */
    List<Map<String, String>> getList_mysql(IPage<Map<String, String>> page,@Param("params") Map<String, Object> params);

    /**
     * 获取国际化资源列表数据
     *
     * @param params
     * @return
     */
    List<Map<String, String>> getList_oracle(IPage<Map<String, String>> page,@Param("params") Map<String, Object> params);

    /**
     * 根据key获取国际化资源（各种类型的查询集合，不是单纯的单条记录）
     *
     * @param params
     * @return
     */
    Map<String, Object> getByMesKey_mysql(Map<String, Object> params);

    /**
     * 根据key获取国际化资源（各种类型的查询集合，不是单纯的单条记录）
     *
     * @param params
     * @return
     */
    Map<String, Object> getByMesKey_oracle(Map<String, Object> params);

    /**
     * 根据国际化资源key删除资源
     *
     * @param key
     */
    void delByKey(String key);

    /**
     * 根据key和type删除国际化资源
     *
     * @param params
     */
    void delByKeyAndType(Map<String, Object> params);

    /**
     * 根据资源key和类型查找对应的资源信息
     *
     * @param key
     * @param type
     * @return
     */
    I18nMessage getByKeyAndType(@Param("key") String key, @Param("type") String type);

    /**
     * 获取资源与语言类型的组合信息
     *
     * @return
     */
    List<Map<String, String>> getI18nInfo();

    /**
     * 国际化资源
     *
     * @param params
     * @return
     */
    List<Map<String, String>> searchList_mysql(Map<String, Object> params);

    /**
     * 国际化资源
     *
     * @param params
     * @return
     */
    List<Map<String, String>> searchList_oracle(Map<String, Object> params);
}
