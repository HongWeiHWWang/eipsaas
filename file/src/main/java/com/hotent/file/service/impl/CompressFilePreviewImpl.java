package com.hotent.file.service.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.attachment.AttachmentServiceFactory;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.AppFileUtil;
import com.hotent.file.util.FileUtils;
import com.hotent.file.util.ZipReader;

/**
 * Created by kl on 2018/1/17.
 * Content :处理压缩包文件
 */
@SuppressWarnings(value ={"unchecked","rawtypes"})
@Service
public class CompressFilePreviewImpl implements FilePreview{

	@Value("${file.file.dir}")
	String fileDir;

	@Autowired
	ZipReader zipReader;
	@Resource
	AttachmentServiceFactory attachmentServiceFactory;

	@Override
	public String filePreviewHandle(DefaultFile fileMode,Map map) {
		String fileName=fileMode.getId()+"."+fileMode.getExtensionName();
		String filePath = fileDir +fileMode.getFilePath();
		String suffix=fileMode.getExtensionName();
		String fileTree = null;
		boolean falg=true;
		String outPath = filePath.substring(0,filePath.lastIndexOf("."))+".json";
		File file=new File(outPath);
		if(file.exists()){
			StringBuilder result = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new FileReader(file));){
				String s = null;
				while((s = br.readLine())!=null){//使用readLine方法，一次读一行
					result.append(s);
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileTree=result.toString();
			falg=false;
		}
		// 判断文件名是否存已经解压缩
		if (falg) {
			try {
				File dirFile = new File(fileDir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				String str=filePath.substring(0, filePath.lastIndexOf(File.separator));
				AppFileUtil.createPath(str, fileName);
				AttachmentService attachmentService=attachmentServiceFactory.getCurrentServices(AppFileUtil.getSaveType());
				attachmentService.download(fileMode, new FileOutputStream(filePath));
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if ("zip".equalsIgnoreCase(suffix) || "jar".equalsIgnoreCase(suffix) || "gzip".equalsIgnoreCase(suffix)) {
				fileTree = zipReader.readZipFile(filePath, fileName);
			} else if ("rar".equalsIgnoreCase(suffix)) {
				fileTree = zipReader.unRar(filePath, fileName);
			}
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(outPath));
				pw.print(fileTree);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (null != fileTree) {
			map.put("fileTree", fileTree);
			return "compress";
		} else {
			map.put("msg", "压缩文件类型不受支持，尝试在压缩的时候选择RAR4格式");
			return "fileNotSupported";
		}
	}

}
