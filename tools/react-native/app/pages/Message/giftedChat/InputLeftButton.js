import PropTypes from 'prop-types';
import React from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  ViewPropTypes,
  Image
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Emoji  from '@ardentlabs/react-native-emoji';
const iconChatKeyboard = require('../../../img/icon/icon_14.png');
const iconChatSound = require('../../../img/icon/icon_15.png');


export default class InputLeftButton extends React.Component {
  constructor(props) {
    super(props);
    this.iconChatKeyboardPress = this.iconChatKeyboardPress.bind(this);
    this.iconChatSoundPress = this.iconChatSoundPress.bind(this);
  }

  iconChatKeyboardPress(){
    if(this.props.onInputLeftButtonPress){
      this.props.onInputLeftButtonPress('keyboard');
      if(this.props.isShowAccessory){
        this.props.onToggleAccessory();
      }
    }
  }

  iconChatSoundPress(){
    if(this.props.onInputLeftButtonPress){
      this.props.onInputLeftButtonPress('sound');
    }
  }

  render() {
    return (
      <View style={styles.container}>
        {
          this.props.isKeyboardInput?
          (
            <TouchableOpacity onPress={this.iconChatKeyboardPress.bind(this)}  activeOpacity={1} accessibilityTraits="button">
              <Image style={styles.iconChatKeyboard} source={iconChatSound} />
            </TouchableOpacity>
          )
          :
          (
            <TouchableOpacity onPress={this.iconChatSoundPress.bind(this)} activeOpacity={1} accessibilityTraits="button">
              <Image style={styles.iconChatSound} source={iconChatKeyboard} />
            </TouchableOpacity>
          )
        }
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection:'row',
    marginLeft:5,
    height:44,
    alignItems:'center',
    justifyContent: 'center',
    backgroundColor:'#fff'
  },
  iconChatKeyboard: {
    height:35,
    width:35,
    backgroundColor:'#fff'
  },
  iconChatSound: {
    height:35,
    width:35,
    backgroundColor:'#fff'
  },
});

InputLeftButton.defaultProps = {
  text: '',
  onSend: () => {},
  label: 'Send',
  containerStyle: {},
  textStyle: {},
};

InputLeftButton.propTypes = {
  text: PropTypes.string,
  onSend: PropTypes.func,
  label: PropTypes.string,
  containerStyle: ViewPropTypes.style,
  textStyle: Text.propTypes.style,
};
