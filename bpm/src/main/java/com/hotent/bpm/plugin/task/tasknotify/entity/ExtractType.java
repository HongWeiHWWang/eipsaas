//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.06 at 03:52:33 下午 CST 
//


package com.hotent.bpm.plugin.task.tasknotify.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for extractType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="extractType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="no"/>
 *     &lt;enumeration value="extract"/>
 *     &lt;enumeration value="secondExtract"/>
 *     &lt;enumeration value="usergroup"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "extractType", namespace = "http://www.jee-soft.cn/bpm/plugins/userCalc/base")
@XmlEnum
public enum ExtractType {

    @XmlEnumValue("no")
    NO("no"),
    @XmlEnumValue("extract")
    EXTRACT("extract"),
    @XmlEnumValue("secondExtract")
    SECOND_EXTRACT("secondExtract"),
    @XmlEnumValue("usergroup")
    USERGROUP("usergroup");
    private final String value;

    ExtractType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExtractType fromValue(String v) {
        for (ExtractType c: ExtractType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
