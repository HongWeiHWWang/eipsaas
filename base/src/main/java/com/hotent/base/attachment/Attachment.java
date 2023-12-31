/**
 * 
 */
package com.hotent.base.attachment;

import com.hotent.base.model.ExtraProp;

/**
 * 附件实体接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
public interface Attachment extends ExtraProp{

	public static final String SAVE_TYPE_FOLDER = "folder";
	public static final String SAVE_TYPE_FTP = "ftp";
	public static final String SAVE_TYPE_DTABASE = "database";
	
	String getId();
	
	String getStoreType();//存储类型

	String getExtensionName();/*扩展名*/

	String getFileName();/*文件名*/

	Long  getByteCount();/*总字节数*/

	String getFilePath();/*文件路径*/
	
	byte[]  getBytes();/*文件二进制数据*/
	
	void setBytes(byte[] bytes);

}
