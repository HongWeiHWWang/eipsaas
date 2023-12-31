
import React from 'react';
import { StyleSheet, Image, Text, Linking, View ,DeviceEventEmitter,ScrollView,Platform} from 'react-native';
import { connect } from 'react-redux';
import * as store from '../../utils/StoreUtil';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import Modal from 'antd-mobile-rn/lib/modal';
import Button from 'antd-mobile-rn/lib/button';
import List from 'antd-mobile-rn/lib/list';
import Toast from 'antd-mobile-rn/lib/toast';
import { bindActionCreators } from 'redux';
import * as messageCreators from '../../actions/message';
import {SESSION_P2P,SESSION_TEAM} from '../../constants/Constans';
import * as SessionService from '../Message/SessionService';
import * as userCreators from '../../actions/user';
import CirclePortrait from '../../components/CirclePortrait';
import CropImagePicker from 'react-native-image-crop-picker';
import * as UploadUtil from '../../utils/UploadUtil';
import RNFetchBlob from 'rn-fetch-blob';

class UserDetail extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    // title: navigation.state.params.user.user.fullname,
    // tabBarIcon: ({ tintColor }) => (
    // <Icon name="md-person" size={25} color={tintColor} />
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
    const { dispatch, user } = this.props;
    this.setState({
      userDetail:this.props.navigation.getParam('user')
    })
  }

  openMessageModel = (type) =>{
    let title = '';
    let sourceText = ''
    switch (type) {
      case 'mobile':
        title = '常用电话';
        sourceText = this.state.userDetail.user.mobile;
        break;
      case 'email':
        title = '常用邮箱';
        sourceText = this.state.userDetail.user.email;
        break;
      case 'fullname':
        title = '姓名';
        sourceText = this.state.userDetail.user.fullname;
        break;
      default:
    }
    Modal.prompt(
      title,
      '请输入你要修改的内容',
      [
        { text: '取消' },
        { text: '提交', onPress: text => this.changeUserMessage(type,text) },
      ],'',
      sourceText,
    )
  }

  changeUserMessage = (type,text,callback) =>{
    const userDetail = this.state.userDetail;
    const params = {
      type:'updateUserMessage',
      account:userDetail.user.account
    }
    params[type] = text;
    const { userActions } = this.props;
    userActions.updateUserMessage(params,callback);

    userDetail.user[type] = text;
    this.setState({
      userDetail:userDetail
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
      url:global.server.portal+'/system/file/v1/upload',
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
      rtn = JSON.parse(rtn)
      if(rtn.state){
      this.changeUserMessage("photo",JSON.parse(rtn.value).fileId,(message) =>{
          
          if(message.success){
            const userDetail = this.state.userDetail;
            userDetail.user.photo = message.photo
            this.setState({
              userDetail:userDetail
            })
            const {userActions} = this.props;
            userActions.receiveUserDetail(userDetail);
          }          
        });
      }
    })
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
          <List.Item  key = {i} multipleLine extra={<Text style={styles.listExtra}>{org.relName || '无岗位'}</Text>}>
            <Text style={styles.listTitle}>{org.orgName}</Text>
          </List.Item>
      );
      return listView;
    })
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <ScrollView>
            <List>
              <List.Item onClick={this.selectImage} extra={<CirclePortrait title={userDetail.user.fullname} uri={global.server.portal+userDetail.user.photo} style={{width:40,height:40}} textStyle={{lineHeight:40,fontSize:12}}/>} multipleLine >
                <Text style={styles.listTitle}>头像</Text>
              </List.Item>
              <List.Item extra={<Text style={styles.listExtra}>{this.state.userDetail.user.fullname}</Text>} onClick={() => this.openMessageModel('fullname')} arrow="horizontal" multipleLine >
                <Text style={styles.listTitle}>姓名</Text>
              </List.Item>

              <List.Item extra={<Text style={styles.listExtra}>{this.state.userDetail.user.mobile}</Text>} onClick={() => this.openMessageModel('mobile')} arrow="horizontal" multipleLine >
                <Text style={styles.listTitle}>常用电话</Text>
              </List.Item>

              <List.Item extra={<Text style={styles.listExtra}>{this.state.userDetail.user.email}</Text>} onClick={() => this.openMessageModel('email')} arrow="horizontal" multipleLine >
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
  const { message } = state;
  return {
    message
  };
};

const mapDispatchToProps = (dispatch) => {
  const userActions = bindActionCreators(userCreators, dispatch);
  return {
    userActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(UserDetail);

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
  user_title_list: {
    height: 150,
    marginBottom: 5
  },
  user_title: {
    marginTop: 10,
    height: 30
  },
  user_photo: {
    paddingRight: 20,
    paddingLeft: 10
  },
  logout_button: {
    paddingRight: 20,
    paddingLeft: 20,
    paddingTop: 30
  },
  border_bottom: {
    borderBottomWidth: 0,
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
  photoText:{
    width:40,
    height:40,
    borderRadius:20,
    backgroundColor:'#4fabf4',
    fontSize:12,
    color:'#fff',
    textAlign:'center',
    lineHeight:40,
  },
  photoImage:{
    width:40,
    height:40,
    borderRadius:20,
    overflow:'hidden',
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
