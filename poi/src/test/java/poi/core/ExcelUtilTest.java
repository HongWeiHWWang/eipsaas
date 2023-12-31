/**
 * 
 */
package poi.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import poi.util.ExcelUtil;


/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月20日
 */
public class ExcelUtilTest{
	
	
	@Test
	public void testCurd() throws Exception {
		Map<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("A1", "标题1");
		fieldMap.put("A2", "标题2");
		fieldMap.put("A3", "标题3");
		fieldMap.put("A4", "标题4");
		fieldMap.put("A5", "标题5");
		fieldMap.put("A6", "标题6");
		List<Map<String, String>> data=new ArrayList<Map<String, String>>();
		Map<String, String> m1=new HashMap<String, String>();
		m1.put("A1", "LJ1");
		m1.put("A2", "LJ2");
		m1.put("A3", "LJ3");
		m1.put("A4", "LJ4");
		m1.put("A5", "LJ5");
		m1.put("A6", "LJ6");
		Map<String, String> m2=new HashMap<String, String>();
		m2.put("A1", "B1");
		m2.put("A2", "B2");
		m2.put("A3", "B3");
		m2.put("A4", "B4");
		m2.put("A5", "B5");
		m2.put("A6", "B6");
		
		Map<String, String> m3=new HashMap<String, String>();
		m3.put("A1", "C1");
		m3.put("A2", "C2");
		m3.put("A3", "C3");
		m3.put("A4", "C4");
		m3.put("A5", "C5");
		m3.put("A6", "C6");
		data.add(m1);
		data.add(m2);
		data.add(m3);
		HSSFWorkbook bk=ExcelUtil.exportExcel("测试导出", 15, fieldMap, data);
		OutputStream os=null;
		try {
			os=new FileOutputStream("F:\\测试导出.xls");
			bk.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null){
				os.close();
			}
		}
		
		
		
		File f = new File("F:\\测试导出.xls");
		MultipartFile firstFile = new MockMultipartFile(
				"测试导出.xls", //文件名
				"测试导出.xls", //originalName 相当于上传文件在客户机上的文件名
				ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
				new FileInputStream(f) //文件流
				);
		List<Map<String, String>> list= ExcelUtil.ImportDate(firstFile);
		
		
		for (Map<String,String> map : list) {
			for (Entry<String,String> entry : map.entrySet()) {
				System.out.print(entry.getKey()+":"+entry.getValue()+",");
			}
			System.out.println();
		}
		assertEquals(list.size(),data.size());
	}
	
}
