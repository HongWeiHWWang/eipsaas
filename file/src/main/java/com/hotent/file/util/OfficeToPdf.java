package com.hotent.file.util;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.springframework.stereotype.Component;

import com.hotent.base.exception.BaseException;

/**
 * office文档转换为pdf
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年9月30日
 */
@Component
public class OfficeToPdf {
	/**
	 * 获取OpenOffice.org 3的安装目录
	 *
	 * @return OpenOffice.org 3的安装目录
	 */
	@Resource
	ConverterUtils converterUtils;
	/**
	 * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件<br>
	 *
	 * @param inputFilePath
	 *            源文件路径，如："e:/test.docx"
	 * @param outputFilePath
	 *            目标文件路径，如："e:/test_docx.pdf"
	 * @return
	 */
	public  boolean openOfficeToPDF(String inputFilePath, String outputFilePath) {
		return office2pdf(inputFilePath, outputFilePath);
	}


	/**
	 * 转换文件
	 *
	 * @param inputFile
	 * @param outputFilePath_end
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param converter
	 */
	public static void converterFile(File inputFile, String outputFilePath_end,
			String inputFilePath, String outputFilePath,
			OfficeDocumentConverter converter) {
		File outputFile = new File(outputFilePath_end);

		// 假如目标路径不存在,则新建该路径
		if (!outputFile.exists()) {
			outputFile.getParentFile().mkdirs();
		}
		@SuppressWarnings("unused")
		String outputExtension = FilenameUtils.getExtension(outputFile.getName());
		try {
			converter.convert(inputFile, outputFile);
		}
		catch(NullPointerException e) {
			throw new BaseException("未找到服务端的附件预览组件.");
		}
	}

	/**
	 * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件
	 *
	 * @param inputFilePath
	 *            源文件路径，如："e:/test.docx"
	 * @param outputFilePath
	 *            目标文件路径，如："e:/test_docx.pdf"
	 * @return
	 */
	public  boolean office2pdf(String inputFilePath, String outputFilePath) {
		boolean flag = false;
		OfficeDocumentConverter converter = converterUtils.getDocumentConverter();
		if (null != inputFilePath) {
			File inputFile = new File(inputFilePath);
			// 判断目标文件路径是否为空
			if (null == outputFilePath) {
				// 转换后的文件路径
				String outputFilePath_end = getOutputFilePath(inputFilePath);
				if (inputFile.exists()) {// 找不到源文件, 则返回
					converterFile(inputFile, outputFilePath_end, inputFilePath,
							outputFilePath, converter);
					flag = true;
				}
			} else {
				if (inputFile.exists()) {// 找不到源文件, 则返回
					converterFile(inputFile, outputFilePath, inputFilePath,
							outputFilePath, converter);
					flag = true;
				}
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 获取输出文件
	 *
	 * @param inputFilePath
	 * @return
	 */
	public static String getOutputFilePath(String inputFilePath) {
		String outputFilePath = inputFilePath.replaceAll("."
				+ getPostfix(inputFilePath), ".pdf");
		return outputFilePath;
	}

	/**
	 * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"
	 *
	 * @param inputFilePath
	 * @return
	 */
	public static String getPostfix(String inputFilePath) {
		return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
	}
}