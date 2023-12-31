
import { StackActions,NavigationActions } from 'react-navigation';

const reset = (navigation, routeName,params) => {
	const resetAction = StackActions.reset({
	index: 0,
	actions: [NavigationActions.navigate({
			 	routeName:routeName,
			 	params
			})]
	});
	navigation.dispatch(resetAction);
};
const replace = (navigation, routeName,params) => {
	const replaceAction = StackActions.replace({
	actions: [NavigationActions.navigate({
			 	routeName:routeName,
			 	params
			})]
	});
	navigation.dispatch(replaceAction);
};
const push = (navigation, routeName,params) => {
	const pushAction = StackActions.push({
	  routeName: routeName,
	  params: params
	});
	navigation.dispatch(pushAction);
};
const navigate = (navigation, routeName,params) => {
  	const navigateAction = NavigationActions.navigate({
	  routeName: routeName,
	  params: params,
	  key:new Date().getTime()
	});
	navigation.dispatch(navigateAction);
};

export default {
  reset,navigate,replace,push
};
