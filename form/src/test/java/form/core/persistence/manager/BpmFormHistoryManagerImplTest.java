/**
 * 
 */
package form.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.form.model.FormHistory;
import com.hotent.form.persistence.manager.FormHistoryManager;

import form.core.FormTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月12日
 */
public class BpmFormHistoryManagerImplTest extends FormTestCase{
	
	@Resource
	FormHistoryManager bpmFormHistoryManager;
	@Test
	public void testCurd() throws Exception{
		FormHistory bfs=new FormHistory();
		bfs.setId("10000000020045");
		bfs.setFormId("10000000020044");
		bfs.setName("测试");
		String str="validate=\"{&quot;required&quot;:false,&quot;maxLength&quot;:50}\"/></td></tr></tbody></table>";
		bfs.setFormHtml(str);
		bfs.setCreateBy("1");
		bfs.setCreateTime(LocalDateTime.now());
		bpmFormHistoryManager.create(bfs);
		assertEquals("10000000020045", bpmFormHistoryManager.get("10000000020045").getId());
		bfs.setDesc("测试描述");
		bpmFormHistoryManager.update(bfs);
		assertEquals("测试描述", bpmFormHistoryManager.get("10000000020045").getDesc());
		bpmFormHistoryManager.remove("10000000020045");
		assertEquals(null, bpmFormHistoryManager.get("10000000020045"));
	}
}
