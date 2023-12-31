var HtReactNativeMqtt = require('react-native').NativeModules.HtReactNativeMqtt;

module.exports = {
  initClient: function (options) {
    return HtReactNativeMqtt.initClient;
  },
  disconnect: function () {
    return HtReactNativeMqtt.disconnect;
  },
  subscribe: function (topic,qos) {
    return HtReactNativeMqtt.subscribe;
  },
  getUniqueID: function () {
    return HtReactNativeMqtt.uniqueId;
  },
  getInstanceID: function() {
    return HtReactNativeMqtt.instanceId;
  },
  getDeviceId: function () {
    return HtReactNativeMqtt.deviceId;
  },
  getManufacturer: function () {
    return HtReactNativeMqtt.systemManufacturer;
  },
  getModel: function () {
    return HtReactNativeMqtt.model;
  },
  getBrand: function () {
    return HtReactNativeMqtt.brand;
  },
  getSystemName: function () {
    return HtReactNativeMqtt.systemName;
  },
  getSystemVersion: function () {
    return HtReactNativeMqtt.systemVersion;
  },
  getBundleId: function() {
    return HtReactNativeMqtt.bundleId;
  },
  getBuildNumber: function() {
    return HtReactNativeMqtt.buildNumber;
  },
  getVersion: function() {
    return HtReactNativeMqtt.appVersion;
  },
  getReadableVersion: function() {
    return HtReactNativeMqtt.appVersion + "." + HtReactNativeMqtt.buildNumber;
  },
  getDeviceName: function() {
    return HtReactNativeMqtt.deviceName;
  },
  getUserAgent: function() {
    return HtReactNativeMqtt.userAgent;
  },
  getDeviceLocale: function() {
    return HtReactNativeMqtt.deviceLocale;
  },
  getDeviceCountry: function() {
    return HtReactNativeMqtt.deviceCountry;
  },
  getTimezone: function() {
    return HtReactNativeMqtt.timezone;
  },
  isEmulator: function() {
    return HtReactNativeMqtt.isEmulator;
  },
  isTablet: function() {
    return HtReactNativeMqtt.isTablet;
  },
  isPinOrFingerprintSet: function () {
    return HtReactNativeMqtt.isPinOrFingerprintSet;
  },
};