package com.hotent.file.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.file.model.DefaultFile;
import com.hotent.file.service.FilePreview;
import com.hotent.file.util.FileUtils;

/**
 * Created by kl on 2018/1/17.
 * Content :其他文件
 */
@SuppressWarnings(value = "unchecked")
@Service
public class OtherFilePreviewImpl implements FilePreview {
    @Autowired
    FileUtils fileUtils;

    @SuppressWarnings("rawtypes")
	@Override
    public String filePreviewHandle(DefaultFile fileMode,Map map) {
        map.put("fileType",fileMode.getExtensionName());
        map.put("msg", "系统还不支持该格式文件的在线预览，" +
                "如有需要请按下方显示的邮箱地址联系系统维护人员");
        return "fileNotSupported";
    }

}
