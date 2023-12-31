
import React from 'react';
import { StyleSheet, Image, Text, Linking, View,ScrollView } from 'react-native';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import Grid from 'antd-mobile-rn/lib/grid';
import List from 'antd-mobile-rn/lib/list';
import * as store from '../../utils/StoreUtil';
import * as contactsCreators from '../../actions/contacts';
import LoadingView from '../../components/LoadingView';
import ToastUtil from '../../utils/ToastUtil';
import * as userCreators from '../../actions/user';
import CirclePortrait from '../../components/CirclePortrait';


class OrgDetail extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    title: navigation.state.params.org.orgName,
    tabBarIcon: ({ tintColor }) => (
      <Icon name="md-contacts" size={20} color={tintColor} />
    )
  });

  constructor(props) {
    super(props);
    this.state = {
      orgUserList:[],
      orgTreeList:[],
      loading:true
    };
  }

  componentDidMount() {
    const { params } = this.props.navigation.state;
    if(params.org){
      const {dispatch} = this.props;
      const contactsActions = bindActionCreators(contactsCreators, dispatch);
      contactsActions.requestOrgDetail(params.org.orgCode,this.requestOrgDetailCallback);
    }
  }

  requestOrgDetailCallback = (message) =>{
    this.setState({
      orgUserList:message.orgUserList,
      orgTreeList:message.orgTreeList,
      loading:false
    })
  }

  openOrgDetail = (o) =>{
    const {dispatch} = this.props;
    const contactsActions = bindActionCreators(contactsCreators, dispatch);
    contactsActions.requestOrgDetail(o.code,this.requestOrgDetailCallback);
    /*const { navigate } = this.props.navigation;
    navigate('OrgDetail', { org });*/
  }

  openUserDetail =(user) =>{
    const userActions = bindActionCreators(userCreators, this.props.dispatch);
    store.get('loginUser').then((loginUser) => {
      if (loginUser) {
        if(loginUser.account == user.ACCOUNT_){
          userActions.requestUserDetail(user.ACCOUNT_,(message) =>{
            this.props.navigation.navigate('UserDetail',{user:message});
          });
        }else{
          userActions.requestUserDetail(user.ACCOUNT_,(message) =>{
            this.props.navigation.navigate('OtherUserDetail', {
              user: message
            });
          });
        }
      }
    });
  }

  renderContactIcon = (user) =>{
    return (
      <CirclePortrait 
          isShowImage={true} 
          title={user.fullname} 
          uri={global.server.portal+user.photo} 
          style={{width:36,height:36,marginRight:10}} 
          textStyle={{fontSize:12,lineHeight:36}} />
    )
    
    // if (user.photo) {
    //   return <Image style={styles.contactIcon} source={{ uri: user.photo }} />;
    // }else{
    //   return <Text style={styles.userLastName}>{user.fullname.substr(user.fullname.length-2)}</Text>;
    // }
  }

  renderContent = () =>{
    if(this.state.loading){
      return (<LoadingView />)
    }
    const orgUserContent = this.state.orgUserList.map((val,i) => {
      const listView = (
        <List.Item  key = {i}
          thumb={this.renderContactIcon(val)}
          onClick={() => {this.openUserDetail(val)}}
          arrow="horizontal"
        >
          {val.FULLNAME_}
        </List.Item>
      );
      return listView;
    });
    const orgTreeContent = this.state.orgTreeList.map((val,i) => {
      const listView = (
        <List.Item key = {i}
          thumb={<Icon name="ios-people-outline" size={22} color="#2c90e0" style={styles.orgTreeIcon}/>}
          onClick={() => {this.openOrgDetail(val)}}
          arrow="horizontal"
        >
          {val.name}
        </List.Item>
      );
      return listView;
    });
    return (
      <ScrollView>
        <List renderHeader={() => '部门成员'}>
          {orgUserContent}
        </List>
        <List renderHeader={() => '下级部门'}>
          {orgTreeContent}
        </List>
      </ScrollView>
    )
  }

  render() {
    const content = this.renderContent();
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          {content}
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#fff'
  },
  content: {
    flex: 1,
    paddingBottom: 10
  },
  orgUserIcon:{
    paddingLeft:10,
    paddingRight:5
  },
  orgTreeIcon:{
    paddingRight:10
  },
  contactIcon: {
    paddingRight: 10
  },
  userLastName:{
    fontSize:12,
    color:'#fff',
    width:36,
    height:36,
    lineHeight:36,
    textAlign:'center',
    borderRadius:18,
    backgroundColor: '#63baff',
    marginRight:10,
  },
});

const mapDispatchToProps = (dispatch) => {
  return {

  };
};
export default connect(mapDispatchToProps)(OrgDetail);
