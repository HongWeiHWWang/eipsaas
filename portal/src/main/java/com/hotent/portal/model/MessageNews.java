package com.hotent.portal.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 新闻公告
 * <pre> 
 * 描述：新闻公告 实体对象
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-20 16:04:35
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@ApiModel(value = "MessageNews",description = "新闻公告") 
@TableName("w_xwgg")
public class MessageNews extends BaseModel<MessageNews>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(value="外键")
	@TableField("REF_ID_")
	protected String refId; 
	
	@ApiModelProperty(value="标题")
	@TableField("F_bt")
	protected String FBt; 
	
	@ApiModelProperty(value="分类")
	@TableField("F_fl")
	protected String FFl; 
	
	@ApiModelProperty(value="内容")
	@TableField("F_nr")
	protected String FNr; 
	
	@ApiModelProperty(value="附件")
	@TableField("F_fj")
	protected String FFj; 
	
	@ApiModelProperty(value="置顶")
	@TableField("F_zd")
	protected String FZd; 
	
	@ApiModelProperty(value="作者")
	@TableField("F_zz")
	protected String FZz; 
	
	@ApiModelProperty(value="开始时间")
	@TableField("F_kssj")
	protected java.util.Date FKssj; 
	
	@ApiModelProperty(value="结束时间")
	@TableField("F_jssj")
	protected java.util.Date FJssj; 
	
	@ApiModelProperty(value="是否需要审批")
	@TableField("F_sfxysp")
	protected String FSfxysp; 
	
	@ApiModelProperty(value="审批人")
	@TableField("F_spr")
	protected String FSpr; 
	
	@ApiModelProperty(value="审批人ID")
	@TableField("F_sprid")
	protected String FSprid; 
	
	@ApiModelProperty(value="用户ID")
	@TableField("F_yhid")
	protected String FYhid; 
	
	@ApiModelProperty(value="缩略图")
	@TableField("F_slt")
	protected String FSlt; 
	
	@ApiModelProperty(value="查看权限设置")
	@TableField("F_ckqxsz")
	protected String FCkqxsz; 
	
	@ApiModelProperty(value="审批状态1。审批中。2.审批通过")
	@TableField("F_status")
	protected String FStatus; 
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	/**
	 * 返回 外键
	 * @return
	 */
	public String getRefId() {
		return this.refId;
	}
	
	public void setFBt(String FBt) {
		this.FBt = FBt;
	}
	
	/**
	 * 返回 标题
	 * @return
	 */
	public String getFBt() {
		return this.FBt;
	}
	
	public void setFFl(String FFl) {
		this.FFl = FFl;
	}
	
	/**
	 * 返回 分类
	 * @return
	 */
	public String getFFl() {
		return this.FFl;
	}
	
	public void setFNr(String FNr) {
		this.FNr = FNr;
	}
	
	/**
	 * 返回 内容
	 * @return
	 */
	public String getFNr() {
		return this.FNr;
	}
	
	public void setFFj(String FFj) {
		this.FFj = FFj;
	}
	
	/**
	 * 返回 附件
	 * @return
	 */
	public String getFFj() {
		return this.FFj;
	}
	
	public void setFZd(String FZd) {
		this.FZd = FZd;
	}
	
	/**
	 * 返回 置顶
	 * @return
	 */
	public String getFZd() {
		return this.FZd;
	}
	
	public void setFZz(String FZz) {
		this.FZz = FZz;
	}
	
	/**
	 * 返回 作者
	 * @return
	 */
	public String getFZz() {
		return this.FZz;
	}
	
	public void setFKssj(java.util.Date FKssj) {
		this.FKssj = FKssj;
	}
	
	/**
	 * 返回 开始时间
	 * @return
	 */
	public java.util.Date getFKssj() {
		return this.FKssj;
	}
	
	public void setFJssj(java.util.Date FJssj) {
		this.FJssj = FJssj;
	}
	
	/**
	 * 返回 结束时间
	 * @return
	 */
	public java.util.Date getFJssj() {
		return this.FJssj;
	}
	
	public void setFSfxysp(String FSfxysp) {
		this.FSfxysp = FSfxysp;
	}
	
	/**
	 * 返回 是否需要审批
	 * @return
	 */
	public String getFSfxysp() {
		return this.FSfxysp;
	}
	
	public void setFSpr(String FSpr) {
		this.FSpr = FSpr;
	}
	
	/**
	 * 返回 审批人
	 * @return
	 */
	public String getFSpr() {
		return this.FSpr;
	}
	
	public void setFSprid(String FSprid) {
		this.FSprid = FSprid;
	}
	
	/**
	 * 返回 审批人ID
	 * @return
	 */
	public String getFSprid() {
		return this.FSprid;
	}
	
	public void setFYhid(String FYhid) {
		this.FYhid = FYhid;
	}
	
	/**
	 * 返回 用户ID
	 * @return
	 */
	public String getFYhid() {
		return this.FYhid;
	}
	
	public void setFSlt(String FSlt) {
		this.FSlt = FSlt;
	}
	
	/**
	 * 返回 缩略图
	 * @return
	 */
	public String getFSlt() {
		return this.FSlt;
	}
	
	public void setFCkqxsz(String FCkqxsz) {
		this.FCkqxsz = FCkqxsz;
	}
	
	/**
	 * 返回 查看权限设置
	 * @return
	 */
	public String getFCkqxsz() {
		return this.FCkqxsz;
	}
	
	public void setFStatus(String FStatus) {
		this.FStatus = FStatus;
	}
	
	/**
	 * 返回 审批状态1。审批中。2.审批通过
	 * @return
	 */
	public String getFStatus() {
		return this.FStatus;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("refId", this.refId) 
		.append("FBt", this.FBt) 
		.append("FFl", this.FFl) 
		.append("FNr", this.FNr) 
		.append("FFj", this.FFj) 
		.append("FZd", this.FZd) 
		.append("FZz", this.FZz) 
		.append("FKssj", this.FKssj) 
		.append("FJssj", this.FJssj) 
		.append("FSfxysp", this.FSfxysp) 
		.append("FSpr", this.FSpr) 
		.append("FSprid", this.FSprid) 
		.append("FYhid", this.FYhid) 
		.append("FSlt", this.FSlt) 
		.append("FCkqxsz", this.FCkqxsz) 
		.append("FStatus", this.FStatus) 
		.toString();
	}
}