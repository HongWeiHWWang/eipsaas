
import React from 'react';
import { StyleSheet, Image, Text, Linking, View,Platform,Alert } from 'react-native';
import { connect } from 'react-redux';
import * as store from '../../utils/StoreUtil';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import Button from 'antd-mobile-rn/lib/button';
import List from 'antd-mobile-rn/lib/list';
import { bindActionCreators } from 'redux';
import * as userCreators from '../../actions/user';
import * as imUtil from '../../utils/ImUtil';
import CirclePortrait from '../../components/CirclePortrait';
const departmentIcon = require('../../img/icon/icon_20.png');
const boyIcon = require('../../img/icon/icon_21.png');
const girlIcon = require('../../img/icon/icon_22.png');
import NavigationUtil from '../../utils/NavigationUtil';

import UserUtil from '../../utils/UserUtil';



const logoutIcon = require('../../img/icon/icon_23.png');
class Personal extends React.Component {

  static navigationOptions = ({ navigation }) => ({
    title: '我的',
    tabBarIcon: ({ tintColor }) => (
      <Icon name="md-person" size={23} color={tintColor} />
    ),
    headerRight: (
      <Icon onPress={() => {navigation.navigate('UnionSearch', {})}} style={styles.headerRightIcon} name="ios-search" size={28} color="#fff" />
    ),
    tabBarOnPress:(navigation,defaultHandler) => {
      if(navigation.navigation.state.params.refreseCacheSize){
        navigation.navigation.state.params.refreseCacheSize()
      }
      navigation.defaultHandler();
    }
  });
  constructor(props) {
    super(props);
    this.state = {
      userDetail:{
        user:{},
        orgUserRels:[],
        cacheStr:""
      }
    };
  }

  componentDidMount() {

    if(Platform.OS == 'android'){
      this.refreseCacheSize();    
    }
    
    const { dispatch, user } = this.props;
    // store.get('userDetail').then((userDetail) => {
    //   if (userDetail) {
    //     this.setState({
    //       userDetail:userDetail
    //     })
    //   }
    // });

    this.props.navigation.setParams({
      headerSearchPress: this.headerSearchPress,
      refreseCacheSize:this.refreseCacheSize
    });
  }

  refreseCacheSize = () =>{
    const RCTHttpCache = require('react-native').NativeModules.HttpCache;
    const thisObject = this;
    RCTHttpCache.getCacheSize().then(function(fileS){
      let cacheStr = "0MB";
      if (fileS < 1024) {
         cacheStr = (fileS).toFixed(2) + "B";
      } else if (fileS < 1048576) {
          cacheStr = (fileS / 1024).toFixed(2) + "KB";
      } else if (fileS < 1073741824) {
          cacheStr = (fileS / 1048576).toFixed(2) + "MB";
      } else {
          cacheStr = (fileS / 1073741824).toFixed(2) + "G";
      }
      thisObject.setState({
        cacheStr:cacheStr
      });
    })
  }

  clearCache = () =>{
    thisObject = this;
    Alert.alert(
      '将会清除app缓存',
      '是否确认？',
      [
        {text: '取消', style: 'cancel'},
        {text: '清除', onPress: () => {
          const RCTHttpCache = require('react-native').NativeModules.HttpCache;
          RCTHttpCache.clearCache().then(function(obj){
             thisObject.refreseCacheSize();
          })
        }},
      ],
    )
  }

  headerSearchPress = () =>{
    this.props.navigation.navigate('UnionSearch', {});
  }

  openUserSetting = () =>{
    this.props.navigation.navigate('UserSetting', {});
  }

  buttonClick = (e) => {
    this.props.userActions.requestUserDetail(this.props.user.userDetail.user.account,(message) =>{
      this.props.navigation.navigate('UserDetail', {
        user: message
      });
    });
  }

  logOut = () => {
    UserUtil.logout(this.props);
  }

  renderVersion = () =>{
    const oldVersion = DeviceInfo.getVersion();
    if(this.props.user.userDetail.appVersion && oldVersion != this.props.user.userDetail.appVersion){
      return <View style={{flexDirection: 'row',}}><Text style={{color:'#9e9e9e',fontSize:14,}}>{'v'+oldVersion}<Text style={{color:'#f26666'}}> ●</Text></Text></View>;
    }
    return 'v'+oldVersion;
  }

  downloadNewVersion = () =>{
    if(this.props.user.userDetail.appVersion && DeviceInfo.getVersion() != this.props.user.userDetail.appVersion){
      if(Platform.OS == 'android'){
        Alert.alert(
          '发现新版本v'+this.props.user.userDetail.appVersion,
          '是否去下载更新？',
          [
            {text: '取消', style: 'cancel'},
            {text: '下载', onPress: () => {
              const url = global.server.web+'/mobile/apk/hotent.apk'
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
          '发现新版本v'+this.props.user.userDetail.appVersion,
          '请尽快下载更新',
          [
            {text: '确定', style: 'cancel'},
          ],
        )
      }
    }
  }

  cacheStrContent = () =>{
    return <Text>{this.state.cacheStr}</Text>;
  }

  renderSexIcon = () =>{
    const userDetail = this.props.user.userDetail;
    if(!userDetail.user.sex){
      return <View/>;
    }else{
      if(userDetail.user.sex == '女'){
        return <Image source={girlIcon} style={{width: 16, height:16,}}></Image>
      }else{
        return <Image source={boyIcon} style={{width: 16, height:16,}}></Image>
      }
    }
  }
  render() {
    const userDetail = this.props.user.userDetail;
    const orgNameArray = [];
    userDetail.orgUserRels.map(function(orgUserRel,index){
      orgNameArray.push(orgUserRel.orgName);
    })
    const orgNameStr = orgNameArray.join(",");
    const versionCotent = this.renderVersion();
    const sexIcon = this.renderSexIcon();
    const cacheStrContent = this.cacheStrContent();
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <List style={styles.personalMainpad}>
            <View style={styles.personalMain}>
              <CirclePortrait isShowImage={true} title={userDetail.user.fullname} uri={global.server.portal+userDetail.user.photo} style={{width:70,height:70}}/>
              <View style={styles.personalText}>
                <View style={{flexDirection: 'row',}}>
                  <Text style={styles.personalName} numberOfLines={1} ellipsizeMode='tail'>{userDetail.user.fullname}</Text>
                  <View style={{flex:0,justifyContent:'center',height:40,paddingLeft:10,}}>{sexIcon}</View>
                </View>
                <View style={{flexDirection: 'row',paddingTop:5,}}>
                  <Image source={departmentIcon} style={{flex:0,width: 14, height:14,}}></Image>
                  <Text style={styles.personalDepartment} numberOfLines={1} ellipsizeMode='tail'>{orgNameStr}</Text>
                </View>
              </View>
            </View>
          </List>
          <List>
            <List.Item onClick={() => { this.buttonClick('个人资料'); }} arrow="horizontal" ><Text style={styles.listText}>个人资料</Text></List.Item>
            <List.Item onClick={() => {this.downloadNewVersion()}} extra={versionCotent}><Text style={styles.listText}>版本号</Text></List.Item>
            {
              Platform.OS == 'android' ?
              <List.Item onClick={() => {this.clearCache()}} extra={cacheStrContent}><Text style={styles.listText}>缓存</Text></List.Item>
              :
              null
            }
          </List>
          <List style={styles.logout_box}>
            <List.Item onClick={() => {this.logOut()}} style={styles.logout_button}>
              <View style={{flex:1,justifyContent:'center',flexDirection: 'row',}}>
                <Text style={styles.logout_buttonText}>退出登录</Text>
                <View style={styles.logout_buttonImage}><Image source={logoutIcon} style={{width: 16, height:16,}}></Image></View>
              </View>
            </List.Item>
          </List>
        </View>
      </View>
    );
  }
}

const mapStateToProps = (state) => {
  const { user } = state;
  return {
    user
  };
};

const mapDispatchToProps = (dispatch) => {
  const userActions = bindActionCreators(userCreators, dispatch);
  return {
    userActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(Personal);


const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#f3f3f3'
  },
  content: {
    flex: 1,
    paddingBottom: 10,
    backgroundColor: '#f3f3f3'
  },
  personalMainpad:{
    paddingBottom:10,
  },
  disclaimerContent: {
    flexDirection: 'column'
  },
  disclaimer: {
    fontSize: 14,
    textAlign: 'center'
  },
  bottomContainer: {
    alignItems: 'center'
  },

  listText: {
    fontSize:16,
    color:'#262626',
  },
  logout_box: {
    paddingTop: 20,
    height:45,
  },
  logout_button: {
    justifyContent:'center',
    height:45,
  },
  logout_buttonText: {
    flex:1,
    fontSize:16,
    color:'#f26666',
    lineHeight:30,
  },
  logout_buttonImage: {
    flex:0,
    width:16,
    justifyContent:'center',
    alignItems:'flex-end',
  },
  headerRightIcon: {
    paddingRight: 20
  },
  personalMain:{
    padding:15,
    backgroundColor:'#fff',
    flexDirection: 'row',
    justifyContent:'center',
  },
  personalText:{
    flex:1,
    paddingLeft:10,
  },
  personalName:{
    flex:0,
    maxWidth:200,
    justifyContent:'center',
    fontSize:16,
    color:'#000000',
    height:40,
    lineHeight:40,
  },
  personalDepartment:{
    fontSize:12,
    color:'#8a8a8a',
    paddingLeft:5,
    lineHeight:15,
    flex:1,
    justifyContent:'center',
  },
  logout_box: {
    paddingTop: 20,
  },
  logout_button: {
    justifyContent:'center',
    height:45,
  },
  headerRightIcon: {
    paddingRight: 20
  },
});
