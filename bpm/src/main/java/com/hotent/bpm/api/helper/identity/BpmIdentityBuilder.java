package com.hotent.bpm.api.helper.identity;

import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.model.identity.BpmIdentity;

public interface BpmIdentityBuilder {
	public BpmIdentity buildUser(String id,String name);
	public BpmIdentity buildOrg(String id,String name);
	public void setExtractInfo(BpmIdentity bpmIdentity,ExtractType extractType);
}
