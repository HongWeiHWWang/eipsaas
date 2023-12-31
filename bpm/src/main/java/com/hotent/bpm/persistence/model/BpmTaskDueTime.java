package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：任务期限统计 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-05-16 16:25:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("bpm_task_due_time")
public class BpmTaskDueTime extends BaseModel<BpmTaskDueTime>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5196430711796250612L;

	/**
	* 主键
	*/
	@TableId("id_")
	protected String id; 
	
	/**
	* 父id_
	*/
	@TableField("parent_id_")
	protected String parentId; 
	
	/**
	* 流程实例id
	*/
	@TableField("inst_id_")
	protected String instId; 
	
	/**
	* 任务id
	*/
	@TableField("task_id_")
	protected String taskId; 
	
	/**
	* 时间类型  工作日 worktime  日历日 caltime
	*/
	@TableField("date_type_")
	protected String dateType; 
	
	/**
	* 任务时间 (分钟)
	*/
	@TableField("due_time_")
	protected int dueTime; 
	
	/**
	 * 添加任务时间 (分钟)
	 */
	@TableField("add_due_time_")
	protected int addDueTime;
	
	/**
	* 任务开始时间
	*/
	@TableField("start_time_")
	protected LocalDateTime startTime; 
	
	/**
	* 到期时间
	*/
	@TableField("expiration_date_")
	protected LocalDateTime expirationDate; 
	
	/**
	* 剩余时间 (分钟)
	*/
	@TableField("remaining_time_")
	protected int remainingTime; 
	
	
	/**
	* 状态 是否有效  1有效   0无效   无效时需要审批人，审批通过才有效
	*/
	@TableField("status_")
	protected Short status; 
	
	/**
	* 审批人ID 
	*/
	@TableField("user_id_")
	protected String userId; 
	
	/**
	* 审批人姓名
	*/
	@TableField("user_name_")
	protected String userName; 
	
	/**
	* 备注
	*/
	@TableField("remark_")
	protected String remark; 
	
	/**
	* 附件ID
	*/
	@TableField("file_id_")
	protected String fileId; 
	
	/**
	* 是否最新的 1 是  0 否 
	*/
	@TableField("is_new_")
	protected Short isNew; 
	
	
	@TableField("create_time_")
	protected LocalDateTime createTime = LocalDateTime.now();

	
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
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 父id_
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}
	
	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	/**
	 * 返回 流程实例id
	 * @return
	 */
	public String getInstId() {
		return this.instId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * 返回 任务id
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}
	
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	
	/**
	 * 返回 时间类型  工作日 worktime  日历日 caltime
	 * @return
	 */
	public String getDateType() {
		return this.dateType;
	}
	
	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;
	}
	
	/**
	 * 返回 增加任务时间 (分钟)
	 * @return
	 */
	public int getAddDueTime() {
		return this.addDueTime;
	}
	
	public void setAddDueTime(int addDueTime) {
		this.addDueTime = addDueTime;
	}
	
	/**
	 * 返回 任务时间 (分钟)
	 * @return
	 */
	public int getDueTime() {
		return this.dueTime;
	}
	
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 返回 任务开始时间
	 * @return
	 */
	public LocalDateTime getStartTime() {
		return this.startTime;
	}
	
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	/**
	 * 返回 到期时间
	 * @return
	 */
	public LocalDateTime getExpirationDate() {
		return this.expirationDate;
	}
	
	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}
	
	/**
	 * 返回 剩余时间
	 * @return
	 */
	public int getRemainingTime() {
		return this.remainingTime;
	}
	
	public void setStatus(Short status) {
		this.status = status;
	}
	
	/**
	 * 返回 状态 是否有效  1有效   0无效   无效时需要审批人，审批通过才有效
	 * @return
	 */
	public Short getStatus() {
		return this.status;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 审批人ID 
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 返回 审批人姓名
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 返回 备注
	 * @return
	 */
	public String getRemark() {
		return this.remark;
	}
	
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	/**
	 * 返回 附件ID
	 * @return
	 */
	public String getFileId() {
		return this.fileId;
	}
	
	public void setIsNew(Short isNew) {
		this.isNew = isNew;
	}
	
	/**
	 * 返回 是否最新的 1 是  0 否 
	 * @return
	 */
	public Short getIsNew() {
		return this.isNew;
	}
	
	
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("parentId", this.parentId) 
		.append("instId", this.instId) 
		.append("taskId", this.taskId) 
		.append("dateType", this.dateType) 
		.append("dueTime", this.dueTime) 
		.append("startTime", this.startTime) 
		.append("expirationDate", this.expirationDate) 
		.append("remainingTime", this.remainingTime) 
		.append("createTime", this.createTime) 
		.append("status", this.status) 
		.append("userId", this.userId) 
		.append("userName", this.userName) 
		.append("remark", this.remark) 
		.append("fileId", this.fileId) 
		.append("isNew", this.isNew) 
		.toString();
	}
}