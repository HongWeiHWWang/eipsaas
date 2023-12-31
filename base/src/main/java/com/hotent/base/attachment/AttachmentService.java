/**
 * 
 */
package com.hotent.base.attachment;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 附件管理接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
public interface AttachmentService {
	
	/**
	 * 获取附件处理器类型
	 * @return
	 */
	String getStoreType();

	/**
	 * 删除附件
	 * @param sysFile
	 * @throws Exception
	 */
	void remove(Attachment attachment) throws Exception;
	/**
	 * 上传附件
	 * @param sysFile
	 * @param inputStream
	 * @throws Exception
	 */
	void upload(Attachment attachment, InputStream inputStream) throws Exception;
	/**
	 * 下载附件
	 * @param sysFile
	 * @param outStream
	 * @throws Exception
	 */
	void download(Attachment attachment, OutputStream outStream) throws Exception;


	/**
	 * 判断附件是否存在
	 * @param attachment
	 * @return
	 */
	boolean chekckFile(Attachment attachment) throws Exception;
}
