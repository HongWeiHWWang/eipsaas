
import React from 'react';
import { Dimensions, Animated ,AsyncStorage,StatusBar,View} from 'react-native';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as store from '../utils/StoreUtil';
import SplashScreen from 'react-native-splash-screen';
import * as userCreators from '../actions/user';
import ToastUtil from '../utils/ToastUtil';
import NavigationUtil from '../utils/NavigationUtil';
import RequestUtil from '../utils/RequestUtil';
import * as constans from '../constants/Constans';

const maxHeight = Dimensions.get('window').height;
const maxWidth = Dimensions.get('window').width;
const splashImg = require('../img/splash.png');

class Splash extends React.Component {
  static navigationOptions = {
    header: null
  };

  constructor(props) {
    super(props);
    this.state = {
      bounceValue: new Animated.Value(1)
    };
  }

  updateServerSetting = () =>{
    store.get('global.server').then((s) =>{
      global.server = 
          {
            uc:s+"/uc",
            portal:s+"/portal",
            form:s+"/form",
            bpmModel:s+"/bpmModel",
            bpmRunTime:s+"/bpmRunTime",
            web:s
          }
    })
  }


  componentDidMount() {
    const { navigate } = this.props.navigation;
    const { dispatch, user,userActions } = this.props;
//    Animated.timing(this.state.bounceValue, {
//      toValue: 1.2,
//      duration: 1000
//    }).start();
    SplashScreen.hide();
    if(!global.server){
        this.updateServerSetting();
    }    
    
    store.get('loginUser').then((lu) => {
        if (lu && lu.token) {
          global.loginUser = lu;
          if(this.props.navigation.state.params && this.props.navigation.state.params.isLogin){
            NavigationUtil.reset(this.props.navigation,'Home');
          }else{
            const userActions = bindActionCreators(userCreators, dispatch);
            userActions.loginUser({token:lu.token,user:lu.account}, (message) =>{
            	if(message.token){
            		NavigationUtil.reset(this.props.navigation,'Home');
            	}else{
            		NavigationUtil.reset(this.props.navigation,'Login');
            	}
            });
          }
        } else {
          NavigationUtil.reset(this.props.navigation,'Login');
        }
     });
//    this.timer = setTimeout(() => {
//      
//    }, 500);
  }

  componentWillUnmount() {
    clearTimeout(this.timer);
  }

  render() {
    return (
        <View style={{flex:1}}>
          <StatusBar
            StatusBarStyle="default"
            translucent={true}
           />
        <Animated.Image
          style={{
            width: maxWidth,
            height: maxHeight,
            transform: [{ scale: this.state.bounceValue }]
          }}
          source={splashImg}
        />
      </View>
    );
  }
}

const mapDispatchToProps = (dispatch) => {
  return {}
};

export default connect(mapDispatchToProps)(Splash);
