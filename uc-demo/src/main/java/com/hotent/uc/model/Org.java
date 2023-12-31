package com.hotent.uc.model;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;

 


 /**
 * 
 * <pre> 
 * 描述：组织架构 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-28 15:13:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class Org extends UcBaseModel  implements IGroup{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7138977532880036358L;

	/**
	* 主键
	*/
	@ApiModelProperty(name="id",notes="组织id")
	protected String id; 
	
	/**
	* name_
	*/
	@ApiModelProperty(name="name",notes="组织名称")
	protected String name; 
	
	/**
	* prent_id_
	*/
	@ApiModelProperty(name="parentId",notes="组织父节点id")
	protected String parentId; 
	
	/**
	* code_
	*/
	@ApiModelProperty(name="code",notes="组织编码")
	protected String code; 
	
	/**
	* 级别
	*/
	@ApiModelProperty(name="grade",notes="组织级别")
	protected String grade; 
	
	/**
	 * 维度Id
	 */
	@ApiModelProperty(name="demId",notes="维度id")
	protected String demId;
	
	@ApiModelProperty(name="orderNo",notes="序号")
	protected Long orderNo; 
	
	/**
	 * 上级组织名称
	 */
	@ApiModelProperty(name="parentOrgName",notes="上级组织名称")
	protected  String parentOrgName;
	
	/**
	 * 是否主组织。
	 */
	@ApiModelProperty(name="isMaster",notes="是否主组织")
	private int isMaster=0;
	
	/**
	/**
	 * 路径
	 */
	@ApiModelProperty(name="path",notes="路径")
	protected String path;
	
	/**
	 * 组织路径名
	 */
	@ApiModelProperty(name="pathName",notes="组织路径名")
	protected String pathName;
	/**
	 * 是否有子节点   否0  是1  
	 */
	@ApiModelProperty(name="isIsParent",notes="是否有子节点   否0  是1")
	protected int isIsParent = 0;
	
	/**
	 * 组织参数
	 */
	@ApiModelProperty(name="params",notes="组织参数（获取单个组织时才会有值）")
	protected Map<String,Object> params;
	
	/**
	 * 维度名称
	 */
	@ApiModelProperty(name="demName",notes="所属维度")
	protected String demName;
	/**
	 * OA关联ID
	 */
	@ApiModelProperty(name="refId",notes="OA关联ID")
	protected String refId;
	
	/**
	 * 组织用户关联id
	 */
	@ApiModelProperty(name="orgUserId",notes="组织用户关联id")
	protected String orgUserId;

	@ApiModelProperty(name="limitNum",notes="组织限编用户数量(0:不受限制)")
	protected Integer limitNum=0;

	@ApiModelProperty(name="nowNum",notes="组织现编用户数量")
    protected Integer nowNum;

	@ApiModelProperty(name="exceedLimitNum",notes="是否允许超过限编(0:允许；1:不允许)")
    protected Integer exceedLimitNum=0;

     public Integer getExceedLimitNum() {
         return exceedLimitNum;
     }

     public void setExceedLimitNum(Integer exceedLimitNum) {
         this.exceedLimitNum = exceedLimitNum;
     }

     public Integer getLimitNum() {
         return limitNum;
     }

     public void setLimitNum(Integer limitNum) {
         this.limitNum = limitNum;
     }

     public Integer getNowNum() {
         return nowNum;
     }

     public void setNowNum(Integer nowNum) {
         this.nowNum = nowNum;
     }

     public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	public String getPathName() {
		return pathName;
	}

	public boolean isIsParent() {
		return isIsParent==1;
	}

	public void setIsParent(int isIsParent) {
		this.isIsParent = isIsParent;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getParentOrgName() {
		return this.parentOrgName;
	}
	
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 name_
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 prent_id_
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDemId() {
		return demId;
	}

	public void setDemId(String demId) {
		this.demId = demId;
	}

	/**
	 * 返回 code_
	 * @return
	 */
	public String getCode() {
		return this.code;
	}
	
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	/**
	 * 返回 级别
	 * @return
	 */
	public String getGrade() {
		return this.grade;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("parentId", this.parentId) 
		.append("code", this.code) 
		.append("grade", this.grade)
		.append("demId",this.demId)
		.append("path",this.path)
		.append("pathName",this.pathName)
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

	public String getGroupId() {
		return this.id;
	}

	public String getGroupCode() {
		return this.code;
	}

	public Long getOrderNo() {
		 
		return this.orderNo;
	}

	public String getPath() {
		return this.path;
	}

	public int getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(int isMaster) {
		this.isMaster = isMaster;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getDemName() {
		return demName;
	}

	public void setDemName(String demName) {
		this.demName = demName;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	@Override
	public String getIdentityType() {
		return IdentityType.GROUP;
	}

	@Override
	public String getGroupType() {
		return GroupTypeConstant.ORG.key();
	}

	@Override
	public GroupStructEnum getStruct() {
		return null;
	}
	
	
}