/**
*考勤打卡页面
**/
import React, { Component } from 'react'
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Dimensions,
} from 'react-native'
import { connect } from 'react-redux';
import { MapView } from 'react-native-amap3d'
import * as DistanceUtil from '../../utils/DistanceUtil';

import { Geolocation } from "react-native-amap-geolocation"

class ClockOn extends Component {
  
  static navigationOptions = {
    title: '考勤打卡',
  }

  constructor(props) {
    super(props);
    this.state = {
      targetCenter:{
        latitude: 23.09627,
        longitude: 113.36709,
      },
      currentCoordinate:{},
      positionTimestamp:0,
      distance:0
    };
  }

  async componentDidMount() {
    await Geolocation.init({
      ios: "9bd6c82e77583020a73ef1af59d0c759",
      android: "8f77bb8521358a1c62054cc257e43e71"
    })
    Geolocation.setOptions({
      interval: 10000,
      distanceFilter: 10,
      reGeocode: false
    })
    Geolocation.addLocationListener(location =>{
      const distance = DistanceUtil.LatLongDiDistance(
            this.state.targetCenter.longitude,
            this.state.targetCenter.latitude,
            location.longitude,
            location.latitude)
      this.setState({
        currentCoordinate:location,
        positionTimestamp:location.timestamp,
        distance:distance
      })
    })
  }

  startAmapLocation = () => Geolocation.start()

  stopAmapLocation = () => Geolocation.stop()

  watchPositionCallback = (val) =>{
    if(val.coords){
		const distance = DistanceUtil.LatLongDiDistance(
						this.state.targetCenter.longitude,
						this.state.targetCenter.latitude,
						val.coords.longitude,
						val.coords.latitude)
		this.setState({
	      currentCoordinate:val.coords,
	      positionTimestamp:val.timestamp,
	      distance:distance
	    })
    }
  }

  refreshPosition = () =>{
    navigator.geolocation.getCurrentPosition(val => {
      this.watchPositionCallback(val);
    }, val => {
        this.watchPositionCallback(val);
    });
  }

  refreshWatchPosition = () =>{
    navigator.geolocation.watchPosition(val => {
      this.watchPositionCallback(val);
    }, val => {
        this.watchPositionCallback(val);
    });
  }

  refreshAccuratePosition = () =>{
    navigator.geolocation.getCurrentPosition(val => {
      this.watchPositionCallback(val);
    }, val => {
        this.watchPositionCallback(val);
    },{enableHighAccuracy:true});
  }

  render() {
    return (
      <View style={styles.body}>
        <View style={styles.buttons}>
          <View style={styles.button}>
            <TouchableOpacity onPress={this.refreshPosition}>
              <Text style={styles.text}>位置</Text>
            </TouchableOpacity>
          </View>
          <View style={styles.button}>
            <TouchableOpacity onPress={this.refreshWatchPosition}>
              <Text style={styles.text}>监听位置</Text>
            </TouchableOpacity>
          </View>
          <View style={styles.button}>
            <TouchableOpacity onPress={this.refreshAccuratePosition}>
              <Text style={styles.text}>精确位置</Text>
            </TouchableOpacity>
          </View>
        </View>
        <View style={styles.buttons}>
          <View style={styles.button}>
            <TouchableOpacity onPress={this.startAmapLocation}>
              <Text style={styles.text}>高德开始</Text>
            </TouchableOpacity>
          </View>
          <View style={styles.button}>
            <TouchableOpacity onPress={this.stopAmapLocation}>
              <Text style={styles.text}>高德停止</Text>
            </TouchableOpacity>
          </View>
        </View>
        <View style={styles.detail}>
          	<Text>速度：{this.state.currentCoordinate.speed}</Text>
          	<Text>经度：{this.state.currentCoordinate.longitude}</Text>
          	<Text>纬度：{this.state.currentCoordinate.latitude}</Text>
          	<Text>准确度：{this.state.currentCoordinate.accuracy}</Text>
          	<Text>前进方向：{this.state.currentCoordinate.heading}</Text>
          	<Text>海拔：{this.state.currentCoordinate.altitude}</Text>
          	<Text>海拔准确度：{this.state.currentCoordinate.altitudeAccuracy}</Text>
          	<Text>时间：{this.state.positionTimestamp}</Text>
          	<Text>距离：{this.state.distance}</Text>
        </View>
      </View>
    )
  }
}

const mapStateToProps = (state) => {
  return {
  };
};
const mapDispatchToProps = (dispatch) => {
  return {
  };
};
export default connect(mapStateToProps, mapDispatchToProps)(ClockOn);

const styles = StyleSheet.create({
  body: {
    flex: 1,
  },
  detail:{
	width: Dimensions.get('window').width,
    justifyContent: 'center',
  },
  buttons: {
    width: Dimensions.get('window').width,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  buttons: {
    width: Dimensions.get('window').width,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  button: {
    padding: 10,
    paddingLeft: 20,
    paddingRight: 20,
    margin: 10,
    borderRadius: 50,
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
  },
  text: {
    fontSize: 16,
  },
})
