import React, { Component } from 'react'
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Dimensions,
} from 'react-native'
import { MapView } from 'react-native-amap3d'
import * as DistanceUtil from '../../utils/DistanceUtil';

var Geolocation = require('Geolocation');

export default class AnimatedExample extends Component {
  
  static navigationOptions = {
    title: '动画移动',
  }

  constructor(props) {
    super(props);
    this.state = {
      targetCenter:{
        latitude: 23.09627,
        longitude: 113.36709,
      },
      currentCoordinate:{
        latitude: 39.90864,
        longitude: 116.39745,
      }
    };
  }

  componentDidMount() {
    navigator.geolocation.getCurrentPosition(val => {
      this.watchPositionCallback(val);
    }, val => {
        this.watchPositionCallback(val);
    });
  }

  watchPositionCallback = (val) =>{
    if(val.coords){
      this._animatedTo(val.coords)
    }
  }
  _animatedTo = (coordinate) => {
    this.setState({
      currentCoordinate:{
        latitude: coordinate.latitude,
        longitude: coordinate.longitude,
      }
    })
    this.mapView.animateTo({
      tilt: 0,
      rotation: 0,
      zoomLevel: 15,
      coordinate: {
        latitude: coordinate.latitude,
        longitude: coordinate.longitude,
      },
    })
    const distance = DistanceUtil.LatLongDiDistance(
      this.state.targetCenter.longitude,
      this.state.targetCenter.latitude,
      coordinate.longitude,
      coordinate.latitude)
    console.warn(distance);
  }

  _animatedToZGC = () => {
    this.setState({
      currentCoordinate:{
        latitude: 23.09627,
        longitude: 113.36709,
      }
    })
    this.mapView.animateTo({
      tilt: 0,
      rotation: 0,
      zoomLevel: 15,
      coordinate: {
        latitude: 23.09627,
        longitude: 113.36709,
      },
    })
  }

  _animatedToTAM = () => {
    this.setState({
      currentCoordinate:{
        latitude: 39.90864,
        longitude: 116.39745,
      }
    })
    this.mapView.animateTo({
      tilt: 0,
      rotation: 0,
      zoomLevel: 16,
      coordinate: {
        latitude: 39.90864,
        longitude: 116.39745,
      },
    })
    const distance = DistanceUtil.LatLongDiDistance(
      this.state.targetCenter.longitude,
      this.state.targetCenter.latitude,
      116.39745,
      39.90864)
    console.warn(distance);
  }

  onLocation = (nativeEvent) =>{
    console.warn(`${nativeEvent.latitude}, ${nativeEvent.longitude}`);
    /*this.setState({
      currentCoordinate:{
        latitude: nativeEvent.latitude ? nativeEvent.latitude:0,
        longitude: nativeEvent.longitude ? nativeEvent.longitude:0,
      }
    })*/
  }

  refreshLocation = () =>{
    Geolocation.getCurrentPosition(val => {
      this.watchPositionCallback(val);
    }, val => {
        this.watchPositionCallback(val);
    });
  }

  render() {
    return (
      <View style={styles.body}>
        <MapView 
          mapType={'standard'} 
          ref={ref => this.mapView = ref} 
          style={styles.body}
          locationEnabled={false}
          showsLocationButton={false}
          onLocation={this.onLocation}
        >
        <MapView.Marker
            title="我的位置"
            coordinate={this.state.currentCoordinate}
          />
          <MapView.Circle
            strokeWidth={1}
            strokeColor="rgba(0, 0, 255, 0.5)"
            fillColor="rgba(44,144,224, 0.5)"
            radius={100}
            coordinate={this.state.targetCenter}
          />
        </MapView>
        <View style={styles.buttons}>
          <View style={styles.button}>
            <TouchableOpacity onPress={this.refreshLocation}>
              <Text style={styles.text}>刷新位置</Text>
            </TouchableOpacity>
          </View>
          <View style={styles.button}>
            <TouchableOpacity onPress={this._animatedToTAM}>
              <Text style={styles.text}>天安门</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  body: {
    flex: 1,
  },
  buttons: {
    width: Dimensions.get('window').width,
    position: 'absolute',
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
/*import React, { Component } from 'react'
import { StyleSheet, Picker } from 'react-native'
import { MapView } from 'react-native-amap3d'

export default class MapTypesExample extends Component {
  
  static navigationOptions = ({ navigation }) => {
    const { state, setParams } = navigation
    state.params = state.params || { mapType: 'standard' }
    const props = {
      mode: 'dropdown',
      style: { width: 100 },
      selectedValue: state.params.mapType,
      onValueChange: mapType => setParams({ mapType }),
    }
    return {
      title: '地图模式',
      headerRight: (
        <Picker {...props}>
          <Picker.Item label="标准" value="standard" />
          <Picker.Item label="卫星" value="satellite" />
          <Picker.Item label="导航" value="navigation" />
          <Picker.Item label="夜间" value="night" />
          <Picker.Item label="公交" value="bus" />
        </Picker>
      ),
    }
  }

  constructor(props) {
    super(props);
    this.state = {
      showsLocationButton:true
    };
  }

  _animatedToTAM = () => {
    this.mapView.animateTo({
      tilt: 0,
      rotation: 0,
      zoomLevel: 16,
      coordinate: {
        latitude: 39.90864,
        longitude: 116.39745,
      },
    })
  }

  onLocation = (nativeEvent) =>{
    console.log('nativeEvent')
  }

  render() {
    return (
      <MapView
        ref={ref => this.mapView = ref}
        locationEnabled={this.state.showsLocationButton}
        showsLocationButton={this.state.showsLocationButton}
        mapType={this.props.navigation.state.params.mapType}
        style={StyleSheet.absoluteFill}
        onLocation={this.onLocation}
        coordinate={{
          latitude:39.706901,
          longitude:116.397972
        }}
      />
    )
  }
}*/