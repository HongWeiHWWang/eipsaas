package com.hotent.base.util;

import java.io.UnsupportedEncodingException;

/**
 * Base64类 <br>
 * 功能：字符串的BASE64编码解码。
 */
public class Base64 {
	/**
	 * 将字符串转化为base64编码
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getBase64(String s)
			throws UnsupportedEncodingException {
		if(StringUtil.isEmpty(s))
			s = "";
		byte[] bytes = org.apache.commons.codec.binary.Base64.encodeBase64(s
				.getBytes("utf-8"));
		return new String(bytes, "utf-8");

	}

	/**
	 * 将 BASE64 编码的字符串 s 进行解码
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getFromBase64(String s)
			throws UnsupportedEncodingException {
		if(s!=null){
			byte[] bytes = s.getBytes("utf-8");
			byte[] convertBytes = org.apache.commons.codec.binary.Base64
					.decodeBase64(bytes);
			return new String(convertBytes, "utf-8");
		}
		return  "";
	}

	/**
	 * 将Base64编码的字符串解码
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] decodeBase64(String s){
		if(s!=null){
			byte[] bytes;
			try {
				bytes = s.getBytes("utf-8");
				byte[] convertBytes = org.apache.commons.codec.binary.Base64.decodeBase64(bytes);
				return convertBytes;
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		return null;
	}
}
