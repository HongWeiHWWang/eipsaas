package com.hotent.portal.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;


/**
 * 封装一个http请求的工具类
 */
public class HttpClientUtil {
    
    public static ObjectNode executeRestfulApi(String restApi,JsonNode params) throws Exception{
    	ObjectMapper mapper = JsonUtil.getMapper();
    	ObjectNode responseMsg = mapper.createObjectNode();
    	try {
    		if(BeanUtils.isNotEmpty(params)){
    			postHttp(restApi,params);
    		}else{
    			responseMsg = getHttp(restApi);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return (ObjectNode) JsonUtil.toJsonNode(responseMsg);
    }
    
    /**
     * get方式
     * @param url
     * @return
     * @throws Exception 
     */
    public static ObjectNode getHttp(String url) throws Exception {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(getMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = getMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放连接
            getMethod.releaseConnection();
        }
        return (ObjectNode)JsonUtil.toJsonNode(responseMsg);
    }

    /**
     * post方式
     * @param url
     * @param code
     * @param type
     * @return
     * @throws Exception 
     */
    public static JsonNode postHttp(String url,JsonNode params) throws Exception {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("GBK");
        PostMethod postMethod = new PostMethod(url);
        try {
        	if(BeanUtils.isNotEmpty(params)){
        		ObjectMapper mapper = JsonUtil.getMapper();
            	ObjectNode obj = mapper.createObjectNode();
        		for (Object object : params) {
        			JsonNode jsonObj = JsonUtil.toJsonNode(object);
        			obj.put(jsonObj.get("name").asText(), jsonObj.get("value").asText());
        			//postMethod.addParameter((String)jsonObj.get("name"),(String) jsonObj.get("value"));
        		}
        		postMethod.setRequestEntity(new StringRequestEntity(obj.toString(), "application/json", "UTF-8"));
        	}
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        
        return (JsonNode)JsonUtil.toJsonNode(responseMsg);
    }

}
