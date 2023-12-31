import PropTypes from 'prop-types';
import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  ViewPropTypes,
} from 'react-native';

import moment from 'moment';

import { isSameDay, isSameUser, warnDeprecated } from './utils';

export default class Day extends React.Component {
  render() {
    const { dateFormat } = this.props;
    if (!isSameDay(this.props.currentMessage, this.props.previousMessage)) {
      return (
        <View style={[styles.container]}>
            <Text style={[styles.text, this.props.textStyle]}>
              {moment(this.props.currentMessage.sendTime).locale(this.context.getLocale()).format(dateFormat).toUpperCase()}
            </Text>
        </View>
      );
    }
    return null;
  }
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 5,
    marginBottom: 10,
  },
  text: {
    backgroundColor: 'transparent',
    color: '#b2b2b2',
    fontSize: 12,
    fontWeight: '600',
  },
});

Day.contextTypes = {
  getLocale: PropTypes.func,
};

Day.defaultProps = {
  currentMessage: {
    // TODO test if crash when createdAt === null
    sendTime: null,
  },
  previousMessage: {},
  containerStyle: {},
  wrapperStyle: {},
  textStyle: {},
  //TODO: remove in next major release
  isSameDay: warnDeprecated(isSameDay),
  isSameUser: warnDeprecated(isSameUser),
};

Day.propTypes = {
  currentMessage: PropTypes.object,
  previousMessage: PropTypes.object,
  containerStyle: ViewPropTypes.style,
  wrapperStyle: ViewPropTypes.style,
  textStyle: Text.propTypes.style,
  //TODO: remove in next major release
  isSameDay: PropTypes.func,
  isSameUser: PropTypes.func,
  dateFormat: PropTypes.string,
};
