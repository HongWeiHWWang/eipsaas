package com.hotent.form.generator;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 代码生成器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月30日
 */
public interface GeneratorService {
	/**
	 * 执行代码生成
	 * <p>
	 * 代码会生成在java临时目录中
	 * </p>
	 * @param generatorModel
	 * @return	生成的代码目录名
	 */
	String generator(GeneratorModel generatorModel) throws IOException;
	
	/**
	 * 根据目录打包zip文件并提供下载
	 * @param response
	 * @param codeFolder
	 */
	void download(HttpServletResponse response, String codeFolder) throws IOException;
}
