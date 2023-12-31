package com.hotent.tools.sqlgen.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * String 帮助类
 * 
 * <pre>
 * 构建组：x6-tools
 * 作者：hugh
 * 邮箱:zhuangxh@jee-soft.cn
 * 日期:2014-9-26-上午10：09:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().equals(""))
			return true;
		return false;
	}

	/**
	 * 判断字符串非空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array,Long,Short是否为空
	 * 
	 * @param o
	 *            java.lang.Object.
	 * @return boolean.
	 */
	@SuppressWarnings("unused")
	public static boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof String) {
			if (((String) o).trim().length() == 0)
				return true;
		} else if (o instanceof Collection) {
			if (((Collection<?>) o).isEmpty())
				return true;
		} else if (o.getClass().isArray()) {
			if (((Object[]) o).length == 0)
				return true;
		} else if (o instanceof Map) {
			if (((Map<?, ?>) o).isEmpty())
				return true;
		} else if (o instanceof Long) {
			if (((Long) o) == null)
				return true;
		} else if (o instanceof Short) {
			if (((Short) o) == null)
				return true;
		} else {
			return false;
		}
		return false;

	}

	/**
	 * 可以用于判断 Map,Collection,StringArray,Long,Short是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * 指定字符集，写入文件。
	 * 
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content, String charset) {
		Writer out;
		try {
			createFolder(fileName, true);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charset));
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile) {
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}
}
