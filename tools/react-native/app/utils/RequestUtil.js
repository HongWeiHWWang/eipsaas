
import * as store from './StoreUtil';

const request = (url, method, body) => {
  let isOk;
  const headers = {
    'Content-Type': 'application/json;charset=utf-8'
  };

  return new Promise((resolve, reject) => {
    setTimeout(function() {
      reject(new Error("timeout"))
    }, 10000);
    fetch(url, {
      method,
      headers,
      body
    }).then((response) => {
      if (response.ok) {
        isOk = true;
      } else {
        isOk = false;
      }
      return response.json();
    }).then((responseData) => {
      if (isOk) {
        resolve(responseData);
      } else {
        resolve(responseData);//暂时都用resolve
      }
    }).catch((error) => {
      reject(error);
    });
  });
};

// 请求会加上store的用户信息
const requestByStoreUser = (url, method, body) => {
  let isOk;
  const headers = {
    'Content-Type': 'application/json;charset=utf-8'
  };
  return new Promise((resolve, reject) => {
    store.get('loginUser').then((loginUser) => {
      if (loginUser) {
        headers.Authorization = `Bearer ${loginUser.token}`;
      }
      fetch(url, {
        method,
        headers,
        body
      }).then((response) => {
        if (response.ok) {
          isOk = true;
        } else {
          isOk = false;
        }
        return response.json();
      }).then((responseData) => {
        if (isOk) {
          resolve(responseData);
        } else {
          resolve(responseData);//暂时都用resolve
        }
      }).catch((error) => {
        reject(error);
      });
    });
  });
};
export default {
  request, requestByStoreUser
};
