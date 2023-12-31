
import React from 'react';
import { StyleSheet, Image, Text, View ,ScrollView,Linking,TouchableOpacity} from 'react-native';
import { connect } from 'react-redux';
import * as store from '../../utils/StoreUtil';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import ToastUtil from '../../utils/ToastUtil';
import Button from 'antd-mobile-rn/lib/button';
import List from 'antd-mobile-rn/lib/list';
import { bindActionCreators } from 'redux';
import * as messageCreators from '../../actions/message';
import * as contactsCreators from '../../actions/contacts';
import {SESSION_P2P,SESSION_TEAM} from '../../constants/Constans';
import * as SessionService from '../Message/SessionService';
import RequestUtil from '../../utils/RequestUtil';
import GridView from '../../components/GridView';
const boyIcon = require('../../img/icon/icon_21.png');
const girlIcon = require('../../img/icon/icon_22.png');
const idIcon = require('../../img/icon/icon_28.png');
import CirclePortrait from '../../components/CirclePortrait';

class OtherUserDetail extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    // title: navigation.state.params.user.user.fullname,
    // tabBarIcon: ({ tintColor }) => (
    //   <Icon name="md-person" size={25} color={tintColor} />
    // )
      headerTitle:navigation.state.params.user.user.fullname,
      headerRight: (
        <View style={{paddingRight:20,}}></View>
      ),
      headerTitleStyle: {
        alignSelf:'center',
        fontSize:18,
      },
  });
  constructor(props) {
    super(props);
    this.state = {
      userDetail:{
        user:{},
        orgUserRels:[]
      }
    };
  }

  componentDidMount() {
    const { dispatch} = this.props;
    const userDetail = this.props.navigation.getParam('user');
    this.setState({
      userDetail:userDetail
    })
    this.addContact(userDetail)
  }

  addContact = (userDetail) =>{
    const {contacts} = this.props;
    const generalContacts =  contacts.generalContacts;
    let flag = true;
    generalContacts.map((contact) =>{
      if(contact.account == userDetail.user.account){
        flag = false;
        return false;
      }
    })
    if(flag){
      const newContact = {
        account:userDetail.user.account,
        fullname:userDetail.user.fullname,
        owner:global.loginUser.account
      }
      const newGeneralContacts = [newContact].concat(generalContacts);
      this.props.contactsActions.receiveGeneralContact(newGeneralContacts);
      const url = global.server.portal+"/im/imGeneralContact/v1/addContact?account="+userDetail.user.account;
      RequestUtil.requestByStoreUser(url,'post',"{}")
    }
  }

  openSession = () =>{
    if(global.imClient){
      const params ={
        scene:SESSION_P2P,
        userStr:this.state.userDetail.user.account
      }
      SessionService.createSession(params,this.props);
    }else{
      ToastUtil.showShort('连接消息服务器失败');
    }
  }

  linkingPhone = () =>{
    const tel = this.state.userDetail.user.mobile;
    if(!tel){
      ToastUtil.showShort('该人没有常用电话');
      return;
    }
    const url = 'tel:'+ tel;;
    Linking.canOpenURL(url).then((supported) => {
      if(supported){
        Linking.openURL(url);
      }
    });
  }

  linkingSms = () =>{
    const tel = this.state.userDetail.user.mobile;
    if(!tel){
      ToastUtil.showShort('该人没有常用电话');
      return;
    }
    const url = 'sms:'+ tel;
    Linking.canOpenURL(url).then((supported) => {
      if(supported){
        Linking.openURL(url);
      }
    });
  }

  linkingEmail = () =>{
    const email = this.state.userDetail.user.email;
    if(!email){
      ToastUtil.showShort('该人没有常用邮箱');
      return;
    }
    const url = 'mailto:' + email;
    Linking.canOpenURL(url).then((supported) => {
      if(supported){
        Linking.openURL(url);
      }
    });
  }
  
  renderUserSex = () =>{
    const userDetail = this.state.userDetail;
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
    const userDetail = this.state.userDetail;
    const orgNameArray = [];
    userDetail.orgUserRels.map(function(orgUserRel,index){
      orgNameArray.push(orgUserRel.orgName);
    })
    const orgNameStr = orgNameArray.join(",");

    const orgListContent = this.state.userDetail.orgUserRels.map((org,i) =>{
      const listView = (
        <List key = {i}>
          <List.Item multipleLine extra={<Text style={styles.listExtra}>{org.relName || '无岗位'}</Text>}>
            <Text style={styles.listTitle}>{org.orgName}</Text>
          </List.Item>
        </List>
      );
      return listView;
    })

    const actionArray = [
      {
        title:'聊天',
        icon:<Icon name="ios-chatbubbles" size={26} style={styles.linkIcon} />,
        actionTo:this.openSession
      },
      {
        title:'打电话',
        icon:<Icon name="md-call" size={26} style={styles.linkIcon} />,
        actionTo:this.linkingPhone
      },
      {
        title:'发短信',
        icon:<Icon name="md-text" size={26} style={styles.linkIcon} />,
        actionTo:this.linkingSms
      },
      {
        title:'发邮件',
        icon:<Icon name="md-mail" size={23} style={styles.linkIcon} />,
        actionTo:this.linkingEmail
      }
    ]
    const sexContent = this.renderUserSex();
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <ScrollView>
            <List style={styles.userMainpad}>
              <View style={styles.userMain}>
                {<CirclePortrait isShowImage title={userDetail.user.fullname} uri={userDetail.user.photo} style={{width:70,height:70}}/>}
                <View style={styles.userText}>
                  <View style={styles.userName}>
                    <Text style={styles.userNameText} numberOfLines={1} ellipsizeMode='tail'>{userDetail.user ? userDetail.user.fullname : '未命名'}</Text>
                    <View style={{flex:0,justifyContent:'center',height:32,paddingLeft:10,}}>
                    {sexContent}
                    </View>
                  </View>
                  <View style={styles.theUserID}>
                    <Image source={idIcon} style={{width:12,height:12,}}></Image>
                    <Text style={styles.userIDText} numberOfLines={1} ellipsizeMode='tail'>{'帐号:'+userDetail.user.account}</Text>
                  </View>
                </View>
              </View>
            </List>
            <View style={styles.fastLink}>
              <GridView
                items={actionArray}
                itemsPerRow={4}
                renderItem={(dataItem,index) =>(
                  <TouchableOpacity key={index} activeOpacity ={1} style={styles.actionItem} onPress={() => dataItem.actionTo()}>
                    {dataItem.icon}
                    <Text numberOfLines={1} style={styles.linkTitle}>{dataItem.title}</Text>
                  </TouchableOpacity>
                )}
              />
            </View>
            <List>
              <List.Item multipleLine extra={<Text style={styles.listExtra}>{this.state.userDetail.user.mobile}</Text>}>
                <Text style={styles.listTitle}>常用电话</Text>
              </List.Item>
            </List>
            <List>
              <List.Item multipleLine extra={<Text style={styles.listExtra}>{this.state.userDetail.user.email}</Text>}>
                <Text style={styles.listTitle}>常用邮箱</Text>
              </List.Item>
            </List>
            <Text style={styles.organize}>所在组织与岗位</Text>
            <List style={styles.padTop}>
              {orgListContent}
            </List>
          </ScrollView>
        </View>
      </View>
    );
  }
}

const mapStateToProps = (state) => {
  const { message,contacts } = state;
  return {
    message,contacts
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  const contactsActions = bindActionCreators(contactsCreators, dispatch);
  return {
    messageActions,contactsActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(OtherUserDetail);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#fff'
  },
  content: {
    flex: 1,
    paddingBottom: 10,
    backgroundColor: '#f3f3f3'
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
  user_title: {
    marginTop: 10,
    height: 30
  },
  border_bottom: {
    borderBottomWidth: 0,
  },
  actionItem: {
    margin: 5,
    backgroundColor: 'transparent',
    alignItems: 'center',
  },
  userMainpad:{
    paddingBottom:10,
  },
  userMain:{
    flexDirection: 'row',
    alignItems: 'center',
    padding:15,
  },
  userPhoto:{
    flex: 0,
    width:70,
    height:70,
    borderRadius:35,
    backgroundColor:'#4fabf4',
    alignItems: 'center',
    overflow:'hidden',
  },
  userPhotoText:{
    fontSize:18,
    color:'#fff',
    textAlign:'center',
    lineHeight:70,
  },
  userPhotoImage:{
    width:70,
    height:70,
  },
  userText:{
    flex:1,
    paddingLeft:10,
  },
  userName:{
    flexDirection: 'row',
  },
  userNameText:{
    flex:0,
    maxWidth:200,
    justifyContent:'center',
    fontSize:16,
    color:'#000000',
    height:32,
    lineHeight:32,
  },
  theUserID:{
    flexDirection: 'row',
    paddingTop:5,
  },
  userIDText:{
    fontSize:12,
    color:'#8a8a8a',
    lineHeight:13,
    paddingLeft:5,
  },
  fastLink:{
    paddingBottom:5,
    paddingTop:5,
    marginBottom:10,
    backgroundColor:'#fff',
  },
  linkIcon:{
    color:'#6cb9f6',
    textAlign:'center',
  },
  linkTitle:{
    fontSize:14,
    color:'#6cb9f6',
  },
  listTitle:{
    fontSize:16,
    color:'#262626',
  },
  listExtra:{
    fontSize:14,
    color:'#8a8a8a',
    lineHeight:18,
  },
  padTop: {
    paddingTop: 8,
  },
  organize:{
    fontSize:12,
    color:'#8a8a8a',
    paddingTop: 12,
    paddingLeft: 15,
    paddingRight: 15,
  },
});
