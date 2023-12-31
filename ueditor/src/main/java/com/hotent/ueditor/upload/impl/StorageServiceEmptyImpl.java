package com.hotent.ueditor.upload.impl;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.hotent.ueditor.define.AppInfo;
import com.hotent.ueditor.define.BaseState;
import com.hotent.ueditor.define.State;
import com.hotent.ueditor.upload.StorageService;

/**
 * 默认实现，未对附件做任何处理，直接返回上传失败的结果
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年12月28日
 */
@Component
public class StorageServiceEmptyImpl implements StorageService{
	@Override
	public State saveBinaryFile(byte[] data, String path) {
		return getState();
	}

	@Override
	public State saveFileByInputStream(InputStream is, String path) {
		return getState();
	}
	
	private State getState() {
		State state = new BaseState(false, AppInfo.IO_ERROR);
		state.putInfo("code", "unimplemented");
		state.putInfo("message", "no implements for StorageService.");
		return state;
	}
}
