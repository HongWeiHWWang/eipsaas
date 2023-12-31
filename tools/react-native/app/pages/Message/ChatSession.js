
import React from 'react';
import { StyleSheet, Platform, Dimensions, TextInput, DeviceEventEmitter, RefreshControl, Text,View,SafeAreaView  } from 'react-native';
import { NavigationActions } from 'react-navigation';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import Icon from 'react-native-vector-icons/Ionicons';
import ToastUtil from '../../utils/ToastUtil';
import moment from 'moment';
import { GiftedChat } from './giftedChat/GiftedChat';
import Grid from 'antd-mobile-rn/lib/grid';
import InputItem from 'antd-mobile-rn/lib/input-item';
import * as store from '../../utils/StoreUtil';
import * as messageCreators from '../../actions/message';
import {
  MESSAGE_TYPE_TEXT,
  MESSAGE_TYPE_IMAGE,
  MESSAGE_TYPE_FILE,
  SESSION_P2P,
  SESSION_TEAM,
  MQTT_RECEIVE_DESTINATION,
  MESSAGE_STATUS_FIRST } from '../../constants/Constans';
import * as userCreators from '../../actions/user';
import * as SessionService from '../Message/SessionService';
import * as timeUtil from '../../utils/TimeUtil';
import * as dbService from '../../db/DbService';
import * as imUtil from '../../utils/ImUtil';


let onScroll = false;
let lastSendMessage = '';
class ChatSession extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    headerTitle: navigation.state.params.session.sessionTitle,
    headerRight: (
      <Icon onPress={navigation.state.params ? navigation.state.params.headerRightPress : null} style={styles.headerRightIcon} name="md-person" size={24} color="#fff" />
    ),
    headerTitleStyle: {
      alignSelf:'center',
      fontSize:18,
    },
  });
  constructor(props) {
    super(props);
    this.state = {
      currentSession: {},
      newMessages: [],
      userDetail: {
        user: {}
      },
      loadMore: false,
      messageActions: {},
      topOfLoadMoreMessages:0,
      messageIds:[],
      isLoadingMsg:false
    };
  }

  componentWillUnmount() {
    SessionService.updateLastReadTime(this.state.currentSession.session.code)
    this.props.messageActions.onOpenSession({});
    DeviceEventEmitter.removeAllListeners('onCurrentSessionMessage');
  }

  componentDidMount() {
    const { params } = this.props.navigation.state;
    const session = params.session;
    DeviceEventEmitter.addListener('onCurrentSessionMessage', (receiveMessage) => {
      if(receiveMessage.sessionCode == this.state.currentSession.session.code){
        this.onMessageArrived(receiveMessage);
      }
    });

    store.get('userDetail').then((userDetail) => {
      this.setState({
        userDetail
      });
    });

    const {messageActions} = this.props;
    //需要更新会话信息
    SessionService.requestSessionDetail({sessionCode:session.sessionCode},(message) =>{
      if (message.success) {
        this.setState({
          currentSession: message
        });
        this.loadStoreMessages(session);
      }else{
        this.props.navigation.goBack();
      }
    });

    this.props.navigation.setParams({
      headerRightPress: this.headerRightPress
    });

    SessionService.onOpenSession(session,this.props)
  }

  headerRightPress = () => {
    const userActions = this.props.userActions;
    const { currentSession } = this.state;
    // 查看用户信息
    if (SESSION_P2P == currentSession.session.scene) {
      for (const key in currentSession.sessionUsers) {
        if (key != currentSession.session.userAccount) {
          userActions.requestUserDetail(key, (message) => {
            if(message.success){
                this.props.navigation.navigate('OtherUserDetail', {
                user: message
              });
            }
          });
        }
      }
    } else if(SESSION_TEAM == currentSession.session.scene) {
      this.props.navigation.navigate('TeamSessionDetail', {sessionCode:currentSession.session.code});
    }
  }

  onMessageArrived = (receiveJson) => {
    this.updateMessages(receiveJson);
  }

  updateMessages = (message) =>{
    this.setState(previousState => ({
      newMessages: GiftedChat.append(previousState.newMessages, message),
    }));
  }

  onSend(message = {}) {
    const { currentSession } = this.state;
    const session = this.state.currentSession.session;
    message.sessionCode = session.code;
    message.from = session.userAccount;
    message.sendTime = timeUtil.getServerTime();
    for (const key in currentSession.sessionUsers) {
      if (key == currentSession.session.userAccount) {
        continue;
      }
      this.doSendMessage(message, key);
    }
    // 需要给服务器发送历史记录
    this.doSendMessage(message, MQTT_RECEIVE_DESTINATION);
    // 需要给服务器发送历史记录end

    SessionService.onSendMessage(lastSendMessage,this.props);

  }

  //待完成
  onResend(message = {}) {
    // const { currentSession } = this.state;
    // const session = this.state.currentSession.session;
    // message.sessionCode = session.code;
    // message.from = session.userAccount;
    // message.sendTime = timeUtil.getServerTime();
    // SessionService.addStoreMessage(message,true);
    // const m = this.state.newMessages;
    // let i = m.length;
    // const newMessages = [];
    // while(i--){
    //   if(!message.messageId == m[i].messageId){
    //     newMessages.push(m[i].messageId)
    //   }
    // }
    // this.setState({
    //   newMessages:newMessages
    // })
    // this.onAppendMessages(message);
  }

  onAppendMessages(message = {}) {
    const { currentSession } = this.state;
    const session = this.state.currentSession.session;
    message.sessionCode = session.code;
    message.from = session.userAccount;
    message.sendTime = timeUtil.getServerTime();
    SessionService.addStoreMessage(message,false);
    this.updateMessages(message);
  }

  doSendMessage = (message, toUser) => {
    
    imUtil.sendMessage(message, toUser)
    lastSendMessage = message;
    
    // global.imClient.publish(toUser, JSON.stringify(message), 0, false);
    //     /*const m = new Message(JSON.stringify(message));
    //     m.destinationName = toUser;
    //     global.imClient.send(m);
    //     return;*/
    // return;
    // const imMessage = new Paho.MQTT.Message(JSON.stringify(message));
    // imMessage.destinationName = toUser;
    // if(imClient.isConnected){
    //   try {
    //     imClient.send(imMessage);
    //   } catch (e) {
    //     ToastUtil.showShort('无法连接网络');
    //   }
    // }else{
    //   ToastUtil.showShort('无法连接网络');
    // }
  }

  _onScroll = (event) => {
    if (this.state.loadMore) {
      return;
    }
    const y = event.nativeEvent.contentOffset.y;
    const height = event.nativeEvent.layoutMeasurement.height;
    const contentHeight = event.nativeEvent.contentSize.height;
    if (y + height >= contentHeight - 20) {
      this.loadStoreMessages(this.props.navigation.state.params.session,this.state.topOfLoadMoreMessages);
    }
  }


  onPressAvatar = (account) => {
    const userActions = this.props.userActions;
    const { currentSession } = this.state;
    if(this.state.userDetail.user.account != account){
      userActions.requestUserDetail(account, (message) => {
        this.props.navigation.navigate('OtherUserDetail', {
          user: message
        });
      });
    }else{

    }
  }

  loadStoreMessages = (session,sendTime) =>{
    if(this.state.isLoadingMsg){
      return;
    }
    this.setState({
      isLoadingMsg:true
    })
    let sql = "select DISTINCT m.message_id_ as messageId,m.session_code_ as sessionCode,m.type_ as type,m.from_ as 'from',m.content_ as content, m.send_time_ as sendTime" 
          +" from message m where m.owner_ = '"+global.loginUser.account+"' and m.session_code_ = '"+session.sessionCode+"'";
    if(sendTime){
      sql += " and m.send_time_ < "+sendTime;
    }
    const orderSql = " group by m.message_id_ order by m.send_time_ desc  LIMIT 20";
    let thisObj = this;
    dbService.executeSql(sql + orderSql,(messages) =>{
      if(messages.length > 0){
        this.setState(previousState => ({
          newMessages: GiftedChat.prepend(previousState.newMessages, messages),
          topOfLoadMoreMessages:messages[messages.length - 1].sendTime,
          isLoadingMsg:false
        }));
      }
    });
  }

  render() {
    return (
	    <SafeAreaView style={{flex: 1, backgroundColor: '#fff'}}>
	    <GiftedChat
        navigation={this.props.navigation}
        messages={this.state.newMessages}
        placeholder="输入文字..."
        onSend={messages => this.onSend(messages)}
        onResend={messages => this.onResend(messages)}
        onAppendMessages={messages => this.onAppendMessages(messages)}
        timeFormat="HH:mm:ss"
        dateFormat="YYYY-MM-DD"
        renderAvatarOnTop={true}
        isAnimated={false}
        listViewProps={{
          onScroll: this._onScroll.bind(this)
        }}
        onPressAvatar={this.onPressAvatar}
        sessionUsers= {this.state.currentSession.sessionUsers}
        showAvatarForEveryMessage={true}
        user={{
          _id: this.state.userDetail.user.account,
        }}
      />
	   </SafeAreaView>
    );
  }
}

const mapStateToProps = (state) => {
  const { message } = state;
  return {
    message
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  const userActions = bindActionCreators(userCreators, dispatch);
  return {
    messageActions,userActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(ChatSession);

const styles = StyleSheet.create({
  keyboardScrollView:{
    flex:1
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#fff'
  },
  content: {
    flex: 1,
    paddingBottom: 10
  },
  inputToolbarContainer: {
    borderTopWidth: StyleSheet.hairlineWidth,
    borderTopColor: '#b2b2b2',
    backgroundColor: '#FFFFFF',
    bottom: 0,
    height: 44,
    width: Dimensions.get('window').width
  },
  primary: {
    flexDirection: 'row',
    alignItems: 'flex-end',
  },
  actionContainer: {
    width: 26,
    height: 26,
    marginLeft: 10,
    marginBottom: 10,
  },
  wrapper: {
    borderRadius: 13,
    borderColor: '#b2b2b2',
    borderWidth: 2,
    flex: 1,
  },
  iconText: {
    color: '#b2b2b2',
    fontWeight: 'bold',
    fontSize: 16,
    backgroundColor: 'transparent',
    textAlign: 'center',
  },
  refreshControlBase: {
    backgroundColor: 'transparent'
  },
  headerRightIcon: {
    paddingRight: 20
  },
  headerLeftIcon: {
    paddingLeft: 20
  }
});
