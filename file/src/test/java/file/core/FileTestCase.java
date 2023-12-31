/**
 * 
 */
package file.core;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.id.IdGenerator;
import com.hotent.file.Application;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
@Transactional
public class FileTestCase {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	protected IdGenerator idGenerator;
	
	@Test
	public void Test(){
		logger.debug("X7 file test initialize.");
	}
}
  