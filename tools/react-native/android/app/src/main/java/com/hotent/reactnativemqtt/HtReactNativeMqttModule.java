package com.hotent.reactnativemqtt;

import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings.Secure;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Nullable;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.facebook.react.bridge.ReadableMap;
// import com.facebook.react.bridge.WritableMap;
// import com.facebook.react.bridge.WritableNativeMap;


public class HtReactNativeMqttModule extends ReactContextBaseJavaModule {

  ReactApplicationContext reactContext;
  
  static MqttAndroidClient mqttAndroidClient = null;

  public HtReactNativeMqttModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "HtReactNativeMqtt";
  }

  private void sendEvent(String eventName,String msg) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, msg);
  }

  @ReactMethod
  public void initClient(ReadableMap options){
    try{
      if(HtReactNativeMqttModule.mqttAndroidClient != null && HtReactNativeMqttModule.mqttAndroidClient.isConnected()){
        HtReactNativeMqttModule.mqttAndroidClient.disconnect();
        //HtReactNativeMqttModule.mqttAndroidClient.close();
        HtReactNativeMqttModule.mqttAndroidClient = null; 
      }
      _initClient(options);
    }
    catch (Exception e){
        sendEvent("initRrror",e.getMessage());
    }
  }

  @ReactMethod
  public void subscribe(final String topic, final int qos) {
      try{
        if(HtReactNativeMqttModule.mqttAndroidClient != null){
          HtReactNativeMqttModule.mqttAndroidClient.subscribe(topic,qos);
          sendEvent("onSubscribe",topic);
        }else{
          sendEvent("subscribeRrror","HtReactNativeMqttModule.mqttAndroidClient is null");
        }
      }catch (Exception e){
          sendEvent("subscribeRrror",e.getMessage());
      }
  }

  @ReactMethod
  public void unsubscribe(final String topic) {
      try{
        if(HtReactNativeMqttModule.mqttAndroidClient != null){
          HtReactNativeMqttModule.mqttAndroidClient.unsubscribe(topic);
        }
      }catch (Exception e){
          sendEvent("unsubscribeRrror",e.getMessage());
      }
  }

  @ReactMethod
  public void disconnect() {
      try{
        if(HtReactNativeMqttModule.mqttAndroidClient != null){
          HtReactNativeMqttModule.mqttAndroidClient.disconnect();
          HtReactNativeMqttModule.mqttAndroidClient = null;
        }else{
          sendEvent("disconnectRrror","HtReactNativeMqttModule.mqttAndroidClient is null");
        }
      }catch (Exception e){
          sendEvent("disconnectRrror",e.getMessage());
      }
  }

  @ReactMethod
  public void isConnected(Callback callback) {
    if(HtReactNativeMqttModule.mqttAndroidClient != null && HtReactNativeMqttModule.mqttAndroidClient.isConnected()){
        callback.invoke(true);
    }else{
      callback.invoke(false);
    }
  }

  @ReactMethod
  public void publish(ReadableMap options) {
      try{
        if(HtReactNativeMqttModule.mqttAndroidClient != null){
          //sendEvent("doPublish",options.getString("topic"));
          HtReactNativeMqttModule.mqttAndroidClient.publish(options.getString("topic"), options.getString("payload").getBytes("utf-8"), options.getInt("qos"), options.getBoolean("retain"));
        }else{
          sendEvent("publishRrror","HtReactNativeMqttModule.mqttAndroidClient is null");
        }
      }catch (Exception e){
          sendEvent("publishRrror",e.getMessage());
      }
  }

  private void _initClient(ReadableMap options) {
    String serverUri = "tcp://192.168.1.126:1883";
    String clientId = "ExampleAndroidClient"+ System.currentTimeMillis();;

    HtReactNativeMqttModule.mqttAndroidClient = new MqttAndroidClient(reactContext, options.getString("serverUri"), options.getString("clientId"));
    
    HtReactNativeMqttModule.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            sendEvent("connectComplete","connectComplete");
        }

        @Override
        public void connectionLost(Throwable cause) {
            sendEvent("connectionLost","connectionLost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            sendEvent("messageArrived",new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            sendEvent("deliveryComplete","deliveryComplete");
        }
    });

    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setAutomaticReconnect(true);
    mqttConnectOptions.setCleanSession(true);
    mqttConnectOptions.setKeepAliveInterval(20);
    // if(options.hasKey("willTopic")){
    //   options.setWill(options.getString("willTopic"),options.getString("willPayload"),options.getInt("willQos"),options.getBoolean("willRetain"));
    // }
    try {
        HtReactNativeMqttModule.mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
              sendEvent("onSuccess","onSuccess");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                sendEvent("onFailure",exception.getMessage());
            }
        });
    } catch (Exception e){
        sendEvent("onException",e.getMessage());
     }
  }


  private String getCurrentLanguage() {
      Locale current = getReactApplicationContext().getResources().getConfiguration().locale;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          return current.toLanguageTag();
      } else {
          StringBuilder builder = new StringBuilder();
          builder.append(current.getLanguage());
          if (current.getCountry() != null) {
              builder.append("-");
              builder.append(current.getCountry());
          }
          return builder.toString();
      }
  }

  private String getCurrentCountry() {
    Locale current = getReactApplicationContext().getResources().getConfiguration().locale;
    return current.getCountry();
  }

  private Boolean isEmulator() {
    return Build.FINGERPRINT.startsWith("generic")
      || Build.FINGERPRINT.startsWith("unknown")
      || Build.MODEL.contains("google_sdk")
      || Build.MODEL.contains("Emulator")
      || Build.MODEL.contains("Android SDK built for x86")
      || Build.MANUFACTURER.contains("Genymotion")
      || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
      || "google_sdk".equals(Build.PRODUCT);
  }

  private Boolean isTablet() {
    int layout = getReactApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    return layout == Configuration.SCREENLAYOUT_SIZE_LARGE || layout == Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }


  @ReactMethod
  public void isPinOrFingerprintSet(Callback callback) {
    KeyguardManager keyguardManager = (KeyguardManager) this.reactContext.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
    //sendEvent("isPinOrFingerprintSet","isPinOrFingerprintSet");
    //_initClient(options);
    callback.invoke(keyguardManager.isKeyguardSecure());
  }

  @Override
  public @Nullable Map<String, Object> getConstants() {
    HashMap<String, Object> constants = new HashMap<String, Object>();

    // PackageManager packageManager = this.reactContext.getPackageManager();
    // String packageName = this.reactContext.getPackageName();

    // constants.put("appVersion", "not available");
    // constants.put("buildVersion", "not available");
    // constants.put("buildNumber", 0);

    // try {
    //   PackageInfo info = packageManager.getPackageInfo(packageName, 0);
    //   constants.put("appVersion", info.versionName);
    //   constants.put("buildNumber", info.versionCode);
    // } catch (PackageManager.NameNotFoundException e) {
    //   e.printStackTrace();
    // }

    // String deviceName = "Unknown";

    // try {
    //   BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    //   if(myDevice!=null){
    //     deviceName = myDevice.getName();
    //   }
    // } catch(Exception e) {
    //   e.printStackTrace();
    // }

    // constants.put("instanceId", InstanceID.getInstance(this.reactContext).getId());
    // constants.put("deviceName", deviceName);
    // constants.put("systemName", "Android");
    // constants.put("systemVersion", Build.VERSION.RELEASE);
    // constants.put("model", Build.MODEL);
    // constants.put("brand", Build.BRAND);
    // constants.put("deviceId", Build.BOARD);
    // constants.put("deviceLocale", this.getCurrentLanguage());
    // constants.put("deviceCountry", this.getCurrentCountry());
    // constants.put("uniqueId", Secure.getString(this.reactContext.getContentResolver(), Secure.ANDROID_ID));
    // constants.put("systemManufacturer", Build.MANUFACTURER);
    // constants.put("bundleId", packageName);
    // constants.put("userAgent", System.getProperty("http.agent"));
    // constants.put("timezone", TimeZone.getDefault().getID());
    // constants.put("isEmulator", this.isEmulator());
    // constants.put("isTablet", this.isTablet());
    return constants;
  }
}
