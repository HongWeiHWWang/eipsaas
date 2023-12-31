
import {createStackNavigator,createBottomTabNavigator  } from 'react-navigation';

import Icon from 'react-native-vector-icons/Ionicons';
import React from 'react';
import {
  Text,Platform
} from 'react-native';
import DeviceInfo from 'react-native-device-info'

import Splash from '../pages/Splash';
import MainContainer from '../containers/MainContainer';
import WebViewPage from '../pages/WebView/WebViewPage';

import Work from '../pages/Work/Work';
import Message from '../pages/Message/Message';

import ChatSession from '../pages/Message/ChatSession';
import Contacts from '../pages/Contacts/Contacts';
import OrgDetail from '../pages/Contacts/OrgDetail';
import Personal from '../pages/Personal/Personal';
import Login from '../pages/Login/Login';
import UserDetail from '../pages/Personal/UserDetail';
import OtherUserDetail from '../pages/Contacts/OtherUserDetail';
import UnionSearch from '../pages/Search/UnionSearch';
import CreateTeam from '../pages/Contacts/CreateTeam';
import TeamSessionDetail from '../pages/Message/TeamSessionDetail';
import UserSetting from '../pages/Personal/UserSetting';
import VideoPlayer from '../pages/Video/VideoPlayer';
import MyDepartment from '../pages/Contacts/MyDepartment';
import MyUnderUser from '../pages/Contacts/MyUnderUser';
import MyTeamSession from '../pages/Contacts/MyTeamSession';
import OrgTreeView from '../pages/Contacts/OrgTreeView';
import ServerSetting from '../pages/Personal/ServerSetting';

const TabContainer = createBottomTabNavigator (
  {
    Main: { screen: MainContainer,mode:"card" },
    Work: { 
      screen: Work,
      mode:"card" ,
    },
    Message: { 
      screen: Message,
      mode:"card" ,
    },
    Contacts: { screen: Contacts,mode:"card" },
    Personal: { screen: Personal,mode:"card" }
  },
  {
    lazy: false,
    tabBarPosition: 'bottom',
    tabBarOptions: {
      activeTintColor: '#2c90e0',
      inactiveTintColor: '#8a8a8a',
      showIcon: true,
      showLabel:true,
      style: {
        backgroundColor: '#fff',
        height:50,
      },
      labelStyle:{
        fontSize:11,
        paddingTop:0,
        marginTop:0,
      },
      indicatorStyle: {
        opacity: 0
      },
      tabStyle: {
        padding: 0,
        marginTop:0,
      },
    }
  }
);

TabContainer.navigationOptions = ({ navigation }) => {
  const component = TabContainer.router.getComponentForState(navigation.state)
  if (typeof component.navigationOptions === 'function') {
    return component.navigationOptions({ navigation })
  }
  return component.navigationOptions
}

const App = createStackNavigator(
  {
    Splash: { screen: Splash },
    Home: {
      screen: TabContainer,
      navigationOptions: {
        headerLeft: null
      },
    },
    Login: {
      screen: Login
    },
    Web: { screen: WebViewPage },
    ChatSession: { screen: ChatSession },
    OrgDetail: { screen: OrgDetail },
    UserDetail: { screen: UserDetail },
    OtherUserDetail:{screen:OtherUserDetail},
    UnionSearch:{screen:UnionSearch},
    CreateTeam:{screen:CreateTeam},
    TeamSessionDetail:{screen:TeamSessionDetail},
    UserSetting:{screen:UserSetting},
    VideoPlayer:{screen:VideoPlayer},
    MyDepartment:{screen:MyDepartment},
    MyUnderUser:{screen:MyUnderUser},
    MyTeamSession:{screen:MyTeamSession},
    OrgTreeView:{screen:OrgTreeView},
    ServerSetting:{screen:ServerSetting}
  },
  {
    headerMode: 'screen',
    navigationOptions: {
      headerStyle: {
        backgroundColor: '#2c90e0',
        height:65,
        paddingTop:Platform.OS == 'ios' ? 0 : 25
      },
      headerTitleStyle: {
        color: '#fff',
        fontSize: 18,
      },
      headerTintColor: '#fff',
    }
  }
);

export default App;
