
import * as imUtil from './ImUtil';
import NavigationUtil from './NavigationUtil';
import * as store from './StoreUtil';

const logout = (props) => {
    if(global.loginUser){
      imUtil.offline();
      store.remove('loginUser');
      global.loginUser = null;
      props.userActions.receiveLoginUser({})
      NavigationUtil.reset(props.navigation,'Login');
    }
};



export default {
  logout
};
