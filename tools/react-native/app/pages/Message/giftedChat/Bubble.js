import PropTypes from 'prop-types';
import React from 'react';
import {
  Text,
  Clipboard,
  StyleSheet,
  TouchableOpacity,
  View,
  ViewPropTypes,
} from 'react-native';

import MessageText from './MessageText';
import MessageImage from './MessageImage';
import MessageAudio from './MessageAudio';
import MessageVideo from './MessageVideo';
import MessageFile from './MessageFile';
import MessageSystem from './MessageSystem';
import Time from './Time';
import { MESSAGE_TYPE_TEXT,
  MESSAGE_TYPE_AUDIO,
  MESSAGE_TYPE_IMAGE,
  MESSAGE_TYPE_VIDEO,
  MESSAGE_TYPE_SYSTEM,
  MESSAGE_TYPE_FILE,
  } from '../../../constants/Constans';


import { isSameUser, isSameDay, warnDeprecated } from './utils';

export default class Bubble extends React.Component {
  constructor(props) {
    super(props);
    this.onLongPress = this.onLongPress.bind(this);
  }



  renderMessageText() {
    if (this.props.currentMessage) {
      const {containerStyle, wrapperStyle, ...messageTextProps} = this.props;
      return <MessageText {...messageTextProps}/>;
    }
    return null;
  }

  renderMessageAudio() {
    if (this.props.currentMessage) {
      const {containerStyle, wrapperStyle, ...messageAudioProps} = this.props;
      return <MessageAudio {...messageAudioProps}/>;
    }
    return null;
  }

  renderMessageImage() {
    if (this.props.currentMessage) {
      const {containerStyle, wrapperStyle, ...messageImageProps} = this.props;
      return <MessageImage {...messageImageProps}/>;
    }
    return null;
  }

  renderMessageVideo() {
    if (this.props.currentMessage) {
      const {containerStyle, wrapperStyle, ...messageVideoProps} = this.props;
      return <MessageVideo {...messageVideoProps}/>;
    }
    return null;
  }

  renderMessageFile() {
    if (this.props.currentMessage) {
      const {containerStyle, wrapperStyle, ...messageFileProps} = this.props;
      return <MessageFile {...messageFileProps}/>;
    }
    return null;
  }
  
  renderMessageSystem() {
    if (this.props.currentMessage) {
      const {containerStyle, wrapperStyle, ...messageSystemProps} = this.props;
      return <MessageSystem {...messageSystemProps}/>;
    }
    return null;
  }

  renderMessage(){
    switch (this.props.currentMessage.type) {
      case MESSAGE_TYPE_TEXT:
        return this.renderMessageText();
        break;
      case MESSAGE_TYPE_AUDIO:
        return this.renderMessageAudio();
        break;
      case MESSAGE_TYPE_IMAGE:
        return this.renderMessageImage();
        break;
      case MESSAGE_TYPE_VIDEO:
        return this.renderMessageVideo();
        break;
      case MESSAGE_TYPE_SYSTEM:
        return this.renderMessageSystem();
        break;
      case MESSAGE_TYPE_FILE:
        return this.renderMessageFile();
        break;
        
      default:
    }
  }

  renderTicks() {
    return null
    // return (
    //   <Text style={[styles.tick, this.props.tickStyle]}>✓</Text>
    // )
  }

  renderTime() {
    if (this.props.currentMessage.sendTime) {
      const {containerStyle, wrapperStyle, ...timeProps} = this.props;
      return <Time {...timeProps}/>;
    }
    return null;
  }

  renderCustomView() {
    if (this.props.renderCustomView) {
      return this.props.renderCustomView(this.props);
    }
    return null;
  }

  onLongPress() {
    if (this.props.currentMessage.type == MESSAGE_TYPE_TEXT) {
      const options = [
        '复制',
        '取消'
      ];
      const cancelButtonIndex = options.length - 1;
      this.context.actionSheet().showActionSheetWithOptions({
        options,
        cancelButtonIndex,
      },
      (buttonIndex) => {
        switch (buttonIndex) {
          case 0:
            Clipboard.setString(JSON.parse(this.props.currentMessage.content).text);
            break;
        }
      });
    }
  }

  render() {
    return (
      <View style={[styles[this.props.position].container]}>
        <View style={[styles[this.props.position].wrapper]}>
          <TouchableOpacity
            activeOpacity={0.5}
            onLongPress={this.onLongPress}
            accessibilityTraits="text"
            {...this.props.touchableProps}
          >
            <View>
              {this.renderCustomView()}
              {this.renderMessage()}
              <View style={[styles.bottom, this.props.bottomContainerStyle[this.props.position]]}>
                {
                  //this.renderTime()
                }
                {this.renderTicks()}
              </View>
            </View>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

const styles = {
  left: StyleSheet.create({
    container: {
      flex: 1,
      alignItems: 'flex-start',
    },
    wrapper: {
      borderRadius:8,
      backgroundColor: '#FFFFFF',
      marginRight: 60,
      minHeight: 20,
      overflow:'hidden',
      justifyContent: 'flex-end',
    },
  }),
  right: StyleSheet.create({
    container: {
      flex: 1,
      alignItems: 'flex-end',
    },
    wrapper: {
      borderRadius:8,
      backgroundColor: '#4fabf4',
      marginLeft: 60,
      minHeight: 20,
      overflow:'hidden',
      justifyContent: 'flex-end',
    },
  }),
  bottom: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
  },
  tick: {
    fontSize: 10,
    backgroundColor: 'transparent',
    color: 'white',
  },
  tickView: {
    flexDirection: 'row',
    marginRight: 10,
  }
};

Bubble.contextTypes = {
  actionSheet: PropTypes.func,
};

Bubble.defaultProps = {
  touchableProps: {},
  onLongPress: null,
  renderMessageImage: null,
  renderMessageText: null,
  renderCustomView: null,
  renderTime: null,
  position: 'left',
  currentMessage: {
    text: null,
    sendTime: null,
    image: null,
  },
  nextMessage: {},
  previousMessage: {},
  containerStyle: {},
  wrapperStyle: {},
  bottomContainerStyle: {},
  tickStyle: {},
  //TODO: remove in next major release
  isSameDay: warnDeprecated(isSameDay),
  isSameUser: warnDeprecated(isSameUser),
};

Bubble.propTypes = {
  touchableProps: PropTypes.object,
  onLongPress: PropTypes.func,
  renderMessageImage: PropTypes.func,
  renderMessageText: PropTypes.func,
  renderCustomView: PropTypes.func,
  renderTime: PropTypes.func,
  position: PropTypes.oneOf(['left', 'right']),
  currentMessage: PropTypes.object,
  nextMessage: PropTypes.object,
  previousMessage: PropTypes.object,
  containerStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
  wrapperStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
  bottomContainerStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
  tickStyle: Text.propTypes.style,
  //TODO: remove in next major release
  isSameDay: PropTypes.func,
  isSameUser: PropTypes.func,
};
