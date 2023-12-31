
import * as types from '../constants/ActionTypes';
import {ListView } from 'react-native';

const initialState = {
  myRequestList:{
    rows:[]
  },
  myTaskList:{
    rows:[]
  },
  alreadyMattersList:{
    rows:[],
  },
  myDraftList:{
    rows:[]
  },
  myCopyToList:{
    rows:[]
  },
  myTurnOutList:{
    rows:[]
  },
  applications:[]
};

export default function user(state = initialState, action) {
  switch (action.type) {
    case types.RECEIVE_MYREQUEST_LIST:
      return Object.assign({}, state, {
        myRequestList: action.myRequestList
      });
    case types.RECEIVE_MYREQUEST_LIST:
      return Object.assign({}, state, {
        myRequestList: action.myRequestList
      });
    case types.RECEIVE_MYTASK_LIST:
      return Object.assign({}, state, {
        myTaskList: action.myTaskList
      });
    case types.RECEIVE_ALREADYMATTERS_LIST:
      return Object.assign({},state, {
        alreadyMattersList: action.alreadyMattersList
      });
    case types.RECEIVE_MYDRAFT_LIST:
      return Object.assign({}, state, {
        myDraftList: action.myDraftList
      });
    case types.RECEIVE_MYCOPYTO_LIST:
      return Object.assign({}, state, {
        myCopyToList: action.myCopyToList
      });
    case types.RECEIVE_MYTURNOUT_LIST:
      return Object.assign({}, state, {
        myTurnOutList: action.myTurnOutList
      });
    case types.RECEIVE_MYWORKS:
      return Object.assign({}, state, {
        applications: action.applications
    });
    default:
      return state;
  }
}
