package com.hotent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.bugly.crashreport.CrashReport;

public class ReactNativeJSCrashReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.richardcao.android.REACT_NATIVE_CRASH_REPORT_ACTION")) {
            Throwable js = (Throwable) intent.getSerializableExtra("JavascriptException");
            Throwable e = (Throwable) intent.getSerializableExtra("Exception");
            if (js != null) {
                CrashReport.postCatchedException(js);
            }
            if (e != null) {
                CrashReport.postCatchedException(e);
            }
        }
    }
}
