package com.hotent.base.cache.support;

/**
 * 缓存失效模式
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月15日
 */
public enum ExpireMode {
    /**
     * 每写入一次重新计算一次缓存的有效时间
     */
    WRITE("最后一次写入后到期失效"),

    /**
     * 每访问一次重新计算一次缓存的有效时间
     */
    ACCESS("最后一次访问后到期失效");

    private String label;

    ExpireMode(String label) {
    	this.label = label;
    }

	public String getLabel() {
		return label;
	}
}