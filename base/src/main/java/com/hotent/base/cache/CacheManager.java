package com.hotent.base.cache;

import java.util.Collection;

import com.hotent.base.cache.setting.CacheSetting;

/**
 * 缓存管理器
 * 允许通过缓存名称来获的对应的 {@link ICache}.
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
public interface CacheManager {

    /**
     * 根据缓存名称返回对应的{@link Collection}.
     *
     * @param name 缓存的名称 (不能为 {@code null})
     * @return 返回对应名称的Cache, 如果没找到返回 {@code null}
     */
    Collection<ICache> getCache(String name);

    /**
     * 根据缓存名称返回对应的{@link ICache}，如果没有找到就新建一个并放到容器
     *
     * @param name                 缓存名称
     * @param cacheSetting 		        多级缓存配置
     * @return {@link ICache}
     */
    ICache getCache(String name, CacheSetting cacheSetting);

    /**
     * 获取所有缓存名称的集合
     *
     * @return 所有缓存名称的集合
     */
    Collection<String> getCacheNames();
}
