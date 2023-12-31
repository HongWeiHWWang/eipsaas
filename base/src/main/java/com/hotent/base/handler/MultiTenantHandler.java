package com.hotent.base.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.conf.SaaSConfig;
import com.hotent.base.constants.SQLConst;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.context.BaseContext;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

import io.jsonwebtoken.lang.Assert;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;

/**
 * 多租户处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月6日
 */
@Component
public class MultiTenantHandler implements TenantHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 多租户标识
     */
    private String tenantIdColumn = "tenant_id_";

    /**
     * 需要过滤的表
     */
    private List<String> ignoreTableNames = new ArrayList<>();

    @Autowired
    private BaseContext apiContext;
    @Resource
    SaaSConfig saaSConfig;
    
    private static ThreadLocal<Boolean> threadLocalIgnore = new ThreadLocal<Boolean>();
    
    /**
     * 线程变量 临时忽略 自动添加租户id的操作
     */
    public static MultiTenantIgnoreResult setThreadLocalIgnore() {
    	threadLocalIgnore.set(true);
    	return new MultiTenantIgnoreResult();
    }
    
    public static Boolean getThreadLocalIgnore() {
    	Boolean ignoreTable = threadLocalIgnore.get();
    	if(BeanUtils.isNotEmpty(ignoreTable) && ignoreTable) {
    		return true;
    	}
    	return false;
    }
    
    public static void removeThreadLocalIgnore() {
    	threadLocalIgnore.remove();
    }
    
    /**	
     * 租户Id
     *
     * @return
     */
    @Override
    public Expression getTenantId(boolean where) {
        // 从当前系统上下文中取出当前请求的服务商ID，通过解析器注入到SQL中。
        String tenantId = apiContext.getCurrentTenantId();
        log.debug("当前租户为{}", tenantId);
        if (tenantId == null) {
            return new NullValue();
        }
        return new StringValue(tenantId);
    }
    
    /**
     * 获取当前用户所属租户的租户CODE
     * <pre>
     * 如果当前是平台管理用户则返回null
     * </pre>
     * @return
     */
    public String getTenantCode() {
    	// 租户模式下生成物理表时需要在表名中追加租户别名
		if(saaSConfig.isEnable()) {
			String currentTenantId = apiContext.getCurrentTenantId();
			// 非平台管理用户
			if(!TenantConstant.PLATFORM_TENANT_ID.equals(currentTenantId)) {
				UCFeignService ucFeign = AppUtil.getBean(UCFeignService.class);
				JsonNode tenantManage = ucFeign.getTenantById(currentTenantId);
				Assert.notNull(tenantManage, "未获取到当前用户所属的租户信息");
				String tenantCode = JsonUtil.getString((ObjectNode)tenantManage, "code");
				Assert.isTrue(StringUtil.isNotEmpty(tenantCode), "租户中的租户别名为空");
				return tenantCode;
			}
		}
		return null;
    }
    
    /**
     * 设置租户字段名
     * @param idColumn
     */
    public void setTenantId(String tenantId) {
    	this.tenantIdColumn = tenantId;
    }
    
    /**
     * 设置忽略的表名
     * @param list
     */
    public void setIgnoreTableNames(List<String> list) {
    	this.ignoreTableNames = list;
    }
    
    /**
     * 获取忽略的表名列表
     * @return
     */
    public List<String> getIgnoreTableNames() {
    	return this.ignoreTableNames;
    }

    /**
     * 租户字段名
     *
     * @return
     */
    @Override
    public String getTenantIdColumn() {
        return tenantIdColumn;
    }

    /**
     * 根据表名判断是否进行过滤
     * 忽略掉一些表：如租户表（sys_tenant）本身不需要执行这样的处理
     *
     * @param tableName
     * @return
     */
    @Override
    public boolean doTableFilter(String tableName) {
    	if(getThreadLocalIgnore()) {
    		return true;
    	}
    	if(StringUtil.isNotEmpty(tableName) && tableName.toUpperCase().startsWith(SQLConst.CUSTOMER_TABLE_PREFIX)){
            return true;
        }
        return ignoreTableNames.stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
    }
}