
import { all, fork } from 'redux-saga/effects';

import { watchRequestUserDetail, watchLoginUser,watchUpdateUserMessage } from './user';
import { watchRequestOrgDetail,watchRequestGeneralContact } from './contacts';
import {watchRequestSessionDetail,watchRequestMoreMessages,watchCreateSession,watchUpdateSession,watchInitTeamSesionDetail } from './message';
import { watchRequestGeneralBpmList } from './work';

export default function* rootSaga() {
  yield all([
    fork(watchRequestUserDetail),
    fork(watchLoginUser),
    fork(watchRequestOrgDetail),
    fork(watchRequestSessionDetail),
    fork(watchRequestMoreMessages),
    fork(watchCreateSession),
    fork(watchRequestGeneralContact),
    fork(watchInitTeamSesionDetail),
    fork(watchRequestGeneralBpmList),
    fork(watchUpdateSession),
    fork(watchUpdateUserMessage),
    //全局监听组件注册
  ]);
}
