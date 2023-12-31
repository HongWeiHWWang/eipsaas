package com.hotent.ueditor.hunter;

import java.util.Map;

import com.hotent.ueditor.define.BaseState;
import com.hotent.ueditor.define.MultiState;
import com.hotent.ueditor.define.State;

public class FileManager {
    private int count;
    private int total;

    public static String endpoint;
    public static String accessKeyId;
    public static String accessKeySecret;
    public static String baseUrl;
    public static String bucket;
    public static String uploadDirPrefix;
    public static int pageSize;

    public FileManager(Map<String, Object> conf) {
        this.count = (Integer) conf.get("count");

    }

    public State listFile(int index, String marker) {
        return null;
    }

    public State deleteFile(String key) {
        return null;
    }

    private State getState(String[] files) {
        MultiState state = new MultiState(true);
        BaseState fileState;

        for (String url : files) {
            if (url == null) break;
            fileState = new BaseState(true);
            fileState.putInfo("url", baseUrl + "/" + url);
            state.addState(fileState);
        }

        return state;
    }
}
