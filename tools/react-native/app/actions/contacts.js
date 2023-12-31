
import * as types from '../constants/ActionTypes';

export function requestOrgDetail(orgCode,callback) {
  return {
    type: types.REQUEST_ORG_DETAIL,
    orgCode,
    callback
  };
}


export function requestGeneralContact(params,callback) {
  return {
    type: types.REQUEST_GENERAL_CONTACT,
    params,
    callback
  };
}

export function receiveGeneralContact(generalContacts) {
  return {
    type: types.RECEIVE_GENERAL_CONTACT,
    generalContacts
  };
}
