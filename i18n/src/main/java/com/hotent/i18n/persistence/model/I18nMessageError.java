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
 * 国际化资源异常日志 实体对象
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@TableName("portal_i18n_message_error")
@ApiModel(description = "error定义")
public class I18nMessageError extends BaseModel<I18nMessageError> {

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
    @ApiModelProperty(name = "key", notes = "资源值")
    protected String key;

    @XmlAttribute(name = "count")
    @TableField("count_")
    @ApiModelProperty(name = "count", notes = "异常数量")
    protected Integer count;


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

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 返回 异常数量
     *
     * @return
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("type", this.type)
                .append("key", this.key)
                .append("count", this.count)
                .toString();
    }
}