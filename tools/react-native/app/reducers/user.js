
import * as types from '../constants/ActionTypes';

const initialState = {
  loginUser:{},
  userDetail:{
    user:{},
    orgUserRels:[]
  }
};

export default function user(state = initialState, action) {
  switch (action.type) {
    case types.REQUEST_USER_DETAIL:
      return Object.assign({}, state, {
        account: action.account
      });
    case types.RECEIVE_USER_DETAIL:
      return Object.assign({}, state, {
        userDetail: action.userDetail
      });
    case types.RECEIVE_LOGIN_USRE:
      return Object.assign({}, state, {
        loginUser: action.loginUser
      });
    default:
      return state;
  }
}
