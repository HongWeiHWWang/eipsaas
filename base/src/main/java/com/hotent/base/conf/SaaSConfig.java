package com.hotent.base.conf;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 多租户的配置属性
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
@Component
@ConfigurationProperties(prefix = "system.saas")
public class SaaSConfig {
	/**
	 * 是否开启多租户
	 * <pre>
	 * 未开启期间产生的所有数据不会记录租户ID，开启多租户后这部分数据不会归集到任何租户下。
	 * </pre>
	 */
	private boolean enable = false;
	/**
	 * 多租户字段名
	 */
	private String tenantId = "tenant_id_";
	
	/**
	 * 忽略多租户的表名
	 * <pre>
	 * 数据库中物理表表名
	 * </pre>
	 */
	private List<String> ignoreTables = new ArrayList<>();
	
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public List<String> getIgnoreTables() {
		return ignoreTables;
	}
	public void setIgnoreTables(List<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}
}
