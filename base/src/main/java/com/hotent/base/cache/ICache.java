package com.hotent.base.cache;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import org.springframework.cache.CacheManager;

/**
 * 缓存操作接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月1日
 */
public interface ICache {
	/**
     * 返回缓存名称
     * <p>
     * 要缓存的数据名称，例如：要缓存{@link com.hotent.uc.model.User}对象时，该Name属性可以为{@code user}。
     * </p>
     * @return String
     */
    String getName();

    /**
     * 返回真实Cache对象
     *
     * @return Object
     */
    Object getNativeCache();

    /**
     * 根据Key返回key对应的值，如果没有就返回NULL
     *
     * @param key key
     * @return 缓存key对应的值
     */
    Object get(Object key);
    
    /**
     * 根据Key数组及类型批量返回对应的缓存值
     * <p>该方法主要用于批量获取指定语种的国际化资源</p>
     * @param keys	key数组
     * @return		Map格式的缓存数据，Map中的可以与key数组中的key对应
     */
    Map<String, String> getAll(@Nonnull Iterable<String> keys, String type);

    /**
     * 根据KEY返回缓存中对应的值，并将其返回类型转换成对应类型，如果对应key不存在则返回NULL
     *
     * @param key  缓存key
     * @param type 返回值类型
     * @param <T>  Object
     * @return 缓存key对应的值
     */
    <T> T get(Object key, Class<T> type);

    /**
     * 根据KEY返回缓存中对应的值，并将其返回类型转换成对应类型，如果对应key不存在则调用valueLoader加载数据
     *
     * @param key         缓存key
     * @param valueLoader 加载缓存的回调方法
     * @param <T>         Object
     * @return 缓存key对应的值
     */
    <T> T get(Object key, Callable<T> valueLoader);

    /**
     * 将对应key-value放到缓存，如果key原来有值就直接覆盖
     *
     * @param key   缓存key
     * @param value 缓存的值
     */
    void put(Object key, Object value);
    
    /**
     * 将Map中的键值对批量放到缓存中
     * <p>该方法主要用于将多语种的国际化资源放入缓存</p>
     * @param map	缓存的Map数据
     */
    void putAll(@Nonnull Map<String, Map<String, String>> map);

    /**
     * 如果缓存key没有对应的值就将值put到缓存，如果有就直接返回原有的值
     * <p>就相当于:
     * <pre><code>
     * Object existingValue = cache.get(key);
     * if (existingValue == null) {
     *     cache.put(key, value);
     *     return null;
     * } else {
     *     return existingValue;
     * }
     * </code></pre>
     * except that the action is performed atomically. While all out-of-the-box
     * {@link CacheManager} implementations are able to perform the put atomically,
     * the operation may also be implemented in two steps, e.g. with a check for
     * presence and a subsequent put, in a non-atomic way. Check the documentation
     * of the native cache implementation that you are using for more details.
     *
     * @param key   缓存key
     * @param value 缓存key对应的值
     * @return 因为值本身可能为NULL，或者缓存key本来就没有对应值的时候也为NULL，
     * 所以如果返回NULL就表示已经将key-value键值对放到了缓存中
     * @since 4.1
     */
    Object putIfAbsent(Object key, Object value);

    /**
     * 在缓存中删除对应的key
     *
     * @param key 缓存key
     */
    void evict(Object key);
    
    /**
     * 清除指定类型的指定字段缓存
     * <p>该方法用于清除指定语种的指定字段国际化资源</p>
     * @param key
     * @param field
     */
    void hdel(String key, String field);
    
    /**
     * 通过key数组删除对应缓存
     * @param keys	key数组
     */
    void evictAll(@Nonnull Iterable<? extends Object> keys);

    /**
     * 清除缓存
     */
    void clear();
}