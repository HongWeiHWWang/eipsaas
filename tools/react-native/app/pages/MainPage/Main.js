
import React from 'react';
import PropTypes from 'prop-types';
import {
  DeviceEventEmitter,
  InteractionManager,
  ListView,
  StyleSheet,
  View,
  NetInfo,
  Text,
  Linking,
  Platform,
  Alert,
  NativeModules
} from 'react-native';
import ScrollableTabView, {ScrollableTabBar} from 'react-native-scrollable-tab-view';
import { connect } from 'react-redux';
import { AudioUtils} from 'react-native-audio';
import DeviceInfo from 'react-native-device-info'
import * as store from '../../utils/StoreUtil';
import LoadingView from '../../components/LoadingView';
import ToastUtil from '../../utils/ToastUtil';
import RequestUtil from '../../utils/RequestUtil';
import AndroidWebView from '../../components/WebView';
import NotificationsUtil from '../../utils/NotificationsUtil';
import UserUtil from '../../utils/UserUtil';

import Footer from './Footer';
import EmptyView from './EmptyView';
import * as userCreators from '../../actions/user';
import * as imUtil from '../../utils/ImUtil';
import * as messageCreators from '../../actions/message';
import * as contactsCreators from '../../actions/contacts';
import * as workCreators from '../../actions/work';
import * as urlUtil from '../../utils/UrlUtil';

import { bindActionCreators } from 'redux';
import * as SessionService from '../Message/SessionService';
import {WORK_TYPE_FLOW,WORK_TYPE_INNERWEB,WORK_TYPE_OTHERWEB } from '../../constants/Constans';
import { WebView } from "react-native-webview";


const propTypes = {
  
};
const indexHtmlPath = '/mobile/index_app.html';
const pages = [];
let loadMoreTime = 0;
let currentLoadMoreTypeId;

class Main extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dataSource: new ListView.DataSource({
        rowHasChanged: (row1, row2) => row1 !== row2
      }),
      typeIds: [],
      typeList: {},
      currentSession:{},
      indexUrl : urlUtil.getUrl(global.server.web+indexHtmlPath)
    };
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeAllListeners('connectComplete');
    DeviceEventEmitter.removeAllListeners('connectionLost');
    DeviceEventEmitter.removeAllListeners('onFailure');
    DeviceEventEmitter.removeAllListeners('messageArrived');
  }

  componentDidMount() {

    if(!global.HtReactNativeMqtt){
      var HtReactNativeMqtt = NativeModules.HtReactNativeMqtt;
      global.HtReactNativeMqtt = HtReactNativeMqtt;
    }

    const { dispatch,userActions,messageActions,contactsActions } = this.props;
    //请求用户信息
    if (global.loginUser) {
        this.initUserSetting(global.loginUser);
        this.loadUserDetail();
        imUtil.initIm({onMessageArrived:this.onMessageArrived,user:global.loginUser,onSuccess:this.loadSessionData});
        // NetInfo.isConnected.fetch().done((isConnected) => {
        //   global.netInfoisConnected = isConnected;
        //   if(isConnected){
        //     this.loadUserDetail(loginUser);
        //   }else{
        //     ToastUtil.showShort('网络错误');
        //   }
        // });
        //监听网络变化，
        NetInfo.isConnected.addEventListener('connectionChange',(isConnected)=>{
          if(isConnected){
            userActions.loginUser({token:global.loginUser.token}, (message) =>{
              if(message.token){
                global.loginUser.token = message.token;
              }
            });
          }
        });        
      }
    global.audioPath = AudioUtils.DocumentDirectoryPath;
    global.imagePath = AudioUtils.DocumentDirectoryPath;
    global.videoPath = AudioUtils.DocumentDirectoryPath;
    global.filePath = AudioUtils.DocumentDirectoryPath;
  }

  loadSessionData = () =>{
    SessionService.requestSessionList(this.props);
  }

  loadUserDetail = () =>{
    const {userActions,messageActions,contactsActions,workActions } = this.props;
    const tempTime = new Date().getTime();//用来计算网络延迟,精确计算服务器时间。
    userActions.requestUserDetail(loginUser.account,(message) =>{
      if(message.success){
        this.checkAppVersion(message);
        store.save('userDetail',message);
        userActions.receiveUserDetail(message);
        //userActions.receiveLoginUser(global.loginUser);
        contactsActions.requestGeneralContact({});
        this.loadWorkItems();
      } 
    });
  }

   loadWorkItems = () => {
    const works = [];
    RequestUtil.requestByStoreUser(global.server.portal+"/sys/sysMenu/v1/getCurrentUserMenu?menuType=2",'get').then((data) =>{
      if(data && data.state){
        for(var i = 0 ; i < data.value.length ; i++){
          var work = data.value[i];
          var item = {
                name:work.name,
                works:[]
              }
          for(var j = 0 ; j < work.children.length ; j++){
            var child = work.children[j]
            item.works.push({
                icon: global.server.web+"/mobile/img/"+child.menuIcon+".png",
                title:child.name,
                url:urlUtil.getUrl(global.server.web+child.menuUrl)
              })
          }
          works.push(item);
        }
      }
      const {workActions } = this.props;
      workActions.receiveMyWorks(works);
    })
  }

  checkAppVersion = (message) =>{
    if(message.appVersion && DeviceInfo.getVersion() != message.appVersion){
      if(Platform.OS == 'android'){
        Alert.alert(
          '发现新版本v'+message.appVersion,
          '是否去下载更新？',
          [
            {text: '取消', style: 'cancel'},
            {text: '下载', onPress: () => {
              const url = global.server.web+'/mobile/apk/x5hotent.apk'
              Linking.canOpenURL(url).then((supported) => {
                if(supported){
                  Linking.openURL(url);
                }
              });
            }},
          ],
        )
      }else{
        Alert.alert(
          '发现新版本v'+message.appVersion,
          '请尽快下载更新',
          [
            {text: '确定', style: 'cancel'},
          ],
        )
      }
    }
  }

  //获取用户的相关设置
  initUserSetting = (loginUser) =>{
    store.get(loginUser.account+'_userSetting').then((userSetting) =>{
      if(userSetting == null){
        global.userSetting = {
          vibration:true,
          notification:true,
          isFirstLogin:true
        }
      }else{
        global.userSetting = userSetting;
      }
    })
  }

  onMessageArrived = (message) =>{
    const m = JSON.parse(message);
    if(m.messageId && m.content){
      SessionService.onMessageArrived(m,this.props);
    }
  }


  onPress = (article) => {
    const { navigate } = this.props.navigation;
    const webItem = article;
    navigate('Web', { webItem });
  };

  onShouldStartLoadWithRequest=(request)=>{
     if(request.url != this.state.indexUrl ){
        const { navigate } = this.props.navigation;
        const webItem = {
          title:'',
          url:request.url
        }
        navigate('Web', { webItem });
        return false;
     }
     return true;
  }

  onMessage = (message) =>{
    try{
      const json = JSON.parse(message);
      switch(json.type) {
        case "goBack":
          this.goBack();
          break;
        case "website":
          const { navigate } = this.props.navigation;
          const webItem = {
            title:json.title,
            url:json.url
          }
          navigate('Web', { webItem });
          break;
        default:
          break;
        }
    }catch(error){

    }
  }

  renderWebview = () =>{
    const token = global.loginUser.token;
    const injectScript = 
    ` 
    setTimeout(function(){
        window.postMessage = window.ReactNativeWebView.postMessage;
        window.token = "${token}";
        window.fromApp = "hotent";
        localStorage.setItem('fromApp', "hotent");
        localStorage.setItem('token', "${token}");
        if(afterLoadApp){
          afterLoadApp();
        }
    },100);
     `;
    return (
      <WebView
            ref={(ref) => {
              this.webview = ref;
            }}
            cacheEnabled={false}
            useWebKit={true}
            injectedJavaScript={injectScript}
            style={styles.base}
            source={{ uri: this.state.indexUrl,headers: {}}}
            javaScriptEnabled
            onShouldStartLoadWithRequest={this.onShouldStartLoadWithRequest}
            onMessage={(event) => {
              this.onMessage(event.nativeEvent.data)}}
          />
      )
  }

  render() {    
    return (
      <View style={styles.container}>
        <View style={{flex:1,flexDirection: 'column'}}>
            {this.renderWebview()}
        </View>
      </View>
    );
  }
}

const mapStateToProps = (state) => {
  const { message,user } = state;
  return {
    message,user
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  const userActions = bindActionCreators(userCreators, dispatch);
  const contactsActions = bindActionCreators(contactsCreators, dispatch);
  const workActions = bindActionCreators(workCreators, dispatch);
  return {
    messageActions,userActions,contactsActions,workActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(Main);

const styles = StyleSheet.create({
  base: {
    flex: 1
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#f3f3f3'
  },
  drawerContent: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#ddd'
  },
  drawerTitleContent: {
    height: 120,
    justifyContent: 'flex-end',
    padding: 20,
    backgroundColor: '#3e9ce9'
  },
  drawerIcon: {
    width: 30,
    height: 30,
    marginLeft: 5
  },
  drawerTitle: {
    fontSize: 20,
    textAlign: 'left',
    color: '#fcfcfc'
  },
  drawerText: {
    fontSize: 18,
    marginLeft: 15,
    textAlign: 'center',
    color: 'black'
  },
  timeAgo: {
    fontSize: 14,
    color: '#aaaaaa',
    marginTop: 5
  },
  refreshControlBase: {
    backgroundColor: 'transparent'
  },
  tab: {
    paddingBottom: 0
  },
  tabText: {
    fontSize: 16
  },
  tabBarUnderline: {
    backgroundColor: '#3e9ce9',
    height: 2
  }
});

Main.propTypes = propTypes;
