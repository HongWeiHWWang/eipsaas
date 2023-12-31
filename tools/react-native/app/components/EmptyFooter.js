

import React from 'react';
import { ActivityIndicator, StyleSheet, Text, View } from 'react-native';

const EmptyFooter = () => (
  <View style={styles.footerContainer}>
    <Text style={styles.footerText}>没有更多数据了哦……</Text>
  </View>
);

const styles = StyleSheet.create({
  footerContainer: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 5
  },
  footerText: {
    textAlign: 'center',
    fontSize: 16,
    marginLeft: 10
  }
});

export default EmptyFooter;
