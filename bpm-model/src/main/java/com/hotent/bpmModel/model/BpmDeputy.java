package com.hotent.bpmModel.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 流程代理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月15日
 */
@ApiModel("流程代理")
@TableName("bpm_deputy")
public class BpmDeputy extends BaseModel<BpmDeputy> {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="主键ID")
   @TableId("id_")
   protected String id;

   @ApiModelProperty(value="被代理人id")
   @TableField("user_id_")
   protected String userId;

   @ApiModelProperty(value="被代理人姓名")
   @TableField("user_name_")
   protected String userName;

   @ApiModelProperty(value="代理人id")
   @TableField("agent_id_")
   protected String agentId;

   @ApiModelProperty(value="代理人姓名")
   @TableField("agent_name_")
   protected String agentName;

   @ApiModelProperty(value="是否接收邮件(1:接收，0：不接收)")
   @TableField("is_mail_")
   protected Integer isMail;

   @ApiModelProperty(value="是否可用（1：启用，0：禁用）")
   @TableField("is_usable_")
   protected Integer isUsable;


   public void setId(String id) {
       this.id = id;
   }

   /**
    * 返回 主键ID
    * @return
    */
   public String getId() {
       return this.id;
   }

   public void setUserId(String userId) {
       this.userId = userId;
   }

   /**
    * 返回 被代理人id
    * @return
    */
   public String getUserId() {
       return this.userId;
   }

   public void setUserName(String userName) {
       this.userName = userName;
   }

   /**
    * 返回 被代理人姓名
    * @return
    */
   public String getUserName() {
       return this.userName;
   }

   public void setAgentId(String agentId) {
       this.agentId = agentId;
   }

   /**
    * 返回 代理人id
    * @return
    */
   public String getAgentId() {
       return this.agentId;
   }

   public void setAgentName(String agentName) {
       this.agentName = agentName;
   }

   /**
    * 返回 代理人姓名
    * @return
    */
   public String getAgentName() {
       return this.agentName;
   }

   public void setIsMail(Integer isMail) {
       this.isMail = isMail;
   }

   /**
    * 返回 是否接收邮件(1:接收，0：不接收)
    * @return
    */
   public Integer getIsMail() {
       return this.isMail;
   }

   public void setIsUsable(Integer isUsable) {
       this.isUsable = isUsable;
   }

   /**
    * 返回 是否可用（1：启用，0：禁用）
    * @return
    */
   public Integer getIsUsable() {
       return this.isUsable;
   }
   /**
    * @see Object#toString()
    */
   public String toString() {
       return new ToStringBuilder(this)
       .append("id", this.id)
       .append("userId", this.userId)
       .append("userName", this.userName)
       .append("agentId", this.agentId)
       .append("agentName", this.agentName)
       .append("isMail", this.isMail)
       .append("isUsable", this.isUsable)
       .toString();
   }
}