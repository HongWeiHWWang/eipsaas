package com.hotent.file.service;


import java.util.Map;

import com.hotent.file.model.DefaultFile;


/**
 * Created by kl on 2018/1/17.
 * Content :
 */
@SuppressWarnings("rawtypes")
public interface FilePreview {
	/**
	 * 处理上传的文件
	 * @param fileMode
	 * @param map
	 * @return
	 */

	String filePreviewHandle(DefaultFile fileMode,Map map);
}
