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
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-02 18:17:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @ApiModel(value = "PressRelease",description = "新闻公告") 
 @TableName("w_xwgg")
public class PressRelease extends BaseModel<PressRelease>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(value="外键")
	@TableField("REF_ID_")
	protected String refId; 
	
	@ApiModelProperty(value="是否需要审批")
	@TableField("F_sfxysp")
	protected String FSfxysp; 
	
	@ApiModelProperty(value="审批人")
	@TableField("F_spr")
	protected String FSpr; 
	
	@ApiModelProperty(value="发布方式")
	@TableField("F_fbfs")
	protected String FFbfs; 
	
	@ApiModelProperty(value="轮播图所属分类")
	@TableField("F_lbtssfl")
	protected String FLbtssfl; 
	
	@ApiModelProperty(value="告示版所属分类")
	@TableField("F_gsbssfl")
	protected String FGsbssfl; 
	
	@ApiModelProperty(value="标题")
	@TableField("F_bt")
	protected String FBt; 
	
	@ApiModelProperty(value="创建时间")
	@TableField("F_cjsj")
	protected java.util.Date FCjsj; 
	
	@ApiModelProperty(value="创建人")
	@TableField("F_cjr")
	protected String FCjr; 
	
	@ApiModelProperty(value="是否外部链接")
	@TableField("F_sfwblj")
	protected String FSfwblj; 
	
	@ApiModelProperty(value="内容url")
	@TableField("F_nrurl")
	protected String FNrurl; 
	
	@ApiModelProperty(value="内容")
	@TableField("F_nr")
	protected String FNr; 
	
	@ApiModelProperty(value="排序")
	@TableField("F_px")
	protected Integer FPx; 
	
	@ApiModelProperty(value="有效时间")
	@TableField("F_yxsj")
	protected java.util.Date FYxsj; 
	
	@ApiModelProperty(value="审批人账号")
	@TableField("F_sprzh")
	protected String FSprzh; 
	
	@ApiModelProperty(value="表单数据版本")
	@TableField("F_form_data_rev_")
	protected Long FFormDataRev; 
	
	@ApiModelProperty(value="轮播展示图片")
	@TableField("F_lbzstp")
	protected String FLbzstp; 
	
	@ApiModelProperty(value="标题描述")
	@TableField("F_btms")
	protected String FBtms; 
	
	@ApiModelProperty(value="页面嵌套高度")
	@TableField("F_ymqtgd")
	protected String FYmqtgd;

	@ApiModelProperty(value = "是否初始化数据 0:不是 1:是")
	@TableField("F_status")
	protected int FStatus;

	 public int getFStatus() {
		 return FStatus;
	 }

	 public void setFStatus(int FStatus) {
		 this.FStatus = FStatus;
	 }

	 public String getFYmqtgd() {
		return FYmqtgd;
	}

	public void setFYmqtgd(String fYmqtgd) {
		FYmqtgd = fYmqtgd;
	}

	public String getFBtms() {
		return FBtms;
	}

	public void setFBtms(String fBtms) {
		FBtms = fBtms;
	}

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
	
	public void setFFbfs(String FFbfs) {
		this.FFbfs = FFbfs;
	}
	
	/**
	 * 返回 发布方式
	 * @return
	 */
	public String getFFbfs() {
		return this.FFbfs;
	}
	
	public void setFLbtssfl(String FLbtssfl) {
		this.FLbtssfl = FLbtssfl;
	}
	
	/**
	 * 返回 轮播图所属分类
	 * @return
	 */
	public String getFLbtssfl() {
		return this.FLbtssfl;
	}
	
	public void setFGsbssfl(String FGsbssfl) {
		this.FGsbssfl = FGsbssfl;
	}
	
	/**
	 * 返回 告示版所属分类
	 * @return
	 */
	public String getFGsbssfl() {
		return this.FGsbssfl;
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
	
	public void setFCjsj(java.util.Date FCjsj) {
		this.FCjsj = FCjsj;
	}
	
	/**
	 * 返回 创建时间
	 * @return
	 */
	public java.util.Date getFCjsj() {
		return this.FCjsj;
	}
	
	public void setFCjr(String FCjr) {
		this.FCjr = FCjr;
	}
	
	/**
	 * 返回 创建人
	 * @return
	 */
	public String getFCjr() {
		return this.FCjr;
	}
	
	public void setFSfwblj(String FSfwblj) {
		this.FSfwblj = FSfwblj;
	}
	
	/**
	 * 返回 是否外部链接
	 * @return
	 */
	public String getFSfwblj() {
		return this.FSfwblj;
	}
	
	public void setFNrurl(String FNrurl) {
		this.FNrurl = FNrurl;
	}
	
	/**
	 * 返回 内容url
	 * @return
	 */
	public String getFNrurl() {
		return this.FNrurl;
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
	
	public void setFPx(Integer FPx) {
		this.FPx = FPx;
	}
	
	/**
	 * 返回 排序
	 * @return
	 */
	public Integer getFPx() {
		return this.FPx;
	}
	
	public void setFYxsj(java.util.Date FYxsj) {
		this.FYxsj = FYxsj;
	}
	
	/**
	 * 返回 有效时间
	 * @return
	 */
	public java.util.Date getFYxsj() {
		return this.FYxsj;
	}
	
	public void setFSprzh(String FSprzh) {
		this.FSprzh = FSprzh;
	}
	
	/**
	 * 返回 审批人账号
	 * @return
	 */
	public String getFSprzh() {
		return this.FSprzh;
	}
	
	public void setFFormDataRev(Long FFormDataRev) {
		this.FFormDataRev = FFormDataRev;
	}
	
	/**
	 * 返回 表单数据版本
	 * @return
	 */
	public Long getFFormDataRev() {
		return this.FFormDataRev;
	}
	
	public void setFLbzstp(String FLbzstp) {
		this.FLbzstp = FLbzstp;
	}
	
	/**
	 * 返回 轮播展示图片
	 * @return
	 */
	public String getFLbzstp() {
		return this.FLbzstp;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("refId", this.refId) 
		.append("FSfxysp", this.FSfxysp) 
		.append("FSpr", this.FSpr) 
		.append("FFbfs", this.FFbfs) 
		.append("FLbtssfl", this.FLbtssfl) 
		.append("FGsbssfl", this.FGsbssfl) 
		.append("FBt", this.FBt) 
		.append("FCjsj", this.FCjsj) 
		.append("FCjr", this.FCjr) 
		.append("FSfwblj", this.FSfwblj) 
		.append("FNrurl", this.FNrurl) 
		.append("FNr", this.FNr) 
		.append("FPx", this.FPx) 
		.append("FYxsj", this.FYxsj) 
		.append("FSprzh", this.FSprzh) 
		.append("FFormDataRev", this.FFormDataRev) 
		.append("FLbzstp", this.FLbzstp)
		.append("FStatus", this.FStatus)
		.toString();
	}
}