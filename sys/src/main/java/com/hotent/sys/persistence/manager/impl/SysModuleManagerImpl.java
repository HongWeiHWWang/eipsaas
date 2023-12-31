package com.hotent.sys.persistence.manager.impl;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.dao.SysModuleDao;
import com.hotent.sys.persistence.manager.SysModuleDetailManager;
import com.hotent.sys.persistence.manager.SysModuleManager;
import com.hotent.sys.persistence.model.SysModule;
import com.hotent.sys.persistence.model.SysModuleDetail;
import com.hotent.sys.persistence.param.SysModuleImportXml;
import com.hotent.sys.persistence.param.SysModuleXml;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

@Service
public class SysModuleManagerImpl extends BaseManagerImpl<SysModuleDao, SysModule> implements SysModuleManager {
    @Resource
    SysModuleDetailManager sysModuleDetailManager;

    @Override
    public void remove(Serializable id) {
        super.remove(id);
        sysModuleDetailManager.removeByModuleId(id);
    }

    @Override
    public void removeByIds(String... ids) {
        if (ids != null) {
            for (String pk : ids) {
                this.remove(pk);
            }
        }
    }

    @Override
    public void saveModule(SysModule sysModule) {
        String id = sysModule.getId();
        IUser user = ContextUtil.getCurrentUser();
        if (BeanUtils.isNotEmpty(user)) {
            sysModule.setCreateBy(user.getUserId());
            sysModule.setCreator(user.getFullname());
        } else {
            sysModule.setCreator("系统");
        }
        if (StringUtil.isEmpty(id)) {
            sysModule.setId(UniqueIdUtil.getSuid());
            sysModule.setUpdateTime(LocalDateTime.now());
            this.create(sysModule);
        } else {
            this.update(sysModule);
            sysModuleDetailManager.removeByModuleId(sysModule.getId());
        }
        List<SysModuleDetail> details = sysModule.getModuleDetail();
        if (BeanUtils.isNotEmpty(details)) {
            for (int x = 0; x < details.size(); x++) {
                SysModuleDetail sysModuleDetail = details.get(x);
                //重新设置排序
                sysModuleDetail.setSn(x);
                if (StringUtil.isEmpty(sysModuleDetail.getId())) {
                    sysModuleDetail.setId(UniqueIdUtil.getSuid());
                }
                sysModuleDetail.setModuleId(sysModule.getId());
                sysModuleDetailManager.create(sysModuleDetail);
            }
        }
    }

    @Override
    public SysModule getModuleByCode(String code) {
        return baseMapper.getModuleByCode(code);
    }

    @Override
    public Map<String, String> exportModules(List<String> idList) throws JAXBException {
        SysModuleImportXml importXml = new SysModuleImportXml();
        Map<String, String> map = new HashMap<String, String>();
        for (String id : idList) {
            SysModule module = this.get(id);
            if (BeanUtils.isNotEmpty(module)) {
                SysModuleXml moduleXml = new SysModuleXml();
                moduleXml.setSysModule(module);
                List<SysModuleDetail> details = sysModuleDetailManager.getModuleDetail(id, null);
                moduleXml.setModuleDetail(details);
                importXml.addModuleXml(moduleXml);
            }
        }
        map.put("module.xml", JAXBUtil.marshall(importXml, SysModuleImportXml.class));
        return map;
    }


    public void importModules(String unZipFilePath) throws JAXBException, UnsupportedEncodingException {
        String moduleXmlStr = FileUtil.readFile(unZipFilePath + File.separator + "module.xml");
        //模块导入
        if (StringUtil.isNotEmpty(moduleXmlStr)) {
            SysModuleImportXml importXml = (SysModuleImportXml) JAXBUtil.unmarshall(moduleXmlStr, SysModuleImportXml.class);
            List<SysModuleXml> moduleXmls = importXml.getModuleXmlList();
            for (SysModuleXml sysModuleXml : moduleXmls) {
                SysModule module = sysModuleXml.getSysModule();
                IUser user = ContextUtil.getCurrentUser();
                module.setCreateBy(user.getFullname());
                module.setCreateBy(user.getUserId());
                module.setUpdateBy(user.getUserId());
                SysModule oldModule = this.getModuleByCode(module.getCode());
                if (BeanUtils.isEmpty(oldModule)) {
                    module.setUpdateTime(LocalDateTime.now());
                    this.create(module);
                    ThreadMsgUtil.addMsg("模块 “" + module.getName() + "”[" + module.getCode() + "] 添加成功！");
                } else {
                    module.setId(module.getId());
                    this.update(module);
                    ThreadMsgUtil.addMsg("模块 “" + module.getName() + "”[" + module.getCode() + "] 已存在，更新成功！");
                }
                List<SysModuleDetail> details = sysModuleXml.getModuleDetail();
                sysModuleDetailManager.removeByModuleId(module.getId());
                if (BeanUtils.isNotEmpty(details)) {
                    for (SysModuleDetail detail : details) {
                        if (BeanUtils.isNotEmpty(sysModuleDetailManager.get(detail.getId()))) {
                            detail.setId(UniqueIdUtil.getSuid());
                        }
                        sysModuleDetailManager.create(detail);
                    }
                }
            }
        }

    }
}
