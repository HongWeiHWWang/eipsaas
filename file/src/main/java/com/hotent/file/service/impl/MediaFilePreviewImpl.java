package com.hotent.file.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.FileUtils;
/**
 * @author : kl
 * @authorboke : kailing.pub
 * @create : 2018-03-25 上午11:58
 * @description:
 **/
@SuppressWarnings(value = {"unchecked","rawtypes"})
@Service
public class MediaFilePreviewImpl implements FilePreview {

    @Autowired
    FileUtils fileUtils;

	@Override
    public String filePreviewHandle(DefaultFile fileMode,Map map) {
    	map.put("mediaUrl", "/file/onlinePreviewController/v1/getFileById_"+fileMode.getId());
        return "media";
    }
}
