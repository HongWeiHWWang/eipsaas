package com.hotent.uc.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.util.OrgUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 
 * <pre> 
 * 描述：SYS_ORGOPERATE_LOG 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-12-11 12:02:11
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("UC_OPERATE_LOG")
@ApiModel(description="用户操作日志")
public class OperateLog extends UcBaseModel<OperateLog>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5927017979901650993L;

	/**
	* 主键
	*/  
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="日志id")
	protected String id; 
	
	/**
	* 请求ip地址
	*/
	@TableField("REQ_IP_")
	@ApiModelProperty(name="reqIp",notes="请求ip地址")
	protected String reqIp; 
	
	/**
	* 请求类型
	*/
	@TableField("REQ_TYPE_")
	@ApiModelProperty(name="reqType",notes="请求类型")
	protected String reqType; 
	
	/**
	* 请求URL
	*/
	@TableField("REQ_URL_")
	@ApiModelProperty(name="reqUrl",notes="请求URL")
	protected String reqUrl; 
	
	/**
	* 请求参数
	*/
	@TableField("REQ_PARAM_")
	@ApiModelProperty(name="reqParam",notes="请求参数")
	protected String reqParam; 
	
	/**
	* startTime
	*/
	@TableField("START_TIME_")
	@ApiModelProperty(name="startTime",notes="请求开始时间")
	protected LocalDateTime startTime; 
	

	/**
	* endTime
	*/
	@TableField("END_TIME_")
	@ApiModelProperty(name="endTime",notes="请求结束时间")
	protected LocalDateTime endTime; 
	
	
	/**
	* operatorName
	*/
	@TableField("OPERATOR_NAME_")
	@ApiModelProperty(name="operatorName",notes="操作人姓名")
	protected String operatorName; 
	
	/**
	* note
	*/
	@TableField("NOTE_")
	@ApiModelProperty(name="note",notes="请求方法说明")
	protected String note; 
	
	/**
	* success
	*/
	@TableField("SUCCESS_")
	@ApiModelProperty(name="success",notes="是否成功")
	protected int success; 
	
	/**
	* failReason
	*/
	@TableField("FAIL_REASON_")
	@ApiModelProperty(name="failReason",notes="失败原因")
	protected String failReason;
	
	public OperateLog(int success,String reqType,String reqUrl, String note, String reqParam, String operatorName, String failReason){
		User user =OrgUtil.getCurrentUser();
		if (BeanUtils.isNotEmpty(user)) {
			this.operatorName = user.getFullname();
		}
		this.id = UniqueIdUtil.getSuid();
		this.success = success;
		this.reqType = reqType;
		this.reqUrl = reqUrl;
		this.note = note;
		this.reqParam = reqParam;
		this.failReason = failReason;
		this.startTime = LocalDateTime.now();
	}
	
	public OperateLog(){

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReqIp() {
		return reqIp;
	}

	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getReqParam() {
		return reqParam;
	}

	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getOperatorName() {
		return operatorName;
	}
	public String getOperatorName(String name) {
		return operatorName;
	}
	public void setOperatorName() {
		User user =OrgUtil.getCurrentUser();
		if (BeanUtils.isNotEmpty(user)) {
			this.operatorName = user.getFullname();
		}
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	} 
	
}