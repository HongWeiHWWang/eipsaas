package com.hotent.file.persistence.manager.impl;


import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.attachment.AttachmentServiceFactory;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.model.UploadResult;
import com.hotent.file.persistence.dao.FileDao;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.util.AppFileUtil;
import com.hotent.uc.api.model.IUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;


@Service("fileManager")
public class FileManagerImpl extends BaseManagerImpl<FileDao,DefaultFile> implements FileManager{


	private AttachmentService attachmentService;

	// 初始化当前附件处理器
	private void initCurrentHandler() throws Exception{
		AttachmentServiceFactory attachmentHandlerFactory = AppUtil.getBean(AttachmentServiceFactory.class);
		attachmentService = attachmentHandlerFactory.getCurrentServices(AppFileUtil.getSaveType());
	}


	@Override
	public List<DefaultFile> getAllByExt(String[] allowFiles) {
		return baseMapper.getAllByExt(allowFiles);
	}

	@Override
	@Transactional
	public void delSysFileByIds(String[] ids) throws Exception {
		initCurrentHandler();
		for (String id : ids) {
			DefaultFile def=this.get(id);
			attachmentService.remove(def);
			baseMapper.deleteById(id);;
		}
	}

	@Override
	@Transactional
	public UploadResult uploadFile(DefaultFile file, List<MultipartFile> files, String fileFormates, IUser currentUser) throws Exception {
		Iterator<MultipartFile> it = files.iterator();
		initCurrentHandler();
		UploadResult result=new UploadResult();
		boolean mark=true;
		short isdel=0;
		while (it.hasNext()) {
			Boolean isAdd = false;
			String oldFilePath = "";
			DefaultFile sysFile;
			if(StringUtil.isNotEmpty(file.getId())){
				sysFile = this.get(file.getId());
				oldFilePath = sysFile.getFilePath();
			}
			else{
				// 新增的上传文件
				isAdd = true;
				sysFile = new DefaultFile();
				// 生成新的文件ID
				sysFile.setId(UniqueIdUtil.getSuid());
			}
			MultipartFile f = it.next();
			if(StringUtil.isNotEmpty(file.getFileName())){
				if(!file.getFileName().equals(f.getName())){
					throw new RuntimeException("上传名称与文件名称不符");
				}
			}
			String oriFileName = f.getOriginalFilename();
			String extName = FileUtil.getFileExt(oriFileName);
			//文件格式要求
			if(StringUtil.isNotEmpty(fileFormates)){
				//不符合文件格式要求的就标志为false
				if((fileFormates.indexOf(extName)<0) ){
					mark = false;
				}
			}
			if(mark){
				String fileName = sysFile.getId() + "." + extName;
				String filePath = "";

				String creatorAccount = DefaultFile.FILE_UPLOAD_UNKNOWN;
				// 当前用户的信息
				if (currentUser != null) {
					sysFile.setCreateBy(currentUser.getUserId());
					sysFile.setCreatorName(currentUser.getFullname());
					creatorAccount = currentUser.getAccount();
				}else{
					sysFile.setCreatorName(creatorAccount);
				}
				String sysPath = AppFileUtil.getAttachPath();
				if("pictureShow".equals(sysFile.getFileType())){
					filePath = AppFileUtil.createFilePath(sysPath+File.separator + creatorAccount + File.separator +"pictureShow", fileName);
				}else{
					filePath = AppFileUtil.createFilePath(sysPath + File.separator + currentUser.getAccount(), fileName);
				}
				// 附件名称
				sysFile.setFileName(oriFileName.lastIndexOf('.')==-1 ? oriFileName:oriFileName.substring(0, oriFileName.lastIndexOf('.')));
				//保存相对路径
				filePath=filePath.replace(sysPath + File.separator,"");
				sysFile.setFilePath(filePath);
				// 上传时间
				sysFile.setCreateTime(LocalDateTime.now());
				// 扩展名
				sysFile.setExtensionName(extName);
				// 字节总数
				sysFile.setByteCount(f.getSize());
				// 说明

				sysFile.setIsDel(isdel);
				sysFile.setNote(FileUtil.getSize(f.getSize()));
				String saveType = AppFileUtil.getSaveType();
				sysFile.setStoreType(saveType);
				// 总的字节数

				if(isAdd){
					sysFile.setBytes(f.getBytes());
					attachmentService.upload(sysFile, f.getInputStream());
					if(!saveType.equals(DefaultFile.SAVE_TYPE_DTABASE)){
						sysFile.setBytes(null);
					}
					super.create(sysFile);
				}
				else{
					attachmentService.upload(sysFile, f.getInputStream());
					this.update(sysFile);
					boolean tag = true;
					String newFilePath = sysFile.getFilePath();
					if(StringUtil.isNotEmpty(newFilePath)&&StringUtil.isNotEmpty(oldFilePath)){
						if(newFilePath.trim().equals(oldFilePath.trim())){
							tag = false;
						}
					}
					// 修改了文件的存放路径，需要删除之前路径下的文件
					if(tag){
						sysFile.setFilePath(oldFilePath);
						attachmentService.remove(sysFile);
					}
				}
				result.setSuccess(true);
				result.setFileId(sysFile.getId());
				result.setFileName(oriFileName);
				result.setSize(sysFile.getByteCount());


			}else{
				result.setSuccess(false);
				result.setMessage("系统不允许该类型文件的上传！:"+extName);
			}
		}
		return result;
	}

	@Override
	public DefaultFile downloadFile(String fileId, OutputStream outStream) throws Exception {
		initCurrentHandler();
		DefaultFile sysFile = this.get(fileId);
		if (BeanUtils.isEmpty(sysFile)){
			return null;
		}
		attachmentService.download(sysFile, outStream);
		return sysFile;
	}

	@Override
	@Transactional
	public void uploadFile(DefaultFile file, InputStream inputStream) throws Exception {
		initCurrentHandler();
		String storeType = file.getStoreType();
		file.setId(UniqueIdUtil.getSuid());
		attachmentService.upload(file, inputStream);
		if(StringUtil.isEmpty(storeType)) {
			storeType = AppFileUtil.getSaveType();
			file.setStoreType(storeType);
		}
		if(!DefaultFile.SAVE_TYPE_DTABASE.equals(storeType)){
			file.setBytes(null);
		}
		super.create(file);
	}

	@Override
	@Transactional
	public void setXbTypeId(List<String> fileId, String xbTypeId,String type) throws Exception {
		baseMapper.setXbTypeId(fileId,xbTypeId,type);
	}

	@Override
	@Transactional
	public void updateFileExtraProp(List<DefaultFile> files) {
		if (BeanUtils.isNotEmpty(files)) {
			for (DefaultFile defaultFile : files) {
				baseMapper.updateExtraProps(defaultFile);
			}
		}
	}
}
