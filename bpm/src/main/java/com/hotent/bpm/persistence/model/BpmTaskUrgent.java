package com.hotent.bpm.persistence.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 人工催办
 * <pre> 
 * 描述：人工催办 实体对象
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-09-02 15:26:38
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @ApiModel(value = "BpmTaskUrgent",description = "人工催办表")
 @TableName("bpm_task_urgent")
public class BpmTaskUrgent extends BaseModel<BpmTaskUrgent>{

	private static final long serialVersionUID = 1L;
	
	public static final String TYPR_MAIL = "mail";
	
	public static final String TYPR_SMS = "sms";
	
	public static final String TYPR_SMS_APPROVAL = "smsApproval";
	
	@TableId("id_")
	@ApiModelProperty(value="主键ID")
	protected String id; 
	
	@TableField("task_id_")
	@ApiModelProperty(value="任务ID")
	protected String taskId; 
	
	
	@TableField("node_name_")
	@ApiModelProperty(value="节点名称")
	protected String nodeName;
	
	
	@TableField("inst_id_")
	@ApiModelProperty(value="流程实例ID")
	protected String instId; 
	
	
	@TableField("subject_")
	@ApiModelProperty(value="流程标题")
	protected String subject; 
	
	
	@TableField("content_")
	@ApiModelProperty(value="催办内容")
	protected String content; 
	
	
	@TableField("urgrnt_date_")
	@ApiModelProperty(value="催办时间")
	protected LocalDateTime urgrntDate;

	
	@TableField("promoter_id_")
	@ApiModelProperty(value="催办人ID")
    protected String promoterId;

	
	@TableField("promoter_")
	@ApiModelProperty(value="催办人")
	protected String promoter;

	
	@TableField("appointee_id_")
	@ApiModelProperty(value="被催办人ID")
    protected String appointeeId;
	
	@TableField("appointee_")
     @ApiModelProperty(value="被催办人")
	protected String appointee; 
	
	@TableField("type_")
	@ApiModelProperty(value="催办类型")
	protected String type;
	
	@TableField("appointee_secretary_conf_")
	@ApiModelProperty(value="催办的领导和秘书的关系map，方便秘书回复时确定代哪个领导审批的")
    protected String appointeeSecretaryConf;
	
	@TableField(exist=false)	
    @ApiModelProperty(value="催办的节点id")
	protected String nodeId;
	
	@TableField(exist=false)	
    @ApiModelProperty(value="被催办人集合")
    protected Map<String,Object> appointeeObj;
	
	@TableField(exist=false)
    @ApiModelProperty(value="是否催办秘书")
	protected boolean appointeeSecretary; 
    public Map<String, Object> getAppointeeObj() {
        return appointeeObj;
    }

    public void setAppointeeObj(Map<String, Object> appointeeObj) {
        this.appointeeObj = appointeeObj;
    }

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
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * 返回 任务ID
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}

     public String getNodeName() {
         return nodeName;
     }

     public void setNodeName(String nodeName) {
         this.nodeName = nodeName;
     }

     public void setInstId(String instId) {
		this.instId = instId;
	}
	
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	public String getInstId() {
		return this.instId;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 返回 流程标题
	 * @return
	 */
	public String getSubject() {
		return this.subject;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 返回 催办内容
	 * @return
	 */
	public String getContent() {
		return this.content;
	}
	
	public void setUrgrntDate(LocalDateTime urgrntDate) {
		this.urgrntDate = urgrntDate;
	}
	
	/**
	 * 返回 催办时间
	 * @return
	 */
	public LocalDateTime getUrgrntDate() {
		return this.urgrntDate;
	}
	
	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}
	
	/**
	 * 返回 催办人
	 * @return
	 */
	public String getPromoter() {
		return this.promoter;
	}
	
	public void setAppointee(String appointee) {
		this.appointee = appointee;
	}
	
	/**
	 * 返回 被催办人
	 * @return
	 */
	public String getAppointee() {
		return this.appointee;
	}

     public String getPromoterId() {
         return promoterId;
     }

     public void setPromoterId(String promoterId) {
         this.promoterId = promoterId;
     }

     public String getAppointeeId() {
         return appointeeId;
     }

     public void setAppointeeId(String appointeeId) {
         this.appointeeId = appointeeId;
     }

     public String getType() {
         return type;
     }

     public void setType(String type) {
         this.type = type;
     }

     
     
     public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isAppointeeSecretary() {
		return appointeeSecretary;
	}

	public void setAppointeeSecretary(boolean appointeeSecretary) {
		this.appointeeSecretary = appointeeSecretary;
	}

	public String getAppointeeSecretaryConf() {
		return appointeeSecretaryConf;
	}

	public void setAppointeeSecretaryConf(String appointeeSecretaryConf) {
		this.appointeeSecretaryConf = appointeeSecretaryConf;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("taskId", this.taskId) 
		.append("nodeId", this.nodeName)
		.append("instId", this.instId) 
		.append("subject", this.subject) 
		.append("content", this.content) 
		.append("urgrntDate", this.urgrntDate) 
		.append("promoterId", this.promoterId)
        .append("promoter", this.promoter)
        .append("appointeeId", this.appointeeId)
        .append("appointee", this.appointee)
		.append("type", this.type)
		.toString();
	}
}