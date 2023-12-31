import RNFetchBlob from 'rn-fetch-blob';
import { MESSAGE_AUDIO_SUFFIX} from '../constants/Constans';
import { Linking } from 'react-native';
export async function downloadToPath(fileId,path) {
  return new Promise((resolve, reject) => {
    RNFetchBlob.config({
        path : path
      }).fetch('GET', __CTX+'/system/file/download?id='+fileId, {
      }).then((res) => {
        resolve(res.path())
      }).catch((err) => {
        console.warn(err)
        reject(false)
      })
  });
}

export function linkTodownload(fileId) {
  const url = global.server.portal+'/system/file/download?id='+fileId;
  Linking.canOpenURL(url).then((supported) => {
    if(supported){
      Linking.openURL(url);
    }
  });
}
