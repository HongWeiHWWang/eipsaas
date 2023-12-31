import PropTypes from 'prop-types';
import React from 'react';
import {
  Image,
  StyleSheet,
  View,
  ViewPropTypes,
  Text,
  TouchableOpacity,
  Dimensions,
  Platform
} from 'react-native';

import VideoPlayer from 'react-native-video-controls';
import RNFetchBlob from 'rn-fetch-blob';
import * as DownloadUtil from '../../utils/DownloadUtil';

export default class MessageVideo extends React.Component {

  static navigationOptions = ({ navigation }) => ({
    header:null
  });

  constructor(props){
    super(props);
    this.state = {
      source:{uri:''}
    }
  }

  componentDidMount() {
    const { params } = this.props.navigation.state;
    this.loadVideo(params.data);
  }

  loadVideo(data){
    //直接是url连接
    if(typeof data == 'string'){
      this.setState({
        source:{uri: data}
      })
    }else if(typeof data == 'object'){
      let localPath = global.videoPath + '/'+data.messageId+'.'+data.ext;
      RNFetchBlob.fs.exists(localPath)
      .then((exist) => {
          if(exist){
            if(Platform.OS == 'android'){
              localPath = 'file://'+localPath;
            }
            this.setState({
              source: {uri:localPath}
            });
          }else {
            this.downloadFromServer(data.fileId,localPath);
          }

      }).catch(() => {  })
    }
  }

  async downloadFromServer (fileId,path){
    let localPath = await DownloadUtil.downloadToPath(fileId,path);
    if(localPath){
      if(Platform.OS == 'android'){
        localPath = 'file://'+localPath;
      }
      this.setState({
        source: {uri:localPath}
      });
    }
  }
  //source={{ uri: 'https://vjs.zencdn.net/v/oceans.mp4' }}
  onBack = () =>{
    this.props.navigation.goBack();
  }

  onEnd = () =>{

  }

  render() {
    return (
      <View style={[styles.container, this.props.containerStyle]}>
        <VideoPlayer
          disableFullscreen={true}
          toggleResizeModeOnFullscreen={true}
          source={this.state.source}
          navigator={ this.props.navigator }
          onBack={this.onBack}
          onEnd={this.onEnd}
          repeat={true}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    width: Dimensions.get('window').width,
  },
});

MessageVideo.defaultProps = {

};

MessageVideo.propTypes = {

};
