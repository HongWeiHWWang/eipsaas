import PropTypes from 'prop-types';
import React from 'react';
import {
  View,
  ViewPropTypes,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
  Text
} from 'react-native';

import Avatar from './Avatar';
import Bubble from './Bubble';
import SystemMessage from './SystemMessage';
import Day from './Day';

import {isSameUser, isSameDay} from './utils';

export default class Message extends React.Component {

  constructor(props) {
    super(props);
    this.onMessageContainerClick = this.onMessageContainerClick.bind(this);
  }

  getInnerComponentProps() {
    const {containerStyle, ...props} = this.props;
    return {
      ...props,
      isSameUser,
      isSameDay
    }
  }

  renderDay() {
    const dayProps = this.getInnerComponentProps();
    return <Day {...dayProps}/>;
  }

  renderBubble() {
    const bubbleProps = this.getInnerComponentProps();
    if (this.props.renderBubble) {
      return this.props.renderBubble(bubbleProps);
    }
    return <Bubble {...bubbleProps}/>;
  }

  onMessageContainerClick(props){
    if(this.props.onMessageContainerClick){
      this.props.onMessageContainerClick();
    }
  }

  renderSystemMessage() {
    const systemMessageProps = this.getInnerComponentProps();
    if (this.props.renderSystemMessage) {
      return this.props.renderSystemMessage(systemMessageProps);
    }
    return <SystemMessage {...systemMessageProps} />;
  }

  renderAvatar() {
    if (this.props.user._id === this.props.currentMessage.from && !this.props.showUserAvatar) {
      return null;
    }
    const avatarProps = this.getInnerComponentProps();
    const { currentMessage } = avatarProps;
    return <Avatar {...avatarProps} />;
  }

  render() {
    return (
      <TouchableOpacity activeOpacity={1} onPress={() => this.onMessageContainerClick()} style={styles.common.container} >
        {this.renderDay()}
        {this.props.currentMessage.system ?
          this.renderSystemMessage() :
          <View style={[styles[this.props.position].container, this.props.containerStyle[this.props.position]]}>
            {this.props.position === "left" ? this.renderAvatar() : null}
            {this.renderBubble()}
            {this.props.position === "right" ? this.renderAvatar() : null}
          </View>}
      </TouchableOpacity>
    );
  }
}

const styles = {
  common: StyleSheet.create({
    container: {
      width:Dimensions.get('window').width
    },
  }),
  left: StyleSheet.create({
    container: {
      flexDirection: 'row',
      alignItems: 'flex-end',
      justifyContent: 'flex-start',
      marginLeft: 8,
      marginRight: 0,
      marginBottom:10
    },
  }),
  right: StyleSheet.create({
    container: {
      flexDirection: 'row',
      alignItems: 'flex-end',
      justifyContent: 'flex-end',
      marginLeft: 0,
      marginRight: 8,
      marginBottom:10
    },
  }),
};

Message.defaultProps = {
  renderAvatar: undefined,
  renderBubble: null,
  renderDay: null,
  renderSystemMessage: null,
  position: 'left',
  currentMessage: {},
  nextMessage: {},
  previousMessage: {},
  user: {},
  containerStyle: {},
};

Message.propTypes = {
  renderAvatar: PropTypes.func,
  showUserAvatar: PropTypes.bool,
  renderBubble: PropTypes.func,
  renderDay: PropTypes.func,
  renderSystemMessage: PropTypes.func,
  position: PropTypes.oneOf(['left', 'right']),
  currentMessage: PropTypes.object,
  nextMessage: PropTypes.object,
  previousMessage: PropTypes.object,
  user: PropTypes.object,
  containerStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
};
