
import { Platform } from 'react-native';
import {MESSAGE_TYPE_SYSTEM,MESSAGE_TYPE_VIDEO, MESSAGE_TYPE_TEXT,MESSAGE_TYPE_AUDIO, MESSAGE_TYPE_IMAGE, MESSAGE_TYPE_FILE, SESSION_P2P, SESSION_TEAM } from '../constants/Constans';

const localNotification = (message) => {
  
};

const renderMessageText = (message) =>{
  switch (message.type) {
    case MESSAGE_TYPE_TEXT:
      return JSON.parse(message.content).text;
      break;
    case MESSAGE_TYPE_IMAGE:
      return '图片';
      break;
    case MESSAGE_TYPE_FILE:
      return '文件';
      break;
    case MESSAGE_TYPE_AUDIO:
      return '语音';
      break;
    case MESSAGE_TYPE_VIDEO:
      return '视频';
      break;
    case MESSAGE_TYPE_SYSTEM:
      return '系统通知';
      break;
    default:
      return '';
  }
}

export default {
  localNotification
};
