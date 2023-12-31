

import React from 'react';

import {StyleSheet, View ,Text, Platform} from 'react-native';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as messageCreators from '../actions/message';
import Icon from 'react-native-vector-icons/Ionicons';

class MessageTabBar extends React.Component {

  constructor(props) {
    super(props);
  }
  renderBadge = ()=>{
    if(!this.props.message.allUnRead || this.props.message.allUnRead < 1){
      return null
    }
    if(this.props.message.allUnRead > 99){
      return (
        <View style={styles.IconBadge}>
          <Text style={styles.IconText}>{'99+'}</Text>
        </View>
        )
    }
    return (
        <View style={styles.IconBadge}>
          <Text style={styles.IconText}>{this.props.message.allUnRead}</Text>
        </View>
      )
  }
  render() {
    const badgeContent = this.renderBadge();
    return (
      <View style={styles.MainView}>
          <Icon name="md-chatbubbles" size={23} color={this.props.tintColor} />
          {badgeContent}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  IconBadge: {
    position:'absolute',
    left:8,
    width:13,
    height:13,
    borderRadius:15,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FF0000'
  },
  IconText: {
    color:'#FFFFFF',
    fontSize:6
  },
  MainView:{
    flex:1,
    paddingTop:Platform.OS == 'ios' ? 7 :5
  }
});
const mapStateToProps = (state) => {
  const { message } = state;
  return {
    message
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  return {
    messageActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(MessageTabBar);

