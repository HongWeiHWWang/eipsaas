package com.hotent.tools.sqlgen.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 文件操作帮助类
 * 
 * <pre>
 * 构建组：x6-tools
 * 作者：hugh
 * 邮箱:zhuangxh@jee-soft.cn
 * 日期:2014-9-26-上午10：09:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class FileUtil {
	/**
	 * 指定UTF-8字符集，写入文件。
	 * 
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content) {
		writeFile(fileName, content, "UTF-8");
	}

	/**
	 * 指定字符集，写入文件。
	 * 
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content, String charset) {
		Writer out = null;
		try {
			createFolder(fileName, true);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charset));
			out.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile)
			path = path.substring(0, path.lastIndexOf(File.separator));
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}
}
