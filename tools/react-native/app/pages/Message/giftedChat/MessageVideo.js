import PropTypes from 'prop-types';
import React from 'react';
import {
  Image,
  StyleSheet,
  View,
  ViewPropTypes,
  Platform,
  Text,
  TouchableOpacity,
  ImageBackground
} from 'react-native';
import Modal from 'antd-mobile-rn/lib/modal';
import RNFetchBlob from 'rn-fetch-blob';
import * as UploadUtil from '../../../utils/UploadUtil';
import {MESSAGE_STATUS_FIRST,MESSAGE_STATUS_UPLOADDONE,MESSAGE_STATUS_UPLOADERROR} from '../../../constants/Constans';
import * as DownloadUtil from '../../../utils/DownloadUtil';
import Icon from 'react-native-vector-icons/Ionicons';
import Video from 'react-native-video';
import pleyIcon from '../../../img/icon/icon_16.png';

import VideoPlayer from 'react-native-video-controls';



export default class MessageVideo extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      uploadProgress:0,
      complete:true,
      uploading:false
    }
  }

  componentDidMount() {
    const message =  this.props.currentMessage;
    //this.loadImage();
    if(message.messageId){
      switch (message.status) {
        case MESSAGE_STATUS_FIRST:
          this.uploadVideo(message);
          break;
        case MESSAGE_STATUS_UPLOADERROR:

          break;
        default:
      }
    }
  }

  uploadVideo = (message) =>{
    const content = JSON.parse(message.content);
    let localPath = global.videoPath + '/'+message.messageId+'.'+content.ext;
    this.setState({
      complete:false
    })
    const base64Data = RNFetchBlob.wrap(localPath);
    const options = {
      url:global.server.portal+'/system/file/v1/upload',
      files:[
        {
          name:'file',
          filename:'file.'+content.ext,
          data:base64Data
        }
      ],
      uploadProgress:(n) =>{
        this.setState({
          uploadProgress:Math.round(n * 100),
          uploading:true
        })
      }
    }
    UploadUtil.fileUpload(options).then((rtn) =>{
      const json = JSON.parse(rtn);
      const m = {
        messageId: message.messageId,
        type:message.type
      }
      if(json.success){
        m.content = JSON.stringify({
          ext:content.ext,
          text:json.fileId
        }),
        m.status = MESSAGE_STATUS_UPLOADDONE;
        this.props.onSend(m, true);
      }
      this.setState({
        complete:true,
        uploading:false
      })
    })
  }

  renderStausContent = () =>{
    return (
      this.state.complete
      ?
      <ImageBackground source={pleyIcon} style={styles.videoView}>
      </ImageBackground>
      :
      <Text style={styles.statusText}>{this.state.uploadProgress+'%'}</Text>
    )
  }

  resendMessage =()=>{
    const message =  this.props.currentMessage;
    const content = JSON.parse(message.content);
    const m = {
      messageId: message.messageId,
      type:message.type,
      status:MESSAGE_STATUS_FIRST,
      content:JSON.stringify({
        ext:content.ext
      }),
    }
    this.props.onResend(m, true);
  }

  toPlayVideo = () =>{
    const message =  this.props.currentMessage;
    const content = JSON.parse(message.content);
    const { navigate } = this.props.navigation;
    const data = {
      messageId:message.messageId,
      fileId:content.text,
      ext:content.ext
    }
    navigate('VideoPlayer', { data })
  }

  render() {
    const statusContent = this.renderStausContent();
    return (
      <TouchableOpacity activeOpacity={1} onPress={this.toPlayVideo} style={[styles.container, this.props.containerStyle]}>
        {statusContent}
      </TouchableOpacity>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#b2b2b2',
    width: 150,
    height: 100,
    borderRadius: 8,
  },
  videoView: {
    width: 50,
    height: 50,
  },
  imageActive: {
    flex: 1,
    justifyContent:'center',
    resizeMode: 'contain',
  },
  statusText:{
    color:'white'
  }
});

MessageVideo.defaultProps = {
  currentMessage: {
    image: null,
  },
  containerStyle: {},
  imageStyle: {},
};

MessageVideo.propTypes = {
  currentMessage: PropTypes.object,
  containerStyle: ViewPropTypes.style,
  imageStyle: Image.propTypes.style,
  imageProps: PropTypes.object,
  lightboxProps: PropTypes.object,
};
