
import {
  NativeModules,Platform,PushNotificationIOS
} from 'react-native';

export function setBadge(count){
	if(Platform.OS == 'android'){
		const badge = NativeModules.Badge;
   		badge.showBadge(count);
	}else if(Platform.OS == 'ios'){
		PushNotificationIOS.setApplicationIconBadgeNumber(Number(count));
	}
}

