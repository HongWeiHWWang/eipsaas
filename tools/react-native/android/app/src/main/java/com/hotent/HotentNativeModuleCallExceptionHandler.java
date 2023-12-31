package com.hotent;

import com.facebook.react.bridge.NativeModuleCallExceptionHandler;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by caolicheng on 2016/10/25.
 */

public class HotentNativeModuleCallExceptionHandler implements NativeModuleCallExceptionHandler {
    @Override
    public void handleException(Exception e) {
        CrashReport.postCatchedException(e);
    }
}
