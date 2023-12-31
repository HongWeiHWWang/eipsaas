package com.hotent.file.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hotent.base.attachment.Attachment;
import com.hotent.file.model.FileType;
import com.hotent.file.util.FileUtils;

/**
 * Created by kl on 2018/1/17.
 * Content :
 */
@Service
public class FilePreviewFactory {

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ApplicationContext context;

    public FilePreview get(Attachment attachment) {
        Map<String, FilePreview> filePreviewMap = context.getBeansOfType(FilePreview.class);
        FileType type=fileUtils.typeFromUrl(attachment.getExtensionName());
        return filePreviewMap.get(type.getInstanceName());
    }
}
