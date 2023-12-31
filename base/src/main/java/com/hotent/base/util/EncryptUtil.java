package com.hotent.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密算法
 * <pre>
 * 1.MD5
 * 2.AES
 * </pre>
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月11日
 */
public class EncryptUtil {
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String DEFAULT_PASSWORD = "Djk@%&opN!$$*";

	/**
	 * 使用MD5编码字符串
	 * 
	 * @param inStr 字符串
	 * @return
	 * @throws Exception
	 */
	public static String encryptMd5(String inStr) throws Exception {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(inStr.getBytes());
			return new String(Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 获得文件的MD5码
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String encryptFileMd5(String fileName) throws Exception {
		byte[] bytes=FileUtil.readByte(fileName);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(bytes);
			return new String(Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 输出明文按sha-256加密后的密文
	 * 
	 * @param inputStr
	 *            明文
	 * @return
	 */
	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte digest[] = md.digest(inputStr.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * AES 加密操作
	 * <pre>
	 * 使用默认密码进行加密
	 * </pre>
	 * @param content 加密内容
	 * @return 返回Base64转码后的加密数据
	 * @throws Exception
	 */
	public static String encrypt(String content) throws Exception {
		return encrypt(content, DEFAULT_PASSWORD);
	}

	/**
	 * AES 加密操作
	 *
	 * @param content 待加密内容
	 * @param password 加密密码
	 * @return 返回Base64转码后的加密数据
	 * @throws Exception
	 */
	public static String encrypt(String content, String password) throws Exception {
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
		byte[] result = cipher.doFinal(byteContent);// 加密
		return Base64.encodeBase64String(result);//通过Base64转码返回
	}
	
	/**
	 * AES 解密操作
	 * @param content 要解密的内容
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content) throws Exception {
		return decrypt(content, DEFAULT_PASSWORD);
	}

	/**
	 * AES 解密操作
	 *
	 * @param content 要解密内容
	 * @param password 解密密码
	 * @return 解密后的内容
	 * @throws Exception
	 */
	public static String decrypt(String content, String password) throws Exception {
		//实例化
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		//使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
		//执行操作
		byte[] result = cipher.doFinal(Base64.decodeBase64(content));
		return new String(result, "utf-8");
	}

	/**
	 * 生成加密秘钥
	 *
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
		//返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg = null;
		kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
		secureRandom.setSeed(password.getBytes());
		//AES 要求密钥长度为 128
		kg.init(128, secureRandom);
		//生成一个密钥
		SecretKey secretKey = kg.generateKey();
		return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
	}
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}
	
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}
	
	public static void main(String[] args) {
		String a = "4C52742B6E55716F725172452F6B5031584D6F4D4E773D3D";
		byte[] decodeBase64 = Base64.decodeBase64(a);
		String b = decodeBase64.toString();
		System.out.print(b);
	}
}
