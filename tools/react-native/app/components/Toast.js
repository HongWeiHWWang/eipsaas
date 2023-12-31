
import React, {Component} from 'react';
import {
    StyleSheet,
    View,
    Animated,
    Dimensions,
    Text,
    ViewPropTypes as RNViewPropTypes,
    Image
} from 'react-native'

import PropTypes from 'prop-types';
const ViewPropTypes = RNViewPropTypes || View.propTypes;

const windowHeight = Dimensions.get('window').height;

export default class Toast extends Component {

    constructor(props) {
      super(props);
      this.state = {
          isShow: false,
          text: '',
          opacityValue: new Animated.Value(this.props.opacity),
      }
    }

    componentWillReceiveProps(nextProps) {
      const {isShow} = nextProps;
      if(isShow && !this.state.isShow){
        this.show();
      }else if(!isShow && this.state.isShow){
        this.close();
      }
    }

    show() {
        this.duration = this.props.duration;
        this.setState({
            isShow: true,
        });

        Animated.timing(
            this.state.opacityValue,
            {
                toValue: this.props.opacity,
                duration: this.props.duration,
            }
        ).start(() => {
            this.isShow = true;
            if(this.props.duration !== 0) this.close();
        });
    }

    close( duration ) {

        if (!this.isShow && !this.state.isShow) return;
        this.timer && clearTimeout(this.timer);
        this.timer = setTimeout(() => {
          Animated.timing(
            this.state.opacityValue,
            {
                toValue: 0.0,
                duration: this.props.fadeOutDuration,
            }
          ).start(() => {
            this.setState({
              isShow: false,
            });
            this.isShow = false;
            if(this.props.callback) {
              this.props.callback();
            }
          });
        }, this.props.closeDelay);
    }

    componentWillUnmount() {
      this.timer && clearTimeout(this.timer);
    }

    render() {
        let pos;
        switch (this.props.position) {
          case 'top':
              pos = this.props.positionValue;
              break;
          case 'center':
              pos = windowHeight / 2.5;
              break;
          case 'bottom':
              pos = windowHeight - this.props.positionValue;
              break;
        }
        {
          return this.state.isShow ?
            <View
                style={[styles.container, { bottom: pos }]}
                pointerEvents="none"
            >
              <Animated.View style={[styles.content, { opacity: this.props.opacity }]}>
                <View style={{alignItems:'center'}}>
                  {this.props.isShowTime?<Text style={styles.timeText}>{this.props.time}</Text>:null}
                  <Image style={{width:120,height:120}} source={this.props.image} />
                  <Text style={[styles.text,{color:this.props.textColor}]}>{this.props.text}</Text>
                </View>
              </Animated.View>
          </View> : null;
        }
    }
}

const styles = StyleSheet.create({
    container: {
      position: 'absolute',
      left: 0,
      right: 0,
      elevation: 999,
      alignItems: 'center',
      zIndex: 10000,
    },
    content: {
      backgroundColor: 'black',
      borderRadius: 5,
      padding: 10,
    },
    text: {
      color: 'white',
      fontSize:20,
      marginTop:10
    },
    timeText:{
      color: 'white'
    }
});

Toast.propTypes = {
    style: ViewPropTypes.style,
    position: PropTypes.oneOf([
      'top',
      'center',
      'bottom',
    ]),
    textStyle: Text.propTypes.style,
    positionValue:PropTypes.number,
    fadeInDuration:PropTypes.number,
    fadeOutDuration:PropTypes.number,
    opacity:PropTypes.number
}

Toast.defaultProps = {
    position: 'center',
    textStyle: styles.text,
    positionValue: 120,
    fadeInDuration: 500,
    fadeOutDuration: 500,
    opacity: 0.8,
    closeDelay:250,
    textColor:'white',
    duration:100000,
    isShowTime:false
}
