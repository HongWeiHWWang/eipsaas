package com.hotent.base.cache.impl;

import java.util.concurrent.Callable;

import org.springframework.util.Assert;

import com.hotent.base.cache.ICache;
import com.hotent.base.cache.support.NullValue;
import com.hotent.base.util.JsonUtil;


/**
 * Cache 接口的抽象实现类，对公共的方法做了一些实现，如是否允许存NULL值
 * <p>如果允许为NULL值，则需要在内部将NULL替换成{@link NullValue#INSTANCE} 对象</p>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月15日
 */
public abstract class AbstractValueAdaptingCache implements ICache {
    /**
     * 缓存名称
     */
    private final String name;

    /**
     * 通过构造方法设置缓存配置
     *
     * @param stats           是否开启监控统计
     * @param name            缓存名称
     */
    protected AbstractValueAdaptingCache(String name) {
        Assert.notNull(name, "缓存名称不能为NULL");
        this.name = name;
    }

    /**
     * 获取是否允许存NULL值
     *
     * @return true:允许，false:不允许
     */
    public abstract boolean isAllowNullValues();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        return (T) fromStoreValue(get(key));
    }

    /**
     * Convert the given value from the internal store to a user value
     * returned from the get method (adapting {@code null}).
     *
     * @param storeValue the store value
     * @return the value to return to the user
     */
    protected Object fromStoreValue(Object storeValue) {
        if (isAllowNullValues() && storeValue instanceof NullValue) {
            return null;
        }
        return storeValue;
    }

    /**
     * Convert the given user value, as passed into the put method,
     * to a value in the internal store (adapting {@code null}).
     *
     * @param userValue the given user value
     * @return the value to store
     */
    protected Object toStoreValue(Object userValue) {
        if (isAllowNullValues() && userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

    /**
     * {@link #get(Object, Callable)} 方法加载缓存值的包装异常
     */
    public class LoaderCacheValueException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		private final Object key;

        public LoaderCacheValueException(Object key, Throwable ex) {
            super(String.format("加载key为 %s 的缓存数据,执行被缓存方法异常", toJsonString(key)), ex);
            this.key = key;
        }

        public Object getKey() {
            return this.key;
        }
    }
    
    /**
     * 将对象转为字符串
     * @param obj
     * @return
     */
    protected String toJsonString(Object obj) {
    	return JsonUtil.toJsonString(obj);
    }
}
