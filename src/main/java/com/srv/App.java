package com.srv;

import com.srv.fileQueu.FileQueuMain;
import com.srv.result.ResultDataThread;
import com.srv.send.FcmSendProcess;
import com.srv.send.SendDataReadThread;
import com.srv.send.SendDataWriteThread;
import com.srv.util.TestDataInsert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args){

        Logger logger = LogManager.getLogger("App Main");

        int writeThreadCnt = 6;
        int readThreadCnt = 16;
        int resultThreadCnt = 16;

        int monitorThreadCnt = 1;

        ExecutorService writeExecutorService = Executors.newFixedThreadPool(writeThreadCnt);
        ExecutorService readExecutorService = Executors.newFixedThreadPool(readThreadCnt);

        ExecutorService resultExecutorService = Executors.newFixedThreadPool(resultThreadCnt);

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config/spring-setting.xml");


        /*##################### Fcm App Reset ########################*/

        FcmSendProcess fcmSendProcess = (FcmSendProcess)context.getBean("fcmSendProcess");
        fcmSendProcess.appReset();

        String prifix = System.getProperty("ServerPrifix");

        SendDataWriteThread[] sendDataWriteThread = new SendDataWriteThread[writeThreadCnt];
        for(int i = 0; i < writeThreadCnt; i++) {
            sendDataWriteThread[i] = (SendDataWriteThread)context.getBean("writeThread");
            sendDataWriteThread[i].setName(prifix+"T"+i);
            writeExecutorService.execute(sendDataWriteThread[i]);
        }


        SendDataReadThread[] sendDataReadThreads = new SendDataReadThread[readThreadCnt];
        for(int i = 0; i < readThreadCnt; i++) {
            sendDataReadThreads[i] = (SendDataReadThread)context.getBean("readThread");
            sendDataReadThreads[i].setFcmSendProcess(fcmSendProcess);
            sendDataReadThreads[i].setName(prifix+"T"+i);
            readExecutorService.execute(sendDataReadThreads[i]);
        }

        ResultDataThread[] resultDataThreads = new ResultDataThread[resultThreadCnt];
        for(int i = 0; i < resultThreadCnt; i++){
            resultDataThreads[i] = (ResultDataThread)context.getBean("resultThread");
            resultExecutorService.execute(resultDataThreads[i]);
        }

        ExecutorService monitorService = Executors.newFixedThreadPool(monitorThreadCnt);
        MonitorApp monitorApp = (MonitorApp)context.getBean("monitorApp");
        monitorService.execute(monitorApp);

        logger.info(prifix + " SrvFcmServer Start ........");


/*
        TestDataInsert testDataInsert = (TestDataInsert) context.getBean("testDataInsert");
        testDataInsert.insertData();
*/

    }
}
