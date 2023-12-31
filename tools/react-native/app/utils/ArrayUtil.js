
export function removeValue(arr,val) {
  let i = arr.length;
  while(i--){
    if(val == arr[i]){
      arr.splice(i,1);
    }
  }
  return arr;
}
