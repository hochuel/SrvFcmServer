package com.srv;

import com.srv.fileQueu.FileQueuMain;
import com.srv.send.FcmSendProcess;
import com.srv.send.SendDataReadThread;
import com.srv.send.SendDataWriteThread;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args){


        int writeThreadCnt = 2;
        int readThreadCnt = 2;

        ExecutorService writeExecutorService = Executors.newFixedThreadPool(writeThreadCnt);
        ExecutorService readExecutorService = Executors.newFixedThreadPool(readThreadCnt);

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config/spring-setting.xml");
        //HelloWorld obj = (HelloWorld) context.getBean("helloWorld");
        //obj.getMessage();


        /*##################### Fcm App Reset ########################*/
        FcmSendProcess fcmSendProcess = (FcmSendProcess)context.getBean("fcmSendProcess");
        fcmSendProcess.appReset();


        SendDataWriteThread[] sendDataWriteThread = new SendDataWriteThread[writeThreadCnt];
        for(int i = 0; i < writeThreadCnt; i++) {
            sendDataWriteThread[i] = (SendDataWriteThread)context.getBean("writeThread");
            sendDataWriteThread[i].setName("Thread_"+i);
            writeExecutorService.execute(sendDataWriteThread[i]);
        }


        SendDataReadThread[] sendDataReadThreads = new SendDataReadThread[readThreadCnt];
        for(int i = 0; i < readThreadCnt; i++) {
            sendDataReadThreads[i] = (SendDataReadThread)context.getBean("readThread");
            sendDataReadThreads[i].setFcmSendProcess(fcmSendProcess);
            sendDataReadThreads[i].setName("Thread_"+i);
            readExecutorService.execute(sendDataReadThreads[i]);
        }


        //writeExecutorService.isShutdown();
    }



}
