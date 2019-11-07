package com.srv.fileQueu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilqueuTestApp {
/*
    public static void main(String[] args){

        int writeThreadCnt = 10;
        int readThreadCnt = 2;

        ExecutorService writeExecutorService = Executors.newFixedThreadPool(writeThreadCnt);
        ExecutorService readExecutorService = Executors.newFixedThreadPool(readThreadCnt);

        FileQueuMain fileQueuMain = new FileQueuMain();

        FileWriteThread[] fwt = new FileWriteThread[writeThreadCnt];
        for(int i = 0; i < writeThreadCnt; i++) {
            fwt[i] = new FileWriteThread(fileQueuMain);
            fwt[i].setName("Thread_"+i);
            writeExecutorService.execute(fwt[i]);
        }



        FileReadThread [] frt = new FileReadThread[readThreadCnt];
        for(int i = 0; i < readThreadCnt; i++) {
            frt[i] = new FileReadThread(fileQueuMain);
            frt[i].setName("RThread :" + i);
            readExecutorService.execute(frt[i]);
        }

    }
*/

/*

    public static void main(String[] args) throws Exception{
        FileQueuMain fileQueuMain = FileQueuMain.getInstance();

        fileQueuMain.fileQueu.fileWrite("This is test");


        int cnt = fileQueuMain.fileQueu.getFileCnt();
        System.out.println(fileQueuMain.fileQueu.getFileCnt());

        for(int i = 0; i < cnt; i++) {
            System.out.println(i + ":" + fileQueuMain.fileQueu.getData());
            fileQueuMain.fileQueu.fileDelete();
        }

        System.out.println(fileQueuMain.fileQueu.getFileCnt());

    }
*/
}
