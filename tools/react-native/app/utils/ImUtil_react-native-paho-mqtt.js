
import init from 'react_native_mqtt';
import ToastUtil from './ToastUtil';
import timeUtil from './TimeUtil';
import {AsyncStorage,Platform } from 'react-native';
import {MQTT_RECEIVE_DESTINATION,MQTT_PORT,MQTT_SERVER } from '../constants/Constans';

import { Client, Message } from 'react-native-paho-mqtt';

export function initIm(option) {
  const user = option.user;
  const myStorage = {
    setItem: (key, item) => {
      myStorage[key] = item;
    },
    getItem: (key) => myStorage[key],
    removeItem: (key) => {
      delete myStorage[key];
    },
  };
  const willMessage = new Paho.MQTT.Message("{'from':'"+global.loginUser.account+"','type':'offline'}");
  willMessage.destinationName = MQTT_RECEIVE_DESTINATION;
  global.imClient = new Client({ 
    uri: 'ws://'+MQTT_SERVER+':'+MQTT_PORT+'/ws', 
    clientId: Platform.OS+'_'+user.account+'_'+new Date().getTime(), 
    storage: myStorage,
    willMessage:willMessage
  });
  global.imClient.on('connectionLost', (responseObject) => {
    if (responseObject.errorCode !== 0) {
      console.warn(responseObject.errorMessage);
    }
  });
  global.imClient.on('messageReceived', (message) => {
    if(option.onMessageArrived){
      option.onMessageArrived(message.payloadString);
    }
    ToastUtil.showShort(global.imClient.getTraceLog());
  });

  // connect the client
  global.imClient.connect({keepAliveInterval:20})
    .then(() => {
      if(option.onSuccess){
          option.onSuccess();
        }
        ToastUtil.showShort("mqtt服务连接成功");
        global.imClient.subscribe(user.account); 
    })
    .then(() => {
      // const message = new Message('Hello');
      // message.destinationName = 'World';
      // client.send(message);
    })
    .catch((responseObject) => {
       ToastUtil.showShort("连接失败,"+responseObject);
    });

    global.imClient.on('connectionLost', function(e){
      ToastUtil.showShort("new mqtt connectionLost :"+e.errorCode);
      
    });
}

export function offline() {
  debugger
  const willMessage = new Message("{'from':'"+global.loginUser.account+"','type':'offline'}");
  willMessage.destinationName = MQTT_RECEIVE_DESTINATION;
  if(global.imClient && global.imClient.isConnected()){
    global.imClient.send(willMessage);
    global.imClient.disconnect();
  }
}

export function sendMessage(message,toUser) {
  const m = new Message(JSON.stringify(message));
  m.destinationName = toUser;
  global.imClient.send(m);
}