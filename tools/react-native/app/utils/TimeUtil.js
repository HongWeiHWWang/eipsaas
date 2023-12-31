

export const getServerTime = () => {
  //通过时间差来计算服务器的时间，网络延迟会导致误差变大，可以考虑计算网络的延迟
  const t = new Date().getTime() - global.initAppTime;
  return global.initServerTime + t;
};
