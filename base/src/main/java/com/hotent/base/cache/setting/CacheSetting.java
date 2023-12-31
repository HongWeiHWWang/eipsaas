package com.hotent.base.cache.setting;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 多级缓存配置项
 * <p>
 * 一般采用两级缓存：
 * 一级缓存采用 Caffeine，仅支持单服务部署；
 * 二级缓存采用 redis，可支持分布式部署。
 * </p>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
public class CacheSetting implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String SPLIT = "-";
    /**
     * 内部缓存名，由[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]组成
     */
    private String internalKey;

    /**
     * 描述
     */
    private String depict;

    /**
     * 是否使用二级缓存
     */
    boolean useSecondCache = true;

    /**
     * 一级缓存配置
     */
    private FirstCacheSetting firstCacheSetting;

    /**
     * 二级缓存配置
     */
    private SecondaryCacheSetting secondaryCacheSetting;
    
    /**
     * 构建默认缓存设置
     * <p>
     * 默认只使用一级缓存，缓存过期时间为14Hours
     * </p>
     * @param depict
     * @return
     */
    public static CacheSetting buildDefault(String depict) {
    	CacheSetting cacheSetting = new CacheSetting();
    	cacheSetting.setDepict(depict);
    	cacheSetting.setUseSecondCache(false);
    	cacheSetting.withDefaultFirstSetting()
    				.withDefaultSecondaryCacheSetting();
    	cacheSetting.internalKey();
    	return cacheSetting;
    }
    
    private CacheSetting withDefaultFirstSetting() {
    	FirstCacheSetting fcs = new FirstCacheSetting();
    	fcs.setExpireTime(7);
    	fcs.setTimeUnit(TimeUnit.HOURS);
    	this.setFirstCacheSetting(fcs);
    	return this;
    }
    
    private CacheSetting withDefaultSecondaryCacheSetting() {
    	SecondaryCacheSetting scs = new SecondaryCacheSetting();
    	scs.setExpiration(24);
    	scs.setTimeUnit(TimeUnit.HOURS);
    	scs.setPreloadTime(5);
    	this.setSecondaryCacheSetting(scs);
    	return this;
    }

    private CacheSetting() {
    }

    public CacheSetting(FirstCacheSetting firstCacheSetting, SecondaryCacheSetting secondaryCacheSetting, String depict) {
        this.firstCacheSetting = firstCacheSetting;
        this.secondaryCacheSetting = secondaryCacheSetting;
        this.depict = depict;
        internalKey();
    }

    @JsonIgnore
    private void internalKey() {
        // 一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间
        StringBuilder sb = new StringBuilder();
        if (firstCacheSetting != null) {
            sb.append(firstCacheSetting.getTimeUnit().toMillis(firstCacheSetting.getExpireTime()));
        }
        if (isUseSecondCache() && secondaryCacheSetting != null) {
        	sb.append(SPLIT);
            sb.append(secondaryCacheSetting.getTimeUnit().toMillis(secondaryCacheSetting.getExpiration()));
            sb.append(SPLIT);
            sb.append(secondaryCacheSetting.getTimeUnit().toMillis(secondaryCacheSetting.getPreloadTime()));
        }
        internalKey = sb.toString();
    }

    public FirstCacheSetting getFirstCacheSetting() {
        return firstCacheSetting;
    }

    public SecondaryCacheSetting getSecondaryCacheSetting() {
        return secondaryCacheSetting;
    }

    public String getInternalKey() {
        return internalKey;
    }

    public void internalKey(String internalKey) {
        this.internalKey = internalKey;
    }


    public boolean isUseSecondCache() {
		return useSecondCache;
	}

	public void setUseSecondCache(boolean useSecondCache) {
		this.useSecondCache = useSecondCache;
	}

	public void setFirstCacheSetting(FirstCacheSetting firstCacheSetting) {
        this.firstCacheSetting = firstCacheSetting;
    }

    public void setSecondaryCacheSetting(SecondaryCacheSetting secondaryCacheSetting) {
        this.secondaryCacheSetting = secondaryCacheSetting;
    }

    public void setInternalKey(String internalKey) {
        this.internalKey = internalKey;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }
}
