package com.hotent.sys.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 系统操作日志
 * <pre> 
 * 描述：系统操作日志 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 10:59:25
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("portal_sys_logs")
@ApiModel(value = "SysLogs",description = "系统操作日志") 
public class SysLogs extends BaseModel<SysLogs>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty(value="操作名称")
	@TableField("ope_name_")
	protected String opeName; 
	
	@ApiModelProperty(value="执行时间")
	@TableField("execution_time_")
	protected LocalDateTime executionTime; 
	
	@ApiModelProperty(value="执行人")
	@TableField("executor_")
	protected String executor; 
	
	@ApiModelProperty(value="ip地址")
	@TableField("ip_")
	protected String ip; 
	
	@ApiModelProperty(value="日志类型")
	@TableField("log_type_")
	protected String logType; 
	
	@ApiModelProperty(value="模块")
	@TableField("module_type_")
	protected String moduleType; 
	
	@ApiModelProperty(value="请求url地址")
	@TableField("req_url_")
	protected String reqUrl; 
	
	@ApiModelProperty(value="操作内容")
	@TableField("ope_content_")
	protected String opeContent; 
	
	public SysLogs(){
		
	}
	
	public SysLogs(String opeName,LocalDateTime executionTime,String executor,String ip,String logType,String moduleType,String reqUrl,String opeContent){
		this.opeName       = opeName;
		this.executionTime = executionTime;
		this.executor      = executor;
		this.ip            = ip;
		this.logType       = logType;
		this.moduleType    = moduleType;
		this.reqUrl        = reqUrl;
		this.opeContent    =  opeContent;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 编号
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setOpeName(String opeName) {
		this.opeName = opeName;
	}
	
	/**
	 * 返回 操作名称
	 * @return
	 */
	public String getOpeName() {
		return this.opeName;
	}
	
	public void setExecutionTime(LocalDateTime executionTime) {
		this.executionTime = executionTime;
	}
	
	/**
	 * 返回 执行时间
	 * @return
	 */
	public LocalDateTime getExecutionTime() {
		return this.executionTime;
	}
	
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	
	/**
	 * 返回 执行人
	 * @return
	 */
	public String getExecutor() {
		return this.executor;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * 返回 ip地址
	 * @return
	 */
	public String getIp() {
		return this.ip;
	}
	
	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	/**
	 * 返回 日志类型
	 * @return
	 */
	public String getLogType() {
		return this.logType;
	}
	
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	
	/**
	 * 返回 模块
	 * @return
	 */
	public String getModuleType() {
		return this.moduleType;
	}
	
	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	
	public void setOpeContent(String opeContent) {
		this.opeContent = opeContent;
	}
	
	/**
	 * 返回 操作内容
	 * @return
	 */
	public String getOpeContent() {
		return this.opeContent;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("opeName", this.opeName) 
		.append("executionTime", this.executionTime) 
		.append("executor", this.executor) 
		.append("ip", this.ip) 
		.append("logType", this.logType) 
		.append("moduleType", this.moduleType) 
		.append("opeContent", this.opeContent) 
		.toString();
	}
}