
import React from 'react';
import { StyleSheet } from 'react-native';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import Icon from 'react-native-vector-icons/Ionicons';
import Main from '../pages/MainPage/Main';

class MainContainer extends React.Component {

  static navigationOptions = ({ navigation }) => ({
    title: '首页',
    tabBarIcon: ({ tintColor }) => (
      <Icon name="md-home" size={23} color={tintColor} />
    ),
    headerRight: (
      <Icon onPress={() => {navigation.navigate('UnionSearch', {})}} style={styles.headerRightIcon} name="ios-search" size={28} color="#fff" />
    )
  });

  componentDidMount() {
   
  }

  render() {
    return <Main {...this.props} />;
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

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#efeff4'
  },
  headerRightIcon: {
    paddingRight: 20
  },
});


export default connect(mapStateToProps, mapDispatchToProps)(MainContainer);
