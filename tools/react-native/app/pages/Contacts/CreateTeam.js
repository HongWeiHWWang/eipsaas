
import React from 'react';
import { StyleSheet,View,Image, Text, Linking,ScrollView,Dimensions,Platform,DeviceEventEmitter } from 'react-native';
import { connect } from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';
import * as store from '../../utils/StoreUtil';
import { bindActionCreators } from 'redux';
import LoadingView from '../../components/LoadingView';
import SelectUser from './SelectUser';
import * as messageCreators from '../../actions/message';
import {SESSION_P2P,SESSION_TEAM} from '../../constants/Constans';
import * as SessionService from '../Message/SessionService';
import ToastUtil from '../../utils/ToastUtil';
class CreateTeam extends React.Component {

  static navigationOptions = ({ navigation }) => ({
    title: '发起群聊',
  });

  constructor(props) {
    super(props);
    this.state = {
    };
  }

  componentDidMount() {
    store.get('userDetail').then((userDetail) => {
      if(userDetail){
        this.setState({
          orgUserRels:userDetail.orgUserRels,
          user:userDetail.user
        })
      }
    });
  }

  onSelectOk = (users) =>{
    if(global.imClient){
      const params ={
        scene:SESSION_TEAM,
        userStr:users.join(',')
      }
      SessionService.createSession(params,this.props);
    }else{
      ToastUtil.showShort('连接消息服务器失败');
    }
    
  }

  render() {
    return (
      <SelectUser
        onSelectOk={this.onSelectOk}
        disabledCurrentUser={true}
      />
    );
  }
}

const mapStateToProps = (state) => {
  const { contacts,message } = state;
  return {
    contacts,message
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  return {
    messageActions
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateTeam);

const styles = StyleSheet.create({

});
