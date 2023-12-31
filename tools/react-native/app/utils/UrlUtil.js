
export default getUrl;
export function getUrl(url) {
   if (url.indexOf('?') === -1) {
    return url+"?token="+global.loginUser.token;
  }
  return url+"&token="+global.loginUser.token;
}
