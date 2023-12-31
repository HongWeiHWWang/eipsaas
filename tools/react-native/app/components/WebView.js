

import React from 'react';
import ReactNative from 'react-native';

import {StyleSheet, TextInput, View,Dimensions,requireNativeComponent,UIManager,Platform,WebView } from 'react-native';

var RCTWebViewBridge = requireNativeComponent('RCTWebViewBridge', HtWebView);
var RCT_WEBVIEWBRIDGE_REF = 'webviewbridge';

class HtWebView extends React.Component {
  constructor(props) {
    super(props);
  }

  onMessage = (event) =>{ 
    if (this.props.onMessage != null && event.nativeEvent != null) {
      this.props.onMessage(event.nativeEvent.message)
    }
  }
  onLoadingFinish = (event) =>{
    var {onLoad, onLoadEnd} = this.props;
    onLoad && onLoad(event);
    this.updateNavigationState(event);
  }

  onLoadingStart = (event) =>{
    var onLoadStart = this.props.onLoadStart;
    onLoadStart && onLoadStart(event);
    this.updateNavigationState(event);
  }

  onLoadingError = (event) => {
     event.persist(); // persist this event because we need to store it
     var {onError, onLoadEnd} = this.props;
     onError && onError(event);
     onLoadEnd && onLoadEnd(event);
   }


  updateNavigationState = (event) => {
    if (this.props.onNavigationStateChange) {
      this.props.onNavigationStateChange(event.nativeEvent);
    }
  }

  sendToBridge = (message) =>{
    UIManager.dispatchViewManagerCommand(
      this.getWebViewBridgeHandle(),
      UIManager.RCTWebViewBridge.Commands.sendToBridge,
      [message]
    );
  }

  goBack = () => {
    UIManager.dispatchViewManagerCommand(
      this.getWebViewBridgeHandle(),
      UIManager.RCTWebViewBridge.Commands.goBack,
      null
    );
  }

  getWebViewBridgeHandle = () =>{
    return ReactNative.findNodeHandle(this.refs[RCT_WEBVIEWBRIDGE_REF]);
  }

  render() {
    return (
      <View style={styles.container}>
        <RCTWebViewBridge
          ref={RCT_WEBVIEWBRIDGE_REF}
          {...this.props}
          onChange={this.onMessage}
          onLoadingFinish={this.onLoadingFinish}
          onLoadingStart={this.onLoadingStart}
          onLoadingError={this.onLoadingError}
        />
      </View>
    );
  }
}


const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  input: {

  }
});

export default HtWebView;
