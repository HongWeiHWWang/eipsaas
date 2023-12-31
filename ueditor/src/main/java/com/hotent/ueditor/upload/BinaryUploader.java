package com.hotent.ueditor.upload;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hotent.base.util.AppUtil;
import com.hotent.ueditor.define.AppInfo;
import com.hotent.ueditor.define.BaseState;
import com.hotent.ueditor.define.FileType;
import com.hotent.ueditor.define.State;

@Component
public class BinaryUploader {
	@Autowired
	StorageService storageService;
	
	public State save(HttpServletRequest request, Map<String, Object> conf) {
		String contentType = request.getContentType();
		
		if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		
		MultipartFile file = ((MultipartHttpServletRequest)request).getFile("upfile");
		
		String suffix = FileType.getSuffixByFilename(file.getOriginalFilename());
		
		if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
			return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
		}

		try {
			if(storageService==null) {
				storageService = AppUtil.getBean(StorageService.class);
			}
			State storageState = storageService.saveFileByInputStream(file.getInputStream(), file.getOriginalFilename());

			if (storageState.isSuccess()) {
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", file.getOriginalFilename());
			}

			return storageState;
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
	}

	private boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
}
