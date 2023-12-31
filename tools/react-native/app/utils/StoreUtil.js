
import {
  AsyncStorage,
} from 'react-native';

export function get(key){
  if (!Array.isArray(key)) {
		return AsyncStorage.getItem(key).then(function (value) {
			return JSON.parse(value);
		});
	} else {
		return AsyncStorage.multiGet(key).then(function (values) {
			return values.map(function (value) {
				return JSON.parse(value[1]);
			});
		});
	}
}

export function save(key, value){
  if (!Array.isArray(key)) {
		return AsyncStorage.setItem(key, JSON.stringify(value));
	} else {
		var pairs = key.map(function (pair) {
			return [pair[0], JSON.stringify(pair[1])];
		});
		return AsyncStorage.multiSet(pairs);
	}
}

export function remove(key){
  if(Array.isArray(key)) {
		return AsyncStorage.multiRemove(key);
	} else {
		return AsyncStorage.removeItem(key);
	}
}

export function keys(){
  return AsyncStorage.getAllKeys();
}

export function push(key,value){
  return get(key).then(function (currentValue) {
		if (currentValue === null) {
			return save(key, [value]);
		}
		if (Array.isArray(currentValue)) {
      if(Array.isArray(value)){
        return save(key, [].concat(_toConsumableArray(currentValue), value));
      }else{
        return save(key, [].concat(_toConsumableArray(currentValue), [value]));
      }
		}
		//throw new Error('Existing value for key "' + key + '" must be of type null or Array, received ' + (typeof currentValue === 'undefined' ? 'undefined' : _typeof(currentValue)) + '.');
	});
}

function _toConsumableArray(arr) {
  if (Array.isArray(arr)){
    for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }else{
    return Array.from(arr);
  }
}
