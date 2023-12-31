import React, {Component} from 'react';

import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Dimensions,
  Image,
  NativeModules,
  Alert,
  Platform
} from 'react-native';
import CropImagePicker from 'react-native-image-crop-picker';
import ImagePicker from 'react-native-image-picker';
import GridView from '../../../components/GridView';

import { MESSAGE_TYPE_IMAGE,MESSAGE_TYPE_VIDEO,MESSAGE_STATUS_FIRST,MESSAGE_TYPE_FILE} from '../../../constants/Constans';
import RNFetchBlob from 'rn-fetch-blob';
const photoIcon = require('../../../img/icon/icon_10.png');
const fileIcon = require('../../../img/icon/icon_11.png');
const cameraIcon = require('../../../img/icon/icon_12.png');
const videoIcon = require('../../../img/icon/icon_13.png');
import { AudioUtils} from 'react-native-audio';

import FileSelector from '../../../components/RNFileSelector';

class MoreAccessory extends Component {


    constructor(props:any){
      super(props);

      this.state = {
        videoIcon:videoIcon,
        moreItem:[
          {
            title:'相册',
            icon:photoIcon,
            onPress:this.selectPhoto,
          },
          {
            title:'拍摄',
            icon:cameraIcon,
            onPress:this.selectPhotoByCamera,
          },
          {
            title:'短视频 ',
            icon:videoIcon,
            onPress:this.selectVideoByCamera,
          },
        ]
      };
    }

    componentWillUnmount(){

    }

    componentDidMount() {
      if(Platform.OS == 'android'){
         this.setState({
            moreItem:[
            {
              title:'相册',
              icon:photoIcon,
              onPress:this.selectPhoto,
            },
            {
              title:'拍摄',
              icon:cameraIcon,
              onPress:this.selectPhotoByCamera,
            },
            {
              title:'短视频 ',
              icon:videoIcon,
              onPress:this.selectVideoByCamera,
            },
            {
              title:'文件 ',
              icon:fileIcon,
              onPress:this.selectFile,
            }
          ]
         })
      }
    }

    async uploadImage(media){
      const filePathSplit = media.path.split('.');
      const ext = filePathSplit[filePathSplit.length-1];
      const messageId = this.props.messageIdGenerator();
      const localPath = global.imagePath + '/' +messageId + '.' +ext;

      RNFetchBlob.fs.writeFile(localPath, media.path, 'uri')
      .then(()=>{
        const message = {
          messageId: messageId,
          type:MESSAGE_TYPE_IMAGE,
          content:JSON.stringify({
            ext:ext
          }),
          status:MESSAGE_STATUS_FIRST
        }
        this.props.onAppendMessages(message, true);
      }).catch(() => {})
    }

    async uploadFile(file){
      const filePathSplit = file.path.split('.');
      const ext = filePathSplit[filePathSplit.length-1];
      const messageId = this.props.messageIdGenerator();
      const localPath = global.filePath + '/' +messageId + '.' +ext;

      RNFetchBlob.fs.writeFile(localPath, file.path, 'uri')
      .then(()=>{
        const message = {
          messageId: messageId,
          type:MESSAGE_TYPE_FILE,
          content:JSON.stringify({
            ext:ext,
            title:file.name
          }),
          status:MESSAGE_STATUS_FIRST
        }
        this.props.onAppendMessages(message, true);
      }).catch(() => {})
    }

    async uploadVideo(media){
      const filePathSplit = media.path.split('.');
      const ext = filePathSplit[filePathSplit.length-1];
      const messageId = this.props.messageIdGenerator();
      const localPath = global.videoPath + '/' +messageId + '.' +ext;
      RNFetchBlob.fs.writeFile(localPath, media.path, 'uri')
      .then(()=>{
        const message = {
          messageId: messageId,
          type:MESSAGE_TYPE_VIDEO,
          content:JSON.stringify({
            ext:ext
          }),
          status:MESSAGE_STATUS_FIRST
        }

        this.props.onAppendMessages(message, true);
      }).catch((err) => {
      })
    }

    selectFile = () =>{
      let filter;
      if (Platform.OS === 'ios') {
        return;
        filter = [];
      } else if (Platform.OS === 'android') {
        filter = ".*\\.*";
      }
      FileSelector.Show(
        {
          closeMenu: true,
          onDone: (path) => {
            this.uploadFile(path);   
          },
          onCancel: () => {
            //console.warn('cancelled')
          }
        }
      )
    }

    selectPhoto = () =>{
      CropImagePicker.openPicker({
        mediaType:'any',
        cropping: false,
        // cropperCircleOverlay: false,
        // compressImageMaxWidth: 640,
        // compressImageMaxHeight: 480,
        // compressImageQuality: 0.5,
        includeExif: false,
        multiple:true
      }).then(images => {
        images.map(i => {
          if (i.mime && i.mime.toLowerCase().indexOf('video/') !== -1) {
            this.uploadVideo(i);
          }else{
            this.uploadImage(i);
          }
        })
      }).catch(e => {
      });
    }

    selectPhotoByCamera = () =>{
      CropImagePicker.openCamera({

      }).then(image => {
        this.uploadImage(image);
      }).catch(e => {
      });
    }

    selectVideoByCamera = () =>{
      var options = {
        mediaType:'video',
        durationLimit:10,
        videoQuality:'low',
        storageOptions: {
          skipBackup: true,
          path: 'images'
        }
      };
      ImagePicker.launchCamera(options, (response) => {
        if (response.didCancel) {
        }
        else if (response.error) {
        }
        else if (response.customButton) {
        }
        else {
          this.uploadVideo(response);
        }
      });
    }


    render() {
      // const moreItem = 
      // if(Platform.OS == 'android'){

      // }

      return (
        <View
          style={[styles.container, this.props.containerStyle, { position: this.state.position }]}>
          <View style={[styles.accessoryMorePrimary]}>
            <GridView
              items={this.state.moreItem}
              itemsPerRow={4}
              renderItem={(dataItem,index) =>(
                <TouchableOpacity key={index} activeOpacity={1} onPress={dataItem.onPress} style={styles.moreItemContainer}>
                  <Image style={styles.moreItemIcon} source={dataItem.icon} />
                  <Text key={index}  style={styles.moreItemTitle}>{dataItem.title}</Text>
                </TouchableOpacity>
              )}
            />
          </View>
        </View>
      );
    }
  }

  var styles = StyleSheet.create({
    container: {
      borderTopWidth: StyleSheet.hairlineWidth,
      borderTopColor: '#b2b2b2',
      backgroundColor: '#FFFFFF',
      bottom: 0,
      width: Dimensions.get('window').width
    },
    primary: {
      flexDirection: 'row',
      alignItems: 'flex-end',
    },
    accessory: {
      height: 44,
    },
    emojiText: {
      textAlign:'center',
      marginTop:10,
      marginBottom:10,
      fontSize:22,
      backgroundColor: 'transparent',
      color: '#000',
      fontWeight: '600',
    },
    accessoryEmojiPrimary:{
      flex: 1,
      alignItems: 'center',
      height:200,
      maxWidth:Dimensions.get('window').width
    },
    accessoryMorePrimary:{
      flex: 1,
      alignItems: 'center',
      height:'auto',
      marginTop:10,
      marginBottom:10,
    },
    moreItemContainer:{
      alignItems: 'center',
      margin:5
    },
    moreItemIcon:{
      height:55,
      width:55
    },
    moreItemTitle:{
      marginBottom:5,
      marginTop:2,
      fontSize:12,
      color: '#8a8a8a',
    }
  });

export default MoreAccessory;
