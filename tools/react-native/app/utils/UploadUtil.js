import RNFetchBlob from 'rn-fetch-blob';
import Toast from 'antd-mobile-rn/lib/toast';

export async function fileUpload(options) {
  return new Promise((resolve, reject) => {
    RNFetchBlob.fetch('POST', options.url, {
      Authorization : `Bearer ${global.loginUser.token}`,
      'Content-Type' : 'multipart/form-data',
    }, options.files)
    .uploadProgress({ interval : 250 },(written, total) => {
      if(options.uploadProgress){
        options.uploadProgress(written / total);
      }
    })
    .then((resp) => {
      if(resp.data){
        resolve(resp.data);
      }else{
        resolve(resp)
      }
    }).catch((err) => {
      Toast.hide()
      resolve('{"success":false}')
    })
  });
}
