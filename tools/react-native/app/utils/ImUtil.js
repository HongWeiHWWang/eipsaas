
import init from 'react_native_mqtt';
import ToastUtil from './ToastUtil';
import * as timeUtil from './TimeUtil';
import {AsyncStorage,Platform,DeviceEventEmitter,NativeModules } from 'react-native';
import {MQTT_RECEIVE_DESTINATION,MQTT_PORT,MQTT_SERVER } from '../constants/Constans';
import HtAndroidMqtt from '../components/HtAndroidMqtt';
import * as store from '../utils/StoreUtil';


//目前分为两种mqtt连接方式，android可以常驻后台来监听接收到的消息，ios由于权限问题无法做到。
export function initIm(option) {
  if(Platform.OS == 'android'){
    androidInitIm(option);
  }else{
    iosInitIm(option);
  } 
}

export function offline() {
  if(Platform.OS == 'android'){
      const message = {
      from:global.loginUser.account,
      type:"offline",
      sendTime:timeUtil.getServerTime()
    }
    sendMessage(message,MQTT_RECEIVE_DESTINATION)
    global.HtReactNativeMqtt.disconnect();
  }else{
    const willMessage = new Paho.MQTT.Message("{'from':'"+global.loginUser.account+"','type':'offline'}");
    willMessage.destinationName = MQTT_RECEIVE_DESTINATION;
    if(global.imClient && global.imClient.isConnected()){
      imClient.send(willMessage);
      global.imClient.disconnect();
    }
  }   
}

export function sendMessage(message,toUser) {
    if(Platform.OS == 'android'){
      var option = {
          topic:toUser,
          payload:JSON.stringify(message),
          qos:0,
          retain:false
      }
      global.HtReactNativeMqtt.publish(option);
    }else{
      const imMessage = new Paho.MQTT.Message(JSON.stringify(message));
      imMessage.destinationName = toUser;
      global.imClient.send(imMessage);
    }
}

function iosInitIm(option){
    const user = option.user;
    init({
      size: 10000,
      storageBackend: AsyncStorage,
      defaultExpires: 2000,
      enableCache: true,
      sync : {
      }
    });
    const options = {
      host: global.imHost,
      port: Number(global.imPort)
    }

    if(!global.imClient){
      global.imClient = new Paho.MQTT.Client(options.host, options.port,Platform.OS+'_'+user.account+'_'+new Date().getTime());
    }

    global.imClient.disconnectedPublishing = true

    global.imClient.onMessageArrived = (message) =>{
      try {
        if(option.onMessageArrived){
          option.onMessageArrived(message.payloadString);
        }
      } catch (error) {
        ToastUtil.showLong(error);
      }
    }
    global.imClient.onConnectionLost = () =>{
      //ToastUtil.showShort("消息服务器断开")
    }
    if(!global.imClient.isConnected()){
      const willMessage = new Paho.MQTT.Message("{'from':'"+user.account+"','type':'offline'}");
      willMessage.destinationName = MQTT_RECEIVE_DESTINATION;
      global.imClient.connect({
        onSuccess:() =>{
          if(option.onSuccess){
            option.onSuccess();
          }
          //ToastUtil.showShort("mqtt服务连接成功");
          global.imClient.subscribe(user.account, { qos: 0 });
        },
        timeout:10,
        keepAliveInterval:60,
        useSSL: false,
        reconnect:true,
        willMessage:willMessage,
        onFailure:(message) =>{
          ToastUtil.showShort('连接消息服务器失败');
        }
      });
    }else{
        if(option.onSuccess){
          option.onSuccess();
        }
        global.imClient.subscribe(user.account, { qos: 0 });
        //ToastUtil.showShort('isConnected');
    }
}

function androidInitIm(option){
    const user = option.user;
    DeviceEventEmitter.addListener('connectComplete', (receiveMessage) => {
      //ToastUtil.showShort("im connection success");
      global.HtReactNativeMqtt.subscribe(user.account,0);
      if(option.onSuccess){
        option.onSuccess();
      }
    });
    DeviceEventEmitter.addListener('connectionLost', (receiveMessage) => {
      //ToastUtil.showShort("消息服务器断开");
    });

    DeviceEventEmitter.addListener('onFailure', (receiveMessage) => {
      ToastUtil.showShort('连接消息服务器失败');
    });    

    DeviceEventEmitter.addListener('messageArrived', (receiveMessage) => {
      if(option.onMessageArrived){
        option.onMessageArrived(receiveMessage);
      }
    });
    var  p = {
      serverUri:'ws://'+global.imHost+':'+global.imPort+'',
      clientId:Platform.OS+'_'+user.account+'_'+new Date().getTime(),
      account:user.account
    }
    global.HtReactNativeMqtt.initClient(p);
}
