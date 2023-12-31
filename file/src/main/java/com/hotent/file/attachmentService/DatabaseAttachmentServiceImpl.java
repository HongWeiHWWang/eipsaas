package com.hotent.file.attachmentService;

import com.hotent.base.attachment.Attachment;
import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.util.FileUtil;
import com.hotent.file.util.AppFileUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class DatabaseAttachmentServiceImpl implements AttachmentService{
	public void remove(Attachment sysFile) throws Exception {
		// 数据库存放附件模式，直接删除数据库中的记录即可
	}

	public void upload(Attachment sysFile, InputStream inputStream) throws Exception {
		// 上传附件时，将附件字节流设置到SysFile的FileBlob属性中
		sysFile.setBytes(FileUtil.readByte(inputStream));
	}

	public void download(Attachment sysFile, OutputStream outStream) throws Exception {
		//获取附件字节数组
		if(Attachment.SAVE_TYPE_FOLDER.equals(sysFile.getStoreType())){
			/* 根据文件路径获取文件 */
			File file = new File(AppFileUtil.getAttachPath()+File.separator+sysFile.getFilePath());
			if (file.exists()) { // 文件存在
				/* 根据已存在的文件，创建文件输入流 */
				try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));){
					/* 创建缓冲区，大小为流的最大字符数 */
					byte[] buffer = new byte[inputStream.available()]; // int available() 返回值为流中尚未读取的字节的数量
					/* 从文件输入流读字节流到缓冲区 */
					inputStream.read(buffer);
	                /* 关闭输入流 */
					outStream.write(buffer);
					/* 刷空输出流，并输出所有被缓存的字节 */
					outStream.flush();
				} catch (Exception e) {

				} finally {
					if (outStream != null) {
						outStream.close();
					}
				}
			}
		}else if(Attachment.SAVE_TYPE_FTP.equals(sysFile.getStoreType())) {

		}else{
			byte[] fileBlob = sysFile.getBytes();
			try{
				outStream.write(fileBlob);
				outStream.flush();
			}
			catch(Exception e){
				throw e;
			}
			finally{
				if (outStream != null) {
					outStream.close();
				}
			}
		}
	}

	
	@Override
	public String getStoreType() {
		return "database";
	}


	@Override
	public boolean chekckFile(Attachment attachment) {
		return true;
	}
}
