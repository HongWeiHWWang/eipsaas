
package com.hotent;

import android.app.Application;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactApplication;
import com.brentvatne.react.ReactVideoPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import org.devio.rn.splashscreen.SplashScreenReactPackage;
import com.zmxv.RNSound.RNSoundPackage;
import com.imagepicker.ImagePickerPackage;
import com.reactnative.ivpusic.imagepicker.PickerPackage;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import com.rnim.rn.audio.ReactNativeAudioPackage;
import com.RNFetchBlob.RNFetchBlobPackage;
import com.reactnativecommunity.webview.RNCWebViewPackage;
import com.reactnativecommunity.webview.RNCWebViewPackage;
import com.hotent.ui.fileselector.RNFileSelectorPackage;
import com.hotent.reactnativewebviewbridge.WebViewBridgePackage;
import com.brentvatne.react.ReactVideoPackage;
import com.reactnative.ivpusic.imagepicker.PickerPackage;
import com.imagepicker.ImagePickerPackage;
import com.zmxv.RNSound.RNSoundPackage;
import com.rnim.rn.audio.ReactNativeAudioPackage;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.oblador.vectoricons.VectorIconsPackage;
import com.tencent.bugly.crashreport.CrashReport;
import org.pgsqlite.SQLitePluginPackage;
import org.devio.rn.splashscreen.SplashScreenReactPackage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hotent.reactnativemqtt.HtReactNativeMqtt;
import com.hotent.cache.HtHttpCache;

import javax.annotation.Nullable;

import com.hotent.shortcut.BadgePackage;

public class MainApplication extends Application implements ReactApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "900019562", false);
        }
    }

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        @Override
        protected ReactInstanceManager createReactInstanceManager() {
            ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                    .setApplication(getApplication())
                    .setJSMainModulePath(getJSMainModuleName())
                    .setUseDeveloperSupport(getUseDeveloperSupport())
                    .setRedBoxHandler(getRedBoxHandler())
                    .setUIImplementationProvider(getUIImplementationProvider())
                    .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
                    .setNativeModuleCallExceptionHandler(
                            new HotentNativeModuleCallExceptionHandler());

            for (ReactPackage reactPackage : getPackages()) {
                builder.addPackage(reactPackage);
            }

            String jsBundleFile = getJSBundleFile();
            if (jsBundleFile != null) {
                builder.setJSBundleFile(jsBundleFile);
            } else {
                builder.setBundleAssetName(Assertions.assertNotNull(getBundleAssetName()));
            }
            return builder.build();
        }

        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }

        @Override
        protected List<ReactPackage> getPackages() {
            List<ReactPackage> packages = Arrays.asList(
                    new MainReactPackage(),
                    new RNFetchBlobPackage(),
                    new RNCWebViewPackage(),
                    new RNFileSelectorPackage(),
                    new BadgePackage(),
                    new WebViewBridgePackage(),
                    new ReactVideoPackage(),
                    new PickerPackage(),
                    new ImagePickerPackage(),
                    new RNSoundPackage(),
                    new SQLitePluginPackage(),
                    new ReactNativeAudioPackage(),
                    new SplashScreenReactPackage(),
                    new RNDeviceInfo(),
                    new HtReactNativeMqtt(),
                    new HtHttpCache(),
                    new VectorIconsPackage());
            ArrayList<ReactPackage> packageList = new ArrayList<>(packages);
            return packageList;
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }
}
