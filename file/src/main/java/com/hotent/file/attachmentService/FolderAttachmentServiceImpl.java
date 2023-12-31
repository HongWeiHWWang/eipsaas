package com.hotent.file.attachmentService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.hotent.base.attachment.Attachment;
import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.file.util.AppFileUtil;

@Service
public class FolderAttachmentServiceImpl implements AttachmentService{
	//当前存放附件的路径
	//TODO
	
	
	public FolderAttachmentServiceImpl(){
		
	}

	public void remove(Attachment attachment) throws Exception {
		String filePath = attachment.getFilePath();
		String fullPath = AppFileUtil.getAttachPath() + File.separator + filePath;
		// 删除文件
		FileUtil.deleteFile(fullPath);
	}

	@Override
	public void upload(Attachment attachment, InputStream inputStream) throws Exception {
		String filePath = attachment.getFilePath();
		filePath=AppFileUtil.getAttachPath()+File.separator+filePath;
		if(BeanUtils.isNotEmpty(inputStream)) {
			FileUtil.createFolderFile(filePath);
			FileUtil.writeFile(filePath, inputStream);
		}
		else {
			FileUtil.writeByte(filePath, attachment.getBytes());
		}
	}

	public void download(Attachment attachment, OutputStream outStream)
			throws Exception {
		String filePath = attachment.getFilePath();
		String fullPath = StringUtil.trimSufffix(AppFileUtil.getAttachPath(), File.separator) + File.separator 
												+ filePath.replace("/", File.separator);
		File file = new File(fullPath);
		if (file.exists()) {
			FileInputStream inputStream = null;
			try{
				inputStream = new FileInputStream(fullPath);
				byte[] b = new byte[1024];
				int i = 0;
				while ((i = inputStream.read(b)) > 0) {
					outStream.write(b, 0, i);
				}
				outStream.flush();
			}
			catch(Exception e){
				throw e;
			}
			finally{
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
				if (outStream != null) {
					outStream.close();
					outStream = null;
				}
			}
		}
		else {
			throw new RuntimeException("该附件不存在");
		}
	}


	@Override
	public String getStoreType() {
		return "folder";
	}

	@Override
	public boolean chekckFile(Attachment attachment) {
		String filePath = attachment.getFilePath();
		String fullPath = StringUtil.trimSufffix(AppFileUtil.getAttachPath(), File.separator) + File.separator
				+ filePath.replace("/", File.separator);
		File file = new File(fullPath);
		return file.exists();
	}
}
