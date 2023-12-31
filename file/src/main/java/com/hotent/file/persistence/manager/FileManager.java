package com.hotent.file.persistence.manager;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.manager.BaseManager;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.model.UploadResult;
import com.hotent.uc.api.model.IUser;

public interface FileManager extends BaseManager<DefaultFile> {
	/**
	 * 根据附件格式获取附件
	 * @param allowFiles
	 * @return
	 */
	List<DefaultFile> getAllByExt(String[] allowFiles);
	/**
	 * 删除附件及附件表中的记录
	 * @param ids
	 * @throws Exception
	 */
	void delSysFileByIds(String[] ids) throws Exception;
	/**
	 * 附件上传
	 * @param file 附件对象
	 * @param files 上传的文件流
	 * @param fileFormates 格式限定
	 * @param currentUser 当前用户
	 * @return
	 * @throws Exception
	 */
	UploadResult uploadFile(DefaultFile file, List<MultipartFile> files, String fileFormates, IUser currentUser) throws Exception;
	/**
	 * 附件上传
	 * @param file			附件对象
	 * @param inputStream	附件输入流
	 * @throws Exception
	 */
	void uploadFile(DefaultFile file, InputStream inputStream) throws Exception;
	/**
	 * 附件的下载
	 * @param fileId		附件ID
	 * @param outStream		附件输出流
	 * @return
	 * @throws Exception
	 */
	DefaultFile downloadFile(String fileId, OutputStream outStream) throws Exception;


	/**
	 * 设置附件分类
	 * @param fileId
	 * @param xbTypeId
	 * @throws Exception
	 */
	void setXbTypeId( List<String> fileId, String xbTypeId,String type) throws Exception;

	void updateFileExtraProp(List<DefaultFile> files);

}
