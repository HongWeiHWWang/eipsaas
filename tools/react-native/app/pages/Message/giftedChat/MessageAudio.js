import PropTypes from 'prop-types';
import React from 'react';
import {
  Linking,
  StyleSheet,
  Text,
  View,
  ViewPropTypes,
  ImageBackground,
  Image,
  TouchableOpacity,
  Dimensions
} from 'react-native';
import RNFetchBlob from 'rn-fetch-blob';
import Sound from 'react-native-sound';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as messageCreators from '../../../actions/message';
import * as UploadUtil from '../../../utils/UploadUtil';
import  myVoise from '../../../img/my_voise.gif';
import  otherVoise from '../../../img/other_voise.gif';
import  myFixedVoise from '../../../img/my_fixed_voise.png';
import  otherFixedVoise from '../../../img/other_fixed_voise.png';
import { MESSAGE_AUDIO_SUFFIX,MESSAGE_STATUS_FIRST,MESSAGE_STATUS_UPLOADDONE,MESSAGE_STATUS_UPLOADERROR} from '../../../constants/Constans';
import * as DownloadUtil from '../../../utils/DownloadUtil';

const minWidth = 35
/*
语音消息，播放语音先会从本地获取，如果没有就到服务器取，然后保存在本地。播放语音之前会将其他正在播放的语音消息停止
*/
class MessageAudio extends React.Component {

  constructor(props) {
    super(props);
    const messageContent = JSON.parse(this.props.currentMessage.content);
    const maxWidth = Dimensions.get('window').width/2-minWidth;
    const stepWidth = maxWidth/60;
    let whoosh;
    this.state = {
      voiseImage:this.props.position == 'left' ? otherFixedVoise:myFixedVoise,
      playing:false,
      voiseWidth:stepWidth*messageContent.size+minWidth,
      closeAllMedia:null,
      complete:true
    }
  }

  componentWillReceiveProps(nextProps) {
    const {message} = nextProps;
    if(message.playingVoise && this.state.playing){
      this.stopVoise()
    }
  }

  componentDidMount() {
    const message =  this.props.currentMessage;
    if(message.messageId){
      switch (message.status) {
        case MESSAGE_STATUS_FIRST:
          this.uploadAudio(message);
          break;
        case MESSAGE_STATUS_UPLOADERROR:
          break;
        default:
      }
    }
  }

  uploadAudio = (message) =>{
    const content = JSON.parse(message.content);
    let localPath = global.audioPath + '/'+message.messageId+'.'+content.ext;
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
      ]
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
          size:content.size
        }),
        m.status = MESSAGE_STATUS_UPLOADDONE;
        this.props.onSend(m, true);
      }
      this.setState({
        complete:true,
      })
    })
  }

  onVoisePress = () =>{
    if(this.state.playing){
      this.whoosh.stop(() =>{
        this.stopVoise();
      });
      return ;
    }
    const message =  this.props.currentMessage;
    if(message.messageId){
      const localPath = global.audioPath + '/'+message.messageId+'.'+MESSAGE_AUDIO_SUFFIX;
      RNFetchBlob.fs.exists(localPath)
      .then((exist) => {
          if(exist){
            this.playVoise(localPath)
          }else {
            this.downloadVoiseMessage()
          }
      }).catch((error) => {  })
    }
  }

  async downloadVoiseMessage (){
    const message =  this.props.currentMessage;
    const fileId = JSON.parse(message.content).text;
    let localPath = global.audioPath + '/'+message.messageId+'.'+MESSAGE_AUDIO_SUFFIX;
    await DownloadUtil.downloadToPath(fileId,localPath)
    this.playVoise(localPath);
  }

  stopVoise = () =>{
    this.setState({
      voiseImage:this.props.position == 'left' ? otherFixedVoise:myFixedVoise,
      playing:false
    })
    this.whoosh.release();
  }

  playVoise = (path) =>{
    const {messageActions} = this.props;
    messageActions.onPlayVoise(true);
    this.setState({
      voiseImage:this.props.position == 'left' ? otherVoise:myVoise,
      playing:true
    })
    setTimeout(() => {
      this.whoosh = new Sound(path, '', (error) => {
        if (error) {
        }
      });
      setTimeout(() => {
        this.whoosh.play((success) => {
          this.stopVoise();
          messageActions.onPlayVoise(false);
          if (success) {
          } else {
          }
        });
      }, 100);
    }, 100);
  }


  render() {
    const c = this.props.currentMessage.content;
    const textJson = c.text?c:JSON.parse(c);
    return (
      <View style={[styles[this.props.position].container, this.props.containerStyle[this.props.position]]}>
        <TouchableOpacity onPress={this.onVoisePress} activeOpacity={1} style={[styles[this.props.position].text,{width:this.state.voiseWidth}]}>
          {this.props.position == 'right' ? <Text style={[styles[this.props.position].time]}>{textJson.size+'"'}</Text>:null}
          <Image source={this.state.voiseImage} style={{width:25,height:20}} />
          {this.props.position == 'left' ? <Text style={[styles[this.props.position].time]}>{textJson.size+'"'}</Text>:null}
        </TouchableOpacity>
      </View>
    );
  }
}

const mapStateToProps = (state) => {
  const { message } = state;
  return {
    message
  };
};

const mapDispatchToProps = (dispatch) => {
  const messageActions = bindActionCreators(messageCreators, dispatch);
  return {
    messageActions
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(MessageAudio);

const textStyle = {
  minHeight:25,
  minWidth:minWidth,
  marginTop: 7,
  marginBottom: 7,
  marginLeft: 10,
  marginRight: 10,
  alignItems:'center',
  flexDirection: 'row',
};

const styles = {
  left: StyleSheet.create({
    container: {
    },
    time:{
      textAlign:'right',
      fontSize:12,
      flex:1,
    },
    text: {
      justifyContent: 'flex-start',
      ...textStyle,
    },
  }),
  right: StyleSheet.create({
    container: {
    },
    time:{
      color:'white',
      textAlign:'left',
      fontSize:12,
      flex:1,
    },
    text: {
      justifyContent: 'flex-end',
      ...textStyle,
    },
  }),
};

MessageAudio.contextTypes = {
  actionSheet: PropTypes.func,
};

MessageAudio.defaultProps = {
  position: 'left',
  currentMessage: {
    text: '',
  },
  containerStyle: {},
  textStyle: {},
  closeAllMedia: null
};

MessageAudio.propTypes = {
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
  textProps: PropTypes.object,
};
