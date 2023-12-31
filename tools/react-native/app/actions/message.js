
import * as types from '../constants/ActionTypes';


export function requestSessionDetail(sessionCode,callback) {
  return {
    type: types.REQUEST_SESSION_DETAIL,
    sessionCode,
    callback
  };
}

export function requestMoreMessages(params,callback) {
  return {
    type: types.REQUEST_MORE_MESSAGES,
    params,
    callback
  };
}

export function updateSession(params,callback) {
  return {
    type: types.UPDATE_SESSION,
    params,
    callback
  };
}

export function createSession(params,callback) {
  return {
    type: types.CREATE_SESSION,
    params,
    callback
  };
}

export function receiveSessionJson(sessionJson) {
  return {
    type: types.RECEIVE_SESSION_JSON,
    sessionJson
  };
}

export function onOpenSession(currentSession) {

  return {
    type: types.ON_OPEN_SESSION,
    currentSession
  };
}

export function removeSession(params) {
  return {
    type: types.REMOVE_SESSION,
    params
  };
}

export function initTeamSesionDetail(params) {
  return {
    type: types.INIT_TEAMSESSION_DETAIL,
    params
  };
}

export function onPlayVoise(playingVoise) {
  return {
    type: types.ON_PLAY_VOISE,
    playingVoise
  };
}

export function onRefreseUnReadNum(num) {
  return {
    type: types.ON_REFRESE_UNREAD_NUM,
    num
  };
}

