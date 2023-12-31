
import * as types from '../constants/ActionTypes';

export function requestGeneralBpmList(params,callback) {
  return {
    type: types.REQUEST_GEMERALBPM_LIST,
    params,
    callback
  };
}


export function receiveMyRequestList(myRequestList) {
  return {
    type: types.RECEIVE_MYREQUEST_LIST,
    myRequestList
  };
}

export function receiveMyTaskList (myTaskList) {
  return {
    type: types.RECEIVE_MYTASK_LIST,
    myTaskList
  };
}

export function receiveAlreadyMattersList(alreadyMattersList) {
  return {
    type: types.RECEIVE_ALREADYMATTERS_LIST,
    alreadyMattersList
  };
}

export function receiveMyDraftList (myDraftList) {
  return {
    type: types.RECEIVE_MYDRAFT_LIST,
    myDraftList
  };
}

export function receiveMyCopyToList (myCopyToList) {
  return {
    type: types.RECEIVE_MYCOPYTO_LIST,
    myCopyToList
  };
}

export function receiveMyTurnOutList (myTurnOutList) {
  return {
    type: types.RECEIVE_MYTURNOUT_LIST,
    myTurnOutList
  };
}

export function receiveMyWorks (applications) {
  return {
    type: types.RECEIVE_MYWORKS,
    applications
  };
}
