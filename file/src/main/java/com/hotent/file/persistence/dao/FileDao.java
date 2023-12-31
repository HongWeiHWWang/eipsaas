package com.hotent.file.persistence.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.file.model.DefaultFile;
import org.apache.ibatis.annotations.Param;


public interface FileDao extends BaseMapper<DefaultFile> {

	/**
	 * 根据格式获取附件
	 * @param allowFiles
	 * @return
	 */
	List<DefaultFile> getAllByExt(String[] allowFiles);

	public void setXbTypeId(@Param("fileId")List<String> fileId, @Param("xbTypeId")String xbTypeId,@Param("type")String type);

	void  updateExtraProps(DefaultFile defaultFile);
}
