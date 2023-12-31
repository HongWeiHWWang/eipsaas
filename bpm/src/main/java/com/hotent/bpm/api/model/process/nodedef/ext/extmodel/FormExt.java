package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import com.hotent.bpm.model.form.Form;


public interface FormExt  extends Form {
	/**
	 * 获取前置处理
	 * @return
	 */
	String getPrevHandler();
	/**
	 * 设置前置处理
	 * @param prevHandler
	 */
	void setPrevHandler(String prevHandler);
	/**
	 * 获取后置处理
	 * @return
	 */
	String getPostHandler();
	/**
	 * 设置后置处理
	 * @param postHandler
	 */
	void setPostHandler(String postHandler);
}
