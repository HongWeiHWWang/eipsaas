package com.hotent.ueditor.upload;

import java.io.InputStream;

import com.hotent.ueditor.define.State;

public interface StorageService {
	/**
	 * 以二进制保存图片
	 * @param data		二进制数据
	 * @param path		路径
	 * @return			保存结果
	 */
	State saveBinaryFile(byte[] data, String path);
	/**
	 * 以输入流保存图片
	 * @param is		输入流
	 * @param path		路径
	 * @return			保存结果
	 */
	State saveFileByInputStream(InputStream is, String path);
}
