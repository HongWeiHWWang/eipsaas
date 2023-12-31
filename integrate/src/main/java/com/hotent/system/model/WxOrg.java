package com.hotent.system.model;

import com.hotent.base.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WxOrg {
    private static final Log logger= LogFactory.getLog(WxOrg.class);
	private String id;
	private String parentid;
	private String name;
	private String order;



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	@Override
	public String toString() {
        String json = "";
	    try {
            json = JsonUtil.toJson(this);
        }catch (Exception e){
	        logger.error(e);
        }
		return json;
	}
}
