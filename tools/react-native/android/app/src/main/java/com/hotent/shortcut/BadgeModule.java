package com.hotent.shortcut;
import com.facebook.react.bridge.ReactApplicationContext;
import   com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.leolin.shortcutbadger.*;

public class BadgeModule extends ReactContextBaseJavaModule {

public BadgeModule(ReactApplicationContext reactContext) {
    super(reactContext);
}

/**
 * 这个返回的字符串是我们js端调用时会用到的
 */
@Override
public String getName() {
    return "Badge";
}

/**
*这个方法是我们js端调用的方法，其中的参数可以从js端传过来  如这里我们js端可以类似  Badge.showBadge(2)来调用这个方法
*/
@ReactMethod
public void showBadge(int badgeNum) {
    boolean success = ShortcutBadger.applyCount(getCurrentActivity(), badgeNum);
    //Toast.makeText(getCurrentActivity(), "Set count=" + badgeNum + ", success=" + success, Toast.LENGTH_SHORT).show();
    //System.out.println(success);
}}