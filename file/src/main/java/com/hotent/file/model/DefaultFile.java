package com.hotent.file.model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.attachment.Attachment;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 对象功能:附件 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:miao
 * 创建时间:2014-04-04 08:54:40
 */
@TableName("portal_sys_file")
@ApiModel(description="附件信息")
public class DefaultFile extends AutoFillModel<DefaultFile> implements Attachment{
	private static final long serialVersionUID = 1L;
	@TableField(exist=false)
	public static Short FILE_DEL = 1;//文件已经删除
	@TableField(exist=false)
	public static Short FILE_NOT_DEL = 0;//文件存在
	@TableField(exist=false)
	public static String FILE_UPLOAD_UNKNOWN = "unknown";//匿名用户
	@TableField(exist=false)
	public static String SAVE_TYPE_FOLDER="folder";//基于磁盘
	@TableField(exist=false)
	public static String SAVE_TYPE_DTABASE="database";//基于数据库
	@TableField(exist=false)
	public static String SAVE_TYPE_FTP="ftp";//FTP服务器

	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id;

	@ApiModelProperty(name="xbTypeId", notes="附件分类ID")
	@TableField("XB_TYPE_ID_")
	protected String  xbTypeId;

	@ApiModelProperty(name="fileName", notes="文件名")
	@TableField("FILE_NAME_")
	protected String  fileName;

	@ApiModelProperty(name="fileType", notes="附件类型(mail:邮件附件  user:用户信息附件)", allowableValues="mail,user")
	@TableField("FILE_TYPE_")
	protected String  fileType;

	@ApiModelProperty(name="storeType", notes="存储类型(folder:基于磁盘  database:基于数据库 ftp:FTP服务器)", allowableValues="folder,database,ftp")
	@TableField("STORE_TYPE_")
	protected String  storeType;

	@ApiModelProperty(name="filePath", notes="文件路径")
	@TableField("FILE_PATH_")
	protected String  filePath;

	@ApiModelProperty(name="bytes", notes="文件二进制数据")
	@TableField("BYTES_")
	protected byte[]  bytes;

	@ApiModelProperty(name="byteCount", notes="总字节数")
	@TableField("BYTE_COUNT_")
	protected Long  byteCount;

	@ApiModelProperty(name="extensionName", notes="扩展名")
	@TableField("EXT_")
	protected String  extensionName;

	@ApiModelProperty(name="note", notes="说明")
	@TableField("NOTE_")
	protected String  note;



	@ApiModelProperty(name="isDel", notes="删除标识")
	@TableField("IS_DEL_")
	protected Short isDel = FILE_NOT_DEL; /*删除标识*/

	@ApiModelProperty("扩展属性1")
	@TableField("prop1_")
	protected String prop1;

	@ApiModelProperty("扩展属性2")
	@TableField("prop2_")
	protected String prop2;

	@ApiModelProperty("扩展属性3")
	@TableField("prop3_")
	protected String prop3;

	@ApiModelProperty("扩展属性4")
	@TableField("prop4_")
	protected String prop4;

	@ApiModelProperty("扩展属性5")
	@TableField("prop5_")
	protected String prop5;

	@ApiModelProperty("扩展属性6")
	@TableField("prop6_")
	protected String prop6;

    @ApiModelProperty("所属分类")
	@TableField("TYPE_")
    protected String type;


	@ApiModelProperty(name="creatorName", notes="上传者")
	@TableField("CREATOR_NAME_")
	protected String  creatorName;

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId()
	{
		return this.id;
	}
	public void setXbTypeId(String xbTypeId)
	{
		this.xbTypeId = xbTypeId;
	}
	/**
	 * 返回 附件分类ID
	 * @return
	 */
	public String getXbTypeId()
	{
		return this.xbTypeId;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	/**
	 * 返回 文件名
	 * @return
	 */
	public String getFileName()
	{
		return this.fileName;
	}
	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}
	/**
	 * 返回 附件类型。如：mail=邮件附件；user=用户信息附件
	 * @return
	 */
	public String getFileType()
	{
		return this.fileType;
	}
	public void setStoreType(String storeType)
	{
		this.storeType = storeType;
	}
	/**
	 * 返回 存储类型。DISK=基于磁盘；DB=基于数据库；BOTH=两者
	 * @return
	 */
	public String getStoreType()
	{
		return this.storeType;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	/**
	 * 返回 文件路径
	 * @return
	 */
	public String getFilePath()
	{
		return this.filePath;
	}
	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}
	/**
	 * 返回 文件二进制数据
	 * @return
	 */
	public byte[] getBytes()
	{
		return this.bytes;
	}
	public void setByteCount(Long byteCount)
	{
		this.byteCount = byteCount;
	}
	/**
	 * 返回 总字节数
	 * @return
	 */
	public Long getByteCount()
	{
		return this.byteCount;
	}

	/**
	 * @return the extensionName
	 */
	public String getExtensionName() {
		return extensionName;
	}
	/**
	 * @param extensionName the extensionName to set
	 */
	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	/**
	 * 返回 说明
	 * @return
	 */
	public String getNote()
	{
		return this.note;
	}


	public void setCreatorName(String creatorName)
	{
		this.creatorName = creatorName;
	}
	/**
	 * 返回 上传者
	 * @return
	 */
	public String getCreatorName()
	{
		return this.creatorName;
	}

	public void setIsDel(Short isDel)
	{
		this.isDel = isDel;
	}
	/**
	 * 返回 删除标识
	 * @return
	 */
	public Short getIsDel()
	{
		return this.isDel;
	}

	public String getProp1() {
		return prop1;
	}
	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}
	public String getProp2() {
		return prop2;
	}
	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}
	public String getProp3() {
		return prop3;
	}
	public void setProp3(String prop3) {
		this.prop3 = prop3;
	}
	public String getProp4() {
		return prop4;
	}
	public void setProp4(String prop4) {
		this.prop4 = prop4;
	}
	public String getProp5() {
		return prop5;
	}
	public void setProp5(String prop5) {
		this.prop5 = prop5;
	}
	public String getProp6() {
		return prop6;
	}
	public void setProp6(String prop6) {
		this.prop6 = prop6;
	}


}
