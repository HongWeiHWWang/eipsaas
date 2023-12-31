package com.hotent.file.service.impl;




import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.attachment.AttachmentServiceFactory;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.AppFileUtil;
import com.hotent.file.util.FileUtils;
import com.hotent.file.util.OfficeToPdf;

/**
 * Created by kl on 2018/1/17.
 * Content :处理office文件
 */
@SuppressWarnings(value = "unchecked")
@Service
public class OfficeFilePreviewImpl implements FilePreview {

	@Autowired
	FileUtils fileUtils;

	@Value("${file.file.dir}")
	String fileDir;
	/*@Value("${imageMagick}")
	String imageMagick;*/
	@Resource
	AttachmentServiceFactory attachmentServiceFactory;
	@Autowired
	private OfficeToPdf officeToPdf;


	@Override
	public String filePreviewHandle(DefaultFile fileMode,@SuppressWarnings("rawtypes") Map map) {
		String fileName=fileMode.getId()+"."+fileMode.getExtensionName();
		String suffix=fileMode.getExtensionName();
		boolean isHtml = suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx");
		String filePath = fileDir+fileMode.getId()+"."+fileMode.getExtensionName();
		String checkfilePath=filePath.substring(0,filePath.lastIndexOf(".")+1)+(isHtml ? "html" : "pdf");
		//判断该文件是否已经转过PDF
		boolean falg=checkFile(checkfilePath);
		if(!falg){
			if (!new File(filePath).exists()) {
				try {
					File dirFile = new File(fileDir);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					AppFileUtil.createPath(filePath.substring(0,filePath.lastIndexOf(File.separator)), fileName);
					AttachmentService attachmentService=attachmentServiceFactory.getCurrentServices(AppFileUtil.getSaveType());
					attachmentService.download(fileMode, new FileOutputStream(filePath));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			String outFilePath = filePath.substring(0,filePath.lastIndexOf(".")+1)+(isHtml ? "html" : "pdf");
			if (StringUtils.hasText(outFilePath)) {
				officeToPdf.openOfficeToPDF(filePath, outFilePath);
				File f = new File(filePath);
				if (f.exists()) {
					f.delete();
				}
			}
		}
		map.put("pdfUrl","/file/onlinePreviewController/v1/getFileByPathAndId_"+fileMode.getId()+"_"+(isHtml ? "html" : "pdf"));
		return isHtml ? "html" : "pdf";
		/*
		map.put("imgUrls", list);
		map.put("currentUrl","//word/OnlinePreviewController/getFileById_"+fileMode.getId()+"-0"+"_png");
		return "picture";*/
	}

	private boolean checkFile(String path){
		File file=new File(path);
		return file.exists();
	}

}
