package com.hotent.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class FluentUtil {

	private final static int CONNECT_TIMEOUT = 30000;
	private final static int SOCKET_TIMEOUT = 30000;

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url, String headerStr) throws ClientProtocolException, IOException {
		return get(url, headerStr, 0, 0);
	}

	public static String get(String url, String headerStr, int connectTimeout, int socketTimeout)
			throws ClientProtocolException, IOException {
		connectTimeout = connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT;
		socketTimeout = socketTimeout > 0 ? socketTimeout : SOCKET_TIMEOUT;
		Request request = Request.Get(url);
		request = setHeaders(request, headerStr);
		HttpResponse returnResponse = request.connectTimeout(connectTimeout).socketTimeout(socketTimeout).execute()
				.returnResponse();
		return handleResponse(returnResponse);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static String post(String url, String headerStr, Object params) throws ClientProtocolException, IOException {
		return post(url, headerStr, params, 0, 0);
	}

	public static String post(String url, String headerStr, Object params, int connectTimeout, int socketTimeout)
			throws ClientProtocolException, IOException {
		connectTimeout = connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT;
		socketTimeout = socketTimeout > 0 ? socketTimeout : SOCKET_TIMEOUT;

		Request request = Request.Post(url);
		request = setHeaders(request, headerStr);
		String paramStr = "";
		if(BeanUtils.isNotEmpty(params)){
			paramStr = JsonUtil.toJson(params);
		}
		HttpResponse returnResponse = request.bodyString(paramStr, ContentType.APPLICATION_JSON)
				.connectTimeout(connectTimeout).socketTimeout(socketTimeout).execute().returnResponse();
		return handleResponse(returnResponse);
	}

	@SuppressWarnings("rawtypes")
	private static Request setHeaders(Request request, String headerStr) {
		if (StringUtil.isNotEmpty(headerStr)) {
			try {
				headerStr = Base64.getFromBase64(headerStr);
				ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(headerStr);
				Iterator it = obj.fieldNames();
				while (it.hasNext()) {
					String key = (String) it.next();
					request.setHeader(key, obj.get(key).asText());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return request;
	}

	/**
	 * 处理结果，状态不等于200，则解析异常内容，
	 * @param returnResponse
	 * @return
	 * @throws IOException
	 */
	private static String handleResponse(HttpResponse returnResponse) throws IOException {
		int statusCode = returnResponse.getStatusLine().getStatusCode();
		InputStream content = returnResponse.getEntity().getContent();
		String res = StringUtil.InputStreamToString(content);
		if (statusCode != 200) {
			throw new RuntimeException(JsonUtil.toJsonNode(res).get("message").asText());
		}
		return res;
	}

}
