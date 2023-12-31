package com.hotent.portal.model;




import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 首页栏目 Model对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
@ApiModel(description="首页栏目 Model对象")
@TableName("portal_sys_column")
public class SysIndexColumn extends AutoFillModel<SysIndexColumn>{
	/**SERVICE方法*/
	public static short DATA_MODE_SERVICE = 0;
	/**自定义查询*/
	public static short DATA_MODE_QUERY = 1;
	/**WEBSERVICE方法*/
	public static short DATA_MODE_WEBSERVICE = 2;
	/**RESTFUL*/
	public static short DATA_MODE_RESTFUL = 3;
	
	/**栏目类型-一般栏目*/
	public static short  COLUMN_TYPE_COMMON =0;
	
	/**栏目类型-图表栏目*/
	public static short  COLUMN_TYPE_CHART =1;
	/**栏目类型-日历栏目*/
	public static short  COLUMN_TYPE_CALENDAR =2;
	/**栏目类型-滚动栏目*/
	public static short  COLUMN_TYPE_ROLL =3;
	
	// PC端
	public static final short  TYPE_PC =0;
	// 手机端
	public static final short  TYPE_MOBILE =1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4018816120529407191L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID")
	protected String  id;

	@ApiModelProperty(name="name", notes="栏目名称")
	@TableField("NAME")
	protected String  name;
	
	@ApiModelProperty(name="alias", notes="栏目别名")
	@TableField("ALIAS")
	protected String  alias;
 
	@ApiModelProperty(name="catalog", notes="栏目分类id")
	@TableField("CATALOG")
	protected String  catalog;
	
	@ApiModelProperty(name="catalogName", notes="栏目分类名称")
	@TableField("CATALOG_NAME")
	protected String catalogName;

	@ApiModelProperty(name="colType", notes="栏目类别(0:一般栏目  1:图表栏目  2:滚动栏目)", allowableValues="0,1,2")
	@TableField("COL_TYPE")
	protected Short  colType;

	@ApiModelProperty(name="dataMode", notes="数据加载方式(0:服务方法  1:自定义查询)", allowableValues="0,1")
	@TableField("DATA_MODE")
	protected Short  dataMode;

	@ApiModelProperty(name="dataFrom", notes="数据来源")
	@TableField("DATA_FROM")
	protected String  dataFrom;

	@ApiModelProperty(name="dataParam", notes="数据参数")
	@TableField("DATA_PARAM")
	protected String  dataParam;
	
	@ApiModelProperty(name="dsAlias", notes="数据别名")
	@TableField("DS_ALIAS")
	protected String  dsAlias;

	@ApiModelProperty(name="dsName", notes="数据源名称")
	@TableField("DS_NAME")
	protected String  dsName;

	@ApiModelProperty(name="colHeight", notes="栏目高度")
	@TableField("COL_HEIGHT")
	protected Long  colHeight;
	 
	@ApiModelProperty(name="colUrl", notes="栏目url")
	@TableField("COL_URL")
	protected String  colUrl;
	 
	@ApiModelProperty(name="templateHtml", notes="栏目模版")
	@TableField("TEMPLATE_HTML")
	protected String  templateHtml;
	 
	@ApiModelProperty(name="isPublic", notes="栏目类型(0:PC端  1:手机端)", allowableValues="0,1")
	@TableField("IS_PUBLIC")
	protected Short  isPublic;
	 
	@ApiModelProperty(name="orgId", notes="所属组织ID")
	@TableField("ORG_ID")
	protected String  orgId;
	
	@ApiModelProperty(name="supportRefesh", notes="是否支持刷新")
	@TableField("SUPPORT_REFESH")
	protected Short  supportRefesh;
 
	@ApiModelProperty(name="refeshTime", notes="刷新时间")
	@TableField("REFESH_TIME")
	protected Long  refeshTime;
	 
	@ApiModelProperty(name="showEffect", notes="展示效果")
	@TableField("SHOW_EFFECT")
	protected Short  showEffect;
	 
	@ApiModelProperty(name="memo", notes="描述")
	@TableField("MEMO")
	protected String  memo;
	
	@ApiModelProperty(name="needPage", notes="是否需要分页")
	@TableField("NEEDPAGE")
	protected Short needPage = 0;
	
	@ApiModelProperty(name="requestType", notes="请求地址类型：POST、GET（当数据加载方式为RESTFUL时）")
	@TableField("REQUEST_TYPE")
	protected String requestType;
	
	@ApiModelProperty(name="creator", notes="操作人")
	@TableField("CREATOR")
	protected String creator;
	
	@TableField("tenant_id_")
	@ApiModelProperty(name="tenantId",notes="租户id")
	private String tenantId;
	
	@ApiModelProperty(name="orgName", notes="组织名称，非数据字段")
	@TableField(exist=false)
	protected String orgName;
	
	@ApiModelProperty(name="displayRights", notes="是否有展示权限")
	@TableField(exist=false)
	protected boolean displayRights = true;
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 设置栏目名称
	 * @param name  栏目名称
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 返回 栏目名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 设置栏目名称
	 * @param alias  栏目名称
	 */
	public void setAlias(String alias){
		this.alias = alias;
	}
	
	/**
	 * 返回 栏目别名
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}
	
	/**
	 * 设置栏目分类
	 * @param catalog 栏目分类
	 */
	public void setCatalog(String catalog){
		this.catalog = catalog;
	}
	
	/**
	 * 返回 栏目分类
	 * @return
	 */
	public String getCatalog() {
		return this.catalog;
	}
	
	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * 返回 栏目类型(0：一般栏目、1：图表栏目、2、滚动栏目)
	 * @return
	 */
	public Short getColType() {
		return this.colType;
	}
	
	/**
	 * 返回  数据加载方式(0:服务方法;1:自定义查询)
	 * @return
	 */
	public Short getDataMode() {
		return dataMode;
	}
	
	/**
	 * 设置数据加载方式(0:服务方法;1:自定义查询)
	 * @param dataMode 数据加载方式(0:服务方法;1:自定义查询)
	 */
	public void setDataMode(Short dataMode) {
		this.dataMode = dataMode;
	}
	
	/**
	 * 设置栏目类型(0：一般栏目、1：图表栏目、2、滚动栏目)
	 * @param colType 栏目类型(0：一般栏目、1：图表栏目、2、滚动栏目)
	 */
	public void setColType(Short colType){
		this.colType = colType;
	}
	
	/**
	 * 设置数据别名
	 * @param dsAlias
	 */
	public void setDsAlias(String dsAlias){
		this.dsAlias = dsAlias;
	}
	
	/**
	 * 返回 数据别名
	 * @return
	 */
	public String getDsAlias() {
		return this.dsAlias;
	}
	
	/**
	 * 设置数据源名称
	 * @param dsName 数据源名称
	 */
	public void setDsName(String dsName){
		this.dsName = dsName;
	}
	
	/**
	 * 返回 数据源名称
	 * @return
	 */
	public String getDsName() {
		return this.dsName;
	}
	
	/**
	 * 设置数据来源
	 * @param dataFrom 数据来源
	 */
	public void setDataFrom(String dataFrom){
		this.dataFrom = dataFrom;
	}
	
	/**
	 * 返回 数据来源
	 * @return
	 */
	public String getDataFrom() {
		return this.dataFrom;
	}
	
	/**
	 * 设置栏目高度
	 * @param colHeight 栏目高度
	 */
	public void setColHeight(Long colHeight){
		this.colHeight = colHeight;
	}
	
	/**
	 * 返回 栏目高度
	 * @return
	 */
	public Long getColHeight() {
		return this.colHeight;
	}
	
	/**
	 * 设置栏目url
	 * @param colUrl
	 */
	public void setColUrl(String colUrl){
		this.colUrl = colUrl;
	}
	
	/**
	 * 返回 栏目url
	 * @return
	 */
	public String getColUrl() {
		return this.colUrl;
	}
	
	/**
	 * 设置栏目模版
	 * @param templateHtml 栏目模版
	 */
	public void setTemplateHtml(String templateHtml){
		this.templateHtml = templateHtml;
	}
	
	/**
	 * 返回 栏目模版
	 * @return
	 */
	public String getTemplateHtml() {
		return this.templateHtml;
	}
	
	/**
	 * 设置是否公共栏目
	 * @param isPublic 是否公共栏目
	 */
	public void setIsPublic(Short isPublic){
		this.isPublic = isPublic;
	}
	
	/**
	 * 返回 是否公共栏目
	 * @return
	 */
	public Short getIsPublic() {
		return this.isPublic;
	}
	
	/**
	 * 设置所属组织ID
	 * @param orgId 所属组织ID
	 */
	public void setOrgId(String orgId){
		this.orgId = orgId;
	}
	
	/**
	 * 返回 所属组织ID
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}
	
	/**
	 * 设置是否支持刷新
	 * @param supportRefesh 是否支持刷新
	 */
	public void setSupportRefesh(Short supportRefesh){
		this.supportRefesh = supportRefesh;
	}
	
	/**
	 * 返回 是否支持刷新
	 * @return
	 */
	public Short getSupportRefesh() {
		return this.supportRefesh;
	}
	
	/**
	 * 设置刷新时间
	 * @param refeshTime 刷新时间
	 */
	public void setRefeshTime(Long refeshTime){
		this.refeshTime = refeshTime;
	}
	
	/**
	 * 返回 刷新时间
	 * @return
	 */
	public Long getRefeshTime() {
		return this.refeshTime;
	}
	
	/**
	 * 设置展示效果
	 * @param showEffect 展示效果
	 */
	public void setShowEffect(Short showEffect){
		this.showEffect = showEffect;
	}
	
	/**
	 * 返回 展示效果
	 * @return
	 */
	public Short getShowEffect() {
		return this.showEffect;
	}
	
	/**
	 * 设置描述
	 * @param memo
	 */
	public void setMemo(String memo){
		this.memo = memo;
	}
	
	/**
	 * 返回 描述
	 * @return
	 */
	public String getMemo() {
		return this.memo;
	}
	
	/**
	 * 返回组织名称，非数据字段
	 * @return
	 */
   	public String getOrgName() {
		return orgName;
	}
   	
   	/**
   	 * 设置组织名称，非数据字段
   	 * @param orgName 组织名称，非数据字段
   	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	/**
	 * 返回数据参数
	 * @return
	 */
	public String getDataParam() {
		return dataParam;
	}
	
	/**
	 * 设置数据参数
	 * @param dataParam
	 */
	public void setDataParam(String dataParam) {
		this.dataParam = dataParam;
	}
	
	/**
	 * 返回是否需要分页
	 * @return
	 */
	public Short getNeedPage() {
		return needPage;
	}
	
	/**
	 * 设置是否需要分页
	 * @param needPage 是否需要分页
	 */
	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}
	
	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public boolean isDisplayRights() {
		return displayRights;
	}

	public void setDisplayRights(boolean displayRights) {
		this.displayRights = displayRights;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof SysIndexColumn)) 
		{
			return false;
		}
		SysIndexColumn rhs = (SysIndexColumn) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.name, rhs.name)
		.append(this.alias, rhs.alias)
		.append(this.catalog, rhs.catalog)
		.append(this.colType, rhs.colType)
		.append(this.dataMode, rhs.dataMode)
		.append(this.dataFrom, rhs.dataFrom)
		.append(this.dsAlias, rhs.dsAlias)
		.append(this.dsName, rhs.dsName)
		.append(this.colHeight, rhs.colHeight)
		.append(this.colUrl, rhs.colUrl)
		.append(this.templateHtml, rhs.templateHtml)
		.append(this.isPublic, rhs.isPublic)
		.append(this.orgId, rhs.orgId)
		.append(this.supportRefesh, rhs.supportRefesh)
		.append(this.refeshTime, rhs.refeshTime)
		.append(this.showEffect, rhs.showEffect)
		.append(this.requestType, rhs.requestType)
		.append(this.memo, rhs.memo)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.name) 
		.append(this.alias) 
		.append(this.catalog) 
		.append(this.colType)
		.append(this.dataMode)
		.append(this.dataFrom) 
		.append(this.dsAlias) 
		.append(this.dsName) 
		.append(this.colHeight) 
		.append(this.colUrl) 
		.append(this.templateHtml) 
		.append(this.isPublic) 
		.append(this.orgId) 
		.append(this.supportRefesh) 
		.append(this.refeshTime) 
		.append(this.showEffect) 
		.append(this.requestType)
		.append(this.memo) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("alias", this.alias) 
		.append("catalog", this.catalog) 
		.append("colType", this.colType)
		.append("dataMode", this.dataMode)
		.append("dataFrom", this.dataFrom) 
		.append("dsAlias", this.dsAlias) 
		.append("dsName", this.dsName) 
		.append("colHeight", this.colHeight) 
		.append("colUrl", this.colUrl) 
		.append("templateHtml", this.templateHtml) 
		.append("isPublic", this.isPublic) 
		.append("orgId", this.orgId) 
		.append("supportRefesh", this.supportRefesh) 
		.append("refeshTime", this.refeshTime) 
		.append("showEffect", this.showEffect) 
		.append("requestType", this.requestType)
		.append("memo", this.memo) 
		.toString();
	}
   
  

}