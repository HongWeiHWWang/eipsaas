//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.10 at 04:35:24 下午 CST 
//


package com.hotent.bpm.plugin.execution.message.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="plainText" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.jee-soft.cn/bpm/plugins/userCalc/base}userRule" maxOccurs="unbounded"/>
 *                   &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="msgType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="html" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.jee-soft.cn/bpm/plugins/userCalc/base}userRule" maxOccurs="unbounded"/>
 *                   &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="msgType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *       &lt;attribute name="externalClass" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "message", namespace = "http://www.jee-soft.cn/bpm/plugins/execution/message")
public class Message {

    @XmlElement(namespace = "http://www.jee-soft.cn/bpm/plugins/execution/message")
    protected Message.PlainText plainText;
    @XmlElement(namespace = "http://www.jee-soft.cn/bpm/plugins/execution/message")
    protected Message.Html html;
    @XmlAttribute
    protected String externalClass;

    /**
     * Gets the value of the plainText property.
     * 
     * @return
     *     possible object is
     *     {@link Message.PlainText }
     *     
     */
    public Message.PlainText getPlainText() {
        return plainText;
    }

    /**
     * Sets the value of the plainText property.
     * 
     * @param value
     *     allowed object is
     *     {@link Message.PlainText }
     *     
     */
    public void setPlainText(Message.PlainText value) {
        this.plainText = value;
    }

    /**
     * Gets the value of the html property.
     * 
     * @return
     *     possible object is
     *     {@link Message.Html }
     *     
     */
    public Message.Html getHtml() {
        return html;
    }

    /**
     * Sets the value of the html property.
     * 
     * @param value
     *     allowed object is
     *     {@link Message.Html }
     *     
     */
    public void setHtml(Message.Html value) {
        this.html = value;
    }

    /**
     * Gets the value of the externalClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalClass() {
        return externalClass;
    }

    /**
     * Sets the value of the externalClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalClass(String value) {
        this.externalClass = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://www.jee-soft.cn/bpm/plugins/userCalc/base}userRule" maxOccurs="unbounded"/>
     *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="msgType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "userRule",
        "subject",
        "content"
    })
    public static class Html {

        @XmlElement(required = true)
        protected List<UserRule> userRule;
        @XmlElement(namespace = "http://www.jee-soft.cn/bpm/plugins/execution/message", required = true)
        protected String subject;
        @XmlElement(namespace = "http://www.jee-soft.cn/bpm/plugins/execution/message", required = true)
        protected String content;
        @XmlAttribute(required = true)
        protected String msgType;

        /**
         * 消息人员 Gets the value of the userRule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userRule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUserRule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link UserRule }
         * 
         * 
         */
        public List<UserRule> getUserRule() {
            if (userRule == null) {
                userRule = new ArrayList<UserRule>();
            }
            return this.userRule;
        }

        /**
         * Gets the value of the subject property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSubject() {
            return subject;
        }

        /**
         * Sets the value of the subject property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSubject(String value) {
            this.subject = value;
        }

        /**
         * Gets the value of the content property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContent() {
            return content;
        }

        /**
         * Sets the value of the content property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContent(String value) {
            this.content = value;
        }

        /**
         * Gets the value of the msgType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMsgType() {
            return msgType;
        }

        /**
         * Sets the value of the msgType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMsgType(String value) {
            this.msgType = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://www.jee-soft.cn/bpm/plugins/userCalc/base}userRule" maxOccurs="unbounded"/>
     *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="msgType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "userRule",
        "content"
    })
    public static class PlainText {

        @XmlElement(required = true)
        protected List<UserRule> userRule;
        @XmlElement(namespace = "http://www.jee-soft.cn/bpm/plugins/execution/message", required = true)
        protected String content;
        @XmlAttribute(required = true)
        protected String msgType;

        /**
         * 消息人员 Gets the value of the userRule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userRule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUserRule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link UserRule }
         * 
         * 
         */
        public List<UserRule> getUserRule() {
            if (userRule == null) {
                userRule = new ArrayList<UserRule>();
            }
            return this.userRule;
        }

        /**
         * Gets the value of the content property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContent() {
            return content;
        }

        /**
         * Sets the value of the content property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContent(String value) {
            this.content = value;
        }

        /**
         * Gets the value of the msgType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMsgType() {
            return msgType;
        }

        /**
         * Sets the value of the msgType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMsgType(String value) {
            this.msgType = value;
        }

    }

}
