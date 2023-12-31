import PropTypes from 'prop-types';
import React from 'react';
import {
  Linking,
  StyleSheet,
  Text,
  View,
  ViewPropTypes,
  TouchableOpacity
} from 'react-native';

import ParsedText from 'react-native-parsed-text';
import Communications from 'react-native-communications';

export default class MessageSystem extends React.Component {
  constructor(props) {
    super(props);
  }

  onMessagePress = (content)=>{
    if(content.url){
      const { navigate } = this.props.navigation;
      const webItem = {
        title:content.title,
        url:content.url
      }
      navigate('Web', { webItem });
    }
  }

  render() {
    const c = this.props.currentMessage.content;
    const textJson = c.text?c:JSON.parse(c);
    return (
      <TouchableOpacity activeOpacity={0.5} onPress={() => this.onMessagePress(textJson)} style={[styles[this.props.position].container, this.props.containerStyle[this.props.position]]}>
        <Text
          style={[styles[this.props.position].textTitle]}
        >
          {textJson.title}
        </Text>
        <Text style={[styles[this.props.position].text, this.props.textStyle[this.props.position]]}>
          {textJson.text}
        </Text>
      </TouchableOpacity>
    );
  }
}

const textStyle = {
  fontSize: 15,
  lineHeight: 20,
  letterSpacing:3,
  marginBottom: 7,
  marginLeft: 10,
  marginRight: 10,
};

const textTitleStyle = {
  lineHeight: 25,
  marginTop: 7,
  letterSpacing:3,
  marginBottom: 7,
  marginLeft: 10,
  marginRight: 10,
};

const styles = {
  left: StyleSheet.create({
    container: {
      width:200
    },
    text: {
      color: 'black',
      ...textStyle,
    },
    textTitle:{
      fontSize:14,
      ...textTitleStyle
    },
    link: {
      color: 'black',
      textDecorationLine: 'underline',
    },
  }),
  right: StyleSheet.create({
    container: {
      width:200
    },
    text: {
      color: 'white',
      ...textStyle,
    },
    textTitle:{
      fontSize:14,
      ...textTitleStyle
    },
    link: {
      color: 'white',

      textDecorationLine: 'underline',
    },
  }),
};

MessageSystem.contextTypes = {
  actionSheet: PropTypes.func,
};

MessageSystem.defaultProps = {
  position: 'left',
  currentMessage: {
    text: '',
  },
  containerStyle: {},
  textStyle: {},
  linkStyle: {},
  parsePatterns: () => [],
};

MessageSystem.propTypes = {
  position: PropTypes.oneOf(['left', 'right']),
  currentMessage: PropTypes.object,
  containerStyle: PropTypes.shape({
    left: ViewPropTypes.style,
    right: ViewPropTypes.style,
  }),
  textStyle: PropTypes.shape({
    left: Text.propTypes.style,
    right: Text.propTypes.style,
  }),
  linkStyle: PropTypes.shape({
    left: Text.propTypes.style,
    right: Text.propTypes.style,
  }),
  parsePatterns: PropTypes.func,
  textProps: PropTypes.object,
};
