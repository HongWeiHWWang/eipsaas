import PropTypes from 'prop-types';
import React from 'react';
import {
  StyleSheet,
  View,
  Text,
  Keyboard,
  ViewPropTypes,
  Dimensions,
  ScrollView,
  Image,
  TouchableOpacity
} from 'react-native';
import Composer from './Composer';
import MoreAccessory from './MoreAccessory';

import InputRigthButton from './InputRigthButton';
import InputLeftButton from './InputLeftButton';
import Emoji  from '@ardentlabs/react-native-emoji';
import GridView from '../../../components/GridView';
import * as emoji from './Emoji';


export default class InputToolbar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      position: 'absolute',
      isShowAccessory:this.props.isShowAccessory,
      isKeyboardInput:true,
      accessoryType:'',
      accessory:null
    };
  }

  componentWillMount () {
    this.keyboardWillShowListener =
      Keyboard.addListener('keyboardWillShow', this._keyboardWillShow);
    this.keyboardWillHideListener =
      Keyboard.addListener('keyboardWillHide', this._keyboardWillHide);
  }

  componentWillUnmount () {
    this.keyboardWillShowListener.remove();
    this.keyboardWillHideListener.remove();
  }

  _keyboardWillShow = () => {
    this.setState({
      position: 'relative'
    });
  }

  _keyboardWillHide = () => {
    this.setState({
      position: 'absolute'
    });
  }

  onInputLeftButtonPress = (type) =>{
    this.setState({
      isKeyboardInput:!this.state.isKeyboardInput
    })
  }

  onInputRightButtonPress = (type) =>{
    if(this.state.accessoryType == type){
      this.props.onToggleAccessory();
    }else{
      this.setState({
        accessoryType:type
      })
      if(!this.props.isShowAccessory){
        this.props.onToggleAccessory();
      }
    }
  }

  renderRigthButton() {
    return <InputRigthButton onInputRightButtonPress={this.onInputRightButtonPress} {...this.props}/>;
  }

  renderLeftButton() {
    return <InputLeftButton {...this.props} isKeyboardInput = {this.state.isKeyboardInput} onInputLeftButtonPress = {this.onInputLeftButtonPress} />;
  }

  renderComposer() {
    return (
      <Composer
        {...this.props}
        isKeyboardInput = {this.state.isKeyboardInput}
      />
    );
  }

  onEmojiClick = (emoji) =>{
    this.props.setInputText(this.props.text+'['+emoji.name+']')
  }

  renderAccessory = () => {
    return(
      this.props.isShowAccessory
      ?
      this.state.accessoryType == 'emoji' ?
      <View style={[styles.accessoryEmojiPrimary]}>
        <GridView
          items={Object.values(emoji.emojiJson)}
          itemsPerRow={7}
          renderItem={(dataItem,index) =>(
            <Text key={index} onPress={() => this.onEmojiClick(dataItem)} style={styles.emojiText}><Emoji name={dataItem.key}/></Text>
          )}
        />
      </View>
      :

      <MoreAccessory {...this.props}/>
      :
      <View/>
    )
  }

  render() {
    return (
      <View
        style={[styles.container, this.props.containerStyle, { position: this.state.position }]}>
        <View style={[styles.primary, this.props.primaryStyle]}>
          {this.renderLeftButton()}
          {this.renderComposer()}
          {this.renderRigthButton()}
        </View>
        {this.renderAccessory()}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    borderTopWidth: StyleSheet.hairlineWidth,
    borderTopColor: '#b2b2b2',
    backgroundColor: '#FFFFFF',
    bottom: 0,
    width: Dimensions.get('window').width
  },
  primary: {
    flexDirection: 'row',
    alignItems: 'flex-end',
  },
  accessory: {
    height: 44,
  },
  emojiText: {
    textAlign:'center',
    marginTop:10,
    marginBottom:10,
    fontSize:22,
    backgroundColor: 'transparent',
    color: '#000',
    fontWeight: '600',
  },
  accessoryEmojiPrimary:{
    flex: 1,
    alignItems: 'center',
    height:200,
    maxWidth:Dimensions.get('window').width
  },
  accessoryMorePrimary:{
    flex: 1,
    alignItems: 'center',
    height:'auto'
  },
  moreItemContainer:{
    alignItems: 'center',
    margin:5
  },
  moreItemIcon:{
    height:80,
    width:80
  },
  moreItemTitle:{
    marginBottom:5,
    fontSize:12,
    color: '#8a8a8a',
  }
});

InputToolbar.defaultProps = {
  renderAccessory: null,
  renderComposer: null,
  containerStyle: {},
  primaryStyle: {},
  accessoryStyle: {}
};

InputToolbar.propTypes = {
  renderAccessory: PropTypes.func,
  renderComposer: PropTypes.func,
  containerStyle: ViewPropTypes.style,
  primaryStyle: ViewPropTypes.style,
  accessoryStyle: ViewPropTypes.style
};
