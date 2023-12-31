package com.hotent.sys.persistence.param;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * <pre>
 * 描述：导出模块开发 实体对象
 * 作者:leijian
 * 日期:2020-3-23 10:55:02
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@XmlRootElement(name = "sysModuleImport")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysModuleImportXml {

    @XmlElement(name = "moduleXml", type = SysModuleXml.class)
    List<SysModuleXml> moduleXmlList = new ArrayList<SysModuleXml>();

    public List<SysModuleXml> getModuleXmlList() {
        return moduleXmlList;
    }

    public void setModuleXmlList(List<SysModuleXml> moduleXmlList) {
        this.moduleXmlList = moduleXmlList;
    }

    public void addModuleXml(SysModuleXml sysModuleXml){
        moduleXmlList.add(sysModuleXml);
    }
}
