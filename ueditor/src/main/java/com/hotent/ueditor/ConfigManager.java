package com.hotent.ueditor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.ueditor.define.ActionMap;

/**
 * 配置管理器
 *
 * @author hancong03@baidu.com
 */
public class ConfigManager {
    private ObjectNode jsonConfig = null;
    // 涂鸦上传filename定义
    private final static String SCRAWL_FILE_NAME = "scrawl";
    // 远程图片抓取filename定义
    private final static String REMOTE_FILE_NAME = "remote";

    /*
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */
    public ConfigManager(String config) {
        this.initEnv(config);
    }


    // 验证配置文件加载是否正确
    public boolean valid() {
        return this.jsonConfig != null;
    }

    public ObjectNode getAllConfig() {
        return this.jsonConfig;
    }

    public Map<String, Object> getConfig(int type) {

        Map<String, Object> conf = new HashMap<>();
        String savePath = null;

        switch (type) {

            case ActionMap.UPLOAD_FILE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.getLong("fileMaxSize"));
                conf.put("allowFiles", this.getArray("fileAllowFiles"));
                conf.put("fieldName", JsonUtil.getString(this.jsonConfig, "fileFieldName"));
                savePath =  JsonUtil.getString(this.jsonConfig, "filePathFormat");
                break;

            case ActionMap.UPLOAD_IMAGE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.getLong("imageMaxSize"));
                conf.put("allowFiles", this.getArray("imageAllowFiles"));
                conf.put("fieldName", JsonUtil.getString(this.jsonConfig, "imageFieldName"));
                savePath = JsonUtil.getString(this.jsonConfig, "imagePathFormat");
                break;

            case ActionMap.UPLOAD_VIDEO:
                conf.put("maxSize", this.getLong("videoMaxSize"));
                conf.put("allowFiles", this.getArray("videoAllowFiles"));
                conf.put("fieldName", JsonUtil.getString(this.jsonConfig, "videoFieldName"));
                savePath = JsonUtil.getString(this.jsonConfig, "videoPathFormat");
                break;

            case ActionMap.UPLOAD_SCRAWL:
                conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
                conf.put("maxSize", this.getLong("scrawlMaxSize"));
                conf.put("fieldName", JsonUtil.getString(this.jsonConfig, "scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = JsonUtil.getString(this.jsonConfig, "scrawlPathFormat");
                break;

            case ActionMap.CATCH_IMAGE:
                conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
                conf.put("filter", this.getArray("catcherLocalDomain"));
                conf.put("maxSize", this.getLong("catcherMaxSize"));
                conf.put("allowFiles", this.getArray("catcherAllowFiles"));
                conf.put("fieldName", JsonUtil.getString(this.jsonConfig, "catcherFieldName") + "[]");
                savePath = JsonUtil.getString(this.jsonConfig, "catcherPathFormat");
                break;

            case ActionMap.LIST_IMAGE:
                conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
                conf.put("dir", JsonUtil.getString(this.jsonConfig, "imageManagerListPath"));
                conf.put("count", JsonUtil.getInt(this.jsonConfig, "imageManagerListSize"));
                break;

            case ActionMap.LIST_FILE:
                conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
                conf.put("dir", JsonUtil.getString(this.jsonConfig, "fileManagerListPath"));
                conf.put("count", JsonUtil.getInt(this.jsonConfig, "fileManagerListSize"));
                break;

            case ActionMap.DELETE_FILE:
                conf.put("dir", JsonUtil.getString(this.jsonConfig, "fileManagerListPath"));
                break;
        }
        conf.put("savePath", savePath);
        return conf;
    }

    private void initEnv(String config) {
        try {
        	JsonNode jsonNode = JsonUtil.toJsonNode(config);
            this.jsonConfig = (ObjectNode)jsonNode;
        } catch (Exception e) {
            this.jsonConfig = null;
        }
    }
    
    private Long getLong(String key) {
    	JsonNode jsonNode = this.jsonConfig.get(key);
    	Assert.notNull(jsonNode, String.format("通过Key：%s未能获取到对应的值。", key));
    	return jsonNode.asLong();
    }

    private String[] getArray(String key) {
        JsonNode jsonNode = this.jsonConfig.get(key);
        Assert.notNull(jsonNode, String.format("通过Key：%s未能获取到对应的值。", key));
        Assert.isTrue(jsonNode.isArray(), String.format("Key：%s所对应的值不是数组格式", key));
        ArrayNode aryNode = (ArrayNode)jsonNode;
        int size = aryNode.size();
        String[] result = new String[size];

        for (int i = 0; i < size; i++) {
        	JsonNode node = aryNode.get(i);
        	result[i] = node.asText();
        }
        return result;
    }
}
