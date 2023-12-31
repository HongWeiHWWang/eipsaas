
import React from 'react';
import { StyleSheet,View,Image, Text, Linking,ScrollView,Dimensions,Platform,TouchableOpacity } from 'react-native';
import { connect } from 'react-redux';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import List from 'antd-mobile-rn/lib/list';
import * as store from '../../utils/StoreUtil';
import ToastUtil from '../../utils/ToastUtil';
import { bindActionCreators } from 'redux';
import * as contactsCreators from '../../actions/contacts';
import * as userCreators from '../../actions/user';
import GridView from '../../components/GridView';
import HeaderPopover from '../../components/HeaderPopover';
import CirclePortrait from '../../components/CirclePortrait';

const contacts_img = require('../../img/icon/icon_2.png');
const team_img = require('../../img/icon/icon_3.png');
const departmentIcon = require('../../img/icon/icon_17.png');
const underUserIcon = require('../../img/icon/icon_19.png');
const teamSessionIcon = require('../../img/icon/icon_18.png');

class Contacts extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    title: '通讯录',
    tabBarIcon: ({ tintColor }) => (
      <Icon name="md-contacts" size={23} color={tintColor} />
    ),
    headerRight: (
      <View style={styles.headerRightView}>
        <Icon onPress={() => {navigation.navigate('UnionSearch', {})}} style={styles.headerRightIcon}  name="ios-search" size={28} color="#fff" />
        <Icon onPress={() => {navigation.state.routes[navigation.state.index].params.headerAddPress()}}  style={styles.headerRightIcon} name="md-add" size={28} color="#fff" />
      </View>
    )
  });
  constructor(props) {
    super(props);
    this.state = {
      orgUserRels:[],
      popoverShow:false,
    };
  }

  componentDidMount() {
    store.get('userDetail').then((userDetail) => {
      if(userDetail){
        this.setState({
          orgUserRels:userDetail.orgUserRels
        })
      }
    });

    this.props.navigation.setParams({
      headerSearchPress: this.headerSearchPress,
      headerAddPress:this.headerAddPress
    });
  }

  headerAddPress = () =>{
    this.setState({
      popoverShow:!this.state.popoverShow
    })
  }

  headerSearchPress = () =>{
    this.props.navigation.navigate('UnionSearch', {});
  }

  openMyDepartment = () =>{
    this.props.navigation.navigate('MyDepartment');
  }

  openMyUnderUser = () =>{
    this.props.navigation.navigate('OrgTreeView');
  }

  openOrgTreeView = () =>{
    this.props.navigation.navigate('OrgTreeView');
  }

  openMyTeamSession = () =>{
    this.props.navigation.navigate('MyTeamSession');
  }

  readerTopGrid = () =>{
    const listData = [
      {
        icon:departmentIcon,
        title: '组织架构',
        actionTo:this.openOrgTreeView
      },
      {
        icon:teamSessionIcon,
        title: '我的群组',
        actionTo:this.openMyTeamSession
      }
    ];
    return (
      <GridView
        items={listData}
        itemsPerRow={4}
        renderItem={(dataItem,index) =>(
          <TouchableOpacity key={index} activeOpacity ={1} style={styles.gridItem} onPress={() => {dataItem.actionTo()}}>
             <Image style={styles.listDataItemIcon} source={dataItem.icon} />
            <Text numberOfLines={1} style={styles.gridTitle}>{dataItem.title}</Text>
          </TouchableOpacity>
        )}
      />
    )
  }

  openOrgDetail = (org) =>{
    const { navigate } = this.props.navigation;
    navigate('OrgDetail', { org });
  }

  openOhterUserDetail = (user) =>{
    this.props.userActions.requestUserDetail(user.account,(message) =>{
      this.props.navigation.navigate('OtherUserDetail', {
        user: message
      });
    });
  }

  renderContactIcon = (user) =>{
    if(!user.account){
      return <View/>
    }
    return (
        <CirclePortrait 
          isShowImage={true} 
          title={user.fullname} 
          uri={global.server.portal+user.photo} 
          style={{width:36,height:36,marginRight:10}} 
          textStyle={{fontSize:12,lineHeight:36}} />
        )
  }

  onCreateTeamPress =() =>{
    this.setState({
      popoverShow:false
    })
    this.props.navigation.navigate('CreateTeam');
    // const webItem = {
    //   title:'选择用户',
    //   url:__CTX+'/mobile/view/dialog/userSelectorDialog.html?dialog_alias_=userSelector',
    //   callback:(json) => console.warn(json)
    // }
    // this.props.navigation.navigate('Web', { webItem });
  }

  render() {
    const topGridContent = this.readerTopGrid();
    const departmentContent = this.state.orgUserRels.map((val,i) => {
      const listView = (
        <List.Item  key = {i}
          thumb={<Icon name="md-contacts" size={16} style={styles.orgTreeIcon}/>}
          onClick={() => {this.openOrgDetail(val)}}
          arrow="horizontal"
        >
          {val.orgName}
        </List.Item>
      );
      return listView;
    });
    
    const generalContactContent = this.props.contacts.generalContacts.map((val,i) => {
      const listView = (
        <List.Item  key = {i}
          thumb={this.renderContactIcon(val)}
          onClick={() => {this.openOhterUserDetail(val)}}
          arrow="horizontal"
          align="middle"
          style={styles.generalListName}
        >
          <View style={{flex:1,justifyContent:'center',}}>
            <Text style={{fontSize:16,color:'#262626',}}>{val.fullname}</Text>
          </View>
        </List.Item>
      );
      return listView;
    });
    const popoverItems =[{
        title:'发起群聊',
        icon:<Icon style={{paddingRight:5}} name="md-contacts" size={22} color="#fff" />,
        onPress:this.onCreateTeamPress
    }]
    return (
      <View>
        <ScrollView>
          <View style={styles.container}>
            <View style={styles.content}>
              <List style={{flex:1}}>
                 {topGridContent}
              </List>
              <List renderHeader={() => '常用联系人'} style={styles.departmentList}>
                  {generalContactContent}
              </List>
            </View>
          </View>
        </ScrollView>
        <HeaderPopover
          items={popoverItems}
          isVisible={this.state.popoverShow}
          onPopoverContainerPress={() => this.setState({popoverShow:!this.state.popoverShow})}/>
      </View>
    );
  }
}

const mapStateToProps = (state) => {
  const { contacts } = state;
  return {
    contacts
  };
};

const mapDispatchToProps = (dispatch) => {
  const contactsActions = bindActionCreators(contactsCreators, dispatch);
  const userActions = bindActionCreators(userCreators, dispatch);
  return {
    contactsActions,
    userActions
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Contacts);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#f3f3f3'
  },
  content: {
    flex: 1,
  },
  contactIcon: {
    paddingRight: 10
  },
  headerRightView: {
    flexDirection:'row',
  },
  headerRightIcon: {
    paddingRight: 20
  },
  orgTreeIcon:{
    paddingRight:10,
  },
  gridItem: {
    marginTop:15,
    marginBottom:15,
    backgroundColor: 'transparent',
    alignItems: 'center',
  },
  gridTitle:{
    fontSize: 13,
    textAlign: 'center',
    color: '#5e5d5d',
    marginTop: 5,
    width:80,
  },
  listDataItemIcon:{
    width:45,
    height:45,
  },

  generalListName:{
    height:56,
  }
});
