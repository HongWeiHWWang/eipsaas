/**
 *
 */
package file.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.file.model.UploadResult;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.model.IUser;

import file.core.FileTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月14日
 */
public class FileManagerTest extends FileTestCase{
	@Resource
	FileManager fileManager;

	@Test
	public void testCurd() {
		try {
			String fileFormates="txt,doc";
			DefaultFile file=new DefaultFile();
			List<MultipartFile> files=new ArrayList<MultipartFile>();
			IUser currentUser = new UserFacade();
			currentUser.setUserId("1");
			currentUser.setFullname("雷健");
			currentUser.setAccount("lj");
			File f = new File("D:\\x5\\js控制审批定义显示隐藏.doc");
			MultipartFile firstFile = new MockMultipartFile(
					"js控制审批定义显示隐藏.doc", //文件名
					"js控制审批定义显示隐藏.doc", //originalName 相当于上传文件在客户机上的文件名
					ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
					new FileInputStream(f) //文件流
			);
			files.add(firstFile);
			file.setCreateBy("1");
			file.setCreateOrgId("1");
			UploadResult result=fileManager.uploadFile(file, files, fileFormates, currentUser);
			String fileId=result.getFileId();
			System.out.println(fileId);
			boolean success=result.isSuccess();
			assertTrue(success);
			String testFileName="D:\\x5\\测试下载.doc";
			file=fileManager.downloadFile(fileId, new FileOutputStream(testFileName));
			assertEquals(fileId, file.getId());
			fileManager.delSysFileByIds(new String[]{fileId});
			assertEquals(null,fileManager.get(fileId));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
