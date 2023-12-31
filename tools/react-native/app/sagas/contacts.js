
import { put, take, call, fork } from 'redux-saga/effects';
import * as store from '../utils/StoreUtil';

import * as types from '../constants/ActionTypes';
import ToastUtil from '../utils/ToastUtil';
import RequestUtil from '../utils/RequestUtil';
import { receiveGeneralContact } from '../actions/contacts';

function* requestOrgDetail(orgCode,callback) {
  try {
    const url = global.server.uc+"/api/user/v1/user/getOrgMsg?orgCode="+orgCode;
    const message = yield call(RequestUtil.requestByStoreUser, url, 'get');
    debugger
    if(callback){
      callback(message);
    }

  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchRequestOrgDetail() {
  while (true) {
    const { orgCode,callback } = yield take(types.REQUEST_ORG_DETAIL);
    yield fork(
      requestOrgDetail,
      orgCode,
      callback
    );
  }
}

function* requestGeneralContact(params,callback) {
  try {
    const url = global.server.portal+"/im/imGeneralContact/v1/getMyGeneralContact";
    const message = yield call(RequestUtil.requestByStoreUser, url, 'get');
    if(message.success){
      yield put(receiveGeneralContact(message.users));
    }else{
      yield ToastUtil.showShort("系统错误");
    }
  } catch (error) {
    console.warn(error);
    yield ToastUtil.showShort('网络发生错误，请重试');
  }
}

export function* watchRequestGeneralContact() {
  while (true) {
    const { params,callback } = yield take(types.REQUEST_GENERAL_CONTACT);
    yield fork(
      requestGeneralContact,
      params,
      callback
    );
  }
}
