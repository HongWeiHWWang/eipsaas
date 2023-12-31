
import React from 'react';
import { StyleSheet, Text, View,ScrollView,Image,Dimensions,ListView,TextInput } from 'react-native';

import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import * as constans from '../../constants/Constans';
import { Input } from 'react-native-elements';
import { connect } from 'react-redux';
import List from 'antd-mobile-rn/lib/list';
import { bindActionCreators } from 'redux';
import * as userCreators from '../../actions/user';
import * as messageCreators from '../../actions/message';
import Search from '../../components/Search';


import ScrollableTabView, {
  ScrollableTabBar
} from 'react-native-scrollable-tab-view';

class UnionSearch extends React.Component {

  static navigationOptions = {
    title: '查询',
    headerTitle:'查询',
    headerRight: (
      <Icon style={{paddingRight: 20,}} color="#fff" />
    ),
    headerTitleStyle: {
      alignSelf:'center',
      fontSize:18,
    },
  };

  constructor(props) {
    super(props);
    this.state = {
      generalContacts:[],
      sessionList:[],
      applicationList:[]
    };
  }

  onSearchTextChange = (text) =>{
    if(!text){
      this.setState({
        generalContacts:[],
        sessionList:[],
        applicationList:[]
      })
      return;
    }
    const generalContacts = [];
    this.props.contacts.generalContacts.map((contact,index) =>{
        if(contact.contactName.indexOf(text) > -1){
          generalContacts.push(contact);
        }
    });
    const sessionList = [];
    Object.values(this.props.message.sessionJson).map((session,index) =>{
      if(session.sessionTitle.indexOf(text) > -1){
        sessionList.push(session);
      }
    });

    const applicationList = [];
    this.props.work.applications.map((application,index) =>{
      const newWorks = [];
      const newApplication = {};
      application.works.map((work,index) =>{
        if(work.title.indexOf(text) > -1){
          newWorks.push(work);
        }
      });
      newApplication.name = application.name;
      newApplication.works = newWorks;
      applicationList.push(newApplication);
    })
    this.setState({
      generalContacts:generalContacts,
      sessionList:sessionList,
      applicationList:applicationList
    })
  }

  renderContactIcon = (user) =>{
    if (user.photo) {
      return <Image style={styles.contactIcon} source={{ uri: user.photo }} />;
    }else{
      return <Icon style={styles.contactIcon} name="ios-contact" size={22} color="#b21928" />;
    }
  }

  renderTabList = (type) =>{
    switch (type) {
      case '联系人':
        return this.renderContactList()
        break;
      case '会话':
        return this.renderSessionList()
        break;
      case '应用':
        return this.renderApplicationList()
        break;
      default:
        return(<Text></Text>)
    }
  }

  renderApplicationList = () =>{
    const applicationListContent = this.state.applicationList.map((item,i) => {
      const listView = (
        <List key={i} renderHeader={item.name}>
          {
            item.works.map((work,index) =>{
                const workView = (
                  <List.Item  key = {index}
                    thumb={<Image style={styles.applicationIcon} source={{ uri: work.icon }} />}
                    onClick={() => {this.openApplication(work)}}
                    arrow="horizontal"
                  >
                    {work.title}
                  </List.Item>
                )
                return workView;
            })
          }
        </List>
      );
      return listView;
    });
    return (applicationListContent);
  }

  openApplication = (application) =>{
    const { navigate } = this.props.navigation;
    
    if(!application.url){
      return false;
    }
    let url = "";
    if(constans.WORK_TYPE_FLOW == application.workType){
      const json = JSON.parse(application.url);
      url = global.server.web+"/mobile/view/bpm/startFlow.html?defId="+json.defId;
      
    }else{
      url = application.url;
    }
    const webItem = {
      title:application.title,
      url:url
    }
    this.props.navigation.navigate('Web', { webItem });
  }

  renderContactList = () =>{
    const generalContactContent = this.state.generalContacts.map((val,i) => {
      const listView = (
        <List.Item  key = {i}

          onClick={() => {this.openOhterUserDetail(val)}}
          arrow="horizontal"
        >
          {val.contactName}
        </List.Item>
      );
      return listView;
    });
    return (generalContactContent);
  }

  openOhterUserDetail = (user) =>{
    this.props.userActions.requestUserDetail(user.account,(message) =>{
      this.props.navigation.navigate('OtherUserDetail', {
        user: message
      });
    });
  }

  renderSessionList = () =>{
    const generalContactContent = this.state.sessionList.map((session,i) => {
      const listView = (
        <List.Item  key = {i}
          onClick={() => {this.openSession(session)}}
          arrow="horizontal"
        >
          {session.sessionTitle}
        </List.Item>
      );
      return listView;
    });
    return (generalContactContent);
  }

  openSession = (session) => {
    const { navigate } = this.props.navigation;
    navigate('ChatSession', {
      session
    });
  }

  render() {
    const tabList = ['联系人','会话','应用'];
    const content = tabList.map((val,index) => {
      const typeView = (
        <View key={index} tabLabel={val} style={styles.base}>
            {this.renderTabList(val)}
        </View>
      );
      return typeView;
    });
    return (
      <View style={styles.container}>
          <View>
            <Search onChangeText={(text) => this.onSearchTextChange(text)}/>
          </View>
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
export default connect(mapStateToProps, mapDispatchToProps)(UnionSearch);

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
  tab: {
    paddingBottom: 0,
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
});
