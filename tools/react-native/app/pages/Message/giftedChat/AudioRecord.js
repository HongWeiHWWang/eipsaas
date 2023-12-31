import React, {Component} from 'react';

import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight,
  Platform,
  PermissionsAndroid,
  PixelRatio,
  TouchableOpacity
} from 'react-native';
import Sound from 'react-native-sound';
import {AudioRecorder} from 'react-native-audio';
import RNFetchBlob from 'rn-fetch-blob';

import * as permissionUtil from '../../../utils/PermissionUtil';
import * as UploadUtil from '../../../utils/UploadUtil';
import { MESSAGE_TYPE_AUDIO,MESSAGE_AUDIO_SUFFIX,MESSAGE_STATUS_FIRST,MESSAGE_STATUS_UPLOADERROR,MESSAGE_STATUS_UPLOADDONE} from '../../../constants/Constans';


class AudioRecord extends Component {

    constructor(props:any){
      super(props);
      let timer = null;
      this.state = {
        recordTime: 0.0,
        recording: false,
        paused: false,
        stoppedRecording: false,
        finished: false,
        audioPath: '',
        hasPermission: undefined,
        recordX:30,
        recordY:360,
        concel:false
      };
    }

    prepareRecordingPath(audioPath){
      AudioRecorder.prepareRecordingAtPath(audioPath, {
        SampleRate: 22050,
        Channels: 1,
        AudioQuality: "Low",
        AudioEncoding: MESSAGE_AUDIO_SUFFIX,
        AudioEncodingBitRate: 32000
      });
    }

    componentWillUnmount(){
      if(this.state.recording){
        this.setState({
          concel:true
        })
        this.props.onAudioRecord('concel');
        this._stop();
      }
    }

    componentDidMount() {
      permissionUtil.checkRecordAudioPermission().then((hasPermission) =>{
        this.setState({ hasPermission });
      })
    }

    async _pause() {
      if (!this.state.recording) {
        return;
      }

      try {
        const filePath = await AudioRecorder.pauseRecording();
        this.setState({paused: true});
      } catch (error) {
      }
    }

    async _resume() {
      if (!this.state.paused) {
        return;
      }

      try {
        await AudioRecorder.resumeRecording();
        this.setState({paused: false});
      } catch (error) {
      }
    }

    async _stop() {
      if (!this.state.recording) {
        return;
      }

      this.setState({stoppedRecording: true, recording: false, paused: false});

      try {
        //这里可能出错，假如按住的时间刚刚好达到录音的边界值，可能会因为还没有申请到硬件，
        const filePath = await AudioRecorder.stopRecording();

        if (Platform.OS === 'android') {
          this._finishRecording(true, filePath);
        }

        if(this.state.recordTime > 1 && !this.state.concel){
          const formData = new FormData();
          const options = {
            url:global.server.portal+'/system/file/v1/upload',
            files:[
              {
                name:this.state.messageId,
                filename:this.state.messageId+'.'+MESSAGE_AUDIO_SUFFIX,
                data:RNFetchBlob.wrap(filePath)
              }
            ]
          }
          const message = {
            messageId: this.state.messageId,
            type:MESSAGE_TYPE_AUDIO,
            status:MESSAGE_STATUS_FIRST,
            content:JSON.stringify({
              size:this.state.recordTime,
              ext:MESSAGE_AUDIO_SUFFIX
            })
          }
          this.props.onAppendMessages(message, true);
          // if(global.imClient.isConnected){
          //   const rtn = await UploadUtil.fileUpload(options);
          //   console.warn(rtn)
          //   const json = JSON.parse(rtn);
          //   if(json.success){
          //     message.content.text = json.fileId
          //     message.status = MESSAGE_STATUS_UPLOADDONE;
          //   }else{
          //     message.status = MESSAGE_STATUS_UPLOADERROR;
          //   }
          // }else{
          //   message.status = MESSAGE_STATUS_UPLOADERROR;
          // }
          //this.props.onSend(message);
        }
        return filePath;
      } catch (error) {
        throw error
      }
    }

    async _record() {
      try {
        if (this.state.recording) {
          return;
        }

        if (!this.state.hasPermission) {
          return;
        }
        const messageId = this.props.messageIdGenerator();
        const path = global.audioPath + '/'+messageId+'.'+MESSAGE_AUDIO_SUFFIX
        this.setState({
          audioPath:path,
          messageId:messageId
        })
        this.prepareRecordingPath(path);

        this.setState({recording: true, paused: false});

        this.props.onAudioRecord('start',0);

        const filePath = await AudioRecorder.startRecording();

        AudioRecorder.onProgress = (data) => {
          const time = Math.floor(data.currentTime);
          this.setState({recordTime: time});
          this.props.onAudioRecord('progress',time);
        };

      } catch (error) {
        this.props.onAudioRecord('error');
      }
    }

    _finishRecording(didSucceed, filePath) {
      this.setState({ finished: didSucceed });
    }

    onResponderGrant = (event) =>{
    }

    onResponderMove = (event) =>{
      if(event.nativeEvent.locationX > this.state.recordX || event.nativeEvent.locationY > this.state.recordY){
        this.setState({
          concel:true
        })
        this.props.onAudioRecord('concel');
        this._stop();
      }
    }

    onResponderRelease = (event) =>{
      if(this.timer){
        clearTimeout(this.timer);
      }
      this.props.onAudioRecord('stop');
      this._stop();
    }

    onStartShouldSetResponder = () =>{
      this.timer = setTimeout(() => {
        this._record();
      }, 200);
      return true;
    }

    onLayout = (event) =>{
      if(event.nativeEvent.layout.width > event.nativeEvent.layout.height){
        this.setState({
          recordX:event.nativeEvent.layout.width,
          recordY:event.nativeEvent.layout.height
        })
      }else{
        this.setState({
          recordX:event.nativeEvent.layout.width,
          recordY:event.nativeEvent.layout.height
        })
      }
    }

    render() {
      return (
        <View
            style={styles.container}
            onLayout={this.onLayout}
            style={{flex: 1}}
            onStartShouldSetResponder={() => this.onStartShouldSetResponder()}
            onMoveShouldSetResponder={()=> true}
            onResponderTerminationRequest={() => true}
            onResponderGrant={(evt) => this.onResponderGrant(evt)}
            onResponderMove={(evt) => this.onResponderMove(evt)}
            onResponderRelease={(evt) => this.onResponderRelease(evt)}
          >
          <View  style={styles.recorder}>
            <Text>{this.state.recording ?'松开 结束':'按住 说话' }</Text>
          </View>
        </View>
      );
    }
  }

  var styles = StyleSheet.create({
    container: {
    },
    recorder: {
      flex:1,
      marginLeft: 5,
      marginRight: 5,
      marginTop: 5,
      marginBottom: 5,
      borderWidth: 1 / PixelRatio.get(),
      borderColor: '#6E7377',
      borderRadius: 5,
      alignItems: 'center',
      justifyContent: 'center',
    },
  });

export default AudioRecord;
