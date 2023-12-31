package com.hotent.base.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.id.MybatisPlusIdGenerator;
import com.hotent.base.interceptor.DataPermissionInterceptor;
import com.hotent.base.interceptor.MasterSlaveAutoRoutingPlugin;

/**
 * mybatis配置类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月30日
 */
@Configuration
public class MybatisPlusConfig {
    @Autowired
    MultiTenantHandler myTenantHandler;
    @Autowired
    SaaSConfig saaSConfig;

    /**
     * 数据权限拦截器
     * @return
     */
    @Bean
	public Interceptor getInterceptor(){
		return new DataPermissionInterceptor();
	}

    /**
     * 自定义ID生成器
     * @return
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
    	return new MybatisPlusIdGenerator();
    }

    /**
     * 多租户拦截器
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 未开启多租户模式
        if(!saaSConfig.isEnable()) {
        	return paginationInterceptor;
        }
        myTenantHandler.setTenantId(saaSConfig.getTenantId());
        List<String> ignoreTables = saaSConfig.getIgnoreTables();
        ignoreTables.addAll(TenantConstant.IGNORE_TABLES);
        myTenantHandler.setIgnoreTableNames(ignoreTables);
        // SQL解析处理拦截：增加租户处理回调。
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());
        // 多租户拦截
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(myTenantHandler);
        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    /**
     * 读写分离的主从数据库
     * @return
     */
    @Bean
    public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin(){
        return new MasterSlaveAutoRoutingPlugin();
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    
    /**
     * 方言类型识别器
     * @return
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
    	VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        // 在mapper中标注databaseId="mysql"则表示该sql仅支持MySQL数据库
        properties.put("Oracle","oracle");
        properties.put("MySQL","mysql");
        properties.put("SQLServer","sqlserver");
        properties.put("PostgreSQL","pg");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
