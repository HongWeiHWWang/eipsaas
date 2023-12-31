package com.hotent.portal.ueditor;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hotent.base.util.FileUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.util.AppFileUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.ueditor.define.AppInfo;
import com.hotent.ueditor.define.BaseState;
import com.hotent.ueditor.define.State;
import com.hotent.ueditor.upload.StorageService;

@Service
@Primary
public class StorageServiceFileImpl implements StorageService{
	@Resource
	FileManager fileManager;
	@Value("${base.domain}")
	private String baseDomain;
	
	@Override
	public State saveBinaryFile(byte[] data, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State saveFileByInputStream(InputStream is, String fileName) {
		Assert.isTrue(StringUtil.isNotEmpty(fileName), "上传图片时path不能为空");
		DefaultFile file = new DefaultFile();
		IUser currentUser = ContextUtil.getCurrentUser();
		if (currentUser != null) {
			file.setCreateBy(currentUser.getUserId());
			file.setCreatorName(currentUser.getFullname());
		}else{
			file.setCreatorName(DefaultFile.FILE_UPLOAD_UNKNOWN);
		}
		file.setFileName(fileName.lastIndexOf('.')==-1 ? fileName:fileName.substring(0, fileName.lastIndexOf('.')));
		String sysPath = AppFileUtil.getAttachPath();
		String filePath = AppFileUtil.createFilePath(sysPath + File.separator , fileName);
		filePath = filePath.replace(sysPath + File.separator,"");
		file.setFilePath(filePath);
		// 上传时间
		file.setCreateTime(LocalDateTime.now());
		String extName = FileUtil.getFileExt(fileName);
		// 扩展名
		file.setExtensionName(extName);
		String saveType = AppFileUtil.getSaveType();
		file.setStoreType(saveType);
		try {
			fileManager.uploadFile(file, is);
			State state = new BaseState(true);
			state.putInfo("title", fileName);
			state.putInfo("url", String.format("%s/system/file/v1/downloadFile?fileId=%s", baseDomain, file.getId()));
			return state;
		} catch (Exception e) {
			State state = new BaseState(false, AppInfo.IO_ERROR);
			state.putInfo("code", e.getClass().getCanonicalName());
			state.putInfo("message", e.getMessage());
			return state;
		}
	}
}
