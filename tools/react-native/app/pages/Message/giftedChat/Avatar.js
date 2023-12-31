import PropTypes from 'prop-types';
import React from "react";
import {Image, StyleSheet, View, ViewPropTypes} from "react-native";
import GiftedAvatar from "./GiftedAvatar";
import {isSameUser, isSameDay, warnDeprecated} from "./utils";
import Icon from 'react-native-vector-icons/Ionicons';
import CirclePortrait from '../../../components/CirclePortrait';

export default class Avatar extends React.Component {
  renderAvatar() {
    const user = {}
    const messageUser = this.props.sessionUsers[this.props.currentMessage.from];
    user.name = messageUser.userAlias;
    user.avatar = messageUser.photo?<CirclePortrait uri={global.server.portal+messageUser.photo} style={{width:40,height:40}}/>:<CirclePortrait title={user.name } style={{width:40,height:40,}} textStyle={{lineHeight:40,fontSize:14}}/>;


    return (
      <GiftedAvatar
        avatarStyle={StyleSheet.flatten([styles[this.props.position].image, this.props.imageStyle[this.props.position]])}
        user={user}
        onPress={() => this.props.onPressAvatar && this.props.onPressAvatar(this.props.currentMessage.from)}
      />
    );
  }

  render() {
    const renderAvatarOnTop = this.props.renderAvatarOnTop;
    const messageToCompare = renderAvatarOnTop ? this.props.previousMessage : this.props.nextMessage;
    const computedStyle = renderAvatarOnTop ? "onTop" : "onBottom"

    if (this.props.renderAvatar === null) {
      return null
    }

    return (
      <View
        style={[styles[this.props.position].container, styles[this.props.position][computedStyle], this.props.containerStyle[this.props.position]]}>
        {this.renderAvatar()}
      </View>
    );
  }
}

const styles = {
  left: StyleSheet.create({
    container: {
      marginRight: 8
    },
    onTop: {
      alignSelf: "flex-start"
    },
    onBottom: {},
    image: {
      height: 36,
      width: 36,
      borderRadius: 18,
    },
  }),
  right: StyleSheet.create({
    container: {
      marginLeft: 8,
    },
    onTop: {
      alignSelf: "flex-start"
    },
    onBottom: {},
    image: {
      height: 36,
      width: 36,
      borderRadius: 18,
    },
  }),
};

Avatar.defaultProps = {
  renderAvatarOnTop: false,
  position: 'left',
  currentMessage: {
    user: null,
  },
  nextMessage: {},
  containerStyle: {},
  imageStyle: {},
  //TODO: remove in next major release
  isSameDay: warnDeprecated(isSameDay),
  isSameUser: warnDeprecated(isSameUser)
};

Avatar.propTypes = {
  renderAvatarOnTop: PropTypes.bool,
  position: PropTypes.oneOf(['left', 'right']),
  currentMessage: PropTypes.object,
  nextMessage: PropTypes.object,
  onPressAvatar: PropTypes.func,
  containerStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
  imageStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
  //TODO: remove in next major release
  isSameDay: PropTypes.func,
  isSameUser: PropTypes.func
};
