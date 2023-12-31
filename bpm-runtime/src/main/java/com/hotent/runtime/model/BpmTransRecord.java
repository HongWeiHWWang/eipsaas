package com.hotent.runtime.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;


/**
* 移交记录
* <pre>
* 描述：移交记录 实体对象
* 构建组：x7
* @author zhaoxy
* @company 广州宏天软件股份有限公司
* @email zhxy@jee-soft.cn
* @date 2019-02-18 09:46
* </pre>
*/
@TableName("bpm_trans_record")
@ApiModel(value = "BpmTransRecord",description = "移交记录")
public class BpmTransRecord extends AutoFillModel<BpmTransRecord> {

   private static final long serialVersionUID = 1L;

   @XmlTransient
   @TableId("id_")
   @ApiModelProperty(value="ID_")
   protected String id;

   @XmlAttribute(name = "type")
   @TableField("TYPE_")
   @ApiModelProperty(value="移交类型")
   protected String type;

   @XmlAttribute(name = "transfer")
   @TableField("TRANSFER_")
   @ApiModelProperty(value="移交人ID")
   protected String transfer;

   @XmlAttribute(name = "transferName")
   @TableField("TRANSFER_NAME_")
   @ApiModelProperty(value="移交人姓名")
   protected String transferName;

   @XmlAttribute(name = "transfered")
   @TableField("TRANSFERED_")
   @ApiModelProperty(value="被移交人id")
   protected String transfered;

   @XmlAttribute(name = "transferedName")
   @TableField("TRANSFERED_NAME_")
   @ApiModelProperty(value="被移交人姓名")
   protected String transferedName;

   @XmlAttribute(name = "reason")
   @TableField("REASON_")
   @ApiModelProperty(value="移交原因")
   protected String reason;

   @XmlAttribute(name = "result")
   @TableField("RESULT_")
   @ApiModelProperty(value="移交结果")
   protected String result;

   @XmlAttribute(name = "var")
   @TableField("VAR_")
   @ApiModelProperty(value="移交方式")
   protected Integer var;

   @XmlAttribute(name = "procinstIds")
   @TableField("PROC_INST_IDS_")
   @ApiModelProperty(value="流程实例组，多个用逗号隔开")
   protected String procinstIds;

   @TableField(exist=false)
   @ApiModelProperty(value="y 为管理员干预")
   protected String isadmin;

   @TableField(exist=false)
   @ApiModelProperty(value="流程实例id和流程定义id组")
   protected String insts;

   @TableField(exist=false)
   @ApiModelProperty(value="流程实例id组")
   protected List<String> instIds;
   
   public String getIsadmin() {
       return isadmin;
   }

   public void setIsadmin(String isadmin) {
       this.isadmin = isadmin;
   }

   public String getProcinstIds() {
       return procinstIds;
   }

   public void setProcinstIds(String procinstIds) {
       this.procinstIds = procinstIds;
   }

   public List<String> getInstIds() {
       return instIds;
   }

   public void setInstIds(List<String> instIds) {
       this.instIds = instIds;
   }

   public String getInsts() {
       return insts;
   }

   public void setInsts(String insts) {
       this.insts = insts;
   }

   public void setId(String id) {
       this.id = id;
   }

   /**
    * 返回 ID_
    * @return
    */
   public String getId() {
       return this.id;
   }

   public void setType(String type) {
       this.type = type;
   }

   /**
    * 返回 移交类型
    * @return
    */
   public String getType() {
       return this.type;
   }

   public void setTransfer(String transfer) {
       this.transfer = transfer;
   }

   /**
    * 返回 移交人ID
    * @return
    */
   public String getTransfer() {
       return this.transfer;
   }

   public void setTransferName(String transferName) {
       this.transferName = transferName;
   }

   /**
    * 返回 移交人姓名
    * @return
    */
   public String getTransferName() {
       return this.transferName;
   }

   public void setTransfered(String transfered) {
       this.transfered = transfered;
   }

   /**
    * 返回 被移交人id
    * @return
    */
   public String getTransfered() {
       return this.transfered;
   }

   public void setTransferedName(String transferedName) {
       this.transferedName = transferedName;
   }

   /**
    * 返回 被移交人姓名
    * @return
    */
   public String getTransferedName() {
       return this.transferedName;
   }

   public void setReason(String reason) {
       this.reason = reason;
   }

   public Integer getVar() {
       return var;
   }

   public void setVar(Integer var) {
       this.var = var;
   }

   /**
    * 返回 移交原因
    * @return
    */
   public String getReason() {
       return this.reason;
   }

   public void setResult(String result) {
       this.result = result;
   }

   /**
    * 返回 移交结果
    * @return
    */
   public String getResult() {
       return this.result;
   }
   /**
    * @see Object#toString()
    */
   public String toString() {
       return new ToStringBuilder(this)
       .append("id", this.id)
       .append("type", this.type)
       .append("transfer", this.transfer)
       .append("transferName", this.transferName)
       .append("transfered", this.transfered)
       .append("transferedName", this.transferedName)
       .append("reason", this.reason)
       .append("result", this.result)
       .append("var", this.var)
       .toString();
   }
}