
import { combineReducers } from 'redux';
import user from './user';
import contacts from './contacts';
import message from './message';
import work from './work';

const rootReducer = combineReducers({
  user,
  contacts,
  message,
  work
});

export default rootReducer;
