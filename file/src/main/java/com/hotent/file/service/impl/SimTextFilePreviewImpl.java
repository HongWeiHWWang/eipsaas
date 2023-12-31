package com.hotent.file.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.FileUtils;

/**
 * Created by kl on 2018/1/17.
 * Content :处理文本文件
 */
@SuppressWarnings(value = "unchecked")
@Service
public class SimTextFilePreviewImpl implements FilePreview{



	@Autowired
	FileUtils fileUtils;

	@SuppressWarnings("rawtypes")
	@Override
	public String filePreviewHandle(DefaultFile fileMode,Map map){
		map.put("TxtUrl", "/file/onlinePreviewController/v1/getFileById_"+fileMode.getId());
		return "txt";
	}
}
