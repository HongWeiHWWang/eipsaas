package com.hotent.bpmModel.core.persistence.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.JsonUtil;
import com.hotent.bpmModel.core.BpmModelTestCase;
import com.hotent.bpmModel.manager.BpmDefTransform;

/**
 * 流程导入导出manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月25日
 */
public class BpmDefTransformTest extends BpmModelTestCase{
	@Resource
	BpmDefTransform bpmDefTransform;
	
	@Test
	public void testCurd() throws Exception{
		//List<String> defList = new ArrayList<String>();
		//defList.add("10000000000392");
		//defList.add("10000000090345");
		////流程导出接口
		//Map<String,String> map = bpmDefTransform.exportDef(defList);
		//System.out.println(JsonUtil.toJson(map));
		//
		////导入流程
		//String path = "C:"+File.separator+"Users"+File.separator+"win 10"+File.separator+"Downloads"+File.separator+"ht_flow_2018_0625_1137.zip";
		//bpmDefTransform.importDef(path);
		
		
	}
	
}
