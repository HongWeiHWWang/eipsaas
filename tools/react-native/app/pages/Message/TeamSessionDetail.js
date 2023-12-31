
import React from 'react';
import { StyleSheet, Image, Alert ,Text, View, DeviceEventEmitter, ScrollView,TouchableOpacity,Platform } from 'react-native';
import { bindActionCreators } from 'redux';
import * as store from '../../utils/StoreUtil';
import moment from 'moment';
import { connect } from 'react-redux';
import List from 'antd-mobile-rn/lib/list';
import Modal from 'antd-mobile-rn/lib/modal';
import Icon from 'react-native-vector-icons/Ionicons';
import * as messageCreators from '../../actions/message';
import {SESSION_P2P, SESSION_TEAM } from '../../constants/Constans';
import RequestUtil from '../../utils/RequestUtil';
import GridView from '../../components/GridView';
import * as userCreators from '../../actions/user';
import Button from 'antd-mobile-rn/lib/button';
import * as SessionService from '../Message/SessionService';
const editorIcon = require('../../img/icon/icon_24.png');
const timeIcon = require('../../img/icon/icon_25.png');
const addIcon = require('../../img/icon/icon_26.png');
const reduIcon = require('../../img/icon/icon_27.png');
const icon19 = require('../../img/icon/icon_19.png');
import CirclePortrait from '../../components/CirclePortrait';
import CropImagePicker from 'react-native-image-crop-picker';
import * as UploadUtil from '../../utils/UploadUtil';
import RNFetchBlob from 'rn-fetch-blob';
import Toast from 'antd-mobile-rn/lib/toast';

class TeamSessionDetail extends React.Component {

  static navigationOptions = ({ navigation }) => ({
      // title:'群设置',
      headerTitle:'群设置',
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
      imMessageSession:{},
      userList:[],
      userAlias:''
    };
  }

  componentWillUnmount() {
  }

  componentDidMount() {
    const { messageActions } = this.props;
    messageActions.initTeamSesionDetail({sessionCode:this.props.navigation.getParam('sessionCode'),callback:this.initTeamSesionDetailCallback})
  }

  initTeamSesionDetailCallback = (message) =>{
    this.setState({
      imMessageSession:message.imMessageSession,
      userList:message.userList,
      userAlias:message.userAlias
    })
  }

  invitationUser = () =>{

  }

  openMessageModel = (type) =>{
    let title = '';
    let sourceText = ''
    switch (type) {
      case 'title':
        title = '群名称';
        sourceText = this.state.imMessageSession.title;
        break;
      case 'userAlias':
        title = '群名片';
        sourceText = this.state.imMessageSession.userAlias;
        break;
      case 'description':
        title = '群说明';
        sourceText = this.state.imMessageSession.description;
        break;
      default:

    }
    Modal.prompt(
      title,
      '请输入你要修改的内容',
      [
        { text: '取消' },
        { text: '提交', onPress: text => this.changeTeamMessage(type,text) },
      ],'',
      sourceText,
    )
  }

  changeTeamMessage = (type,text,callback) =>{
    const imMessageSession = this.state.imMessageSession;
    const params = {
      type:'updateTeamMessage',
      sessionCode:imMessageSession.code
    }
    params[type] = text;
    const { messageActions } = this.props;
    messageActions.updateSession(params,(message)=>{
      if(type == "title"){
        const {sessionJson} = this.props.message;
        sessionJson[imMessageSession.code].sessionTitle = text;
        messageActions.receiveSessionJson(sessionJson);
      }
      if(type == "icon" && message.success){
        const {sessionJson} = this.props.message;
        sessionJson[imMessageSession.code].sessionIcon = message.icon;
        messageActions.receiveSessionJson(sessionJson);
        imMessageSession.icon = message.icon;
        this.setState({
          imMessageSession:imMessageSession
        })
      }
    });
    imMessageSession[type] = text;
    this.setState({
      imMessageSession:imMessageSession
    })
  }

  selectImage = () =>{
    CropImagePicker.openPicker({
      mediaType:'photo',
      includeExif: false,
      cropping:true,
      width: 500,
      height: 500,
      multiple:false
    }).then(images => {
      if(images.mime.indexOf("image") > -1){
        this.uploadImage(images);
      }  
    }).catch(e => {
    });
  }

  async uploadImage(media){
    const localPath = Platform.OS == 'android'?media.path.replace('file:///',''):media.path;
    const base64Data = RNFetchBlob.wrap(localPath);
    const options = {
      url:__CTX+'/system/file/v1/upload',
      files:[
        {
          name:'file',
          filename:'file.jpg',
          data:base64Data
        }
      ],
    }
    Toast.loading('上传中',1000);
    UploadUtil.fileUpload(options).then((rtn) =>{
      Toast.hide();
      const json = JSON.parse(rtn);
      if(json.success){
        this.changeTeamMessage("icon",json.fileId,(message) =>{
          if(message.success){
    
          }          
        });
      }
    })
  }

  openUserDetail = (user) =>{
    store.get('loginUser').then((loginUser) => {
      if (loginUser) {
        const userActions = this.props.userActions;
        if(loginUser.account == user.userAccount){
          userActions.requestUserDetail(user.userAccount,(message) =>{
            this.props.navigation.navigate('UserDetail',{user:message});
          });
        }else{
          userActions.requestUserDetail(user.userAccount,(message) =>{
            this.props.navigation.navigate('OtherUserDetail', {
              user: message
            });
          });
        }
      }
    });
  }

  quitTeam = () =>{
    Alert.alert(
      '退出群组',
      '消息记录也将会被删除',
      [
        {text: '取消', style: 'cancel'},
        {text: '确定', onPress: () => {
          const url = global.server.portal+"/im/imSessionUser/v1/quitTeamSession?sessionCode="+this.state.imMessageSession.code+"&owner="+this.state.imMessageSession.owner;
          RequestUtil.requestByStoreUser(url,'post',"{}").then((message) =>{
            if(message.success){
              SessionService.removeMessageHistory(this.state.imMessageSession.code);
              SessionService.removeSession(this.state.imMessageSession.code,this.props);
              this.props.navigation.navigate('Message');
            }
          })
        }},
      ],
    )
  }

  renderTeamIcon = () =>{
    const imMessageSession = this.state.imMessageSession;
    return (
      <CirclePortrait 
        title={imMessageSession.title} 
        uri={imMessageSession.icon} 
        style={{width:70,height:70}} 
        textStyle={{fontSize:18,lineHeight:70}} />
    )
  }

  renderUserIcon = (user) =>{
    return (
      <CirclePortrait 
        title={user.userAlias} 
        uri={user.photo} 
        style={{width:36,height:36}} 
        textStyle={{fontSize:12,lineHeight:36}} />
    )
  }

  render() {
    const imMessageSession = this.state.imMessageSession;
    const userList = this.state.userList;
    const teamIconContent = this.renderTeamIcon();
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <ScrollView>
            <View style={styles.teamMainpad}>
              <View style={styles.teamMain}>
                <TouchableOpacity onPress={this.selectImage} activeOpacity={1}>{teamIconContent}</TouchableOpacity>
                <View style={styles.teamText}>
                  <TouchableOpacity activeOpacity={1} style={styles.teamName} onPress={e => this.openMessageModel('title')}>
                    <Text style={styles.teamNameText} numberOfLines={1} ellipsizeMode='tail'>{imMessageSession.title}</Text>
                    <View style={{flex:0,justifyContent:'center',height:32,paddingLeft:10,}}><Image source={editorIcon} style={{width:16,height:16,}}></Image></View>
                  </TouchableOpacity>
                  <View style={styles.createTime}>
                    <Image source={timeIcon} style={{width:14,height:14,}}></Image>
                    <Text style={styles.timeText} numberOfLines={1} ellipsizeMode='tail'>创建时间：{moment(imMessageSession.createTime).format('YYYY-MM-DD')}</Text>
                  </View>
                </View>
              </View>
            </View>
            <View style={styles.teamMainpad}>
              <List.Item extra={<Text style={styles.listExtra}>{'共'+userList.length+'人'}</Text>}>
                  <Text style={styles.listTitle}>群成员</Text>
              </List.Item>
              <View style={styles.userListView}>
                <GridView
                  items={userList}
                  itemsPerRow={6}
                  renderItem={(user,index) =>(
                    <TouchableOpacity style={styles.userListItem} onPress={() => this.openUserDetail(user)}>
                      {this.renderUserIcon(user)}
                      <Text style={styles.userNameText} numberOfLines={1} ellipsizeMode='tail'>{user.userAlias}</Text>
                    </TouchableOpacity>
                  )}
                />
                {
                // <View style={styles.userListItem}>
                //   <Image source={addIcon} style={styles.userPhotoIcon}></Image>
                //   <Text style={styles.userNameText} numberOfLines={1} ellipsizeMode='tail'>邀请</Text>
                // </View>
                // <View style={styles.userListItem}>
                //   <Image source={reduIcon} style={styles.userPhotoIcon}></Image>
                //   <Text style={styles.userNameText} numberOfLines={1} ellipsizeMode='tail'>减员</Text>
                // </View>
                }
              </View>
            </View>
            <View style={styles.teamMainpad}>
              <List.Item  extra={<Text style={styles.listExtra}>{imMessageSession.userAlias}</Text>} arrow="horizontal" onClick={e => this.openMessageModel('userAlias')}>
                  <Text style={styles.listTitle}>我在本群昵称</Text>
              </List.Item>
              <List.Item multipleLine onClick={e => this.openMessageModel('description')}>
                  <Text style={styles.listTitle}>群介绍</Text>
                  <List.Item.Brief><Text style={styles.listExtra}>{imMessageSession.description ? imMessageSession.description:'未设置'}</Text></List.Item.Brief>
              </List.Item>
            </View>
            <View style={styles.teamMainpad}>
              <List.Item onClick={this.quitTeam} style={styles.logout_button}>
                <Text style={styles.logout_buttonText}>删除并退出</Text>
              </List.Item>
            </View>
          </ScrollView>
        </View>
      </View>
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
  const userActions = bindActionCreators(userCreators, dispatch);
  const messageActions = bindActionCreators(messageCreators, dispatch);
  return {
    messageActions,userActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(TeamSessionDetail);

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
  teamMainpad:{
    marginBottom:10,
    backgroundColor:"#fff"
  },
  teamMain:{
    flexDirection: 'row',
    alignItems: 'center',
    padding:15,
  },
  teamPhoto:{
    flex: 0,
    width:70,
    height:70,
    borderRadius:35,
    backgroundColor:'#4fabf4',
    alignItems: 'center',
    overflow:'hidden',
  },
  teamPhotoText:{
    fontSize:18,
    color:'#fff',
    textAlign:'center',
    lineHeight:70,
  },
  teamPhotoImage:{
    width:70,
    height:70,
  },
  teamText:{
    flex:1,
    paddingLeft:10,
  },
  teamName:{
    flexDirection: 'row',
  },
  teamNameText:{
    flex:0,
    maxWidth:200,
    justifyContent:'center',
    fontSize:16,
    color:'#000000',
    height:32,
    lineHeight:32,
  },
  createTime:{
    flexDirection: 'row',
    paddingTop:5,
  },
  timeText:{
    fontSize:12,
    color:'#8a8a8a',
    lineHeight:15,
    paddingLeft:5,
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
  userListView:{
    flexDirection: 'row',
    flex: 1,
    alignItems: 'center',
    paddingBottom:10,
    paddingLeft:5,
    paddingRight:5,
  },
  userListItem:{
    marginTop:10,
    backgroundColor: 'transparent',
    alignItems: 'center',
  },
  userPhotoText:{
    width:40,
    height:40,
    borderRadius:20,
    backgroundColor:'#4fabf4',
    fontSize:14,
    color:'#fff',
    textAlign:'center',
    lineHeight:40,
  },
  userPhotoImage:{
    width:40,
    height:40,
    borderRadius:20,
    overflow:'hidden',
  },
  userPhotoIcon:{
    width:40,
    height:40,
  },
  userNameText:{
    width:40,
    fontSize:12,
    color:'#8a8a8a',
    textAlign:'center',
    lineHeight:14,
    paddingTop:3,
  },
  logout_box: {
    paddingTop: 20,
  },
  logout_button: {
    justifyContent:'center',
    height:45,
  },
  logout_buttonText: {
    flex:1,
    fontSize:16,
    color:'#f26666',
    lineHeight:31,
    alignItems: 'center',
    textAlign:'center',
  },
});
