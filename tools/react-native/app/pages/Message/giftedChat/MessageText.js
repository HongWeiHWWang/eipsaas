import PropTypes from 'prop-types';
import React from 'react';
import {
  Linking,
  StyleSheet,
  Text,
  View,
  ViewPropTypes,
} from 'react-native';

import ParsedText from 'react-native-parsed-text';
import Communications from 'react-native-communications';
import * as emoji from './Emoji';
import Emoji  from '@ardentlabs/react-native-emoji';


const WWW_URL_PATTERN = /^www\./i;

export default class MessageText extends React.Component {
  constructor(props) {
    super(props);
    this.onUrlPress = this.onUrlPress.bind(this);
    this.onPhonePress = this.onPhonePress.bind(this);
    this.onEmailPress = this.onEmailPress.bind(this);
  }

  onUrlPress(url) {
    // When someone sends a message that includes a website address beginning with "www." (omitting the scheme),
    // react-native-parsed-text recognizes it as a valid url, but Linking fails to open due to the missing scheme.
    if (WWW_URL_PATTERN.test(url)) {
      this.onUrlPress(`http://${url}`);
    } else {
      Linking.canOpenURL(url).then((supported) => {
        if (!supported) {
        } else {
          Linking.openURL(url);
        }
      });
    }
  }

  onPhonePress(phone) {
    const options = [
      'Call',
      'Text',
      'Cancel',
    ];
    const cancelButtonIndex = options.length - 1;
    this.context.actionSheet().showActionSheetWithOptions({
      options,
      cancelButtonIndex,
    },
    (buttonIndex) => {
      switch (buttonIndex) {
        case 0:
          Communications.phonecall(phone, true);
          break;
        case 1:
          Communications.text(phone);
          break;
      }
    });
  }

  onEmailPress(email) {
    Communications.email([email], null, null, null, null);
  }

  stringToContentArray(text) {
    var contentArray = [];
    if(!text){
        contentArray.push({"Content" : ""});
        return contentArray;
    }
    var regex = new RegExp('\\[[a-zA-Z0-9\\/\\u4e00-\\u9fa5]+\\]', 'g');    
    var regArray = text.match(regex);
    if (regArray === null) {
        contentArray.push({"Content" : text});
        return contentArray;
    }

    var indexArray = [];
    var pos = text.indexOf(regArray[0]);//头
    for (let i = 1; i < regArray.length; i++) {
        indexArray.push(pos);
        pos = text.indexOf(regArray[i],pos + 1);
    }
    indexArray.push(pos);//尾

    for (let i=0; i<indexArray.length; i++) {
        if (indexArray[i] === 0) {//一开始就是表情
            contentArray.push({"Resources" :emoji.emojiJson[regArray[i]]?<Emoji name={emoji.emojiJson[regArray[i]].key}/>:null });
        } else {
            if (i === 0) {
                contentArray.push({"Content" : text.substr(0,indexArray[i])});
            } else {
                if (indexArray[i] - indexArray[i-1] - regArray[i-1].length > 0) {//两个表情相邻，中间不加content
                    contentArray.push({"Content" : text.substr(indexArray[i-1] + regArray[i-1].length,indexArray[i] - indexArray[i-1] - regArray[i-1].length)});
                }
            }
            contentArray.push({"Resources" : emoji.emojiJson[regArray[i]]?<Emoji name={emoji.emojiJson[regArray[i]].key}/>:null });
        }
    }

    let lastLocation = indexArray[indexArray.length - 1] + regArray[regArray.length - 1].length;
    if (text.length > lastLocation) {
        contentArray.push({"Content": text.substr(lastLocation,text.length - lastLocation)});
    }
    return contentArray;
}

  render() {
    const linkStyle = StyleSheet.flatten([styles[this.props.position].link, this.props.linkStyle[this.props.position]]);
    const c = this.props.currentMessage.content;
    const textJson = c.text?c:JSON.parse(c);
    const contentArray = this.stringToContentArray(textJson.text);
    const content = contentArray.map((value,index) =>{
              if(value.Content){
                return value.Content;
              }else if(value.Resources){
                return (<Text key={index} style={[{ fontSize: 22 },{height:30}]}>{value.Resources}</Text>)
              }
          });
    return (
      <View style={[styles[this.props.position].container, this.props.containerStyle[this.props.position]]}>
        <ParsedText
          style={[styles[this.props.position].text, this.props.textStyle[this.props.position]]}
          parse={[
            ...this.props.parsePatterns(linkStyle),
            {type: 'url', style: linkStyle, onPress: this.onUrlPress},
            {type: 'phone', style: linkStyle, onPress: this.onPhonePress},
            {type: 'email', style: linkStyle, onPress: this.onEmailPress},
          ]}
          childrenProps={{...this.props.textProps}}
        >
          {content}
        </ParsedText>
      </View>
    );
  }
}

const textStyle = {

  fontSize: 16,
  lineHeight: 26,
  marginTop: 7,
  letterSpacing:3,
  marginBottom: 7,
  marginLeft: 10,
  marginRight: 10,
};

const styles = {
  left: StyleSheet.create({
    container: {
    },
    text: {
      color: '#262626',
      ...textStyle,
    },
    link: {
      color: '#262626',
      textDecorationLine: 'underline',
    },
  }),
  right: StyleSheet.create({
    container: {
    },
    text: {
      color: 'white',
      ...textStyle,
    },
    link: {
      color: 'white',

      textDecorationLine: 'underline',
    },
  }),
};

MessageText.contextTypes = {
  actionSheet: PropTypes.func,
};

MessageText.defaultProps = {
  position: 'left',
  currentMessage: {
    text: '',
  },
  containerStyle: {},
  textStyle: {},
  linkStyle: {},
  parsePatterns: () => [],
};

MessageText.propTypes = {
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
