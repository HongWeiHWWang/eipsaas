
import * as types from '../constants/ActionTypes';

const initialState = {
  generalContacts:[]
};

export default function contacts(state = initialState, action) {
  switch (action.type) {
    case types.REQUEST_ORG_DETAIL:
      return Object.assign({}, state, {
        orgCode: action.orgCode,
        callback:action.callback
      });
    case types.RECEIVE_GENERAL_CONTACT:
      return Object.assign({}, state, {
        generalContacts: action.generalContacts
      });
    default:
      return state;
  }
}
