package com.hotent.file.service.impl;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.FileUtils;

/**
 * Created by kl on 2018/1/17.
 * Content :图片文件处理
 */
@SuppressWarnings(value = "unchecked")
@Service
public class PictureFilePreviewImpl implements FilePreview {

	@Autowired
	FileUtils fileUtils;

	@SuppressWarnings("rawtypes")
	@Override
	public String filePreviewHandle(DefaultFile fileMode,Map map) {
		List<String> list=new ArrayList<String>();
		String str="/file/onlinePreviewController/v1/getFileById_"+fileMode.getId();
		list.add(str);
		map.put("imgUrls", list);
		map.put("currentUrl",str);
		return "picture";
	}
}
