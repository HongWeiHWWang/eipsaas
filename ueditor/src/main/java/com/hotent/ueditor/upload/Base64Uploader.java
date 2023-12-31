package com.hotent.ueditor.upload;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.ueditor.PathFormat;
import com.hotent.ueditor.define.AppInfo;
import com.hotent.ueditor.define.BaseState;
import com.hotent.ueditor.define.FileType;
import com.hotent.ueditor.define.State;


public class Base64Uploader {
	@Resource
	StorageService storageService;
	
	public State save(HttpServletRequest request, Map<String, Object> conf) {
	    String filedName = (String) conf.get("fieldName");
		String fileName = request.getParameter(filedName);
		byte[] data = decode(fileName);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"));
		
		savePath = savePath + suffix;
//		String rootPath = ConfigManager.getRootPath(request,conf);
		String rootPath = "";
		String physicalPath = rootPath + savePath;
		if(storageService==null) {
			storageService = AppUtil.getBean(StorageService.class);
		}
		State storageState = storageService.saveBinaryFile(data, physicalPath);

		if (storageState.isSuccess()) {
			storageState.putInfo("url", PathFormat.format(savePath));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}

	private byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}