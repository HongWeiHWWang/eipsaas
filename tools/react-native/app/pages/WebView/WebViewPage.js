
import React from 'react';
import {
  StyleSheet,
  BackHandler,
  Dimensions,
  Text,
  Image,
  TouchableOpacity,
  TouchableWithoutFeedback,
  requireNativeComponent,
  View,
  Modal,
  Platform,
  I18nManager,
  NetInfo,
  SafeAreaView,
  ProgressViewIOS,
  ProgressBarAndroid
} from 'react-native';
import { connect } from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';
import ToastUtil from '../../utils/ToastUtil';
import LoadingView from '../../components/LoadingView';
import { formatStringWithHtml } from '../../utils/FormatUtil';
import {SESSION_P2P,SESSION_TEAM} from '../../constants/Constans';
import * as SessionService from '../Message/SessionService';
import { bindActionCreators } from 'redux';
import * as messageCreators from '../../actions/message';
import * as userCreators from '../../actions/user';

import AndroidWebView from '../../components/WebView';

import UserUtil from '../../utils/UserUtil';
import { WebView } from "react-native-webview";

const backAndroid = require('../../img/icon/back_icon_android.png');
const backIOS = require('../../img/icon/back_icon_ios.png');
let canGoBack = false;



class WebViewPage extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    tabBarIcon: ({ tintColor }) => (
      <Icon name="md-home" size={25} color={tintColor} />
    ),
    headerTitle:(
        <Text style={styles.title}>{navigation.state.params.headerTitle}</Text>
    ),
    headerLeft:(
        <TouchableOpacity activeOpacity={1} onPress={navigation.state.params ? navigation.state.params.headerBackPress : null}>
        { 
          Platform.OS === 'ios'?    
          <View>
            <Image style={styles.headerLeftIcon} source={backIOS}></Image>
          </View>
          :
          <Image style={styles.headerLeftIcon} source={backAndroid}></Image>
        }
        </TouchableOpacity>
    ),
    headerRight:(
        <View style={Platform.OS === 'ios' ? '' :styles.headerRightIcon}></View>
    )
    // header:(
    //   <View style={{backgroundColor: '#2c90e0',height:65,paddingTop:20,justifyContent: 'center',alignItems: 'center',flexDirection: 'row'}}>
    //     { 
    //       Platform.OS === 'ios'?
    //       <TouchableOpacity activeOpacity={1} onPress={navigation.state.params ? navigation.state.params.headerBackPress : null}>
    //         <Image onPress={navigation.state.params ? navigation.state.params.headerBackPress : null} style={styles.headerLeftIcon} source={backIOS}></Image>
    //       </TouchableOpacity>
    //       :
    //       <TouchableOpacity activeOpacity={1} onPress={navigation.state.params ? navigation.state.params.headerBackPress : null}>
    //         <Image style={styles.headerLeftIcon} source={backAndroid}></Image>
    //       </TouchableOpacity>
    //     }
    //     <Text style={styles.title}>{navigation.state.params.headerTitle}</Text>
    //     <View style={styles.headerRightIcon}></View>
    //   </View>
    // )
    // /*title:navigation.state.params.webItem.title,
    // headerTitleStyle: {
    //   alignSelf:'center',
    //   fontSize:18,
    // },
    // headerRight: (
    //   <View  style={styles.headerRightIcon} />
    // )*/
  });

  constructor(props) {
    super(props);
    this.state = {
      isShareModal: false,
      url:'',
      progress:0
    };
  }

  componentDidMount() {
    const { params } = this.props.navigation.state;
    this.setState({
      url:params.webItem.url
    })
    BackHandler.addEventListener('hardwareBackPress', this.goBack);
    this.props.navigation.setParams({
      headerBackPress: this.goBack,
      headerTitle:params.webItem.title
    });
  }

  componentWillUnmount() {
    BackHandler.removeEventListener('hardwareBackPress', this.goBack);
  }

  onActionSelected = () => {
    this.setState({
      isShareModal: true
    });
  };

  onNavigationStateChange = (navState) => {
    if(navState.url.indexOf("login") > -1){
      //UserUtil.logout(this.props);
    }
    //用indexOf防止有参数的情况
    if(navState.url.indexOf(this.state.url) > -1){
      canGoBack = false;
    }else{
      canGoBack = navState.canGoBack;
    }
    this.props.navigation.setParams({
      headerBackPress: this.goBack,
      headerTitle:navState.title
    });
  };

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
        case "chat":
          const account = json.account;
          console.warn(account);
          if(account == global.loginUser.account){
            ToastUtil.showShort('申请人是自己');
          }else{
            const params ={
              scene:SESSION_P2P,
              userStr:account
            }
            SessionService.createSession(params,this.props);
          }
        break;
        default:
          break;
        }
    }catch(error){

    }
  }

  goBack = () => {
    if (canGoBack) {
      this.webview.goBack();
      return true;
    }else{
      this.props.navigation.goBack();
      return true;
    }
    return false;
  };

  renderWebview = () =>{
    const { params } = this.props.navigation.state;
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
    return(
	  <SafeAreaView style={{flex: 1, backgroundColor: '#fff'}}>
      {this.state.progress !== 1 && Platform.OS === 'android' && <ProgressBarAndroid color={"gray"} styleAttr="Horizontal" progress={this.state.progress}/>}
      {this.state.progress !== 1 && Platform.OS === 'ios' && <ProgressViewIOS progressViewStyle={"bar"} progressTintColor={"gray"} progress={this.state.progress}/>}
      <WebView
        ref={(ref) => {
          this.webview = ref;
        }}
      	onLoadProgress={({nativeEvent}) => this.setState(
              {progress: nativeEvent.progress}
        )}
        cacheEnabled={false}
        useWebKit={true}
        injectedJavaScript={injectScript}
        style={styles.base}
        source={{ uri: params.webItem.url,headers: {}}}
        javaScriptEnabled
        startInLoadingState
        scalesPageToFit
        onNavigationStateChange={this.onNavigationStateChange}
        onMessage={(event) => {this.onMessage(event.nativeEvent.data)}}
      />
      </SafeAreaView>
    )
  }

  render() {
    return (
      <View style={styles.container}>
        {this.renderWebview()}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  base: {
    flex: 1
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#FFF'
  },
  title:{
    fontSize: 18,
    fontWeight: Platform.OS === 'ios' ? '700' : '500',
    color: '#fff',
    textAlign: 'center',
    marginHorizontal: 16,
    flex:1
  },
  backTitle:{
    fontSize: 18,
    fontWeight: '700',
    color: '#fff',
    marginHorizontal: 16,
  },
  headerLeftIcon:
    Platform.OS === 'ios'
      ? { 
          tintColor:"#fff",
          height: 19,
          width: 11,
          marginRight: 0,
          marginVertical: 12,
          marginLeft:10,
          transform: [{ scaleX: I18nManager.isRTL ? -1 : 1 }],
        }
      : {
          tintColor:"#fff",
          height: 24,
          width: 24,
          marginRight:0,
          marginLeft:10,
          transform: [{ scaleX: I18nManager.isRTL ? -1 : 1 }],
        },
  
  headerRightIcon:Platform.OS === 'ios'
      ? { 
          width: 23,
        }
      : {
          width: 34,
        },
});

const mapStateToProps = (state) => {
  const { message,user } = state;
  return {
    message,user
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  const userActions = bindActionCreators(userCreators, dispatch);
  
  return {
    messageActions,userActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(WebViewPage);
