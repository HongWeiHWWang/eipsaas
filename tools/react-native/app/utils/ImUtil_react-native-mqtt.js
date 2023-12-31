
import init from 'react_native_mqtt';
import ToastUtil from './ToastUtil';
import timeUtil from './TimeUtil';
import {AsyncStorage,Platform } from 'react-native';
import {MQTT_RECEIVE_DESTINATION,MQTT_URI } from '../constants/Constans';
import * as store from '../utils/StoreUtil';
import mqtt from 'react-native-mqtt';

export function initIm(option) {
  //用新的im客户端
  try{
    otherImInit(option);
  }catch(err){
    ToastUtil.showShort("im error:",err);
  }
  
  // const user = option.user;
  // init({
  //   size: 10000,
  //   storageBackend: AsyncStorage,
  //   defaultExpires: 1000 * 3600 * 24,
  //   enableCache: true,
  //   sync : {
  //   }
  // });
  // const options = {
  //   host: MQTT_SERVER,
  //   port: MQTT_PORT
  // }

  // if(!global.imClient){
  //   global.imClient = new Paho.MQTT.Client(options.host, options.port,Platform.OS+'_'+user.account+'_'+new Date().getTime());
  // }

  // global.imClient.disconnectedPublishing = true

  // global.imClient.onMessageArrived = (message) =>{
  //   try {
  //     if(option.onMessageArrived){
  //       option.onMessageArrived(message.payloadString);
  //     }
  //   } catch (error) {
  //     ToastUtil.showLong(error);
  //   }
  // }
  // global.imClient.onConnectionLost = () =>{
  //   ToastUtil.showShort("onConnectionLost")
  // }
  // if(!global.imClient.isConnected()){
  //   const willMessage = new Paho.MQTT.Message("{'from':'"+user.account+"','type':'offline'}");
  //   willMessage.destinationName = MQTT_RECEIVE_DESTINATION;
  //   global.imClient.connect({
  //     onSuccess:() =>{
  //       if(option.onSuccess){
  //         option.onSuccess();
  //       }
  //       ToastUtil.showShort("mqtt服务连接成功");
  //       global.imClient.subscribe(user.account, { qos: 0 });
  //     },
  //     timeout:10,
  //     keepAliveInterval:300,
  //     useSSL: false,
  //     reconnect:true,
  //     willMessage:willMessage,
  //     onFailure:(message) =>{ToastUtil.showShort("mqtt服务连接失败")}
  //   });
  // }else{
  //     if(option.onSuccess){
  //       option.onSuccess();
  //     }
  //     global.imClient.subscribe(user.account, { qos: 0 });
  //     ToastUtil.showShort('isConnected');
  // }
}

export function offline() {
  global.imClient.disconnect();
  /*const willMessage = new Paho.MQTT.Message("{'from':'"+global.loginUser.account+"','type':'offline'}");
  willMessage.destinationName = MQTT_RECEIVE_DESTINATION;
  if(global.imClient && global.imClient.isConnected()){
    imClient.send(willMessage);
    global.imClient.disconnect();
  }*/
}

export function sendMessage(message,toUser) {
  global.imClient.publish(toUser, JSON.stringify(message), 0, false);
}


function otherImInit (option){
  const user = option.user;

  mqtt.createClient({
    uri: MQTT_URI, 
    clientId: Platform.OS+'_'+user.account+'_'+new Date().getTime(),
    will:true,
    willMsg:"{'from':'"+global.loginUser.account+"','type':'offline'}",
    willtopic:MQTT_RECEIVE_DESTINATION,
    willQos:0,
    willRetainFlag:false
  }).then(function(client) {

    global.imClient = client;
    global.imClient.on('closed', function() {
      ToastUtil.showShort("connect lost")
    });
    
    global.imClient.on('error', function(msg) {
      ToastUtil.showShort("connect error")
    });

    global.imClient.on('message', function(msg) {
      if(option.onMessageArrived){
        option.onMessageArrived(msg.data);
      }
    });

    global.imClient.on('connect', function() {
      if(option.onSuccess){
        option.onSuccess();
      }
      ToastUtil.showShort("mqtt服务连接成功");
      global.imClient.subscribe(user.account,0);
    });
    global.imClient.connect();
  }).catch(function(err){
    ToastUtil.showShort("im connect error:",err);
  });
} 