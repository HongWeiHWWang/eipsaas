

import React from 'react';

import {StyleSheet, View ,Text,Platform,I18nManager,TouchableOpacity,Image} from 'react-native';
import { connect } from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';


class CustomHeader extends React.Component {
  
  constructor(props) {
    super(props);
    console.warn(props)
  }

  toSearch = () =>{
    console.warn(this.props)
  }

  render() {
    return (
      <Icon onPress={() => {this.toSearch()}} name="ios-search" size={28} color="#fff" />
    );
  }
}

const styles = StyleSheet.create({
  
});
const mapStateToProps = (state) => {
  return {
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(CustomHeader);

