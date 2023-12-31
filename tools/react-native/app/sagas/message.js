
import { put, take, call, fork } from 'redux-saga/effects';
import * as store from '../utils/StoreUtil';

import * as types from '../constants/ActionTypes';
import ToastUtil from '../utils/ToastUtil';
import RequestUtil from '../utils/RequestUtil';
import {receiveSessionJson } from '../actions/message';

export function* requestSessionDetail(sessionCode,callback) {
  try {
    const session = {
      sessionCode:sessionCode
    }
    const url = `${global.server.portal}/im/imMessageSession/initSession`;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(session));
    if(callback){
      yield call(callback,message);
    }
  } catch (error) {
    console.log(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchRequestSessionDetail() {
  while (true) {
    const { sessionCode,callback } = yield take(types.REQUEST_SESSION_DETAIL);
    yield fork(
      requestSessionDetail,
      sessionCode,
      callback
    );
  }
}

export function* requestMoreMessages(params,callback) {
  try {
    const url = `${global.server.portal}/im/imMessageSession/refreshMessageHistory`;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    if(message.success && callback){
      yield call(callback,message.messages);
    }
  } catch (error) {
    console.log(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}
export function* watchRequestMoreMessages() {
  while (true) {
    const { params,callback } = yield take(types.REQUEST_MORE_MESSAGES);
    yield fork(
      requestMoreMessages,
      params,
      callback
    );
  }
}

export function* createSession(params,callback) {
  try {
    const url = global.server.portal+"/im/imMessageSession/v1/createSession?scene="+params.scene+"&userStr="+params.userStr;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post','{}');
    if(message.success){
      callback(message)
    }
  } catch (error) {
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}
export function* watchCreateSession() {
  while (true) {
    const { params,callback } = yield take(types.CREATE_SESSION);
    yield fork(
      createSession,
      params,
      callback
    );
  }
}

export function* initTeamSesionDetail(params) {
  try {
    const url = global.server.portal+"/im/imMessageSession/v1/initTeamSessionDetail?sessionCode="+params.sessionCode;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'get');
    if(message.success){
      if(params.callback){
        yield call(params.callback,message);
      }
    }else{
      yield ToastUtil.showShort('系统错误');
    }
  } catch (error) {
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchInitTeamSesionDetail() {
  while (true) {
    const { params } = yield take(types.INIT_TEAMSESSION_DETAIL);
    yield fork(
      initTeamSesionDetail,
      params
    );
  }
}

export function* showSession(params,callback) {
  try {
    const p = {
      sessionCode:params.session.sessionCode
    }
    const url = `${global.server.portal}/im/imSessionUser/showSession`;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(p));
    if(message.success){
      if(callback){
        yield call(callback,message.imSessionUserList);
      }
      /*const sessionJson = params.sessionJson;
      message.imSessionUserList.map((item, i) => {
        item.sessionUnRead += 1;
        sessionJson[item.sessionCode] = item;
      });
      yield put(receiveSessionJson(sessionJson));*/
    }else{
      yield ToastUtil.showShort('系统错误');
    }
  }catch (error) {
    console.warn(error)
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* removeSession(params,callback) {
  try {
    const p = {
      sessionCode:params.session.sessionCode
    }
    const url = `${global.server.portal}/im/imSessionUser/remove`;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(p));
    if(message.success){
      if(callback){
        yield call(callback);
      }
    }else{
      yield ToastUtil.showShort('系统错误');
    }
  } catch (error) {
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}
export function* updateTeamMessage(params,callback) {
  try {
    const sendParams = params;
    delete sendParams['type'];
    delete sendParams['callback'];
    const url = global.server.portal+ "/im/imSessionUser/v1/updateTeamMessage";
    const message = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(sendParams));
    if(callback){
      yield call(callback,message);
    }
  } catch (error) {
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchUpdateSession() {
  while (true) {
    const { params,callback } = yield take(types.UPDATE_SESSION);
    switch (params.type) {
      case 'showSession':
        yield fork(showSession,params,callback);
        break;
      case 'removeSession':
        yield fork(removeSession,params,callback);
        break;
      case 'updateTeamMessage':
        yield fork(updateTeamMessage,params,callback);
        break;
      default:
    }
  }
}
