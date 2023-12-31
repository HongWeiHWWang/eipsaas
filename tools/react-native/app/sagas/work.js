
import { put, take, call, fork } from 'redux-saga/effects';
import * as store from '../utils/StoreUtil';

import * as types from '../constants/ActionTypes';
import ToastUtil from '../utils/ToastUtil';
import RequestUtil from '../utils/RequestUtil';
import { receiveMyRequestList,receiveMyTaskList,receiveAlreadyMattersList,receiveMyDraftList,receiveMyCopyToList,receiveMyTurnOutList } from '../actions/work';

export function* requestMyRequestList(params,callback) {
  try {
    const url = `${__CTX}/mobile/bpm/myRequestListJson`;
    const myRequestlist = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    yield put(receiveMyRequestList(myRequestlist));
    if(callback){
      yield call(callback);
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* requestMyTaskList(params,callback) {
  try {
    const url = `${__CTX}/mobile/bpm/getMyTask`;
    const myTask = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    yield put(receiveMyTaskList(myTask));
    if(callback){
      yield call(callback);
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* requestMyDraftList(params,callback) {
  try {
    const url = `${__CTX}/mobile/bpm/getMyDraft`;
    const myDraft = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    yield put(receiveMyDraftList(myDraft));
    if(callback){
      yield call(callback);
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* requestMyCopyToList(params,callback) {
  try {
    const url = `${__CTX}/mobile/bpm/getCopyToJson`;
    const myCopyTo = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    yield put(receiveMyCopyToList(myCopyTo));
    if(callback){
      yield call(callback);
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* requestMyTurnOutList(params,callback) {
  try {
    const url = `${__CTX}/mobile/bpm/getMyTurnOutJson`;
    const myTurnOut = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    yield put(receiveMyTurnOutList(myTurnOut));
    if(callback){
      yield call(callback);
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* requestAlreadyMattersList(params,callback) {
  try {
    const url = `${__CTX}/mobile/bpm/getAlreadyMattersList`;
    const alreadyMattersList = yield call(RequestUtil.requestByStoreUser, url, 'post',JSON.stringify(params));
    yield put(receiveAlreadyMattersList(alreadyMattersList));
    if(callback){
      yield call(callback);
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchRequestGeneralBpmList() {
  while (true) {
    const { params,callback } = yield take(types.REQUEST_GEMERALBPM_LIST);
    switch (params.type) {
      case 'myRequest':
        yield fork(requestMyRequestList,params,callback);
        break;
      case 'myTask':
        yield fork(requestMyTaskList,params,callback);
        break;
      case 'myDraft':
        yield fork(requestMyDraftList,params,callback);
        break;
      case 'myCopyTo':
        yield fork(requestMyCopyToList,params,callback);
        break;
      case 'myTurnOut':
        yield fork(requestMyTurnOutList,params,callback);
        break;
      case 'alreadyMatters':
        yield fork(requestAlreadyMattersList,params,callback);
        break;
      default:
    }
  }
}
