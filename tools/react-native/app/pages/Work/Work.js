
import React from 'react';
import { StyleSheet, Text, View,ScrollView,Image } from 'react-native';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/Ionicons';
import Grid from 'antd-mobile-rn/lib/grid';
import List from 'antd-mobile-rn/lib/list';
import * as constans from '../../constants/Constans';
import * as workCreators from '../../actions/work';

class Work extends React.Component {
  static navigationOptions = ({ navigation }) => {
    return ({
      title: '工作台',
      tabBarIcon: ({ tintColor }) => (
        <Icon name="md-briefcase" size={22} color={tintColor} />
      ),
      headerRight: (
        <Icon onPress={() => {navigation.navigate('UnionSearch', {})}} style={styles.headerRightIcon} name="ios-search" size={28} color="#fff" />
      )
    })
  };

  constructor(props) {
    super(props);
    this.state = {
      workItems:[],
      childView:false
    };
  }

componentDidMount() {
  this.props.navigation.setParams({
    headerSearchPress: this.headerSearchPress
  });
}

componentWillReceiveProps(nextProps) {
  if(nextProps.work){
    const applications = nextProps.work.applications;
    if(applications.length > 0){
      this.setState({
        workItems:applications
      })
    };
  }
}

headerSearchPress = () =>{
  this.props.navigation.navigate('UnionSearch', {});
}

onGridClick = (grid,index) =>{
    if(!grid.url){
      return false;
    }
    let url = "";
    if(constans.WORK_TYPE_FLOW == grid.workType){
      const json = JSON.parse(grid.url);
      url = global.server.web+"/mobile/view/bpm/startFlow.html?defId="+json.defId;
    }else{
      url = grid.url;
    }
    const webItem = {
      title:grid.title,
      url:url
    }
    this.props.navigation.navigate('Web', { webItem });
};

readerWordGrid = (works) =>{
  return (
    <Grid
      data={works.works}
      hasLine={false}
      onClick={this.onGridClick}
      style={styles.workGrid}
      renderItem={dataItem => (
        <View style={styles.workGridView}>
          <Image resizeMode={'contain'} style={styles.workGridImg} source={{uri:dataItem.icon}} />
          <Text numberOfLines={1} style={styles.workGridTitle}>
            {dataItem.title}
          </Text>
        </View>
      )}
    />
  )
}

render() {
  const content = this.state.workItems.map((val,i) => {
    const gridView = (
        <List.Item key={i}>
          <Text style={styles.itemName}>
              {val.name}
          </Text>
          {this.readerWordGrid(val)}
        </List.Item>
    );
    return gridView;
  });
  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <ScrollView>
          <List>
            {content}
          </List>
        </ScrollView>
      </View>
    </View>
  );
}
}

const mapStateToProps = (state) => {
  const { work } = state;
  return {
    work
  };
};

const mapDispatchToProps = (dispatch) => {
  const workActions = bindActionCreators(workCreators, dispatch);
  return {
    workActions
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Work);

const styles = StyleSheet.create({
container: {
  flex: 1,
  flexDirection: 'column',
  backgroundColor: '#f3f3f3'
},
content: {
  flex: 1,
  paddingBottom: 5,
},
itemName: {
  fontSize: 16,
  color: '#252525',
  fontWeight: '600',
  marginTop:15,
  marginLeft:5,
  marginBottom:10,
},
workGrid: {
  flex: 1,
},
workGridView: {
  flex: 1,
  alignItems: 'center',
  marginTop:10,
  marginBottom:10,
},
workGridImg: {
  width: 32,
  height: 32,
},
workGridTitle: {
  fontSize: 13,
  textAlign: 'center',
  color: '#5e5d5d',
  marginTop: 5,
  width:80,
},
headerRightIcon: {
  paddingRight: 20
}
});
