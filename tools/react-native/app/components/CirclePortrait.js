

import React from 'react';
import PropTypes from 'prop-types';
import { View, StyleSheet, ListView, ViewPropTypes,Dimensions,Image,Text } from 'react-native';
const departmentIcon = require('../img/icon/icon_20.png');
const boyIcon = require('../img/icon/icon_21.png');
const girlIcon = require('../img/icon/icon_22.png');
import Lightbox from 'react-native-lightbox';
import { connect } from 'react-redux';


class CirclePortrait extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      uri:""
    };
  }

  componentDidMount() {
  }

  componentWillReceiveProps(nextProps){
    if(nextProps.uri && nextProps.uri != global.server.portal && nextProps.uri != this.state.uri){
      this.setState({
        uri:nextProps.uri
      })
    }
  }

  onError = (event) =>{
    if(this.props.defaultUri){
      this.setState({
        uri:this.props.defaultUri
      })
    }else{
      this.setState({
        uri:global.server.web+"/mobile/img/user.jpg"
      })
    }
  }
  
  renderPhoto = () =>{
    if(this.state.uri){
      if(this.props.isShowImage){
        return (
        <Lightbox
          activeProps={{
            style: styles.imageActive,
          }}
        ><Image resizeMode={'cover'} onError={this.onError} source={{uri:this.state.uri}} style={[styles.personalIconPotho,this.props.style]}></Image>
        </Lightbox>
        )
      }else{
        return (
          <Image resizeMode={'cover'} onError={this.onError} source={{uri:this.state.uri}} style={[styles.personalIconPotho,this.props.style]}></Image>
        )
      }
    }else if(this.props.title){
      return(<Text style={[styles.personalIconText,this.props.style,this.props.textStyle]}>{this.props.title.substr(this.props.title.length -2)}</Text>)
    }else{
      return <View/>
    }
  }

  render() {
    const userPhoto = this.renderPhoto();
    return (
      <View>
        {userPhoto}
      </View>
    );
  }
}


const styles = StyleSheet.create({
  group: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  personalMain:{
    padding:15,
    backgroundColor:'#fff',
    flexDirection: 'row',
    justifyContent:'center',
  },
  imageActive: {
    flex: 1,
    justifyContent:'center',
    resizeMode: 'contain',
  },
  personalIconText:{
    flex:0,
    borderRadius:35,
    fontSize:18,
    backgroundColor:'#4fabf4',
    color:'#fff',
    textAlign:'center',
    lineHeight:70,
  },
  personalIconPotho:{
    flex:0,
    borderRadius:35,
  },

  personalText:{
    flex:1,
    paddingLeft:10,
  },
  personalName:{
    flex:0,
    justifyContent:'center',
    fontSize:16,
    color:'#000000',
    height:40,
    lineHeight:40,
  },
});

const propTypes = {
  title:PropTypes.string,
  uri:PropTypes.string,
  defaultUri:PropTypes.string,
  style:PropTypes.object,
  isShowImage:PropTypes.bool,
  textStyle:PropTypes.object,
};


CirclePortrait.propTypes = propTypes;

CirclePortrait.defaultProps = {
  title:'',
  uri:'',
  style:{
    width:70,
    height:70,
  },
  isShowImage:false,
  textStyle:{
    lineHeight:70,
  },
};


const mapStateToProps = (state) => {
  return {
    
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(CirclePortrait);


