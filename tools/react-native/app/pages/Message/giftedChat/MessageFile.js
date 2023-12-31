import PropTypes from 'prop-types';
import React from 'react';
import {
  Image,
  StyleSheet,
  View,
  ViewPropTypes,
  Platform,
  Text,
  TouchableOpacity
} from 'react-native';
import Modal from 'antd-mobile-rn/lib/modal';
import Lightbox from 'react-native-lightbox';
import defaultImage from '../../../img/defaul_white_image.png';
import RNFetchBlob from 'rn-fetch-blob';
import * as UploadUtil from '../../../utils/UploadUtil';
import {MESSAGE_STATUS_FIRST,MESSAGE_STATUS_UPLOADDONE,MESSAGE_STATUS_UPLOADERROR} from '../../../constants/Constans';
import * as DownloadUtil from '../../../utils/DownloadUtil';
import Icon from 'react-native-vector-icons/Ionicons';

export default class MessageFile extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      content:{},
      uploadProgress:0,
      complete:true,
      uploading:false
    }
  }

  componentDidMount() {
    const message =  this.props.currentMessage;
    this.loadImage();
    if(message.messageId){
      switch (message.status) {
        case MESSAGE_STATUS_FIRST:
          this.uploadImage(message);
          break;
        case MESSAGE_STATUS_UPLOADERROR:
          break;
        default:
      }
    }
  }

  loadImage(){
    const message =  this.props.currentMessage;
    const content = JSON.parse(message.content);
    let localPath = global.imagePath + '/'+message.messageId+'.'+content.ext;
    RNFetchBlob.fs.exists(localPath)
    .then((exist) => {
        if(exist){
          if(Platform.OS == 'android'){
            localPath = 'file://'+localPath;
          }this.setState({
            content: content
          });
        }else {
          this.downloadFromServer(message);
        }

    }).catch(() => {  })
  }

  async downloadFromServer (message){
    const content = JSON.parse(message.content);
    let localPath = global.imagePath + '/'+message.messageId+'.'+content.ext;
    await DownloadUtil.downloadToPath(content.text,localPath);
    if(localPath){
      if(Platform.OS == 'android'){
        localPath = 'file://'+localPath;
      }this.setState({
        content: content
      });
    }
  }

  uploadImage = (message) =>{
    const content = JSON.parse(message.content);
    let localPath = global.filePath + '/'+message.messageId+'.'+content.ext;
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
          text:json.fileId,
          title:content.title
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
      null
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

  onPressFile = () =>{
    DownloadUtil.linkTodownload(this.state.content.text)
  }

  render() {
    const statusContent = this.renderStausContent();
    return (
      <View style={[styles[this.props.position].container, this.props.containerStyle[this.props.position]]}>
        <Text
          style={[styles[this.props.position].text]} onPress={this.onPressFile}
        >
          <Icon name="md-document" />文件:{this.state.content.title}
        </Text>
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

MessageFile.defaultProps = {
  currentMessage: {
    image: null,
  },
  containerStyle: {},
  imageStyle: {},
};

MessageFile.propTypes = {
  currentMessage: PropTypes.object,
  containerStyle: ViewPropTypes.style,
  imageStyle: Image.propTypes.style,
  imageProps: PropTypes.object,
  lightboxProps: PropTypes.object,
};
