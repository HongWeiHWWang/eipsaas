

import React from 'react';
import {StyleSheet, TextInput, View,Dimensions,Image } from 'react-native';
import { SearchBar } from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

class Search extends React.Component {
  render() {
    return (
      <View style={styles.inputContnaier}>
        <View style={styles.searchContnaier}>
          <Icon style={styles.searchIcon} name="ios-search" size={26} color="#ccc" />
          <TextInput {...this.props} style={{height:38,flex:1,justifyContent:'center',textAlign:'left',fontSize:16,color:'#262626',}} underlineColorAndroid='transparent' />
        </View>
      </View>
    );
  }
}


const styles = StyleSheet.create({
  inputContnaier: {
    backgroundColor:'#ffffff',
    paddingLeft:15,
    paddingRight:15,
    paddingTop:10,
    paddingBottom:10,
  },
  searchContnaier: {
    height:38,
    backgroundColor:'#e9e9e9',
    borderRadius:4,
    flexDirection: 'row',
    justifyContent:'center',
    alignItems:'flex-start',
  },
  searchIcon:{
    height:38,
    lineHeight:38,
    paddingLeft:10,
    textAlign:'left',
    flexDirection: 'row',
    justifyContent:'center',
    alignItems:'flex-start',
  },
});

export default Search;
