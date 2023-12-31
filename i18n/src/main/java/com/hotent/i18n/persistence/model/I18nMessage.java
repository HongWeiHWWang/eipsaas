package com.hotent.i18n.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 国际化资源 实体对象
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@ApiModel(description = "i18n定义")
@TableName("portal_i18n_message")
@JsonIgnoreProperties(ignoreUnknown = true)
public class I18nMessage extends AutoFillModel<I18nMessage> {
    private static final long serialVersionUID = 1L;

    @XmlTransient
    @TableId("id_")
    @ApiModelProperty("主键")
    protected String id = "";

    @XmlAttribute(name = "type")
    @TableField("type_")
    @ApiModelProperty(name = "type", notes = "类型")
    protected String type;

    @XmlAttribute(name = "key")
    @TableField("key_")
    @ApiModelProperty(name = "key", notes = "资源key")
    protected String key;

    @XmlAttribute(name = "value")
    @TableField("value_")
    @ApiModelProperty(name = "value", notes = "资源值")
    protected String value;

    /**
     * 类型字符串集
     */
    @TableField(exist=false)
    protected String types;

    /**
     * 资源值字符串集
     */
    @TableField(exist=false)
    protected String vals;

    /**
     * 国际化类型信息
     */
    @TableField(exist=false)
    protected List<Map<String, String>> mesTypeInfo;


    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回 主键
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 返回 类型
     *
     * @return
     */
    public String getType() {
        return this.type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 返回 资源KEY
     *
     * @return
     */
    public String getKey() {
        return this.key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 返回 资源值
     *
     * @return
     */
    public String getValue() {
        return this.value;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getVals() {
        return vals;
    }

    public List<Map<String, String>> getMesTypeInfo() {
        return mesTypeInfo;
    }

    public void setMesTypeInfo(List<Map<String, String>> mesTypeInfo) {
        this.mesTypeInfo = mesTypeInfo;
    }

    public void setVals(String vals) {
        this.vals = vals;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("type", this.type)
                .append("key", this.key)
                .append("value", this.value)
                .append("vals", this.vals)
                .append("types", this.types)
                .toString();
    }
}