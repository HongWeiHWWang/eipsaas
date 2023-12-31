import PropTypes from 'prop-types';
import React from 'react';

import {
  ListView,
  View,
  StyleSheet,
  TouchableOpacity
} from 'react-native';

import shallowequal from 'shallowequal';
import InvertibleScrollView from 'react-native-invertible-scroll-view';
import md5 from 'md5';
import Message from './Message';

export default class MessageContainer extends React.Component {
  constructor(props) {
    super(props);

    this.renderRow = this.renderRow.bind(this);
    this.renderFooter = this.renderFooter.bind(this);
    this.renderScrollComponent = this.renderScrollComponent.bind(this);
    const dataSource = new ListView.DataSource({
      rowHasChanged: (r1, r2) => {
        return r1.hash !== r2.hash;
      }
    });

    const messagesData = this.prepareMessages(props.messages);
    this.state = {
      dataSource: dataSource.cloneWithRows(messagesData.blob, messagesData.keys)
    };
  }

  prepareMessages(messages) {
    return {
      keys: messages.map(m => m.messageId),
      blob: messages.reduce((o, m, i) => {
        const previousMessage = messages[i + 1] || {};
        const nextMessage = messages[i - 1] || {};
        // add next and previous messages to hash to ensure updates
        const toHash = JSON.stringify(m) + previousMessage.messageId + nextMessage.messageId;
        o[m.messageId] = {
          ...m,
          previousMessage,
          nextMessage,
          hash: md5(toHash)
        };
        return o;
      }, {})
    };
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (!shallowequal(this.props, nextProps)) {
      return true;
    }
    if (!shallowequal(this.state, nextState)) {
      return true;
    }
    return false;
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.messages === nextProps.messages) {
      return;
    }
    try {
      const messagesData = this.prepareMessages(nextProps.messages);
      this.setState({
        dataSource: this.state.dataSource.cloneWithRows(messagesData.blob, messagesData.keys)
      });
    } catch (e) {

    } finally {

    }
  }

  renderFooter() {
    if (this.props.renderFooter) {
      const footerProps = {
        ...this.props,
      };
      return this.props.renderFooter(footerProps);
    }
    return null;
  }

  scrollTo(options) {
    this._invertibleScrollViewRef.scrollTo(options);
  }

  renderRow(message, sectionId, rowId) {

    if (!message.from) {
      if (!message.system) {
      }
      message.user = {};
    }

    const messageProps = {
      ...this.props,
      key: message.messageId,
      currentMessage: message,
      previousMessage: message.previousMessage,
      nextMessage: message.nextMessage,
      position: message.from === this.props.user._id ? 'right' : 'left',
    };
    return <Message {...messageProps}/>;
  }

  renderScrollComponent(props) {
    const invertibleScrollViewProps = this.props.invertibleScrollViewProps;
    return (
        <InvertibleScrollView
          {...props}
          {...invertibleScrollViewProps}
          ref={component => this._invertibleScrollViewRef = component}
        />
    );
  }


  render() {
    return (
          <ListView

            enableEmptySections={true}
            automaticallyAdjustContentInsets={false}
            initialListSize={20}
            pageSize={20}

            {...this.props.listViewProps}

            dataSource={this.state.dataSource}

            renderRow={this.renderRow}
            renderHeader={this.renderFooter}
            renderScrollComponent={this.renderScrollComponent}
          />
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  }
});

MessageContainer.defaultProps = {
  messages: [],
  user: {},
  renderFooter: null,
  renderMessage: null
};

MessageContainer.propTypes = {
  messages: PropTypes.array,
  user: PropTypes.object,
  renderFooter: PropTypes.func,
  renderMessage: PropTypes.func,
  listViewProps: PropTypes.object,
};
