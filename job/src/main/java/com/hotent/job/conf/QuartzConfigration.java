package com.hotent.job.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月13日
 */
@Configuration
public class QuartzConfigration implements SchedulerFactoryBeanCustomizer{
	private DataSource dataSource;

    private PlatformTransactionManager transactionManager;

    @Autowired
    public QuartzConfigration(@Qualifier("dataSource") DataSource dataSource, 
    						  @Qualifier("transactionManager") PlatformTransactionManager transactionManager) 
    {
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(transactionManager);
        ClassPathResource resource = new ClassPathResource("quartz.properties");
        Properties properties = new Properties();
        InputStream in= null;
        try {
            in = resource.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            properties.load(bf);
            schedulerFactoryBean.setQuartzProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
