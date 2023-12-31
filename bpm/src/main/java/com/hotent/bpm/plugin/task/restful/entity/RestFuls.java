//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2017.10.23 时间 01:30:36 PM CST 
//


package com.hotent.bpm.plugin.task.restful.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.jee-soft.cn/bpm/plugins/execution/restFuls}restFul" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "restFul"
})
@XmlRootElement(name = "restFuls")
public class RestFuls {

    protected List<RestFul> restFul;

    /**
     * Gets the value of the restFul property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the restFul property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestFul().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RestFul }
     * 
     * 
     */
    public List<RestFul> getRestFul() {
        if (restFul == null) {
            restFul = new ArrayList<RestFul>();
        }
        return this.restFul;
    }
    
    public void setRestFul(List<RestFul> restFulExtList){
    	this.restFul = restFulExtList;
    }

}
