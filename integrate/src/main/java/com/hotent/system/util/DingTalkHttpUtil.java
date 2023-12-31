package com.hotent.system.util;



import com.hotent.base.util.MyX509TrustManager;
import com.hotent.base.util.StringUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 钉钉的http工具栏
 * @author Administrator
 *
 */
public class DingTalkHttpUtil {

	/**
	 * 发送请求。
	 *
	 * @param url
	 *            URL地址
	 * @param params
	 *            发送参数
	 * @param requestMethod
	 *            GET,POST
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	public static String sendHttpsRequest(String url, String params,
			String requestMethod) {
		HttpsURLConnection conn;
		String str = null;
		try {
			conn = getHttpsConnection(url);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod(requestMethod);

			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (StringUtil.isNotEmpty(params)) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(params.getBytes("utf-8"));
				outputStream.close();
			}
			str = getOutPut(conn);
		} catch (KeyManagementException e) {
			throw new RuntimeException("远程服务器请求失败！"+e.getMessage(),e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("远程服务器请求失败！"+e.getMessage(),e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException("远程服务器请求失败！"+e.getMessage(),e);
		} catch (IOException e) {
			throw new RuntimeException("远程服务器请求失败！"+e.getMessage(),e);
		}

		return str;
	}

	/**
	 * 获取https连接。
	 * @param accessUrl
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	public static HttpsURLConnection getHttpsConnection(String accessUrl)
			throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchProviderException, IOException {
		URL url = new URL(accessUrl);
		HttpsURLConnection connection = (HttpsURLConnection) url
				.openConnection();

		TrustManager[] tm = { new MyX509TrustManager() };

		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		connection.setSSLSocketFactory(ssf);
		return connection;
	}


	/**
	 * 读取返回数据。
	 *
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public static String getOutPut(HttpsURLConnection conn) throws IOException {
		InputStream inputStream = conn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuffer buffer = new StringBuffer();
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		conn.disconnect();
		return buffer.toString();
	}

	/**
	 * 发送数据到指定的URL并读取返回结果。
	 * @param url
	 * @param data
	 * @return
	 */
	public static String sendData(String url,String data,String charset){
		URL uRL;
		URLConnection conn;

		BufferedReader bufferedReader = null;
		try {
			uRL = new URL(url);
			conn = uRL.openConnection();
			conn.setDoOutput(true);
			if(StringUtil.isNotEmpty(data)){
				OutputStream stream=conn.getOutputStream();
				stream.write(data.getBytes(charset));
				stream.flush();
				stream.close();
			}


			// Get the response
			bufferedReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}

			bufferedReader.close();

			return response.toString();
		}
		 catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}

