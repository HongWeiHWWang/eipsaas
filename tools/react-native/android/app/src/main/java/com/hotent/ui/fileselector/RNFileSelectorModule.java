
package com.hotent.ui.fileselector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hotent.ui.fileselector.RealPathUtil;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.regex.Pattern;
import java.io.File;

public class RNFileSelectorModule extends ReactContextBaseJavaModule {

  private Callback onDone;
  private Callback onCancel;

  public RNFileSelectorModule(ReactApplicationContext reactContext) {
    super(reactContext);

    getReactApplicationContext().addActivityEventListener(new ActivityEventListener());
  }

  @Override
  public String getName() {
    return "RNFileSelector";
  }

  @ReactMethod
  public void Show(final ReadableMap props, final Callback onDone, final Callback onCancel) {
    boolean openFilePicker = checkPermissionsAndOpenFilePicker();
    if (openFilePicker) {
      this.onDone = onDone;
      this.onCancel = onCancel;
      try{
          openFilePicker(props);
      }catch(Exception e){
         onCancel.invoke(e.toString());
      }
    }
  }


  private boolean checkPermissionsAndOpenFilePicker() {
    String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    if (ContextCompat.checkSelfPermission(getCurrentActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(getCurrentActivity(), permission)) {
        showError();
        return false;
      } else {
        ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{permission}, 60000);
        return true;
      }
    }

    return true;
  }

  private void showError() {
    Toast.makeText(getCurrentActivity(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
  }

  @ReactMethod
  public void ShowOtherPicker(final ReadableMap props, final Callback onDone, final Callback onCancel) {
    boolean openFilePicker = checkPermissionsAndOpenFilePicker();
    if (openFilePicker) {
      try{
          this.onDone = onDone;
          this.onCancel = onCancel;
          openOtherFilePicker(props);
      }catch(Exception e){
          onCancel.invoke(e.toString());
      }
    }
  }

  private void openOtherFilePicker(final ReadableMap props) {
      MaterialFilePicker picker = new MaterialFilePicker();
      picker = picker.withActivity(getCurrentActivity());
      picker = picker.withRequestCode(60001);

      String filter = props.getString("filter");
      boolean filterDirectories = props.getBoolean("filterDirectories");
      String path = props.getString("path");
      boolean hiddenFiles = props.getBoolean("hiddenFiles");
      boolean closeMenu = props.getBoolean("closeMenu");
      String title = props.getString("title");

      if (filter.length() > 0) {
        picker = picker.withFilter(Pattern.compile(filter));
      }

      picker = picker.withFilterDirectories(filterDirectories);

      if (path.length() > 0) {
        picker = picker.withRootPath(path);
      }

      picker = picker.withHiddenFiles(hiddenFiles);
      picker = picker.withCloseMenu(closeMenu);

      picker = picker.withTitle(title);
      
      picker.start();
  }


  private void openFilePicker(final ReadableMap props) {
    try {
          
          final Intent galleryIntent = new Intent(Intent.ACTION_PICK);

          galleryIntent.setType("*/*");

          galleryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
          galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
          galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

          final Intent chooserIntent = Intent.createChooser(galleryIntent, "Pick an image");
          Activity activity = getCurrentActivity();
          activity.startActivityForResult(chooserIntent, 60001);
      } catch (Exception e) {
    }
  }

  private class ActivityEventListener implements com.facebook.react.bridge.ActivityEventListener {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

      if (requestCode == 60001 && resultCode == AppCompatActivity.RESULT_OK) {
        String path = "";
        Uri uri = data.getData();
        path = RealPathUtil.getRealPathFromURI(activity, uri);
        File f = new File(path);

        // if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
        //     path = uri.getPath();
        //     return;
        // }
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
        //     path = getPath(this, uri);
        // } else {//4.4以下下系统调用方法
        //     path = getRealPathFromURI(uri);
        // }

        WritableMap file = new WritableNativeMap();
        file.putString("path", "file://" + path);
        file.putString("name", f.getName());
        onDone.invoke(file);
      } else if (requestCode == 60001 && resultCode == AppCompatActivity.RESULT_CANCELED) {
        onCancel.invoke();
      }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
  }
}