
import { put, take, call, fork } from 'redux-saga/effects';
import * as store from '../utils/StoreUtil';

import * as types from '../constants/ActionTypes';
import ToastUtil from '../utils/ToastUtil';
import RequestUtil from '../utils/RequestUtil';
import { receiveLoginUser } from '../actions/user';

export function* requestUserDetail(account,callback) {
  try {
    const url = global.server.uc + "/api/user/v1/user/getUserMsg?account="+account;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'get');
    if(callback){
      yield call(callback,message);
    }
  } catch (error) {
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchRequestUserDetail() {
  while (true) {
    const { account,callback } = yield take(types.REQUEST_USER_DETAIL);
    yield fork(
      requestUserDetail,
      account,
      callback
    );
  }
}

export function* loginUser(user, callback) {
    try {
      var proUrl = `${global.server.portal}/portal/main/v1/appProperties`;
      const tempTime = new Date().getTime();//用来计算网络延迟,精确计算服务器时间。
      const properties = yield call(RequestUtil.request, proUrl, 'get');
      global.imHost = properties.host;
      global.imPort = properties.port;
      const currentTime = new Date().getTime();
      global.initServerTime = (currentTime - tempTime) + properties.initServerTime;
      global.initAppTime = currentTime;
      
      if(user.password){//登录
        const url = global.server.uc+"/auth";
        const message = yield call(RequestUtil.request, url, 'post', JSON.stringify({username: user.username,password: user.password}));
        if (message.token) {
          yield call(store.save, 'loginUser', message);
          global.loginUser = message;
          yield put(receiveLoginUser(message));
          yield call(callback,message);
        } else {
          global.loginUser = '';
          yield call(store.save, 'loginUser', '');
          yield ToastUtil.showShort("用户验证失败，"+message.message);
        }
      }else if(user.token){//刷新
        const url = global.server.uc+"/refresh";
        const message = yield call(RequestUtil.requestByStoreUser, url, 'get');
        if(message.token) {
          global.loginUser.token = message.token;
          yield call(store.save, 'loginUser', global.loginUser);
          yield put(receiveLoginUser(global.loginUser));
          yield call(callback,global.loginUser);
        } else {
          global.loginUser = '';
          yield call(store.save, 'loginUser', '');
          yield ToastUtil.showShort("用户验证失败");
          yield call(callback,message);
        }
      }
  } catch (error) {
    global.loginUser = '';
    yield call(store.save, 'loginUser', '');
    yield call(callback,{});
    yield ToastUtil.showShort("系统错误，用户登录失败"+error);
  }
}

export function* watchLoginUser() {
  while (true) {
    const { user, callback } = yield take(types.LOGIN_USRE);
    yield fork(
      loginUser,
      user,
      callback
    );
  }
}


export function* updateUserMessage(params, callback) {
  try {
    const url = global.server.uc+"/api/user/v1/user/updateUserMessage";
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post', JSON.stringify(params));
    if(callback){
      yield call(callback,message);
    }
  } catch (error) {
    console.log(error);
    yield call(store.save, 'loginUser', '');
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchUpdateUserMessage() {
  while (true) {
    const { params, callback } = yield take(types.UPDATE_USER_MESSAGE);
    yield fork(
      updateUserMessage,
      params,
      callback
    );
  }
}
