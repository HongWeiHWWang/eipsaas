import PropTypes from 'prop-types';
import React from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  ViewPropTypes,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Emoji  from '@ardentlabs/react-native-emoji';
import { MESSAGE_TYPE_TEXT,MESSAGE_STATUS_FIRST,MESSAGE_STATUS_UPLOADDONE,MESSAGE_STATUS_UPLOADERROR} from '../../../constants/Constans';

export default class InputRigthButton extends React.Component {

  constructor(props) {
    super(props);
  }

  onSend = () =>{
    if(this.props.text.trim()){
      const message = {
        messageId: this.props.messageIdGenerator(),
        type:MESSAGE_TYPE_TEXT,
        content:JSON.stringify({text:this.props.text.trim()})
      }
      message.status = MESSAGE_STATUS_UPLOADDONE;
      this.props.onAppendMessages(message, true);
      this.props.onSend(message);
    }
    if(this.props.isShowAccessory){
      this.props.onToggleAccessory();
    }
  }

  onEmojiButtonPress = () =>{
    if(this.props.onInputRightButtonPress){
      this.props.onInputRightButtonPress('emoji')
    }
  }

  onMoreButtonPress = () =>{
    if(this.props.onInputRightButtonPress){
      this.props.onInputRightButtonPress('more')
    }
  }

  renderSendButton = () =>{
    // return (
    //   <Text onPress={this.onSend.bind(this)} style={[styles.inputSendButton]}>发送</Text>
    // )
    if(this.props.text.trim().length > 0){
      return (
        <Text onPress={this.onSend} style={[styles.inputSendButton]}>发送</Text>
      )
    }else {
      return (
        <Icon onPress={this.onMoreButtonPress} style={[styles.inputMoreButton]} name="ios-add-circle-outline" />
      )
    }
  }

  render() {
    const renderSendButton = this.renderSendButton();
    return (
      <View style={styles.container}>
        <TouchableOpacity accessibilityTraits="button">
          <Text style={[styles.inputEmojiButton]} onPress={this.onEmojiButtonPress.bind(this)}><Emoji name="smile"/></Text>
        </TouchableOpacity>
        <TouchableOpacity activeOpacity={1} accessibilityTraits="button">
          {renderSendButton}
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection:'row',
    marginRight:5,
    height:47,
    alignItems:'center',
    justifyContent: 'center',
  },
  inputEmojiButton: {
    color: '#000',
    fontWeight: '600',
    fontSize: 27,
    backgroundColor: 'transparent',
    width:40,
    textAlign: 'center',
  },
  inputMoreButton: {
    color: '#8a8a8a',
    fontWeight: '600',
    fontSize: 34,
    backgroundColor: 'transparent',
    width:40,
    textAlign: 'center',
  },
  inputSendButton: {
    fontWeight: '600',
    fontSize: 19,
    backgroundColor: 'transparent',
    width:40,
    textAlign: 'center',
  },
});

InputRigthButton.defaultProps = {
  text: '',
  onSend: () => {},
  label: 'Send',
  containerStyle: {},
  textStyle: {},
};

InputRigthButton.propTypes = {
  text: PropTypes.string,
  onSend: PropTypes.func,
  label: PropTypes.string,
  containerStyle: ViewPropTypes.style,
  textStyle: Text.propTypes.style,
};
