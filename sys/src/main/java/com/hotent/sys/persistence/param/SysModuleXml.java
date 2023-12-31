package com.hotent.sys.persistence.param;

import com.hotent.sys.persistence.model.SysModule;
import com.hotent.sys.persistence.model.SysModuleDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 *
 * <pre>
 * 描述：导入模块开发 实体对象
 * 作者:leijian
 * 日期:2020-3-23 10:55:02
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@XmlRootElement(name = "sysModules")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysModuleXml {

    @XmlElement(name = "sysModule", type = SysModule.class)
    private SysModule sysModule;

    @XmlElement(name = "moduleDetail", type = SysModuleDetail.class)
    private List<SysModuleDetail> moduleDetail;

    public SysModule getSysModule() {
        return sysModule;
    }

    public void setSysModule(SysModule sysModule) {
        this.sysModule = sysModule;
    }

    public List<SysModuleDetail> getModuleDetail() {
        return moduleDetail;
    }

    public void setModuleDetail(List<SysModuleDetail> moduleDetail) {
        this.moduleDetail = moduleDetail;
    }

}
