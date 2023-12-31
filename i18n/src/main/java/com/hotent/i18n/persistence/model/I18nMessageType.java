package com.hotent.i18n.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <pre>
 * 国际化资源支持的语言类型 实体对象
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@ApiModel(description = "type定义")
@TableName("portal_i18n_message_type")
public class I18nMessageType extends BaseModel<I18nMessageType> {
    private static final long serialVersionUID = 1L;

    @XmlTransient
    @TableId("id_")
    @ApiModelProperty("主键")
    protected String id = "";

    @XmlAttribute(name = "type")
    @TableField("type_")
    @ApiModelProperty(name = "type", notes = "类型")
    protected String type;

    @XmlAttribute(name = "desc")
    @TableField("desc_")
    @ApiModelProperty(name = "desc", notes = "说明")
    protected String desc;


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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 返回 说明
     *
     * @return
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("type", this.type)
                .append("desc", this.desc)
                .toString();
    }
}