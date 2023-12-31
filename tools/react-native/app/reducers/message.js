
import * as types from '../constants/ActionTypes';
import { DeviceEventEmitter} from 'react-native';
import RequestUtil from '../utils/RequestUtil';
import * as ShortcutBadgeUtil from '../utils/ShortcutBadgeUtil';

const initialState = {
  messages:[],
  sessionJson:{},
  sessionArray:[],
  currentSession:{},
  playingVoise:false,
  allUnRead:0
};

export default function message(state = initialState, action) {
  switch (action.type) {
    case types.RECEIVE_SESSION_JSON:
      return Object.assign({}, state, {
        sessionJson: action.sessionJson,
        sessionArray:renderSessionArray(action.sessionJson)
      });
    case types.ON_OPEN_SESSION:
      return Object.assign({}, state, {
        currentSession: action.currentSession
      });
    case types.ON_PLAY_VOISE:
      return Object.assign({}, state, {
        playingVoise: action.playingVoise
      });
    case types.ON_REFRESE_UNREAD_NUM:
      refreshShortcutBadger(action.num);
      return Object.assign({}, state, {
        allUnRead: action.num
      });
      
    default:
      return state;
  }
}

function refreshShortcutBadger(num){
    ShortcutBadgeUtil.setBadge(num);
}

function renderSessionArray(sessionJson){
  const array = Object.values(sessionJson);
  const sortArray = array.sort(function(a, b) {
    return  b.lastReadTime - a.lastReadTime;
  });
  return sortArray;
}
