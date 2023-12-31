package com.hotent.file.util;


import com.hotent.base.util.StringUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.sys.util.SysPropertyUtil;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Calendar;
 
/**
 * 附件工具类
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月15日
 */
public class AppFileUtil {
	/**
	 * 获取系统属性中的文件存储路径
	 * @return
	 */
	public static String getAttachPath() {
		return SysPropertyUtil.getByAlias("file.upload", "D:\\x7\\file");
	}
	
	/**
	 * 配置文件中获取文件存放的类型
	 * @author xcx
	 * @version 创建时间：2013-12-27  下午3:53:20
	 * @return
	 */
	public static String getSaveType() {
		return SysPropertyUtil.getByAlias("file.saveType", DefaultFile.SAVE_TYPE_DTABASE);
	}
	
	
	/**
	 * 创建文件目录
	 *            文件名称
	 * @return 文件的完整目录
	 */
	public static String createFilePath(String tempPath, String fileName) {
		File one = new File(tempPath);
		Calendar cal = Calendar.getInstance();
		Integer year = cal.get(Calendar.YEAR); // 当前年份
		Integer month = cal.get(Calendar.MONTH) + 1; // 当前月份
		one = new File(tempPath +  File.separator + year +  File.separator + month);
		if (!one.exists()) {
			one.mkdirs();
		}
		return one.getPath() + File.separator + fileName;
	}
	/**
	 * 配置文件中获取文件上传路径
	 * 如果为空则采用默认路径/attachFiles/temp
	 * 这个路径返回没有/或\结尾。
	 * 
	 * @author hjx
	 * @version 创建时间：2013-11-4  下午3:46:28
	 * @return
	 */
	public static String getBasePath() {
		String attachPath=getAttachPath();
		if (StringUtil.isEmpty(attachPath)) {
			attachPath = AppFileUtil.getRealPath("/attachFiles/temp");
		}
		attachPath=StringUtil.trimSufffix(attachPath, "\\") ;
		attachPath=StringUtil.trimSufffix(attachPath, "/") ;
		
		return attachPath;
	}
	
	public static String createPath(String tempPath, String fileName) {
		File one = new File(tempPath);
		if (!one.exists()) {
			one.mkdirs();
		}
		return one.getPath() + File.separator + fileName;
	}

	private static ServletContext servletContext;
	
	
	/**
	 * 
	 * @param _servletContext
	 */
	public static void init(ServletContext _servletContext)
	{
		servletContext=_servletContext;
	}

	/**
	 * 在web环境中根据web页面的路径获取对应页面的绝对路径。
	 * @param path
	 * @return
	 */
	public static String getRealPath(String path){
		return servletContext.getRealPath(path);
	}
	
}
