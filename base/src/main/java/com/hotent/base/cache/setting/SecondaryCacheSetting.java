package com.hotent.base.cache.setting;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 二级缓存配置项
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
public class SecondaryCacheSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
     * 缓存有效时间
     */
    private int expiration = 0;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     */
    private int preloadTime = 0;

    /**
     * 时间单位 {@link TimeUnit}
     */
    private TimeUnit timeUnit = TimeUnit.HOURS;

    /**
     * 是否强制刷新（走数据库），默认是false
     */
    private boolean forceRefresh = false;

    /**
     * 是否使用缓存名称作为 redis key 前缀
     */
    private boolean usePrefix = true;

    /**
     * 是否允许存NULL值
     */
    boolean allowNullValue = false;

    /**
     * 非空值和null值之间的时间倍率，默认是1。allowNullValue=true才有效
     * <p>
     * 如配置缓存的有效时间是200秒，倍率这设置成10，
     * 那么当缓存value为null时，缓存的有效时间将是20秒，非空时为200秒
     * </p>
     */
    int magnification = 1;

    public SecondaryCacheSetting() {
    }

    /**
     * @param expiration      缓存有效时间
     * @param preloadTime     缓存刷新时间
     * @param timeUnit        时间单位 {@link TimeUnit}
     * @param forceRefresh    是否强制刷新
     * @param allowNullValues 是否允许存NULL值，模式允许
     * @param magnification   非空值和null值之间的时间倍率
     */
    public SecondaryCacheSetting(int expiration, int preloadTime, TimeUnit timeUnit, boolean forceRefresh,
                                 boolean allowNullValues, int magnification) {
        this.expiration = expiration;
        this.preloadTime = preloadTime;
        this.timeUnit = timeUnit;
        this.forceRefresh = forceRefresh;
        this.allowNullValue = allowNullValues;
        this.magnification = magnification;
        this.usePrefix = true;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public long getPreloadTime() {
        return preloadTime;
    }

    public void setPreloadTime(int preloadTime) {
        this.preloadTime = preloadTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    public boolean isUsePrefix() {
        return usePrefix;
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public boolean isAllowNullValue() {
        return allowNullValue;
    }

    public void setAllowNullValue(boolean allowNullValue) {
        this.allowNullValue = allowNullValue;
    }

    public int getMagnification() {
        return magnification;
    }

    public void setMagnification(int magnification) {
        this.magnification = magnification;
    }
}
