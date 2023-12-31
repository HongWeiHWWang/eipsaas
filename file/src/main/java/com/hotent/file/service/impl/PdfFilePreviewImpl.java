package com.hotent.file.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.FileUtils;

/**
 * Created by kl on 2018/1/17.
 * Content :处理pdf文件
 */
@SuppressWarnings(value = "unchecked")
@Service
public class PdfFilePreviewImpl implements FilePreview{

	@Autowired
	FileUtils fileUtils;
	
    @SuppressWarnings("rawtypes")
	@Override
    public String filePreviewHandle(DefaultFile fileMode,Map map) {
    	map.put("pdfUrl", "/file/onlinePreviewController/v1/getFileById_"+fileMode.getId());
        return "pdf";
    }


}
