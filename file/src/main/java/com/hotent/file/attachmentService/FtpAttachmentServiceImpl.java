package com.hotent.file.attachmentService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotent.base.attachment.Attachment;
import com.hotent.base.attachment.AttachmentService;

@Service
public class FtpAttachmentServiceImpl implements AttachmentService{
	@Value("${FTP.FTP.url:''}")
	String url;								/*服务器地址*/
	@Value("${FTP.FTP.port:21}")
	int port = 21;							/*端口*/
	@Value("${FTP.FTP.username:''}")
	String username;						/*账号*/
	@Value("${FTP.FTP.password:''}")
	String password;						/*密码*/
	String separator = File.separator;		/*FTP服务器的分隔符*/

	private FTPClient ftp = new FTPClient();
	/** 本地字符编码 */
	private String LOCAL_CHARSET = "GBK";
	// FTP协议里面，规定文件名编码为iso-8859-1
	private String SERVER_CHARSET = "ISO-8859-1";

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	//连接FTP服务器
	private void connect(){
		try {
			int reply;
			ftp.connect(url, port);
			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			conf.setServerLanguageCode("zh");
			boolean loginResult = ftp.login(username, password);
			if(loginResult){
				// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
				if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) { 
					LOCAL_CHARSET = "UTF-8";
				}
				ftp.setControlEncoding(LOCAL_CHARSET);
				ftp.enterLocalPassiveMode(); // 设置被动模式
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void upload(Attachment attachment, InputStream inputStream) throws Exception {
		String path = attachment.getFilePath();
		String fileName = attachment.getId() + "." + attachment.getExtensionName();
		path = replaceFileSeparator(path);
		// 编码转换，避免上传到服务器上时出现中文乱码
		path = new String(path.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
		fileName = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
		validConnection();
		// 转到指定上传目录,没有该目录则创建
		CreateDirecroty(path);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		boolean result = ftp.storeFile(fileName, inputStream);
		if (!result) {
			throw new RuntimeException("上传文件失败");
		}
		// 关闭输入流
		inputStream.close();
	}

	@Override
	public void download(Attachment attachment, OutputStream outStream) throws Exception{
		validConnection();
		String path = attachment.getFilePath();
		path = replaceFileSeparator(path);
		String fileName = attachment.getId() + "." + attachment.getExtensionName();
		// 编码转换，避免上传到服务器上时出现中文乱码
		path = new String(path.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
		boolean changeResult = ftp.changeWorkingDirectory(path);// 转移到FTP服务器目录
		if(!changeResult){
			throw new RuntimeException("要下载的文件路径不存在");
		}
		FTPFile[] fs = ftp.listFiles();
		boolean tag = false;
		for (FTPFile ff : fs) {
			String n = ff.getName();
			if (n.equals(fileName)) {
				tag = true;
				fileName = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
				ftp.retrieveFile(fileName, outStream);
				outStream.close();
				break;
			}
		}
		if(!tag){
			throw new RuntimeException("要下载的文件不存在");
		}
	}

	@Override
	public boolean chekckFile(Attachment attachment) throws Exception{
		boolean ref=true;
		validConnection();
		String path = attachment.getFilePath();
		path = replaceFileSeparator(path);
		String fileName = attachment.getId() + "." + attachment.getExtensionName();
		// 编码转换，避免上传到服务器上时出现中文乱码
		path = new String(path.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
		boolean changeResult = ftp.changeWorkingDirectory(path);// 转移到FTP服务器目录
		if(!changeResult){
			ref=false;
		}
		FTPFile[] fs = ftp.listFiles();
		boolean tag = false;
		for (FTPFile ff : fs) {
			String n = ff.getName();
			if (n.equals(fileName)) {
				tag = true;
				break;
			}
		}
		if(!tag){
			ref=false;
		}
		return  ref;
	}

	@Override
	public void remove(Attachment attachment) throws Exception {
		validConnection();
		String path = attachment.getFilePath();
		path = replaceFileSeparator(path);
		String fileName = attachment.getId() + "." + attachment.getExtensionName();
		ftp.deleteFile(path + separator + fileName);
	}

	//验证连接
	private void validConnection(){
		try{
			if(!ftp.isConnected()||!ftp.isRemoteVerificationEnabled()||!ftp.sendNoOp()){
				connect();
			}
			// 重置当前目录到根目录
			ftp.changeWorkingDirectory(separator);
		}
		catch(Exception e){
			connect();
		}
	}

	/*//转移FTP文件目录位置(不存在的目录会自动创建)	
	private void changeDirectory(String path) throws Exception{
		boolean changeResult = ftp.changeWorkingDirectory(path);
		//转移失败，则该目录不存在
		if(!changeResult){
			String errorMsg = "上传文件时，创建文件路径失败";
			//创建目录
			int mkd = ftp.mkd(path);
			if(mkd!=257){
				throw new RuntimeException(errorMsg);
			}
			//转移到该目录
			changeResult = ftp.changeWorkingDirectory(path);
			if(!changeResult){
				throw new RuntimeException(errorMsg);
			}
		}
	}*/


	/**
	 * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	private  boolean CreateDirecroty(String remote) throws IOException {
		boolean success = true;
		String directory = remote + "/";
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
				path = path + "/" + subDirectory;
				if (!existFile(path)) {
					if (makeDirectory(subDirectory)) {
						changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录[" + subDirectory + "]失败");
						changeWorkingDirectory(subDirectory);
					}
				} else {
					changeWorkingDirectory(subDirectory);
				}

				paths = paths + "/" + subDirectory;
				start = end + 1;
				end = directory.indexOf("/", start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	/**
	 * 改变目录路径
	 * @param directory
	 * @return
	 */
	private  boolean changeWorkingDirectory(String directory) {
		boolean flag = true;
		try {
			flag = ftp.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("进入文件夹" + directory + " 成功！");

			} else {
				System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断ftp服务器文件是否存在  
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private  boolean existFile(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftp.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}



	/**
	 * 创建目录
	 * @param dir
	 * @return
	 */
	private boolean makeDirectory(String dir) {
		boolean flag = true;
		try {
			flag = ftp.makeDirectory(dir);
			if (flag) {
				System.out.println("创建文件夹" + dir + " 成功！");

			} else {
				System.out.println("创建文件夹" + dir + " 失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}






	private String replaceFileSeparator(String path){
		// 替换路径中的分隔符
		String ftpFormatPath = regReplace(path, separator);
		// 替换文件名，只保留路径
		ftpFormatPath = ftpFormatPath.replaceAll("/\\w+\\.?(\\w+)?(\\s+)?$", "");
		return ftpFormatPath;
	}

	private String regReplace(String str, String replaceChar){
		StringBuffer resultString = new StringBuffer();
		try {
			Pattern regex = Pattern.compile("[\\\\|/]");
			Matcher regexMatcher = regex.matcher(str);
			while (regexMatcher.find()) {
				regexMatcher.appendReplacement(resultString, replaceChar);
			}
			regexMatcher.appendTail(resultString);
		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		return resultString.toString();
	}

	@Override
	public String getStoreType() {
		return "ftp";
	}
}
