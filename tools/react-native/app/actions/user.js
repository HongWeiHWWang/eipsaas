
import * as types from '../constants/ActionTypes';

export function requestUserDetail(account,callback) {
  return {
    type: types.REQUEST_USER_DETAIL,
    account,
    callback
  };
}

export function receiveUserDetail(userDetail) {
  return {
    type: types.RECEIVE_USER_DETAIL,
    userDetail
  };
}

export function loginUser(user, callback) {
  return {
    type: types.LOGIN_USRE,
    user,
    callback
  };
}
export function receiveLoginUser(loginUser) {
  return {
    type: types.RECEIVE_LOGIN_USRE,
    loginUser
  };
}


export function updateUserMessage(params, callback) {
  return {
    type: types.UPDATE_USER_MESSAGE,
    params,
    callback
  };
}
