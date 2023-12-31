package com.hotent.activemq.jms;

import com.hotent.activemq.Application;
import com.hotent.base.id.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 17:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
@Transactional
public class JmsTestCase {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    protected IdGenerator idGenerator;

    @Test
    public void Test(){
        logger.debug("X7 jms test initialize.");
    }
}
