package com.hotent.bpm.model.process.nodedef.ext.extmodel;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.model.form.DefaultForm;

public class DefaultFormExt extends DefaultForm implements FormExt{
	private static final long serialVersionUID = 1L;

	private String prevHandler;
	
	private String postHandler;

	public String getPrevHandler() {
		return prevHandler;
	}

	public void setPrevHandler(String prevHandler) {
		this.prevHandler = prevHandler;
	}

	public String getPostHandler() {
		return postHandler;
	}

	public void setPostHandler(String postHandler) {
		this.postHandler = postHandler;
	}

}
