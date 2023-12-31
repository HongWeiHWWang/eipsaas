import PropTypes from 'prop-types';
import React from 'react';
import {
  Animated,
  Platform,
  StyleSheet,
  View,
  Keyboard,
  Text,
  Image,
  TouchableOpacity,
  KeyboardAvoidingView
} from 'react-native';

import ActionSheet from '@expo/react-native-action-sheet';
import moment from 'moment';
import uuid from 'uuid';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import * as utils from './utils';
import Avatar from './Avatar';
import Bubble from './Bubble';
import SystemMessage from "./SystemMessage";;
import MessageImage from './MessageImage';
import MessageText from './MessageText';
import Composer from './Composer';
import Day from './Day';
import InputToolbar from './InputToolbar';
import Message from './Message';
import MessageContainer from './MessageContainer';
import Time from './Time';
import GiftedAvatar from './GiftedAvatar';
import GiftedChatInteractionManager from './GiftedChatInteractionManager';
import Toast from '../../../components/Toast'
import InputRigthButton from './InputRigthButton';

const audioRecording = require('../../../img/audio_recording.gif');
const recordCancel = require('../../../img/cancel.png');

const MIN_COMPOSER_HEIGHT = Platform.select({
  ios: 33,
  android: 41,
});
const MAX_COMPOSER_HEIGHT = 100;

class GiftedChat extends React.Component {
  constructor(props) {
    super(props);

    // default values
    this._isMounted = false;
    this._keyboardHeight = 0;
    this._bottomOffset = 0;
    this._maxHeight = null;
    this._isFirstLayout = true;
    this._locale = 'en';
    this._messages = [];

    this.state = {
      isInitialized: false, // initialization will calculate maxHeight before rendering the chat
      composerHeight: MIN_COMPOSER_HEIGHT,
      messagesContainerHeight: null,
      typingDisabled: false,
      isShowAccessory:false,
      audioToastColor:'white',
      isAudioToastShow:false,
      audioToastImage:null,
      audioToastText:'',
      audioRecordTime:0,
      isKeyboardShow:false
    };

    this.onKeyboardWillShow = this.onKeyboardWillShow.bind(this);
    this.onKeyboardWillHide = this.onKeyboardWillHide.bind(this);
    this.onKeyboardDidShow = this.onKeyboardDidShow.bind(this);
    this.onKeyboardDidHide = this.onKeyboardDidHide.bind(this);
    this.onAppendMessages = this.onAppendMessages.bind(this);
    this.getLocale = this.getLocale.bind(this);
    this.onInputSizeChanged = this.onInputSizeChanged.bind(this);
    this.onInputTextChanged = this.onInputTextChanged.bind(this);
    this.onMainViewLayout = this.onMainViewLayout.bind(this);
    this.onInitialLayoutViewLayout = this.onInitialLayoutViewLayout.bind(this);

    this.setInputText = this.setInputText.bind(this);
    this.onTextInputFocus = this.onTextInputFocus.bind(this);
    this.onMessageContainerClick = this.onMessageContainerClick.bind(this);
    this.onToggleAccessory = this.onToggleAccessory.bind(this);
    this.onAudioRecord = this.onAudioRecord.bind(this);

    this.invertibleScrollViewProps = {
      inverted: true,
      keyboardShouldPersistTaps: this.props.keyboardShouldPersistTaps,
      onKeyboardWillShow: this.onKeyboardWillShow,
      onKeyboardWillHide: this.onKeyboardWillHide,
      onKeyboardDidShow: this.onKeyboardDidShow,
      onKeyboardDidHide: this.onKeyboardDidHide,
    };
  }

  static append(currentMessages = [], messages) {
    if (!Array.isArray(messages)) {
      messages = [messages];
    }
    return messages.concat(currentMessages);
  }

  static prepend(currentMessages = [], messages) {
    if (!Array.isArray(messages)) {
      messages = [messages];
    }
    return currentMessages.concat(messages);
  }

  getChildContext() {
    return {
      actionSheet: () => this._actionSheetRef,
      getLocale: this.getLocale,
    };
  }

  componentWillMount() {
    const { messages, text } = this.props;
    this.setIsMounted(true);
    this.initLocale();
    this.setMessages(messages || []);
    this.setTextFromProp(text);
  }

  componentWillUnmount() {
    this.setIsMounted(false);
  }

  componentWillReceiveProps(nextProps = {}) {
    const { messages, text } = nextProps;
    this.setMessages(messages || []);
    this.setTextFromProp(text);
  }

  initLocale() {
    if (this.props.locale === null || moment.locales().indexOf(this.props.locale) === -1) {
      this.setLocale('en');
    } else {
      this.setLocale(this.props.locale);
    }
  }

  setLocale(locale) {
    this._locale = locale;
  }

  getLocale() {
    return this._locale;
  }

  setTextFromProp(textProp) {
    // Text prop takes precedence over state.
    if (textProp !== undefined && textProp !== this.state.text) {
      this.setState({ text: textProp });
    }
  }

  getTextFromProp(fallback) {
    if (this.props.text === undefined) {
      return fallback;
    }
    return this.props.text;
  }

  setMessages(messages) {
    this._messages = messages;
  }

  getMessages() {
    return this._messages;
  }

  setMaxHeight(height) {
    this._maxHeight = height;
  }

  getMaxHeight() {
    return this._maxHeight;
  }

  setKeyboardHeight(height) {
    this._keyboardHeight = height;
  }

  getKeyboardHeight() {
    if (Platform.OS === 'android' && !this.props.forceGetKeyboardHeight) {
      // For android: on-screen keyboard resized main container and has own height.
      // @see https://developer.android.com/training/keyboard-input/visibility.html
      // So for calculate the messages container height ignore keyboard height.
      return 0;
    } else {
      return this._keyboardHeight;
    }
  }

  setBottomOffset(value) {
    this._bottomOffset = value;
  }

  getBottomOffset() {
    return this._bottomOffset;
  }

  setIsFirstLayout(value) {
    this._isFirstLayout = value;
  }

  getIsFirstLayout() {
    return this._isFirstLayout;
  }

  setIsTypingDisabled(value) {
    this.setState({
      typingDisabled: value
    });
  }

  getIsTypingDisabled() {
    return this.state.typingDisabled;
  }

  setIsMounted(value) {
    this._isMounted = value;
  }

  getIsMounted() {
    return this._isMounted;
  }


  // TODO
  // setMinInputToolbarHeight
  getMinInputToolbarHeight() {
    return this.props.renderAccessory ? this.props.minInputToolbarHeight * 2 : this.props.minInputToolbarHeight;
  }

  calculateInputToolbarHeight(composerHeight) {
    return composerHeight + (this.getMinInputToolbarHeight() - MIN_COMPOSER_HEIGHT);
  }

  /**
   * Returns the height, based on current window size, without taking the keyboard into account.
   */
  getBasicMessagesContainerHeight(composerHeight = this.state.composerHeight) {
    return this.getMaxHeight() - this.calculateInputToolbarHeight(composerHeight);
  }

  /**
   * Returns the height, based on current window size, taking the keyboard into account.
   */
  getMessagesContainerHeightWithKeyboard(composerHeight = this.state.composerHeight) {
    return this.getBasicMessagesContainerHeight(composerHeight) - this.getKeyboardHeight() + this.getBottomOffset();
  }

  prepareMessagesContainerHeight(value) {
    if (this.props.isAnimated === true) {
      return new Animated.Value(value);
    }
    return value;
  }

  onKeyboardWillShow(e) {
    this.setIsTypingDisabled(true);
    this.setKeyboardHeight(e.endCoordinates ? e.endCoordinates.height : e.end.height);
    this.setBottomOffset(this.props.bottomOffset);
    const newMessagesContainerHeight = this.getMessagesContainerHeightWithKeyboard();
    if (this.props.isAnimated === true) {
      Animated.timing(this.state.messagesContainerHeight, {
        toValue: newMessagesContainerHeight,
        duration: 210,
      }).start();
    } else {
      this.setState({
        messagesContainerHeight: newMessagesContainerHeight
      });
    }
  }

  onKeyboardWillHide() {
    this.setIsTypingDisabled(true);
    this.setKeyboardHeight(0);
    this.setBottomOffset(0);
    const newMessagesContainerHeight = this.getBasicMessagesContainerHeight();
    if (this.props.isAnimated === true) {
      Animated.timing(this.state.messagesContainerHeight, {
        toValue: newMessagesContainerHeight,
        duration: 210,
      }).start();
    } else {
      this.setState({
        messagesContainerHeight: newMessagesContainerHeight,
      });
    }
    this.setState({
      isKeyboardShow:false
    })
  }

  onKeyboardDidShow(e) {
    if (Platform.OS === 'android') {
      this.onKeyboardWillShow(e);
    }
    this.setIsTypingDisabled(false);
    this.setState({
      isKeyboardShow:true
    })
  }

  onKeyboardDidHide(e) {
    if (Platform.OS === 'android') {
      this.onKeyboardWillHide(e);
    }
    this.setIsTypingDisabled(false);
  }

  scrollToBottom(animated = true) {
    if (this._messageContainerRef === null) { return }
    this._messageContainerRef.scrollTo({
      y: 0,
      animated,
    });
  }

  renderMessages() {
    const AnimatedView = this.props.isAnimated === true ? Animated.View : View;
    return (
      <TouchableOpacity onPress={this.onMessageContainerClick} activeOpacity={1} style={{flex:1}}>
        <AnimatedView style={{
          height: this.state.messagesContainerHeight
        }}>
            <MessageContainer

              {...this.props}
              invertibleScrollViewProps={this.invertibleScrollViewProps}
              onMessageContainerClick = {this.onMessageContainerClick}
              messages={this.getMessages()}
              ref={component => this._messageContainerRef = component}
            />
        </AnimatedView>
      </TouchableOpacity>
    );
  }

  onAppendMessages(message = {}, shouldResetInputToolbar = false) {

    message.user = this.props.user
    if (shouldResetInputToolbar === true) {
      this.setIsTypingDisabled(true);
      this.resetInputToolbar();
    }

    this.props.onAppendMessages(message);
    this.scrollToBottom();

    if(this.state.isShowAccessory){
      this.onToggleAccessory();
    }

    if (shouldResetInputToolbar === true) {
      setTimeout(() => {
        if (this.getIsMounted() === true) {
          this.setIsTypingDisabled(false);
        }
      }, 100);
    }
  }

  resetInputToolbar() {
    if (this.textInput) {
      this.textInput.clear();
    }
    this.notifyInputTextReset();
    const newComposerHeight = MIN_COMPOSER_HEIGHT;
    const newMessagesContainerHeight = this.getMessagesContainerHeightWithKeyboard(newComposerHeight);
    this.setState({
      text: this.getTextFromProp(''),
      composerHeight: newComposerHeight,
      messagesContainerHeight: this.prepareMessagesContainerHeight(newMessagesContainerHeight),
    });
  }


  onInputSizeChanged(size) {
    const newComposerHeight = Math.max(MIN_COMPOSER_HEIGHT, Math.min(MAX_COMPOSER_HEIGHT, size.height));
    const newMessagesContainerHeight = this.getMessagesContainerHeightWithKeyboard(newComposerHeight);
    this.setState({
      composerHeight: newComposerHeight,
      messagesContainerHeight: this.prepareMessagesContainerHeight(newMessagesContainerHeight),
    });
  }

  onInputTextChanged(text) {
    if (this.getIsTypingDisabled()) {
      return;
    }
    if (this.props.onInputTextChanged) {
      this.props.onInputTextChanged(text);
    }
    // Only set state if it's not being overridden by a prop.
    if (this.props.text === undefined) {
      this.setState({ text });
    }
  }

  setInputText(text) {
    if (this.props.onInputTextChanged) {
      this.onInputTextChanged(text);
    }
    if (text) {
      this.setState({ text });
    }
  }

  onTextInputFocus(text) {
    // this.setState({
    //   isShowAccessory:false
    // })
  }

  onMessageContainerClick() {
    this.setState({
      isShowAccessory:false
    });
    Keyboard.dismiss();
  }

  onToggleAccessory(){
    this.setState({
      isShowAccessory:!this.state.isShowAccessory
    })
    if(this.state.isKeyboardShow){
      Keyboard.dismiss()
    }
    //Keyboard.dismiss();
  }


  onAudioRecord(type,something){
    switch (type) {
      case 'start':
        this.setState({
          audioToastColor:'white',
          isAudioToastShow:true,
          audioToastText:'上滑 取消',
          audioToastImage:audioRecording,
          audioRecordTime:something
        })

        break;
      case 'stop':
        this.setState({
          isAudioToastShow:false,
          audioToastImage:audioRecording
        })
        break;
      case 'concel':
        this.setState({
          audioToastColor:'red',
          isAudioToastShow:false,
          audioToastText:'松开 取消',
          audioToastImage:recordCancel
        })
        break;
      case 'progress':
        this.setState({
          audioRecordTime:something
        })
        break;
      default:
        this.refs.audioToast.close()
    }
  }


  notifyInputTextReset() {
    if (this.props.onInputTextChanged) {
      this.props.onInputTextChanged('');
    }
  }

  onInitialLayoutViewLayout(e) {
    const layout = e.nativeEvent.layout;
    if (layout.height <= 0) {
      return;
    }
    this.notifyInputTextReset();
    this.setMaxHeight(layout.height);
    const newComposerHeight = MIN_COMPOSER_HEIGHT;
    const newMessagesContainerHeight = this.getMessagesContainerHeightWithKeyboard(newComposerHeight);
    this.setState({
      isInitialized: true,
      text: this.getTextFromProp(''),
      composerHeight: newComposerHeight,
      messagesContainerHeight: this.prepareMessagesContainerHeight(newMessagesContainerHeight),
    });
  }

  onMainViewLayout(e) {
    // fix an issue when keyboard is dismissing during the initialization
    const layout = e.nativeEvent.layout;
    if (this.getMaxHeight() !== layout.height || this.getIsFirstLayout() === true) {
      this.setMaxHeight(layout.height);
      this.setState({
        messagesContainerHeight: this.prepareMessagesContainerHeight(this.getBasicMessagesContainerHeight()),
      });
    }
    if (this.getIsFirstLayout() === true) {
      this.setIsFirstLayout(false);
    }
  }

  renderInputToolbar() {
    const inputToolbarProps = {
      ...this.props,
      text: this.getTextFromProp(this.state.text),
      composerHeight: Math.max(MIN_COMPOSER_HEIGHT, this.state.composerHeight),
      onSend: this.props.onSend,
      onAppendMessages:this.onAppendMessages,
      onInputSizeChanged: this.onInputSizeChanged,
      onTextChanged: this.onInputTextChanged,
      setInputText:this.setInputText,
      onTextInputFocus:this.onTextInputFocus,
      isShowAccessory:this.state.isShowAccessory,
      isKeyboardInput:this.state.isKeyboardInput,
      onToggleAccessory:this.onToggleAccessory,
      onAudioRecord:this.onAudioRecord,
      textInputProps: {
        ...this.props.textInputProps,
        ref: textInput => this.textInput = textInput,
        maxLength: this.getIsTypingDisabled() ? 0 : this.props.maxInputLength
      }
    };
    if (this.getIsTypingDisabled()) {
      inputToolbarProps.textInputProps.maxLength = 0;
    }
    if (this.props.renderInputToolbar) {
      return this.props.renderInputToolbar(inputToolbarProps);
    }
    return (
      <InputToolbar
        {...inputToolbarProps}
      />
    );
  }

  renderLoading() {
    return null;
  }

  render() {
    if (this.state.isInitialized === true) {
      return (
        <ActionSheet ref={component => this._actionSheetRef = component}>
          <KeyboardAvoidingView behavior={"padding"} style={styles.container} onLayout={this.onMainViewLayout}>
            {this.renderMessages()}
            {this.renderInputToolbar()}
            <Toast
              isShow={this.state.isAudioToastShow}
              textColor={this.state.audioToastColor}
              text={this.state.audioToastText}
              image={this.state.audioToastImage}
              time={this.state.audioRecordTime}
              isShowTime={true}
              />
          </KeyboardAvoidingView>
        </ActionSheet>
      );
    }
    return (
      <View style={styles.container} onLayout={this.onInitialLayoutViewLayout}>
        {this.renderLoading()}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

GiftedChat.childContextTypes = {
  actionSheet: PropTypes.func,
  getLocale: PropTypes.func,
};

GiftedChat.defaultProps = {
  messages: [],
  text: undefined,
  placeholder: 'Type a message...',
  messageIdGenerator: () => uuid.v4(),
  user: {},
  onSend: () => {},
  locale: null,
  timeFormat: 'LT',
  dateFormat: 'll',
  isAnimated: Platform.select({
    ios: true,
    android: false,
  }),
  renderAvatar: undefined,
  showUserAvatar: false,
  onPressAvatar: null,
  renderAvatarOnTop: false,
  renderSystemMessage: null,
  onLongPress: null,
  renderMessage: null,
  renderMessageText: null,
  renderMessageImage: null,
  lightboxProps: {},
  renderCustomView: null,
  renderDay: null,
  renderTime: null,
  renderInputToolbar: null,
  renderComposer: null,
  renderSend: null,
  renderAccessory: null,
  onPressActionButton: null,
  bottomOffset: 0,
  minInputToolbarHeight: 44,
  listViewProps: {},
  keyboardShouldPersistTaps: Platform.select({
    ios: 'never',
    android: 'always',
  }),
  onInputTextChanged: null,
  setInputText:null,
  onTextInputFocus:null,
  maxInputLength: null,
  forceGetKeyboardHeight: false
};

GiftedChat.propTypes = {
  messages: PropTypes.array,
  text: PropTypes.string,
  placeholder: PropTypes.string,
  messageIdGenerator: PropTypes.func,
  user: PropTypes.object,
  onSend: PropTypes.func,
  locale: PropTypes.string,
  timeFormat: PropTypes.string,
  dateFormat: PropTypes.string,
  isAnimated: PropTypes.bool,
  renderAvatar: PropTypes.func,
  showUserAvatar: PropTypes.bool,
  onPressAvatar: PropTypes.func,
  renderAvatarOnTop: PropTypes.bool,
  renderSystemMessage: PropTypes.func,
  onLongPress: PropTypes.func,
  renderMessage: PropTypes.func,
  renderMessageText: PropTypes.func,
  renderMessageImage: PropTypes.func,
  lightboxProps: PropTypes.object,
  renderCustomView: PropTypes.func,
  renderDay: PropTypes.func,
  renderTime: PropTypes.func,
  renderInputToolbar: PropTypes.func,
  renderComposer: PropTypes.func,
  renderSend: PropTypes.func,
  renderAccessory: PropTypes.func,
  bottomOffset: PropTypes.number,
  minInputToolbarHeight: PropTypes.number,
  listViewProps: PropTypes.object,
  keyboardShouldPersistTaps: PropTypes.oneOf(['always', 'never', 'handled']),
  onInputTextChanged: PropTypes.func,
  setInputText:PropTypes.func,
  onTextInputFocus:PropTypes.func,
  maxInputLength: PropTypes.number,
  forceGetKeyboardHeight: PropTypes.bool,
};

export {
  GiftedChat,
  Avatar,
  Bubble,
  SystemMessage,
  MessageImage,
  MessageText,
  Composer,
  Day,
  InputToolbar,
  Message,
  MessageContainer,
  InputRigthButton,
  Time,
  GiftedAvatar,
  utils
};
