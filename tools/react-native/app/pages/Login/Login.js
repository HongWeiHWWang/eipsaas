import React, { Component } from 'react';
import { Dimensions,StyleSheet, Image, Text, Linking, View, ImageBackground, TextInput,StatusBar,Modal,TouchableHighlight } from 'react-native';
import { connect } from 'react-redux';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import * as store from '../../utils/StoreUtil';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import { Input,Button } from 'react-native-elements';
import InputItem from 'antd-mobile-rn/lib/input-item';
import { bindActionCreators } from 'redux';
import NavigationUtil from '../../utils/NavigationUtil';
import * as userCreators from '../../actions/user';
import ToastUtil from '../../utils/ToastUtil';
import * as constans from '../../constants/Constans';


const SHOW_API = 'https://www.hotent.com';
const Logo = require('../../img/logo.png');
const userIcon = require('../../img/account.png');
const passWord = require('../../img/password.png');
const backG = require('../../img/Bitmap.jpg');
const aboutLogo = require('../../img/text.png');

class Personal extends React.Component {
    static navigationOptions = ({ navigation }) => ({
      header: null
    });

    constructor(props) {
      super(props);
      this.state = {
        username: 'admin',
        password: '123456',
        modalVisible:false,
        server:''
      };
    }

    componentDidMount() {
        store.get('global.server').then((s) =>{
            if(!s){
              s = constans.__CTX
            }
            this.setState({
                server:s
            })
        })
    }

    thunks = (message) => {
      if(message.token){
    		NavigationUtil.reset(this.props.navigation,'Home',{isLogin:true});
    	}else{
    		NavigationUtil.reset(this.props.navigation,'Login');
    	}
    };

    updateServerSetting = () =>{
      store.save('global.server',this.state.server);
      global.server = 
            {
              uc:this.state.server+"/uc",
              portal:this.state.server+"/portal",
              form:this.state.server+"/form",
              bpmModel:this.state.server+"/bpmModel",
              bpmRunTime:this.state.server+"/bpmRunTime",
              web:this.state.server+""
            }
    }

    buttonClick = () => {
      this.updateServerSetting();
      const { dispatch } = this.props;
      if (this.state.username) {
        if (this.state.password) {
          const userActions = bindActionCreators(userCreators, dispatch);
          userActions.loginUser({username:this.state.username,password:this.state.password}, this.thunks);
        } else {
          ToastUtil.showShort('请输入密码');
        }
      } else {
        ToastUtil.showShort('请输入用户名');
      }
    }

    onPressSetting =()=>{
      const { navigate } = this.props.navigation;
      navigate('ServerSetting');
    }

   
    render() {
      return (
    	<KeyboardAwareScrollView contentContainerStyle={styles.keyboardScrollView}>
        <ImageBackground source={backG} style={styles.container}>
        	<StatusBar
            StatusBarStyle="default"
            translucent={true}
           />
          <View style={styles.content}>
             
        	    <View style={styles.logoView}>
            	   <Image style={styles.logo} source={Logo} resizeMode="contain" />
            	   <Text style={{color:"#fff",fontSize:26}}>宏天协同办公平台移动端</Text>
              </View>
              <View style={styles.inputView}>
                <View style={styles.noBG}>
                  <Input
                    defaultValue={this.state.server}
                    placeholder="服务器"
                    containerStyle={[styles.login,styles.loginPR]}
                    placeholderTextColor="rgba(0,0,0,1)"
                    selectionColor="rgba(0,0,0,1)"
                    inputStyle={{ color: 'rgba(0,0,0,1)',paddingLeft: 0, paddingTop: 0, paddingBottom: 0,paddingRight: 15 }}
                    onChangeText={(text) => {
                      this.setState({
                        server:text
                      })
                    }}
                  />
                </View>
                <View style={styles.noBG}>
                  <Input
                    defaultValue={this.state.username}
                    placeholder="帐号"
                    containerStyle={[styles.login,styles.loginPR]}
                  	placeholderTextColor="rgba(0,0,0,1)"
                    selectionColor="rgba(0,0,0,1)"
                    inputStyle={{ color: 'rgba(0,0,0,1)',paddingLeft: 0, paddingTop: 0, paddingBottom: 0,paddingRight: 15 }}
                    onChangeText={(text) => {
                      this.setState({
                        username:text
                      })
                    }}
                  />
                </View>
                <View style={styles.noBG}>
                <Input
                    containerStyle={[styles.login,styles.loginPR]}
                    
                    defaultValue={this.state.password}
                    secureTextEntry
                    placeholder="密码"
                    placeholderTextColor="rgba(0,0,0,1)"
                    selectionColor="rgba(0,0,0,1)"
                    inputStyle={{ color: 'rgba(0,0,0,1)',paddingLeft: 0, paddingTop: 0, paddingBottom: 0,paddingRight: 15 }}
                    onChangeText={(text) => {
                      this.setState({
                        password:text
                      })
                    }}
                  />
                </View>
              </View>
              
              <Button
              	title="登录"
          		titleStyle={styles.loginButtonText}
                buttonStyle={[styles.loginButton]}
                onPress={this.buttonClick}>
              </Button>
            </View>
            <View style={styles.bottomContainer}>
              <View style={styles.disclaimerContent}>
                <Text style={[styles.disclaimer, { color: '#999999' }]}>
                               
                </Text>
              </View>
          </View>
        </ImageBackground>
        </KeyboardAwareScrollView>
      );
    }
}

const mapDispatchToProps = (state, dispatch) => {
  const userActions = bindActionCreators(userCreators, dispatch);
  return {
    userActions
  };
};

export default connect(mapDispatchToProps)(Personal);

var width = Dimensions.get("window").width; 
const styles = StyleSheet.create({
  keyboardScrollView:{
	flex:1  
  },
  container: {
    flex: 1,
    backgroundColor: '#fff'
  },
  content: {
	  justifyContent:'center',
    marginTop:30
  },
  inputView:{
	  alignItems: 'center',
	  marginTop:60
  },
  logoView:{
	  alignItems: 'center',
    marginTop:30
  },
  logo: {
    width: 160,
    height: 80,
    marginTop: 10
  },
  aboutLogo: {
    marginTop: -10
  },
  version: {
    fontSize: 16,
    textAlign: 'center',
    color: '#aaaaaa',
    marginTop: 5
  },
  title: {
    fontSize: 30,
    textAlign: 'center',
    color: '#313131',
    marginTop: 10
  },

  usersT: {
    height: 40,
    borderBottomWidth: 0
  },
  Icon: {
    width: 20,
    height: 20,
    position: 'absolute',
    left: -15
  },
  noBG: {
	paddingTop:10,	
    width: width-100,
    backgroundColor: 'rgba(0,0,0,0)'
  },
  loginPR:{
	  backgroundColor: '#f8f8f8',
	  opacity:0.5
  },
  loginButton: {
  	width: width-100,
  	height: 40,
    borderWidth: 1,
    borderColor: '#fff',
    marginTop: 10,
    backgroundColor: '#fff',
    opacity:0.9
  },
  loginButtonText:{
  	color:"#313131", 
  	fontSize:20
  },
  bgb: {
    backgroundColor: 'black'
  },
  halfCir: {
    borderWidth: 1
  }
  // middleView:{
  //   flex:1,
  //   position:'absolute',
  //   alignItems: 'center'
  // }
});
