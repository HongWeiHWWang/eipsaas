/*
*计算两个经纬度点的距离
*/
export function LatLongDiDistance(longitude1,latitude1,longitude2,latitude2) {
	let Lat1 = rad(latitude1); // 纬度
	let Lat2 = rad(latitude2);
	let a = Lat1 - Lat2;//两点纬度之差
	let b = rad(longitude1) - rad(longitude2); //经度之差
	let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式
	s = s * 6371393.0;//弧长乘地球半径（半径为米）
	s = Math.round(s * 10000) / 10000;//精确距离的数值
	return s;
}

function rad(d){
	return 	d * Math.PI / 180.00;
}