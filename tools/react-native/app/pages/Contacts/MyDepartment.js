
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

class MyDepartment extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    title: '所在部门'
  });
  constructor(props) {
    super(props);
    this.state = {
      orgUserRels:[]
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
    if (user.photo) {
      return <Image style={styles.contactIcon} source={{ uri: user.photo }} />;
    }else{
      return <Icon style={styles.contactIcon} name="ios-person-outline" size={22} color="#2c90e0" />;
    }
  }

  render() {
    const departmentContent = this.state.orgUserRels.map((val,i) => {
      const listView = (
        <List.Item  key = {i}
          onClick={() => {this.openOrgDetail(val)}}
          arrow="horizontal"
        >
          {val.orgName}
        </List.Item>
      );
      return listView;
    });

    return (
      <View>
        <ScrollView>
          <View style={styles.container}>
            <View style={styles.content}>
              <List >
                 {departmentContent}
              </List>
            </View>
          </View>
        </ScrollView>
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

export default connect(mapStateToProps, mapDispatchToProps)(MyDepartment);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#f3f3f3'
  },
  content: {
    flex: 1,
    paddingBottom: 10
  },
  orgTreeIcon:{
    paddingRight:10
  },
});
