package com.hotent.file.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ShedulerClean {
    @Value("${file.file.dir}")
    String fileDir;

    @Scheduled(cron = "0 0 23 * * ?")   //每晚23点执行一次
    public void clean(){
        System.out.println("执行一次清空文件夹");
        DeleteFileUtil.deleteDirectory(fileDir);
    }
}
