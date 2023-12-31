
import React from 'react';
import { StyleSheet,View,Image, Text, Linking,ScrollView,Dimensions,Platform,TouchableOpacity } from 'react-native';
import { connect } from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';
import List from 'antd-mobile-rn/lib/list';
import { bindActionCreators } from 'redux';
import * as contactsCreators from '../../actions/contacts';
import * as userCreators from '../../actions/user';
import RequestUtil from '../../utils/RequestUtil';
import CirclePortrait from '../../components/CirclePortrait';

class MySubordinate extends React.Component {

  static navigationOptions = ({ navigation }) => ({
    title: '我的下属'
  });

  constructor(props) {
    super(props);
    this.state = {
      underUserList:[]
    };
  }

  componentDidMount() {
    this.loadUnderUser();
  }

  loadUnderUser = () =>{
    const url = global.server.uc+"/api/user/v1/user/getUnderUsers?userId="+global.loginUser.userId;
    RequestUtil.requestByStoreUser(url,'get').then((message) =>{
      if(message.success){
        this.setState({
          underUserList:message.underUserList
        })
      }
    })
  }

  renderContactIcon = (user) =>{
    return (<View style={{paddingRight:10}}><CirclePortrait title={user.fullname} uri={global.server.portal+user.photo} textStyle={{lineHeight:36,fontSize:12}} style={{width:36,height:36,}}/></View>)
  }

  openUserDetail =(user) =>{
    const {userActions} = this.props;
    userActions.requestUserDetail(user.account,(message) =>{
      this.props.navigation.navigate('OtherUserDetail', {
        user: message
      });
    });
  }
  render() {
    return (
      <View>
        <ScrollView>
          <View style={styles.container}>
            <View style={styles.content}>
              <List >
                {
                  this.state.underUserList.map((underUser,index) =>{
                    return (
                      <List.Item  key = {index}
                        thumb={this.renderContactIcon(underUser)}
                        onClick={() => {this.openUserDetail(underUser)}}
                        arrow="horizontal"
                      >
                        {underUser.fullname}
                      </List.Item>
                    )
                  })
                }
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

export default connect(mapStateToProps, mapDispatchToProps)(MySubordinate);

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
  contactIcon:{
    paddingRight:10
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
