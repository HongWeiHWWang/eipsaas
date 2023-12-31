import PropTypes from 'prop-types';
import React from 'react';
import {
  Platform,
  StyleSheet,
  TextInput,
  View,
  Text,
  PixelRatio,
  TouchableOpacity,
  Animated,
  Dimensions
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {AudioRecorder, AudioUtils} from 'react-native-audio';
import Sound from 'react-native-sound'; // 播放声音组件
import * as permissionUtil from '../../../utils/PermissionUtil';
import AudioRecord from './AudioRecord';

Sound.setCategory('Playback');
let audioPath = AudioUtils.DocumentDirectoryPath + '/test.aac';

export default class Composer extends React.Component {

  constructor(props) {
     super(props);
     this.state = {
       isSoundRecording:'recording'
     };
   }

  onContentSizeChange(e) {
    const contentSize = e.nativeEvent.contentSize;
    if (!contentSize) return;
    if (!this.contentSize || this.contentSize.width !== contentSize.width || this.contentSize.height !== contentSize.height) {
      this.contentSize = contentSize;
      this.props.onInputSizeChanged(this.contentSize);
    }
  }

  onChangeText(text) {
    this.props.onTextChanged(text);
  }

  renderContent = () =>{
    if(this.props.isKeyboardInput){
      return (
        <TextInput
          placeholder={this.props.placeholder}
          placeholderTextColor={this.props.placeholderTextColor}
          multiline={this.props.multiline}

          onChange={(e) => this.onContentSizeChange(e)}
          onContentSizeChange={(e) => this.onContentSizeChange(e)}

          onChangeText={(text) => this.onChangeText(text)}

          onFocus = {this.props.onTextInputFocus}

          style={[styles.textInput, this.props.textInputStyle, { height: this.props.composerHeight }]}

          autoFocus={this.props.textInputAutoFocus}

          value={this.props.text}
          accessibilityLabel={this.props.text || this.props.placeholder}
          enablesReturnKeyAutomatically
          underlineColorAndroid="transparent"
          {...this.props.textInputProps}
        />
      )
    }else{
      return (
        <AudioRecord {...this.props}/>
      )
    }

  }

  render() {
    const content = this.renderContent();
    const {height, width} = Dimensions.get('window');

    return (
      <View style={{flex:1}}>
        {content}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  textInput: {
    flex: 1,
    marginLeft: 10,
    fontSize: 16,
    lineHeight: 16,
    marginTop: Platform.select({
      ios: 6,
      android: 0,
    }),
    marginBottom: Platform.select({
      ios: 5,
      android: 3,
    }),
  },
  recorder: {
    flex: 1,
    marginLeft: 20,
    marginRight: 20,
    marginTop: 10,
    marginBottom: 10,
    borderWidth: 1 / PixelRatio.get(),
    borderColor: '#6E7377',
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

Composer.defaultProps = {
  composerHeight: Platform.select({
    ios: 33,
    android: 41,
  }), // TODO SHARE with GiftedChat.js and tests
  text: '',
  placeholderTextColor: '#b2b2b2',
  textInputProps: null,
  multiline: true,
  textInputStyle: {},
  textInputAutoFocus: false,
  onTextChanged: () => {
  },
  onInputSizeChanged: () => {
  },
};

Composer.propTypes = {
  composerHeight: PropTypes.number,
  text: PropTypes.string,
  placeholder: PropTypes.string,
  placeholderTextColor: PropTypes.string,
  textInputProps: PropTypes.object,
  onTextChanged: PropTypes.func,
  onInputSizeChanged: PropTypes.func,
  multiline: PropTypes.bool,
  textInputStyle: TextInput.propTypes.style,
  textInputAutoFocus: PropTypes.bool,
};
