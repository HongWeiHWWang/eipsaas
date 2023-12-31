
import {Vibration,Platform } from 'react-native';

const vibrate = () => {
  if(global.userSetting.vibration){
    Vibration.vibrate();
  }
};

export default {
  vibrate
};
