package com.hotent.uc.params.org;

import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.util.OperateLogUtil;
import com.hotent.uc.util.UpdateCompare;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 *<pre>组织视图</pre>
 */
@ApiModel
public class OrgVo implements UpdateCompare {
	
	@ApiModelProperty(name="name",notes="组织名称",required=true)
	private String name;
	
	@ApiModelProperty(name="code",notes="组织代码",required=true)
	private String code;
	
	@ApiModelProperty(name="parentId",notes="父组织id",example="0")
	private String parentId;
	
	@ApiModelProperty(name="grade",notes="级别")
	private String grade;
	
	@ApiModelProperty(name="demId",notes="维度id",required=true)
	private String demId;
	
	@ApiModelProperty(name="orderNo",notes="排序号")
	private Long orderNo;

    @ApiModelProperty(name="limitNum",notes="组织限编用户数量(0:不受限制)")
    protected Integer limitNum=0;

    @ApiModelProperty(name="nowNum",notes="组织现编用户数量")
    protected Integer nowNum;

    @ApiModelProperty(name="exceedLimitNum",notes="是否允许超过限编(0:允许；1:不允许)")
    protected Integer exceedLimitNum=0;

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

    public Integer getExceedLimitNum() {
        return exceedLimitNum;
    }

    public void setExceedLimitNum(Integer exceedLimitNum) {
        this.exceedLimitNum = exceedLimitNum;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDemId() {
		return demId;
	}

	public void setDemId(String demId) {
		this.demId = demId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	public String toString() {
		return "{"
				+ "\""+"name"+"\""+":"+"\""+this.name+"\","
				+"\""+"code"+"\""+":"+"\""+this.code+"\","
				+"\""+"parentId"+"\""+":"+"\""+this.parentId+"\","
				+"\""+"grade"+"\""+":"+"\""+this.grade+"\","
				+"\""+"demId"+"\""+":"+"\""+this.demId+"\","
				+"\""+"orderNo"+"\""+":"+"\""+this.orderNo+"\""
				+ "}";
	}
	
	@Override
	public String compare() throws Exception {
	    OrgManager service =	AppUtil.getBean(OrgManager.class);
	    Org oldVo=service.getByCode(this.code);
		return OperateLogUtil.compare(this,changeVo(oldVo));
	}


	public OrgVo changeVo(Org oldVo) {
		OrgVo newVo=new OrgVo();
		if (BeanUtils.isEmpty(newVo)) return newVo;
		newVo.setCode(oldVo.getCode());
		newVo.setDemId(oldVo.getDemId());
		newVo.setGrade(oldVo.getGrade());
		newVo.setName(oldVo.getName());
		newVo.setOrderNo(oldVo.getOrderNo());
		newVo.setParentId(oldVo.getParentId());
		return newVo;
	}
}
