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

class ServerSetting extends React.Component {
    static navigationOptions = ({ navigation }) => ({
      header: null
    });

    constructor(props) {
      super(props);
      this.state = {
        server:{}
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
            global.server = s;
        })
    }

    thunks = (message) => {
    	if(message.success){
    		NavigationUtil.reset(this.props.navigation,'Splash',{isLogin:true});
    	}else{
    		NavigationUtil.reset(this.props.navigation,'Login');
    	}
    };

    buttonClick = () => {
      const { dispatch } = this.props;
      if (this.state.username) {
        if (this.state.password) {
          const userActions = bindActionCreators(userCreators, dispatch);
          userActions.loginUser({user:this.state.username,password:this.state.password}, this.thunks);
        } else {
          ToastUtil.showShort('请输入密码');
        }
      } else {
        ToastUtil.showShort('请输入用户名');
      }
    }

    onPressSetting = () =>{
      this.setState({
        modalVisible:true
      })
    }

    settingOk =()=>{
      store.save('global.server',this.state.server);
      global.server = this.state.server;
      this.props.navigation.goBack();
    }

    settingNo =()=>{
      this.props.navigation.goBack();
    }

    settingTextChange =(val,field)=>{
      let s = this.state.server;
      s[field] = val;
      this.setState({
        server:s
      })
    }

    render() {
      return (
    	<KeyboardAwareScrollView contentContainerStyle={styles.keyboardScrollView}>
        <View style={styles.content}>
            <View style={styles.logoView}>
               <Text style={{fontSize:26}}>服务配置</Text>
            </View>
            <View style={styles.inputView}>
                <View style={styles.inputContainer}>
                    <View style={styles.inputTextContainer}>
                      <Text style={styles.inputText}>uc:</Text>
                    </View>
                    <TextInput
                      defaultValue={this.state.server.uc}
                      placeholder="server.uc"
                      style={styles.inputValue}
                      onChangeText={(text) => {
                        this.settingTextChange(text,"uc");
                      }}
                    />
                </View>
                <View style={styles.inputContainer}>
                    <View style={styles.inputTextContainer}>
                      <Text style={styles.inputText}>portal:</Text>
                    </View>
                    <TextInput
                      defaultValue={this.state.server.portal}
                      placeholder="server.portal"
                      style={styles.inputValue}
                      onChangeText={(text) => {
                        this.settingTextChange(text,"portal");
                      }}
                    />
                </View>
                <View style={styles.inputContainer}>
                    <View style={styles.inputTextContainer}>
                      <Text style={styles.inputText}>form:</Text>
                    </View>
                    <TextInput
                      defaultValue={this.state.server.form}
                      placeholder="server.form"
                      style={styles.inputValue}
                      onChangeText={(text) => {
                        this.settingTextChange(text,"form");
                      }}
                    />
                </View>
                <View style={styles.inputContainer}>
                    <View style={styles.inputTextContainer}>
                      <Text style={styles.inputText}>bpmModel:</Text>
                    </View>
                    <TextInput
                      defaultValue={this.state.server.bpmModel}
                      placeholder="server.bpmModel"
                      style={styles.inputValue}
                      onChangeText={(text) => {
                        this.settingTextChange(text,"bpmModel");
                      }}
                    />
                </View>
                <View style={styles.inputContainer}>
                    <View style={styles.inputTextContainer}>
                      <Text style={styles.inputText}>bpmRunTime:</Text>
                    </View>
                    <TextInput
                      defaultValue={this.state.server.bpmRunTime}
                      placeholder="server.bpmRunTime"
                      style={styles.inputValue}
                      onChangeText={(text) => {
                        this.settingTextChange(text,"bpmRunTime");
                      }}
                    />
                </View>
                <View style={styles.inputContainer}>
                    <View style={styles.inputTextContainer}>
                      <Text style={styles.inputText}>web:</Text>
                    </View>
                    <TextInput
                      defaultValue={this.state.server.web}
                      placeholder="server.web"
                      style={styles.inputValue}
                      onChangeText={(text) => {
                        this.settingTextChange(text,"web");
                      }}
                    />
                </View>
            </View>
            <Button
              title="确认"
              titleStyle={styles.buttonText}
              buttonStyle={[styles.button]}
              onPress={this.settingOk}>
            </Button>
            <Button
              title="取消"
              titleStyle={styles.buttonText}
              buttonStyle={[styles.button]}
              onPress={this.settingNo}>
            </Button>
            </View>
            <View style={styles.bottomContainer}>
              <View style={styles.disclaimerContent}>
                <Text style={[styles.disclaimer, { color: '#999999' }]}>
                               
                </Text>
              </View>
          </View>
      </KeyboardAwareScrollView>
      );
    }
}

const mapDispatchToProps = (state, dispatch) => {
  return {
    
  };
};

export default connect(mapDispatchToProps)(ServerSetting);

var width = Dimensions.get("window").width; 
const styles = StyleSheet.create({
  keyboardScrollView:{
	   flex:1  
  },
  logoView:{
    alignItems: 'center',
    marginTop:30
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
	  marginTop:30
  },
  inputContainer:{
    flexDirection:"row"
  },
  inputTextContainer:{
    height:  40,
    width:100,
    padding: 8
  },
  inputText:{
    fontSize:16
  },
  inputValue:{
    height:  40,
    width: 200,
    padding: 4
  },
  button: {
    width: width-100,
    height: 40,
    borderWidth: 1,
    borderColor: '#fff',
    marginTop: 10,
    backgroundColor: '#fff',
    opacity:0.9
  },
  buttonText:{
    color:"#313131", 
    fontSize:20
  },
});
