package com.hotent.table;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.hotent.base.id.IdGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
@Transactional
public class TableBaseTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	protected IdGenerator idGenerator;
	
	@Test
	public void Test(){
		logger.debug("X7 table test initialize.");
	}
}
