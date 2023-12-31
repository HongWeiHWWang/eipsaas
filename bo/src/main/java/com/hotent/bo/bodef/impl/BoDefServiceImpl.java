package com.hotent.bo.bodef.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.hotent.base.util.JAXBUtil;
import com.hotent.bo.bodef.BoDefService;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.persistence.manager.BoEntManager;

/**
 * bo定义接口实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@Service
public class BoDefServiceImpl implements BoDefService {
	@Resource
	BoDefManager boDefManager;
	@Resource
	BoEntManager boEntManager;

	@Override
	public BoDef getByAlias(String alias) {
		BoDef def= boDefManager.getByAlias(alias);
		return def;
	}

	@Override
	public BoDef getPureByAlias(String alias) {
		return boDefManager.getPureByAlias(alias);
	}

	@Override
	public BoDef getByDefId(String defId) {
		BoDef def= boDefManager.getByDefId(defId);
		return def;
	}

	@Override
	public String getXmlByDefId(String defId) throws JAXBException {
		BoDef def= boDefManager.getByDefId(defId);
		String xml=JAXBUtil.marshall(def, BoDef.class);
		return xml;
	}

	@Override
	public BoDef parseXml(String xml) throws UnsupportedEncodingException, JAXBException {
		BoDef def = (BoDef)JAXBUtil.unmarshall(xml, BoDef.class);
		return (BoDef)def;
	}

	@Override
	public BoEnt getEntByName(String name) {
		return boEntManager.getByName(name);
	}

	@Override
	public List<BoDef> importBoDef(List<BoDef> boDefs) {
		return boDefManager.importBoDef(boDefs);
	}
}
