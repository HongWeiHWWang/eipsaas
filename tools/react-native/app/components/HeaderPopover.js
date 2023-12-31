
import React from 'react';
import PropTypes from 'prop-types';
import {View, Text, ViewPropTypes, Image, TouchableOpacity,Dimensions,Platform,StyleSheet} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
const dimensions = Dimensions.get('window');
const windowWidth = dimensions.width;
const windowHeight = dimensions.height;

const propTypes = {
  onPress: PropTypes.func,
  disabled: PropTypes.bool,
  source: PropTypes.object,
  style: ViewPropTypes.style,
  containerStyle: ViewPropTypes.style
};

class HeaderPopover extends React.Component {

  constructor(props) {
    super(props);
    this.onPopoverContainerPress = this.onPopoverContainerPress.bind(this);
  }

  onPopoverContainerPress(){
    if(this.props.onPopoverContainerPress){
      this.props.onPopoverContainerPress();
    }
  }
  render() {
    if(this.props.isVisible){
      const content = this.props.items.map((item,index) =>{
        const itemView = (
          <TouchableOpacity key={index} activeOpacity ={1} onPress={item.onPress} style={styles.overlay}>
            {item.icon}
            <Text style={{color:'#fff'}}>{item.title}</Text>
          </TouchableOpacity>
        )
        return itemView;
      })
      return (
        <TouchableOpacity activeOpacity ={1} onPress={this.onPopoverContainerPress} style={styles.popoverContainer}>
          {content}
        </TouchableOpacity>
      );
    }else{
      return <View />
    }
  }
}

HeaderPopover.propTypes = propTypes;

HeaderPopover.defaultProps = {
  onPress() {},
  items:[],
};

export default HeaderPopover;

const styles = StyleSheet.create({
  popoverContainer: {
    position: 'absolute',
    top: 0,
    left: 0,
    width: windowWidth,
    height: windowHeight,
    backgroundColor: 'rgba(0, 0, 0, 0)',
  },
  overlay: {
    borderRadius: 3,
    flexDirection:'row',
    width: 110,
    left:Dimensions.get('window').width - 130,
    padding: 10,
    backgroundColor:'#4fabf4',
    ...Platform.select({
      ios: {
        shadowColor: 'rgba(0, 0, 0, .3)',
        shadowOffset: { width: 0, height: 1 },
        shadowRadius: 4,
      },
      android: {
        elevation: 2,
        borderWidth: 1,
        borderColor: '#ccc',
      },
    }),
  }
});
