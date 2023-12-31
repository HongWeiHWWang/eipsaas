import {
  DeviceEventEmitter,
} from 'react-native';
import NotificationsUtil from '../../utils/NotificationsUtil';
import vibrationUtil from '../../utils/VibrationUtil';
import * as store from '../../utils/StoreUtil';
import RequestUtil from '../../utils/RequestUtil';
import {__CTX } from '../../constants/Constans';
import * as timeUtil from '../../utils/TimeUtil';
import * as arrayUtil from '../../utils/ArrayUtil';
import * as dbService from '../../db/DbService';
import NavigationUtil from '../../utils/NavigationUtil';
import ToastUtil from '../../utils/ToastUtil';


global.sessionIds = new Map();//保存会话的消息ids
//有消息到达
export function onMessageArrived(message,props){
  const {sessionJson,currentSession} = props.message;
  const {messageActions} = props;
  addStoreMessage([message]);

  if(!sessionJson[message.sessionCode]){
    //聊天会话被删除,需要显示会话
    const session = {
      sessionCode:message.sessionCode
    }
    messageActions.updateSession({type:'showSession',session:session},(sessionList) =>{
      sessionList.map(function(item,i){
        insertSession(item,JSON.stringify(message))
        item.sessionUnRead = 1;
        item.lastReadTime = timeUtil.getServerTime();
        sessionJson[item.sessionCode] = item;
      })
      messageActions.receiveSessionJson(sessionJson);
    });

    NotificationsUtil.localNotification(message);
    vibrationUtil.vibrate();
    messageActions.onRefreseUnReadNum(props.message.allUnRead+1);
  }else if(sessionJson[message.sessionCode]){
    //会话列表已经有会话
    const newSession = sessionJson[message.sessionCode];
    newSession.sessionLastText = JSON.stringify(message);
    if(currentSession.sessionCode && message.sessionCode == currentSession.sessionCode){
      //正在聊天会话页面
      DeviceEventEmitter.emit('onCurrentSessionMessage',message);
      updateLastReadTime(currentSession.sessionCode);
    }else {
      NotificationsUtil.localNotification(message);
      vibrationUtil.vibrate();
      newSession.sessionUnRead += 1;
      messageActions.onRefreseUnReadNum(props.message.allUnRead+1);
    }
    const updateSql = "UPDATE 'session' SET last_text_ = '"+newSession.sessionLastText+"' WHERE session_code_ = '"+newSession.sessionCode+"'";
    dbService.executeSql(updateSql);
    newSession.lastReadTime = timeUtil.getServerTime();
    sessionJson[message.sessionCode] = newSession;
    messageActions.receiveSessionJson(sessionJson)
  }
}

//打开会话，进入会话页面后会调用
export function onOpenSession(session,props){
  const {sessionJson,currentSession} = props.message;
  const {messageActions} = props;
  messageActions.onOpenSession(session);
  updateLastReadTime(session.sessionCode);
  if(sessionJson[session.sessionCode]){
    //会话列表已经有会话
    const newSession = sessionJson[session.sessionCode];
    reduceSessionUnRead(props,newSession)
    newSession.sessionUnRead  = 0 ;
    newSession.lastReadTime = timeUtil.getServerTime();
    sessionJson[session.sessionCode] = newSession;
    messageActions.receiveSessionJson(sessionJson);
  }else{
    const url = global.server.portal+"/im/imSessionUser/v1/mySessionList";
    RequestUtil.requestByStoreUser(url,'post',JSON.stringify({sessionCode:session.sessionCode})).then((rtn) =>{
      rtn.imSessionUserList.map((val, i) => {
          val.sessionLastText = "";
          sessionJson[val.sessionCode] = val;
          messageActions.receiveSessionJson(sessionJson);
          insertSession(val,"");
      });
    })
  }
}

//创建一个会话，这里表示在用户信息页发起聊天或者创建群组的时候
export function createSession(params,props){
  //const {sessionJson,currentSession} = props.message;
  const {messageActions} = props;
  const { navigate } = props.navigation;
  messageActions.createSession(params,(message) =>{
    const session ={
      sessionCode:message.imMessageSession.code,
      sessionTitle:message.imMessageSession.title,
      sessionScene:message.imMessageSession.scene
    }
    NavigationUtil.push(props.navigation,'ChatSession',{session});
  })
}

export function onSendMessage(sendMessage,props){
  const {sessionJson} = props.message;
  const {messageActions} = props;
  updateLastReadTime(sendMessage.sessionCode);
  if(sessionJson[sendMessage.sessionCode]){
    //会话列表已经有会话
    const newSession = sessionJson[sendMessage.sessionCode];
    newSession.sessionLastText  = JSON.stringify(sendMessage) ;
    sessionJson[sendMessage.sessionCode] = newSession;
    messageActions.receiveSessionJson(sessionJson)
  }
  addStoreMessage([sendMessage])
}

//添加消息到本地存储
export async function addStoreMessage(messages){
  global.dbEntity.transaction((tx) => {
    for(let i = 0 ; i < messages.length ; i++){
        const message = messages[i];
        if(!message.messageId || !message.content){
            continue;
        }
        const content = typeof message.content == 'string' ? message.content:JSON.stringify(message.content);
        const sql = "INSERT INTO message (id_,message_id_,session_code_,type_,from_,content_,send_time_,owner_) VALUES (null,'"+message.messageId+"','"+message.sessionCode+"','"+message.type+"','"+message.from+"','"+content+"','"+message.sendTime+"','"+global.loginUser.account+"');";
        tx.executeSql(sql, [],(result)=>{
          
        },(err) =>{
          console.warn(err)
        });
    }
  }, (error) => {
    
    console.warn(error)
  }, () => {

  });
}

//更新最后阅读时间,目前只更新本地保存会话的最后阅读时间
export function updateLastReadTime(sessionCode){
  const updateSql = "UPDATE 'session' SET last_read_time_ = '"+timeUtil.getServerTime()+"' WHERE session_code_ = '"+sessionCode+"' and owner_ = '"+global.loginUser.account+"'";
  dbService.executeSql(updateSql);

  // if(global.netInfoisConnected){
  //   const lastReadTimeStoreKey = sessionCode+'_lastReadTime_'+global.loginUser.account;
  //   store.save(lastReadTimeStoreKey,timeUtil.getServerTime());
  // }
}

//获取会话列表,先获取本地的列表，然后拉取远程会话列表更新。
//更新未读信息是根据本地保存会话的最后消息的发送时间和本地会话创建时间来获取的，未读数根据本地保存会话的最后阅读时间来计算
export async function requestSessionList(props){
  const url = global.server.portal+"/im/imSessionUser/v1/mySessionList";
  
  const rtn = await RequestUtil.requestByStoreUser(url,'post','{}')
  if(!rtn.success){
    ToastUtil.showShort('获取会话失败');
    return;
  }
  const sessionList = rtn.imSessionUserList;
  const {messageActions} = props;
  const sessionCodeArray = [];
  //const {sessionJson} = props.message;
  const sessionJson = [];
  //获取本地保存的会话
  const sql = "select DISTINCT s.session_code_ as sessionCode,s.title_ as sessionTitle,s.last_text_ as sessionLastText, s.last_read_time_ as lastReadTime,(select send_time_ from message m where s.session_code_ = m.session_code_ and m.owner_ = s.owner_ order by m.send_time_ desc limit 1) as lastMessageTime,(select count(*) from message m where m.send_time_  > s.last_read_time_ and s.session_code_ = m.session_code_ and m.owner_ = s.owner_ ) as sessionUnRead,create_time_ as localCreateTime from session s where owner_ = '"+global.loginUser.account+"';";
  const hasUnReadMsgMsgSession = [];//保存需要到后台获取新消息的会话
  const needUpdateSession = [];//保存需要更新本地信息的会话
  
  dbService.executeSql(sql,function(result){
    sessionList.map((newSession, i) => {
      let hasSession = false;
      result.map(function(oldSession){
        if(oldSession.sessionCode == newSession.sessionCode){
          hasSession = true;
          newSession.sessionUnRead = oldSession.sessionUnRead;
          newSession.lastReadTime = oldSession.lastReadTime;
          //判断是否需要从后台获取消息
          if(newSession.sessionLastText){
            //本地会话有消息保存，则判断本地会话最后一条消息时间和远程会话最后一条消息时间
            if(oldSession.lastMessageTime){
              const newLastText = JSON.parse(newSession.sessionLastText);
              if(oldSession.lastMessageTime < newLastText.sendTime){
                hasUnReadMsgMsgSession.push(oldSession);
              }
            //本地没有保存消息，且本地会话创建时间小于于服务器会话最后消息时间，就根据本地会话创建时间去获取历史记录
            }else if(oldSession.create_time_){
              const newLastText = JSON.parse(newSession.sessionLastText);
              if(oldSession.create_time_ < newLastText.sendTime){
                hasUnReadMsgMsgSession.push(oldSession);
              }
            }else{
              //本地没有保存消息，且本地会话创建时间大于服务器会话最后消息时间
              newSession.sessionLastText = "";
            }
          }
        }
      });
      if(!hasSession){//如果本地没有保存,就要加进去
        insertSession(newSession,newSession.sessionLastText);
        hasUnReadMsgMsgSession.push(newSession);
      }else{
        needUpdateSession.push(needUpdateSession);
      }
      sessionJson[newSession.sessionCode] = newSession;
    });
    messageActions.receiveSessionJson(sessionJson);

    //以下是到后台获取消息,根据hasUnReadMsgMsgSession去获取，根据本地最后一条消息和本地会话创建时间来获取
    if(hasUnReadMsgMsgSession.length > 0){
      const params = [];
      hasUnReadMsgMsgSession.map(function(unReadSession,i){
        let sendTime = 0;
        if(unReadSession.lastMessageTime){
          sendTime = unReadSession.lastMessageTime;
        }else if(unReadSession.localCreateTime){
          sendTime = unReadSession.localCreateTime;
        }
        params.push({sessionCode:unReadSession.sessionCode,sendTime:sendTime})
      })
      const messageUrl = global.server.portal+"/im/imMessageSession/v1/refreshSessionHistory";
      RequestUtil.requestByStoreUser(messageUrl,'post',params).then((message) =>{
        if(message.success){
          let messages = [];
          const history = message.history;
          for(let [key,values] of Object.entries(history)){           
            addStoreMessage(values);
            if(sessionJson[key]){
              sessionJson[key].sessionUnRead += values.length;
            }
          } 
          messageActions.receiveSessionJson(sessionJson); 

          //更新未读的消息
          const allUnRead = 0;
          Object.values(sessionJson).map(function(session){
            if(session.sessionUnRead){
              allUnRead += session.sessionUnRead
            }
          })
          messageActions.onRefreseUnReadNum(allUnRead);
        }
      });
    }
    //更新本地会话信息
    if(needUpdateSession.length > 0){
      updateLocalSession(needUpdateSession);
    }
  }) 
  
}

function updateLocalSession(newSessions){
  global.dbEntity.transaction((tx) => {
    for(let i = 0 ; i < newSessions.length ; i++){
        const session = newSessions[i];
        const sql = "update 'session' set last_text_ = '"+newSessions.sessionLastText+"', icon_ = '"+newSessions.sessionIcon+"', title_ = '"+newSessions.sessionTitle+"'  where owner_ = '"+global.loginUser.account+"' and session_code_ = '"+newSessions.sessionCode+"';";
        tx.executeSql(sql, [],(result)=>{
          
        },(err) =>{
          console.warn(err)
        });
    }
  }, (error) => {
    
    console.warn(error)
  }, () => {
  });
}

function insertSession(session,message){
    const sql = "INSERT INTO session (id_,session_code_,title_,last_text_,scene_,icon_,owner_,last_read_time_,create_time_)"
          +" VALUES (null,'"+session.sessionCode+"','"+session.sessionTitle+"','"+message+"','"+session.sessionScene+"','"+session.sessionIcon+"','"+global.loginUser.account+"',0,'"+timeUtil.getServerTime()+"');";
    dbService.executeSql(sql)
}

export async function requestSessionDetail(params,callback){
  const url = global.server.portal+"/im/imMessageSession/v1/initSession?sessionCode="+params.sessionCode;
  const message = await RequestUtil.requestByStoreUser(url,'get')
  if(callback){
    callback(message);
  }
  if(message.success){
    const sessionDetailStoreKey = renderSessionDetailKey(params.sessionCode);
    store.save(sessionDetailStoreKey,message);
  }
}

export function removeMessageHistory(sessionCode){
  //删除历史
  updateLastReadTime(sessionCode);
  const sql = "delete from message where session_code_ = '"+sessionCode+"' and owner_ = '"+global.loginUser.account+"'";
  dbService.executeSql(sql);
}

export function removeSession(sessionCode,props){
  const {sessionJson,allUnRead} = props.message;
  const {messageActions} = props;
  const sql = "delete from session where session_code_ = '"+sessionCode+"' and owner_ = '"+global.loginUser.account+"'";
  dbService.executeSql(sql);
  reduceSessionUnRead(props,sessionJson[sessionCode])
  delete sessionJson[sessionCode];
  messageActions.receiveSessionJson(sessionJson);
}

//减去总未读数
function reduceSessionUnRead(props,session){
  const {messageActions} = props;
  const {allUnRead} = props.message;
  if(session.sessionUnRead && allUnRead){
    const num = allUnRead - session.sessionUnRead;
    messageActions.onRefreseUnReadNum(num >= 0 ? num : 0);
  }
}

//用于保存会话详细信息的key
export function renderSessionDetailKey(sessionCode){
  return sessionCode+'_detail_'+global.loginUser.account;
}
