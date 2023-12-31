
import React from 'react';
import { StyleSheet, Image, Text, Linking, View,ScrollView,Picker,PickerIOS,Platform } from 'react-native';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import * as store from '../../utils/StoreUtil';
import * as contactsCreators from '../../actions/contacts';
import LoadingView from '../../components/LoadingView';
import ToastUtil from '../../utils/ToastUtil';
import * as userCreators from '../../actions/user';
import CirclePortrait from '../../components/CirclePortrait';
import RequestUtil from '../../utils/RequestUtil';
import List from 'antd-mobile-rn/lib/list';
import Grid from 'antd-mobile-rn/lib/grid';

class OrgTreeView extends React.Component {
  static navigationOptions = ({ navigation }) => ({
    title: "组织架构",
    tabBarIcon: ({ tintColor }) => (
      <Icon name="md-contacts" size={20} color={tintColor} />
    )
  });
  constructor(props) {
    super(props);
    this.state = {
      orgUserList:[],
      orgTreeList:[],
      loading:true,
      currentUserOrg:{},
      paths:[],
      pathNames:[],
      demensions:[],
      demId:""
    };
  }

  componentDidMount() {
    
    const demUrl = global.server.uc+"/api/demension/v1/dems/getAll";
    RequestUtil.requestByStoreUser(demUrl,'get').then((result) =>{
        if(result){
            this.setState({
                demensions:result
            })
            for(let i = 0 ; i < result.length; i++){
              if(result[i].isDefault == 1){
                  this.onDemChange(result[i].id);
                  break;
              }
            }
        }
    })
  }

  changeOrg = (orgId) =>{
    const url = global.server.uc+"/api/org/v1/orgs/getByParentAndDem";
    RequestUtil.requestByStoreUser(url,'post',"{\"demId\":\""+this.state.demId+"\",\"parentId\":\""+orgId+"\"}").then((result) =>{
        if(result){
            this.requestOrgDetailCallback(result);
        }
    });

    //0: {property: "org.ID_", value: "71002", group: "main", operation: "EQUAL", relation: "AND"}

    const userUrl = global.server.uc+"/api/user/v1/users/queryByType";
    //const queryBean = "{\"querys\":\"[{\"property\":\"org.ID_\", \"value\":\""+orgId+"\",\"group\":\"main\",\"operation\":\"EQUAL\",\"relation\":\"AND\"}]\"}";
    const queryBean = {querys:[{property:"orguser.org_id_", value:orgId,group:"main",operation:"EQUAL","relation":"AND"}]};
    RequestUtil.requestByStoreUser(userUrl,'post',JSON.stringify(queryBean)).then((result) =>{
        if(result.rows){
          this.setState({
            orgUserList:result.rows
          })
        } 
    });

    // //Toast.loading("加载中",0.5);
    const orgUrl = global.server.uc+"/api/org/v1/org/get?id="+orgId;
    RequestUtil.requestByStoreUser(orgUrl,'get').then((currentUserOrg) =>{
        if(currentUserOrg){
            const paths = currentUserOrg.path.split(".");
            const pathNames = currentUserOrg.pathName.split("/");
            this.setState({
                currentUserOrg:currentUserOrg,
                paths:paths,
                pathNames:pathNames,
                demId:currentUserOrg.demId
            })
        }
    })
  }

  requestOrgDetailCallback = (orgList) =>{
    this.setState({
      orgTreeList:orgList,
      loading:false
    })
  }

  onDemChange = (demId) =>{
    //Toast.loading("加载中",0.5);
    this.setState({demId:demId,currentUserOrg:""})
    const url = global.server.uc+"/api/org/v1/orgs/getByParentAndDem";
    RequestUtil.requestByStoreUser(url,'post',"{\"demId\":\""+demId+"\"}").then((result) =>{
        if(result){
            this.requestOrgDetailCallback(result);
        }
    })
  }

  openUserDetail =(user) =>{
    debugger
    const userActions = bindActionCreators(userCreators, this.props.dispatch);
    store.get('loginUser').then((loginUser) => {
      if (loginUser) {
        if(loginUser.account == user.account){
          userActions.requestUserDetail(user.account,(message) =>{
            this.props.navigation.navigate('UserDetail',{user:message});
          });
        }else{
          userActions.requestUserDetail(user.account,(message) =>{
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
          uri={user.photo} 
          style={{width:36,height:36,marginRight:10}} 
          textStyle={{fontSize:12,lineHeight:36}} />
    )
  }

  onPathNameLayout = () =>{
    try{
      this.pathNameScrollView.scrollToEnd({animated: true,duration:500})
    }catch(err){
      debugger
    }
  }

  renderContent = () =>{
    const orgUserContent = this.state.orgUserList.map((val,i) => {
      const listView = (
        <List.Item  key = {i}
          thumb={this.renderContactIcon(val)}
          onClick={() => {this.openUserDetail(val)}}
          arrow="horizontal"
        >
          <Text style={{fontSize:16}}>{val.fullname}</Text>
        </List.Item>
      );
      return listView;
    });

    const orgTreeContent = this.state.orgTreeList.map((val,i) => {
      const listView = (
        <List.Item key = {i}
          onClick={() => {this.changeOrg(val.id)}}
          arrow="horizontal"
        >
          <Text style={{fontSize:16}}>{val.name}</Text>
        </List.Item>
      );
      return listView;
    });

    const showPathNameContent = this.state.pathNames.map((val,i) => {
      if(val){
        let text = "";
        if(i < this.state.pathNames.length - 1){
            text = (
                <View key = {i} style={styles.pathNameLinkContainer}>
                   <Text onPress={() => this.changeOrg(this.state.paths[i])} style={styles.pathNameLink}>{val}</Text>
                   <Text style={styles.pathNameSplit}>{">"}</Text>
                </View>  
            );
          }else{
            text = (
               <Text key = {i} style={styles.pathNameUnLink}>{val}</Text>
            );
          }
          return text;
      }
    });
    
    const demPickerItemContent = this.state.demensions.map((val,i)=>{
        const listView = (
            <Picker.Item key={i} label={val.demName} value={val.id} />
          );
        return listView;
    });

    return (
        <View>
            { Platform.OS === 'android' ?
              <Picker
                selectedValue={this.state.demId}
                style={styles.demPicker}
                onValueChange={this.onDemChange}>
                {demPickerItemContent}
              </Picker>
              :
              <PickerIOS itemStyle={{height:80,fontSize:16}} selectedValue={this.state.demId} onValueChange={this.onDemChange}>{demPickerItemContent}</PickerIOS>
            }
            <ScrollView>
            {
            this.state.currentUserOrg ?
            <View>
            <ScrollView ref={(ref) => {this.pathNameScrollView = ref}} onContentSizeChange={this.onPathNameLayout} showsHorizontalScrollIndicator={false} horizontal={true} style={styles.pathNameContainer}>
                <View style={{flexDirection: 'row',paddingRight:15,paddingBottom:15}}>
                    <Text onPress={() => this.onDemChange(this.state.demId)} style={styles.pathNameLink}>根组织</Text>
                    <Text style={styles.pathNameSplit}>{">"}</Text>
                    {showPathNameContent}
                </View>
            </ScrollView>
            <List renderHeader={() => '组织成员'}>
              {orgUserContent}
            </List>
            </View>
            :
            <View></View>
            }
            <List renderHeader={() => '下级部门'}>
              {orgTreeContent}
            </List>
          </ScrollView>
      </View>
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
    backgroundColor: '#fff',
  },
  demPicker:{
    marginLeft:10,
    fontSize:16
  },
  pathNameContainer: {
    flexDirection: 'row',
    marginLeft:15
  },
  pathNameSplit: {
    fontSize:16,
  },
  pathNameLinkContainer: {
    flexDirection: 'row'
  },
  pathNameLink: {
    fontSize:16,
    color:'#2c90e0'
  },
  pathNameUnLink:{
    fontSize:16,
  },
  content: {
    flex: 1,
    paddingBottom: 10
  },
  orgTreeIcon:{
    paddingRight:10
  },
  contactIcon: {
    paddingRight: 10
  },
});

const mapDispatchToProps = (dispatch) => {
  return {

  };
};
export default connect(mapDispatchToProps)(OrgTreeView);
