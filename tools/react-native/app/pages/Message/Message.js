
import React from 'react';
import { StyleSheet, Image, Text, View, DeviceEventEmitter, ScrollView ,TouchableOpacity} from 'react-native';
import { bindActionCreators } from 'redux';
import * as store from '../../utils/StoreUtil';
import moment from 'moment';
import { connect } from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';
import { List, ListItem} from 'react-native-elements';
import ToastUtil from '../../utils/ToastUtil';
import * as messageCreators from '../../actions/message';
import Badge from 'antd-mobile-rn/lib/badge';
import {
  MESSAGE_TYPE_TEXT,
  MESSAGE_TYPE_AUDIO,
  MESSAGE_TYPE_IMAGE,
  MESSAGE_TYPE_FILE,
  MESSAGE_TYPE_VIDEO,
  MESSAGE_TYPE_SYSTEM,
  SESSION_P2P,
  SESSION_TEAM } from '../../constants/Constans';
import ActionSheet from '@expo/react-native-action-sheet';
import * as SessionService from '../Message/SessionService';
const systemMessageIcon = require('../../img/systemMesIcon.png');
const teamMessageIcon = require('../../img/teamMesIcon.png');
import CirclePortrait from '../../components/CirclePortrait';
import MessageTabBar from '../../components/MessageTabBar';

import momentLocale from 'moment/locale/zh-cn';
moment.updateLocale('zh-cn', momentLocale);

class Message extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    title: '沟通',
    tabBarIcon: ({ tintColor }) => (
      // <View>
      //     <Icon name="md-chatbubbles" size={23} color={tintColor} />
      //     {navigation.state.params && navigation.state.params.unRead? <View style={styles.IconBadge}>
      //       <Text style={styles.IconText}>{navigation.state.params.unRead}</Text>
      //     </View> : null}
          
      // </View>
      <MessageTabBar tintColor={tintColor}/>
    ),
    headerRight: (
      <Icon onPress={() => {navigation.navigate('UnionSearch', {})}} style={styles.headerRightIcon} name="ios-search" size={28} color="#fff" />
    )
  });

  constructor(props) {
    super(props);
    this.state = {
      sessionJson: {},
      unRead:10
    };
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeAllListeners('createSession');
  }

  componentWillReceiveProps(nextProps){
    //const sessionJson =  nextProps.message.sessionJson;
    // nextProps.navigation.state.params.unRead = Math.round(Math.random() * 10)
    // console.warn(nextProps.navigation.state.params.unRead)

    // nextProps.sessionArray.map((val, i) => {

    // });
    //nextProps.navigation.state.params.unRead = nextProps.message.allUnRead
    // console.warn(nextProps.message.allUnRead)
    // this.setState({
    //   unRead:nextProps.message.allUnRead
    // })
  }

  componentWillUpdate(nextProps,nextState){
    nextProps.navigation.state.params.unRead = nextProps.message.allUnRead
  }

  componentDidMount() {
    const { messageActions } = this.props;
    this.props.navigation.setParams({
      headerSearchPress: this.headerSearchPress,
    });
  }

  headerSearchPress = () =>{
    this.props.navigation.navigate('UnionSearch', {});
  }

  openSession = (session) => {
    const { navigate } = this.props.navigation;
    navigate('ChatSession', {
      session
    });
  }

  onLongPress = (session) =>{
    const options = [
      '移除:'+session.sessionTitle,
      '取消'
    ];
    const cancelButtonIndex = options.length - 1;
    this._actionSheetRef.showActionSheetWithOptions({
      options,
      cancelButtonIndex,
    },
    (buttonIndex) => {
      switch (buttonIndex) {
        case 0:
          this.removeSession(session);
          break;
      }
    });
  }

  removeMessageHistory = (session) =>{
    SessionService.removeMessageHistory(session.sessionCode);
  }

  removeSession = (session) =>{
    SessionService.removeMessageHistory(session.sessionCode);
    SessionService.removeSession(session.sessionCode,this.props)
    this.props.messageActions.updateSession({type:'removeSession',session:session,sessionJson:this.props.message.sessionJson});
  }

  renderListContent = () => {
    
    const sessionArray = Object.values(this.props.message.sessionJson);

    const sortArray = sessionArray.sort(function(a, b) {
      return  b.lastReadTime - a.lastReadTime;
    });
    
    const list = this.props.message.sessionArray.map((val, i) => {
      const listView = (
        <TouchableOpacity key={i} activeOpacity={0.5} onLongPress={() => this.onLongPress(val)} onPress={() => { this.openSession(val); }} style={styles.sessionContainer}>
          {this.renderSessionIcon(val)}
          <View style={{flex:1,justifyContent:'center',paddingLeft:10,}}>
            <View style={{flex:1,flexDirection: 'row',}}>
              <Text style={styles.sessionTitle} numberOfLines={1} ellipsizeMode='tail'>{val.sessionTitle}</Text>
              <Text style={styles.lastMessageTime}>{val.sessionLastText ? moment(JSON.parse(val.sessionLastText).sendTime).fromNow():''}</Text>
            </View>
            <View style={{flex:1,flexDirection: 'row',}}>
              <Text style={styles.lastText} numberOfLines={1} ellipsizeMode='tail'>{this.renderLastText(val.sessionLastText)}</Text>
              <View style={{flex:0,justifyContent:'center',width:96,height:20,alignItems:'flex-end',}}>
                {val.sessionUnRead?<Text style={styles.unRead}>{val.sessionUnRead}</Text>:<Text></Text>}
              </View>
            </View>
          </View>
        </TouchableOpacity>
      );
      return listView;
    });
    return list;
  }

  renderSessionIcon = (session) => {
    return (<CirclePortrait title={session.sessionTitle} uri={global.server.portal+session.sessionIcon} style={{width:40,height:40,}} textStyle={{lineHeight:40,fontSize:14}}/>)
  }

  renderLastText = (lastTetxt) => {
    if (!lastTetxt) {
      return '';
    }
    let text = '';
    const json = JSON.parse(lastTetxt);
    switch (json.type) {
      case MESSAGE_TYPE_TEXT:
        text = json.content.text?json.content.text:JSON.parse(json.content).text;
        break;
      case MESSAGE_TYPE_IMAGE:
        text = '[图片]';
        break;
      case MESSAGE_TYPE_FILE:
        text = '[文件]';
        break;
      case MESSAGE_TYPE_AUDIO:
        text = '[语音]';
        break;
      case MESSAGE_TYPE_VIDEO:
        text = '[视频]';
        break;
      case MESSAGE_TYPE_SYSTEM:
        text = json.content.title?json.content.text:JSON.parse(json.content).title;;
        break;
      default: text = '';
    }
    return text;
  }

  render() {
    const listContent = this.renderListContent();
    return (
      <ActionSheet ref={component => this._actionSheetRef = component}>
        <View style={styles.container}>
          <View style={styles.content}>
            {/* <List> */}
            <ScrollView>
              {listContent}
            </ScrollView>
            {/* </List> */}
          </View>
        </View>
      </ActionSheet>
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
  return {
    messageActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(Message);


const styles = StyleSheet.create({
  
  IconBadge: {
    position:'absolute',
    left:10,
    width:10,
    height:10,
    borderRadius:15,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FF0000'
  },
  IconText: {
    color:'#FFFFFF',
    fontSize:7
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#f3f3f3'
  },
  content: {
    flex: 1,
    paddingBottom: 12,
  },
  listItem:{
    backgroundColor: '#FFFFFF',
    height:70
  },
  sessionIcon: {
    paddingRight: 10
  },
  places: {
    marginRight: 10,
    justifyContent: 'center'
  },
  rowCenter: {
    flexDirection: 'row',
    justifyContent: 'center'
  },
  headerRightIcon: {
    paddingRight: 20
  },
  sessionTitle:{
    flex:1,justifyContent:'center',fontSize:16,color:'#262626',lineHeight:24,height:24,
  },
  lastMessageTime:{flex:0,justifyContent:'center',width:96,fontSize:12,color:'#b0b0b0',lineHeight:24,textAlign:'right',},
  sessionContainer:{flex:1,flexDirection: 'row',backgroundColor:'#fff',borderBottomColor:'#ececec',borderBottomWidth:1,borderStyle:'solid',paddingLeft:15,paddingRight:15,paddingTop:10,paddingBottom:10,},
  iconText:{flex:0,justifyContent:'center',width:40,height:40,lineHeight:40,textAlign: 'center',backgroundColor:'#4fabf4',borderRadius:20,color:'#fff',},
  iconImage:{flex:0,justifyContent:'center',width:40,height:40,borderRadius:20,overflow:'hidden',},
  lastText:{flex:1,lineHeight:20,height:20,flexWrap:'nowrap',justifyContent:'center',fontSize:12,color:'#b0b0b0',},
  unRead:{width:18,height:18,backgroundColor:'#f93f3f',fontSize:10,color:'#fff',lineHeight:18,borderRadius:9,textAlign:'center',overflow:'hidden',},
});
