
import React from 'react';
import { StyleSheet, Text, View,Image,Dimensions,TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { connect } from 'react-redux';
import List from 'antd-mobile-rn/lib/list';
import { bindActionCreators } from 'redux';
import * as userCreators from '../../actions/user';
import * as messageCreators from '../../actions/message';
import Search from '../../components/Search';
import RequestUtil from '../../utils/RequestUtil';
import ActionSheet from '@expo/react-native-action-sheet';
import CirclePortrait from '../../components/CirclePortrait';

import ScrollableTabView, {
  ScrollableTabBar
} from 'react-native-scrollable-tab-view';

class MyTeamSession extends React.Component {

  static navigationOptions = {
    headerTitle:'我的群组'
  };

  constructor(props) {
    super(props);
    this.state = {
      myCreateList:[],
      myJoinList:[]
    };
  }

  componentDidMount() {
    this.loadMyTeamSession();
  }

  loadMyTeamSession = () =>{
    const url = `${global.server.portal}/im/imSessionUser/v1/myTeamSessionList`;
    const myCreateList = [];
    const myJoinList = [];
    RequestUtil.requestByStoreUser(url,'get').then((message) =>{
      if(message.success){
        message.imSessionUserList.map(function(imSession){
          if(global.loginUser.account == imSession.sessionOwner){
            myCreateList.push(imSession);
          }else{
            myJoinList.push(imSession);
          }
        })
        this.setState({
          myCreateList:myCreateList,
          myJoinList:myJoinList
        })
      }
    })
  }

  renderContactIcon = (session) =>{
    return (<View style={{marginRight:10}}><CirclePortrait title={session.sessionTitle} uri={global.server.portal+session.sessionIcon} style={{width:28,height:28}} textStyle={{lineHeight:28,fontSize:12}}/></View>)
  }

  renderTabList = (type) =>{
    switch (type) {
      case '我创建的':
        return this.renderMyCreate()
        break;
      case '我加入的':
        return this.renderMyJoin()
        break;
      default:
        return(<Text></Text>)
    }
  }

  onLongPress = (session) =>{
    const options = [
      '移除:'+session.sessionTitle,
      '删除记录',
      '取消'
    ];
    const cancelButtonIndex = options.length - 1;
    this._actionSheetRef.showActionSheetWithOptions({
      options,
      cancelButtonIndex,
    },
    (buttonIndex) => {
      switch (buttonIndex) {
        case 0:
          this.removeSession(session);
          break;
        case 1:
          this.removeMessageHistory(session);
          break;
      }
    });
  }

  renderMyCreate = () =>{
    const content = this.state.myCreateList.map((session,i) => {
      const listView = (
          <List.Item  key = {i}
            thumb={this.renderContactIcon(session)}
            arrow="horizontal"
            onClick={() => {this.openSession(session)}}
          >
            {session.sessionTitle}
          </List.Item>
      );
      return listView;
    });
    return (content);
  }

  renderMyJoin = () =>{
    const content = this.state.myJoinList.map((session,i) => {
      const listView = (
        <List.Item  key = {i}
          thumb={this.renderContactIcon(session)}
          onClick={() => {this.openSession(session)}}
          arrow="horizontal"
        >
          {session.sessionTitle}
        </List.Item>
      );
      return listView;
    });
    return (content);
  }

  openSession = (session) => {
    const { navigate } = this.props.navigation;
    navigate('ChatSession', {
      session
    });
  }

  render() {
    const tabList = ['我创建的','我加入的'];
    const content = tabList.map((val,index) => {
      const typeView = (
        <View key={index} tabLabel={val} style={styles.base}>
            {this.renderTabList(val)}
        </View>
      );
      return typeView;
    });
    return (
      <ActionSheet ref={component => this._actionSheetRef = component}>
        <View style={styles.container}>
            <ScrollableTabView
              renderTabBar={() => (
                <ScrollableTabBar
                  tabStyle={styles.tab}
                  textStyle={styles.tabText}
                />
              )}
              tabBarUnderlineStyle={styles.tabBarUnderline}
              tabBarBackgroundColor="#fcfcfc"
              tabBarActiveTextColor="#3e9ce9"
              tabBarInactiveTextColor="#aaaaaa"
            >
              {content}
            </ScrollableTabView>
        </View>
      </ActionSheet>
    );
  }
}

const mapStateToProps = (state) => {
  const { contacts,message,work } = state;
  return {
    contacts:contacts,
    message:message,
    work:work
  };
};

const mapDispatchToProps = (dispatch) => {
  const userActions = bindActionCreators(userCreators, dispatch);
  const messageActions = bindActionCreators(messageCreators, dispatch);
  return {
    userActions,messageActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(MyTeamSession);

const styles = StyleSheet.create({
  base: {
    flex: 1
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#fcfcfc'
  },
  content: {
    flex: 1,
    paddingBottom: 10
  },
  searchContainer:{
    borderBottomWidth:0,
    paddingLeft:5,
    width: Dimensions.get('window').width
  },
  tab: {
    paddingBottom: 0
  },
  tabText: {
    fontSize: 16
  },
  tabBarUnderline: {
    backgroundColor: '#3e9ce9',
    height: 2
  },
  contactIcon: {
    paddingRight: 10
  },
  applicationIcon: {
    width: 20,
    height: 20,
    marginRight: 10,
  },
  iconText:{flex:0,justifyContent:'center',width:28,height:28,lineHeight:28,textAlign: 'center',backgroundColor:'#4fabf4',borderRadius:20,color:'#fff',},
  iconImage:{flex:0,justifyContent:'center',width:28,height:28,borderRadius:28,overflow:'hidden',},
});
