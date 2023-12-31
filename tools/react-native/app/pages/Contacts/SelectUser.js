import PropTypes from 'prop-types';
import React from 'react';
import {ViewPropTypes, StyleSheet,View,Image, Text, Linking,ScrollView,Dimensions,Platform } from 'react-native';
import { connect } from 'react-redux';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import Grid from 'antd-mobile-rn/lib/grid';
import List from 'antd-mobile-rn/lib/list';
import Button from 'antd-mobile-rn/lib/button';
import Checkbox from 'antd-mobile-rn/lib/checkbox';
import * as store from '../../utils/StoreUtil';
import ToastUtil from '../../utils/ToastUtil';
import { bindActionCreators } from 'redux';
import * as contactsCreators from '../../actions/contacts';
import * as userCreators from '../../actions/user';
import LoadingView from '../../components/LoadingView';

class SelectUser extends React.Component {

  static navigationOptions = ({ navigation }) => ({
    title: '发起群聊',
  });
  constructor(props) {
    super(props);
    this.state = {
      orgUserRels:[],
      orgUserList:[],
      orgTreeList:[],
      selectedUsers:[],
      user:{},
      loading:false,
      firstPage:true
    };
    this.renderContent = this.renderContent.bind(this);
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

  openOrgDetail = (orgCode) =>{
    this.setState({
      loading:true,
    })
    const {contactsActions} = this.props;
    contactsActions.requestOrgDetail(orgCode,this.requestOrgDetailCallback);
  }

  requestOrgDetailCallback = (message) =>{
    this.setState({
      orgUserList:message.orgUserList,
      orgTreeList:message.orgTreeList,
      loading:false,
      firstPage:false
    })
  }

  renderContactIcon = (user) =>{
    if (user.photo) {
      return <Image style={styles.contactIcon} source={{ uri: user.photo }} />;
    }else{
      return <Icon style={styles.contactIcon} name="ios-person-outline" size={22} color="#2c90e0" />;
    }
  }

  onContactChange = (account) =>{
    const oldArray = this.state.selectedUsers;
    const index =  oldArray.findIndex(function(value,index,arr){
      return value ===account;
    })
    if(index != -1){
      oldArray.splice(index,1);
    }else{
      oldArray.push(account)
    }
    this.setState({
      selectedUsers:oldArray
    })
  }

  renderCheck = (item) =>{
    return this.state.selectedUsers.includes(item.account);
  }

  renderDisabled = (item) =>{
    if(this.props.disabledCurrentUser && item.account == this.state.user.account){
      return true;
    }
    return false;
  }

  renderContent = () =>{
    if(this.state.loading){
      return (<LoadingView />)
    }
    if(this.state.firstPage){
      const departmentContent = this.state.orgUserRels.map((item,i) => {
        const listView = (
          <List.Item  key = {i}
            thumb={<Icon name="md-contacts" size={16} style={styles.orgTreeIcon}/>}
            onClick={() => {this.openOrgDetail(item.orgCode)}}
            arrow="horizontal"
          >
            {item.orgName}
          </List.Item>
        );
        return listView;
      });
      const generalContactContent = this.props.contacts.generalContacts.map((item,index) => {
        const listView = (
          <Checkbox.CheckboxItem key={index} checked={this.renderCheck(item)} disabled={this.renderDisabled(item)} onChange={() => this.onContactChange(item.account)}>
              {item.fullname}
          </Checkbox.CheckboxItem>
        );
        return listView;
      });
      return (
        <View style={styles.content}>
          <List renderHeader={() => '组织关系'} style={styles.departmentList}>
          {departmentContent}
          </List>
          <List renderHeader={() => '常用联系人'} style={styles.departmentList}>
              {generalContactContent}
          </List>
        </View>
        )
    }else{
      const orgUserContent = this.state.orgUserList.map((item,index) => {
        const listView = (
          <Checkbox.CheckboxItem key={index}  checked={this.renderCheck(item)} disabled={this.renderDisabled(item)} onChange={() => this.onContactChange(item.account)}>
              {item.fullname}
          </Checkbox.CheckboxItem>
        );
        return listView;
      });
      const orgTreeContent = this.state.orgTreeList.map((item,i) => {
        const listView = (
          <List.Item key = {i}
            thumb={<Icon name="md-contacts" size={16} style={styles.orgTreeIcon}/>}
            onClick={() => {this.openOrgDetail(item.code)}}
            arrow="horizontal"
          >
            {item.name}
          </List.Item>
        );
        return listView;
      });
      return (
        <View style={styles.content}>
          <List renderHeader={() => '部门成员'} style={styles.departmentList}>
              {orgUserContent}
          </List>
          <List renderHeader={() => '下级部门'}>
            {orgTreeContent}
          </List>
        </View>
      )
    }
  }
  render() {

    const content = this.renderContent();
    return (
      <View style={{flex:1}}>
        <ScrollView style={styles.container}>
            {content}
        </ScrollView>
        <View style={styles.bottomView}>
          <Text style={styles.bottomSelectText}>已选择:{this.state.selectedUsers.length}人</Text>
          <Button style={styles.bottomButton} onClick={() => this.props.onSelectOk(this.state.selectedUsers)}><Text style={styles.bottomButtonText}>确定</Text></Button>
        </View>
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

export default connect(mapStateToProps, mapDispatchToProps)(SelectUser);

const propTypes = {
  onSelectOk: PropTypes.func,
  initUsers: PropTypes.array,
  disabledCurrentUser:PropTypes.bool
};

SelectUser.propTypes = propTypes;

SelectUser.defaultProps = {
  onSelectOk() {},
  initUsers:[],
  disabledCurrentUser:false
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    height: Dimensions.get('window').height - 130
  },
  content: {

  },
  topGrid: {
    flex: 1,
    alignItems: 'center',
    padding:12.5
  },
  topGridView:{
    flex: 1,
    alignItems: 'center'
  },
  topGridImg: {
    width: 50,
    height: 50,
    marginTop: 10
  },
  topGridTitle: {
    fontSize: 16,
    alignItems: 'center',
    color: '#000',
    marginTop: 5
  },
  itemName: {
    fontSize: 16,
    color: '#000',
    fontWeight: '600',
  },
  orgTreeIcon:{
    paddingRight:10
  },
  bottomButton:{
    width:100,
    height:40,
    backgroundColor:'#2c90e0',
    marginRight:10
  },
  bottomView:{
    height:50,
    backgroundColor:'#fff',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  bottomSelectText:{
    flex:1,
    paddingLeft:10,
    color:'#000',
    fontSize:16
  },
  bottomButtonText:{
    color:'#fff'
  },
});
