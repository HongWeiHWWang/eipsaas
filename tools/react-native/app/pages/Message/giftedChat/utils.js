import moment from 'moment';

const DEPRECATION_MESSAGE = 'isSameUser and isSameDay should be imported from the utils module instead of using the props functions';

export function isSameDay(currentMessage = {}, diffMessage = {}) {

  if (!diffMessage.sendTime) {
    return false
  }

  let currentCreatedAt = moment(currentMessage.sendTime);
  let diffCreatedAt = moment(diffMessage.sendTime);

  if (!currentCreatedAt.isValid() || !diffCreatedAt.isValid()) {
    return false;
  }

  return currentCreatedAt.isSame(diffCreatedAt, 'day');

}

export function isSameUser(currentMessage = {}, diffMessage = {}) {

  return !!(diffMessage.user && currentMessage.user && diffMessage.user._id === currentMessage.user._id);

}

export function warnDeprecated(fn) {

  return (...args) => {
    return fn(...args);
  };

}
