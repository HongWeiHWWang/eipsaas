/**
 *
 */
package file.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.file.model.UploadResult;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.service.FilePreview;
import com.hotent.file.service.FilePreviewFactory;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.model.IUser;

import file.core.FileTestCase;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月14日
 */
public class FilePreviewTest extends FileTestCase{
	@Resource
	FilePreviewFactory previewFactory;
	@Resource
	FileManager fileManager;

	@Value("${file.file.dir}")
	String fileDir;

	@SuppressWarnings("rawtypes")
	@Test
	public void testCurd() throws Exception {
		String fileFormates="txt,docx";
		DefaultFile file=new DefaultFile();
		List<MultipartFile> files=new ArrayList <MultipartFile>();
		IUser currentUser = new UserFacade();
		currentUser.setUserId("1");
		currentUser.setFullname("雷健");
		currentUser.setAccount("lj");
		File f = new File("F:\\TS\\门户管理操作手册.docx");
		MultipartFile firstFile = new MockMultipartFile(
				"门户管理操作手册.docx", //文件名
				"门户管理操作手册.docx", //originalName 相当于上传文件在客户机上的文件名
				ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
				new FileInputStream(f) //文件流
				);
		files.add(firstFile);
		UploadResult result=fileManager.uploadFile(file, files, fileFormates, currentUser);
		String fileId=result.getFileId();
		file=fileManager.get(fileId);
		assertEquals(fileId,file.getId());
		FilePreview filePreview = previewFactory.get(file);
		Map map=new HashMap();
		//office 文件转PDF   //压缩文件解压缩
		filePreview.filePreviewHandle(file, map);
		fileDir=fileDir+file.getFilePath();
		fileDir=fileDir.substring(0,fileDir.lastIndexOf("."))+".pdf";
		File sysFile=new File(fileDir);
		assertTrue(sysFile.exists());
		fileManager.delSysFileByIds(new String[]{fileId});
		assertEquals(null,fileManager.get(fileId));
	}
}
