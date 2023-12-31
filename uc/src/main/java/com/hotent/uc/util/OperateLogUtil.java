package com.hotent.uc.util;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.uc.manager.OperateLogManager;
import com.hotent.uc.model.OperateLog;

import io.swagger.annotations.ApiModelProperty;


public class OperateLogUtil {
	
	static WorkQueue wq = new WorkQueue(10);

	public static void doLogAsync(OperateLog operateLog){
		LogHolder logHolder = new LogHolder();
		logHolder.setOperateLog(operateLog);
		LogExecutor logExecutor = new LogExecutor();
		logExecutor.setLogHolders(logHolder);
		wq.execute(logExecutor);
	}
	
	public static <T> String compare(T ObjNew, T objOld) throws Exception {
		if (BeanUtils.isEmpty(ObjNew)) return "";
		 StringBuilder result =new StringBuilder("");
		 Field[] fs = ObjNew.getClass().getDeclaredFields();
		 for (Field f : fs) {
		  f.setAccessible(true);
		  ApiModelProperty aa=  f.getAnnotation(ApiModelProperty.class);
		  Object v1 = f.get(objOld);
		  Object v2 = f.get(ObjNew);
		  if( ! equals(v1, v2) ){
			result.append("，【"+aa.notes()+":"+f.getName()+"】由\""+v1+"\"修改为\""+v2+"\"");
		    }
		 }
		 return result.toString();
	}
	
	public static boolean equals(Object obj1, Object obj2) {
         //如果都为空的相等
		 if ((BeanUtils.isEmpty(obj1) && BeanUtils.isEmpty(obj2)) || (obj1 == obj2)) {
		   return true;
		 }
		 if (BeanUtils.isEmpty(obj1) &&  BeanUtils.isNotEmpty(obj2)) {
			 return false;
		}
		return obj1.equals(obj2);
	}
}

/**
 * 执行记录日志的任务作业 
 */
class LogExecutor implements Runnable{
	private Logger logger =  LoggerFactory.getLogger(LogExecutor.class);
	private LogHolder logHolder;
	private OperateLogManager operateLogService;


	public void setLogHolders(LogHolder logHolder) {
		this.logHolder = logHolder;
		this.operateLogService = AppUtil.getBean(OperateLogManager.class);
	}

	private void doLog() {
		OperateLog operateLog = logHolder.getOperateLog();
		operateLogService.create(operateLog);
	}
	
	@Override
	public void run() {
		try {
			doLog();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
}

/**
 * 日志信息容器
 */
class  LogHolder{
	OperateLog operateLog;
	
	public OperateLog getOperateLog() {
		return operateLog;
	}
	public void setOperateLog(OperateLog operateLog) {
		this.operateLog = operateLog;
	}
}

/**
 * 作业队列
 */
class WorkQueue{
	private final int nThreads;
	private final PoolWorker[] threads;
	LinkedList<Runnable> queue;
	
	public WorkQueue(int nThreads){
		this.nThreads=nThreads;
		queue = new LinkedList<Runnable>();
		threads = new PoolWorker[nThreads];
		for(int i=0;i<this.nThreads;i++){
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}
	 
	public void execute(Runnable r){
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}
	
	private class PoolWorker extends Thread{
		private Logger logger =  LoggerFactory.getLogger(PoolWorker.class);
		public void run(){
			Runnable r;
			while(true){
				synchronized (queue) {
					while(queue.isEmpty()){
						try {
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
					r=(Runnable)queue.removeFirst();
				}
				try{
					r.run();
				}catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
	}
}
