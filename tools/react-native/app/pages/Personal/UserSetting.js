
import React from 'react';
import { StyleSheet, Image, Text, Linking, View ,DeviceEventEmitter,ScrollView} from 'react-native';
import { connect } from 'react-redux';
import * as store from '../../utils/StoreUtil';

import Switch from 'antd-mobile-rn/lib/switch';
import List from 'antd-mobile-rn/lib/list';
import { bindActionCreators } from 'redux';


class UserSetting extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    // title: '设置',
    headerTitle:'设置',
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
      vibration:global.userSetting.vibration,
      notification:global.userSetting.notification
    };
  }

  onVibrationChange = () =>{
    this.setState({
      vibration:!this.state.vibration
    })
    global.userSetting.vibration = !this.state.vibration
    const loginUser = this.props.user.loginUser
    store.save(loginUser.account+'_userSetting',global.userSetting);
  }

  onNotificationsChange = () =>{
    this.setState({
      notification:!this.state.notification
    })
    global.userSetting.notification = !this.state.notification
    const loginUser = this.props.user.loginUser
    store.save(loginUser.account+'_userSetting',global.userSetting);
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <List
            renderHeader={() => '消息提醒'}
          >
            <List.Item
              extra={<Switch color='#B2DFDB' checked={this.state.notification} onChange={this.onNotificationsChange}/>}
            >通知栏提醒</List.Item>
            <List.Item
              extra={<Switch color='#B2DFDB' checked={this.state.vibration} onChange={this.onVibrationChange}/>}
            >震动提醒</List.Item>
          </List>
        </View>
      </View>
    );
  }
}

const mapStateToProps = (state) => {
  const {user} = state
  return {
    user
  };
};

const mapDispatchToProps = (dispatch) => {
  return {

  };
};
export default connect(mapStateToProps, mapDispatchToProps)(UserSetting);

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
});
