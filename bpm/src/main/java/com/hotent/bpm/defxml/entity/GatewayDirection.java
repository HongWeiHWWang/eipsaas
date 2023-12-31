//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.26 at 07:21:56 下午 CST 
//


package com.hotent.bpm.defxml.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tGatewayDirection.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tGatewayDirection">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Unspecified"/>
 *     &lt;enumeration value="Converging"/>
 *     &lt;enumeration value="Diverging"/>
 *     &lt;enumeration value="Mixed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tGatewayDirection")
@XmlEnum
public enum GatewayDirection {

    @XmlEnumValue("Unspecified")
    UNSPECIFIED("Unspecified"),
    @XmlEnumValue("Converging")
    CONVERGING("Converging"),
    @XmlEnumValue("Diverging")
    DIVERGING("Diverging"),
    @XmlEnumValue("Mixed")
    MIXED("Mixed");
    private final String value;

    GatewayDirection(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GatewayDirection fromValue(String v) {
        for (GatewayDirection c: GatewayDirection.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
