package com.hotent.reactnativewebviewbridge;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.content.SharedPreferences;
import android.app.DownloadManager.Request;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.webview.ReactWebViewManager;
import com.hotent.MainActivity;

import java.util.List;
import com.facebook.react.bridge.ReactApplicationContext;

import java.util.Map;

import javax.annotation.Nullable;

public class WebViewBridgeManager extends ReactWebViewManager {
    private static final String REACT_CLASS = "RCTWebViewBridge";

    private Activity mActivity = null;
    
    ReactApplicationContext r;

    public WebViewBridgeManager(ReactApplicationContext reactContext) {
      this.r = reactContext;
    }

    public static final int COMMAND_SEND_TO_BRIDGE = 101;
    
    @Override
    public String getName() { return REACT_CLASS;  }

    @Override
    public
    @Nullable
    Map<String, Integer> getCommandsMap() {
        Map<String, Integer> commandsMap = super.getCommandsMap();

        commandsMap.put("sendToBridge", COMMAND_SEND_TO_BRIDGE);

        return commandsMap;
    }
    @Override
    protected WebView createViewInstance(ThemedReactContext reactContext) {
        WebView root = super.createViewInstance(reactContext);

        final Activity mActivity = reactContext.getCurrentActivity();
        this.setmActivity(mActivity);

        root.addJavascriptInterface(new JavascriptBridge(root), "WebViewBridge");
        root.setDownloadListener(new WebviewDownload(reactContext));
        root.setWebChromeClient(new WebChromeClient() {
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                ((MainActivity) mActivity).setUploadMessage(uploadMsg);
                if(acceptType == null || acceptType == ""){
                	acceptType = "*/*";
                }
                openFileChooserView(acceptType);
            }

            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return true;
            }

            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return true;
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                ((MainActivity) mActivity).setUploadMessage(uploadMsg);
                openFileChooserView("*/*");
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                ((MainActivity) mActivity).setUploadMessage(uploadMsg);
                if(acceptType == null || acceptType == ""){
                	acceptType = "*/*";
                }
                openFileChooserView(acceptType);
            }

            // For Android > 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                ((MainActivity) mActivity).setmUploadCallbackAboveL(filePathCallback);
                String[] scceptTypes = fileChooserParams.getAcceptTypes();
                if(scceptTypes != null && scceptTypes.length > 0 && scceptTypes[0] != ""){
                	openFileChooserView(scceptTypes[0]);
                }else{
                	openFileChooserView("*/*");
                }
                return true;
            }

            private void openFileChooserView(String scceptType) {
                try {
                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType(scceptType);
                    galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    final Intent chooserIntent = Intent.createChooser(galleryIntent, "choose file");
                    mActivity.startActivityForResult(chooserIntent, 1);
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
            }
        });

        return root;
    }

    @Override
    public void receiveCommand(WebView root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);

        switch (commandId) {
            case COMMAND_SEND_TO_BRIDGE:
                sendToBridge(root, args.getString(0));
                break;
            default:
                //do nothing!!!!
        }
    }

    private void sendToBridge(WebView root, String message) {
        String script = "WebViewBridge.onMessage('" + message + "');";
        WebViewBridgeManager.evaluateJavascript(root, script);
    }

    static private void evaluateJavascript(WebView root, String javascript) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            root.evaluateJavascript(javascript, null);
        } else {
            root.loadUrl("javascript:" + javascript);
        }
    }

    @ReactProp(name = "allowFileAccessFromFileURLs")
    public void setAllowFileAccessFromFileURLs(WebView root, boolean allows) {
        root.getSettings().setAllowFileAccessFromFileURLs(allows);
    }

    @ReactProp(name = "allowUniversalAccessFromFileURLs")
    public void setAllowUniversalAccessFromFileURLs(WebView root, boolean allows) {
        root.getSettings().setAllowUniversalAccessFromFileURLs(allows);
    }

    public void setmActivity(Activity mActivity) {  this.mActivity = mActivity;  }

}

/**
 * 下载
 */
class WebviewDownload implements DownloadListener {
    ReactContext reactContext;

    DownloadManager downManager ;

    Activity myActivity;

    public WebviewDownload(ReactContext reactContext) {
         this.reactContext = reactContext;
     }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        try {
              Uri uri = Uri.parse(url);
              Intent intent = new Intent(Intent.ACTION_VIEW, uri);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              reactContext.startActivity(intent);
//             if (!isDownloadManagerAvailable(reactContext)) {
//                 Uri uri = Uri.parse(url);
//                 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                 reactContext.startActivity(intent);
//             } else {
//                 DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                 Uri uri = Uri.parse(url);
//                 String[] path = uri.getPath().split("/");
//                 String  fileName = "";
//                 if(path.length>1){
//                     fileName = path[path.length - 1];
//                 }
//                 request.setTitle(fileName);
//                 request.setDescription("下载完成后，点击开始安装。");
// // in order for this if to run, you must use the android 3.2 to compile your app
//                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                     request.allowScanningByMediaScanner();
//                     request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                 }
//                 request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
// // get download service and enqueue file
//                 DownloadManager manager = (DownloadManager) reactContext.getSystemService(Context.DOWNLOAD_SERVICE);
//                 manager.enqueue(request);
//
//                // 开始下载了，就发送一个 事件通知到 React Native 做相关处理，记得加上监听
//                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//                         .emit("NativeCustomEvent", "startDownload");
//             }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否支持 Download Manager
     * @param context used to check the device version and DownloadManager information
     * @return true if the download manager is available
     */
    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
