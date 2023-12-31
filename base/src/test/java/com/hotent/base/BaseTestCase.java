package com.hotent.base;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
@Transactional
public class BaseTestCase {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Before
	public void init(){
		logger.debug("开始测试...");
	}
	
	@After
	public void after() {
		logger.debug("测试结束...");
	}
}
