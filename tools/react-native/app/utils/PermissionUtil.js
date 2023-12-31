
import {Platform,PermissionsAndroid } from 'react-native';


export function checkRecordAudioPermission(onMessageArrived,user) {
  if (Platform.OS !== 'android') {
    return Promise.resolve(true);
  }

  const rationale = {
    'title': 'Microphone Permission',
    'message': '需要申请录音权限'
  };

  return PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.RECORD_AUDIO, rationale)
  .then((result) => {
    return (result === true || result === PermissionsAndroid.RESULTS.GRANTED);
  });
}
